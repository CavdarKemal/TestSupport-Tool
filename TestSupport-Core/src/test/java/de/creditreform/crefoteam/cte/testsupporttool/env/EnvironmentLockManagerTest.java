package de.creditreform.crefoteam.cte.testsupporttool.env;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class EnvironmentLockManagerTest {

    @AfterEach
    void release() {
        EnvironmentLockManager.releaseLock();
    }

    @Test
    void acquireLock_createsLockFileAndMarksEnvironmentAsLocked(@TempDir Path tmp) {
        File envDir = tmp.resolve("ENE").toFile();

        boolean acquired = EnvironmentLockManager.acquireLock(envDir, "ENE");

        assertThat(acquired).isTrue();
        assertThat(new File(envDir, ".env.lock")).exists();
        assertThat(EnvironmentLockManager.isLocked(envDir)).isTrue();
        assertThat(EnvironmentLockManager.getCurrentLockedEnvironment()).isEqualTo("ENE");
    }

    @Test
    void releaseLock_deletesLockFileAndClearsState(@TempDir Path tmp) {
        File envDir = tmp.resolve("GEE").toFile();
        EnvironmentLockManager.acquireLock(envDir, "GEE");

        EnvironmentLockManager.releaseLock();

        assertThat(new File(envDir, ".env.lock")).doesNotExist();
        assertThat(EnvironmentLockManager.isLocked(envDir)).isFalse();
        assertThat(EnvironmentLockManager.getCurrentLockedEnvironment()).isNull();
    }
}
