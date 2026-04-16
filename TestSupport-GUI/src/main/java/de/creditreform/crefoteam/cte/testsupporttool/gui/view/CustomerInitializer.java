package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.testsupporttool.util.CustomerUtils;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.apache.log4j.Level;

/**
 * Kapselt die Initialisierung von Kunden und Testquellen.
 * Port aus {@code testsupport_client.tesun.gui.view.CustomerInitializer}.
 *
 * <p>REST-Calls gegen TesunRestService sind als CLAUDE_MODE ausgeklammert.
 * {@code TesunUtilites.dumpCustomers} wird durch
 * {@link CustomerUtils#dumpCustomers(File, String, Map)} ersetzt (gleiches
 * Verhalten, andere Klasse im Tool-Port).
 */
class CustomerInitializer {

    private final TestSupportView view;

    CustomerInitializer(TestSupportView view) {
        this.view = view;
    }

    void initCustomers() {
        try {
            SwingUtilities.invokeLater(() -> {
                GUIStaticUtils.setWaitCursor(view, true);
                view.enableComponentsToOnOff(false);
                view.getViewTestSupportMainProcess().setTestCasesPath("");
                view.getViewCustomersSelection().setTestCustomersTableModelMap(new HashMap<>());
            });
            checkAndSetTestsSource(view.getViewTestSupportMainProcess().getSelectedTestSource());
            initTestCasesForCustomers();
        } catch (Exception ex) {
            view.notifyClientJob(Level.ERROR, GUIStaticUtils.showExceptionMessage(view, "Quelle für ITSQ-Revision ändern", ex));
        } finally {
            SwingUtilities.invokeLater(() -> {
                GUIStaticUtils.setWaitCursor(view, false);
                view.enableComponentsToOnOff(true);
            });
        }
    }

    void checkAndSetTestsSource(String testSetSource) throws Exception {
        // CTEWE-1984::
        File sourceDir = new File(view.currentEnvironment.getTestResourcesRoot(), testSetSource);
        if (sourceDir.exists()) {
            view.currentEnvironment.setTestResourcesDir(sourceDir);
            String testCasesPath = view.currentEnvironment.getItsqRefExportsRoot().getAbsolutePath();
            view.currentEnvironment.setLastTestSource(testSetSource);
            SwingUtilities.invokeLater(() -> view.getViewTestSupportMainProcess().setTestCasesPath(testCasesPath));
        } else {
            GUIStaticUtils.showExceptionMessage(view, "", new RuntimeException("\nDie selektierte Quelle " + sourceDir + " existiert nicht!\nBitte andere Quelle wählen."));
        }
    }

    void initTestCasesForCustomers() throws Exception {
        view.notifyClientJob(Level.INFO, "\n\tLese die Test-Crefos-Konfiguration aus dem ITSQ-Verzeichnis...");
        Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> customerTestInfoMapMap = view.currentEnvironment.getCustomerTestInfoMapMap();
        view.notifyClientJob(Level.INFO, "\n\tErmittle TesunConfigInfo für die Kunden...");
        /* CLAUDE_MODE
         * TesunRestService tesunRestServiceWLS = view.testSupportHelper.getTesunRestServiceWLS();
         * SystemInfo systemInfo = tesunRestServiceWLS.getSystemPropertiesInfo();
         */
        view.notifyClientJob(Level.INFO, "\n\tErmittle KundenKonfigList für die Kunden...");
        for (TestSupportClientKonstanten.TEST_PHASE testPhase : customerTestInfoMapMap.keySet()) {
            Map<String, TestCustomer> testCustomerMap = customerTestInfoMapMap.get(testPhase);
            view.notifyClientJob(Level.INFO, "\n" + testCustomerMap.size() + " Kunden sind für den Test in " + testPhase + " ausgewählt.");
            testCustomerMap.entrySet().forEach(testCustomerEntry -> {
                try {
                    TestCustomer testCustomer = testCustomerEntry.getValue();
                    view.notifyClientJob(Level.INFO, "\n\t\tInitialisiere Testfälle des Kunden für " + testCustomer.getCustomerName() + " aus " + testPhase);
                    /* CLAUDE_MODE
                     * tesunRestServiceWLS.extendTestCustomerProperiesInfos(testCustomer, systemInfo);
                     */
                } catch (Exception ex) {
                    view.notifyClientJob(Level.ERROR, GUIStaticUtils.showExceptionMessage(view, "Konfiguration vervollständigen", ex));
                }
            });
            CustomerUtils.dumpCustomers(view.currentEnvironment.getLogOutputsRoot(), "INIT-" + testPhase.name(), testCustomerMap);
        }
        SwingUtilities.invokeLater(() -> view.getViewCustomersSelection().setTestCustomersTableModelMap(customerTestInfoMapMap));
    }
}
