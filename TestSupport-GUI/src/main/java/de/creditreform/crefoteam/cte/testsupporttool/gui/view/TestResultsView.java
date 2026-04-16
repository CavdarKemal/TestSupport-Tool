package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.design.TestResultsPanel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.tesun.util.TestResults;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestResultsView extends TestResultsPanel {
    private final float DIVIDER_LOCATION = 0.2f;

    private final ClosableTabbedPane tabbedPaneTestResults = getTabbedPaneTestResults();
    private TestResultsTabView testResultsTabViewMain;
    private EnvironmentConfig environmentConfig;

    public TestResultsView() {
        this.tabbedPaneTestResults.setTitleAt(0, "Test Results");
        this.tabbedPaneTestResults.removeTabAt(0);
    }

    public void setEnvironmentConfig(EnvironmentConfig environmentConfig) {
        this.environmentConfig = environmentConfig;
    }

    private TestResultsTabView createTestResultsTabView(String tabName) {
        if (environmentConfig == null) {
            throw new RuntimeException("EnvironmentConfig wurde nicht gesetzt! Bitte TestResultsView#setEnvironmentConfig aufrufen!");
        }
        TestResultsTabView testResultsTabView = new TestResultsTabView(this, environmentConfig);
        testResultsTabView.getSplitPanelTreeView().setDividerLocation(DIVIDER_LOCATION);
        tabbedPaneTestResults.addTab(tabName, testResultsTabView);
        tabbedPaneTestResults.setSelectedComponent(testResultsTabView);
        return testResultsTabView;
    }

    public void doLoadTestResultsFromFile() {
        try {
            String newChoosenFileName = GUIStaticUtils.chooseFileName(this, environmentConfig.getTestResultsRoot().getAbsolutePath(), "*.zip", true);
            if ((newChoosenFileName == null)) {
                return;
            }
            Path testResultsFile = Paths.get(newChoosenFileName);
            TestResultsTabView testResultsTabView = createTestResultsTabView(testResultsFile.getFileName().toString());
            testResultsTabView.loadTestResultsFromZipFile(testResultsFile);
        } catch (Exception ex) {
            GUIStaticUtils.showExceptionMessage(this, "Fehler beim Laden!", ex);
        }
    }

    public void refreshTestResultsForMap(Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> testCustomerMapMap, boolean saveResults) {
        try {
            this.environmentConfig = environmentConfig;
            if (testResultsTabViewMain == null) {
                testResultsTabViewMain = createTestResultsTabView("Process-Test");
            }
            testResultsTabViewMain.getSplitPanelTreeView().setDividerLocation(DIVIDER_LOCATION);
            testResultsTabViewMain.initTreeForMap(testCustomerMapMap);
            if (saveResults && isThereSomeTestResults(testCustomerMapMap)) {
                Path testResultsZipPath = Paths.get(environmentConfig.getTestResultsRoot().getAbsolutePath(), "TestResults.zip");
                testResultsTabViewMain.doSaveTestResults(testResultsZipPath);
            }
        } catch (Exception ex) {
            GUIStaticUtils.showExceptionMessage(this, "Fehler beim Aktualisieren!", ex);
        }
    }

    private boolean isThereSomeTestResults(Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> testCustomerMapMap) {
        AtomicBoolean isEmpty = new AtomicBoolean();
        Iterator<TestSupportClientKonstanten.TEST_PHASE> iterator = testCustomerMapMap.keySet().iterator();
        while (iterator.hasNext()) {
            TestSupportClientKonstanten.TEST_PHASE testPhase = iterator.next();
            testCustomerMapMap.get(testPhase).entrySet().forEach(testCustomerEntry -> {
                TestCustomer testCustomer = testCustomerEntry.getValue();
                Map<String, TestResults> testResultsMapForCommands = testCustomer.getTestResultsMapForCommands();
                testResultsMapForCommands.forEach((command, testResults) -> {
                    List<TestResults.ResultInfo> resultInfosList = testResults.getResultInfosList();
                    if (resultInfosList.isEmpty()) {
                        isEmpty.set(true);
                    }
                });
                testCustomer.getTestScenariosMap().forEach((scenarioName, testScenario) -> {
                    Map<String, Map<String, TestResults>> testResultsMapForCommandsScenariosMap = testScenario.getTestResultsMapForCommandsScenariosMap();
                    testResultsMapForCommandsScenariosMap.forEach((s, testResultsMap) -> {
                        testResultsMap.forEach((command, testResults) -> {
                            List<TestResults.ResultInfo> resultInfosList = testResults.getResultInfosList();
                            if (resultInfosList.isEmpty()) {
                                isEmpty.set(true);
                            }
                        });
                    });
                });
            });
        }
        return isEmpty.get();
    }

}
