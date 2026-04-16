package de.creditreform.crefoteam.cte.testsupporttool.gui.base.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractMutableTreeNodeTest {

    /** Konkrete Sub-Klasse fuer Tests. */
    private static final class TestNode extends AbstractMutableTreeNode {
        private boolean activated;

        TestNode(Object userObject) {
            super(userObject);
        }

        @Override
        public void setActivated(boolean activated) { this.activated = activated; }

        @Override
        public boolean isActivated() { return activated; }

        @Override
        public int compareTo(Object o) {
            return getUserObject().toString().compareTo(((TestNode) o).getUserObject().toString());
        }
    }

    @Test
    void toString_delegatesToUserObject() {
        TestNode node = new TestNode("hello");
        assertThat(node.toString()).isEqualTo("hello");
    }

    @Test
    void getChildrensUserObjects_emptyForLeaf() {
        TestNode leaf = new TestNode("L");
        assertThat(leaf.getChildrensUserObjects()).isEmpty();
    }

    @Test
    void getChildrensUserObjects_recursivelyCollectsUserObjects() {
        TestNode root = new TestNode("root");
        TestNode child1 = new TestNode("c1");
        TestNode child2 = new TestNode("c2");
        TestNode grandchild = new TestNode("g1");
        root.add(child1);
        root.add(child2);
        child1.add(grandchild);

        Object[] result = root.getChildrensUserObjects();
        // Reihenfolge laut Original (DFS): c1, g1, c2
        assertThat(result).containsExactly("c1", "g1", "c2");
    }

    @Test
    void activatedFlag_setAndGetRoundtrip() {
        TestNode node = new TestNode("X");
        assertThat(node.isActivated()).isFalse();
        node.setActivated(true);
        assertThat(node.isActivated()).isTrue();
    }
}
