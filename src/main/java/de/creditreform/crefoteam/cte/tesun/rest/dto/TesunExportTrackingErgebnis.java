package de.creditreform.crefoteam.cte.tesun.rest.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO-Pendant aus {@code restservices.tesun.xmlbinding.trackingexport}.
 * Stark reduziert (nur die Felder, die der Handler braucht).
 */
public final class TesunExportTrackingErgebnis {

    private final List<CrefoResult> crefoResults = new ArrayList<>();

    public List<CrefoResult> getCrefoResults() { return crefoResults; }

    public static final class CrefoResult {
        private Long crefoNummer;
        private String status;
        private String message;

        public Long getCrefoNummer() { return crefoNummer; }
        public void setCrefoNummer(Long crefoNummer) { this.crefoNummer = crefoNummer; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
