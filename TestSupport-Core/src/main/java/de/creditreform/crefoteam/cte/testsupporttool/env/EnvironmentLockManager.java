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

    /**
     * Pro Umgebung reservieren wir einen Port-Range (10 Ports). Hintergrund:
     * unter Windows mit Hyper-V/WSL/Antivirus sind einzelne Loopback-Ports
     * gelegentlich unbindable, ohne dass {@code netstat} oder
     * {@code netsh excludedportrange} sie ausweisen. Durch den Range findet
     * {@link #acquireLock} immer einen freien Port; {@link #isLocked} prüft
     * den gesamten Range — eine zweite JVM kann den Lock also nicht stillschweigend
     * umgehen.
     */
    private static final int PORT_RANGE_SIZE = 10;

    private static final Map<String, Integer> ENV_PORT_BASES = new HashMap<>();
    static {
        ENV_PORT_BASES.put("ENE", BASE_PORT);                        // 47100-47109
        ENV_PORT_BASES.put("ABE", BASE_PORT + PORT_RANGE_SIZE);      // 47110-47119
        ENV_PORT_BASES.put("GEE", BASE_PORT + 2 * PORT_RANGE_SIZE);  // 47120-47129
    }

    private static ServerSocket lockSocket;
    private static File currentLockFile;
    private static String currentLockedEnv;
    private static boolean shutdownHookRegistered = false;

    private EnvironmentLockManager() { }

    private static int getPortBaseForEnvironment(String envName) {
        return ENV_PORT_BASES.getOrDefault(envName.toUpperCase(),
                BASE_PORT + Math.abs(envName.hashCode() % 100) * PORT_RANGE_SIZE);
    }

    /**
     * Versucht, die Umgebung zu sperren.
     *
     * <p>SO_REUSEADDR + 3-fach Retry mit 50ms Pause: unter JDK 21 / Windows
     * gibt der OS einen frisch geschlossenen Loopback-Port nicht immer sofort
     * wieder frei. In Test-Suites, wo schnell hintereinander Lock-erwerb +
     * -freigabe stattfindet, wuerde die erste {@code bind()} sonst sporadisch
     * scheitern, obwohl der Lock semantisch frei ist.
     *
     * @return {@code true} bei Erfolg, {@code false} wenn bereits gesperrt
     */
    public static synchronized boolean acquireLock(File envDir, String envName) {
        if (!envDir.exists() && !envDir.mkdirs()) {
            TimelineLogger.error(EnvironmentLockManager.class,
                    "Konnte Umgebungsverzeichnis nicht anlegen: {}", envDir.getAbsolutePath());
            return false;
        }
        int basePort = getPortBaseForEnvironment(envName);
        int boundPort = -1;
        ServerSocket socket = null;
        for (int offset = 0; offset < PORT_RANGE_SIZE; offset++) {
            int port = basePort + offset;
            socket = bindLoopbackPortWithRetry(port);
            if (socket != null) {
                boundPort = port;
                break;
            }
        }
        if (socket == null) {
            TimelineLogger.warn(EnvironmentLockManager.class,
                    "Umgebung {} ist bereits gesperrt (Port-Range {}-{} komplett belegt)",
                    envName, basePort, basePort + PORT_RANGE_SIZE - 1);
            return false;
        }
        try {
            lockSocket = socket;
            currentLockedEnv = envName;
            File lockFile = new File(envDir, LOCK_FILE_NAME);
            String lockInfo = "Locked by PID: " + ProcessHandle.current().pid() + "\n"
                    + "Environment: " + envName + "\n"
                    + "Port: " + boundPort + "\n"
                    + "Time: " + java.time.LocalDateTime.now() + "\n";
            Files.writeString(lockFile.toPath(), lockInfo,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            currentLockFile = lockFile;
            TimelineLogger.info(EnvironmentLockManager.class,
                    "Lock für Umgebung {} erworben (Port {})", envName, boundPort);
            return true;
        } catch (IOException e) {
            // Lock-Datei konnte nicht geschrieben werden → Socket sauber zuruecknehmen.
            try { socket.close(); } catch (IOException ignored) { }
            lockSocket = null;
            currentLockedEnv = null;
            TimelineLogger.error(EnvironmentLockManager.class,
                    "Lock-Datei konnte nicht geschrieben werden: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Versucht bis zu 3x (mit jeweils 50ms Pause) einen ServerSocket auf
     * dem IPv4-Loopback-Port zu binden. Liefert den gebundenen Socket oder
     * {@code null}, wenn alle Versuche scheitern.
     *
     * <p>IPv4 explizit (127.0.0.1) statt {@code InetAddress.getLoopbackAddress()}:
     * unter JDK 21 / Windows liefert die generische Loopback-Variante je nach
     * IPv6-Konfiguration manchmal {@code ::1}, was beim {@code bind} im
     * dual-stack-Setup zu sporadischen IOExceptions fuehrt.
     */
    private static ServerSocket bindLoopbackPortWithRetry(int port) {
        InetAddress loopback;
        try {
            loopback = InetAddress.getByName("127.0.0.1");
        } catch (IOException e) {
            // sollte nie passieren — 127.0.0.1 ist literal, kein DNS-Lookup
            loopback = InetAddress.getLoopbackAddress();
        }
        IOException lastException = null;
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                ServerSocket socket = new ServerSocket();
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(loopback, port), 1);
                return socket;
            } catch (IOException e) {
                lastException = e;
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
        }
        if (lastException != null) {
            TimelineLogger.warn(EnvironmentLockManager.class,
                    "bind(127.0.0.1:{}) nach 3 Versuchen gescheitert: {}",
                    port, lastException.getMessage());
        }
        return null;
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

    /**
     * {@code true} wenn die Umgebung anhand des Verzeichnis-Namens gesperrt ist.
     *
     * <p>Prüft den vollständigen Port-Range: gelingt das probe-bind auf ALLEN
     * Ports im Range → Port-Range frei → nicht gesperrt. Sobald ein Port
     * belegt ist → gesperrt (Lock läuft auf diesem Port).
     *
     * <p>Nicht-bindbare Ports aus OS-Gründen (Hyper-V-Reservierung etc.)
     * würden hier als "gesperrt" gelten; das ist bewusst konservativ.
     */
    public static boolean isLocked(File envDir) {
        String envName = envDir.getName().toUpperCase();
        int basePort = getPortBaseForEnvironment(envName);
        for (int offset = 0; offset < PORT_RANGE_SIZE; offset++) {
            int port = basePort + offset;
            ServerSocket probe = bindLoopbackPortWithRetry(port);
            if (probe == null) {
                return true;
            }
            try {
                probe.close();
            } catch (IOException ignored) {
                // best-effort
            }
        }
        return false;
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
