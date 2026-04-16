package de.creditreform.crefoteam.cte.tesun.rest.inso;

/** DTO-Pendant aus {@code cteinsoexporttesun.insoxmlbinding}. */
public final class TesunInsoAktuellerStand {

    private Long crefonummer;
    private String produktStatus;

    public Long getCrefonummer() { return crefonummer; }
    public void setCrefonummer(Long crefonummer) { this.crefonummer = crefonummer; }

    public String getProduktStatus() { return produktStatus; }
    public void setProduktStatus(String produktStatus) { this.produktStatus = produktStatus; }
}
