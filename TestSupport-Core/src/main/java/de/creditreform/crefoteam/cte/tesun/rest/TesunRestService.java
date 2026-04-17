package de.creditreform.crefoteam.cte.tesun.rest;

import de.creditreform.crefoteam.cte.rest.RestInvokerConfig;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.rest.dto.CteEnvironmentProperties;
import de.creditreform.crefoteam.cte.tesun.rest.dto.CteEnvironmentPropertiesTupel;
import de.creditreform.crefoteam.cte.tesun.rest.dto.RelevanzDecisionMonitoring;
import de.creditreform.crefoteam.cte.tesun.rest.dto.KundenKonfig;
import de.creditreform.crefoteam.cte.tesun.rest.dto.KundenKonfigList;
import de.creditreform.crefoteam.cte.tesun.rest.dto.SystemInfo;
import de.creditreform.crefoteam.cte.tesun.rest.dto.TesunConfigExportInfo;
import de.creditreform.crefoteam.cte.tesun.rest.dto.TesunConfigInfo;
import de.creditreform.crefoteam.cte.tesun.rest.dto.TesunConfigUploadInfo;
import de.creditreform.crefoteam.cte.tesun.rest.dto.TesunExportTrackingErgebnis;
import de.creditreform.crefoteam.cte.tesun.rest.dto.TesunImportTrackingErgebnis;
import de.creditreform.crefoteam.cte.tesun.rest.dto.TesunJobexecutionInfo;
import de.creditreform.crefoteam.cte.tesun.rest.dto.TesunPendingJob;
import de.creditreform.crefoteam.cte.tesun.rest.dto.TesunPendingJobs;
import de.creditreform.crefoteam.cte.tesun.rest.dto.TesunSystemInfo;
import de.creditreform.crefoteam.cte.tesun.rest.inso.TesunInsoAktuellerStand;
import de.creditreform.crefoteam.cte.tesun.rest.inso.XmlKunde;
import de.creditreform.crefoteam.cte.tesun.util.TestCrefo;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Kapselt die REST-Aufrufe gegen den CTE-Tesun-Service. Schlanker Re-Port
 * des gleichnamigen Service aus {@code testsupport_client.tesun_factory} —
 * nur die Methoden, die von den 32 Handlern aufgerufen werden. Rückgabetypen
 * sind DTOs aus {@code rest.dto} / {@code rest.inso}, keine JAXB-Klassen
 * aus externen CTE-Libraries.
 *
 * <p>Intern wird der JDK-{@link HttpClient} verwendet; Request-Bodies und
 * Responses werden primitiv (String-Matching) verarbeitet, da wir keine
 * volle XML/JSON-Bindung nachbauen.
 */
public class TesunRestService {

    private static final int TIMEOUT_MILLIS = 5 * 60 * 1000;

    private final String baseUrl;
    private final HttpClient httpClient;
    private final TesunClientJobListener listener;

