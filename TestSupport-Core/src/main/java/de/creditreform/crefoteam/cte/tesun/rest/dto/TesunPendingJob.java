package de.creditreform.crefoteam.cte.tesun.rest.dto;

public class TesunPendingJob {
    private final String prozessIdentifier;
    private final int anzahlTodoBloecke;
    private final String infokeyStart;

    public TesunPendingJob(String prozessIdentifier, int anzahlTodoBloecke, String infokeyStart) {
        this.prozessIdentifier = prozessIdentifier;
        this.anzahlTodoBloecke = anzahlTodoBloecke;
        this.infokeyStart = infokeyStart;
    }

    public String getProzessIdentifier() { return prozessIdentifier; }
    public int getAnzahlTodoBloecke() { return anzahlTodoBloecke; }
    public String getInfokeyStart() { return infokeyStart; }
}
