package de.creditreform.crefoteam.cte.tesun.rest.dto;

import java.util.ArrayList;
import java.util.List;

/** DTO-Pendant aus {@code restservices.tesun.xmlbinding.trackingimport}. */
public final class TesunImportTrackingErgebnis {

    private final List<CrefoImportResult> crefoImportResults = new ArrayList<>();

    public List<CrefoImportResult> getCrefoImportResults() { return crefoImportResults; }

    public static final class CrefoImportResult {
        private String crefonummer;
        private String stagingStatus;

        public String getCrefonummer() { return crefonummer; }
        public void setCrefonummer(String crefonummer) { this.crefonummer = crefonummer; }

        public String getStagingStatus() { return stagingStatus; }
        public void setStagingStatus(String stagingStatus) { this.stagingStatus = stagingStatus; }
    }
}
