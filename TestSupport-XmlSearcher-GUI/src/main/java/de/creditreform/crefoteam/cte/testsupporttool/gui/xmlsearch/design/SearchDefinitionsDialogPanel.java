/*
 * Created by JFormDesigner on Mon Nov 24 14:41:51 CET 2014
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * @author Uzayli Temel
 */
public class SearchDefinitionsDialogPanel extends JDialog {
    public SearchDefinitionsDialogPanel(Frame owner) {
        super(owner);
        initComponents();
    }

    public SearchDefinitionsDialogPanel(Dialog owner) {
        super(owner);
        initComponents();
    }

    public JPanel getDialogPane() {
        return dialogPane;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public JLabel getLabelName() {
        return labelName;
    }

    public JTextField getTextFieldName() {
        return textFieldName;
    }

    public JLabel getLabelSource() {
        return labelSource;
    }

    public JTextField getTextFieldSourcePath() {
        return textFieldSourcePath;
    }

    public JButton getButtonSourcePath() {
        return buttonSourcePath;
    }

    public JLabel getLabelIdentifier() {
        return labelIdentifier;
    }

    public JTextField getTextFieldIdentifier() {
        return textFieldIdentifier;
    }

    public JButton getButtonOK() {
        return buttonOK;
    }

    public JButton getButtonCancel() {
        return buttonCancel;
    }

    public JLabel getLabelSearchType() {
        return labelSearchType;
    }

    public JComboBox getComboBoxSearchType() {
        return comboBoxSearchType;
    }

    public JCheckBox getCheckBoxInvert() {
        return checkBoxInvert;
    }

    public JCheckBox getCheckBoxSrcForAll() {
        return checkBoxSrcForAll;
    }

    public JCheckBox getCheckBoxCrfTagNameForAll() {
        return checkBoxCrfTagNameForAll;
    }

    public JCheckBox getCheckBoxInvertForAll() {
        return checkBoxInvertForAll;
    }

    public JLabel getLabelXmlProcessor() {
        return labelXmlProcessor;
    }

    public JComboBox getComboBoxXmlProcessor() {
        return comboBoxXmlProcessor;
    }

    public JLabel getLabelLogicalConnection() {
        return labelLogicalConnection;
    }

    public JRadioButton getRadioButtonLogicOr() {
        return radioButtonLogicOr;
    }

    public JRadioButton getRadioButtonLogicAnd() {
        return radioButtonLogicAnd;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        labelName = new JLabel();
        textFieldName = new JTextField();
        labelXmlProcessor = new JLabel();
        comboBoxXmlProcessor = new JComboBox();
        labelSource = new JLabel();
        textFieldSourcePath = new JTextField();
        checkBoxSrcForAll = new JCheckBox();
        buttonSourcePath = new JButton();
        labelIdentifier = new JLabel();
        textFieldIdentifier = new JTextField();
        checkBoxCrfTagNameForAll = new JCheckBox();
        labelLogicalConnection = new JLabel();
        radioButtonLogicOr = new JRadioButton();
        radioButtonLogicAnd = new JRadioButton();
        checkBoxInvert = new JCheckBox();
        checkBoxInvertForAll = new JCheckBox();
        labelSearchType = new JLabel();
        comboBoxSearchType = new JComboBox();
        buttonOK = new JButton();
        buttonCancel = new JButton();

        //======== this ========
        setName("this");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setName("dialogPane");
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
                contentPanel.setName("contentPanel");
                contentPanel.setLayout(new GridBagLayout());
                ((GridBagLayout) contentPanel.getLayout()).columnWidths = new int[]{0, 54, 80, 59, 141, 37, 32, 0};
                ((GridBagLayout) contentPanel.getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0, 0};
                ((GridBagLayout) contentPanel.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0E-4};
                ((GridBagLayout) contentPanel.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

                //---- labelName ----
                labelName.setText("Name:");
                labelName.setName("labelName");
                contentPanel.add(labelName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(4, 2, 7, 7), 0, 0));

                //---- textFieldName ----
                textFieldName.setName("textFieldName");
                contentPanel.add(textFieldName, new GridBagConstraints(1, 0, 3, 1, 1.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(4, 2, 7, 7), 0, 0));

                //---- labelXmlProcessor ----
                labelXmlProcessor.setText("XML-Processor:");
                labelXmlProcessor.setName("labelXmlProcessor");
                contentPanel.add(labelXmlProcessor, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 7, 7), 0, 0));

