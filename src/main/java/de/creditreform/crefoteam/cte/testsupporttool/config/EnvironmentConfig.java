package de.creditreform.crefoteam.cte.testsupporttool.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Umgebungs-Konfiguration. Adaption von
 * {@code testsupport_client.tesun_util.EnvironmentConfig} — auf den
 * Spike-Bedarf reduziert (Property-File-Loading, Log-Pfade,
 * REST-Konfiguration), aber mit kompatibler Konstruktor-Form, um späteren
 * Wechsel auf die volle Variante einfach zu machen.
 *
 * <h3>Property-Datei-Form:</h3>
 * <pre>
 *   ENE-config.properties
 *   ABE-config.properties
 *   GEE-config.properties
 * </pre>
 *
 * Inhalt (alle Keys optional, Defaults im Code):
 * <pre>
 *   TESUN_REST_BASE_URL=http://wls-server.example/cte-rest
 *   JOB_STATUS_POLLING_MILLIS=2000
 *   JOB_TIMEOUT_MILLIS=1800000
 *   LOG_OUTPUTS_ROOT=logs
 * </pre>
 */
public final class EnvironmentConfig {

    public static final String ENV_CONFIG_FILE_SUFFIX = "-config.properties";

    public static final String PROP_TESUN_REST_BASE_URL    = "TESUN_REST_BASE_URL";
    public static final String PROP_JOB_STATUS_POLLING_MS  = "JOB_STATUS_POLLING_MILLIS";
    public static final String PROP_JOB_TIMEOUT_MS         = "JOB_TIMEOUT_MILLIS";
    public static final String PROP_LOG_OUTPUTS_ROOT       = "LOG_OUTPUTS_ROOT";

    private final String currentEnvName;
    private final String tesunRestBaseUrl;
    private final long jobStatusPollingMillis;
    private final long jobTimeoutMillis;
    private final File logOutputsRoot;
    private final File environmentConfigFile;

    public EnvironmentConfig(String envName,
                             String tesunRestBaseUrl,
                             long jobStatusPollingMillis,
                             long jobTimeoutMillis) {
        this(envName, tesunRestBaseUrl, jobStatusPollingMillis, jobTimeoutMillis,
                new File("logs/" + envName), null);
    }

    private EnvironmentConfig(String envName,
                              String tesunRestBaseUrl,
                              long jobStatusPollingMillis,
                              long jobTimeoutMillis,
                              File logOutputsRoot,
                              File environmentConfigFile) {
        this.currentEnvName = Objects.requireNonNull(envName, "envName");
        this.tesunRestBaseUrl = Objects.requireNonNull(tesunRestBaseUrl, "tesunRestBaseUrl");
        this.jobStatusPollingMillis = jobStatusPollingMillis;
        this.jobTimeoutMillis = jobTimeoutMillis;
        this.logOutputsRoot = Objects.requireNonNull(logOutputsRoot, "logOutputsRoot");
        this.environmentConfigFile = environmentConfigFile;
    }

    // ====================== Getter ======================

    /** Beibehalten für rückwärtskompatible Aufrufer. */
    public String getEnvName() { return currentEnvName; }

    /** Bevorzugter Getter — entspricht dem Original-Namen. */
    public String getCurrentEnvName() { return currentEnvName; }

    public String getTesunRestBaseUrl() { return tesunRestBaseUrl; }

    public long getJobStatusPollingMillis() { return jobStatusPollingMillis; }

    public long getJobTimeoutMillis() { return jobTimeoutMillis; }

    /** Wurzel-Verzeichnis für Log-Dateien dieser Umgebung. */
    public File getLogOutputsRoot() { return logOutputsRoot; }

    /**
     * Wurzel-Verzeichnis für Logs einer (anderen) Umgebung — z. B. wenn
     * vor dem Wechsel geprüft werden soll, ob das Ziel-Logs-Verzeichnis
     * gesperrt ist.
     */
    public File getLogOutputsRootForEnv(String envName) {
        if (envName.equalsIgnoreCase(currentEnvName)) {
            return logOutputsRoot;
        }
        // Geschwister-Verzeichnis unter demselben Logs-Root
        return new File(logOutputsRoot.getParentFile(), envName);
    }

    public File getEnvironmentConfigFile() { return environmentConfigFile; }

    // ====================== Factories ======================

    /** Default für Tests / lokale Demos — schreibt Logs nach {@code logs/DEMO}. */
    public static EnvironmentConfig forDemo(String tesunRestBaseUrl) {
        return new EnvironmentConfig("DEMO", tesunRestBaseUrl, 100L, 5_000L);
    }

