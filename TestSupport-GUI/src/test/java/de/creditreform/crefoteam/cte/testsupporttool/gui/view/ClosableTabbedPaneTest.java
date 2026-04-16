package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GraphicsEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class ClosableTabbedPaneTest {

    @Test
    void addTab_appendsTrailingSpaces() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        ClosableTabbedPane pane = new ClosableTabbedPane();
        pane.addTab("Tab A", new JLabel("A"));
        // Original: der Titel bekommt "  " angehaengt, damit Platz fuer das Close-X ist.
        assertThat(pane.getTitleAt(0)).endsWith("  ");
        assertThat(pane.getTabTitleAt(0)).isEqualTo("Tab A");
    }

    @Test
    void addTab_preservesComponentIdentity() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        ClosableTabbedPane pane = new ClosableTabbedPane();
        JPanel p = new JPanel();
        pane.addTab("X", p);
        assertThat(pane.getComponentAt(0)).isSameAs(p);
        assertThat(pane.getTabCount()).isEqualTo(1);
    }

    @Test
    void tabAboutToClose_defaultsToTrue() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        ClosableTabbedPane pane = new ClosableTabbedPane();
        assertThat(pane.tabAboutToClose(0)).isTrue();
    }

    @Test
    void getTabTitleAt_returnsTrimmedTitle() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        ClosableTabbedPane pane = new ClosableTabbedPane();
        pane.addTab("Mein Tab", new JPanel());
        // "Mein Tab  " (mit Spaces) → getTabTitleAt trimmt → "Mein Tab"
        assertThat(pane.getTabTitleAt(0)).isEqualTo("Mein Tab");
    }
}
