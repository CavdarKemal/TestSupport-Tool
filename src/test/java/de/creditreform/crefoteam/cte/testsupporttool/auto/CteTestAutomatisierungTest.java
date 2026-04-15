package de.creditreform.crefoteam.cte.testsupporttool.auto;

import de.creditreform.crefoteam.cte.statemachine.ProcessOutcome;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.env.EnvironmentLockManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
    void initForEnvironment_acquiresLockAndConfiguresLogger() {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);

        assertThat(EnvironmentLockManager.getCurrentLockedEnvironment())
                .as("Lock muss durch initForEnvironment erworben sein")
                .isEqualTo("ENE");
        assertThat(runner.getEnvironmentConfig()).isSameAs(env);
        assertThat(runner.getTestCustomerMapMap())
                .as("TestCustomer-Map-Map sollte für jede TEST_PHASE einen Eintrag haben")
                .containsKey(TestSupportClientKonstanten.TEST_PHASE.PHASE_1)
                .containsKey(TestSupportClientKonstanten.TEST_PHASE.PHASE_2);
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
    void shutdown_releasesLockAndClearsLogger() {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);
        assertThat(EnvironmentLockManager.getCurrentLockedEnvironment()).isEqualTo("ENE");

        runner.shutdown();
        runner = null;

        assertThat(EnvironmentLockManager.getCurrentLockedEnvironment()).isNull();
    }
}
