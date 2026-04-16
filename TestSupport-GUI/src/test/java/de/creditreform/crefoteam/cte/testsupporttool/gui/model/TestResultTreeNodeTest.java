package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestResultTreeNodeTest {

    @Test
    void toString_isLiteralTestResults() {
        TestResultTreeNode node = new TestResultTreeNode("any");
        assertThat(node.toString()).isEqualTo("Test-Results");
    }

    @Test
    void userObject_isStoredUnchanged() {
        Object payload = new Object();
        TestResultTreeNode node = new TestResultTreeNode(payload);
        assertThat(node.getUserObject()).isSameAs(payload);
    }

    @Test
    void activatedFlag_alwaysFalse() {
        TestResultTreeNode node = new TestResultTreeNode("x");
        node.setActivated(true);
        assertThat(node.isActivated()).isFalse();
    }

    @Test
    void compareTo_returnsZero() {
        TestResultTreeNode a = new TestResultTreeNode("a");
        TestResultTreeNode b = new TestResultTreeNode("b");
        assertThat(a.compareTo(b)).isZero();
    }
}
