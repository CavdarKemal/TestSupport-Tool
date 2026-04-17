/*
 * Created by JFormDesigner on Mon Nov 24 12:00:26 CET 2014
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design;

import javax.swing.*;
import java.awt.*;

/**
 * @author Uzayli Temel
 */
public class SearchCriteriasPanel extends JPanel {
    public SearchCriteriasPanel() {
        initComponents();
    }

    public JLabel getLabelSearchCrits() {
        return labelSearchCrits;
    }

    public JScrollPane getScrollPaneSearchCriterias() {
        return scrollPaneSearchCriterias;
    }

    public JTable getTableSearchCriterias() {
        return tableSearchCriterias;
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

    public JRadioButton getRadioButtonLogicOr() {
        return radioButtonLogicOr;
    }

    public JRadioButton getRadioButtonLogicAnd() {
        return radioButtonLogicAnd;
    }

    public JCheckBox getCheckBoxInverted() {
        return checkBoxInverted;
    }

    public JSeparator getSeparator1() {
        return separator1;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        labelSearchCrits = new JLabel();
        radioButtonLogicOr = new JRadioButton();
        radioButtonLogicAnd = new JRadioButton();
        separator1 = new JSeparator();
        checkBoxInverted = new JCheckBox();
        scrollPaneSearchCriterias = new JScrollPane();
        tableSearchCriterias = new JTable();
        buttonClone = new JButton();
        buttonAdd = new JButton();
        buttonRemove = new JButton();
        buttonSelectAll = new JButton();
        buttonSelectNone = new JButton();
        buttonSelectInvert = new JButton();

        //======== this ========
        setName("this");
        setLayout(new GridBagLayout());
        ((GridBagLayout) getLayout()).columnWidths = new int[]{92, 90, 94, 75, 0, 88, 0, 0};
        ((GridBagLayout) getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
        ((GridBagLayout) getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0E-4};
        ((GridBagLayout) getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

        //---- labelSearchCrits ----
        labelSearchCrits.setText("Suchkriterien-Verkn\u00fcpfung:");
        labelSearchCrits.setName("labelSearchCrits");
        add(labelSearchCrits, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 7), 0, 0));

        //---- radioButtonLogicOr ----
        radioButtonLogicOr.setText("ODER");
        radioButtonLogicOr.setEnabled(false);
        radioButtonLogicOr.setName("radioButtonLogicOr");
        add(radioButtonLogicOr, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 7), 0, 0));

        //---- radioButtonLogicAnd ----
        radioButtonLogicAnd.setText("UND");
        radioButtonLogicAnd.setEnabled(false);
        radioButtonLogicAnd.setName("radioButtonLogicAnd");
        add(radioButtonLogicAnd, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 7), 0, 0));

        //---- separator1 ----
        separator1.setOrientation(SwingConstants.VERTICAL);
        separator1.setName("separator1");
        add(separator1, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 7), 0, 0));

        //---- checkBoxInverted ----
        checkBoxInverted.setText("Wert Invertiert");
        checkBoxInverted.setEnabled(false);
        checkBoxInverted.setName("checkBoxInverted");
        add(checkBoxInverted, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 7), 1, 0));

        //======== scrollPaneSearchCriterias ========
        {
            scrollPaneSearchCriterias.setName("scrollPaneSearchCriterias");

            //---- tableSearchCriterias ----
            tableSearchCriterias.setPreferredScrollableViewportSize(new Dimension(450, 180));
            tableSearchCriterias.setAutoCreateRowSorter(true);
            tableSearchCriterias.setBorder(null);
            tableSearchCriterias.setName("tableSearchCriterias");
            scrollPaneSearchCriterias.setViewportView(tableSearchCriterias);
        }
        add(scrollPaneSearchCriterias, new GridBagConstraints(0, 1, 6, 4, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 7), 0, 0));

        //---- buttonClone ----
        buttonClone.setIcon(new ImageIcon(getClass().getResource("/icons/copy.png")));
        buttonClone.setName("buttonClone");
        add(buttonClone, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 2), 0, 0));

        //---- buttonAdd ----
        buttonAdd.setIcon(new ImageIcon(getClass().getResource("/icons/add.png")));
        buttonAdd.setName("buttonAdd");
        add(buttonAdd, new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 2), 0, 0));

        //---- buttonRemove ----
        buttonRemove.setIcon(new ImageIcon(getClass().getResource("/icons/cancel.png")));
        buttonRemove.setName("buttonRemove");
        add(buttonRemove, new GridBagConstraints(6, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 2), 0, 0));

        //---- buttonSelectAll ----
        buttonSelectAll.setText("Alle");
        buttonSelectAll.setIcon(new ImageIcon(getClass().getResource("/icons/table_selection_all.png")));
        buttonSelectAll.setName("buttonSelectAll");
        add(buttonSelectAll, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 7), 0, 0));

        //---- buttonSelectNone ----
        buttonSelectNone.setText("Keine");
        buttonSelectNone.setIcon(new ImageIcon(getClass().getResource("/icons/table_sql.png")));
        buttonSelectNone.setName("buttonSelectNone");
        add(buttonSelectNone, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 7), 0, 0));

        //---- buttonSelectInvert ----
        buttonSelectInvert.setText("Umkehren");
        buttonSelectInvert.setIcon(new ImageIcon(getClass().getResource("/icons/table_replace.png")));
        buttonSelectInvert.setName("buttonSelectInvert");
        add(buttonSelectInvert, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 7), 0, 0));

        //---- buttonGroup1 ----
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(radioButtonLogicOr);
        buttonGroup1.add(radioButtonLogicAnd);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel labelSearchCrits;
    private JRadioButton radioButtonLogicOr;
    private JRadioButton radioButtonLogicAnd;
    private JSeparator separator1;
    private JCheckBox checkBoxInverted;
    private JScrollPane scrollPaneSearchCriterias;
    private JTable tableSearchCriterias;
    private JButton buttonClone;
    private JButton buttonAdd;
    private JButton buttonRemove;
    private JButton buttonSelectAll;
    private JButton buttonSelectNone;
    private JButton buttonSelectInvert;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
