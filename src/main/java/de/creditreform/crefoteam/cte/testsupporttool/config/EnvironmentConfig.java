package de.creditreform.crefoteam.cte.testsupporttool.config;

import java.util.Objects;

/**
 * Reduzierte Umgebungs-Konfiguration für den Spike. Die Produktiv-Variante
 * im Original-Projekt (`tesun_util.EnvironmentConfig`) ist deutlich
 * umfangreicher und liest Properties aus mehreren Dateien — der Spike
 * abstrahiert hier auf das absolute Minimum.
 */
public final class EnvironmentConfig {

    private final String envName;
    private final String tesunRestBaseUrl;
    private final long jobStatusPollingMillis;
    private final long jobTimeoutMillis;

    public EnvironmentConfig(String envName,
                             String tesunRestBaseUrl,
                             long jobStatusPollingMillis,
                             long jobTimeoutMillis) {
        this.envName = Objects.requireNonNull(envName, "envName");
        this.tesunRestBaseUrl = Objects.requireNonNull(tesunRestBaseUrl, "tesunRestBaseUrl");
        this.jobStatusPollingMillis = jobStatusPollingMillis;
        this.jobTimeoutMillis = jobTimeoutMillis;
    }

    public String getEnvName() { return envName; }

    public String getTesunRestBaseUrl() { return tesunRestBaseUrl; }

    public long getJobStatusPollingMillis() { return jobStatusPollingMillis; }

    public long getJobTimeoutMillis() { return jobTimeoutMillis; }

    /** Defaults für lokale Demos / Tests. */
    public static EnvironmentConfig forDemo(String tesunRestBaseUrl) {
        return new EnvironmentConfig("DEMO", tesunRestBaseUrl, 100L, 5_000L);
    }
}
