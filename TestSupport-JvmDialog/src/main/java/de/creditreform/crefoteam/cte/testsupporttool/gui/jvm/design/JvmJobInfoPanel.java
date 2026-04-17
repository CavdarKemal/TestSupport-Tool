/*
 * Created by JFormDesigner on Thu Aug 16 17:30:39 CEST 2018
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.jvm.design;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.view.TableWithButtonsView;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * @author Kemal Cavdar
 */
public class JvmJobInfoPanel extends JPanel {
    public JvmJobInfoPanel() {
        initComponents();
    }

    public JPanel getPanelNorth() {
        return panelNorth;
    }

    public JLabel getLabelJobName() {
        return labelJobName;
    }

    public JTextField getTextFieldJobName() {
        return textFieldJobName;
    }

    public TableWithButtonsView getPaneJobExecutions() {
        return paneJobExecutions;
    }

    public JLabel getLabelJobExecutions() {
        return labelJobExecutions;
    }

    public JLabel getLabelExecCount() {
        return labelExecCount;
    }

    public JTextField getTextFieldExecCnt() {
        return textFieldExecCnt;
    }

    public JButton getButtonStartJob() {
        return buttonStartJob;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public JSplitPane getSplitPaneJobExecutions() {
        return splitPaneJobExecutions;
    }

    public JScrollPane getScrollPaneInfoArea() {
        return scrollPaneInfoArea;
    }

    public JTextArea getTextAreaInfo() {
        return textAreaInfo;
    }

    public JButton getButtonRefreshJobExecs() {
        return buttonRefreshJobExecs;
    }

    public JButton getButtonAbortJob() {
        return buttonAbortJob;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panelNorth = new JPanel();
        labelJobName = new JLabel();
        textFieldJobName = new JTextField();
        labelExecCount = new JLabel();
        textFieldExecCnt = new JTextField();
        labelJobExecutions = new JLabel();
        buttonRefreshJobExecs = new JButton();
        buttonStartJob = new JButton();
        buttonAbortJob = new JButton();
        splitPaneJobExecutions = new JSplitPane();
        paneJobExecutions = new TableWithButtonsView();
        scrollPaneInfoArea = new JScrollPane();
        textAreaInfo = new JTextArea();
        progressBar = new JProgressBar();

        //======== this ========
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setName("this");
        setLayout(new BorderLayout());

        //======== panelNorth ========
        {
            panelNorth.setName("panelNorth");
            panelNorth.setLayout(new GridBagLayout());
            ((GridBagLayout) panelNorth.getLayout()).columnWidths = new int[]{70, 87, 17, 17, 19, 50, 0};
            ((GridBagLayout) panelNorth.getLayout()).rowHeights = new int[]{0, 0, 0};
            ((GridBagLayout) panelNorth.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0E-4};
            ((GridBagLayout) panelNorth.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0E-4};

            //---- labelJobName ----
            labelJobName.setText("Job-Name:");
            labelJobName.setName("labelJobName");
            panelNorth.add(labelJobName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(4, 2, 7, 7), 0, 0));

            //---- textFieldJobName ----
            textFieldJobName.setEditable(false);
            textFieldJobName.setName("textFieldJobName");
            panelNorth.add(textFieldJobName, new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 7, 7), 0, 0));

            //---- labelExecCount ----
            labelExecCount.setText("Exec-Cnt:");
            labelExecCount.setName("labelExecCount");
            panelNorth.add(labelExecCount, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(4, 2, 7, 7), 0, 0));

            //---- textFieldExecCnt ----
            textFieldExecCnt.setEditable(false);
            textFieldExecCnt.setName("textFieldExecCnt");
            panelNorth.add(textFieldExecCnt, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 7, 2), 0, 0));

            //---- labelJobExecutions ----
            labelJobExecutions.setText("Job-Executions (letzte 20)");
            labelJobExecutions.setName("labelJobExecutions");
            panelNorth.add(labelJobExecutions, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 4, 2, 7), 0, 0));

            //---- buttonRefreshJobExecs ----
            buttonRefreshJobExecs.setIcon(new ImageIcon(getClass().getResource("/icons/refresh.png")));
            buttonRefreshJobExecs.setActionCommand("OK");
            buttonRefreshJobExecs.setName("buttonRefreshJobExecs");
            panelNorth.add(buttonRefreshJobExecs, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 7), 0, 0));

            //---- buttonStartJob ----
            buttonStartJob.setIcon(new ImageIcon(getClass().getResource("/icons/gear_run.png")));
            buttonStartJob.setActionCommand("OK");
            buttonStartJob.setText("Start Job...");
            buttonStartJob.setName("buttonStartJob");
            panelNorth.add(buttonStartJob, new GridBagConstraints(3, 1, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 7), 0, 0));

            //---- buttonAbortJob ----
            buttonAbortJob.setIcon(new ImageIcon(getClass().getResource("/icons/gear_run.png")));
            buttonAbortJob.setActionCommand("OK");
            buttonAbortJob.setText("Abbruch");
            buttonAbortJob.setName("buttonAbortJob");
            panelNorth.add(buttonAbortJob, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 2), 0, 0));
        }
        add(panelNorth, BorderLayout.NORTH);

        //======== splitPaneJobExecutions ========
        {
            splitPaneJobExecutions.setOrientation(JSplitPane.VERTICAL_SPLIT);
            splitPaneJobExecutions.setDividerLocation(180);
            splitPaneJobExecutions.setName("splitPaneJobExecutions");

            //---- paneJobExecutions ----
            paneJobExecutions.setName("paneJobExecutions");
            splitPaneJobExecutions.setTopComponent(paneJobExecutions);

            //======== scrollPaneInfoArea ========
            {
                scrollPaneInfoArea.setName("scrollPaneInfoArea");

                //---- textAreaInfo ----
                textAreaInfo.setEditable(false);
                textAreaInfo.setName("textAreaInfo");
                scrollPaneInfoArea.setViewportView(textAreaInfo);
            }
            splitPaneJobExecutions.setBottomComponent(scrollPaneInfoArea);
        }
        add(splitPaneJobExecutions, BorderLayout.CENTER);

        //---- progressBar ----
        progressBar.setName("progressBar");
        add(progressBar, BorderLayout.SOUTH);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panelNorth;
    private JLabel labelJobName;
    private JTextField textFieldJobName;
    private JLabel labelExecCount;
    private JTextField textFieldExecCnt;
    private JLabel labelJobExecutions;
    private JButton buttonRefreshJobExecs;
    private JButton buttonStartJob;
    private JButton buttonAbortJob;
    private JSplitPane splitPaneJobExecutions;
    private TableWithButtonsView paneJobExecutions;
    private JScrollPane scrollPaneInfoArea;
    private JTextArea textAreaInfo;
    private JProgressBar progressBar;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
