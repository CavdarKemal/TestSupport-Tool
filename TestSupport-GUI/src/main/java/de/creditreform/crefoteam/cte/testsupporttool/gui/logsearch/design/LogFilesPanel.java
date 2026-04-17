/*
 * Created by JFormDesigner on Wed Jun 24 15:36:10 CEST 2015
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.design;

import javax.swing.*;
import java.awt.*;

/**
 * @author Uzayli Temel
 */
public class LogFilesPanel extends JPanel {
    public LogFilesPanel() {
        initComponents();
    }

    public JScrollPane getScrollPaneLogFiles() {
        return scrollPaneLogFiles;
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

    public JButton getButtonInvertSelection() {
        return buttonInvertSelection;
    }

    public JLabel getLabelLogFiles() {
        return labelLogFiles;
    }

    public JTable getTableLogFiles() {
        return tableLogFiles;
    }

    public JButton getButtonRemoveAll() {
        return buttonRemoveAll;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        labelLogFiles = new JLabel();
        scrollPaneLogFiles = new JScrollPane();
        tableLogFiles = new JTable();
        buttonAdd = new JButton();
        buttonRemove = new JButton();
        buttonRemoveAll = new JButton();
        buttonSelectAll = new JButton();
        buttonSelectNone = new JButton();
        buttonInvertSelection = new JButton();

        //======== this ========
        setName("this");
        setLayout(new GridBagLayout());
        ((GridBagLayout) getLayout()).columnWidths = new int[]{92, 89, 0};
        ((GridBagLayout) getLayout()).rowHeights = new int[]{0, 0, 0, 0, 28, 0, 23, 0, 0, 0};
        ((GridBagLayout) getLayout()).columnWeights = new double[]{1.0, 0.0, 1.0E-4};
        ((GridBagLayout) getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 1.0E-4};

        //---- labelLogFiles ----
        labelLogFiles.setText("LOG Dateien");
        labelLogFiles.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
        labelLogFiles.setName("labelLogFiles");
        add(labelLogFiles, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 5, 7, 2), 0, 0));

        //======== scrollPaneLogFiles ========
        {
            scrollPaneLogFiles.setName("scrollPaneLogFiles");

            //---- tableLogFiles ----
            tableLogFiles.setAutoCreateRowSorter(true);
            tableLogFiles.setName("tableLogFiles");
            scrollPaneLogFiles.setViewportView(tableLogFiles);
        }
        add(scrollPaneLogFiles, new GridBagConstraints(0, 1, 1, 8, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 7), 0, 0));

        //---- buttonAdd ----
        buttonAdd.setIcon(new ImageIcon(getClass().getResource("/icons/add.png")));
        buttonAdd.setText("Hinzuf\u00fcgen");
        buttonAdd.setName("buttonAdd");
        add(buttonAdd, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 2), 0, 0));

        //---- buttonRemove ----
        buttonRemove.setIcon(new ImageIcon(getClass().getResource("/icons/cancel.png")));
        buttonRemove.setText("L\u00f6schen");
        buttonRemove.setEnabled(false);
        buttonRemove.setName("buttonRemove");
        add(buttonRemove, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 2), 0, 0));

        //---- buttonRemoveAll ----
        buttonRemoveAll.setIcon(new ImageIcon(getClass().getResource("/icons/cancel.png")));
        buttonRemoveAll.setText("Alle L\u00f6schen");
        buttonRemoveAll.setEnabled(false);
        buttonRemoveAll.setActionCommand("Alle L\u00f6schen");
        buttonRemoveAll.setName("buttonRemoveAll");
        add(buttonRemoveAll, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 2), 0, 0));

        //---- buttonSelectAll ----
        buttonSelectAll.setText("Alle");
        buttonSelectAll.setIcon(new ImageIcon(getClass().getResource("/icons/table_selection_all.png")));
        buttonSelectAll.setEnabled(false);
        buttonSelectAll.setName("buttonSelectAll");
        add(buttonSelectAll, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 2), 0, 0));

        //---- buttonSelectNone ----
        buttonSelectNone.setText("Keine");
        buttonSelectNone.setIcon(new ImageIcon(getClass().getResource("/icons/table_sql.png")));
        buttonSelectNone.setEnabled(false);
        buttonSelectNone.setName("buttonSelectNone");
        add(buttonSelectNone, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 2), 0, 0));

        //---- buttonInvertSelection ----
        buttonInvertSelection.setText("Umkehren");
        buttonInvertSelection.setIcon(new ImageIcon(getClass().getResource("/icons/table_replace.png")));
        buttonInvertSelection.setEnabled(false);
        buttonInvertSelection.setName("buttonInvertSelection");
        add(buttonInvertSelection, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 2), 0, 0));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel labelLogFiles;
    private JScrollPane scrollPaneLogFiles;
    private JTable tableLogFiles;
    private JButton buttonAdd;
    private JButton buttonRemove;
    private JButton buttonRemoveAll;
    private JButton buttonSelectAll;
    private JButton buttonSelectNone;
    private JButton buttonInvertSelection;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
