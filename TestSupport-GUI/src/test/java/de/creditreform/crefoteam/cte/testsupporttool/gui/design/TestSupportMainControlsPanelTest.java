package de.creditreform.crefoteam.cte.testsupporttool.gui.design;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.awt.GraphicsEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class TestSupportMainControlsPanelTest {

    @Test
    void allHostComboBoxesAndButtonsInitialized() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestSupportMainControlsPanel panel = new TestSupportMainControlsPanel();

        assertThat(panel.getComboBoxEnvironment()).isNotNull();
        assertThat(panel.getButtonRefreshEnvironment()).isNotNull();
        assertThat(panel.getButtonManageJVMs()).isNotNull();
        assertThat(panel.getComboBoxRestServicesHost()).isNotNull();
        assertThat(panel.getComboBoxBatchGUIHost()).isNotNull();
        assertThat(panel.getComboBoxImpCycleHost()).isNotNull();
        assertThat(panel.getComboBoxInsoHost()).isNotNull();
        assertThat(panel.getComboBoxInsoBackEndHost()).isNotNull();
    }

    @Test
    void labels_haveGermanTexts() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestSupportMainControlsPanel panel = new TestSupportMainControlsPanel();
        assertThat(panel.getLabelEnvironment().getText()).isEqualTo("Umgebung:");
        assertThat(panel.getLabelRestServicesHost().getText()).isEqualTo("RestServices-Host:");
        assertThat(panel.getLabelBatchGUIHost().getText()).isEqualTo("Batch-GUI-Host:");
        assertThat(panel.getLabelImpCycleHost().getText()).isEqualTo("ImportCycle-Host:");
        assertThat(panel.getLabelInsoHost().getText()).isEqualTo("INSO-Host:");
        assertThat(panel.getLabelInsoBackEndHost().getText()).isEqualTo("INSO-Backend-Host:");
    }

    @Test
    void refreshAndJvmButtons_haveIcons() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestSupportMainControlsPanel panel = new TestSupportMainControlsPanel();
        assertThat(panel.getButtonRefreshEnvironment().getIcon()).isNotNull();
        assertThat(panel.getButtonManageJVMs().getIcon()).isNotNull();
    }
}
