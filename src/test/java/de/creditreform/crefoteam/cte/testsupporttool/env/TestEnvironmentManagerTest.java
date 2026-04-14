package de.creditreform.crefoteam.cte.testsupporttool.env;

import de.creditreform.crefoteam.cte.testsupporttool.config.EnvironmentConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class TestEnvironmentManagerTest {

    @AfterEach
    void cleanup() {
        TestEnvironmentManager.reset();
    }

    @Test
    void switchEnvironment_acquiresLockAndConfiguresLogging(@TempDir Path tmp) {
        File logsRoot = tmp.resolve("logs").toFile();
        EnvironmentConfig ene = EnvironmentConfig.fromMap("ENE",
                java.util.Map.of("TESUN_REST_BASE_URL", "http://x"), logsRoot);

        boolean switched = TestEnvironmentManager.switchEnvironment(ene);

        assertThat(switched).isTrue();
        assertThat(TestEnvironmentManager.getCurrentEnvironment()).isSameAs(ene);
        assertThat(EnvironmentLockManager.getCurrentLockedEnvironment()).isEqualTo("ENE");
        assertThat(new File(logsRoot, "ENE")).exists();
        assertThat(new File(new File(logsRoot, "ENE"), ".env.lock")).exists();
    }

    @Test
    void switchEnvironment_toSameEnv_isNoOp(@TempDir Path tmp) {
        File logsRoot = tmp.resolve("logs").toFile();
        EnvironmentConfig ene = EnvironmentConfig.fromMap("ENE",
                java.util.Map.of("TESUN_REST_BASE_URL", "http://x"), logsRoot);

        assertThat(TestEnvironmentManager.switchEnvironment(ene)).isTrue();
        assertThat(TestEnvironmentManager.switchEnvironment(ene)).isTrue();
    }
}
