/*
 * Created by JFormDesigner on Sun Nov 23 16:00:41 CET 2014
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design;

import javax.swing.*;
import java.awt.*;

/**
 * @author Uzayli Temel
 */
public class SearchResultZipFileDetailPanel extends JPanel {
    public SearchResultZipFileDetailPanel() {
        initComponents();
    }

    public JLabel getLabelZipFileName() {
        return labelZipFileName;
    }

    public JTextField getTextFieldZipFileName() {
        return textFieldZipFileName;
    }

    public JScrollPane getScrollPaneZipEntries() {
        return scrollPaneZipEntries;
    }

    public JTable getTableZipEntries() {
        return tableZipEntries;
    }

    public JPanel getPanelControls() {
        return panelControls;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panelControls = new JPanel();
        labelZipFileName = new JLabel();
        textFieldZipFileName = new JTextField();
        scrollPaneZipEntries = new JScrollPane();
        tableZipEntries = new JTable();

        //======== this ========
        setName("this");
        setLayout(new BorderLayout());

        //======== panelControls ========
        {
            panelControls.setName("panelControls");
            panelControls.setLayout(new GridBagLayout());
            ((GridBagLayout) panelControls.getLayout()).columnWidths = new int[]{0, 172, 0};
            ((GridBagLayout) panelControls.getLayout()).rowHeights = new int[]{0, 0};
            ((GridBagLayout) panelControls.getLayout()).columnWeights = new double[]{0.0, 1.0, 1.0E-4};
            ((GridBagLayout) panelControls.getLayout()).rowWeights = new double[]{0.0, 1.0E-4};

            //---- labelZipFileName ----
            labelZipFileName.setText("ZIP-Name:");
            labelZipFileName.setName("labelZipFileName");
            panelControls.add(labelZipFileName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

            //---- textFieldZipFileName ----
            textFieldZipFileName.setName("textFieldZipFileName");
            panelControls.add(textFieldZipFileName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        add(panelControls, BorderLayout.PAGE_START);

        //======== scrollPaneZipEntries ========
        {
            scrollPaneZipEntries.setName("scrollPaneZipEntries");

            //---- tableZipEntries ----
            tableZipEntries.setPreferredScrollableViewportSize(new Dimension(450, 180));
            tableZipEntries.setAutoCreateRowSorter(true);
            tableZipEntries.setBorder(null);
            tableZipEntries.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            tableZipEntries.setName("tableZipEntries");
            scrollPaneZipEntries.setViewportView(tableZipEntries);
        }
        add(scrollPaneZipEntries, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panelControls;
    private JLabel labelZipFileName;
    private JTextField textFieldZipFileName;
    private JScrollPane scrollPaneZipEntries;
    private JTable tableZipEntries;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
