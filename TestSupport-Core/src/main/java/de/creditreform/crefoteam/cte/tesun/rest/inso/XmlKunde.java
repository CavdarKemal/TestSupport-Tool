package de.creditreform.crefoteam.cte.tesun.rest.inso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/** DTO-Pendant aus {@code cte.inso.monitor.xmlbinding}. */
public final class XmlKunde {

    private String firmenName;
    private String kundenKuerzel;
    private Calendar produktivSeit;
    private Calendar prduktivBis;
    private boolean aktiv;
    private Boolean loeschKennzeichen;
    private final List<PgpKey> pgpKeyList = new ArrayList<>();

    public String getFirmenName() { return firmenName; }
    public void setFirmenName(String firmenName) { this.firmenName = firmenName; }

    public String getKundenKuerzel() { return kundenKuerzel; }
    public void setKundenKuerzel(String kundenKuerzel) { this.kundenKuerzel = kundenKuerzel; }

    public Calendar getProduktivSeit() { return produktivSeit; }
    public void setProduktivSeit(Calendar produktivSeit) { this.produktivSeit = produktivSeit; }

    public Calendar getPrduktivBis() { return prduktivBis; }
    public void setPrduktivBis(Calendar prduktivBis) { this.prduktivBis = prduktivBis; }

    public boolean isAktiv() { return aktiv; }
    public void setAktiv(boolean aktiv) { this.aktiv = aktiv; }

    public Boolean isLoeschKennzeichen() { return loeschKennzeichen; }
    public void setLoeschKennzeichen(Boolean loeschKennzeichen) { this.loeschKennzeichen = loeschKennzeichen; }

    public List<PgpKey> getPgpKeyList() { return pgpKeyList; }
}
