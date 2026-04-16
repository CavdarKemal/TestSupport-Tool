/*
 * Created by JFormDesigner on Thu Dec 05 10:38:06 CET 2024
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.design;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * @author CavdarK
 */
public class TestResultsTabPanel extends JPanel {
    public TestResultsTabPanel() {
        initComponents();
    }

    public JButton getButtonSaveTestResults() {
        return buttonSaveTestResults;
    }

    public JButton getButtonRefreshTestResults() {
        return buttonRefreshTestResults;
    }

    public JSplitPane getSplitPanelTreeView() {
        return splitPanelTreeView;
    }

    public JPanel getPanelTestResults() {
        return panelTestResults;
    }

    public JButton getButtonLoadTestResults() {
        return buttonLoadTestResults;
    }

    public JComboBox getComboBoxDiffTools() {
        return comboBoxDiffTools;
    }

    public JButton getButtonStartDifTool() {
        return buttonStartDifTool;
    }

    public JPanel getPanelControls() {
        return panelControls;
    }

    public JLabel getLabel1() {
        return label1;
    }

    public JSplitPane getSplitPaneCustomerTrees() {
        return splitPaneCustomerTrees;
    }

    public JPanel getPanelCustomerP2() {
        return panelCustomerP2;
    }

    public JScrollPane getScrollPaneTreeCustomers2() {
        return scrollPaneTreeCustomers2;
    }

    public JTree getTreeCustomersPhase2() {
        return treeCustomersPhase2;
    }

    public JPanel getPanelCustomerP1() {
       return panelCustomerP1;
    }

    public JTree getTreeCustomersPhase1() {
       return treeCustomersPhase1;
    }

