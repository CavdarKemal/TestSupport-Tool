package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.AbstractMutableTreeNode;
import de.creditreform.crefoteam.cte.tesun.util.TestResults;

public class DifferenceTreeNode extends AbstractMutableTreeNode {
    public DifferenceTreeNode(TestResults.DiffenrenceInfo diffenrenceInfo) {
        super(diffenrenceInfo);
    }

    @Override
    public String toString() {
        return "Testfall: " + getDiffenrenceInfo().getTestFallName();
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

    public TestResults.DiffenrenceInfo getDiffenrenceInfo() {
        return (TestResults.DiffenrenceInfo) userObject;
    }

}
