package de.creditreform.crefoteam.cte.jvmclient;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * HTTP-basierte Implementierung des {@link JvmRestClient}.
 *
 * <p>Endpoint-Schema: {@code POST {baseUrl}/jobs/{jobName}/start?k1=v1&k2=v2}.
 * Die Antwort wird primitiv (Regex-basiert) geparst — konsistent mit
 * {@code TesunRestService}.
 */
public final class JvmRestClientImpl implements JvmRestClient {

    private final String baseUrl;
    private final HttpClient httpClient;
    private final AtomicBoolean abortFlag;

    public JvmRestClientImpl(String baseUrl, AtomicBoolean abortFlag) {
        this(baseUrl, HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build(), abortFlag);
    }

    public JvmRestClientImpl(String baseUrl, HttpClient httpClient, AtomicBoolean abortFlag) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
        this.abortFlag = abortFlag;
    }

    @Override
    public JobStartResponse startJob(String jobName, Properties queryParameters) throws IOException, InterruptedException {
        StringBuilder query = new StringBuilder();
        if (queryParameters != null) {
            for (Map.Entry<Object, Object> entry : queryParameters.entrySet()) {
                if (query.length() == 0) query.append('?');
                else query.append('&');
                query.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8))
                        .append('=')
                        .append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
            }
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/jobs/" + jobName + "/start" + query))
                .timeout(Duration.ofSeconds(30))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("JVM-Job-Start '" + jobName + "' lieferte HTTP " + response.statusCode());
        }
        JobStartResponse jsr = new JobStartResponse();
        jsr.setId(extract(response.body(), "id"));
        jsr.setJobId(extract(response.body(), "jobId"));
        return jsr;
    }

    @Override
    public List<JvmJobInfo> readJvmJobInfos(String jvmName) throws IOException, InterruptedException {
        return new java.util.ArrayList<>();
    }

    @Override
    public List<JvmJobExecutionInfo> readJobExecutions(String jobName) throws IOException, InterruptedException {
        return new java.util.ArrayList<>();
    }

    @Override
    public void abortJob(String jobName, String jobId) throws IOException, InterruptedException {
        // Stub: no-op
    }

    @Override
    public JobStatusResponse getJobStatus(String jobName, String processId) throws IOException, InterruptedException {
        JobStatusResponse response = new JobStatusResponse();
        response.setRunning("false");
        response.setStatus("COMPLETED");
        response.setExitCode("COMPLETED");
        return response;
    }

    public boolean isAborted() { return abortFlag != null && abortFlag.get(); }

    private static String extract(String json, String key) {
        String marker = "\"" + key + "\"";
        int idx = json.indexOf(marker);
        if (idx < 0) return null;
        int colon = json.indexOf(':', idx);
        int q1 = json.indexOf('"', colon);
        if (q1 < 0) return null;
        int q2 = json.indexOf('"', q1 + 1);
        if (q2 < 0) return null;
        return json.substring(q1 + 1, q2);
    }
}