    public JScrollPane getScrollPaneTreeCustomers1() {
       return scrollPaneTreeCustomers1;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panelControls = new JPanel();
        buttonRefreshTestResults = new JButton();
        buttonLoadTestResults = new JButton();
        buttonSaveTestResults = new JButton();
        label1 = new JLabel();
        comboBoxDiffTools = new JComboBox();
        buttonStartDifTool = new JButton();
        splitPanelTreeView = new JSplitPane();
        splitPaneCustomerTrees = new JSplitPane();
        panelCustomerP1 = new JPanel();
        scrollPaneTreeCustomers1 = new JScrollPane();
        treeCustomersPhase1 = new JTree();
        panelCustomerP2 = new JPanel();
        scrollPaneTreeCustomers2 = new JScrollPane();
        treeCustomersPhase2 = new JTree();
        panelTestResults = new JPanel();

        //======== this ========
        setFont(new Font("Noto Sans", Font.PLAIN, 11));
        setLayout(new BorderLayout());

        //======== panelControls ========
        {
           panelControls.setBorder(new BevelBorder(BevelBorder.LOWERED));
           panelControls.setFont(new Font("Noto Sans", Font.PLAIN, 11));
           panelControls.setLayout(new GridBagLayout());
           ((GridBagLayout)panelControls.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
           ((GridBagLayout)panelControls.getLayout()).rowHeights = new int[] {0, 0, 0};
           ((GridBagLayout)panelControls.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0, 0.0, 1.0E-4};
           ((GridBagLayout)panelControls.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

           //---- buttonRefreshTestResults ----
           buttonRefreshTestResults.setText("Aktualisieren");
           buttonRefreshTestResults.setIcon(new ImageIcon(getClass().getResource("/icons/refresh.png")));
           panelControls.add(buttonRefreshTestResults, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(2, 2, 7, 5), 0, 0));

           //---- buttonLoadTestResults ----
           buttonLoadTestResults.setText("Test Results Laden...");
           buttonLoadTestResults.setIcon(new ImageIcon(getClass().getResource("/icons/folder_up.png")));
           panelControls.add(buttonLoadTestResults, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(2, 2, 7, 5), 0, 0));

           //---- buttonSaveTestResults ----
           buttonSaveTestResults.setText("Test Results Speichern");
           buttonSaveTestResults.setIcon(new ImageIcon(getClass().getResource("/icons/save.png")));
           panelControls.add(buttonSaveTestResults, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(2, 2, 7, 0), 0, 0));

           //---- label1 ----
           label1.setText("Diff-Tool:");
           panelControls.add(label1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(1, 2, 2, 7), 0, 0));
           panelControls.add(comboBoxDiffTools, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(1, 2, 2, 7), 0, 0));

           //---- buttonStartDifTool ----
           buttonStartDifTool.setText("Start Diff-Tool");
           panelControls.add(buttonStartDifTool, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(1, 2, 2, 2), 0, 0));
        }
        add(panelControls, BorderLayout.PAGE_START);

        //======== splitPanelTreeView ========
        {
           splitPanelTreeView.setDividerLocation(150);
           splitPanelTreeView.setFont(new Font("Noto Sans", Font.PLAIN, 11));
           splitPanelTreeView.setDividerSize(5);

           //======== splitPaneCustomerTrees ========
           {
              splitPaneCustomerTrees.setOrientation(JSplitPane.VERTICAL_SPLIT);
              splitPaneCustomerTrees.setDividerLocation(160);
              splitPaneCustomerTrees.setFont(new Font("Noto Sans", Font.PLAIN, 11));
              splitPaneCustomerTrees.setLastDividerLocation(160);
              splitPaneCustomerTrees.setDividerSize(5);

              //======== panelCustomerP1 ========
              {
                 panelCustomerP1.setBorder(null);
                 panelCustomerP1.setPreferredSize(new Dimension(200, 482));
                 panelCustomerP1.setFont(new Font("Noto Sans", Font.PLAIN, 11));
                 panelCustomerP1.setLayout(new CardLayout());

                 //======== scrollPaneTreeCustomers1 ========
                 {
                    scrollPaneTreeCustomers1.setFont(new Font("Noto Sans", Font.PLAIN, 11));

                    //---- treeCustomersPhase1 ----
                    treeCustomersPhase1.setCellRenderer(null);
                    treeCustomersPhase1.setForeground(Color.blue);
                    treeCustomersPhase1.setFont(new Font("Noto Sans", Font.PLAIN, 11));
                    scrollPaneTreeCustomers1.setViewportView(treeCustomersPhase1);
                 }
                 panelCustomerP1.add(scrollPaneTreeCustomers1, "card2");
              }
              splitPaneCustomerTrees.setTopComponent(panelCustomerP1);

              //======== panelCustomerP2 ========
              {
                 panelCustomerP2.setBorder(null);
                 panelCustomerP2.setPreferredSize(new Dimension(200, 482));
                 panelCustomerP2.setFont(new Font("Noto Sans", Font.PLAIN, 11));
                 panelCustomerP2.setLayout(new CardLayout());

                 //======== scrollPaneTreeCustomers2 ========
                 {
                    scrollPaneTreeCustomers2.setFont(new Font("Noto Sans", Font.PLAIN, 11));

                    //---- treeCustomersPhase2 ----
                    treeCustomersPhase2.setCellRenderer(null);
                    treeCustomersPhase2.setForeground(Color.blue);
                    treeCustomersPhase2.setFont(new Font("Noto Sans", Font.PLAIN, 11));
                    scrollPaneTreeCustomers2.setViewportView(treeCustomersPhase2);
                 }
                 panelCustomerP2.add(scrollPaneTreeCustomers2, "card2");
              }
              splitPaneCustomerTrees.setBottomComponent(panelCustomerP2);
           }
           splitPanelTreeView.setLeftComponent(splitPaneCustomerTrees);

           //======== panelTestResults ========
           {
              panelTestResults.setFont(new Font("Noto Sans", Font.PLAIN, 11));
              panelTestResults.setLayout(new CardLayout());
           }
           splitPanelTreeView.setRightComponent(panelTestResults);
        }
        add(splitPanelTreeView, BorderLayout.CENTER);
      // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel panelControls;
    private JButton buttonRefreshTestResults;
    private JButton buttonLoadTestResults;
    private JButton buttonSaveTestResults;
    private JLabel label1;
    private JComboBox comboBoxDiffTools;
    private JButton buttonStartDifTool;
    private JSplitPane splitPanelTreeView;
    private JSplitPane splitPaneCustomerTrees;
    private JPanel panelCustomerP1;
    private JScrollPane scrollPaneTreeCustomers1;
    private JTree treeCustomersPhase1;
    private JPanel panelCustomerP2;
    private JScrollPane scrollPaneTreeCustomers2;
    private JTree treeCustomersPhase2;
    private JPanel panelTestResults;
   // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
