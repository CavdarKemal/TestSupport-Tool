package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.tesun.util.TestResults;
import de.creditreform.crefoteam.cte.tesun.util.TestScenario;

import javax.swing.tree.DefaultTreeModel;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestResultsTreeModel extends DefaultTreeModel {
    private final Map<String, TestCustomer> testCustomerMap;

    public TestResultsTreeModel(Map<String, TestCustomer> testCustomerMap) {
        super(new TestResultTreeNode(testCustomerMap));
        this.testCustomerMap = testCustomerMap;
        intTree();
    }

    private void intTree() {
        TestResultTreeNode rootNode = (TestResultTreeNode) super.getRoot();
        testCustomerMap.entrySet().forEach(testCustomerEntry -> {
            TestCustomer testCustomer = testCustomerEntry.getValue();
            Map<String, TestResults> testResultsMapForCommands = testCustomer.getTestResultsMapForCommands();
            if (!testResultsMapForCommands.isEmpty()) {
                TestCustomerTreeNode testCustomerTreeNode = new TestCustomerTreeNode(testCustomer);
                if (populateTestCustomersCommands(testCustomerTreeNode, testCustomer)) {
                    rootNode.insert(testCustomerTreeNode, rootNode.getChildCount());
                }
            }
        });
    }

    private boolean populateTestCustomersCommands(TestCustomerTreeNode testCustomerTreeNode, TestCustomer testCustomer) {
        Map<String, TestResults> testResultsMapForCommands = testCustomer.getTestResultsMapForCommands();
        testResultsMapForCommands.entrySet().forEach(testResultsEntry -> {
            Map<String, TestScenario> testScenariosMap = testCustomer.getTestScenariosMap();
            TestResults testResultsForCommand = testResultsEntry.getValue();
            if (!isTestResultsForCommandEmpty(testResultsForCommand, testScenariosMap)) {
                TestCommandTreeNode testCommandTreeNode = new TestCommandTreeNode(testResultsForCommand.getCommand());
                if (populateTestCustomersScenarios(testScenariosMap, testResultsForCommand, testCommandTreeNode)) {
                    testCustomerTreeNode.insert(testCommandTreeNode, testCustomerTreeNode.getChildCount());
                }
            }
        });
        return true;
    }

    private boolean populateTestCustomersScenarios(Map<String, TestScenario> testScenariosMap, TestResults testResultsForCommand, TestCommandTreeNode testCommandTreeNode) {
        testScenariosMap.entrySet().forEach(testScenarioEntry -> {
            TestScenario testScenario = testScenarioEntry.getValue();
            TestResults testResultsForScenario = testScenario.getTestResultsForScenario(testResultsForCommand.getCommand(), testScenario.getScenarioName());
            if (!testResultsForScenario.getResultInfosList().isEmpty()) {
                TestScenarioTreeNode treeNodeForTestScenario = new TestScenarioTreeNode(testScenario);
                testCommandTreeNode.insert(treeNodeForTestScenario, testCommandTreeNode.getChildCount());
                for (TestResults.ResultInfo resultInfo : testResultsForScenario.getResultInfosList()) {
                    if (!resultInfo.getDiffenrenceInfosList().isEmpty()) {
                        for (TestResults.DiffenrenceInfo diffenrenceInfo : resultInfo.getDiffenrenceInfosList()) {
                            DifferenceTreeNode treeNodeForDiff = new DifferenceTreeNode(diffenrenceInfo);
                            treeNodeForTestScenario.insert(treeNodeForDiff, treeNodeForTestScenario.getChildCount());
                        }
                    }
                }
            }
        });
        return true;
    }

    private boolean isTestResultsForCommandEmpty(TestResults testResultsForCommand, Map<String, TestScenario> testScenariosMap) {
        boolean empty = testResultsForCommand.getResultInfosList().isEmpty();
        List<Map.Entry<String, TestScenario>> entryList = testScenariosMap.entrySet().stream().filter(testScenarioEntry -> {
            TestScenario testScenario = testScenarioEntry.getValue();
            TestResults testResultsForScenario = testScenario.getTestResultsForScenario(testResultsForCommand.getCommand(), testScenario.getScenarioName());
            return !testResultsForScenario.getResultInfosList().isEmpty();
        }).collect(Collectors.toList());
        return empty && entryList.isEmpty();
    }

}
