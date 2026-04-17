/*
 * Created by JFormDesigner on Fri Nov 21 13:13:56 CET 2014
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.design;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.view.TableWithButtonsView;

import javax.swing.*;
import java.awt.*;

public class CustomersSelectionPanel extends JPanel {
    public CustomersSelectionPanel() {
        initComponents();
    }

    public JSplitPane getSplitPaneMain() {
        return splitPaneMain;
    }

    public JSplitPane getSplitPaneScenarios() {
        return splitPaneScenarios;
    }

    public TableWithButtonsView getPanelScenarios() {
        return panelScenarios;
    }

    public TableWithButtonsView getPanelTestCrefos() {
        return panelTestCrefos;
    }

    public JSplitPane getSplitPaneCustomerTrees() {
        return splitPaneCustomerTrees;
    }

    public JScrollPane getScrollPaneTreeCustomersP1() {
        return scrollPaneTreeCustomersP1;
    }

    public JPanel getPanelCustomerP1() {
        return panelCustomerP1;
    }

    public TableWithButtonsView getTableWithButtonsViewP1() {
        return tableWithButtonsViewP1;
    }

    public JTree getTreeCustomersPhase1() {
        return treeCustomersPhase1;
    }

    public JPanel getPanelCustomerP2() {
        return panelCustomerP2;
    }

    public TableWithButtonsView getTableWithButtonsViewP2() {
        return tableWithButtonsViewP2;
    }

    public JScrollPane getScrollPaneTreeCustomers2() {
        return scrollPaneTreeCustomers2;
    }

    public JTree getTreeCustomersPhase2() {
        return treeCustomersPhase2;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        splitPaneMain = new JSplitPane();
        splitPaneCustomerTrees = new JSplitPane();
        panelCustomerP1 = new JPanel();
        tableWithButtonsViewP1 = new TableWithButtonsView();
        scrollPaneTreeCustomersP1 = new JScrollPane();
        treeCustomersPhase1 = new JTree();
        panelCustomerP2 = new JPanel();
        tableWithButtonsViewP2 = new TableWithButtonsView();
        scrollPaneTreeCustomers2 = new JScrollPane();
        treeCustomersPhase2 = new JTree();
        splitPaneScenarios = new JSplitPane();
        panelScenarios = new TableWithButtonsView();
        panelTestCrefos = new TableWithButtonsView();

        //======== this ========
        setName("this");
        setLayout(new GridBagLayout());
        ((GridBagLayout)getLayout()).columnWidths = new int[] {116, 0, 0};
        ((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0, 0};
        ((GridBagLayout)getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0E-4};
        ((GridBagLayout)getLayout()).rowWeights = new double[] {1.0, 0.0, 1.0E-4};

        //======== splitPaneMain ========
        {
           splitPaneMain.setDividerLocation(200);
           splitPaneMain.setName("splitPaneMain");

           //======== splitPaneCustomerTrees ========
           {
              splitPaneCustomerTrees.setOrientation(JSplitPane.VERTICAL_SPLIT);
              splitPaneCustomerTrees.setDividerLocation(250);
              splitPaneCustomerTrees.setName("splitPaneCustomerTrees");

              //======== panelCustomerP1 ========
              {
                 panelCustomerP1.setBorder(null);
                 panelCustomerP1.setName("panelCustomerP1");
                 panelCustomerP1.setLayout(new CardLayout());

                 //---- tableWithButtonsViewP1 ----
                 tableWithButtonsViewP1.setName("tableWithButtonsViewP1");
                 panelCustomerP1.add(tableWithButtonsViewP1, "card1");

                 //======== scrollPaneTreeCustomersP1 ========
                 {
                    scrollPaneTreeCustomersP1.setName("scrollPaneTreeCustomersP1");

                    //---- treeCustomersPhase1 ----
                    treeCustomersPhase1.setName("treeCustomersPhase1");
                    scrollPaneTreeCustomersP1.setViewportView(treeCustomersPhase1);
                 }
                 panelCustomerP1.add(scrollPaneTreeCustomersP1, "card2");
              }
              splitPaneCustomerTrees.setTopComponent(panelCustomerP1);

              //======== panelCustomerP2 ========
              {
                 panelCustomerP2.setBorder(null);
                 panelCustomerP2.setName("panelCustomerP2");
                 panelCustomerP2.setLayout(new CardLayout());

                 //---- tableWithButtonsViewP2 ----
                 tableWithButtonsViewP2.setName("tableWithButtonsViewP2");
                 panelCustomerP2.add(tableWithButtonsViewP2, "card1");

                 //======== scrollPaneTreeCustomers2 ========
                 {
                    scrollPaneTreeCustomers2.setName("scrollPaneTreeCustomers2");

                    //---- treeCustomersPhase2 ----
                    treeCustomersPhase2.setName("treeCustomersPhase2");
                    scrollPaneTreeCustomers2.setViewportView(treeCustomersPhase2);
                 }
                 panelCustomerP2.add(scrollPaneTreeCustomers2, "card2");
              }
              splitPaneCustomerTrees.setBottomComponent(panelCustomerP2);
           }
           splitPaneMain.setLeftComponent(splitPaneCustomerTrees);

           //======== splitPaneScenarios ========
           {
              splitPaneScenarios.setOrientation(JSplitPane.VERTICAL_SPLIT);
              splitPaneScenarios.setDividerLocation(180);
              splitPaneScenarios.setName("splitPaneScenarios");

              //---- panelScenarios ----
              panelScenarios.setBorder(null);
              panelScenarios.setName("panelScenarios");
              splitPaneScenarios.setTopComponent(panelScenarios);

              //---- panelTestCrefos ----
              panelTestCrefos.setName("panelTestCrefos");
              splitPaneScenarios.setBottomComponent(panelTestCrefos);
           }
           splitPaneMain.setRightComponent(splitPaneScenarios);
        }
        add(splitPaneMain, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(2, 2, 7, 7), 0, 0));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JSplitPane splitPaneMain;
    private JSplitPane splitPaneCustomerTrees;
    private JPanel panelCustomerP1;
    private TableWithButtonsView tableWithButtonsViewP1;
    private JScrollPane scrollPaneTreeCustomersP1;
    private JTree treeCustomersPhase1;
    private JPanel panelCustomerP2;
    private TableWithButtonsView tableWithButtonsViewP2;
    private JScrollPane scrollPaneTreeCustomers2;
    private JTree treeCustomersPhase2;
    private JSplitPane splitPaneScenarios;
    private TableWithButtonsView panelScenarios;
    private TableWithButtonsView panelTestCrefos;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
