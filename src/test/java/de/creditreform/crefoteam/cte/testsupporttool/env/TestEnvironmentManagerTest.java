package de.creditreform.crefoteam.cte.testsupporttool.env;

import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
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

    /**
     * Einfacher Positiv-Test mit der Demo-Config. Wir können das
     * Logs-Verzeichnis nicht frei wählen — {@code getLogOutputsRoot} nutzt
     * {@code testResourcesRoot}, das im Demo-Mode auf {@code X-TESTS} zeigt.
     * Der Test prüft nur, dass der Switch erfolgreich ist und der Lock erworben wurde.
     */
    @Test
    void switchEnvironment_onDemoConfig_succeedsAndAcquiresLock(@TempDir Path tmp) {
        // user.dir temporär auf tmp umlenken, damit das logs/ENE-Verzeichnis hier landet
        String originalUserDir = System.getProperty("user.dir");
        try {
            System.setProperty("user.dir", tmp.toAbsolutePath().toString());
            new File(tmp.toFile(), "X-TESTS").mkdirs();

            EnvironmentConfig ene = new EnvironmentConfig("ENE");

            boolean switched = TestEnvironmentManager.switchEnvironment(ene);

            assertThat(switched).isTrue();
            assertThat(TestEnvironmentManager.getCurrentEnvironment()).isSameAs(ene);
            assertThat(EnvironmentLockManager.getCurrentLockedEnvironment()).isEqualTo("ENE");
        } finally {
            System.setProperty("user.dir", originalUserDir);
        }
    }

    @Test
    void switchEnvironment_toSameEnv_isNoOp(@TempDir Path tmp) {
        String originalUserDir = System.getProperty("user.dir");
        try {
            System.setProperty("user.dir", tmp.toAbsolutePath().toString());
            new File(tmp.toFile(), "X-TESTS").mkdirs();

            EnvironmentConfig ene = new EnvironmentConfig("ENE");
            assertThat(TestEnvironmentManager.switchEnvironment(ene)).isTrue();
            assertThat(TestEnvironmentManager.switchEnvironment(ene)).isTrue();
        } finally {
            System.setProperty("user.dir", originalUserDir);
        }
    }
}
