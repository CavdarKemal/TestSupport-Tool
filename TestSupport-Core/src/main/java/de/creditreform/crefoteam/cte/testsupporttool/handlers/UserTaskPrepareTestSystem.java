package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.rest.TesunRestService;
import de.creditreform.crefoteam.cte.tesun.rest.dto.CteEnvironmentProperties;
import de.creditreform.crefoteam.cte.tesun.rest.dto.CteEnvironmentPropertiesTupel;
import de.creditreform.crefoteam.cte.tesun.rest.inso.PgpKey;
import de.creditreform.crefoteam.cte.tesun.rest.inso.XmlKunde;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskRunnable;
import de.creditreform.crefoteam.cte.testsupporttool.util.CustomerUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.log4j.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Port aus {@code testsupport_client.tesun_activiti.handlers}. Schreibt die
 * Environment-Properties (CLZ-Listen) für Phase 2 und prüft, dass der
 * INSO-Test-Tool-Kunde sauber konfiguriert ist.
 */
public class UserTaskPrepareTestSystem extends AbstractUserTaskRunnable {
    public static final String COMMAND = "UserTask PREPARE_TEST_SYSTEM";

    protected TesunRestService tesunRestServiceWLS;
    protected TesunRestService tesunRestServiceJvmInsoBackend;

    public UserTaskPrepareTestSystem(EnvironmentConfig environmentConfig, TesunClientJobListener listener) throws PropertiesException {
        super(environmentConfig, listener);
        this.tesunRestServiceJvmInsoBackend = new TesunRestService(environmentConfig.getRestServiceConfigsForJvmInsoBackend().get(0), listener);
        this.tesunRestServiceWLS = new TesunRestService(environmentConfig.getRestServiceConfigsForMasterkonsole().get(0), listener);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> runTask(Map<String, Object> taskVariablesMap) throws Exception {
        Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> customersMapMap = (Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>>)taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_ACTIVE_CUSTOMERS);
        Map<String, TestCustomer> customersPhase2 = customersMapMap.get(TestSupportClientKonstanten.TEST_PHASE.PHASE_2);
        appendInsoCustomers(customersPhase2);
        boolean onlyTestCrefos = Boolean.parseBoolean(String.valueOf(taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_USE_ONLY_TEST_CLZ)));

        CustomerUtils.dumpCustomers(environmentConfig.getLogOutputsRoot(), "VOR-" + COMMAND, customersPhase2);

        tesunRestServiceWLS.restoreEnvironmentProperties();
        Map<String, MutablePair<String, String>> uniquePairsMap = new TreeMap<>();
        List<CteEnvironmentPropertiesTupel> tupelList = new ArrayList<>();
        for (TestCustomer testCustomer : customersPhase2.values()) {
            if (testCustomer.getCustomerKey().contains("INSO")) {
                checkInsoKunde();
            }
            for (MutablePair<String, String> pair : testCustomer.getPropertyPairsList()) {
                if (pair.getLeft().endsWith(".vc")) {
                    extendVcList(pair, onlyTestCrefos);
                }
                if (!uniquePairsMap.containsKey(pair.getLeft())) {
                    CteEnvironmentPropertiesTupel t = new CteEnvironmentPropertiesTupel();
                    t.setKey(pair.getLeft());
                    t.setValue(pair.getRight());
                    t.setDbOverride(true);
                    tupelList.add(t);
                    uniquePairsMap.put(pair.getLeft(), pair);
                }
            }
        }
        // XSD-Validierung erzwingen
        CteEnvironmentPropertiesTupel xsd = new CteEnvironmentPropertiesTupel();
        xsd.setKey("cte_cta_validation.mode");
        xsd.setValue("VALIDIERE_XSD_SCHEMA");
        xsd.setDbOverride(true);
        tupelList.add(xsd);

