package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.AbstractMutableTreeNode;

public class TestCommandTreeNode extends AbstractMutableTreeNode {
    public TestCommandTreeNode(String value) {
        super(value);
    }

    @Override
    public String toString() {
        return userObject.toString();
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
