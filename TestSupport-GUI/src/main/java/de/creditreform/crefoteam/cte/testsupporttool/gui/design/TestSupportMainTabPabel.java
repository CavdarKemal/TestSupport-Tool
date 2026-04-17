/*
 * Created by JFormDesigner on Sat Mar 28 14:41:27 CET 2026
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.design;

import java.awt.*;
import javax.swing.*;
import de.creditreform.crefoteam.cte.testsupporttool.gui.view.*;

/**
 * @author CavdarK
 */
public class TestSupportMainTabPabel extends JTabbedPane {
    public TestSupportMainTabPabel() {
        initComponents();
    }

    public JPanel getPanelLogs() {
        return panelLogs;
    }

    public JScrollPane getScrollPanelProcessImage() {
        return scrollPanelProcessImage;
    }

    public JTextArea getTextAreaTaskListenerInfo() {
        return textAreaTaskListenerInfo;
    }

    public JCheckBox getCheckBoxScrollToEnd() {
        return checkBoxScrollToEnd;
    }

    public JButton getButtonClearLOGPanel() {
        return buttonClearLOGPanel;
    }

    public TestResultsView getViewTestResults() {
        return viewTestResults;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panelLogs = new JPanel();
        scrollPaneTaskListenerInfo = new JScrollPane();
        textAreaTaskListenerInfo = new JTextArea();
        checkBoxScrollToEnd = new JCheckBox();
        buttonClearLOGPanel = new JButton();
        scrollPanelProcessImage = new JScrollPane();
        labelProcessImage = new JLabel();
        viewTestResults = new TestResultsView();

        //======== this ========

        //======== panelLogs ========
        {
            panelLogs.setLayout(new GridBagLayout());
            ((GridBagLayout)panelLogs.getLayout()).columnWidths = new int[] {0, 0, 0};
            ((GridBagLayout)panelLogs.getLayout()).rowHeights = new int[] {0, 0, 0};
            ((GridBagLayout)panelLogs.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
            ((GridBagLayout)panelLogs.getLayout()).rowWeights = new double[] {1.0, 0.0, 1.0E-4};

            //======== scrollPaneTaskListenerInfo ========
            {

                //---- textAreaTaskListenerInfo ----
                textAreaTaskListenerInfo.setTabSize(4);
                textAreaTaskListenerInfo.setLineWrap(true);
                textAreaTaskListenerInfo.setEditable(false);
                textAreaTaskListenerInfo.setBackground(Color.white);
                textAreaTaskListenerInfo.setFont(new Font("Verdana", Font.PLAIN, 12));
                textAreaTaskListenerInfo.setMinimumSize(new Dimension(455, 16));
                scrollPaneTaskListenerInfo.setViewportView(textAreaTaskListenerInfo);
            }
            panelLogs.add(scrollPaneTaskListenerInfo, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

            //---- checkBoxScrollToEnd ----
            checkBoxScrollToEnd.setText("Auto-Scroll");
            panelLogs.add(checkBoxScrollToEnd, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0));

            //---- buttonClearLOGPanel ----
            buttonClearLOGPanel.setIcon(new ImageIcon(getClass().getResource("/icons/table_replace.png")));
            buttonClearLOGPanel.setActionCommand("clearLOGs");
            buttonClearLOGPanel.setToolTipText("LOG's aus dem Panel l\u00f6schen");
            buttonClearLOGPanel.setText("LOG's l\u00f6schen");
            panelLogs.add(buttonClearLOGPanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                new Insets(2, 2, 2, 2), 0, 0));
        }
        addTab("LOG's", panelLogs);

        //======== scrollPanelProcessImage ========
        {
            scrollPanelProcessImage.setViewportView(labelProcessImage);
        }
        addTab("Prozess", scrollPanelProcessImage);
        addTab("Test-Results", viewTestResults);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel panelLogs;
    private JScrollPane scrollPaneTaskListenerInfo;
    private JTextArea textAreaTaskListenerInfo;
    private JCheckBox checkBoxScrollToEnd;
    private JButton buttonClearLOGPanel;
    private JScrollPane scrollPanelProcessImage;
    private JLabel labelProcessImage;
    private TestResultsView viewTestResults;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
