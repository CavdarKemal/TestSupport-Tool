package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class TestSupportMainTabViewTest {

    @Test
    void appendToConsole_addsMessageToTextArea() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestSupportMainTabView view = new TestSupportMainTabView();
        view.appendToConsole("Hallo Welt");
        SwingUtilities.invokeAndWait(() -> { /* flush EDT */ });
        assertThat(view.getTextAreaTaskListenerInfo().getText()).contains("Hallo Welt");
    }

    @Test
    void appendToConsole_replacesTabsWithSpaces() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestSupportMainTabView view = new TestSupportMainTabView();
        view.appendToConsole("A\tB\tC");
        SwingUtilities.invokeAndWait(() -> { });
        assertThat(view.getTextAreaTaskListenerInfo().getText()).contains("A  B  C");
    }

    @Test
    void appendToConsole_ignoresNullSilently() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestSupportMainTabView view = new TestSupportMainTabView();
        view.appendToConsole(null);
        SwingUtilities.invokeAndWait(() -> { });
        assertThat(view.getTextAreaTaskListenerInfo().getText()).isEmpty();
    }

    @Test
    void clearLogButton_emptiesTextArea() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestSupportMainTabView view = new TestSupportMainTabView();
        view.appendToConsole("Vor dem Loeschen");
        SwingUtilities.invokeAndWait(() -> { });
        view.getButtonClearLOGPanel().doClick();
        assertThat(view.getTextAreaTaskListenerInfo().getText()).isEmpty();
    }
}
