package de.creditreform.crefoteam.cte.testsupporttool.gui.design;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.awt.GraphicsEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class TestSupportMainTabPabelTest {

    @Test
    void threeTabsExistInOrder() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestSupportMainTabPabel pane = new TestSupportMainTabPabel();
        // ACTITI-Exporer-Tab entfernt (kein Activiti-Server im Tool-Modus)
        assertThat(pane.getTabCount()).isEqualTo(3);
        assertThat(pane.getTitleAt(0)).isEqualTo("LOG's");
        assertThat(pane.getTitleAt(1)).isEqualTo("Prozess");
        assertThat(pane.getTitleAt(2)).isEqualTo("Test-Results");
    }

    @Test
    void componentAccessors_returnNonNull() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestSupportMainTabPabel pane = new TestSupportMainTabPabel();
        assertThat(pane.getPanelLogs()).isNotNull();
        assertThat(pane.getScrollPanelProcessImage()).isNotNull();
        assertThat(pane.getTextAreaTaskListenerInfo()).isNotNull();
        assertThat(pane.getCheckBoxScrollToEnd()).isNotNull();
        assertThat(pane.getButtonClearLOGPanel()).isNotNull();
        assertThat(pane.getViewTestResults()).isNotNull();
    }

    @Test
    void logTextArea_isNotEditable() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        assertThat(new TestSupportMainTabPabel().getTextAreaTaskListenerInfo().isEditable()).isFalse();
    }

    @Test
    void clearLogButton_hasGermanText() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        assertThat(new TestSupportMainTabPabel().getButtonClearLOGPanel().getText()).isEqualTo("LOG's löschen");
    }
}
