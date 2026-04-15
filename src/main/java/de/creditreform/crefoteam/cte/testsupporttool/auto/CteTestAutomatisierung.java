package de.creditreform.crefoteam.cte.testsupporttool.auto;

import de.creditreform.crefoteam.cte.statemachine.ProcessDefinition;
import de.creditreform.crefoteam.cte.statemachine.ProcessEngine;
import de.creditreform.crefoteam.cte.statemachine.ProcessOutcome;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.ConsoleProcessListener;
import de.creditreform.crefoteam.cte.testsupporttool.ProcessRunner;
import de.creditreform.crefoteam.cte.testsupporttool.env.TestEnvironmentManager;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;
import de.creditreform.crefoteam.cte.testsupporttool.process.CteAutomatedTestProcess;
import de.creditreform.crefoteam.cte.testsupporttool.util.CustomerUtils;
import de.creditreform.crefoteam.cte.testsupporttool.util.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public final class CteTestAutomatisierung implements TesunClientJobListener {

    private static final Logger LOGGER = Logger.getLogger(CteTestAutomatisierung.class);

    public static final String APP_LOG_FILE = TestSupportClientKonstanten.TEST_TYPES.PHASE1_AND_PHASE2 + ".log";
    public static final String TIMELINE_LOG_FILE = "TimeLine.log";

    private final EnvironmentConfig environmentConfig;
    private Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> testCustomerMapMap;
    /** Länge des über alle Phasen aggregierten TestResults-Dumps aus dem letzten Lauf. */
    private int lastResultsBodyLength;
    /** Pfad der im letzten Lauf erzeugten ZIP-Datei (oder {@code null} bei Fehler). */
    private String lastZipFilePath;

    public CteTestAutomatisierung(EnvironmentConfig environmentConfig) {
        this.environmentConfig = environmentConfig;
        initForEnvironment();
    }

    private void initForEnvironment() {
        try {
            if (!TimelineLogger.configure(environmentConfig.getLogOutputsRoot(), APP_LOG_FILE, TIMELINE_LOG_FILE)) {
                notifyClientJob(Level.ERROR, "Exception beim Konfigurieren der LOG-Dateien!\n");
            }
            notifyClientJob(Level.INFO, String.format("\nInitialisiere für die Umgebung %s...", environmentConfig.getCurrentEnvName()));
            notifyClientJob(Level.INFO, String.format("\nInitialisiere Test-Resourcen für die Umgebung %s...", environmentConfig.getCurrentEnvName()));

            String testSetSource = environmentConfig.getLastTestSource();
            if (testSetSource == null || testSetSource.isEmpty()) {
                testSetSource = TestSupportClientKonstanten.DEFAUL_TESTS_SOURCE;
            }
            File sourceDir = new File(environmentConfig.getTestResourcesRoot(), testSetSource);
            if (!sourceDir.exists()) {
                throw new IllegalStateException("Die selektierte Quelle " + sourceDir + " existiert nicht! Bitte andere Quelle wählen.");
            }
            environmentConfig.setTestResourcesDir(sourceDir);
            environmentConfig.setLastTestSource(testSetSource);

           initTestCasesForCustomers();

        } catch (Throwable ex) {
            notifyClientJob(Level.ERROR, "Exception beim Laden der Konfiguration!\n" + ex.getMessage());
        }
    }

    /**
     * Pendant zu {@code CustomerInitializer.initTestCasesForCustomers} ohne
     * GUI-Callbacks. Lädt die Kunden-Konfiguration, loggt pro Phase und
     * schreibt pro Phase einen {@code Dump-INIT-<PHASE>.txt}.
     */
    private void initTestCasesForCustomers() throws Exception {
        notifyClientJob(Level.INFO, "\n\tLese die Test-Crefos-Konfiguration aus dem ITSQ-Verzeichnis...");
        this.testCustomerMapMap = environmentConfig.getCustomerTestInfoMapMap();
        notifyClientJob(Level.INFO, "\n\tErmittle TesunConfigInfo für die Kunden...");
/* CLAUDE_MODE
        TesunRestService tesunRestServiceWLS = getTestSupportHelper().getTesunRestServiceWLS();
        SystemInfo systemInfo = tesunRestServiceWLS.getSystemPropertiesInfo();
*/
        notifyClientJob(Level.INFO, "\n\tErmittle KundenKonfigList für die Kunden...");
        for (TestSupportClientKonstanten.TEST_PHASE testPhase : testCustomerMapMap.keySet()) {
            Map<String, TestCustomer> testCustomerMap = testCustomerMapMap.get(testPhase);
            notifyClientJob(Level.INFO, "\n" + testCustomerMap.size() + " Kunden sind für den Test in " + testPhase + " ausgewählt.");
            testCustomerMap.entrySet().forEach(testCustomerEntry -> {
                try {
                    TestCustomer testCustomer = testCustomerEntry.getValue();
                    notifyClientJob(Level.INFO, "\n\t\tInitialisiere Testfälle des Kunden für " + testCustomer.getCustomerName() + " aus " + testPhase);
/* CLAUDE_MODE
                    tesunRestServiceWLS.extendTestCustomerProperiesInfos(testCustomer, systemInfo);
*/
                } catch (Exception ex) {
                    notifyClientJob(Level.ERROR, ex.toString());
                }
            });
            CustomerUtils.dumpCustomers(environmentConfig.getLogOutputsRoot(), "INIT-" + testPhase.name(), testCustomerMap);
        }
    }

     public ProcessOutcome startProcess(boolean isDemoMode) throws PropertiesException {
        try (TimelineLogger.Action overall = TimelineLogger.action("CteAutomatedTestProcess", environmentConfig.getCurrentEnvName())) {
            ProcessDefinition definition = CteAutomatedTestProcess.build(environmentConfig, this);
            Map<String, Object> taskVariablesMap = buildTaskVariablesMap(
                    isDemoMode,
                    testCustomerMapMap,
                    TestSupportClientKonstanten.TEST_TYPES.PHASE1_AND_PHASE2,
                    TestSupportClientKonstanten.TEST_PHASE.PHASE_1,
                    true,
                    false);
            ProcessRunner runner = new ProcessRunner(new ProcessEngine(), new ConsoleProcessListener());
            ProcessOutcome outcome = runner.run(definition, taskVariablesMap);
            overall.result(outcome.name());
            TimelineLogger.info(getClass(), "Endzustand: {}", outcome);
            finalizeResults();
            return outcome;
        }
    }

     private void finalizeResults() {
        StringBuilder stringBuilderAll = new StringBuilder();
        testCustomerMapMap.values().forEach(customersMap -> stringBuilderAll.append(CustomerUtils.dumpAllCustomersResults(customersMap)));
        lastResultsBodyLength = stringBuilderAll.length();
        try {
            File outputsRoot = environmentConfig.getTestOutputsRoot();
            FileUtils.writeStringToFile(new File(outputsRoot, "TestResults.txt"), stringBuilderAll.toString());
            notifyClientJob(Level.INFO, "\nTest-Results sind im Output-Ordner gespeichert");
        } catch (Exception ex) {
            notifyClientJob(Level.ERROR, "Fehler beim Speichern der TestResults-Datei!\n" + ex.getMessage());
        }
        try {
            lastZipFilePath = FileSystemUtils.zipOutputDirectory(
                    environmentConfig.getTestOutputsRoot(),
                    environmentConfig.getItsqRefExportsRoot(),
                    environmentConfig.getCurrentEnvName(),
                    msg -> notifyClientJob(Level.INFO, msg),
                    msg -> notifyClientJob(Level.WARN, msg),
                    msg -> notifyClientJob(Level.ERROR, msg));
        } catch (Exception ex) {
            notifyClientJob(Level.ERROR, "Fehler beim Zippen der Outputs!\n" + ex.getMessage());
            lastZipFilePath = null;
        }
    }

    /**
     * Exit-Code analog {@code ActivitiTestAutomatisierung}: {@code -1}, wenn
     * der aggregierte TestResults-Dump nicht leer ist (Kunden mit Ergebnissen),
     * sonst {@code 0}. Zusätzlich {@code 1}, wenn die State-Machine nicht
     * erfolgreich durchgelaufen ist.
     */
    public int computeExitCode(ProcessOutcome outcome) {
        if (outcome != ProcessOutcome.COMPLETED) {
            return 1;
        }
        return lastResultsBodyLength > 0 ? -1 : 0;
    }

    public int getLastResultsBodyLength() { return lastResultsBodyLength; }

    public String getLastZipFilePath() { return lastZipFilePath; }

    public Map<String, Object> buildTaskVariablesMap(
            boolean isDemoMode,
            Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> customers,
            TestSupportClientKonstanten.TEST_TYPES testType,
            TestSupportClientKonstanten.TEST_PHASE testPhase,
            boolean useOnlyTestClz,
            boolean uploadSynthetics) throws PropertiesException {

        Map<String, Object> vars = new HashMap<>();
        vars.put(UT_TASK_PARAM_NAME_DEMO_MODE, isDemoMode);
        vars.put(UT_TASK_PARAM_NAME_MEIN_KEY, environmentConfig.getActivitProcessKey());
        vars.put(UT_TASK_PARAM_NAME_ACTIVITI_PROCESS_NAME, environmentConfig.getActivitiProcessName());
        vars.put(UT_TASK_PARAM_NAME_TIME_BEFORE_BTLG_IMPORT, environmentConfig.getMillisBeforeBtlgImport(isDemoMode));
        vars.put(UT_TASK_PARAM_NAME_TIME_BEFORE_CT_IMPORT, environmentConfig.getMillisBeforeCtImport(isDemoMode));
        vars.put(UT_TASK_PARAM_NAME_TIME_BEFORE_EXPORT, environmentConfig.getMillisBeforeExports(isDemoMode));
        vars.put(UT_TASK_PARAM_NAME_TIME_BEFORE_EXPORTS_COLLECT, environmentConfig.getMillisBeforeCollectExports(isDemoMode));
        vars.put(UT_TASK_PARAM_NAME_TIME_BEFORE_SFTP_COLLECT, environmentConfig.getMillisBeforeCollectSftpUploads(isDemoMode));
        vars.put(UT_TASK_PARAM_NAME_EMAIL_FROM, environmentConfig.getActivitiEmailFrom());
        vars.put(UT_TASK_PARAM_NAME_SUCCESS_EMAIL_TO, environmentConfig.getActivitiSuccessEmailTo());
        vars.put(UT_TASK_PARAM_NAME_FAILURE_EMAIL_TO, environmentConfig.getActivitiFailureEmailTo());
        vars.put(UT_TASK_PARAM_NAME_ACTIVE_CUSTOMERS, customers);
        vars.put(UT_TASK_PARAM_NAME_TEST_TYPE, testType);
        vars.put(UT_TASK_PARAM_NAME_TEST_PHASE, testPhase);
        vars.put(UT_TASK_PARAM_USE_ONLY_TEST_CLZ, useOnlyTestClz);
        vars.put(UT_TASK_PARAM_UPLOAD_SYNTH_TEST_CREFOS, uploadSynthetics);
        return vars;
    }

    /** Schließt Lock + Logger. Vor JVM-Exit aufrufen. */
    public void shutdown() {
        TestEnvironmentManager.reset();
    }

    public EnvironmentConfig getEnvironmentConfig() { return environmentConfig; }

    public Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> getTestCustomerMapMap() {
        return testCustomerMapMap;
    }

    // ===================================================================
    // TesunClientJobListener — analog ActivitiTestAutomatisierung
    // ===================================================================

    @Override
    public void notifyClientJob(Level level, Object notifyObject) {
        if (notifyObject == null) {
            return;
        }
        String text = notifyObject.toString();
        if (text.startsWith(".")) {
            return;
        }
        TimelineLogger.info(getClass(), text.replaceAll("\t", " "));
    }

    @Override
    public Object askClientJob(ASK_FOR askFor, Object userObject) {
        try {
            switch (askFor) {
                case ASK_OBJECT_RETRY:
                    notifyClientJob(Level.INFO, userObject + "\n\tVersuche NICHT erneut, ABBRUCH!");
                    return Boolean.FALSE;
                case ASK_OBJECT_CTE_VERSION:
                    return environmentConfig.getCteVersion() == null ? 0 : Integer.valueOf(environmentConfig.getCteVersion());
                case ASK_REF_EXPORTS_PATH:
                case ASK_TEST_CASES_PATH:
                    return environmentConfig.getItsqRefExportsRoot().getAbsolutePath();
                case ASK_WAIT_FOR_TEST_SYSTEM:
                case ASK_OBJECT_COPY_EXPORTS_TO_INPUTS:
                case ASK_OBJECT_CREATE_NEW_SOLL:
                case ASK_OBJECT_ANAYLSE_CHECKS:
                case ASK_OBJECT_CHECK_DOWNLOADS:
                case ASK_CHECK_COLLECTS:
                    return Boolean.TRUE;
                case ASK_OBJECT_EXCEPTION:
                    return userObject;
                default:
                    throw new PropertiesException("Unbekannte Rückfrage: " + askFor + "!");
            }
        } catch (Exception ex) {
            LOGGER.error("Fehler in askClientJob: " + askFor, ex);
            return null;
        }
    }
}
