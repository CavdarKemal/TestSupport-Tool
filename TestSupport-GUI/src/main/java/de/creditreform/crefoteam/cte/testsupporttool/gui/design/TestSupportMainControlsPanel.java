/*
 * Created by JFormDesigner on Sat Mar 28 13:42:30 CET 2026
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.design;

import java.awt.*;
import javax.swing.*;

/**
 * @author CavdarK
 */
public class TestSupportMainControlsPanel extends JPanel {
    public TestSupportMainControlsPanel() {
        initComponents();
    }

    public JLabel getLabelEnvironment() {
        return labelEnvironment;
    }

    public JComboBox getComboBoxEnvironment() {
        return comboBoxEnvironment;
    }

    public JButton getButtonRefreshEnvironment() {
        return buttonRefreshEnvironment;
    }

    public JButton getButtonManageJVMs() {
        return buttonManageJVMs;
    }

    public JLabel getLabelActivitiHost() {
        return labelActivitiHost;
    }

    public JComboBox getComboBoxActivitiHost() {
        return comboBoxActivitiHost;
    }

    public JLabel getLabelRestServicesHost() {
        return labelRestServicesHost;
    }

    public JComboBox getComboBoxRestServicesHost() {
        return comboBoxRestServicesHost;
    }

    public JLabel getLabelBatchGUIHost() {
        return labelBatchGUIHost;
    }

    public JComboBox getComboBoxBatchGUIHost() {
        return comboBoxBatchGUIHost;
    }

    public JLabel getLabelImpCycleHost() {
        return labelImpCycleHost;
    }

    public JComboBox getComboBoxImpCycleHost() {
        return comboBoxImpCycleHost;
    }

    public JLabel getLabelInsoHost() {
        return labelInsoHost;
    }

    public JComboBox getComboBoxInsoHost() {
        return comboBoxInsoHost;
    }

    public JLabel getLabelInsoBackEndHost() {
        return labelInsoBackEndHost;
    }

    public JComboBox getComboBoxInsoBackEndHost() {
        return comboBoxInsoBackEndHost;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        labelEnvironment = new JLabel();
        comboBoxEnvironment = new JComboBox();
        buttonRefreshEnvironment = new JButton();
        buttonManageJVMs = new JButton();
        labelActivitiHost = new JLabel();
        comboBoxActivitiHost = new JComboBox();
        labelRestServicesHost = new JLabel();
        comboBoxRestServicesHost = new JComboBox();
        labelBatchGUIHost = new JLabel();
        comboBoxBatchGUIHost = new JComboBox();
        labelImpCycleHost = new JLabel();
        comboBoxImpCycleHost = new JComboBox();
        labelInsoHost = new JLabel();
        comboBoxInsoHost = new JComboBox();
        labelInsoBackEndHost = new JLabel();
        comboBoxInsoBackEndHost = new JComboBox();

        //======== this ========
        setLayout(new GridBagLayout());
        ((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0, 0, 0};
        ((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
        ((GridBagLayout)getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};
        ((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

        //---- labelEnvironment ----
        labelEnvironment.setText("Umgebung:");
        add(labelEnvironment, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(2, 2, 6, 7), 0, 0));
        add(comboBoxEnvironment, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(2, 2, 6, 7), 0, 0));

        //---- buttonRefreshEnvironment ----
        buttonRefreshEnvironment.setIcon(new ImageIcon(getClass().getResource("/icons/folder_refresh.png")));
        buttonRefreshEnvironment.setActionCommand("UT-Thread starten...");
        buttonRefreshEnvironment.setToolTipText("UT-Thread starten...");
        buttonRefreshEnvironment.setText("Aktualisieren");
        buttonRefreshEnvironment.setMinimumSize(new Dimension(24, 24));
        buttonRefreshEnvironment.setPreferredSize(new Dimension(24, 24));
        add(buttonRefreshEnvironment, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(2, 2, 6, 2), 0, 0));

        //---- buttonManageJVMs ----
        buttonManageJVMs.setIcon(new ImageIcon(getClass().getResource("/icons/folder_out.png")));
        buttonManageJVMs.setActionCommand("UT-Thread starten...");
        buttonManageJVMs.setToolTipText("BPMN's aktualisieren");
        buttonManageJVMs.setText("JVM's...");
        add(buttonManageJVMs, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(1, 1, 6, 1), 0, 0));

        //---- labelActivitiHost ----
        labelActivitiHost.setText("Activiti-Host:");
        add(labelActivitiHost, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(0, 1, 5, 6), 0, 0));
        add(comboBoxActivitiHost, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(0, 1, 5, 1), 0, 0));

        //---- labelRestServicesHost ----
        labelRestServicesHost.setText("RestServices-Host:");
        add(labelRestServicesHost, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(0, 1, 5, 6), 0, 0));
        add(comboBoxRestServicesHost, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(0, 1, 5, 1), 0, 0));

        //---- labelBatchGUIHost ----
        labelBatchGUIHost.setText("Batch-GUI-Host:");
        add(labelBatchGUIHost, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(0, 1, 5, 6), 0, 0));
        add(comboBoxBatchGUIHost, new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(0, 1, 5, 1), 0, 0));

        //---- labelImpCycleHost ----
        labelImpCycleHost.setText("ImportCycle-Host:");
        add(labelImpCycleHost, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(0, 1, 5, 6), 0, 0));
        add(comboBoxImpCycleHost, new GridBagConstraints(1, 5, 2, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(0, 1, 5, 1), 0, 0));

        //---- labelInsoHost ----
        labelInsoHost.setText("INSO-Host:");
        add(labelInsoHost, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(0, 1, 5, 6), 0, 0));
        add(comboBoxInsoHost, new GridBagConstraints(1, 6, 2, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(0, 1, 5, 1), 0, 0));

        //---- labelInsoBackEndHost ----
        labelInsoBackEndHost.setText("INSO-Backend-Host:");
        add(labelInsoBackEndHost, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(0, 1, 0, 6), 0, 0));
        add(comboBoxInsoBackEndHost, new GridBagConstraints(1, 7, 2, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(0, 1, 0, 1), 0, 0));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel labelEnvironment;
    private JComboBox comboBoxEnvironment;
    private JButton buttonRefreshEnvironment;
    private JButton buttonManageJVMs;
    private JLabel labelActivitiHost;
    private JComboBox comboBoxActivitiHost;
    private JLabel labelRestServicesHost;
    private JComboBox comboBoxRestServicesHost;
    private JLabel labelBatchGUIHost;
    private JComboBox comboBoxBatchGUIHost;
    private JLabel labelImpCycleHost;
    private JComboBox comboBoxImpCycleHost;
    private JLabel labelInsoHost;
    private JComboBox comboBoxInsoHost;
    private JLabel labelInsoBackEndHost;
    private JComboBox comboBoxInsoBackEndHost;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
