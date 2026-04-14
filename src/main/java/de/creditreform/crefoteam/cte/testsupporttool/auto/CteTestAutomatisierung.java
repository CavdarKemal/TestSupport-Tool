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
import de.creditreform.crefoteam.cte.testsupporttool.env.EnvironmentLockManager;
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

/**
 * Headless-Variante des TestSupport-Tools — Pendant zu
 * {@code testsupport_client.TestSupportGUI...ActivitiTestAutomatisierung},
 * gleiche Funktionalität minus Activiti.
 *
 * <p>Implementiert {@link TesunClientJobListener} selbst, sodass der Runner
 * gleichzeitig der Listener für Handler-Notifications ist (genau wie das
 * Original).
 *
 * <p><b>Demo-Mode-Verhalten:</b> {@code isDemoMode} wirkt ausschließlich
 * über die Task-Variable {@link TesunClientJobListener#UT_TASK_PARAM_NAME_DEMO_MODE},
 * die einzelne Handler via {@code checkDemoMode(...)} auswerten. Der Runner
 * selbst hat keinen demoMode-Branch.
 *
 * <p>Lifecycle (analog Original {@code initForEnvironment}):
 * <ol>
 *   <li>{@link EnvironmentLockManager#registerShutdownHook}</li>
 *   <li>{@link TestEnvironmentManager#switchEnvironment} — Lock + Logger</li>
 *   <li>Logger-Konfig auf {@code <PHASE1_AND_PHASE2>.log} + {@code TimeLine.log}</li>
 *   <li>{@link EnvironmentConfig#setTestResourcesDir} auf {@code <root>/<DEFAUL_TESTS_SOURCE>}</li>
 *   <li>{@link EnvironmentConfig#getCustomerTestInfoMapMap}</li>
 * </ol>
 */
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

    /** Pendant zu {@code ActivitiTestAutomatisierung#initForEnvironment}. */
    private void initForEnvironment() {
        try {
            notifyClientJob(Level.INFO, String.format(
                    "\nInitialisiere Test-Resourcen für die Umgebung %s...",
                    environmentConfig.getCurrentEnvName()));

            EnvironmentLockManager.registerShutdownHook();
            if (!TestEnvironmentManager.switchEnvironment(environmentConfig)) {
                throw new IllegalStateException("Umgebungs-Lock für '"
                        + environmentConfig.getCurrentEnvName() + "' konnte nicht erworben werden.");
            }

            // Logger-Dateien analog zum Original benennen
            File logsDir = environmentConfig.getLogOutputsRootForEnv(environmentConfig.getCurrentEnvName());
            if (!TimelineLogger.configure(logsDir, APP_LOG_FILE, TIMELINE_LOG_FILE)) {
                notifyClientJob(Level.ERROR, "Exception beim Konfigurieren der LOG-Dateien!\n");
            }

            notifyClientJob(Level.INFO, String.format(
                    "\nInitialisiere für die Umgebung %s...",
                    environmentConfig.getCurrentEnvName()));

            File rootDir = environmentConfig.getTestResourcesRoot() != null
                    ? environmentConfig.getTestResourcesRoot()
                    : new File(System.getProperty("user.dir"), "X-TESTS");
            File sourceDir = new File(rootDir, TestSupportClientKonstanten.DEFAUL_TESTS_SOURCE);
            if (!sourceDir.exists()) {
                sourceDir.mkdirs();
            }
            environmentConfig.setTestResourcesDir(sourceDir);

            this.testCustomerMapMap = loadCustomerTestInfoMapMapTolerant();
            initTestCasesForCustomers(testCustomerMapMap);
        } catch (Throwable ex) {
            notifyClientJob(Level.ERROR, "Exception beim Laden der Konfiguration!\n" + ex.getMessage());
        }
    }

    private Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> loadCustomerTestInfoMapMapTolerant() {
        try {
            return environmentConfig.getCustomerTestInfoMapMap();
        } catch (Exception ex) {
            notifyClientJob(Level.WARN, "Keine X-TESTS/ITSQ-Daten gefunden — leere Kundenmap.\n  " + ex.getMessage());
            Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> empty = new TreeMap<>();
            for (TestSupportClientKonstanten.TEST_PHASE p : TestSupportClientKonstanten.TEST_PHASE.values()) {
                empty.put(p, new TreeMap<>());
            }
            return empty;
        }
    }

    /** Pendant zu {@code ActivitiTestAutomatisierung#initTestCasesForCustomers}. */
    private void initTestCasesForCustomers(Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> mapMap) {
        notifyClientJob(Level.INFO, "\n\tErmittle Kunden-Konfiguration für die Test-Phasen...");
        for (TestSupportClientKonstanten.TEST_PHASE phase : mapMap.keySet()) {
            Map<String, TestCustomer> customers = mapMap.get(phase);
            notifyClientJob(Level.INFO, "\n" + customers.size() + " Kunden für " + phase + " ausgewählt.");
            for (TestCustomer testCustomer : customers.values()) {
                notifyClientJob(Level.INFO, "\n\t\tInitialisiere Testfälle für " + testCustomer.getCustomerName() + " in " + phase);
            }
        }
    }

    /**
     * Startet den State-Machine-Prozess. {@code isDemoMode} wird in die
     * Task-Variablen geschrieben und ausschließlich von den Handlern
     * ausgewertet — der Runner verzweigt selbst nicht.
     *
     * <p>Pendant zu {@code ActivitiTestAutomatisierung#startActivitiProcess},
     * das im Original {@code setTaskVariablesMap(false)} hart kodiert.
     */
    public ProcessOutcome startProcess(boolean isDemoMode) throws PropertiesException {
        try (TimelineLogger.Action overall =
                     TimelineLogger.action("CteAutomatedTestProcess", environmentConfig.getCurrentEnvName())) {
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

    /**
     * Pendant zum zweiten Teil von {@code ActivitiTestAutomatisierung#endACTIVITIProcess}:
     * aggregiert Kunden-Dumps über alle Phasen, schreibt {@code TestResults.txt} in
     * {@code testOutputsRoot} und zippt {@code testOutputsRoot} + {@code itsqRefExportsRoot}.
     * Der E-Mail-Versand ist bewusst nicht enthalten — den erledigen die
     * UserTaskFailureMail/UserTaskSuccessMail-Handler.
     */
    private void finalizeResults() {
        StringBuilder stringBuilderAll = new StringBuilder();
        testCustomerMapMap.values().forEach(customersMap ->
                stringBuilderAll.append(CustomerUtils.dumpAllCustomersResults(customersMap)));
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

    /**
     * Pendant zu {@code ActivitiTestSupport#buildTaskVariablesMap} — alle
     * 16 Task-Variablen, die das Original setzt. ACTIVITI_PROCESS_NAME
     * bleibt erhalten (auch wenn Activiti nicht mehr läuft), damit Handler,
     * die diesen Wert lesen, kompatibel bleiben.
     */
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
                    return environmentConfig.getCteVersion() == null ? 0
                            : Integer.valueOf(environmentConfig.getCteVersion());
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
