package de.creditreform.crefoteam.cte.testsupporttool.env;

import de.creditreform.crefoteam.cte.testsupporttool.config.EnvironmentConfig;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;

import java.io.File;

/**
 * Orchestriert Umgebungswechsel: erwirbt den passenden Env-Lock und
 * verdrahtet den {@link TimelineLogger} auf das Logs-Verzeichnis der
 * gewählten Umgebung.
 *
 * <p>Adaption der gleichnamigen Klasse aus
 * {@code testsupport_client.tesun_util} — die Logger-Konfiguration ist hier
 * direkt eingebaut, statt nur „angedeutet" wie im Original.
 */
public final class TestEnvironmentManager {

    public static final String APP_LOG_FILE = "app.log";
    public static final String ACTIONS_LOG_FILE = "actions.log";

    private static EnvironmentConfig currentEnvironment;

    private TestEnvironmentManager() { }

    /** Setzt internen Zustand zurück — vor allem für Tests. */
    public static void reset() {
        TimelineLogger.close();
        EnvironmentLockManager.releaseLock();
        currentEnvironment = null;
    }

    /**
     * Wechselt zur angegebenen Umgebung: Lock erwerben, Logger neu binden.
     *
     * @return {@code true} bei Erfolg, {@code false} wenn die Zielumgebung
     *         bereits durch eine andere Instanz gesperrt ist
     */
    public static boolean switchEnvironment(EnvironmentConfig target) {
        String envName = target.getCurrentEnvName();

        if (currentEnvironment != null && envName.equals(currentEnvironment.getCurrentEnvName())) {
            TimelineLogger.debug(TestEnvironmentManager.class,
                    "Umgebung {} ist bereits aktiv.", envName);
            return true;
        }

        File logsDir = target.getLogOutputsRootForEnv(envName);
        if (EnvironmentLockManager.isLocked(logsDir)) {
            TimelineLogger.warn(TestEnvironmentManager.class,
                    "Umgebung {} ist durch eine andere Instanz gesperrt.", envName);
            return false;
        }

        EnvironmentLockManager.releaseLock();
        if (!EnvironmentLockManager.acquireLock(logsDir, envName)) {
            TimelineLogger.error(TestEnvironmentManager.class,
                    "Lock für Umgebung {} konnte nicht erworben werden.", envName);
            return false;
        }

        // Logger auf das Env-Logs-Verzeichnis umlenken
        TimelineLogger.close();
        if (!TimelineLogger.configure(logsDir, APP_LOG_FILE, ACTIONS_LOG_FILE)) {
            TimelineLogger.error(TestEnvironmentManager.class,
                    "Logger-Konfiguration für Umgebung {} fehlgeschlagen.", envName);
            EnvironmentLockManager.releaseLock();
            return false;
        }

        currentEnvironment = target;
        TimelineLogger.info(TestEnvironmentManager.class,
                "Umgebung gewechselt zu: {} ({})", envName, logsDir.getAbsolutePath());
        return true;
    }

    public static EnvironmentConfig getCurrentEnvironment() {
        return currentEnvironment;
    }
}
