package de.creditreform.crefoteam.cte.testsupporttool.gui.base.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CteAbstractTableModelTest {

    /** Konkrete Sub-Klasse fuer Tests — Row = String, COL_NAMES nach Wahl. */
    private static final class StringTableModel extends CteAbstractTableModel {
        private boolean[] activated;

        StringTableModel() {
            super();
            this.COL_NAMES = new String[]{"Wert"};
            this.activated = new boolean[0];
        }

        StringTableModel(List<?> initial) {
            super(initial);
            this.COL_NAMES = new String[]{"Wert"};
            this.activated = new boolean[initial == null ? 0 : initial.size()];
        }

        @Override public boolean isRowActivated(int rowIndex) {
            return rowIndex >= 0 && rowIndex < activated.length && activated[rowIndex];
        }

        @Override public void setRowActivated(int rowIndex, boolean isActivated) {
            if (rowIndex >= activated.length) {
                boolean[] nw = new boolean[rowIndex + 1];
                System.arraycopy(activated, 0, nw, 0, activated.length);
                activated = nw;
            }
            activated[rowIndex] = isActivated;
        }

        @Override public Object getValueAt(int row, int col) {
            Object r = getRow(row);
            return r;
        }
    }

    @Test
    void emptyConstructor_initializesEmptyData() {
        StringTableModel m = new StringTableModel();
        assertThat(m.getRowCount()).isZero();
        assertThat(m.getColumnCount()).isEqualTo(1);
        assertThat(m.getColumnName(0)).isEqualTo("Wert");
    }

    @Test
    void getColumnName_returnsFallbackForOutOfRange() {
        StringTableModel m = new StringTableModel();
        assertThat(m.getColumnName(99)).isEqualTo("Col 99");
        assertThat(m.getColumnName(-1)).isEqualTo("Col -1");
    }

    @Test
    void getColumnClass_defaultsToStringWhenEmpty() {
        StringTableModel m = new StringTableModel();
        assertThat(m.getColumnClass(0)).isEqualTo(String.class);
    }

    @Test
    void initialDataConstructor_storesAllRows() {
        StringTableModel m = new StringTableModel(List.of("a", "b", "c"));
        assertThat(m.getRowCount()).isEqualTo(3);
        assertThat(m.getRow(0)).isEqualTo("a");
        assertThat(m.getRow(2)).isEqualTo("c");
        assertThat(m.getRow(99)).isNull();
    }

    @Test
    void addRow_appendsAndFiresInsert() {
        StringTableModel m = new StringTableModel();
        int[] firedFirst = {-1};
        m.addTableModelListener(e -> firedFirst[0] = e.getFirstRow());

        m.addRow("x");

        assertThat(m.getRowCount()).isEqualTo(1);
        assertThat(firedFirst[0]).isZero();
    }

    @Test
    void addRows_array_appendsAllAndFiresInsert() {
        StringTableModel m = new StringTableModel(List.of("a"));
        m.addRows(new Object[]{"b", "c"});
        assertThat(m.getRowCount()).isEqualTo(3);
    }

    @Test
    void addRows_list_appendsAllAndFiresInsert() {
        StringTableModel m = new StringTableModel();
        m.addRows(List.of("a", "b"));
        assertThat(m.getRowCount()).isEqualTo(2);
    }

    @Test
    void removeRow_removesByEqualityAndFiresDelete() {
        StringTableModel m = new StringTableModel(List.of("a", "b", "c"));
        m.removeRow("b");
        assertThat(m.getRowCount()).isEqualTo(2);
        assertThat(m.getRow(0)).isEqualTo("a");
        assertThat(m.getRow(1)).isEqualTo("c");
    }

    @Test
    void replaceRow_swapsByEqualityAndFiresUpdate() {
        StringTableModel m = new StringTableModel(List.of("a", "b", "c"));
        m.replaceRow("b", "B");
        assertThat(m.getRow(1)).isEqualTo("B");
    }

    @Test
    void clearTable_emptiesAndFiresDelete() {
        StringTableModel m = new StringTableModel(List.of("a", "b"));
        m.clearTable();
        assertThat(m.getRowCount()).isZero();
    }

    @Test
    void setObjectAt_updatesAndFiresUpdate() {
        StringTableModel m = new StringTableModel(List.of("a", "b", "c"));
        m.setObjectAt("X", 1);
        assertThat(m.getRow(1)).isEqualTo("X");
        // Out-of-range tut nichts.
        m.setObjectAt("Y", 99);
        assertThat(m.getRowCount()).isEqualTo(3);
    }

    @Test
    void rowActivatedFlag_persistsAcrossCalls() {
        StringTableModel m = new StringTableModel(List.of("a", "b"));
        assertThat(m.isRowActivated(0)).isFalse();
        m.setRowActivated(0, true);
        assertThat(m.isRowActivated(0)).isTrue();
        assertThat(m.isRowActivated(1)).isFalse();
    }
}
