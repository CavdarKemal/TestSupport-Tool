package de.creditreform.crefoteam.cte.testsupporttool.gui.base.design;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import javax.swing.Icon;
import java.awt.GraphicsEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class TableWithButtonsPanelTest {

    @Test
    @DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
    void allComponents_areInitialized_andButtonsCarryIcons() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");

        TableWithButtonsPanel panel = new TableWithButtonsPanel();

        assertThat(panel.getScrollPane()).isNotNull();
        assertThat(panel.getTable()).isNotNull();
        assertThat(panel.getPanelButtons()).isNotNull();
        assertThat(panel.getLabelTitle()).isNotNull();
        assertThat(panel.getButtonSelectAll()).isNotNull();
        assertThat(panel.getButtonSelectNone()).isNotNull();
        assertThat(panel.getButtonSelectInvert()).isNotNull();

        // Icons sind als Resource ausm Classpath geladen — Resource-URL muss
        // aufloesen, sonst NPE im Konstruktor (Konstruktor-Smoke-Test).
        Icon allIcon = panel.getButtonSelectAll().getIcon();
        Icon noneIcon = panel.getButtonSelectNone().getIcon();
        Icon invIcon = panel.getButtonSelectInvert().getIcon();
        assertThat(allIcon).isNotNull();
        assertThat(noneIcon).isNotNull();
        assertThat(invIcon).isNotNull();
        assertThat(allIcon.getIconWidth()).isPositive();
    }

    @Test
    @DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
    void table_hasSingleSelectionAndAutoRowSorter() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TableWithButtonsPanel panel = new TableWithButtonsPanel();

        assertThat(panel.getTable().getSelectionModel().getSelectionMode())
                .isEqualTo(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        assertThat(panel.getTable().getAutoCreateRowSorter()).isTrue();
    }

    @Test
    @DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
    void buttonsHaveTooltipsAndFixedSize() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TableWithButtonsPanel panel = new TableWithButtonsPanel();

        assertThat(panel.getButtonSelectAll().getToolTipText()).isEqualTo("Alle selektieren");
        assertThat(panel.getButtonSelectNone().getToolTipText()).isEqualTo("Selektion löschen");
        assertThat(panel.getButtonSelectInvert().getToolTipText()).isEqualTo("Selektion umkehren");
        assertThat(panel.getButtonSelectAll().getPreferredSize().width).isEqualTo(35);
        assertThat(panel.getButtonSelectAll().getPreferredSize().height).isEqualTo(30);
    }
}
