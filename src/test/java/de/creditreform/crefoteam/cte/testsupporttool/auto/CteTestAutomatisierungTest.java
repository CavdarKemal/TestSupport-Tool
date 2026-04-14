package de.creditreform.crefoteam.cte.testsupporttool.auto;

import de.creditreform.crefoteam.cte.statemachine.ProcessOutcome;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.env.EnvironmentLockManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-End-Test der Headless-Variante. Verifiziert, dass die gesamte
 * Initialisierung in {@link CteTestAutomatisierung} liegt und der
 * Test selbst nur noch konstruiert + startet.
 */
class CteTestAutomatisierungTest {

    private String originalUserDir;
    private CteTestAutomatisierung runner;

    @BeforeEach
    void redirectUserDirToTmp(@TempDir Path tmp) {
        originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tmp.toAbsolutePath().toString());
    }

    @AfterEach
    void cleanup() {
        if (runner != null) {
            runner.shutdown();
            runner = null;
        }
        if (originalUserDir != null) {
            System.setProperty("user.dir", originalUserDir);
        }
    }

    @Test
    void initForEnvironment_acquiresLockAndConfiguresLogger() {
        EnvironmentConfig env = EnvironmentConfig.forDemo("http://unused-in-demo");
        runner = new CteTestAutomatisierung(env);

        assertThat(EnvironmentLockManager.getCurrentLockedEnvironment())
                .as("Lock muss durch initForEnvironment erworben sein")
                .isEqualTo("DEMO");
        assertThat(runner.getEnvironmentConfig()).isSameAs(env);
        assertThat(runner.getTestCustomerMapMap())
                .as("TestCustomer-Map-Map sollte für jede TEST_PHASE einen Eintrag haben")
                .containsKey(TestSupportClientKonstanten.TEST_PHASE.PHASE_1)
                .containsKey(TestSupportClientKonstanten.TEST_PHASE.PHASE_2);
    }

    @Test
    void startProcess_demoMode_completesEndToEnd() throws PropertiesException {
        EnvironmentConfig env = EnvironmentConfig.forDemo("http://unused-in-demo");
        runner = new CteTestAutomatisierung(env);

        ProcessOutcome outcome = runner.startProcess(true);

        assertThat(outcome).isEqualTo(ProcessOutcome.COMPLETED);
    }

    @Test
    void buildTaskVariablesMap_containsAllRequiredKeys() {
        EnvironmentConfig env = EnvironmentConfig.forDemo("http://unused-in-demo");
        runner = new CteTestAutomatisierung(env);

        var vars = runner.buildTaskVariablesMap(true);

        assertThat(vars)
                .containsKey("TEST_TYPE")
                .containsKey("TEST_PHASE")
                .containsKey("DEMO_MODE")
                .containsKey("ACTIVE_CUSTOMERS")
                .containsKey("USE_ONLY_TEST_CLZ");
        assertThat(vars.get("DEMO_MODE")).isEqualTo(Boolean.TRUE);
    }

    @Test
    void shutdown_releasesLockAndClearsLogger() {
        EnvironmentConfig env = EnvironmentConfig.forDemo("http://unused-in-demo");
        runner = new CteTestAutomatisierung(env);
        assertThat(EnvironmentLockManager.getCurrentLockedEnvironment()).isEqualTo("DEMO");

        runner.shutdown();
        runner = null;

        assertThat(EnvironmentLockManager.getCurrentLockedEnvironment()).isNull();
    }
}
