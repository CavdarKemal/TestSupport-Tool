package de.creditreform.crefoteam.cte.testsupporttool.auto;

import de.creditreform.crefoteam.cte.statemachine.ProcessOutcome;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import org.apache.log4j.Level;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * End-to-End-Test der Headless-Variante. Verifiziert, dass die gesamte
 * Initialisierung in {@link CteTestAutomatisierung} liegt und der
 * Test selbst nur noch konstruiert + startet.
 *
 * <p>Voraussetzung: {@code X-TESTS/ITSQ/REF-EXPORTS/PHASE-{1,2}} existiert
 * im Projekt-Root — wird durch das {@code maven-dependency-plugin} in der
 * {@code generate-resources}-Phase aus dem {@code itsq_testfaelle}-Artefakt
 * entpackt. In Produktion liefert der Assembly-Descriptor dasselbe
 * Verzeichnis mit.
 */
class CteTestAutomatisierungTest {

    private CteTestAutomatisierung runner;

    @AfterEach
    void cleanup() {
        if (runner != null) {
            runner.shutdown();
            runner = null;
        }
    }

    @Test
    void startProcess_demoMode_completesEndToEnd() throws PropertiesException {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);

        ProcessOutcome outcome = runner.startProcess(true);

        assertThat(outcome).isEqualTo(ProcessOutcome.COMPLETED);
    }

    @Test
    void startProcess_demoMode_writesTestResultsFileAndZip() throws Exception {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);

        runner.startProcess(true);

        // TestResults.txt wird in testOutputsRoot abgelegt — auch wenn leer
        java.io.File testResults = new java.io.File(env.getTestOutputsRoot(), "TestResults.txt");
        assertThat(testResults).as("TestResults.txt muss geschrieben werden").exists();

        // ZIP wird im Elternverzeichnis von testOutputsRoot angelegt
        assertThat(runner.getLastZipFilePath())
                .as("ZIP-Pfad muss gesetzt sein").isNotNull();
        assertThat(new java.io.File(runner.getLastZipFilePath()))
                .as("ZIP-Datei muss existieren").exists();
    }

    @Test
    void computeExitCode_followsOriginalSemantics() {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);

        // outcome != COMPLETED → 1
        assertThat(runner.computeExitCode(ProcessOutcome.ABORTED)).isEqualTo(1);
        assertThat(runner.computeExitCode(ProcessOutcome.FAILED)).isEqualTo(1);

        // outcome == COMPLETED und leerer Dump → 0 (kein Aufruf von startProcess: lastResultsBodyLength ist 0)
        assertThat(runner.computeExitCode(ProcessOutcome.COMPLETED)).isEqualTo(0);
    }

    @Test
    void buildTaskVariablesMap_containsAllSixteenOriginalKeys() throws Exception {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);

        var vars = runner.buildTaskVariablesMap(
                true,
                runner.getTestCustomerMapMap(),
                TestSupportClientKonstanten.TEST_TYPES.PHASE1_AND_PHASE2,
                TestSupportClientKonstanten.TEST_PHASE.PHASE_1,
                true,
                false);

        // Alle 16 Keys, die ActivitiTestSupport.buildTaskVariablesMap setzt
        assertThat(vars).containsKeys(
                "DEMO_MODE", "MEIN_KEY", "ACTIVITI_PROCESS_NAME",
                "TIME_BEFORE_BTLG_IMPORT", "TIME_BEFORE_CT_IMPORT", "TIME_BEFORE_EXPORT",
                "TIME_BEFORE_EXPORTS_COLLECT", "TIME_BEFORE_SFTP_COLLECT",
                "EMAIL_FROM", "SUCCESS_MAIL_TO", "FAILURE_MAIL_TO",
                "ACTIVE_CUSTOMERS", "TEST_TYPE", "TEST_PHASE",
                "USE_ONLY_TEST_CLZ", "UPLOAD_SYNTH_TEST_CREFOS");
        assertThat(vars).hasSize(16);
        assertThat(vars.get("DEMO_MODE")).isEqualTo(Boolean.TRUE);
    }

    @Test
    void buildTaskVariablesMap_propagatesEnvDerivedValues() throws Exception {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);

        var vars = runner.buildTaskVariablesMap(
                false,
                runner.getTestCustomerMapMap(),
                TestSupportClientKonstanten.TEST_TYPES.PHASE1_AND_PHASE2,
                TestSupportClientKonstanten.TEST_PHASE.PHASE_2,
                false,
                true);

        // env-abgeleitete Werte aus ENE-config.properties
        assertThat(vars.get("MEIN_KEY")).isEqualTo("ENE");
        assertThat(vars.get("ACTIVITI_PROCESS_NAME")).isEqualTo("ENE-TestAutomationProcess");
        assertThat(vars.get("EMAIL_FROM")).isEqualTo("test-automatisierung@creditreform.de");
        assertThat(vars.get("SUCCESS_MAIL_TO")).isEqualTo("kemal@cavdar.de");
        assertThat(vars.get("FAILURE_MAIL_TO")).isEqualTo("kemal@cavdar.de");
        // Boolean-Argumente werden 1:1 durchgereicht
        assertThat(vars.get("DEMO_MODE")).isEqualTo(Boolean.FALSE);
        assertThat(vars.get("USE_ONLY_TEST_CLZ")).isEqualTo(Boolean.FALSE);
        assertThat(vars.get("UPLOAD_SYNTH_TEST_CREFOS")).isEqualTo(Boolean.TRUE);
        assertThat(vars.get("TEST_PHASE")).isEqualTo(TestSupportClientKonstanten.TEST_PHASE.PHASE_2);
        // ACTIVE_CUSTOMERS = die uebergebene Map
        assertThat(vars.get("ACTIVE_CUSTOMERS")).isSameAs(runner.getTestCustomerMapMap());
    }

    @Test
    void getters_returnInstancesProvidedAtConstruction() {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);

        assertThat(runner.getEnvironmentConfig()).isSameAs(env);
        assertThat(runner.getTestCustomerMapMap())
                .as("Map-Map muss nach init nicht-null und alle Phasen enthalten")
                .isNotNull()
                .containsKey(TestSupportClientKonstanten.TEST_PHASE.PHASE_1)
                .containsKey(TestSupportClientKonstanten.TEST_PHASE.PHASE_2);
        // Vor startProcess: ZIP-Pfad noch nicht gesetzt, Body leer
        assertThat(runner.getLastZipFilePath()).isNull();
        assertThat(runner.getLastResultsBodyLength()).isZero();
    }

    @Test
    void notifyClientJob_ignoresNullAndLeadingDot() {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);

        // null und Strings die mit '.' beginnen werden uebersprungen — kein Throw, kein State-Change
        assertThatCode(() -> {
            runner.notifyClientJob(Level.INFO, null);
            runner.notifyClientJob(Level.INFO, ".heartbeat");
            runner.notifyClientJob(Level.WARN, "echte Warnung");
        }).doesNotThrowAnyException();
    }

    @Test
    void askClientJob_retry_alwaysReturnsFalse() {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);

        Object result = runner.askClientJob(
                TesunClientJobListener.ASK_FOR.ASK_OBJECT_RETRY, "irgendein Fehler");

        assertThat(result).isEqualTo(Boolean.FALSE);
    }

    @Test
    void askClientJob_cteVersion_returnsZeroWhenUnset() {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);

        // ENE-config setzt keine CTE-Version → Fallback 0
        Object result = runner.askClientJob(
                TesunClientJobListener.ASK_FOR.ASK_OBJECT_CTE_VERSION, null);

        assertThat(result).isEqualTo(0);
    }

    @Test
    void askClientJob_pathQueries_returnRefExportsAbsolutePath() {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);

        String expected = env.getItsqRefExportsRoot().getAbsolutePath();

        assertThat(runner.askClientJob(
                TesunClientJobListener.ASK_FOR.ASK_REF_EXPORTS_PATH, null)).isEqualTo(expected);
        assertThat(runner.askClientJob(
                TesunClientJobListener.ASK_FOR.ASK_TEST_CASES_PATH, null)).isEqualTo(expected);
    }

    @Test
    void askClientJob_booleanQueries_returnTrue() {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);

        assertThat(runner.askClientJob(
                TesunClientJobListener.ASK_FOR.ASK_WAIT_FOR_TEST_SYSTEM, null)).isEqualTo(Boolean.TRUE);
        assertThat(runner.askClientJob(
                TesunClientJobListener.ASK_FOR.ASK_OBJECT_COPY_EXPORTS_TO_INPUTS, null)).isEqualTo(Boolean.TRUE);
        assertThat(runner.askClientJob(
                TesunClientJobListener.ASK_FOR.ASK_CHECK_COLLECTS, null)).isEqualTo(Boolean.TRUE);
    }

    @Test
    void askClientJob_exception_echoesUserObject() {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);

        Object payload = new IllegalStateException("boom");

        assertThat(runner.askClientJob(
                TesunClientJobListener.ASK_FOR.ASK_OBJECT_EXCEPTION, payload)).isSameAs(payload);
    }

    @Test
    void askClientJob_unknownAsk_swallowsExceptionAndReturnsNull() {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);

        // ASK_OBJECT_ENVIRONMENT ist im switch nicht gehandhabt → default-Branch wirft
        // PropertiesException, askClientJob faengt sie und liefert null.
        Object result = runner.askClientJob(
                TesunClientJobListener.ASK_FOR.ASK_OBJECT_ENVIRONMENT, null);

        assertThat(result).isNull();
    }

}
