package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.tesun.util.TestScenario;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TestScenariosTableModelTest {

    @Test
    void columnSetup_matchesConstant() {
        TestScenariosTableModel model = new TestScenariosTableModel(List.of());
        assertThat(model.getColumnCount()).isEqualTo(3);
        assertThat(model.getColumnName(0)).isEqualTo("#");
        assertThat(model.getColumnName(1)).isEqualTo("Scenario");
        assertThat(model.getColumnName(2)).isEqualTo("Crefos");
    }

    @Test
    void getValueAt_returnsAllThreeColumns() {
        TestCustomer customer = new TestCustomer("K", "N");
        TestScenario scenario = new TestScenario(customer, "ScenarioX");
        scenario.setActivated(true);
        TestScenariosTableModel model = new TestScenariosTableModel(List.of(scenario));
        assertThat(model.getValueAt(0, 0)).isEqualTo(true);
        assertThat(model.getValueAt(0, 1)).isEqualTo("ScenarioX");
        assertThat(model.getValueAt(0, 2)).isEqualTo(0);
    }

    @Test
    void setValueAt_scenarioName_updatesScenario() {
        TestCustomer customer = new TestCustomer("K", "N");
        TestScenario scenario = new TestScenario(customer, "Alt");
        TestScenariosTableModel model = new TestScenariosTableModel(List.of(scenario));
        model.setValueAt("Neu", 0, 1);
        assertThat(scenario.getScenarioName()).isEqualTo("Neu");
    }

    @Test
    void setValueAt_crefosColumn_isNoOp() {
        TestCustomer customer = new TestCustomer("K", "N");
        TestScenario scenario = new TestScenario(customer, "Scen");
        TestScenariosTableModel model = new TestScenariosTableModel(List.of(scenario));
        model.setValueAt("42", 0, 2); // darf keinen Fehler werfen und nichts verändern
        assertThat(model.getValueAt(0, 2)).isEqualTo(0);
    }

    @Test
    void isCellEditable_onlyFirstColumn() {
        TestScenariosTableModel model = new TestScenariosTableModel(List.of());
        assertThat(model.isCellEditable(0, 0)).isTrue();
        assertThat(model.isCellEditable(0, 2)).isFalse();
    }
}
