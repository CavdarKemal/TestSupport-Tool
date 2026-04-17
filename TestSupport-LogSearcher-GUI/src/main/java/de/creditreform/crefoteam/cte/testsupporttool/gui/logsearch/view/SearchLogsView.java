package de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.design.SearchLogsPanel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.model.LogFilesTableModel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.model.SearchResultsTableModel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIFrame;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.testsupporttool.logsearch.AbstractSearchableLogFile;
import de.creditreform.crefoteam.cte.testsupporttool.logsearch.LogEntry;
import de.creditreform.crefoteam.cte.testsupporttool.logsearch.SearchCriteria;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class SearchLogsView extends SearchLogsPanel implements MouseListener, TableModelListener, SearchCriteriasListener {
    private final GUIFrame guiFrame;

    public SearchLogsView(GUIFrame guiFrame) {
        super();
        this.guiFrame = guiFrame;
        getSplitPanemain().setDividerLocation(600);

        JTable tableLogFiles = getLogFilesView().getTableLogFiles();
        tableLogFiles.addMouseListener(this);
        LogFilesTableModel logFilesTableModel = (LogFilesTableModel) tableLogFiles.getModel();
        logFilesTableModel.addTableModelListener(this);

        JTable tableSearchResults = getSearchResultsView().getTableSearchResults();
        tableSearchResults.addMouseListener(this);

        getLogFilesView().setGuiFrame(this.guiFrame);

        getSearchCriteriasView().addSearchCriteriasListener(this);

        getSearchResultsView().setLogFilesTableModel(logFilesTableModel);
        getSearchResultsView().setSearchCriteria(getSearchCriteriasView().getModel());
    }

    /*******************************************************************************************
     ***********************          SearchCriteriasListener            ***********************
     *******************************************************************************************/
    @Override
    public void updateSearchCriteria(SearchCriteria searchCriteria) {
        getSearchResultsView().setSearchCriteria(searchCriteria);
    }
    /***********************          SearchCriteriasListener            **********************/

    /*******************************************************************************************
     ***********************           TableModelListener                ***********************
     *******************************************************************************************/
    @Override
    public void tableChanged(TableModelEvent theEvent) {
        getSearchResultsView().setLogFilesTableModel((LogFilesTableModel) theEvent.getSource());
    }
    /*******************************************************************************************/

    /*******************************************************************************************
     ***********************                MouseListener                ***********************
     *******************************************************************************************/
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if ((mouseEvent.getClickCount() < 2)) {
            return;
        }

        JTable theTable = (JTable) mouseEvent.getSource();
        TableModel tableModel = theTable.getModel();
        if (tableModel instanceof SearchResultsTableModel) {
            selectRowsInLogFilesTableModel();
        } else if (tableModel instanceof LogFilesTableModel) {
            selectRowsInSearchResultsTableModel();
        }
    }

    private void selectRowsInSearchResultsTableModel() {
        GUIStaticUtils.setWaitCursor(this, true);
        JTable tableLogFiles = getLogFilesView().getTableLogFiles();
        LogFilesTableModel logFilesTableModel = (LogFilesTableModel) tableLogFiles.getModel();
        int rowInModel = tableLogFiles.convertRowIndexToModel(tableLogFiles.getSelectedRow());
        AbstractSearchableLogFile searchableLogFile = (AbstractSearchableLogFile) logFilesTableModel.getRow(rowInModel);
        File logFile = searchableLogFile.getLogFile();

        JTable tableSearchResults = getSearchResultsView().getTableSearchResults();
        tableSearchResults.clearSelection();
        SearchResultsTableModel searchResultsTableModel = (SearchResultsTableModel) tableSearchResults.getModel();
        int rowCount = searchResultsTableModel.getRowCount();
        for (int modelRowIndex = 0; modelRowIndex < rowCount; modelRowIndex++) {
            LogEntry logEntry = (LogEntry) searchResultsTableModel.getRow(modelRowIndex);
            if (logFile.equals(logEntry.getLogFile())) {
                int viewRowIndex = tableSearchResults.convertRowIndexToView(modelRowIndex);
                tableSearchResults.addRowSelectionInterval(viewRowIndex, viewRowIndex);
                Rectangle cellRect = tableSearchResults.getCellRect(viewRowIndex, viewRowIndex, true);
                tableSearchResults.scrollRectToVisible(cellRect);
            }
        }
        GUIStaticUtils.setWaitCursor(this, false);
    }

    private void selectRowsInLogFilesTableModel() {
        GUIStaticUtils.setWaitCursor(this, true);
        JTable tableSearchResults = getSearchResultsView().getTableSearchResults();
        SearchResultsTableModel searchResultsTableModel = (SearchResultsTableModel) tableSearchResults.getModel();
        int selectedRow = tableSearchResults.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        int rowInModel = tableSearchResults.convertRowIndexToModel(selectedRow);
        LogEntry logEntry = (LogEntry) searchResultsTableModel.getRow(rowInModel);
        File logFile = logEntry.getLogFile();

        JTable tableLogFiles = getLogFilesView().getTableLogFiles();
        tableLogFiles.clearSelection();
        LogFilesTableModel logFilesTableModel = (LogFilesTableModel) tableLogFiles.getModel();
        int rowCount = logFilesTableModel.getRowCount();
        for (int modelRowIndex = 0; modelRowIndex < rowCount; modelRowIndex++) {
            AbstractSearchableLogFile searchableLogFile = (AbstractSearchableLogFile) logFilesTableModel.getRow(modelRowIndex);
            if (logFile.equals(searchableLogFile.getLogFile())) {
                int viewRowIndex = tableLogFiles.convertRowIndexToView(modelRowIndex);
                tableLogFiles.addRowSelectionInterval(viewRowIndex, viewRowIndex);
                Rectangle cellRect = tableLogFiles.getCellRect(viewRowIndex, viewRowIndex, true);
                tableLogFiles.scrollRectToVisible(cellRect);
            }
        }

        GUIStaticUtils.setWaitCursor(this, false);

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }
    /***********************                MouseListener                ***********************/

}
