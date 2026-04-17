/*
 * Created by JFormDesigner on Mon Jan 25 16:35:02 CET 2016
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.design;

import de.creditreform.crefoteam.cte.testsupporttool.gui.view.CustomersSelectionView;
import de.creditreform.crefoteam.cte.testsupporttool.gui.view.TestSupportMainControlsView;
import de.creditreform.crefoteam.cte.testsupporttool.gui.view.TestSupportMainProcessView;
import de.creditreform.crefoteam.cte.testsupporttool.gui.view.TestSupportMainTabView;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * @author Uzayli Temel
 */
public class TestSupportPanel extends JPanel {
    public TestSupportPanel() {
        initComponents();
    }

    public JSplitPane getSplitPaneMain() {
        return splitPaneMain;
    }

    public CustomersSelectionView getViewCustomersSelection() {
        return viewCustomersSelection;
    }

    public JPanel getPanelRight() {
        return panelRight;
    }

    public JPanel getPanelLeft() {
        return panelLeft;
    }

    public TestSupportMainTabView getTabbedPaneMonitor() {
        return tabbedPaneMonitor;
    }

    public TestSupportMainControlsView getViewTestSupportMainControls() {
        return viewTestSupportMainControls;
    }

    public TestSupportMainProcessView getViewTestSupportMainProcess() {
        return viewTestSupportMainProcess;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        splitPaneMain = new JSplitPane();
        panelLeft = new JPanel();
        viewTestSupportMainControls = new TestSupportMainControlsView();
        viewCustomersSelection = new CustomersSelectionView();
        panelRight = new JPanel();
        viewTestSupportMainProcess = new TestSupportMainProcessView();
        tabbedPaneMonitor = new TestSupportMainTabView();

        //======== this ========
        setBorder(new EtchedBorder());
        setName("this");
        setLayout(new GridBagLayout());
        ((GridBagLayout)getLayout()).columnWidths = new int[] {0, 74, 0, 135, 367, 0};
        ((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0, 0};
        ((GridBagLayout)getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0, 1.0, 1.0, 1.0E-4};
        ((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 1.0, 1.0E-4};

        //======== splitPaneMain ========
        {
           splitPaneMain.setDividerLocation(340);
           splitPaneMain.setDividerSize(5);
           splitPaneMain.setName("splitPaneMain");

           //======== panelLeft ========
           {
              panelLeft.setName("panelLeft");
              panelLeft.setLayout(new BorderLayout());

              //---- viewTestSupportMainControls ----
              viewTestSupportMainControls.setBorder(new BevelBorder(BevelBorder.LOWERED));
              viewTestSupportMainControls.setName("viewTestSupportMainControls");
              panelLeft.add(viewTestSupportMainControls, BorderLayout.NORTH);

              //---- viewCustomersSelection ----
              viewCustomersSelection.setBorder(new BevelBorder(BevelBorder.LOWERED));
              viewCustomersSelection.setName("viewCustomersSelection");
              panelLeft.add(viewCustomersSelection, BorderLayout.CENTER);
           }
           splitPaneMain.setLeftComponent(panelLeft);

           //======== panelRight ========
           {
              panelRight.setBorder(null);
              panelRight.setName("panelRight");
              panelRight.setLayout(new BorderLayout());

              //---- viewTestSupportMainProcess ----
              viewTestSupportMainProcess.setName("viewTestSupportMainProcess");
              panelRight.add(viewTestSupportMainProcess, BorderLayout.NORTH);

              //======== tabbedPaneMonitor ========
              {
                 tabbedPaneMonitor.setName("tabbedPaneMonitor");
              }
              panelRight.add(tabbedPaneMonitor, BorderLayout.CENTER);
           }
           splitPaneMain.setRightComponent(panelRight);
        }
        add(splitPaneMain, new GridBagConstraints(0, 0, 5, 2, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(0, 0, 0, 0), 0, 0));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JSplitPane splitPaneMain;
    private JPanel panelLeft;
    private TestSupportMainControlsView viewTestSupportMainControls;
    private CustomersSelectionView viewCustomersSelection;
    private JPanel panelRight;
    private TestSupportMainProcessView viewTestSupportMainProcess;
    private TestSupportMainTabView tabbedPaneMonitor;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
