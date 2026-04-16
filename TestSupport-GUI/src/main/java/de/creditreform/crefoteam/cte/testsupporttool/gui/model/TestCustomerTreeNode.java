package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.AbstractMutableTreeNode;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;

public class TestCustomerTreeNode extends AbstractMutableTreeNode {
    public TestCustomerTreeNode(TestCustomer value) {
        super(value);
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

    public TestCustomer getTestCustomer() {
        return (TestCustomer) userObject;
    }
}
