package de.creditreform.crefoteam.cte.testsupporttool.gui.design;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.awt.GraphicsEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class TestSupportMainProcessPanelTest {

    @Test
    void allComponentsInitialized() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestSupportMainProcessPanel panel = new TestSupportMainProcessPanel();

        assertThat(panel.getComboBoxTestType()).isNotNull();
        assertThat(panel.getComboBoxTestSource()).isNotNull();
        assertThat(panel.getComboBoxITSQRevision()).isNotNull();
        assertThat(panel.getCheckBoxDemoMode()).isNotNull();
        assertThat(panel.getButtonStartProcess()).isNotNull();
        assertThat(panel.getButtonStopUserTasksThread()).isNotNull();
        assertThat(panel.getTextFieldTestCasesPath()).isNotNull();
        assertThat(panel.getCheckBoxUploadSynthetics()).isNotNull();
        assertThat(panel.getCheckBoxUseOnlyTestCLZs()).isNotNull();
        assertThat(panel.getComboBoxTestJobs()).isNotNull();
        assertThat(panel.getComboBoxTestPhase()).isNotNull();
        assertThat(panel.getButtonStartTestJob()).isNotNull();
        assertThat(panel.getTextFieldJobParams()).isNotNull();
        assertThat(panel.getSeparator1()).isNotNull();
    }

    @Test
    void buttons_haveGermanTextsAndIcons() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestSupportMainProcessPanel panel = new TestSupportMainProcessPanel();
        assertThat(panel.getButtonStartProcess().getText()).isEqualTo("Prozess starten...");
        assertThat(panel.getButtonStartProcess().getIcon()).isNotNull();
        assertThat(panel.getButtonStopUserTasksThread().getIcon()).isNotNull();
        assertThat(panel.getButtonStartTestJob().getText()).isEqualTo("JOB starten...");
        assertThat(panel.getCheckBoxDemoMode().getText()).isEqualTo("Demo Mode");
    }

    @Test
    void testCasesPath_isReadOnly() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        assertThat(new TestSupportMainProcessPanel().getTextFieldTestCasesPath().isEditable()).isFalse();
    }

    @Test
    void fachwertAndExportFormatLabels_exist() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestSupportMainProcessPanel panel = new TestSupportMainProcessPanel();
        assertThat(panel.getLabelFachwertConfig().getText()).isEqualTo("FW-Konfiguration:");
        assertThat(panel.getLabelExportFormat().getText()).isEqualTo("Export-Format:");
        assertThat(panel.getRadioButtonFWConfigNewest()).isNotNull();
        assertThat(panel.getRadioButtonFWConfigLikePRE()).isNotNull();
        assertThat(panel.getRadioButtonExportFormatNewest()).isNotNull();
        assertThat(panel.getRadioButtonExportFormatLikePRE()).isNotNull();
    }
}
