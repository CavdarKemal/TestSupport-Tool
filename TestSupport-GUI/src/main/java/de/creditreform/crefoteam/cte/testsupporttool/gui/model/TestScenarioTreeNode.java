package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.AbstractMutableTreeNode;
import de.creditreform.crefoteam.cte.tesun.util.TestScenario;

public class TestScenarioTreeNode extends AbstractMutableTreeNode {
    public TestScenarioTreeNode(TestScenario testScenario) {
        super(testScenario);
    }

    @Override
    public String toString() {
        return getTestScenario().getScenarioName();
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

    public TestScenario getTestScenario() {
        return (TestScenario) userObject;
    }
}
