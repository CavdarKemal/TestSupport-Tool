package de.creditreform.crefoteam.cte.testsupporttool.env;

import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

/**
 * Verwaltet prozessübergreifende Locks pro Umgebung.
 *
 * <p>Pro Umgebungsname wird ein {@link ServerSocket} auf einem Loopback-Port
 * geöffnet. Solange der Socket offen ist, kann keine zweite Instanz dieselbe
 * Umgebung übernehmen. Eine Lock-Datei im Umgebungsverzeichnis dokumentiert
 * den aktuellen Halter (PID, Hostname, Zeit).
 *
 * <p>Direkter Port der gleichnamigen Klasse aus
 * {@code testsupport_client.tesun_util}.
 */
public final class EnvironmentLockManager {

    private static final String LOCK_FILE_NAME = ".env.lock";
    private static final int BASE_PORT = 47100;

    private static final Map<String, Integer> ENV_PORTS = new HashMap<>();
    static {
        ENV_PORTS.put("ENE", BASE_PORT);
        ENV_PORTS.put("ABE", BASE_PORT + 1);
        ENV_PORTS.put("GEE", BASE_PORT + 2);
    }

    private static ServerSocket lockSocket;
    private static File currentLockFile;
    private static String currentLockedEnv;
    private static boolean shutdownHookRegistered = false;

    private EnvironmentLockManager() { }

    private static int getPortForEnvironment(String envName) {
        return ENV_PORTS.getOrDefault(envName.toUpperCase(),
                BASE_PORT + Math.abs(envName.hashCode() % 100));
    }

    /**
     * Versucht, die Umgebung zu sperren.
     *
     * @return {@code true} bei Erfolg, {@code false} wenn bereits gesperrt
     */
    public static synchronized boolean acquireLock(File envDir, String envName) {
        if (!envDir.exists() && !envDir.mkdirs()) {
            TimelineLogger.error(EnvironmentLockManager.class,
                    "Konnte Umgebungsverzeichnis nicht anlegen: {}", envDir.getAbsolutePath());
            return false;
        }
        int port = getPortForEnvironment(envName);
        try {
            // SO_REUSEADDR vor bind() setzen — sonst kann auf Windows der Port nach
            // einem close() noch im TIME_WAIT/CLOSE_WAIT haengen, was zu sporadischen
            // BindException-Fehlern fuehrt (sichtbar bei schnell aufeinanderfolgenden
            // Test-Klassen, die alle den ENE-Lock erwerben/freigeben).
            ServerSocket socket = new ServerSocket();
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(InetAddress.getLoopbackAddress(), port), 1);
            lockSocket = socket;
            currentLockedEnv = envName;
            File lockFile = new File(envDir, LOCK_FILE_NAME);
            String lockInfo = "Locked by PID: " + ProcessHandle.current().pid() + "\n"
                    + "Environment: " + envName + "\n"
                    + "Port: " + port + "\n"
                    + "Time: " + java.time.LocalDateTime.now() + "\n";
            Files.writeString(lockFile.toPath(), lockInfo,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            currentLockFile = lockFile;
            TimelineLogger.info(EnvironmentLockManager.class,
                    "Lock für Umgebung {} erworben (Port {})", envName, port);
            return true;
        } catch (IOException e) {
            TimelineLogger.warn(EnvironmentLockManager.class,
                    "Umgebung {} ist bereits gesperrt (Port {} belegt)", envName, port);
            return false;
        }
    }

    public static synchronized void releaseLock() {
        if (lockSocket != null) {
            try {
                String envName = currentLockedEnv;
                int port = lockSocket.getLocalPort();
                lockSocket.close();
                TimelineLogger.info(EnvironmentLockManager.class,
                        "Lock für Umgebung {} freigegeben (Port {})", envName, port);
            } catch (IOException e) {
                TimelineLogger.error(EnvironmentLockManager.class,
                        "Fehler beim Freigeben des Locks: {}", e.getMessage());
            }
            lockSocket = null;
        }
        if (currentLockFile != null && currentLockFile.exists()) {
            try {
                Files.deleteIfExists(currentLockFile.toPath());
            } catch (IOException e) {
                TimelineLogger.warn(EnvironmentLockManager.class,
                        "Lock-Datei konnte nicht gelöscht werden: {}", currentLockFile.getAbsolutePath());
            }
            currentLockFile = null;
        }
        currentLockedEnv = null;
    }

    /** {@code true} wenn die Umgebung anhand des Verzeichnis-Namens gesperrt ist. */
    public static boolean isLocked(File envDir) {
        String envName = envDir.getName().toUpperCase();
        int port = getPortForEnvironment(envName);
        try (ServerSocket testSocket = new ServerSocket()) {
            testSocket.setReuseAddress(true);
            testSocket.bind(new InetSocketAddress(InetAddress.getLoopbackAddress(), port), 1);
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    public static String getCurrentLockedEnvironment() {
        return currentLockedEnv;
    }

    public static synchronized void registerShutdownHook() {
        if (!shutdownHookRegistered) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                TimelineLogger.info(EnvironmentLockManager.class, "Shutdown-Hook: gebe Lock frei...");
                releaseLock();
            }, "EnvironmentLockManager-ShutdownHook"));
            shutdownHookRegistered = true;
        }
    }
}
