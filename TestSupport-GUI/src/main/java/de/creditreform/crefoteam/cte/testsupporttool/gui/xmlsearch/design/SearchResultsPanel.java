/*
 * Created by JFormDesigner on Sat Nov 22 19:15:41 CET 2014
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design;

import javax.swing.*;
import java.awt.*;

public class SearchResultsPanel extends JPanel {
    public SearchResultsPanel() {
        initComponents();
    }

    public JSplitPane getSplitPaneResults() {
        return splitPaneResults;
    }

    public JScrollPane getScrollPaneResultsTree() {
        return scrollPaneResultsTree;
    }

    public JTree getTreeResults() {
        return treeResults;
    }

    public JPanel getViewReusltsDetails() {
        return viewReusltsDetails;
    }

    public JPanel getPanelTree() {
        return panelTree;
    }

    public JProgressBar getProgressBarSearch() {
        return progressBarSearch;
    }

    public JButton getButtonStartStop() {
        return buttonStartStop;
    }

    public JTabbedPane getTabbedPaneDetails() {
        return tabbedPaneDetails;
    }

    public JPanel getPanelLogs() {
        return panelLogs;
    }

    public JPanel getPanelControls() {
        return panelControls;
    }

    public JLabel getLabelLogLevel() {
        return labelLogLevel;
    }

    public JRadioButton getRadioButtonLogLevelERROR() {
        return radioButtonLogLevelERROR;
    }

    public JRadioButton getRadioButtonLogLevelWARNING() {
        return radioButtonLogLevelWARNING;
    }

    public JRadioButton getRadioButtonLogLevelINFO() {
        return radioButtonLogLevelINFO;
    }

    public JRadioButton getRadioButtonLogLevelDEBUG() {
        return radioButtonLogLevelDEBUG;
    }

    public JButton getButtonClear() {
        return buttonClear;
    }

    public JScrollPane getScrollPaneLogs() {
        return scrollPaneLogs;
    }

    public JTextArea getTextAreaLogs() {
        return textAreaLogs;
    }

    public JCheckBox getCheckBoxShowEmptyNodes() {
        return checkBoxShowEmptyNodes;
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public JButton getButtonAnalyse() {
        return buttonAnalyse;
    }

    public JPanel getHSpacer2() {
        return hSpacer2;
    }

    public JPanel getHSpacer1() {
        return hSpacer1;
    }

    public JLabel getLabelAnalyse() {
        return labelAnalyse;
    }

    public JComboBox getComboBoxAnalyse() {
        return comboBoxAnalyse;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        checkBoxShowEmptyNodes = new JCheckBox();
        hSpacer2 = new JPanel(null);
        buttonStartStop = new JButton();
        hSpacer1 = new JPanel(null);
        labelAnalyse = new JLabel();
        comboBoxAnalyse = new JComboBox();
        buttonAnalyse = new JButton();
        splitPaneResults = new JSplitPane();
        panelTree = new JPanel();
        scrollPaneResultsTree = new JScrollPane();
        treeResults = new JTree();
        tabbedPaneDetails = new JTabbedPane();
        panelLogs = new JPanel();
        panelControls = new JPanel();
        labelLogLevel = new JLabel();
        radioButtonLogLevelERROR = new JRadioButton();
        radioButtonLogLevelWARNING = new JRadioButton();
        radioButtonLogLevelINFO = new JRadioButton();
        radioButtonLogLevelDEBUG = new JRadioButton();
        buttonClear = new JButton();
        scrollPaneLogs = new JScrollPane();
        textAreaLogs = new JTextArea();
        viewReusltsDetails = new JPanel();
        progressBarSearch = new JProgressBar();

        //======== this ========
        setName("this");
        setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setName("panel1");
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout) panel1.getLayout()).columnWidths = new int[]{0, 0, 211, 0, 0, 0, 0, 0};
            ((GridBagLayout) panel1.getLayout()).rowHeights = new int[]{0, 0};
            ((GridBagLayout) panel1.getLayout()).columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0E-4};
            ((GridBagLayout) panel1.getLayout()).rowWeights = new double[]{0.0, 1.0E-4};

            //---- checkBoxShowEmptyNodes ----
            checkBoxShowEmptyNodes.setText("Leere Bl\u00e4tter anzeigen");
            checkBoxShowEmptyNodes.setSelected(true);
            checkBoxShowEmptyNodes.setName("checkBoxShowEmptyNodes");
            panel1.add(checkBoxShowEmptyNodes, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 4, 7), 0, 0));

            //---- hSpacer2 ----
            hSpacer2.setName("hSpacer2");
            panel1.add(hSpacer2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

            //---- buttonStartStop ----
            buttonStartStop.setIcon(new ImageIcon(getClass().getResource("/icons/replace.png")));
            buttonStartStop.setText("Suche starten");
            buttonStartStop.setName("buttonStartStop");
            panel1.add(buttonStartStop, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 4, 7), 0, 0));

            //---- hSpacer1 ----
            hSpacer1.setName("hSpacer1");
            panel1.add(hSpacer1, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 7), 0, 0));

            //---- labelAnalyse ----
            labelAnalyse.setText("Analyse f\u00fcr");
            labelAnalyse.setName("labelAnalyse");
            panel1.add(labelAnalyse, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 7), 0, 0));

            //---- comboBoxAnalyse ----
            comboBoxAnalyse.setName("comboBoxAnalyse");
            panel1.add(comboBoxAnalyse, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 7), 0, 0));

            //---- buttonAnalyse ----
            buttonAnalyse.setIcon(new ImageIcon(getClass().getResource("/icons/gear_run.png")));
            buttonAnalyse.setText("starten");
            buttonAnalyse.setName("buttonAnalyse");
            panel1.add(buttonAnalyse, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 2), 0, 0));
        }
        add(panel1, BorderLayout.NORTH);

        //======== splitPaneResults ========
        {
            splitPaneResults.setDividerLocation(280);
            splitPaneResults.setName("splitPaneResults");

            //======== panelTree ========
            {
                panelTree.setName("panelTree");
                panelTree.setLayout(new BorderLayout());

                //======== scrollPaneResultsTree ========
                {
                    scrollPaneResultsTree.setName("scrollPaneResultsTree");

                    //---- treeResults ----
                    treeResults.setName("treeResults");
                    scrollPaneResultsTree.setViewportView(treeResults);
                }
                panelTree.add(scrollPaneResultsTree, BorderLayout.CENTER);
            }
            splitPaneResults.setLeftComponent(panelTree);

            //======== tabbedPaneDetails ========
            {
                tabbedPaneDetails.setName("tabbedPaneDetails");

                //======== panelLogs ========
                {
                    panelLogs.setName("panelLogs");
                    panelLogs.setLayout(new BorderLayout());

                    //======== panelControls ========
                    {
                        panelControls.setName("panelControls");
                        panelControls.setLayout(new GridBagLayout());
                        ((GridBagLayout) panelControls.getLayout()).columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
                        ((GridBagLayout) panelControls.getLayout()).rowHeights = new int[]{0, 0};
                        ((GridBagLayout) panelControls.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
                        ((GridBagLayout) panelControls.getLayout()).rowWeights = new double[]{0.0, 1.0E-4};

                        //---- labelLogLevel ----
                        labelLogLevel.setText("LOG-Level:");
                        labelLogLevel.setName("labelLogLevel");
                        panelControls.add(labelLogLevel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(2, 2, 2, 7), 0, 0));

                        //---- radioButtonLogLevelERROR ----
                        radioButtonLogLevelERROR.setText("ERROR");
                        radioButtonLogLevelERROR.setName("radioButtonLogLevelERROR");
                        panelControls.add(radioButtonLogLevelERROR, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(2, 2, 2, 7), 0, 0));

                        //---- radioButtonLogLevelWARNING ----
                        radioButtonLogLevelWARNING.setText("WARNING");
                        radioButtonLogLevelWARNING.setName("radioButtonLogLevelWARNING");
                        panelControls.add(radioButtonLogLevelWARNING, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(2, 2, 2, 7), 0, 0));

                        //---- radioButtonLogLevelINFO ----
                        radioButtonLogLevelINFO.setText("INFO");
                        radioButtonLogLevelINFO.setSelected(true);
                        radioButtonLogLevelINFO.setName("radioButtonLogLevelINFO");
                        panelControls.add(radioButtonLogLevelINFO, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(2, 2, 2, 7), 0, 0));

                        //---- radioButtonLogLevelDEBUG ----
                        radioButtonLogLevelDEBUG.setText("DEBUG");
                        radioButtonLogLevelDEBUG.setName("radioButtonLogLevelDEBUG");
                        panelControls.add(radioButtonLogLevelDEBUG, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(2, 2, 2, 7), 0, 0));

                        //---- buttonClear ----
                        buttonClear.setText("L\u00f6schen");
                        buttonClear.setName("buttonClear");
                        panelControls.add(buttonClear, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(2, 2, 2, 2), 0, 0));
                    }
                    panelLogs.add(panelControls, BorderLayout.NORTH);

                    //======== scrollPaneLogs ========
                    {
                        scrollPaneLogs.setName("scrollPaneLogs");

                        //---- textAreaLogs ----
                        textAreaLogs.setEditable(false);
                        textAreaLogs.setName("textAreaLogs");
                        scrollPaneLogs.setViewportView(textAreaLogs);
                    }
                    panelLogs.add(scrollPaneLogs, BorderLayout.CENTER);
                }
                tabbedPaneDetails.addTab("Logs", panelLogs);

                //======== viewReusltsDetails ========
                {
                    viewReusltsDetails.setName("viewReusltsDetails");
                    viewReusltsDetails.setLayout(new BorderLayout());
                }
                tabbedPaneDetails.addTab("Details", viewReusltsDetails);
            }
            splitPaneResults.setRightComponent(tabbedPaneDetails);
        }
        add(splitPaneResults, BorderLayout.CENTER);

        //---- progressBarSearch ----
        progressBarSearch.setName("progressBarSearch");
        add(progressBarSearch, BorderLayout.SOUTH);

        //---- buttonGroup ----
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonLogLevelERROR);
        buttonGroup.add(radioButtonLogLevelWARNING);
        buttonGroup.add(radioButtonLogLevelINFO);
        buttonGroup.add(radioButtonLogLevelDEBUG);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JCheckBox checkBoxShowEmptyNodes;
    private JPanel hSpacer2;
    private JButton buttonStartStop;
    private JPanel hSpacer1;
    private JLabel labelAnalyse;
    private JComboBox comboBoxAnalyse;
    private JButton buttonAnalyse;
    private JSplitPane splitPaneResults;
    private JPanel panelTree;
    private JScrollPane scrollPaneResultsTree;
    private JTree treeResults;
    private JTabbedPane tabbedPaneDetails;
    private JPanel panelLogs;
    private JPanel panelControls;
    private JLabel labelLogLevel;
    private JRadioButton radioButtonLogLevelERROR;
    private JRadioButton radioButtonLogLevelWARNING;
    private JRadioButton radioButtonLogLevelINFO;
    private JRadioButton radioButtonLogLevelDEBUG;
    private JButton buttonClear;
    private JScrollPane scrollPaneLogs;
    private JTextArea textAreaLogs;
    private JPanel viewReusltsDetails;
    private JProgressBar progressBarSearch;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