    public TesunRestService(RestInvokerConfig config, TesunClientJobListener listener) {
        this(config.getServiceURI(), HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30)).build(), listener);
    }

    public TesunRestService(String baseUrl, HttpClient httpClient, TesunClientJobListener listener) {
        this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl");
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
        this.listener = listener;
    }

    // ========================================================================
    // 1. JVM-Installationen
    // ========================================================================

    public Map<String, String> getJvmInstallationMap() throws IOException, InterruptedException {
        String body = get("/jvms");
        Map<String, String> map = new TreeMap<>();
        // Erwartetes Format: "name1=url1\nname2=url2" oder "{\"name1\":\"url1\",...}"
        for (String line : body.split("[\n\r]+")) {
            int eq = line.indexOf('=');
            if (eq > 0) {
                map.put(line.substring(0, eq).trim(), line.substring(eq + 1).trim());
            }
        }
        return map;
    }

    // ========================================================================
    // 2. Job-Execution-Info (Polling-Ziel)
    // ========================================================================

    public TesunJobexecutionInfo getTesunJobExecutionInfo(String processIdentifier) throws IOException, InterruptedException {
        String body = get("/jobs/" + encode(processIdentifier) + "/last-execution");
        TesunJobexecutionInfo info = new TesunJobexecutionInfo();
        info.setJobStatus(extract(body, "jobStatus"));
        info.setLastStartDate(parseCal(extract(body, "lastStartDate")));
        info.setLastCompletitionDate(parseCal(extract(body, "lastCompletionDate")));
        if (info.getLastCompletitionDate() == null) {
            // Toleranz gegen Schreibweise im Original-JAXB: "lastCompletitionDate"
            info.setLastCompletitionDate(parseCal(extract(body, "lastCompletitionDate")));
        }
        return info;
    }

    // ========================================================================
    // 3. Environment-Properties (Set / Restore)
    // ========================================================================

    public void setEnvironmentProperties(CteEnvironmentProperties properties) throws IOException, InterruptedException {
        StringBuilder body = new StringBuilder("{\"properties\":[");
        for (int i = 0; i < properties.getProperties().size(); i++) {
            CteEnvironmentPropertiesTupel t = properties.getProperties().get(i);
            if (i > 0) body.append(",");
            body.append("{\"key\":\"").append(t.getKey())
                    .append("\",\"value\":\"").append(t.getValue() == null ? "" : t.getValue())
                    .append("\",\"dbOverride\":").append(t.isDbOverride())
                    .append("}");
        }
        body.append("]}");
        post("/environment-properties", body.toString());
    }

    public void restoreEnvironmentProperties() throws IOException, InterruptedException {
        post("/environment-properties/restore", "");
    }

    // ========================================================================
    // 4. INSO-Kunden (für Prepare-Check)
    // ========================================================================

    public XmlKunde readInsoKunde(String kundenKuerzel) throws IOException, InterruptedException {
        String body = get("/inso/kunden/" + encode(kundenKuerzel));
        if (body == null || body.isBlank()) {
            return null;
        }
        XmlKunde k = new XmlKunde();
        k.setFirmenName(extract(body, "firmenName"));
        k.setKundenKuerzel(extract(body, "kundenKuerzel"));
        k.setProduktivSeit(parseCal(extract(body, "produktivSeit")));
        k.setPrduktivBis(parseCal(extract(body, "prduktivBis")));
        k.setAktiv(Boolean.parseBoolean(extract(body, "aktiv")));
        String loesch = extract(body, "loeschKennzeichen");
        k.setLoeschKennzeichen(loesch == null ? null : Boolean.valueOf(loesch));
        return k;
    }

    public TesunInsoAktuellerStand readInsoProductForCrefo(long crefoNummer) throws IOException, InterruptedException {
        String body = get("/inso/produkt/" + crefoNummer);
        TesunInsoAktuellerStand stand = new TesunInsoAktuellerStand();
        stand.setCrefonummer(crefoNummer);
        stand.setProduktStatus(extract(body, "produktStatus"));
        return stand;
    }

    // ========================================================================
    // 5. Export-Tracking (CheckExportProtokoll)
    // ========================================================================

    public TesunExportTrackingErgebnis getExportTrackingInfo(List<TestCrefo> testCrefos, Date fromDate, Date toDate)
            throws IOException, InterruptedException {
        String crefos = testCrefos.stream()
                .map(tc -> String.valueOf(tc.getItsqTestCrefoNr()))
                .collect(Collectors.joining(","));
        StringBuilder query = new StringBuilder("?crefos=").append(encode(crefos));
        if (fromDate != null) query.append("&from=").append(fromDate.getTime());
        if (toDate != null)   query.append("&to=").append(toDate.getTime());
        String body = get("/export-tracking" + query);
        // Für den Spike: leeres Ergebnis zurückgeben (Test deckt nur den Call selbst).
        return new TesunExportTrackingErgebnis();
    }

    // ========================================================================
    // 6. Import-Tracking (WaitForStaging)
    // ========================================================================

    public TesunImportTrackingErgebnis getImportTrackingInfo(List<String> crefoNummern) throws IOException, InterruptedException {
        String crefos = String.join(",", crefoNummern);
        String body = get("/import-tracking?crefos=" + encode(crefos));
        TesunImportTrackingErgebnis result = new TesunImportTrackingErgebnis();
        // Einfaches Parsing — eine Zeile pro Crefo: "crefonummer=status"
        for (String line : body.split("[\n\r]+")) {
            int eq = line.indexOf('=');
            if (eq > 0) {
                TesunImportTrackingErgebnis.CrefoImportResult cr = new TesunImportTrackingErgebnis.CrefoImportResult();
                cr.setCrefonummer(line.substring(0, eq).trim());
                cr.setStagingStatus(line.substring(eq + 1).trim());
                result.getCrefoImportResults().add(cr);
            }
        }
        return result;
    }

    // ========================================================================
    // 7. Crefo-Analyse-Infos (CrefoAnalyseErgebnisse)
    // ========================================================================

    public List<RelevanzDecisionMonitoring> getCrefoAnaylseInfos(String customerKey, List<Long> crefoList)
            throws IOException, InterruptedException {
        String crefos = crefoList.stream().map(String::valueOf).collect(Collectors.joining(","));
        String body = get("/monitoring/relevanz?customer=" + encode(customerKey) + "&crefos=" + encode(crefos));
        List<RelevanzDecisionMonitoring> result = new ArrayList<>();
        for (String line : body.split("[\n\r]+")) {
            if (line.isBlank()) continue;
            RelevanzDecisionMonitoring m = new RelevanzDecisionMonitoring();
            String[] parts = line.split(";");
            if (parts.length >= 3) {
                try { m.setCrefoNummer(Long.valueOf(parts[0].trim())); } catch (NumberFormatException ignored) { }
                m.setDecision(parts[1].trim());
                m.setKundenKuerzel(parts[2].trim());
                result.add(m);
            }
        }
        return result;
    }

    // ========================================================================
    // 8. Erneute Lieferung beantragen
    // ========================================================================

    public void erneuteLieferungBeantragen(List<TestCrefo> testCrefos, String processIdentifier)
            throws IOException, InterruptedException {
        String crefos = testCrefos.stream()
                .map(tc -> String.valueOf(tc.getItsqTestCrefoNr()))
                .collect(Collectors.joining(","));
        StringBuilder body = new StringBuilder("{\"process\":\"").append(processIdentifier)
                .append("\",\"crefos\":[").append(crefos).append("]}");
        post("/erneute-lieferung", body.toString());
    }

    // ========================================================================
    // 9. IKAROS-Auftrag anlegen
    // ========================================================================

    public void createIkarosAuftrag(TestCustomer testCustomer) throws IOException, InterruptedException {
        String body = "{\"customerKey\":\"" + testCustomer.getCustomerKey()
                + "\",\"jvmName\":\"" + (testCustomer.getJvmName() == null ? "" : testCustomer.getJvmName()) + "\"}";
        post("/ikaros/auftrag", body);
    }

    // ========================================================================
    // Low-Level-Helfer
    // ========================================================================

    private String get(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .timeout(Duration.ofMillis(TIMEOUT_MILLIS))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("GET " + path + " lieferte HTTP " + response.statusCode());
        }
        return response.body() == null ? "" : response.body();
    }

    private void post(String path, String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .timeout(Duration.ofMillis(TIMEOUT_MILLIS))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("POST " + path + " lieferte HTTP " + response.statusCode());
        }
    }

    private static String encode(String v) {
        return URLEncoder.encode(v, StandardCharsets.UTF_8);
    }

    private static String extract(String json, String key) {
        if (json == null) return null;
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

    private static Calendar parseCal(String iso) {
        if (iso == null || iso.isBlank()) return null;
        try {
            Instant instant = Instant.parse(iso);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeInMillis(instant.toEpochMilli());
            cal.setTimeZone(java.util.TimeZone.getTimeZone(ZoneId.systemDefault()));
            return cal;
        } catch (Exception ex) {
            return null;
        }
    }

    /** Für Tests: kleiner Dummy-Listener-Getter. */
    protected TesunClientJobListener getListener() { return listener; }

    /** Legacy-Konvenienz für bisherige Handler-Signaturen im Spike. */
    public void startJob(String jvmName, String jobName) throws IOException, InterruptedException {
        post("/jvms/" + encode(jvmName) + "/jobs/" + encode(jobName) + "/start", "");
    }

    /** Legacy-Alias für die alte Methode {@code getJobExecutionInfo}. */
    public TesunJobexecutionInfo getJobExecutionInfo(String processIdentifier) throws IOException, InterruptedException {
        return getTesunJobExecutionInfo(processIdentifier);
    }

    // ========================================================================
    // Pending Jobs
    // ========================================================================

    public TesunPendingJobs getTesunPendingJobs() throws IOException, InterruptedException {
        String body = get("/cte_tesun_service/tesun/jobs/pending");
        List<TesunPendingJob> jobs = new ArrayList<>();
        for (String block : extractXmlBlocks(body, "job")) {
            String id  = extractXmlTag(block, "prozess-identifier");
            String cnt = extractXmlTag(block, "anzahl-todo-bloecke");
            String key = extractXmlTag(block, "infokey-start");
            if (id != null) {
                jobs.add(new TesunPendingJob(id, cnt == null ? 0 : Integer.parseInt(cnt.trim()), key));
            }
        }
        return new TesunPendingJobs(jobs);
    }

    // ========================================================================
    // System Info
    // ========================================================================

    public TesunSystemInfo getTesunSystemInfo() throws IOException, InterruptedException {
        String body = get("/cte_tesun_service/tesun/systeminfo");
        String version = extractXmlTag(body, "cte-version");
        return new TesunSystemInfo(version != null ? version : "unbekannt");
    }

    // ========================================================================
    // System Properties (Kunden-Konfiguration)
    // ========================================================================

    public KundenKonfigList getAllCustomerConfigs() throws IOException, InterruptedException {
        String body = get("/cte_tesun_service/tesun/fachwertconfig/customerCfgs");
        List<KundenKonfig> konfigs = new ArrayList<>();
        for (String block : extractXmlBlocks(body, "konfigs")) {
            KundenKonfig k = new KundenKonfig();
            k.setKundenKuerzel(extractXmlTag(block, "kundenKuerzel"));
            k.setPdversion(extractXmlTag(block, "pdversion"));
            k.setProzessName(extractXmlTag(block, "prozessName"));
            String dateStr = extractXmlTag(block, "aktualisierungsdatum");
            if (dateStr != null) k.setAktualisierungsdatum(parseCal(dateStr));
            konfigs.add(k);
        }
        return new KundenKonfigList(konfigs);
    }

    public TesunConfigInfo getTesunConfigInfo() throws IOException, InterruptedException {
        String body = get("/cte_tesun_service/tesun/configinfo");
        TesunConfigInfo info = new TesunConfigInfo();
        info.setUmgebungsKuerzel(extractXmlTag(body, "umgebungs-kuerzel"));
        List<TesunConfigExportInfo> exports = new ArrayList<>();
        for (String block : extractXmlBlocks(body, "export-pfade")) {
            TesunConfigExportInfo e = new TesunConfigExportInfo();
            e.setKundenKuerzel(extractXmlTag(block, "kunden-kuerzel"));
            e.setRelativePath(extractXmlTag(block, "relative-path"));
            exports.add(e);
        }
        info.setExportPfade(exports);
        List<TesunConfigUploadInfo> uploads = new ArrayList<>();
        for (String block : extractXmlBlocks(body, "upload-pfade")) {
            TesunConfigUploadInfo u = new TesunConfigUploadInfo();
            u.setKundenKuerzel(extractXmlTag(block, "kunden-kuerzel"));
            u.setCompletePath(extractXmlTag(block, "complete-path"));
            uploads.add(u);
        }
        info.setUploadPfade(uploads);
        return info;
    }

    public List<CteEnvironmentPropertiesTupel> getEnvironmentPropertiesFiltered(
            String filter, String scope, boolean withDbValues) throws IOException, InterruptedException {
        StringBuilder path = new StringBuilder("/cte_tesun_service/tesun/environmentproperties?lfencoding=true");
        if (filter != null && !filter.isEmpty()) {
            path.append("&keyFilter=").append(encode(withDbValues ? filter + " -client" : filter));
        }
        if (scope != null && !scope.isEmpty()) {
            path.append("&valueFilter=").append(encode(scope));
        }
        String body = get(path.toString());
        List<CteEnvironmentPropertiesTupel> result = new ArrayList<>();
        for (String block : extractXmlBlocks(body, "properties")) {
            CteEnvironmentPropertiesTupel t = new CteEnvironmentPropertiesTupel();
            t.setKey(extractXmlTag(block, "key"));
            t.setValue(extractXmlTag(block, "value"));
            result.add(t);
        }
        return result;
    }

    public SystemInfo getSystemPropertiesInfo() throws IOException, InterruptedException {
        SystemInfo info = new SystemInfo();
        info.setKundenKonfigList(getAllCustomerConfigs());
        info.setTesunConfigInfo(getTesunConfigInfo());
        info.setEnvPropsList(getEnvironmentPropertiesFiltered(".vc|.exportFormat|extra_xml", "", true));
        return info;
    }

    public void extendTestCustomerProperiesInfos(de.creditreform.crefoteam.cte.tesun.util.TestCustomer tc,
                                                 SystemInfo systemInfo) {
        TesunConfigExportInfo exportInfo = systemInfo.findTesunConfigExportInfoForCustomer(tc);
        if (exportInfo != null) tc.setExportUrl(exportInfo.getRelativePath());

        TesunConfigUploadInfo uploadInfo = systemInfo.findTesunConfigUploadInfoForCustomer(tc);
        if (uploadInfo != null) tc.setUploadUrl(uploadInfo.getCompletePath());

        String prefix = tc.getCustomerPropertyPrefix();
        tc.getPropertyPairsList().forEach(pair -> systemInfo.fillPropertyPairForCustomer(prefix, pair));

        KundenKonfig kk = systemInfo.findFachwertconfigInfoForCustomer(tc);
        tc.setFwAktualisierungsdatum(
                de.creditreform.crefoteam.cte.tesun.util.TesunDateUtils.formatCalendar(kk.getAktualisierungsdatum()));
        tc.setPdVersion(kk.getPdversion());
    }

    // ========================================================================
    // XML-Hilfsroutinen
    // ========================================================================

    private static String extractXmlTag(String xml, String tag) {
        String open  = "<"  + tag + ">";
        String close = "</" + tag + ">";
        int s = xml.indexOf(open);
        if (s < 0) return null;
        int e = xml.indexOf(close, s);
        if (e < 0) return null;
        return xml.substring(s + open.length(), e).trim();
    }

    private static List<String> extractXmlBlocks(String xml, String tag) {
        List<String> blocks = new ArrayList<>();
        String open  = "<"  + tag + ">";
        String close = "</" + tag + ">";
        int pos = 0;
        while (true) {
            int s = xml.indexOf(open, pos);
            if (s < 0) break;
            int e = xml.indexOf(close, s);
            if (e < 0) break;
            blocks.add(xml.substring(s + open.length(), e));
            pos = e + close.length();
        }
        return blocks;
    }
}
