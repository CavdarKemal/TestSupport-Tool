package de.creditreform.crefoteam.cte.testsupporttool.handlers.base;

import de.creditreform.crefoteam.cte.jvmclient.JobStartResponse;
import de.creditreform.crefoteam.cte.jvmclient.JvmInstallation;
import de.creditreform.crefoteam.cte.jvmclient.JvmRestClient;
import de.creditreform.crefoteam.cte.jvmclient.JvmRestClientImpl;
import de.creditreform.crefoteam.cte.statemachine.ProcessContext;
import de.creditreform.crefoteam.cte.statemachine.Step;
import de.creditreform.crefoteam.cte.statemachine.StepResult;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.rest.TesunRestService;
import de.creditreform.crefoteam.cte.tesun.rest.dto.TesunJobexecutionInfo;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.JobInfo;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.tesun.util.TesunDateUtils;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;
import org.apache.log4j.Level;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Port der gleichnamigen Basisklasse aus
 * {@code testsupport_client.tesun_activiti.handlers}. Stellt die Gemein-
 * funktionalität für alle UserTask-Handler bereit (Retry, Cancel,
 * JVM-Job-Starten, Listener-Benachrichtigung) und implementiert gleichzeitig
 * das State-Machine-{@link Step}-Interface.
 */
public abstract class AbstractUserTaskRunnable implements UserTaskRunnable, Step {

    private static final int MAX_RETRIES_TO_START_FLUX_JOB = 3;

    protected final EnvironmentConfig environmentConfig;
    protected final TesunClientJobListener tesunClientJobListener;
    protected boolean canceled = false;
    protected final AtomicBoolean abortFlag = new AtomicBoolean();

    protected AbstractUserTaskRunnable(EnvironmentConfig environmentConfig, TesunClientJobListener tesunClientJobListener) {
        this.environmentConfig = environmentConfig;
        this.tesunClientJobListener = tesunClientJobListener;
    }

    // ======================================================================
    // Step-Integration: runTask → ProcessContext
    // ======================================================================

    @Override
    public StepResult execute(ProcessContext context) throws Exception {
        Map<String, Object> updated = runTask(context.variables());
        if (updated != null && updated != context.variables()) {
            context.variables().putAll(updated);
        }
        if (canceled || context.isCancelled()) {
            return StepResult.ABORT;
        }
        return StepResult.NEXT;
    }

    // ======================================================================
    // UserTaskRunnable-API
    // ======================================================================

    @Override
    public Map<String, Object> runTask(Map<String, Object> taskVariablesMap) throws Exception {
        return taskVariablesMap;
    }

    @Override
    public void cancel() {
        canceled = true;
        abortFlag.set(true);
    }

    // ======================================================================
    // Listener-Helfer
    // ======================================================================

    protected String buildNotifyStringForClassName(TestSupportClientKonstanten.TEST_PHASE testPhase) {
        String strName = this.getClass().getSimpleName();
        return "\n@" + strName + (testPhase != null ? (" für " + testPhase.name()) : "");
    }

    protected void notifyUserTask(Level level, Object notifyObject) {
        if (tesunClientJobListener != null) {
            tesunClientJobListener.notifyClientJob(level, notifyObject);
        } else {
            TimelineLogger.info(getClass(), "[{}] {}", level, notifyObject);
        }
    }

    protected Object askUserTask(TesunClientJobListener.ASK_FOR askFor, Object userObject) {
        if (tesunClientJobListener != null) {
            return tesunClientJobListener.askClientJob(askFor, userObject);
        }
        return null;
    }

