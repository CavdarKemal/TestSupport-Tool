package de.creditreform.crefoteam.cte.testsupporttool.gui.base.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.ColumnsInfo;
import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.CteAbstractTableModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class TableWithButtonsViewTest {

    /** Test-Modell mit 3 Zeilen + Activated-Flag-Tracking. */
    private static final class FlagModel extends CteAbstractTableModel {
        private final boolean[] activated;
        FlagModel() {
            super(List.of("a", "b", "c"));
            this.COL_NAMES = new String[]{"Wert"};
            this.activated = new boolean[3];
        }
        @Override public boolean isRowActivated(int rowIndex) {
            return activated[rowIndex];
        }
        @Override public void setRowActivated(int rowIndex, boolean isActivated) {
            activated[rowIndex] = isActivated;
        }
        @Override public Object getValueAt(int row, int col) { return getRow(row); }
    }

    @Test
    @DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
    void selectAllButton_activatesAllRows() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TableWithButtonsView view = new TableWithButtonsView();
        FlagModel model = new FlagModel();
        view.setModel("Title", model, new ColumnsInfo[]{new ColumnsInfo(10, 50, 100)});

        view.getButtonSelectAll().doClick();

        assertThat(model.isRowActivated(0)).isTrue();
        assertThat(model.isRowActivated(1)).isTrue();
        assertThat(model.isRowActivated(2)).isTrue();
    }

    @Test
    @DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
    void selectNoneButton_deactivatesAllRows() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TableWithButtonsView view = new TableWithButtonsView();
        FlagModel model = new FlagModel();
        view.setModel("T", model, new ColumnsInfo[]{new ColumnsInfo(10, 50, 100)});

        view.getButtonSelectAll().doClick();
        view.getButtonSelectNone().doClick();

        assertThat(model.isRowActivated(0)).isFalse();
        assertThat(model.isRowActivated(2)).isFalse();
    }

    @Test
    @DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
    void selectInvertButton_togglesEachRow() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TableWithButtonsView view = new TableWithButtonsView();
        FlagModel model = new FlagModel();
        view.setModel("T", model, new ColumnsInfo[]{new ColumnsInfo(10, 50, 100)});

        // Setze Row 0 aktiviert, Row 1+2 deaktiviert
        model.setRowActivated(0, true);

        view.getButtonSelectInvert().doClick();

        assertThat(model.isRowActivated(0)).isFalse();
        assertThat(model.isRowActivated(1)).isTrue();
        assertThat(model.isRowActivated(2)).isTrue();
    }

    @Test
    @DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
    void setModel_emptyTitle_hidesButtons_setsModel_andSelectsFirstRow() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TableWithButtonsView view = new TableWithButtonsView();
        FlagModel model = new FlagModel();

        view.setModel("", model, new ColumnsInfo[]{new ColumnsInfo(10, 50, 100)});

        // Buttons sollten ausgeblendet sein
        assertThat(view.getPanelButtons().isVisible()).isFalse();
        // Modell ist gesetzt
        assertThat(view.getModel()).isSameAs(model);
        // Erste Zeile selektiert
        assertThat(view.getTable().getSelectedRow()).isZero();
    }

    @Test
    @DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
    void getSelectedRow_minusOne_whenNoSelection() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TableWithButtonsView view = new TableWithButtonsView();
        view.getTable().clearSelection();
        assertThat(view.getSelectedRow()).isEqualTo(-1);
    }

    @Test
    @DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
    void addListSelectionListener_replacesPreviousListener() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TableWithButtonsView view = new TableWithButtonsView();
        List<String> firedFirst = new ArrayList<>();
        List<String> firedSecond = new ArrayList<>();
        view.addListSelectionListener(e -> firedFirst.add("1"));
        view.addListSelectionListener(e -> firedSecond.add("2"));

        // Trigger ueber model-update
        FlagModel model = new FlagModel();
        view.setModel("T", model, new ColumnsInfo[]{new ColumnsInfo(10, 50, 100)});
        view.getTable().setRowSelectionInterval(1, 1);

        assertThat(firedFirst).isEmpty();
        assertThat(firedSecond).isNotEmpty();
    }

    @Test
    @DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
    void enableButtons_andShowButtons_togglePanelState() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TableWithButtonsView view = new TableWithButtonsView();

        view.enableButtons(false);
        assertThat(view.getPanelButtons().isEnabled()).isFalse();

        view.showButtons(true);
        assertThat(view.getPanelButtons().isVisible()).isTrue();
        assertThat(view.getPanelButtons().isEnabled()).isTrue();
    }
}
