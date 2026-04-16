package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestCommandTreeNodeTest {

    @Test
    void toString_delegatesToUserObject() {
        TestCommandTreeNode node = new TestCommandTreeNode("MyCommand");
        assertThat(node.toString()).isEqualTo("MyCommand");
    }

    @Test
    void activatedFlag_alwaysFalse() {
        TestCommandTreeNode node = new TestCommandTreeNode("x");
        node.setActivated(true);
        assertThat(node.isActivated()).isFalse();
    }

    @Test
    void compareTo_returnsZero() {
        TestCommandTreeNode a = new TestCommandTreeNode("a");
        TestCommandTreeNode b = new TestCommandTreeNode("b");
        assertThat(a.compareTo(b)).isZero();
    }
}
