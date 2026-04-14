package de.creditreform.crefoteam.cte.testsupporttool.process;

import de.creditreform.crefoteam.cte.statemachine.ProcessDefinition;
import de.creditreform.crefoteam.cte.statemachine.ProcessOutcome;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.ProcessRunner;
import de.creditreform.crefoteam.cte.testsupporttool.env.EnvironmentLockManager;
import de.creditreform.crefoteam.cte.testsupporttool.env.TestEnvironmentManager;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-End-Test des kompletten {@link CteAutomatedTestProcess} im
 * Demo-Mode. Die Initialisierung in {@link #initForEnvironment(Path)} spiegelt
 * {@code ActivitiTestAutomatisierung#initForEnvironment} aus dem Original-
 * Projekt: Lock erwerben, {@link TimelineLogger} konfigurieren,
 * {@code testResourcesDir} setzen und {@code customerTestInfoMapMap} laden.
 */
class CteAutomatedTestProcessTest {

    private EnvironmentConfig env;
    private Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> customerTestInfoMapMap;
    private String originalUserDir;

    @BeforeEach
    void initForEnvironment(@TempDir Path tmp) throws Exception {
        // user.dir auf TempDir umlenken, damit getLogOutputsRoot/getTestOutputsRoot
        // unter dem Test-Verzeichnis landen und nicht im Projekt-Root.
        originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tmp.toAbsolutePath().toString());
        new File(tmp.toFile(), "X-TESTS").mkdirs();

        env = EnvironmentConfig.forDemo("http://unused-in-demo");

        EnvironmentLockManager.registerShutdownHook();
        boolean switched = TestEnvironmentManager.switchEnvironment(env);
        assertThat(switched)
                .as("TestEnvironmentManager.switchEnvironment muss den Lock erwerben und den Logger konfigurieren")
                .isTrue();

        // testResourcesDir = <root>/<DEFAUL_TESTS_SOURCE>  (z. B. X-TESTS/ITSQ)
        File rootDir = env.getTestResourcesRoot() != null
                ? env.getTestResourcesRoot()
                : new File(tmp.toFile(), "X-TESTS");
        File sourceDir = new File(rootDir, TestSupportClientKonstanten.DEFAUL_TESTS_SOURCE);
        sourceDir.mkdirs();
        env.setTestResourcesDir(sourceDir);

        // Kunden-Konfiguration laden — im Spike-Setup ohne reale X-TESTS-Daten
        // bleiben die Maps leer, das ist für Demo-Mode-Tests ausreichend.
        customerTestInfoMapMap = loadCustomerTestInfoMapMapTolerant(env);

        TimelineLogger.info(getClass(),
                "Test-Initialisierung für Umgebung '{}' abgeschlossen.", env.getCurrentEnvName());
    }

    @AfterEach
    void cleanup() {
        TestEnvironmentManager.reset();
        if (originalUserDir != null) {
            System.setProperty("user.dir", originalUserDir);
        }
    }

    @Test
    void demoMode_phase1AndPhase2_completes() throws PropertiesException {
        ProcessDefinition definition = CteAutomatedTestProcess.build(env, null);
        Map<String, Object> vars = buildTaskVariablesMap(
                CteAutomatedTestProcess.TEST_TYPE_PHASE1_AND_PHASE2,
                TestSupportClientKonstanten.TEST_PHASE.PHASE_2,
                Boolean.TRUE);

        ProcessOutcome outcome = new ProcessRunner().run(definition, vars);

        assertThat(outcome).isEqualTo(ProcessOutcome.COMPLETED);
    }

    @Test
    void demoMode_unsupportedTestType_takesFailureBranch() throws PropertiesException {
        ProcessDefinition definition = CteAutomatedTestProcess.build(env, null);
        Map<String, Object> vars = buildTaskVariablesMap(
                "ANYTHING_ELSE",
                TestSupportClientKonstanten.TEST_PHASE.PHASE_2,
                Boolean.TRUE);

        ProcessOutcome outcome = new ProcessRunner().run(definition, vars);

        // false-Branch: FailureMail → SUB1 → SUB2 → SuccessMail → RestoreTestSystem
        assertThat(outcome).isEqualTo(ProcessOutcome.COMPLETED);
    }

    @Test
    void subProcess_demoMode_runsAllNineSteps() throws PropertiesException {
        ProcessDefinition sub = CteAutomatedTestProcessSUB.build(env, null);
        Map<String, Object> vars = buildTaskVariablesMap(
                CteAutomatedTestProcess.TEST_TYPE_PHASE1_AND_PHASE2,
                TestSupportClientKonstanten.TEST_PHASE.PHASE_1,
                Boolean.TRUE);

        ProcessOutcome outcome = new ProcessRunner().run(sub, vars);

        assertThat(outcome).isEqualTo(ProcessOutcome.COMPLETED);
        assertThat(sub.steps()).hasSize(9);
    }

    /**
     * Pendant zu {@code ActivitiTestSupport#buildTaskVariablesMap}.
     * Befüllt die TaskVariablen für den Prozess.
     */
    private Map<String, Object> buildTaskVariablesMap(String testType,
                                                      TestSupportClientKonstanten.TEST_PHASE phase,
                                                      Boolean demoMode) {
        Map<String, Object> vars = new HashMap<>();
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE, phase);
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_TYPE, testType);
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE, demoMode);
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_ACTIVE_CUSTOMERS, customerTestInfoMapMap);
        vars.put(TesunClientJobListener.UT_TASK_PARAM_USE_ONLY_TEST_CLZ, Boolean.FALSE);
        return vars;
    }

    /**
     * Lädt die Kunden-Konfiguration aus {@code X-TESTS/ITSQ/...}. Im Spike-
     * Setup existieren die Verzeichnisse nicht — dann wird ein leeres
     * Map-Map geliefert (TEST_PHASE → empty map), damit die Demo-Mode-Handler
     * trotzdem laufen.
     */
    private static Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>>
            loadCustomerTestInfoMapMapTolerant(EnvironmentConfig env) {
        try {
            return env.getCustomerTestInfoMapMap();
        } catch (Exception ex) {
            TimelineLogger.info(CteAutomatedTestProcessTest.class,
                    "Keine X-TESTS/ITSQ-Daten gefunden — leere Kundenmap. ({})", ex.getMessage());
            Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> empty = new TreeMap<>();
            for (TestSupportClientKonstanten.TEST_PHASE p : TestSupportClientKonstanten.TEST_PHASE.values()) {
                empty.put(p, new TreeMap<>());
            }
            return empty;
        }
    }
}
