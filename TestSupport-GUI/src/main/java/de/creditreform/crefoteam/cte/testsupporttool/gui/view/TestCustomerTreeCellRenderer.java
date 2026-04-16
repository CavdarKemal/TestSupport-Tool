package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.model.*;
import de.creditreform.crefoteam.cte.tesun.util.TestResults;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class TestCustomerTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        final Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (value == null) {
            return this;
        }
        if (value instanceof TestResultTreeNode) {
            setText(value.toString());
            component.setForeground(Color.BLACK);
            component.setFont(new Font("Verdana", Font.PLAIN, 14));
        } else if (value instanceof TestCustomerTreeNode) {
            setText(((TestCustomerTreeNode) value).getTestCustomer().getCustomerKey());
            component.setForeground(Color.DARK_GRAY);
            component.setFont(new Font("Verdana", Font.TRUETYPE_FONT | Font.ITALIC, 13));
        } else if (value instanceof TestCommandTreeNode) {
            setText(((TestCommandTreeNode) value).getUserObject().toString());
            component.setForeground(Color.CYAN);
            component.setFont(new Font("Verdana", Font.TRUETYPE_FONT, 12));
        } else if (value instanceof TestScenarioTreeNode) {
            setText(((TestScenarioTreeNode) value).getTestScenario().getScenarioName());
            component.setForeground(Color.BLUE);
            component.setFont(new Font("Verdana", Font.ROMAN_BASELINE, 11));
        } else if (value instanceof DifferenceTreeNode) {
            DifferenceTreeNode differenceTreeNode = (DifferenceTreeNode) value;
            TestResults.DiffenrenceInfo diffenrenceInfo = differenceTreeNode.getDiffenrenceInfo();
            setText(diffenrenceInfo.getTestFallName());
            component.setForeground(Color.PINK);
            component.setFont(new Font("Verdana", Font.ITALIC, 10));
        } else {
            setText(value.toString());
            component.setForeground(Color.MAGENTA);
            component.setFont(new Font("Verdana", Font.ITALIC, 9));
        }
        return this;
    }
}
