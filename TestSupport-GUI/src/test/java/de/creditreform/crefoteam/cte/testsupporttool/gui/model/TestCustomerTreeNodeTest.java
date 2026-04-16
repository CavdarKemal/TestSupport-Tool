package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestCustomerTreeNodeTest {

    @Test
    void getTestCustomer_returnsStoredUserObject() {
        TestCustomer customer = new TestCustomer("KEY", "Name");
        TestCustomerTreeNode node = new TestCustomerTreeNode(customer);
        assertThat(node.getTestCustomer()).isSameAs(customer);
    }

    @Test
    void activatedFlag_alwaysFalse() {
        TestCustomerTreeNode node = new TestCustomerTreeNode(new TestCustomer("K", "N"));
        node.setActivated(true);
        assertThat(node.isActivated()).isFalse();
    }

    @Test
    void compareTo_returnsZero() {
        TestCustomerTreeNode a = new TestCustomerTreeNode(new TestCustomer("A", "a"));
        TestCustomerTreeNode b = new TestCustomerTreeNode(new TestCustomer("B", "b"));
        assertThat(a.compareTo(b)).isZero();
    }
}
