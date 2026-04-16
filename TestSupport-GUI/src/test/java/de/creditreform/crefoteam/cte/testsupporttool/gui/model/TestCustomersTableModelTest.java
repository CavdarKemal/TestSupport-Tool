package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TestCustomersTableModelTest {

    @Test
    void columnSetup_matchesConstant() {
        TestCustomersTableModel model = new TestCustomersTableModel(List.of());
        assertThat(model.getColumnCount()).isEqualTo(2);
        assertThat(model.getColumnName(0)).isEqualTo("#");
        assertThat(model.getColumnName(1)).isEqualTo("Kunde");
    }

    @Test
    void getValueAt_returnsActivatedAndCustomerName() {
        TestCustomer c = new TestCustomer("KEY", "Kundenname");
        c.setActivated(true);
        TestCustomersTableModel model = new TestCustomersTableModel(List.of(c));
        assertThat(model.getValueAt(0, 0)).isEqualTo(true);
        assertThat(model.getValueAt(0, 1)).isEqualTo("Kundenname");
    }

    @Test
    void getValueAt_outOfRange_returnsEmptyString() {
        TestCustomersTableModel model = new TestCustomersTableModel(List.of());
        assertThat(model.getValueAt(0, 0)).isEqualTo("");
        assertThat(model.getValueAt(99, 0)).isEqualTo("");
    }

    @Test
    void isCellEditable_onlyFirstColumn() {
        TestCustomersTableModel model = new TestCustomersTableModel(List.of());
        assertThat(model.isCellEditable(0, 0)).isTrue();
        assertThat(model.isCellEditable(0, 1)).isFalse();
    }

    @Test
    void setValueAt_activationFlag_updatesCustomer() {
        TestCustomer c = new TestCustomer("KEY", "Name");
        TestCustomersTableModel model = new TestCustomersTableModel(List.of(c));
        model.setValueAt("true", 0, 0);
        assertThat(c.isActivated()).isTrue();
        model.setValueAt("false", 0, 0);
        assertThat(c.isActivated()).isFalse();
    }

    @Test
    void setValueAt_customerNameColumn_updatesCustomer() {
        TestCustomer c = new TestCustomer("KEY", "Alt");
        TestCustomersTableModel model = new TestCustomersTableModel(List.of(c));
        model.setValueAt("Neu", 0, 1);
        assertThat(c.getCustomerName()).isEqualTo("Neu");
    }

    @Test
    void isRowActivated_reflectsCustomerState() {
        TestCustomer a = new TestCustomer("A", "A");
        a.setActivated(false);
        TestCustomer b = new TestCustomer("B", "B");
        b.setActivated(true);
        TestCustomersTableModel model = new TestCustomersTableModel(Arrays.asList(a, b));
        assertThat(model.isRowActivated(0)).isFalse();
        assertThat(model.isRowActivated(1)).isTrue();
    }
}
