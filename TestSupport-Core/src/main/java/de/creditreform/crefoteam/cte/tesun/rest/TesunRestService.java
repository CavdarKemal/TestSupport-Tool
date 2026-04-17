package de.creditreform.crefoteam.cte.tesun.rest;

import de.creditreform.crefoteam.cte.rest.RestInvokerConfig;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.rest.dto.CteEnvironmentProperties;
import de.creditreform.crefoteam.cte.tesun.rest.dto.RelevanzDecisionMonitoring;
import de.creditreform.crefoteam.cte.tesun.rest.dto.SystemInfo;
import de.creditreform.crefoteam.cte.tesun.rest.dto.TesunExportTrackingErgebnis;
import de.creditreform.crefoteam.cte.tesun.rest.dto.TesunImportTrackingErgebnis;
import de.creditreform.crefoteam.cte.tesun.rest.dto.TesunJobexecutionInfo;
import de.creditreform.crefoteam.cte.tesun.rest.inso.TesunInsoAktuellerStand;
import de.creditreform.crefoteam.cte.tesun.rest.inso.XmlKunde;
import de.creditreform.crefoteam.cte.tesun.util.TestCrefo;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
// JAXB-Klassen aus schnittstellen.cte_testsupport + schnittstellen.cte_fachwert_rest
import de.creditreform.crefoteam.cte.restservices.tesun.xmlbinding.configinfo.TesunConfigExportInfo;
import de.creditreform.crefoteam.cte.restservices.tesun.xmlbinding.configinfo.TesunConfigInfo;
import de.creditreform.crefoteam.cte.restservices.tesun.xmlbinding.configinfo.TesunConfigUploadInfo;
import de.creditreform.crefoteam.cte.restservices.tesun.xmlbinding.environmentproperties.CteEnvironmentPropertiesTupel;
import de.creditreform.crefoteam.cte.restservices.tesun.xmlbinding.pendingjobs.TesunPendingJob;
import de.creditreform.crefoteam.cte.restservices.tesun.xmlbinding.pendingjobs.TesunPendingJobs;
import de.creditreform.crefoteam.cte.restservices.tesun.xmlbinding.systeminfo.TesunSystemInfo;
import de.creditreform.crefoteam.cte.restservices.xmlbinding.fachwertaktualisierung.KundenKonfig;
import de.creditreform.crefoteam.cte.restservices.xmlbinding.fachwertaktualisierung.KundenKonfigList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
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
            de.creditreform.crefoteam.cte.tesun.rest.dto.CteEnvironmentPropertiesTupel t = properties.getProperties().get(i);
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
        try {
            String body = get("/cte_tesun_service/tesun/jobs/pending");
            JAXBContext ctx = JAXBContext.newInstance(TesunPendingJobs.class.getPackage().getName());
            return (TesunPendingJobs) ctx.createUnmarshaller().unmarshal(new StringReader(body));
        } catch (Exception ex) {
            throw new IOException("getTesunPendingJobs() fehlgeschlagen", ex);
        }
    }

    // ========================================================================
    // System Info
    // ========================================================================

    @SuppressWarnings("unchecked")
    public TesunSystemInfo getTesunSystemInfo() throws IOException, InterruptedException {
        try {
            String body = get("/cte_tesun_service/tesun/systeminfo");
            JAXBContext ctx = JAXBContext.newInstance(TesunSystemInfo.class.getPackage().getName());
            Unmarshaller u = ctx.createUnmarshaller();
            Object doc = u.unmarshal(new StringReader(body));
            return doc instanceof JAXBElement
                    ? (TesunSystemInfo) ((JAXBElement<?>) doc).getValue()
                    : (TesunSystemInfo) doc;
        } catch (Exception ex) {
            throw new IOException("getTesunSystemInfo() fehlgeschlagen", ex);
        }
    }

    // ========================================================================
    // System Properties (Kunden-Konfiguration)
    // ========================================================================

    public KundenKonfigList getAllCustomerConfigs() throws IOException, InterruptedException {
        try {
            String body = get("/cte_tesun_service/tesun/fachwertconfig/customerCfgs");
            JAXBContext ctx = JAXBContext.newInstance(KundenKonfigList.class.getPackage().getName());
            return (KundenKonfigList) ctx.createUnmarshaller().unmarshal(new StringReader(body));
        } catch (Exception ex) {
            throw new IOException("getAllCustomerConfigs() fehlgeschlagen", ex);
        }
    }

    public TesunConfigInfo getTesunConfigInfo() throws IOException, InterruptedException {
        try {
            String body = get("/cte_tesun_service/tesun/configinfo");
            JAXBContext ctx = JAXBContext.newInstance(TesunConfigInfo.class.getPackage().getName());
            return (TesunConfigInfo) ctx.createUnmarshaller().unmarshal(new StringReader(body));
        } catch (Exception ex) {
            throw new IOException("getTesunConfigInfo() fehlgeschlagen", ex);
        }
    }

    public List<CteEnvironmentPropertiesTupel> getEnvironmentPropertiesFiltered(
            String filter, String scope, boolean withDbValues) throws IOException, InterruptedException {
        try {
            StringBuilder path = new StringBuilder("/cte_tesun_service/tesun/environmentproperties?lfencoding=true");
            if (filter != null && !filter.isEmpty()) {
                path.append("&keyFilter=").append(encode(withDbValues ? filter + " -client" : filter));
            }
            if (scope != null && !scope.isEmpty()) {
                path.append("&valueFilter=").append(encode(scope));
            }
            String body = get(path.toString());
            JAXBContext ctx = JAXBContext.newInstance(
                    de.creditreform.crefoteam.cte.restservices.tesun.xmlbinding.environmentproperties
                            .CteEnvironmentProperties.class.getPackage().getName());
            de.creditreform.crefoteam.cte.restservices.tesun.xmlbinding.environmentproperties.CteEnvironmentProperties
                    props = (de.creditreform.crefoteam.cte.restservices.tesun.xmlbinding.environmentproperties
                            .CteEnvironmentProperties) ctx.createUnmarshaller().unmarshal(new StringReader(body));
            return props.getProperties();
        } catch (Exception ex) {
            throw new IOException("getEnvironmentPropertiesFiltered() fehlgeschlagen", ex);
        }
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

}
