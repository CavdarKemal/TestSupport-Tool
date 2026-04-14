package de.creditreform.crefoteam.cte.tesun.rest.dto;

/** DTO-Pendant aus {@code monitoringbackend.xmlbinding}. */
public final class RelevanzDecisionMonitoring {

    private Long crefoNummer;
    private String decision;
    private String kundenKuerzel;

    public Long getCrefoNummer() { return crefoNummer; }
    public void setCrefoNummer(Long crefoNummer) { this.crefoNummer = crefoNummer; }

    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }

    public String getKundenKuerzel() { return kundenKuerzel; }
    public void setKundenKuerzel(String kundenKuerzel) { this.kundenKuerzel = kundenKuerzel; }
}
