package de.creditreform.crefoteam.cte.testsupporttool.rest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * Abgespeckter REST-Client für den TesunRestService. Liefert
 * Job-Execution-Infos für das Polling-Pattern.
 *
 * <p>Implementierungs-Strategie:
 * <ul>
 *   <li>JDK-eigener {@link HttpClient}, kein Apache HttpComponents — hält die
 *       Dependency-Liste leer.</li>
 *   <li>Antwort-Parsing primitiv (Regex-basiert), da der Spike kein
 *       JSON-Mapping-Framework rechtfertigt.</li>
 * </ul>
 */
public class TesunRestService {

    private final String baseUrl;
    private final HttpClient httpClient;

    public TesunRestService(String baseUrl) {
        this(baseUrl, HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build());
    }

    /** Test-Konstruktor — erlaubt Injection eines vorkonfigurierten Clients. */
    public TesunRestService(String baseUrl, HttpClient httpClient) {
        this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl");
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
    }

    /**
     * Liefert die letzte Job-Execution-Info für den gegebenen Prozess.
     * Endpoint: {@code GET {baseUrl}/jobs/{processIdentifier}/last-execution}
     */
    public JobExecutionInfo getJobExecutionInfo(String processIdentifier) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/jobs/" + processIdentifier + "/last-execution"))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("REST-Aufruf für Prozess '" + processIdentifier
                    + "' lieferte HTTP " + response.statusCode());
        }
        return parse(response.body());
    }

    /** Triggert das Starten eines JVM-Jobs. */
    public void startJob(String jvmName, String jobName) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/jvms/" + jvmName + "/jobs/" + jobName + "/start"))
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("Job-Start für '" + jobName + "' auf JVM '" + jvmName
                    + "' lieferte HTTP " + response.statusCode());
        }
    }

    private JobExecutionInfo parse(String json) {
        String status = extract(json, "jobStatus");
        String startedAt = extract(json, "lastStartDate");
        String completedAt = extract(json, "lastCompletionDate");
        return new JobExecutionInfo(
                status,
                startedAt != null ? Instant.parse(startedAt) : null,
                completedAt != null ? Instant.parse(completedAt) : null
        );
    }

    /**
     * Sehr einfacher String-Extractor: sucht {@code "key":"value"}.
     * Bewusst keine JSON-Library — ein Spike darf primitiv bleiben.
     */
    private String extract(String json, String key) {
        String marker = "\"" + key + "\"";
        int idx = json.indexOf(marker);
        if (idx < 0) return null;
        int colon = json.indexOf(':', idx);
        int firstQuote = json.indexOf('"', colon);
        if (firstQuote < 0) return null;
        int endQuote = json.indexOf('"', firstQuote + 1);
        if (endQuote < 0) return null;
        return json.substring(firstQuote + 1, endQuote);
    }
}
