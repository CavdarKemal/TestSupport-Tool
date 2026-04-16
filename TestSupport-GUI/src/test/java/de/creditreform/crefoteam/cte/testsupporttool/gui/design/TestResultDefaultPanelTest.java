package de.creditreform.crefoteam.cte.testsupporttool.gui.design;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.awt.GraphicsEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class TestResultDefaultPanelTest {

    @Test
    void allComponentsInitialized() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestResultDefaultPanel panel = new TestResultDefaultPanel();

        assertThat(panel.getScrollPaneSrcFile()).isNotNull();
        assertThat(panel.getTextAreaFileSrc()).isNotNull();
        assertThat(panel.getPanelControls()).isNotNull();
        assertThat(panel.getLabelSearch()).isNotNull();
        assertThat(panel.getTextFieldSearch()).isNotNull();
        assertThat(panel.getButtonNextMath()).isNotNull();
        assertThat(panel.getButtonPrevMatch()).isNotNull();
    }

    @Test
    void textArea_isNotEditable() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        assertThat(new TestResultDefaultPanel().getTextAreaFileSrc().isEditable()).isFalse();
    }

    @Test
    void searchLabel_hasGermanText() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        assertThat(new TestResultDefaultPanel().getLabelSearch().getText()).isEqualTo("Suche:");
    }

    @Test
    void navigationButtons_haveIcons() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestResultDefaultPanel panel = new TestResultDefaultPanel();
        assertThat(panel.getButtonNextMath().getIcon()).isNotNull();
        assertThat(panel.getButtonPrevMatch().getIcon()).isNotNull();
    }
}
