package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.tesun.util.TestResults;
import de.creditreform.crefoteam.cte.tesun.util.TestScenario;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TestResultsTreeModelTest {

    @Test
    void emptyMap_rootHasNoChildren() {
        TestResultsTreeModel model = new TestResultsTreeModel(new HashMap<>());
        TestResultTreeNode root = (TestResultTreeNode) model.getRoot();
        assertThat(root.getChildCount()).isZero();
    }

    @Test
    void customerWithoutCommands_doesNotAppearInTree() {
        Map<String, TestCustomer> map = new HashMap<>();
        map.put("KEY", new TestCustomer("KEY", "Name"));
        TestResultsTreeModel model = new TestResultsTreeModel(map);
        TestResultTreeNode root = (TestResultTreeNode) model.getRoot();
        // Kunde ohne Commands/Ergebnisse taucht nicht auf (Baum filtert leere).
        assertThat(root.getChildCount()).isZero();
    }

    @Test
    void customerWithNonEmptyCommandResults_isIncluded() {
        TestCustomer customer = new TestCustomer("KEY", "Name");
        TestResults tr = customer.addTestResultsForCommand("UserTaskStartCollect");
        tr.addResultInfo(new TestResults.ResultInfo("irgendein Fehler"));

        Map<String, TestCustomer> map = new HashMap<>();
        map.put("KEY", customer);
        TestResultsTreeModel model = new TestResultsTreeModel(map);
        TestResultTreeNode root = (TestResultTreeNode) model.getRoot();
        assertThat(root.getChildCount()).isEqualTo(1);
        TestCustomerTreeNode cNode = (TestCustomerTreeNode) root.getChildAt(0);
        assertThat(cNode.getTestCustomer()).isSameAs(customer);
    }

    @Test
    void customerWithScenarioResults_buildsCommandAndScenarioNodes() {
        TestCustomer customer = new TestCustomer("KEY", "Name");
        customer.addTestResultsForCommand("UserTaskStartCollect");
        TestScenario scenario = new TestScenario(customer, "Scen1");
        customer.addTestScenario(scenario);
        scenario.addResultInfo("UserTaskStartCollect", new TestResults.ResultInfo("boom"));

        Map<String, TestCustomer> map = new HashMap<>();
        map.put("KEY", customer);
        TestResultsTreeModel model = new TestResultsTreeModel(map);
        TestResultTreeNode root = (TestResultTreeNode) model.getRoot();
        TestCustomerTreeNode cNode = (TestCustomerTreeNode) root.getChildAt(0);
        TestCommandTreeNode commandNode = (TestCommandTreeNode) cNode.getChildAt(0);
        assertThat(commandNode.toString()).isEqualTo("UserTaskStartCollect");
        TestScenarioTreeNode sNode = (TestScenarioTreeNode) commandNode.getChildAt(0);
        assertThat(sNode.getTestScenario()).isSameAs(scenario);
    }
}
