/*
 * Created by JFormDesigner on Sun Nov 23 15:32:26 CET 2014
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design;

import javax.swing.*;
import java.awt.*;

/**
 * @author Uzayli Temel
 */
public class SearcResultsDetailPanel extends JPanel {
    public SearcResultsDetailPanel() {
        initComponents();
    }

    public JScrollPane getScrollPaneSearchResults() {
        return scrollPaneSearchResults;
    }

    public JTable getTableSearchResults() {
        return tableSearchResults;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPaneSearchResults = new JScrollPane();
        tableSearchResults = new JTable();

        //======== this ========
        setName("this");
        setLayout(new BorderLayout());

        //======== scrollPaneSearchResults ========
        {
            scrollPaneSearchResults.setName("scrollPaneSearchResults");

            //---- tableSearchResults ----
            tableSearchResults.setPreferredScrollableViewportSize(new Dimension(450, 180));
            tableSearchResults.setAutoCreateRowSorter(true);
            tableSearchResults.setBorder(null);
            tableSearchResults.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            tableSearchResults.setName("tableSearchResults");
            scrollPaneSearchResults.setViewportView(tableSearchResults);
        }
        add(scrollPaneSearchResults, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPaneSearchResults;
    private JTable tableSearchResults;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
