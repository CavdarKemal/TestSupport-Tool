/*
 * Created by JFormDesigner on Fri Aug 10 17:22:27 CEST 2018
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.jvm.design;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * @author Kemal Cavdar
 */
public class ManageJvmsDlg extends JDialog {
    public ManageJvmsDlg(Window owner) {
        super(owner);
        initComponents();
    }

    public JPanel getPanelNorth() {
        return panelNorth;
    }

    public JButton getButtonStartJob() {
        return buttonStartJob;
    }

    public JCheckBox getCheckAutoBoxRefresh() {
        return checkAutoBoxRefresh;
    }

    public JSpinner getSpinnerAutoRefreshRate() {
        return spinnerAutoRefreshRate;
    }

    public JButton getButtonRefreshJVMs() {
        return buttonRefreshJVMs;
    }

    public JSplitPane getSplitPaneTreeView() {
        return splitPaneTreeView;
    }

    public JPanel getPanelTree() {
        return panelTree;
    }

    public JScrollPane getScrollPaneTree() {
        return scrollPaneTree;
    }

    public JTree getTreeJVMs() {
        return treeJVMs;
    }

    public JPanel getPanelView() {
        return panelView;
    }

    public JPanel getPanelSouth() {
        return panelSouth;
    }

    public JButton getButtonClose() {
        return buttonClose;
    }

    public JLabel getLabelEnvironment() {
        return labelEnvironment;
    }

    public JComboBox getComboBoxEnvironment() {
        return comboBoxEnvironment;
    }

    public JPanel getHSpacer1() {
        return hSpacer1;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panelNorth = new JPanel();
        labelEnvironment = new JLabel();
        comboBoxEnvironment = new JComboBox();
        hSpacer1 = new JPanel(null);
        checkAutoBoxRefresh = new JCheckBox();
        spinnerAutoRefreshRate = new JSpinner();
        buttonRefreshJVMs = new JButton();
        splitPaneTreeView = new JSplitPane();
        panelTree = new JPanel();
        scrollPaneTree = new JScrollPane();
        treeJVMs = new JTree();
        panelView = new JPanel();
        panelSouth = new JPanel();
        buttonStartJob = new JButton();
        buttonClose = new JButton();

        //======== this ========
        setName("this");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panelNorth ========
        {
            panelNorth.setName("panelNorth");
            panelNorth.setLayout(new GridBagLayout());
            ((GridBagLayout) panelNorth.getLayout()).columnWidths = new int[]{70, 98, 0, 0, 98, 0, 0};
            ((GridBagLayout) panelNorth.getLayout()).rowHeights = new int[]{0, 0};
            ((GridBagLayout) panelNorth.getLayout()).columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0E-4};
            ((GridBagLayout) panelNorth.getLayout()).rowWeights = new double[]{0.0, 1.0E-4};

            //---- labelEnvironment ----
            labelEnvironment.setText("Umgebung:");
            labelEnvironment.setName("labelEnvironment");
            panelNorth.add(labelEnvironment, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(4, 2, 2, 7), 0, 0));

            //---- comboBoxEnvironment ----
            comboBoxEnvironment.setName("comboBoxEnvironment");
            panelNorth.add(comboBoxEnvironment, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(4, 2, 2, 7), 0, 0));

            //---- hSpacer1 ----
            hSpacer1.setName("hSpacer1");
            panelNorth.add(hSpacer1, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

            //---- checkAutoBoxRefresh ----
            checkAutoBoxRefresh.setText("Auto Refresh [m]");
            checkAutoBoxRefresh.setName("checkAutoBoxRefresh");
            panelNorth.add(checkAutoBoxRefresh, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 7), 0, 0));

            //---- spinnerAutoRefreshRate ----
            spinnerAutoRefreshRate.setName("spinnerAutoRefreshRate");
            panelNorth.add(spinnerAutoRefreshRate, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 7), 0, 0));

            //---- buttonRefreshJVMs ----
            buttonRefreshJVMs.setIcon(new ImageIcon(getClass().getResource("/icons/refresh.png")));
            buttonRefreshJVMs.setActionCommand("OK");
            buttonRefreshJVMs.setName("buttonRefreshJVMs");
            panelNorth.add(buttonRefreshJVMs, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 2), 0, 0));
        }
        contentPane.add(panelNorth, BorderLayout.PAGE_START);

        //======== splitPaneTreeView ========
        {
            splitPaneTreeView.setDividerLocation(320);
            splitPaneTreeView.setName("splitPaneTreeView");

            //======== panelTree ========
            {
                panelTree.setName("panelTree");
                panelTree.setLayout(new BorderLayout());

                //======== scrollPaneTree ========
                {
                    scrollPaneTree.setName("scrollPaneTree");

                    //---- treeJVMs ----
                    treeJVMs.setName("treeJVMs");
                    scrollPaneTree.setViewportView(treeJVMs);
                }
                panelTree.add(scrollPaneTree, BorderLayout.CENTER);
            }
            splitPaneTreeView.setLeftComponent(panelTree);

            //======== panelView ========
            {
                panelView.setBorder(new BevelBorder(BevelBorder.LOWERED));
                panelView.setName("panelView");
                panelView.setLayout(new CardLayout());
            }
            splitPaneTreeView.setRightComponent(panelView);
        }
        contentPane.add(splitPaneTreeView, BorderLayout.CENTER);

        //======== panelSouth ========
        {
            panelSouth.setName("panelSouth");
            panelSouth.setLayout(new GridBagLayout());
            ((GridBagLayout) panelSouth.getLayout()).columnWidths = new int[]{0, 0, 0};
            ((GridBagLayout) panelSouth.getLayout()).rowHeights = new int[]{0, 0};
            ((GridBagLayout) panelSouth.getLayout()).columnWeights = new double[]{0.0, 1.0, 1.0E-4};
            ((GridBagLayout) panelSouth.getLayout()).rowWeights = new double[]{0.0, 1.0E-4};

            //---- buttonStartJob ----
            buttonStartJob.setText("Start");
            buttonStartJob.setIcon(new ImageIcon(getClass().getResource("/icons/gear_run.png")));
            buttonStartJob.setActionCommand("OK");
            buttonStartJob.setName("buttonStartJob");
            panelSouth.add(buttonStartJob, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 7), 0, 0));

            //---- buttonClose ----
            buttonClose.setText("Schliessen");
            buttonClose.setIcon(new ImageIcon(getClass().getResource("/icons/exit.png")));
            buttonClose.setActionCommand("OK");
            buttonClose.setName("buttonClose");
            panelSouth.add(buttonClose, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(2, 2, 6, 2), 0, 0));
        }
        contentPane.add(panelSouth, BorderLayout.PAGE_END);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panelNorth;
    private JLabel labelEnvironment;
    private JComboBox comboBoxEnvironment;
    private JPanel hSpacer1;
    private JCheckBox checkAutoBoxRefresh;
    private JSpinner spinnerAutoRefreshRate;
    private JButton buttonRefreshJVMs;
    private JSplitPane splitPaneTreeView;
    private JPanel panelTree;
    private JScrollPane scrollPaneTree;
    private JTree treeJVMs;
    private JPanel panelView;
    private JPanel panelSouth;
    private JButton buttonStartJob;
    private JButton buttonClose;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
