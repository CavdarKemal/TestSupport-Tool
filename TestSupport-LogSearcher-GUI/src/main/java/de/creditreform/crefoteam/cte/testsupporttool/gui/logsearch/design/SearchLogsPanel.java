/*
 * Created by JFormDesigner on Wed Jun 24 16:18:36 CEST 2015
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.design;

import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.view.LogFilesView;
import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.view.SearchCriteriasView;
import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.view.SearchResultsView;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * @author Uzayli Temel
 */
public class SearchLogsPanel extends JPanel {
    public SearchLogsPanel() {
        initComponents();
    }

    public JSplitPane getSplitPanemain() {
        return splitPanemain;
    }

    public LogFilesView getLogFilesView() {
        return logFilesView;
    }

    public JPanel getPanelSearch() {
        return panelSearch;
    }

    public SearchCriteriasView getSearchCriteriasView() {
        return searchCriteriasView;
    }

    public SearchResultsView getSearchResultsView() {
        return searchResultsView;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        splitPanemain = new JSplitPane();
        logFilesView = new LogFilesView();
        panelSearch = new JPanel();
        searchCriteriasView = new SearchCriteriasView();
        searchResultsView = new SearchResultsView();

        //======== this ========
        setName("this");
        setLayout(new BorderLayout());

        //======== splitPanemain ========
        {
            splitPanemain.setResizeWeight(1.0);
            splitPanemain.setContinuousLayout(true);
            splitPanemain.setName("splitPanemain");

            //---- logFilesView ----
            logFilesView.setBorder(new BevelBorder(BevelBorder.LOWERED));
            logFilesView.setMinimumSize(new Dimension(500, 201));
            logFilesView.setName("logFilesView");
            splitPanemain.setLeftComponent(logFilesView);

            //======== panelSearch ========
            {
                panelSearch.setBorder(new BevelBorder(BevelBorder.LOWERED));
                panelSearch.setMinimumSize(new Dimension(500, 216));
                panelSearch.setName("panelSearch");
                panelSearch.setLayout(new BorderLayout());

                //---- searchCriteriasView ----
                searchCriteriasView.setBorder(new BevelBorder(BevelBorder.RAISED));
                searchCriteriasView.setName("searchCriteriasView");
                panelSearch.add(searchCriteriasView, BorderLayout.NORTH);

                //---- searchResultsView ----
                searchResultsView.setBorder(new BevelBorder(BevelBorder.RAISED));
                searchResultsView.setName("searchResultsView");
                panelSearch.add(searchResultsView, BorderLayout.CENTER);
            }
            splitPanemain.setRightComponent(panelSearch);
        }
        add(splitPanemain, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JSplitPane splitPanemain;
    private LogFilesView logFilesView;
    private JPanel panelSearch;
    private SearchCriteriasView searchCriteriasView;
    private SearchResultsView searchResultsView;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
