/*
 * Created by JFormDesigner on Wed Jun 24 15:14:04 CEST 2015
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.design;

import javax.swing.*;
import java.awt.*;

/**
 * @author Uzayli Temel
 */
public class SearchCriteriasPanel extends JPanel {
    public SearchCriteriasPanel() {
        initComponents();
    }

    public JLabel getLabelSearchCritsType() {
        return labelSearchCritsType;
    }

    public JComboBox getComboBoxSearchCritsType() {
        return comboBoxSearchCritsType;
    }

    public JLabel getLabelSearchCritsFrom() {
        return labelSearchCritsFrom;
    }

    public JTextField getTextFieldSearchCritsFrom() {
        return textFieldSearchCritsFrom;
    }

    public JLabel getLabelSearchCritsTo() {
        return labelSearchCritsTo;
    }

    public JTextField getTextFieldSearchCritsTo() {
        return textFieldSearchCritsTo;
    }

    public JLabel getLabelSearchCritsPackage() {
        return labelSearchCritsPackage;
    }

    public JTextField getTextFieldSearchCritsPackage() {
        return textFieldSearchCritsPackage;
    }

    public JLabel getLabelSearchCritsInfo() {
        return labelSearchCritsInfo;
    }

    public JTextField getTextFieldSearchCritsInfo() {
        return textFieldSearchCritsInfo;
    }

    public JLabel getLabelSearchCriterias() {
        return labelSearchCriterias;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        labelSearchCriterias = new JLabel();
        labelSearchCritsType = new JLabel();
        comboBoxSearchCritsType = new JComboBox();
        labelSearchCritsFrom = new JLabel();
        textFieldSearchCritsFrom = new JTextField();
        labelSearchCritsTo = new JLabel();
        textFieldSearchCritsTo = new JTextField();
        labelSearchCritsPackage = new JLabel();
        textFieldSearchCritsPackage = new JTextField();
        labelSearchCritsInfo = new JLabel();
        textFieldSearchCritsInfo = new JTextField();

        //======== this ========
        setName("this");
        setLayout(new GridBagLayout());
        ((GridBagLayout) getLayout()).columnWidths = new int[]{92, 90, 70, 101, 0, 100, 95, 0};
        ((GridBagLayout) getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0};
        ((GridBagLayout) getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0E-4};
        ((GridBagLayout) getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0E-4};

        //---- labelSearchCriterias ----
        labelSearchCriterias.setText("Suchkriterien");
        labelSearchCriterias.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
        labelSearchCriterias.setName("labelSearchCriterias");
        add(labelSearchCriterias, new GridBagConstraints(0, 0, 7, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 5, 7, 2), 0, 0));

        //---- labelSearchCritsType ----
        labelSearchCritsType.setText("LOG-Typ:");
        labelSearchCritsType.setName("labelSearchCritsType");
        add(labelSearchCritsType, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 2, 7, 7), 0, 0));

        //---- comboBoxSearchCritsType ----
        comboBoxSearchCritsType.setName("comboBoxSearchCritsType");
        add(comboBoxSearchCritsType, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 2, 7, 7), 0, 0));

        //---- labelSearchCritsFrom ----
        labelSearchCritsFrom.setText("Datum vom:");
        labelSearchCritsFrom.setName("labelSearchCritsFrom");
        add(labelSearchCritsFrom, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                new Insets(5, 2, 7, 7), 0, 0));

        //---- textFieldSearchCritsFrom ----
        textFieldSearchCritsFrom.setText("*");
        textFieldSearchCritsFrom.setName("textFieldSearchCritsFrom");
        add(textFieldSearchCritsFrom, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 2, 7, 7), 0, 0));

        //---- labelSearchCritsTo ----
        labelSearchCritsTo.setText("bis:");
        labelSearchCritsTo.setName("labelSearchCritsTo");
        add(labelSearchCritsTo, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 2, 7, 7), 0, 0));

        //---- textFieldSearchCritsTo ----
        textFieldSearchCritsTo.setText("*");
        textFieldSearchCritsTo.setName("textFieldSearchCritsTo");
        add(textFieldSearchCritsTo, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 2, 7, 7), 0, 0));

        //---- labelSearchCritsPackage ----
        labelSearchCritsPackage.setText("Package:");
        labelSearchCritsPackage.setName("labelSearchCritsPackage");
        add(labelSearchCritsPackage, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 7), 0, 0));

        //---- textFieldSearchCritsPackage ----
        textFieldSearchCritsPackage.setText("*");
        textFieldSearchCritsPackage.setName("textFieldSearchCritsPackage");
        add(textFieldSearchCritsPackage, new GridBagConstraints(1, 2, 6, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 2), 0, 0));

        //---- labelSearchCritsInfo ----
        labelSearchCritsInfo.setText("Suchbegriff:");
        labelSearchCritsInfo.setName("labelSearchCritsInfo");
        add(labelSearchCritsInfo, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 7), 0, 0));

        //---- textFieldSearchCritsInfo ----
        textFieldSearchCritsInfo.setText("*");
        textFieldSearchCritsInfo.setName("textFieldSearchCritsInfo");
        add(textFieldSearchCritsInfo, new GridBagConstraints(1, 3, 6, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel labelSearchCriterias;
    private JLabel labelSearchCritsType;
    private JComboBox comboBoxSearchCritsType;
    private JLabel labelSearchCritsFrom;
    private JTextField textFieldSearchCritsFrom;
    private JLabel labelSearchCritsTo;
    private JTextField textFieldSearchCritsTo;
    private JLabel labelSearchCritsPackage;
    private JTextField textFieldSearchCritsPackage;
    private JLabel labelSearchCritsInfo;
    private JTextField textFieldSearchCritsInfo;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
