/*
 * Created by JFormDesigner on Sun Nov 23 16:06:06 CET 2014
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design;

import javax.swing.*;
import java.awt.*;

/**
 * @author Uzayli Temel
 */
public class SearchResultZipEntryDetailPanel extends JPanel {
    public SearchResultZipEntryDetailPanel() {
        initComponents();
    }

    public JLabel getLabelEntyName() {
        return labelEntyName;
    }

    public JTextField getTextFieldEntryName() {
        return textFieldEntryName;
    }

    public JScrollPane getScrollPaneEntryContent() {
        return scrollPaneEntryContent;
    }

    public JTextPane getTextPaneEntryContent() {
        return textPaneEntryContent;
    }

    public JPanel getPanelControls() {
        return panelControls;
    }

    public JComboBox getComboBoxSearchFor() {
        return comboBoxSearchFor;
    }

    public JButton getButtonSearchFor() {
        return buttonSearchFor;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panelControls = new JPanel();
        labelEntyName = new JLabel();
        textFieldEntryName = new JTextField();
        comboBoxSearchFor = new JComboBox();
        buttonSearchFor = new JButton();
        scrollPaneEntryContent = new JScrollPane();
        textPaneEntryContent = new JTextPane();

        //======== this ========
        setName("this");
        setLayout(new BorderLayout());

        //======== panelControls ========
        {
            panelControls.setName("panelControls");
            panelControls.setLayout(new GridBagLayout());
            ((GridBagLayout) panelControls.getLayout()).columnWidths = new int[]{0, 141, 141, 141, 136, 0};
            ((GridBagLayout) panelControls.getLayout()).rowHeights = new int[]{0, 0};
            ((GridBagLayout) panelControls.getLayout()).columnWeights = new double[]{0.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};
            ((GridBagLayout) panelControls.getLayout()).rowWeights = new double[]{0.0, 1.0E-4};

            //---- labelEntyName ----
            labelEntyName.setText("Entry-Name:");
            labelEntyName.setName("labelEntyName");
            panelControls.add(labelEntyName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 7), 0, 0));

            //---- textFieldEntryName ----
            textFieldEntryName.setName("textFieldEntryName");
            panelControls.add(textFieldEntryName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 7), 0, 0));

            //---- comboBoxSearchFor ----
            comboBoxSearchFor.setEditable(true);
            comboBoxSearchFor.setName("comboBoxSearchFor");
            panelControls.add(comboBoxSearchFor, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 7), 0, 0));

            //---- buttonSearchFor ----
            buttonSearchFor.setText("Suche...");
            buttonSearchFor.setName("buttonSearchFor");
            panelControls.add(buttonSearchFor, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 2), 0, 0));
        }
        add(panelControls, BorderLayout.PAGE_START);

        //======== scrollPaneEntryContent ========
        {
            scrollPaneEntryContent.setName("scrollPaneEntryContent");

            //---- textPaneEntryContent ----
            textPaneEntryContent.setName("textPaneEntryContent");
            scrollPaneEntryContent.setViewportView(textPaneEntryContent);
        }
        add(scrollPaneEntryContent, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panelControls;
    private JLabel labelEntyName;
    private JTextField textFieldEntryName;
    private JComboBox comboBoxSearchFor;
    private JButton buttonSearchFor;
    private JScrollPane scrollPaneEntryContent;
    private JTextPane textPaneEntryContent;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
