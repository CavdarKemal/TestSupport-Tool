package de.creditreform.crefoteam.cte.testsupporttool.gui.jvm;

import de.creditreform.crefoteam.cte.jvmclient.JvmInstallation;
import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.ColumnsInfo;
import de.creditreform.crefoteam.cte.testsupporttool.gui.jvm.design.JvmInstallationsPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JvmInstallationsPanelView extends JvmInstallationsPanel {
    private final static ColumnsInfo[] table_ColumnsInfo;

    static {
        table_ColumnsInfo = new ColumnsInfo[]{
                new ColumnsInfo(40, 40, 40), // #
                new ColumnsInfo(140, 140, 0), // Name
                new ColumnsInfo(240, 240, 0), // Url
        };
    }

    private final JvmInstallationsTreeNode jvmInstallationsTreeNode;

    public JvmInstallationsPanelView(JvmInstallationsTreeNode jvmInstallationsTreeNode) {
        super();
        this.jvmInstallationsTreeNode = jvmInstallationsTreeNode;
        initControls();
    }

    private void initControls() {
        List<JvmInstallation> jvmInstallationList = new ArrayList<>();
        Map<String, JvmInstallation> jvmInstallationMap = (Map<String, JvmInstallation>) jvmInstallationsTreeNode.getUserObject();
        for (Map.Entry<String, JvmInstallation> jvmInstallationEntry : jvmInstallationMap.entrySet()) {
            jvmInstallationList.add(jvmInstallationEntry.getValue());
        }
        JvmInstallationTableModel tableModel = new JvmInstallationTableModel(jvmInstallationList);
        getPanelJvmInstallations().setModel(null, tableModel, table_ColumnsInfo);
//      getPanelJvmInstallations().getTable().setDefaultRenderer(JvmInstallation .class, new JvmInstallationTableCellRenderer());
    }

/*
   private class JvmInstallationTableCellRenderer extends DefaultTableCellRenderer {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
         Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         component.setForeground(ManageJvmsDlgView.DEFAULT_COLOR);
         table.repaint();
         return component;
      }
   }
*/

}
