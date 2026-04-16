package de.creditreform.crefoteam.cte.testsupporttool.gui.design;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.awt.GraphicsEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class TestResultsTabPanelTest {

    @Test
    void allComponentsInitialized() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestResultsTabPanel panel = new TestResultsTabPanel();

        assertThat(panel.getButtonSaveTestResults()).isNotNull();
        assertThat(panel.getButtonRefreshTestResults()).isNotNull();
        assertThat(panel.getButtonLoadTestResults()).isNotNull();
        assertThat(panel.getSplitPanelTreeView()).isNotNull();
        assertThat(panel.getPanelTestResults()).isNotNull();
        assertThat(panel.getSplitPaneCustomerTrees()).isNotNull();
        assertThat(panel.getPanelCustomerP1()).isNotNull();
        assertThat(panel.getPanelCustomerP2()).isNotNull();
        assertThat(panel.getTreeCustomersPhase1()).isNotNull();
        assertThat(panel.getTreeCustomersPhase2()).isNotNull();
        assertThat(panel.getScrollPaneTreeCustomers1()).isNotNull();
        assertThat(panel.getScrollPaneTreeCustomers2()).isNotNull();
        assertThat(panel.getComboBoxDiffTools()).isNotNull();
        assertThat(panel.getButtonStartDifTool()).isNotNull();
        assertThat(panel.getPanelControls()).isNotNull();
        assertThat(panel.getLabel1()).isNotNull();
    }

    @Test
    void buttonLabels_haveGermanText() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestResultsTabPanel panel = new TestResultsTabPanel();
        assertThat(panel.getButtonRefreshTestResults().getText()).isEqualTo("Aktualisieren");
        assertThat(panel.getButtonLoadTestResults().getText()).isEqualTo("Test Results Laden...");
        assertThat(panel.getButtonSaveTestResults().getText()).isEqualTo("Test Results Speichern");
        assertThat(panel.getButtonStartDifTool().getText()).isEqualTo("Start Diff-Tool");
        assertThat(panel.getLabel1().getText()).isEqualTo("Diff-Tool:");
    }

    @Test
    void buttonsHaveIcons() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestResultsTabPanel panel = new TestResultsTabPanel();
        assertThat(panel.getButtonRefreshTestResults().getIcon()).isNotNull();
        assertThat(panel.getButtonLoadTestResults().getIcon()).isNotNull();
        assertThat(panel.getButtonSaveTestResults().getIcon()).isNotNull();
    }
}
