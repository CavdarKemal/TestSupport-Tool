/*
 * Created by JFormDesigner on Thu Nov 14 12:11:57 CET 2024
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.design;

import de.creditreform.crefoteam.cte.testsupporttool.gui.view.ClosableTabbedPane;
import de.creditreform.crefoteam.cte.testsupporttool.gui.view.TestResultsTabView;

import javax.swing.*;
import java.awt.*;

/**
 * @author CavdarK
 */
public class TestResultsPanel extends JPanel {
    public TestResultsPanel() {
        initComponents();
    }

    public TestResultsTabView getTestResultsTabView() {
        return testResultsTabView;
    }

    public ClosableTabbedPane getTabbedPaneTestResults() {
        return tabbedPaneTestResults;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        tabbedPaneTestResults = new ClosableTabbedPane();
        testResultsTabView = new TestResultsTabView();

        //======== this ========
        setLayout(new GridBagLayout());
        ((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0, 88, 0};
        ((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0};
        ((GridBagLayout)getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0, 1.0E-4};
        ((GridBagLayout)getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

        //======== tabbedPaneTestResults ========
        {
           tabbedPaneTestResults.addTab("", testResultsTabView);
        }
        add(tabbedPaneTestResults, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(0, 0, 0, 0), 0, 0));
      // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private ClosableTabbedPane tabbedPaneTestResults;
    private TestResultsTabView testResultsTabView;
   // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
