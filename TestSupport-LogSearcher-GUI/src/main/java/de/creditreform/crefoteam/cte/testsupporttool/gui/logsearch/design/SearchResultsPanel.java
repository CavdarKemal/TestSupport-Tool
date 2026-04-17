/*
 * Created by JFormDesigner on Wed Jun 24 15:55:19 CEST 2015
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.design;

import javax.swing.*;
import java.awt.*;

/**
 * @author Uzayli Temel
 */
public class SearchResultsPanel extends JPanel {
    public SearchResultsPanel() {
        initComponents();
    }

    public JScrollPane getScrollPaneSearchResults() {
        return scrollPaneSearchResults;
    }

    public JTable getTableSearchResults() {
        return tableSearchResults;
    }

    public JLabel getLabelSearchResults() {
        return labelSearchResults;
    }

    public JLabel getLabelNumHits() {
        return labelNumHits;
    }

    public JProgressBar getProgressBarFile() {
        return progressBarFile;
    }

    public JProgressBar getProgressBarSummary() {
        return progressBarSummary;
    }

    public JButton getButtonSearch() {
        return buttonSearch;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        labelSearchResults = new JLabel();
        labelNumHits = new JLabel();
        buttonSearch = new JButton();
        progressBarFile = new JProgressBar();
        progressBarSummary = new JProgressBar();
        scrollPaneSearchResults = new JScrollPane();
        tableSearchResults = new JTable();

        //======== this ========
        setName("this");
        setLayout(new GridBagLayout());
        ((GridBagLayout) getLayout()).columnWidths = new int[]{115, 58, 266, 0};
        ((GridBagLayout) getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0};
        ((GridBagLayout) getLayout()).columnWeights = new double[]{0.0, 0.0, 1.0, 1.0E-4};
        ((GridBagLayout) getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 1.0E-4};

        //---- labelSearchResults ----
        labelSearchResults.setText("Suchergebnisse");
        labelSearchResults.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
        labelSearchResults.setName("labelSearchResults");
        add(labelSearchResults, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 5, 7, 7), 0, 0));

        //---- labelNumHits ----
        labelNumHits.setName("labelNumHits");
        add(labelNumHits, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 7), 0, 0));

        //---- buttonSearch ----
        buttonSearch.setText("Suche starten");
        buttonSearch.setIcon(new ImageIcon(getClass().getResource("/icons/folder_view.png")));
        buttonSearch.setEnabled(false);
        buttonSearch.setName("buttonSearch");
        add(buttonSearch, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 2), 0, 0));

        //---- progressBarFile ----
        progressBarFile.setStringPainted(true);
        progressBarFile.setName("progressBarFile");
        add(progressBarFile, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 4, 7, 4), 0, 0));

        //---- progressBarSummary ----
        progressBarSummary.setStringPainted(true);
        progressBarSummary.setName("progressBarSummary");
        add(progressBarSummary, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 4, 7, 4), 0, 0));

        //======== scrollPaneSearchResults ========
        {
            scrollPaneSearchResults.setName("scrollPaneSearchResults");

            //---- tableSearchResults ----
            tableSearchResults.setAutoCreateRowSorter(true);
            tableSearchResults.setName("tableSearchResults");
            scrollPaneSearchResults.setViewportView(tableSearchResults);
        }
        add(scrollPaneSearchResults, new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 5, 2, 2), 0, 0));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel labelSearchResults;
    private JLabel labelNumHits;
    private JButton buttonSearch;
    private JProgressBar progressBarFile;
    private JProgressBar progressBarSummary;
    private JScrollPane scrollPaneSearchResults;
    private JTable tableSearchResults;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
