package de.creditreform.crefoteam.cte.testsupporttool.gui.design;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.awt.GraphicsEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class CustomersSelectionPanelTest {

    @Test
    void allComponentsInitialized() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        CustomersSelectionPanel panel = new CustomersSelectionPanel();

        assertThat(panel.getSplitPaneMain()).isNotNull();
        assertThat(panel.getSplitPaneCustomerTrees()).isNotNull();
        assertThat(panel.getSplitPaneScenarios()).isNotNull();
        assertThat(panel.getPanelCustomerP1()).isNotNull();
        assertThat(panel.getPanelCustomerP2()).isNotNull();
        assertThat(panel.getTableWithButtonsViewP1()).isNotNull();
        assertThat(panel.getTableWithButtonsViewP2()).isNotNull();
        assertThat(panel.getScrollPaneTreeCustomersP1()).isNotNull();
        assertThat(panel.getScrollPaneTreeCustomers2()).isNotNull();
        assertThat(panel.getTreeCustomersPhase1()).isNotNull();
        assertThat(panel.getTreeCustomersPhase2()).isNotNull();
        assertThat(panel.getPanelScenarios()).isNotNull();
        assertThat(panel.getPanelTestCrefos()).isNotNull();
    }

    @Test
    void threeTableWithButtonsViews_exist() {
        // Phase-1-Customer-Table + Phase-2-Customer-Table + Scenarios + TestCrefos = 4 Tables
        // (ein TWV fuer Phase-1, eins fuer Phase-2, eins fuer Scenarios, eins fuer Crefos).
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        CustomersSelectionPanel panel = new CustomersSelectionPanel();
        assertThat(panel.getTableWithButtonsViewP1()).isNotSameAs(panel.getTableWithButtonsViewP2());
        assertThat(panel.getPanelScenarios()).isNotSameAs(panel.getPanelTestCrefos());
    }
}
