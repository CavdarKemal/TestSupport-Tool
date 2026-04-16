package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.tesun.util.TestScenario;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestScenarioTreeNodeTest {

    @Test
    void getTestScenario_returnsStoredUserObject() {
        TestCustomer customer = new TestCustomer("KEY", "Name");
        TestScenario scenario = new TestScenario(customer, "ScenX");
        TestScenarioTreeNode node = new TestScenarioTreeNode(scenario);
        assertThat(node.getTestScenario()).isSameAs(scenario);
    }

    @Test
    void toString_delegatesToScenarioName() {
        TestCustomer customer = new TestCustomer("K", "N");
        TestScenario scenario = new TestScenario(customer, "Relevanz_Positiv");
        TestScenarioTreeNode node = new TestScenarioTreeNode(scenario);
        assertThat(node.toString()).isEqualTo("Relevanz_Positiv");
    }

    @Test
    void activatedFlag_alwaysFalse() {
        TestCustomer customer = new TestCustomer("K", "N");
        TestScenarioTreeNode node = new TestScenarioTreeNode(new TestScenario(customer, "s"));
        node.setActivated(true);
        assertThat(node.isActivated()).isFalse();
    }

    @Test
    void compareTo_returnsZero() {
        TestCustomer customer = new TestCustomer("K", "N");
        TestScenarioTreeNode a = new TestScenarioTreeNode(new TestScenario(customer, "a"));
        TestScenarioTreeNode b = new TestScenarioTreeNode(new TestScenario(customer, "b"));
        assertThat(a.compareTo(b)).isZero();
    }
}