    protected boolean askForRetryUserTask(String simpleName, Exception ex, String... additionalInfos) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bei der Ausführung des Tasks '").append(simpleName).append("' ist ein Problem aufgetreten:\n\n");
        for (String info : additionalInfos) {
            sb.append(info).append("\n");
        }
        sb.append(ex.getMessage() != null ? ex.getMessage() : "");
        if (ex.getCause() != null) sb.append(ex.getCause().getMessage());
        sb.append("\n\nDie Aktion wiederholen oder den Prozess abbrechen?");
        try {
            Boolean retry = (Boolean) askUserTask(TesunClientJobListener.ASK_FOR.ASK_OBJECT_RETRY, sb.toString());
            return retry != null && retry;
        } catch (Exception inner) {
            TimelineLogger.error(getClass(), "askForRetry fehlgeschlagen", inner);
            return false;
        }
    }

    protected Boolean checkDemoMode(Boolean demoMode) {
        if (Boolean.TRUE.equals(demoMode)) {
            notifyUserTask(Level.INFO, "\nDemo-Mode: UserTask-Start wird simuliert!");
            return true;
        }
        return false;
    }

    // ======================================================================
    // REST-Service-Zugriff
    // ======================================================================

    protected TesunRestService getTesunRestService() throws PropertiesException {
        return new TesunRestService(environmentConfig.getRestServiceConfigsForMasterkonsole().get(0), tesunClientJobListener);
    }

    public Map<String, JvmInstallation> getJvmInstallationsMap() throws Exception {
        TesunRestService rest = new TesunRestService(
                environmentConfig.getRestServiceConfigsForBatchGUI().get(0), tesunClientJobListener);
        Map<String, JvmInstallation> installations = new HashMap<>();
        for (Map.Entry<String, String> entry : rest.getJvmInstallationMap().entrySet()) {
            JvmInstallation jvm = new JvmInstallation();
            jvm.setJvmName(entry.getKey());
            jvm.setJvmUrl(entry.getValue());
            installations.put(jvm.getJvmName(), jvm);
        }
        return installations;
    }

    // ======================================================================
    // JVM-Job-Starten (mit Retry + Double-Check auf der REST-Seite)
    // ======================================================================

    protected void doStartJvmJob(JobInfo jobInfo,
                                 Map<String, JvmInstallation> jvmInstallationsMap,
                                 TestSupportClientKonstanten.TEST_PHASE testPhase) throws Exception {
        notifyUserTask(Level.INFO, String.format("\nVersuche JVM-Job '%s' für Test-Phase '%s' zu starten...", jobInfo.getJobName(), testPhase));
        String jvmName = jobInfo.getJvmName();
        JvmInstallation installation = jvmInstallationsMap.get(String.format("%s (%s)", jvmName, environmentConfig.getCurrentEnvName().toUpperCase()));
        if (installation == null) {
            throw new RuntimeException(String.format("Die JVM '%s' existiert auf der Ziel-Umgebung nicht!", jvmName));
        }
        int numRetries = 0;
        TesunRestService rest = getTesunRestService();
        TesunJobexecutionInfo baseline = null;
        if (!jobInfo.getProcessNamesList().isEmpty()) {
            baseline = rest.getTesunJobExecutionInfo(jobInfo.getProcessNamesList().get(0));
        }
        while (true) {
            JvmRestClient client = new JvmRestClientImpl(installation.getJvmUrl(), abortFlag);
            try {
                JobStartResponse response = client.startJob(jobInfo.getJobName(), jobInfo.getQueryParameters());
                if (response.getJobId() != null) {
                    notifyUserTask(Level.INFO, String.format("\n\tJVM-Job '%s' gestartet. JobId=%s", jobInfo.getJobName(), response.getJobId()));
                    return;
                }
                throw new RuntimeException("\tJVM-Job " + jobInfo.getJobName() + " konnte nicht gestartet werden!");
            } catch (Exception ex) {
                if (isConnectionProblem(ex) || ++numRetries > MAX_RETRIES_TO_START_FLUX_JOB) {
                    throw ex;
                }
                if (baseline != null && checkJobStarted(rest, baseline, jobInfo)) {
                    notifyUserTask(Level.INFO, String.format("\nJVM-Job '%s' wurde DOCH gestartet.", jobInfo.getJobName()));
                    return;
                }
                if (!askForRetryUserTask(getClass().getSimpleName(), ex)) {
                    throw ex;
                }
                Thread.sleep(500);
            }
        }
    }

    private boolean checkJobStarted(TesunRestService rest, TesunJobexecutionInfo baseline, JobInfo jobInfo) throws Exception {
        TesunJobexecutionInfo current = rest.getTesunJobExecutionInfo(jobInfo.getProcessNamesList().get(0));
        if (current.getLastStartDate() == null) return false;
        return TesunDateUtils.isSameOrAfter(current.getLastStartDate(), baseline.getLastCompletitionDate());
    }

    private boolean isConnectionProblem(Throwable ex) {
        if (ex == null) return false;
        String msg = ex.getMessage();
        if (msg != null) {
            String lower = msg.toLowerCase();
            if (lower.contains("refused") || lower.contains("connection") || lower.contains("timeout") || lower.contains("timed out")) {
                return true;
            }
        }
        return isConnectionProblem(ex.getCause());
    }

    // ======================================================================
    // Last-Completion-Date (für Wait-Handler)
    // ======================================================================

    protected Calendar getLastCompletitionDate(Map<String, TestCustomer> customersMap) throws Exception {
        List<String> processIdentifiers = new ArrayList<>();
        for (TestCustomer c : customersMap.values()) {
            processIdentifiers.add(c.getProcessIdentifier());
        }
        processIdentifiers.add("BETEILIGUNGEN_IMPORT");
        processIdentifiers.add("ENTSCHEIDUNGSTRAEGER_BERECHNUNG");
        processIdentifiers.add("BTLG_UPDATE_TRIGGER");
        processIdentifiers.add("FROM_STAGING_INTO_CTE");
        return getLastCompletitionDateFromList(processIdentifiers);
    }

    protected Calendar getLastCompletitionDate(JobInfo jobInfo) throws Exception {
        return getLastCompletitionDateFromList(jobInfo.getProcessNamesList());
    }

    protected Calendar getLastCompletitionDateFromList(List<String> processIdentifiers) throws Exception {
        Calendar latest = Calendar.getInstance();
        latest.set(Calendar.YEAR, 1900);
        for (String pid : processIdentifiers) {
            Calendar cal = getLastCompletitionDate(pid);
            if (TesunDateUtils.isSameOrAfter(cal, latest)) {
                latest = cal;
            }
        }
        return latest;
    }

    protected Calendar getLastCompletitionDate(String processIdentifier) throws Exception {
        TesunRestService rest = getTesunRestService();
        TesunJobexecutionInfo info = rest.getTesunJobExecutionInfo(processIdentifier);
        String status = info.getJobStatus();
        if (!"COMPLETED".equals(status)) {
            throw new RuntimeException(String.format(
                    "getLastCompletitionDate():: Der Status des Jobs '%s' ist '%s'; sollte aber 'COMPLETED' sein!",
                    processIdentifier, status));
        }
        Calendar completion = info.getLastCompletitionDate();
        if (completion != null) completion.set(Calendar.SECOND, 0);
        return completion;
    }

    // ======================================================================
    // Diverse Kleinigkeiten
    // ======================================================================

    protected Calendar extractCalendarFromMap(Map<String, Object> taskVariablesMap, String entryName) {
        Object raw = taskVariablesMap.get(entryName);
        if (raw == null) {
            throw new RuntimeException("Die Taskvariable für Zeitangabe fehlt!");
        }
        Calendar cal = TesunDateUtils.toCalendar(raw.toString());
        if (cal == null) {
            throw new RuntimeException("Die Taskvariable für Zeitangabe hat ein ungültiges Format!");
        }
        cal.set(Calendar.SECOND, 0);
        return cal;
    }

    /**
     * Wait-Entscheidung aus dem ehemaligen Activiti-Controller: wird der Task
     * per GUI manuell gestartet ({@code UT_TASK_PARAM_NAME_MANUEL_USER_TASK=true}),
     * wird nicht gewartet — stattdessen wird der angegebene Completion-Cal aus
     * der Variablen-Map an alle aktiven Kunden als {@code lastJobStartetAt}
     * gesetzt (für nachfolgende Export-Protokoll-Checks).
     * Sonst schläft der Handler {@code waitForTime} ms.
     */
    @SuppressWarnings("unchecked")
    protected void checkForWait(Map<String, Object> taskVariablesMap, String waitForTime) {
        Object obj = taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_MANUEL_USER_TASK);
        if (obj != null && ((Boolean) obj).booleanValue()) {
            Calendar jobGestartetCal = extractCalendarFromMap(taskVariablesMap, TestSupportClientKonstanten.LAST_COMPLETITION_TIME);
            Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> selectedCustomersMapMap =
                    (Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>>)
                            taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_ACTIVE_CUSTOMERS);
            TestSupportClientKonstanten.TEST_PHASE testPhase =
                    (TestSupportClientKonstanten.TEST_PHASE) taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE);
            Map<String, TestCustomer> selectedCustomersMapPhaseX = selectedCustomersMapMap.get(testPhase);
            selectedCustomersMapPhaseX.entrySet().forEach(e -> e.getValue().setLastJobStartetAt(jobGestartetCal));
        } else {
            long timeBeforeExport = (Long) taskVariablesMap.get(waitForTime);
            waitMillisForUserTask(timeBeforeExport);
        }
    }

    protected void waitMillisForUserTask(long timeMillis) {
        notifyUserTask(Level.INFO, "\n\tWarte " + timeMillis / 1000 + "s (ab " + new Date() + ") ...");
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() < (start + timeMillis)) {
            if (canceled) return;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
