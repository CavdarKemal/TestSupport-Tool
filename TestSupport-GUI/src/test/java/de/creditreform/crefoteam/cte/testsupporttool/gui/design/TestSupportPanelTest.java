package de.creditreform.crefoteam.cte.testsupporttool.gui.design;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.awt.GraphicsEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class TestSupportPanelTest {

    @Test
    void allSubViewsWired() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestSupportPanel panel = new TestSupportPanel();

        assertThat(panel.getSplitPaneMain()).isNotNull();
        assertThat(panel.getPanelLeft()).isNotNull();
        assertThat(panel.getPanelRight()).isNotNull();
        assertThat(panel.getViewTestSupportMainControls()).isNotNull();
        assertThat(panel.getViewCustomersSelection()).isNotNull();
        assertThat(panel.getViewTestSupportMainProcess()).isNotNull();
        assertThat(panel.getTabbedPaneMonitor()).isNotNull();
    }

    @Test
    void leftAndRightPanel_areDistinct() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestSupportPanel panel = new TestSupportPanel();
        assertThat(panel.getPanelLeft()).isNotSameAs(panel.getPanelRight());
    }
}
