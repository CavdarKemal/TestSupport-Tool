package de.creditreform.crefoteam.cte.tesun.rest.dto;

import java.util.Calendar;

public class KundenKonfig {
    private String kundenKuerzel;
    private Calendar aktualisierungsdatum;
    private String pdversion;
    private String prozessName;

    public String getKundenKuerzel() { return kundenKuerzel; }
    public void setKundenKuerzel(String v) { this.kundenKuerzel = v; }

    public Calendar getAktualisierungsdatum() { return aktualisierungsdatum; }
    public void setAktualisierungsdatum(Calendar v) { this.aktualisierungsdatum = v; }

    public String getPdversion() { return pdversion; }
    public void setPdversion(String v) { this.pdversion = v; }

    public String getProzessName() { return prozessName; }
    public void setProzessName(String v) { this.prozessName = v; }
}