        CustomerUtils.dumpCustomers(environmentConfig.getLogOutputsRoot(), "NACH-" + COMMAND, customersPhase2);
        for (String key : uniquePairsMap.keySet()) {
            notifyUserTask(Level.INFO, "\nSetze Property '" + key + "' auf '" + uniquePairsMap.get(key).getRight() + "'");
        }
        notifyUserTask(Level.INFO, "\nSpeichere die erweiterten Properties in die CTE-Datenbank...");
        CteEnvironmentProperties props = new CteEnvironmentProperties();
        props.getProperties().addAll(tupelList);
        tesunRestServiceWLS.setEnvironmentProperties(props);
        return taskVariablesMap;
    }

    private void extendVcList(MutablePair<String, String> pair, boolean onlyTestCrefos) throws PropertiesException {
        String value = pair.getRight() != null ? pair.getRight() : "";
        boolean containsALL = value.contains(",ALL") || value.equals("ALL");
        boolean containsAT = value.contains("AT") || containsALL;
        boolean containsLU = value.contains("LU") || containsALL;
        if (onlyTestCrefos) {
            pair.setRight(TestSupportClientKonstanten.TEST_CLZ_412);
        } else {
            pair.setRight(value);
        }
        if (containsAT) {
            pair.setRight(pair.getRight() + "," + environmentConfig.getTargetClzForAtPseudoCrefos());
        }
        if (containsLU) {
            pair.setRight(pair.getRight() + "," + environmentConfig.getTargetClzForLuPseudoCrefos());
        }
    }

    private Map<String, TestCustomer> appendInsoCustomers(Map<String, TestCustomer> selectedCustomersMap) {
        Map<String, TestCustomer> copy = new HashMap<>(selectedCustomersMap);
        for (String key : copy.keySet()) {
            if (key.startsWith("INSO")) {
                TestCustomer inso = copy.get(key);
                copy.put("INSOMON2T", TestCustomer.cloneInsoMonitorPhase2(inso, "inso.deltaExportKundeDaily", "inso.deltaUploadKunde", "EXPORT_CTE_TO_INSO_2T"));
                copy.put("INSOMON2W", TestCustomer.cloneInsoMonitorPhase2(inso, "inso.deltaExportKundeWeekly", "inso.deltaUploadKunde", "EXPORT_CTE_TO_INSO_2W"));
                copy.put("INSOMON2M", TestCustomer.cloneInsoMonitorPhase2(inso, "inso.deltaExportKundeMonthly", "inso.deltaUploadKunde", "EXPORT_CTE_TO_INSO_2M"));
                break;
            }
        }
        return copy;
    }

    private void checkInsoKunde() {
        try {
            XmlKunde k = tesunRestServiceJvmInsoBackend.readInsoKunde("Test-Tool");
            StringBuilder problems = new StringBuilder();
            if (k == null) {
                throw new RuntimeException("INSO-Kunde 'Test-Tool' wurde nicht konfiguriert!");
            }
            if (!"Test-Tool-Company".equals(k.getFirmenName())) problems.append("\nFirmenname wurde geändert!");
            if (!"Test-Tool".equals(k.getKundenKuerzel())) problems.append("\nKunden-Kürzel wurde geändert!");
            if (k.getProduktivSeit() == null) problems.append("\nProduktiv-Seit wurde geändert!");
            if (k.getPrduktivBis() != null) problems.append("\nProduktiv-Bis wurde geändert!");
            if (!k.isAktiv()) problems.append("\nAktiv wurde geändert!");
            if (k.isLoeschKennzeichen() == null || k.isLoeschKennzeichen()) problems.append("\nLoeschKennzeichen wurde geändert!");
            List<PgpKey> pgpKeys = k.getPgpKeyList();
            if (pgpKeys.isEmpty()) {
                problems.append("\nPGP-Keys wurde geändert!");
            } else if (!"zew_pgp_public_key.asc".equals(pgpKeys.get(0).getFileName())) {
                problems.append("\nPGP-Key wurde geändert!");
            }
            if (problems.length() > 0) {
                throw new RuntimeException("INSO-Kunde 'Test-Tool' passt nicht zum Test-Tool!\nBitte die Kundenkonfiguration anpassen!" + problems);
            }
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception ex) {
            throw new RuntimeException("INSO-Kunde-Check fehlgeschlagen: " + ex.getMessage(), ex);
        }
    }
}
