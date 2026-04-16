package de.creditreform.crefoteam.cte.testsupporttool.gui.design;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.awt.GraphicsEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class TestResultsPanelTest {

    @Test
    void componentsInitializedAndInitialTabAdded() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestResultsPanel panel = new TestResultsPanel();

        assertThat(panel.getTabbedPaneTestResults()).isNotNull();
        assertThat(panel.getTestResultsTabView()).isNotNull();
        // JFD legt im initComponents() einen initialen Tab an, der die
        // TestResultsTabView enthält — TestResultsView entfernt ihn spaeter im
        // Konstruktor. Hier: Rohform des Designs, noch nicht die View.
        assertThat(panel.getTabbedPaneTestResults().getTabCount()).isEqualTo(1);
    }
}