    /**
     * Lädt eine Umgebung aus einer {@code *-config.properties}-Datei.
     * Der Umgebungsname wird aus dem Dateinamen abgeleitet
     * ({@code ENE-config.properties → ENE}).
     */
    public static EnvironmentConfig load(File configFile) {
        Objects.requireNonNull(configFile, "configFile");
        if (!configFile.exists()) {
            throw new IllegalArgumentException(
                    "Konfigurations-Datei existiert nicht: " + configFile.getAbsolutePath());
        }
        String filename = configFile.getName();
        if (!filename.endsWith(ENV_CONFIG_FILE_SUFFIX)) {
            throw new IllegalArgumentException(
                    "Dateiname muss auf '" + ENV_CONFIG_FILE_SUFFIX + "' enden: " + filename);
        }
        String envName = filename.substring(0, filename.length() - ENV_CONFIG_FILE_SUFFIX.length());

        Properties props = readProperties(configFile);
        String restUrl = required(props, PROP_TESUN_REST_BASE_URL, configFile);
        long polling = parseLong(props.getProperty(PROP_JOB_STATUS_POLLING_MS, "2000"), PROP_JOB_STATUS_POLLING_MS);
        long timeout = parseLong(props.getProperty(PROP_JOB_TIMEOUT_MS, "1800000"), PROP_JOB_TIMEOUT_MS);
        File logsRoot = resolveLogsRoot(props.getProperty(PROP_LOG_OUTPUTS_ROOT, "logs"), envName, configFile);

        return new EnvironmentConfig(envName, restUrl, polling, timeout, logsRoot, configFile);
    }

    /**
     * Lädt eine Umgebung über ihren Namen, indem im aktuellen Arbeits-
     * verzeichnis (oder darüber) nach {@code <envName>-config.properties}
     * gesucht wird.
     */
    public static EnvironmentConfig loadByName(String envName) {
        Map<String, File> envs = discoverEnvironments();
        File configFile = envs.get(envName.toUpperCase());
        if (configFile == null) {
            throw new IllegalArgumentException("Umgebung '" + envName + "' nicht gefunden. "
                    + "Bekannte Umgebungen: " + envs.keySet());
        }
        return load(configFile);
    }

    /**
     * Sucht im aktuellen Arbeitsverzeichnis und allen Eltern-Verzeichnissen
     * nach Dateien {@code *-config.properties} und liefert {@code envName -> Datei}.
     */
    public static Map<String, File> discoverEnvironments() {
        Map<String, File> result = new TreeMap<>();
        Path current = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
        while (current != null) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(current,
                    "*" + ENV_CONFIG_FILE_SUFFIX)) {
                for (Path p : stream) {
                    String name = p.getFileName().toString();
                    String envName = name.substring(0, name.length() - ENV_CONFIG_FILE_SUFFIX.length());
                    result.putIfAbsent(envName.toUpperCase(), p.toFile());
                }
            } catch (IOException ignored) { /* Verzeichnis nicht lesbar */ }
            if (!result.isEmpty()) break;
            current = current.getParent();
        }
        return result;
    }

    // ====================== Helpers ======================

    private static Properties readProperties(File file) {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(file.toPath())) {
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Konnte Properties-Datei nicht lesen: " + file.getAbsolutePath(), e);
        }
        return props;
    }

    private static String required(Properties props, String key, File source) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Property '" + key + "' fehlt in "
                    + source.getAbsolutePath());
        }
        return value.trim();
    }

    private static long parseLong(String raw, String propName) {
        try {
            return Long.parseLong(raw.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Property '" + propName
                    + "' muss eine Zahl sein, ist: '" + raw + "'");
        }
    }

    private static File resolveLogsRoot(String configured, String envName, File configFile) {
        File rawRoot = new File(configured);
        File absoluteRoot = rawRoot.isAbsolute()
                ? rawRoot
                : new File(configFile.getParentFile(), configured);
        return new File(absoluteRoot, envName);
    }

    /** Hilfsmethode: ein Property-Map-Wrapper für ad-hoc-Konfiguration in Tests. */
    public static EnvironmentConfig fromMap(String envName, Map<String, String> props, File logsRoot) {
        Properties p = new Properties();
        p.putAll(new HashMap<>(props));
        long polling = parseLong(p.getProperty(PROP_JOB_STATUS_POLLING_MS, "100"), PROP_JOB_STATUS_POLLING_MS);
        long timeout = parseLong(p.getProperty(PROP_JOB_TIMEOUT_MS, "5000"), PROP_JOB_TIMEOUT_MS);
        String url = p.getProperty(PROP_TESUN_REST_BASE_URL, "http://localhost");
        return new EnvironmentConfig(envName, url, polling, timeout, new File(logsRoot, envName), null);
    }
}
