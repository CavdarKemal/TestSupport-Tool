package de.creditreform.crefoteam.cte.testsupporttool.gui.design;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.awt.GraphicsEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class TestResultDiffsPanelTest {

    @Test
    void allComponentsInitialized() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestResultDiffsPanel panel = new TestResultDiffsPanel();

        assertThat(panel.getSplitPanelDifferences()).isNotNull();
        assertThat(panel.getPanelScrDifFile()).isNotNull();
        assertThat(panel.getLabelScfFilePath()).isNotNull();
        assertThat(panel.getScrollPaneSrcFile()).isNotNull();
        assertThat(panel.getTextAreaFileSrc()).isNotNull();
        assertThat(panel.getPanelDstFile()).isNotNull();
        assertThat(panel.getLabelDstFilePath()).isNotNull();
        assertThat(panel.getScrollPaneDstFile()).isNotNull();
        assertThat(panel.getTextAreaFileDst()).isNotNull();
        assertThat(panel.getLabelDiffFilePath()).isNotNull();
        assertThat(panel.getPanelControls()).isNotNull();
        assertThat(panel.getTextFieldDiffFilePath()).isNotNull();
    }

    @Test
    void textAreas_areNotEditable() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestResultDiffsPanel panel = new TestResultDiffsPanel();
        assertThat(panel.getTextAreaFileSrc().isEditable()).isFalse();
        assertThat(panel.getTextAreaFileDst().isEditable()).isFalse();
        assertThat(panel.getTextFieldDiffFilePath().isEditable()).isFalse();
    }

    @Test
    void diffFileLabel_hasGermanText() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        assertThat(new TestResultDiffsPanel().getLabelDiffFilePath().getText()).isEqualTo("Diff-Datei: ");
    }
}
