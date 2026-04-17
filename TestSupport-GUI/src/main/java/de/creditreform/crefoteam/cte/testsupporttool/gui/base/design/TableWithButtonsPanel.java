/*
 * Created by JFormDesigner on Fri Jan 29 17:19:51 CET 2016
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.base.design;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * @author Uzayli Temel
 */
public class TableWithButtonsPanel extends JPanel {
    public TableWithButtonsPanel() {
        initComponents();
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JTable getTable() {
        return table;
    }

    public JLabel getLabelTitle() {
        return labelTitle;
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

    public JPanel getPanelButtons() {
        return panelButtons;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPane = new JScrollPane();
        table = new JTable();
        panelButtons = new JPanel();
        labelTitle = new JLabel();
        buttonSelectAll = new JButton();
        buttonSelectNone = new JButton();
        buttonSelectInvert = new JButton();

        //======== this ========
        setName("this");
        setLayout(new GridBagLayout());
        ((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
        ((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0, 0};
        ((GridBagLayout)getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0, 1.0, 1.0E-4};
        ((GridBagLayout)getLayout()).rowWeights = new double[] {1.0, 0.0, 1.0E-4};

        //======== scrollPane ========
        {
           scrollPane.setName("scrollPane");

           //---- table ----
           table.setPreferredScrollableViewportSize(new Dimension(450, 180));
           table.setAutoCreateRowSorter(true);
           table.setBorder(new BevelBorder(BevelBorder.LOWERED));
           table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
           table.setName("table");
           scrollPane.setViewportView(table);
        }
        add(scrollPane, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(0, 0, 5, 0), 0, 0));

        //======== panelButtons ========
        {
           panelButtons.setBorder(new EtchedBorder());
           panelButtons.setName("panelButtons");
           panelButtons.setLayout(new GridBagLayout());
           ((GridBagLayout)panelButtons.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
           ((GridBagLayout)panelButtons.getLayout()).rowHeights = new int[] {0, 0};
           ((GridBagLayout)panelButtons.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};
           ((GridBagLayout)panelButtons.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

           //---- labelTitle ----
           labelTitle.setText("????????");
           labelTitle.setName("labelTitle");
           panelButtons.add(labelTitle, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(2, 2, 2, 7), 0, 0));

           //---- buttonSelectAll ----
           buttonSelectAll.setIcon(new ImageIcon(getClass().getResource("/icons/table_selection_all.png")));
           buttonSelectAll.setToolTipText("Alle selektieren");
           buttonSelectAll.setMaximumSize(new Dimension(35, 30));
           buttonSelectAll.setMinimumSize(new Dimension(35, 30));
           buttonSelectAll.setPreferredSize(new Dimension(35, 30));
           buttonSelectAll.setName("buttonSelectAll");
           panelButtons.add(buttonSelectAll, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
              new Insets(2, 2, 2, 7), 0, 0));

           //---- buttonSelectNone ----
           buttonSelectNone.setIcon(new ImageIcon(getClass().getResource("/icons/table_sql.png")));
           buttonSelectNone.setToolTipText("Selektion l\u00f6schen");
           buttonSelectNone.setMaximumSize(new Dimension(35, 30));
           buttonSelectNone.setMinimumSize(new Dimension(35, 30));
           buttonSelectNone.setPreferredSize(new Dimension(35, 30));
           buttonSelectNone.setName("buttonSelectNone");
           panelButtons.add(buttonSelectNone, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
              new Insets(2, 2, 2, 7), 0, 0));

           //---- buttonSelectInvert ----
           buttonSelectInvert.setIcon(new ImageIcon(getClass().getResource("/icons/table_replace.png")));
           buttonSelectInvert.setToolTipText("Selektion umkehren");
           buttonSelectInvert.setMaximumSize(new Dimension(35, 30));
           buttonSelectInvert.setMinimumSize(new Dimension(35, 30));
           buttonSelectInvert.setPreferredSize(new Dimension(35, 30));
           buttonSelectInvert.setName("buttonSelectInvert");
           panelButtons.add(buttonSelectInvert, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
              new Insets(2, 2, 2, 2), 0, 0));
        }
        add(panelButtons, new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.BOTH,
           new Insets(2, 2, 2, 2), 0, 0));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPane;
    private JTable table;
    private JPanel panelButtons;
    private JLabel labelTitle;
    private JButton buttonSelectAll;
    private JButton buttonSelectNone;
    private JButton buttonSelectInvert;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
