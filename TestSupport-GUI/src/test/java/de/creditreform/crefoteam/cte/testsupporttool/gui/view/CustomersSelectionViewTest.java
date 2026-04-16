package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.model.TestCustomersTableModel;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.awt.GraphicsEnvironment;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class CustomersSelectionViewTest {

    @Test
    void setTestCustomersTableModelMap_populatesBothPhaseTables() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        Map<String, TestCustomer> p1 = new LinkedHashMap<>();
        p1.put("A", new TestCustomer("A", "CustA"));
        p1.put("B", new TestCustomer("B", "CustB"));

        Map<String, TestCustomer> p2 = new LinkedHashMap<>();
        p2.put("A", new TestCustomer("A", "CustA"));

        Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> map = new TreeMap<>();
        map.put(TestSupportClientKonstanten.TEST_PHASE.PHASE_1, p1);
        map.put(TestSupportClientKonstanten.TEST_PHASE.PHASE_2, p2);

        CustomersSelectionView view = new CustomersSelectionView();
        view.setTestCustomersTableModelMap(map);

        assertThat(view.getTableWithButtonsViewP1().getModel()).isInstanceOf(TestCustomersTableModel.class);
        assertThat(view.getTableWithButtonsViewP1().getModel().getRowCount()).isEqualTo(2);
        assertThat(view.getTableWithButtonsViewP2().getModel().getRowCount()).isEqualTo(1);
    }

    @Test
    void getActiveTestCustomersMapMap_onlyIncludesActivatedCustomers() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestCustomer a = new TestCustomer("A", "CustA");
        a.setActivated(true);
        TestCustomer b = new TestCustomer("B", "CustB");
        b.setActivated(false);

        Map<String, TestCustomer> p1 = new LinkedHashMap<>();
        p1.put("A", a);
        p1.put("B", b);

        Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> map = new TreeMap<>();
        map.put(TestSupportClientKonstanten.TEST_PHASE.PHASE_1, p1);
        map.put(TestSupportClientKonstanten.TEST_PHASE.PHASE_2, new HashMap<>());

        CustomersSelectionView view = new CustomersSelectionView();
        view.setTestCustomersTableModelMap(map);

        Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> active =
                view.getActiveTestCustomersMapMap();
        assertThat(active.get(TestSupportClientKonstanten.TEST_PHASE.PHASE_1))
                .containsOnlyKeys("A");
    }

    @Test
    void emptyMap_returnsNoActiveCustomers() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        CustomersSelectionView view = new CustomersSelectionView();
        Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> active =
                view.getActiveTestCustomersMapMap();
        assertThat(active.get(TestSupportClientKonstanten.TEST_PHASE.PHASE_1)).isEmpty();
        assertThat(active.get(TestSupportClientKonstanten.TEST_PHASE.PHASE_2)).isEmpty();
    }
}
