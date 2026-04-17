package de.creditreform.crefoteam.cte.tesun.rest.dto;

import de.creditreform.crefoteam.cte.restservices.tesun.xmlbinding.configinfo.TesunConfigExportInfo;
import de.creditreform.crefoteam.cte.restservices.tesun.xmlbinding.configinfo.TesunConfigInfo;
import de.creditreform.crefoteam.cte.restservices.tesun.xmlbinding.configinfo.TesunConfigUploadInfo;
import de.creditreform.crefoteam.cte.restservices.tesun.xmlbinding.environmentproperties.CteEnvironmentPropertiesTupel;
import de.creditreform.crefoteam.cte.restservices.xmlbinding.fachwertaktualisierung.KundenKonfig;
import de.creditreform.crefoteam.cte.restservices.xmlbinding.fachwertaktualisierung.KundenKonfigList;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Aggregiertes Ergebnis von {@code TesunRestService.getSystemPropertiesInfo()} —
 * Port von {@code testsupport_client.tesun_factory.rest.SystemInfo}, nutzt JAXB-Typen.
 */
public class SystemInfo {

    private KundenKonfigList kundenKonfigList;
    private TesunConfigInfo tesunConfigInfo;
    private List<CteEnvironmentPropertiesTupel> envPropsList = new ArrayList<>();

    public void setKundenKonfigList(KundenKonfigList v) { this.kundenKonfigList = v; }
    public void setTesunConfigInfo(TesunConfigInfo v) { this.tesunConfigInfo = v; }
    public void setEnvPropsList(List<CteEnvironmentPropertiesTupel> v) {
        this.envPropsList = v != null ? v : new ArrayList<>();
    }

    public void fillPropertyPairForCustomer(String propertyPrefix, MutablePair<String, String> pair) {
        if (pair.getLeft().contains("%")) {
            pair.setLeft(pair.getLeft().replace("%", propertyPrefix));
        }
        for (CteEnvironmentPropertiesTupel t : envPropsList) {
            if (t.getKey().equalsIgnoreCase(pair.getLeft())) {
                pair.setRight(t.getValue());
                return;
            }
        }
    }

    public TesunConfigExportInfo findTesunConfigExportInfoForCustomer(TestCustomer tc) {
        if (tesunConfigInfo == null) return null;
        for (TesunConfigExportInfo info : tesunConfigInfo.getExportPfade()) {
            if (info.getKundenKuerzel() != null && info.getKundenKuerzel().startsWith(tc.getJvmName())) {
                return info;
            }
        }
        return null;
    }

    public TesunConfigUploadInfo findTesunConfigUploadInfoForCustomer(TestCustomer tc) {
        if (tesunConfigInfo == null) return null;
        for (TesunConfigUploadInfo info : tesunConfigInfo.getUploadPfade()) {
            if (info.getKundenKuerzel() != null && info.getKundenKuerzel().startsWith(tc.getJvmName())) {
                return info;
            }
        }
        return null;
    }

    public KundenKonfig findFachwertconfigInfoForCustomer(TestCustomer tc) {
        if (kundenKonfigList != null) {
            for (KundenKonfig k : kundenKonfigList.getKonfigs()) {
                if (k.getProzessName() != null && k.getProzessName().equalsIgnoreCase(tc.getProcessIdentifier())) {
                    return k;
                }
            }
        }
        KundenKonfig fallback = new KundenKonfig();
        fallback.setAktualisierungsdatum(Calendar.getInstance());
        fallback.setPdversion("???");
        return fallback;
    }
}
