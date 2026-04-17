/*
 * Created by JFormDesigner on Sun Nov 23 15:32:18 CET 2014
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design;

import javax.swing.*;
import java.awt.*;

/**
 * @author Uzayli Temel
 */
public class SearcResultDetailPanel extends JPanel {
    public SearcResultDetailPanel() {
        initComponents();
    }

    public JLabel getLabelSearchName() {
        return labelSearchName;
    }

    public JTextField getTextFieldSearchName() {
        return textFieldSearchName;
    }

    public JScrollPane getScrollPaneZipFileInfos() {
        return scrollPaneZipFileInfos;
    }

    public JTable getTableZipFileInfos() {
        return tableZipFileInfos;
    }

    public JPanel getPanelControls() {
        return panelControls;
    }

    public JLabel getLabelNumZipFiles() {
        return labelNumZipFiles;
    }

    public JTextField getTextFieldNumZipFiles() {
        return textFieldNumZipFiles;
    }

    public JLabel getLabelNumZipEntries() {
        return labelNumZipEntries;
    }

    public JTextField getTextFieldNumZipEntries() {
        return textFieldNumZipEntries;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panelControls = new JPanel();
        labelSearchName = new JLabel();
        textFieldSearchName = new JTextField();
        labelNumZipFiles = new JLabel();
        textFieldNumZipFiles = new JTextField();
        labelNumZipEntries = new JLabel();
        textFieldNumZipEntries = new JTextField();
        scrollPaneZipFileInfos = new JScrollPane();
        tableZipFileInfos = new JTable();

        //======== this ========
        setName("this");
        setLayout(new BorderLayout());

        //======== panelControls ========
        {
            panelControls.setName("panelControls");
            panelControls.setLayout(new GridBagLayout());
            ((GridBagLayout) panelControls.getLayout()).columnWidths = new int[]{0, 86, 111, 42, 87, 42, 0};
            ((GridBagLayout) panelControls.getLayout()).rowHeights = new int[]{0, 0};
            ((GridBagLayout) panelControls.getLayout()).columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
            ((GridBagLayout) panelControls.getLayout()).rowWeights = new double[]{0.0, 1.0E-4};

            //---- labelSearchName ----
            labelSearchName.setText("Suche:");
            labelSearchName.setName("labelSearchName");
            panelControls.add(labelSearchName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

            //---- textFieldSearchName ----
            textFieldSearchName.setEditable(false);
            textFieldSearchName.setName("textFieldSearchName");
            panelControls.add(textFieldSearchName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

            //---- labelNumZipFiles ----
            labelNumZipFiles.setText("Anzahl ZIP-Dateien:");
            labelNumZipFiles.setName("labelNumZipFiles");
            panelControls.add(labelNumZipFiles, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 7), 0, 0));

            //---- textFieldNumZipFiles ----
            textFieldNumZipFiles.setEditable(false);
            textFieldNumZipFiles.setName("textFieldNumZipFiles");
            panelControls.add(textFieldNumZipFiles, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 7), 0, 0));

            //---- labelNumZipEntries ----
            labelNumZipEntries.setText("Anzahl ZIP-Eintr.:");
            labelNumZipEntries.setName("labelNumZipEntries");
            panelControls.add(labelNumZipEntries, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 7), 0, 0));

            //---- textFieldNumZipEntries ----
            textFieldNumZipEntries.setEditable(false);
            textFieldNumZipEntries.setName("textFieldNumZipEntries");
            panelControls.add(textFieldNumZipEntries, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 2), 0, 0));
        }
        add(panelControls, BorderLayout.PAGE_START);

        //======== scrollPaneZipFileInfos ========
        {
            scrollPaneZipFileInfos.setName("scrollPaneZipFileInfos");

            //---- tableZipFileInfos ----
            tableZipFileInfos.setPreferredScrollableViewportSize(new Dimension(450, 180));
            tableZipFileInfos.setAutoCreateRowSorter(true);
            tableZipFileInfos.setBorder(null);
            tableZipFileInfos.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            tableZipFileInfos.setName("tableZipFileInfos");
            scrollPaneZipFileInfos.setViewportView(tableZipFileInfos);
        }
        add(scrollPaneZipFileInfos, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panelControls;
    private JLabel labelSearchName;
    private JTextField textFieldSearchName;
    private JLabel labelNumZipFiles;
    private JTextField textFieldNumZipFiles;
    private JLabel labelNumZipEntries;
    private JTextField textFieldNumZipEntries;
    private JScrollPane scrollPaneZipFileInfos;
    private JTable tableZipFileInfos;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
