package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import javax.swing.text.Highlighter;
import java.awt.GraphicsEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class TestResultDefaultViewTest {

    @Test
    void emptyQuery_clearsHighlights() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestResultCommandView view = new TestResultCommandView();
        view.getTextAreaFileSrc().setText("Hello World");
        view.getTextFieldSearch().setText("world");
        view.getTextFieldSearch().setText("");

        Highlighter highlighter = view.getTextAreaFileSrc().getHighlighter();
        assertThat(highlighter.getHighlights()).isEmpty();
    }

    @Test
    void queryWithMatches_highlightsAllOccurrences() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestResultCommandView view = new TestResultCommandView();
        view.getTextAreaFileSrc().setText("foo bar foo baz foo");
        view.getTextFieldSearch().setText("foo");

        Highlighter.Highlight[] highlights = view.getTextAreaFileSrc().getHighlighter().getHighlights();
        assertThat(highlights).hasSize(3);
    }

    @Test
    void queryWithoutMatches_setsRedishBackground() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestResultCommandView view = new TestResultCommandView();
        view.getTextAreaFileSrc().setText("foo bar");
        view.getTextFieldSearch().setText("xyz");
        // Hellrot (255, 200, 200) signalisiert "keine Treffer".
        assertThat(view.getTextFieldSearch().getBackground().getRed()).isGreaterThan(200);
        assertThat(view.getTextFieldSearch().getBackground().getGreen()).isLessThan(220);
    }

    @Test
    void concreteSubclasses_inheritBehavior() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        // Alle vier konkreten Subklassen müssen via Default-Konstruktor bauen.
        assertThat(new TestResultCommandView()).isNotNull();
        assertThat(new TestResultCustomerView()).isNotNull();
        assertThat(new TestResultScenarioView()).isNotNull();
        assertThat(new TestResultCustomersMapView()).isNotNull();
    }
}
