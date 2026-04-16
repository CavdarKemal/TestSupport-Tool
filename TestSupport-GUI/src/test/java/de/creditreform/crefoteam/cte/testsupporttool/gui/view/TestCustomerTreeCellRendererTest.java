package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.model.DifferenceTreeNode;
import de.creditreform.crefoteam.cte.testsupporttool.gui.model.TestCommandTreeNode;
import de.creditreform.crefoteam.cte.testsupporttool.gui.model.TestCustomerTreeNode;
import de.creditreform.crefoteam.cte.testsupporttool.gui.model.TestResultTreeNode;
import de.creditreform.crefoteam.cte.testsupporttool.gui.model.TestScenarioTreeNode;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.tesun.util.TestResults;
import de.creditreform.crefoteam.cte.tesun.util.TestScenario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import javax.swing.JTree;
import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class TestCustomerTreeCellRendererTest {

    private final TestCustomerTreeCellRenderer renderer = new TestCustomerTreeCellRenderer();
    private final JTree tree = new JTree();

    @Test
    void nullValue_returnsRendererWithoutCrash() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        Component result = renderer.getTreeCellRendererComponent(tree, null, false, false, true, 0, false);
        assertThat(result).isSameAs(renderer);
    }

    @Test
    void testResultTreeNode_rendersInBlack() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        Component c = renderer.getTreeCellRendererComponent(tree, new TestResultTreeNode("x"), false, false, true, 0, false);
        assertThat(c.getForeground()).isEqualTo(Color.BLACK);
        assertThat(renderer.getText()).isEqualTo("Test-Results");
    }

    @Test
    void testCustomerTreeNode_rendersCustomerKeyInDarkGray() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestCustomerTreeNode node = new TestCustomerTreeNode(new TestCustomer("MYKEY", "Name"));
        Component c = renderer.getTreeCellRendererComponent(tree, node, false, false, true, 0, false);
        assertThat(c.getForeground()).isEqualTo(Color.DARK_GRAY);
        assertThat(renderer.getText()).isEqualTo("MYKEY");
    }

    @Test
    void testCommandTreeNode_rendersInCyan() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestCommandTreeNode node = new TestCommandTreeNode("UserTaskStartCollect");
        Component c = renderer.getTreeCellRendererComponent(tree, node, false, false, true, 0, false);
        assertThat(c.getForeground()).isEqualTo(Color.CYAN);
        assertThat(renderer.getText()).isEqualTo("UserTaskStartCollect");
    }

    @Test
    void testScenarioTreeNode_rendersScenarioNameInBlue() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestCustomer customer = new TestCustomer("K", "N");
        TestScenarioTreeNode node = new TestScenarioTreeNode(new TestScenario(customer, "RelevanzPositiv"));
        Component c = renderer.getTreeCellRendererComponent(tree, node, false, false, true, 0, false);
        assertThat(c.getForeground()).isEqualTo(Color.BLUE);
        assertThat(renderer.getText()).isEqualTo("RelevanzPositiv");
    }

    @Test
    void differenceTreeNode_rendersTestfallNameInPink() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestResults.DiffenrenceInfo info = new TestResults.DiffenrenceInfo(
                "p042_17", new File("s.xml"), new File("d.xml"), new File("diff.xml"));
        Component c = renderer.getTreeCellRendererComponent(tree, new DifferenceTreeNode(info), false, false, true, 0, false);
        assertThat(c.getForeground()).isEqualTo(Color.PINK);
        assertThat(renderer.getText()).isEqualTo("p042_17");
    }

    @Test
    void fallback_forUnknownValue_rendersInMagenta() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        Component c = renderer.getTreeCellRendererComponent(tree, "nicht-ein-tree-node", false, false, true, 0, false);
        assertThat(c.getForeground()).isEqualTo(Color.MAGENTA);
        assertThat(renderer.getText()).isEqualTo("nicht-ein-tree-node");
    }
}
