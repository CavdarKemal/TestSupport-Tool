package de.creditreform.crefoteam.cte.testsupporttool.auto;

import de.creditreform.crefoteam.cte.statemachine.ProcessDefinition;
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
import org.apache.log4j.Level;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Headless-Variante des TestSupport-Tools — Pendant zu
 * {@code testsupport_client.TestSupportGUI...ActivitiTestAutomatisierung}.
 *
 * <p>Übernimmt den vollen Bootstrap: Lock erwerben, {@link TimelineLogger}
 * konfigurieren, {@code testResourcesDir} setzen, Kunden-Konfiguration
 * laden, {@link CteAutomatedTestProcess} starten, am Ende sauber aufräumen.
 *
 * <p>Aufrufpfad:
 * <pre>{@code
 *   EnvironmentConfig env = EnvironmentConfig.forDemo("...");
 *   CteTestAutomatisierung runner = new CteTestAutomatisierung(env);
 *   ProcessOutcome outcome = runner.startProcess(true);
 * }</pre>
 *
 * <p>Lifecycle-Reihenfolge (analog {@code ActivitiTestAutomatisierung#initForEnvironment}):
 * <ol>
 *   <li>{@link EnvironmentLockManager#registerShutdownHook}</li>
 *   <li>{@link TestEnvironmentManager#switchEnvironment} — Lock + Logger</li>
 *   <li>{@link EnvironmentConfig#setTestResourcesDir} auf {@code <root>/<DEFAUL_TESTS_SOURCE>}</li>
 *   <li>{@link EnvironmentConfig#getCustomerTestInfoMapMap} — tolerant gegen fehlende Daten</li>
 * </ol>
 */
public final class CteTestAutomatisierung {

    public static final String APP_LOG_FILE = TestSupportClientKonstanten.TEST_TYPES.PHASE1_AND_PHASE2 + ".log";
    public static final String TIMELINE_LOG_FILE = "TimeLine.log";

    private final EnvironmentConfig environmentConfig;
    private final TesunClientJobListener tesunClientJobListener;
    private Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> testCustomerMapMap;

    public CteTestAutomatisierung(EnvironmentConfig environmentConfig) {
        this(environmentConfig, null);
    }

    public CteTestAutomatisierung(EnvironmentConfig environmentConfig, TesunClientJobListener listener) {
        this.environmentConfig = environmentConfig;
        this.tesunClientJobListener = listener;
        initForEnvironment();
    }

    /**
     * Initialisiert die Umgebung — entspricht
     * {@code ActivitiTestAutomatisierung#initForEnvironment}.
     */
    private void initForEnvironment() {
        try {
            notifyClientJob(Level.INFO, String.format(
                    "\nInitialisiere Test-Resourcen für die Umgebung %s...",
                    environmentConfig.getCurrentEnvName()));

            EnvironmentLockManager.registerShutdownHook();
            if (!TestEnvironmentManager.switchEnvironment(environmentConfig)) {
                notifyClientJob(Level.ERROR, "Konnte Umgebung "
                        + environmentConfig.getCurrentEnvName() + " nicht aktivieren!");
                throw new IllegalStateException("Umgebungs-Lock konnte nicht erworben werden.");
            }

            // Logs zusätzlich an die Original-Dateinamen koppeln
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

    /**
     * Lädt das Kunden-Map-Map. Liefert leere Maps zurück, wenn die
     * X-TESTS/ITSQ-Daten fehlen — Demo-Mode bleibt damit lauffähig.
     */
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
                notifyClientJob(Level.INFO, "\n\t\tInitialisiere Testfälle des Kunden "
                        + testCustomer.getCustomerName() + " in " + phase);
            }
        }
    }

    /**
     * Startet den State-Machine-Prozess für den gewählten Modus.
     *
     * @param isDemoMode {@code true} = REST-Aufrufe werden simuliert
     */
    public ProcessOutcome startProcess(boolean isDemoMode) throws PropertiesException {
        try (TimelineLogger.Action overall =
                     TimelineLogger.action("CteAutomatedTestProcess", environmentConfig.getCurrentEnvName())) {
            ProcessDefinition definition = CteAutomatedTestProcess.build(environmentConfig, tesunClientJobListener);
            Map<String, Object> taskVariablesMap = buildTaskVariablesMap(isDemoMode);
            ProcessRunner runner = new ProcessRunner(
                    new de.creditreform.crefoteam.cte.statemachine.ProcessEngine(),
                    new ConsoleProcessListener());
            ProcessOutcome outcome = runner.run(definition, taskVariablesMap);
            overall.result(outcome.name());
            TimelineLogger.info(getClass(), "Endzustand: {}", outcome);
            return outcome;
        }
    }

    /** Pendant zu {@code ActivitiTestSupport#buildTaskVariablesMap}. */
    public Map<String, Object> buildTaskVariablesMap(boolean isDemoMode) {
        return buildTaskVariablesMap(isDemoMode,
                CteAutomatedTestProcess.TEST_TYPE_PHASE1_AND_PHASE2,
                TestSupportClientKonstanten.TEST_PHASE.PHASE_1);
    }

    public Map<String, Object> buildTaskVariablesMap(boolean isDemoMode,
                                                     String testType,
                                                     TestSupportClientKonstanten.TEST_PHASE testPhase) {
        Map<String, Object> vars = new HashMap<>();
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE, testPhase);
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_TYPE, testType);
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE, isDemoMode);
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_ACTIVE_CUSTOMERS, testCustomerMapMap);
        vars.put(TesunClientJobListener.UT_TASK_PARAM_USE_ONLY_TEST_CLZ, Boolean.FALSE);
        return vars;
    }

    /** Schließt Lock + Logger. Vor dem JVM-Exit aufrufen. */
    public void shutdown() {
        TestEnvironmentManager.reset();
    }

    public EnvironmentConfig getEnvironmentConfig() { return environmentConfig; }

    public Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> getTestCustomerMapMap() {
        return testCustomerMapMap;
    }

    private void notifyClientJob(Level level, String message) {
        if (tesunClientJobListener != null) {
            tesunClientJobListener.notifyClientJob(level, message);
        } else {
            TimelineLogger.info(getClass(), "[{}] {}", level, message);
        }
    }
}
