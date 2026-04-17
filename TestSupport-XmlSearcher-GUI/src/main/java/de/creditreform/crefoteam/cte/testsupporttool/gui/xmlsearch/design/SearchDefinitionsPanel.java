/*
 * Created by JFormDesigner on Mon Nov 24 10:37:19 CET 2014
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design;

import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.view.SearchCriteriasView;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * @author Uzayli Temel
 */
public class SearchDefinitionsPanel extends JPanel {
    public SearchDefinitionsPanel() {
        initComponents();
    }

    public JSplitPane getSplitPaneSearchDefs() {
        return splitPaneSearchDefs;
    }

    public JPanel getPanelSearchDefs() {
        return panelSearchDefs;
    }

    public SearchCriteriasView getViewlSearchCrits() {
        return viewlSearchCrits;
    }

    public JScrollPane getScrollPaneSearchDefs() {
        return scrollPaneSearchDefs;
    }

    public JTable getTableSearchDefs() {
        return tableSearchDefs;
    }

    public JLabel getLabelSearchDefs() {
        return labelSearchDefs;
    }

    public JButton getButtonAdd() {
        return buttonAdd;
    }

    public JButton getButtonRemove() {
        return buttonRemove;
    }

    public JButton getButtonSelectAll() {
        return buttonSelectAll;
    }

    public JButton getButtonSelectNone() {
        return buttonSelectNone;
    }

    public JButton getButtonSelectInvert() {
        return buttonSelectInvert;
    }

    public JButton getButtonClone() {
        return buttonClone;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        labelSearchDefs = new JLabel();
        splitPaneSearchDefs = new JSplitPane();
        panelSearchDefs = new JPanel();
        scrollPaneSearchDefs = new JScrollPane();
        tableSearchDefs = new JTable();
        buttonClone = new JButton();
        buttonAdd = new JButton();
        buttonRemove = new JButton();
        buttonSelectAll = new JButton();
        buttonSelectNone = new JButton();
        buttonSelectInvert = new JButton();
        viewlSearchCrits = new SearchCriteriasView();

        //======== this ========
        setName("this");
        setLayout(new GridBagLayout());
        ((GridBagLayout) getLayout()).columnWidths = new int[]{76, 222, 48, 217, 0};
        ((GridBagLayout) getLayout()).rowHeights = new int[]{0, 0, 0};
        ((GridBagLayout) getLayout()).columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, 1.0E-4};
        ((GridBagLayout) getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0E-4};

        //---- labelSearchDefs ----
        labelSearchDefs.setText("Suchdefinitionen");
        labelSearchDefs.setName("labelSearchDefs");
        add(labelSearchDefs, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(6, 2, 7, 7), 0, 0));

        //======== splitPaneSearchDefs ========
        {
            splitPaneSearchDefs.setDividerLocation(250);
            splitPaneSearchDefs.setOrientation(JSplitPane.VERTICAL_SPLIT);
            splitPaneSearchDefs.setName("splitPaneSearchDefs");

            //======== panelSearchDefs ========
            {
                panelSearchDefs.setBorder(new EtchedBorder(EtchedBorder.RAISED));
                panelSearchDefs.setName("panelSearchDefs");
                panelSearchDefs.setLayout(new GridBagLayout());
                ((GridBagLayout) panelSearchDefs.getLayout()).columnWidths = new int[]{0, 0, 0, 249, 0, 0};
                ((GridBagLayout) panelSearchDefs.getLayout()).rowHeights = new int[]{0, 0, 0, 0, 34, 0, 0};
                ((GridBagLayout) panelSearchDefs.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
                ((GridBagLayout) panelSearchDefs.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

                //======== scrollPaneSearchDefs ========
                {
                    scrollPaneSearchDefs.setName("scrollPaneSearchDefs");

                    //---- tableSearchDefs ----
                    tableSearchDefs.setAutoCreateRowSorter(true);
                    tableSearchDefs.setName("tableSearchDefs");
                    scrollPaneSearchDefs.setViewportView(tableSearchDefs);
                }
                panelSearchDefs.add(scrollPaneSearchDefs, new GridBagConstraints(0, 0, 4, 5, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 7, 7), 0, 0));

                //---- buttonClone ----
                buttonClone.setIcon(new ImageIcon(getClass().getResource("/icons/copy.png")));
                buttonClone.setName("buttonClone");
                panelSearchDefs.add(buttonClone, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 7, 2), 0, 0));

                //---- buttonAdd ----
                buttonAdd.setIcon(new ImageIcon(getClass().getResource("/icons/add.png")));
                buttonAdd.setName("buttonAdd");
                panelSearchDefs.add(buttonAdd, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 7, 2), 0, 0));

                //---- buttonRemove ----
                buttonRemove.setIcon(new ImageIcon(getClass().getResource("/icons/cancel.png")));
                buttonRemove.setName("buttonRemove");
                panelSearchDefs.add(buttonRemove, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 7, 2), 0, 0));

                //---- buttonSelectAll ----
                buttonSelectAll.setText("Alle");
                buttonSelectAll.setIcon(new ImageIcon(getClass().getResource("/icons/table_selection_all.png")));
                buttonSelectAll.setName("buttonSelectAll");
                panelSearchDefs.add(buttonSelectAll, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 2, 7), 0, 0));

                //---- buttonSelectNone ----
                buttonSelectNone.setText("Keine");
                buttonSelectNone.setIcon(new ImageIcon(getClass().getResource("/icons/table_sql.png")));
                buttonSelectNone.setName("buttonSelectNone");
                panelSearchDefs.add(buttonSelectNone, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 2, 7), 0, 0));

                //---- buttonSelectInvert ----
                buttonSelectInvert.setText("Umkehren");
                buttonSelectInvert.setIcon(new ImageIcon(getClass().getResource("/icons/table_replace.png")));
                buttonSelectInvert.setName("buttonSelectInvert");
                panelSearchDefs.add(buttonSelectInvert, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 2, 7), 0, 0));
            }
            splitPaneSearchDefs.setTopComponent(panelSearchDefs);

            //---- viewlSearchCrits ----
            viewlSearchCrits.setBorder(new EtchedBorder(EtchedBorder.RAISED));
            viewlSearchCrits.setName("viewlSearchCrits");
            splitPaneSearchDefs.setBottomComponent(viewlSearchCrits);
        }
        add(splitPaneSearchDefs, new GridBagConstraints(0, 1, 4, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel labelSearchDefs;
    private JSplitPane splitPaneSearchDefs;
    private JPanel panelSearchDefs;
    private JScrollPane scrollPaneSearchDefs;
    private JTable tableSearchDefs;
    private JButton buttonClone;
    private JButton buttonAdd;
    private JButton buttonRemove;
    private JButton buttonSelectAll;
    private JButton buttonSelectNone;
    private JButton buttonSelectInvert;
    private SearchCriteriasView viewlSearchCrits;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