                //---- comboBoxXmlProcessor ----
                comboBoxXmlProcessor.setName("comboBoxXmlProcessor");
                contentPanel.add(comboBoxXmlProcessor, new GridBagConstraints(5, 0, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 7, 2), 0, 0));

                //---- labelSource ----
                labelSource.setText("Quelle:");
                labelSource.setName("labelSource");
                contentPanel.add(labelSource, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 7, 7), 0, 0));

                //---- textFieldSourcePath ----
                textFieldSourcePath.setName("textFieldSourcePath");
                contentPanel.add(textFieldSourcePath, new GridBagConstraints(1, 1, 4, 1, 1.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(2, 2, 7, 7), 0, 0));

                //---- checkBoxSrcForAll ----
                checkBoxSrcForAll.setText("*");
                checkBoxSrcForAll.setToolTipText("\u00dcbernehme f\u00fcr alle Suchdefinitionen");
                checkBoxSrcForAll.setName("checkBoxSrcForAll");
                contentPanel.add(checkBoxSrcForAll, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 7, 7), 0, 0));

                //---- buttonSourcePath ----
                buttonSourcePath.setIcon(new ImageIcon(getClass().getResource("/icons/folder_view.png")));
                buttonSourcePath.setPreferredSize(new Dimension(44, 24));
                buttonSourcePath.setMaximumSize(new Dimension(44, 24));
                buttonSourcePath.setMinimumSize(new Dimension(34, 24));
                buttonSourcePath.setName("buttonSourcePath");
                contentPanel.add(buttonSourcePath, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 7, 2), 0, 0));

                //---- labelIdentifier ----
                labelIdentifier.setText("Crefo-Tag:");
                labelIdentifier.setName("labelIdentifier");
                contentPanel.add(labelIdentifier, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 7, 7), 0, 0));

                //---- textFieldIdentifier ----
                textFieldIdentifier.setName("textFieldIdentifier");
                contentPanel.add(textFieldIdentifier, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 7, 7), 0, 0));

                //---- checkBoxCrfTagNameForAll ----
                checkBoxCrfTagNameForAll.setText("*");
                checkBoxCrfTagNameForAll.setToolTipText("\u00dcbernehme f\u00fcr alle Suchdefinitionen");
                checkBoxCrfTagNameForAll.setName("checkBoxCrfTagNameForAll");
                contentPanel.add(checkBoxCrfTagNameForAll, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 7, 7), 0, 0));

                //---- labelLogicalConnection ----
                labelLogicalConnection.setText("Log. Verkn.:");
                labelLogicalConnection.setName("labelLogicalConnection");
                contentPanel.add(labelLogicalConnection, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 7, 7), 0, 0));

                //---- radioButtonLogicOr ----
                radioButtonLogicOr.setText("ODER");
                radioButtonLogicOr.setName("radioButtonLogicOr");
                contentPanel.add(radioButtonLogicOr, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 7, 7), 0, 0));

                //---- radioButtonLogicAnd ----
                radioButtonLogicAnd.setText("UND");
                radioButtonLogicAnd.setName("radioButtonLogicAnd");
                contentPanel.add(radioButtonLogicAnd, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 7, 7), 0, 0));

                //---- checkBoxInvert ----
                checkBoxInvert.setText("Ergebnis invertieren");
                checkBoxInvert.setName("checkBoxInvert");
                contentPanel.add(checkBoxInvert, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 7, 7), 0, 0));

                //---- checkBoxInvertForAll ----
                checkBoxInvertForAll.setText("*");
                checkBoxInvertForAll.setToolTipText("\u00dcbernehme f\u00fcr alle Suchdefinitionen");
                checkBoxInvertForAll.setName("checkBoxInvertForAll");
                contentPanel.add(checkBoxInvertForAll, new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 7, 7), 0, 0));

                //---- labelSearchType ----
                labelSearchType.setText("Suchmethode:");
                labelSearchType.setName("labelSearchType");
                contentPanel.add(labelSearchType, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 2, 7), 0, 0));

                //---- comboBoxSearchType ----
                comboBoxSearchType.setName("comboBoxSearchType");
                contentPanel.add(comboBoxSearchType, new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 2, 7), 0, 0));

                //---- buttonOK ----
                buttonOK.setText("OK");
                buttonOK.setIcon(new ImageIcon(getClass().getResource("/icons/ok.png")));
                buttonOK.setName("buttonOK");
                contentPanel.add(buttonOK, new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 2, 7), 0, 0));

                //---- buttonCancel ----
                buttonCancel.setText("Abbruch");
                buttonCancel.setIcon(new ImageIcon(getClass().getResource("/icons/cancel.png")));
                buttonCancel.setName("buttonCancel");
                contentPanel.add(buttonCancel, new GridBagConstraints(5, 4, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 2, 2), 0, 0));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());

        //---- buttonGroup1 ----
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(radioButtonLogicOr);
        buttonGroup1.add(radioButtonLogicAnd);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel labelName;
    private JTextField textFieldName;
    private JLabel labelXmlProcessor;
    private JComboBox comboBoxXmlProcessor;
    private JLabel labelSource;
    private JTextField textFieldSourcePath;
    private JCheckBox checkBoxSrcForAll;
    private JButton buttonSourcePath;
    private JLabel labelIdentifier;
    private JTextField textFieldIdentifier;
    private JCheckBox checkBoxCrfTagNameForAll;
    private JLabel labelLogicalConnection;
    private JRadioButton radioButtonLogicOr;
    private JRadioButton radioButtonLogicAnd;
    private JCheckBox checkBoxInvert;
    private JCheckBox checkBoxInvertForAll;
    private JLabel labelSearchType;
    private JComboBox comboBoxSearchType;
    private JButton buttonOK;
    private JButton buttonCancel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
