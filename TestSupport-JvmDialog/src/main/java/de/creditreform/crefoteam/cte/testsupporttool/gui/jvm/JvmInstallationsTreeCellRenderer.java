package de.creditreform.crefoteam.cte.testsupporttool.gui.jvm;

import de.creditreform.crefoteam.cte.jvmclient.JvmInstallation;
import de.creditreform.crefoteam.cte.jvmclient.JvmJobExecutionInfo;
import de.creditreform.crefoteam.cte.jvmclient.JvmJobInfo;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.Map;

class JvmInstallationsTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        final Component component = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        if (value instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) value;
            Object userObject = selectedNode.getUserObject();
            if (userObject instanceof Map) {
                component.setForeground(Color.DARK_GRAY);
                component.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 14));
            } else if (userObject instanceof JvmInstallation) {
                JvmInstallation jvmInstallation = (JvmInstallation) userObject;
                component.setForeground(Color.BLACK);
                component.setFont(new Font("Verdana", Font.ITALIC, 13));
            } else if (userObject instanceof String) {
                component.setForeground(Color.ORANGE);
                component.setFont(new Font("Verdana", Font.ITALIC, 13));
            } else if (userObject instanceof JvmJobInfo) {
                JvmJobInfo jvmJobInfo = (JvmJobInfo) userObject;
                component.setForeground(JvmConstants.DEFAULT_COLOR);
                List<JvmJobExecutionInfo> jobExecutionsList = jvmJobInfo.getJobExecutionsList();
                for (JvmJobExecutionInfo jvmJobExecutionInfo : jobExecutionsList) {
                    if (jvmJobExecutionInfo.getRunning().equals("true")) {
                        component.setForeground(JvmConstants.RUNNING_COLOR);
                        break;
                    }
                }
                component.setFont(new Font("Verdana", Font.BOLD | Font.PLAIN, 12));
            } else if (userObject instanceof JvmJobExecutionInfo) {
                component.setFont(new Font("Verdana", Font.ITALIC, 11));
                JvmJobExecutionInfo jvmJobExecutionInfo = (JvmJobExecutionInfo) userObject;
                if (jvmJobExecutionInfo.getRunning().equals("true")) {
                    component.setForeground(JvmConstants.RUNNING_COLOR);
                } else {
                    component.setForeground(calcColorForJvmJobExecutionInfo(jvmJobExecutionInfo));
                }
            }
        }
        return component;
    }

    public static Color calcColorForJvmJobExecutionInfo(JvmJobExecutionInfo jvmJobExecutionInfo) {
        String exitCode = jvmJobExecutionInfo.getExitCode();
        String status = jvmJobExecutionInfo.getStatus();
        if (exitCode.equals("COMPLETED") && status.equals("COMPLETED")) {
            return JvmConstants.STOPPED_COLOR;
        } else if (exitCode.equals("WAITING") || status.equals("WAITING")) {
            return JvmConstants.WAITING_COLOR;
        } else if (exitCode.equals("COMPLETED") || status.equals("COMPLETED")) {
            return JvmConstants.DEFAULT_COLOR;
        } else if (exitCode.equals("FAILED") || status.equals("FAILED")) {
            return JvmConstants.FAILED_COLOR;
        }
        return JvmConstants.DEFAULT_COLOR;
    }
}
