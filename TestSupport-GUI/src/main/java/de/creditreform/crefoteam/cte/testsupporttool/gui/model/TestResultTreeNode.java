package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.AbstractMutableTreeNode;

public class TestResultTreeNode extends AbstractMutableTreeNode {
    public TestResultTreeNode(Object value) {
        super(value);
    }

    @Override
    public String toString() {
        return "Test-Results";
    }

    @Override
    public void setActivated(boolean activated) {
    }

    @Override
    public boolean isActivated() {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
