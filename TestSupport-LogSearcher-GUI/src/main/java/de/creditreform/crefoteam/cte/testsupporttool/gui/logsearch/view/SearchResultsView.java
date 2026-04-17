package de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.ColumnsInfo;
import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.LogSearchSwingWorker;
import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.design.SearchResultsPanel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.model.LogFilesTableModel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.model.SearchResultsTableModel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.testsupporttool.logsearch.*;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchResultsView extends SearchResultsPanel implements IWorkerListener {
    private final static ColumnsInfo[] columnsInfos = new ColumnsInfo[]{
            new ColumnsInfo(120, 120, 120),           // "Datum"
            new ColumnsInfo(55, 55, 55),              // "Typ"
            new ColumnsInfo(65, 255, -1),             // "Package"
            new ColumnsInfo(65, 255, -1),             // "Infos"
    };
    private LogSearchSwingWorker theWorker = null;
    private SearchCriteria searchCriteria;
    private LogFilesTableModel logFilesTableModel;

    public SearchResultsView() {
        super();
        initModel();
        initListeners();
    }

    private void initModel() {
        SearchResultsTableModel searchResultsTableModel = new SearchResultsTableModel(new ArrayList<>());
        getTableSearchResults().setModel(searchResultsTableModel);
        getTableSearchResults().setDefaultRenderer(Date.class, new DateCellRenderer());
        // getTableSearchResults().getRowSorter().toggleSortOrder(0);
        getTableSearchResults().setEnabled(false);
        TableColumnModel columnModel = getTableSearchResults().getColumnModel();
        ColumnsInfo.setColumnsInfos(columnModel, columnsInfos);
        getLabelNumHits().setText("");
        getProgressBarFile().setVisible(true);
        getProgressBarFile().setValue(0);
        getProgressBarSummary().setVisible(true);
        getProgressBarSummary().setValue(0);
    }

    private void initControlsState() {
        getButtonSearch().setEnabled(!logFilesTableModel.getActiveRows().isEmpty());
        getTableSearchResults().setEnabled(theWorker == null);
        getTableSearchResults().getTableHeader().setEnabled(theWorker == null);
    }

    private void initListeners() {
        getButtonSearch().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSearch();
            }
        });
        getTableSearchResults().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                doRowClicked(mouseEvent);
            }
        });
        getProgressBarFile().setVisible(false);
        getProgressBarSummary().setVisible(false);
    }

    private void doRowClicked(MouseEvent mouseEvent) {
        if (theWorker != null) {
            return;
        }
        JTable theTable = (JTable) mouseEvent.getSource();
        int selectedRow = theTable.getSelectedRow();
        int rowInModel = theTable.convertRowIndexToModel(selectedRow);
        SearchResultsTableModel resultsTableModel = (SearchResultsTableModel) theTable.getModel();
        LogEntry logEntry = (LogEntry) resultsTableModel.getRow(rowInModel);
        if ((mouseEvent.getClickCount() == 2)) {
            LogEntryDialogView logEntryDialogView = new LogEntryDialogView(GUIStaticUtils.getParentFrame(this));
            logEntryDialogView.setSize(new Dimension(600, logEntry.getType().equals(LogEntry.ENTRY_TYPE.ERROR) ? 400 : 200));
            logEntryDialogView.setModel(logEntry);
            logEntryDialogView.setModal(true);
            logEntryDialogView.setVisible(true);
        }
    }

    public void addSearchResult(LogEntry logEntry) {
        SearchResultsTableModel searchResultsTableModel = (SearchResultsTableModel) getTableSearchResults().getModel();
        searchResultsTableModel.addRow(logEntry);
        getLabelNumHits().setText(searchResultsTableModel.getRowCount() + "");
    }

    public void addSearchResults(List<LogEntry> logEntryList) {
        SearchResultsTableModel searchResultsTableModel = (SearchResultsTableModel) getTableSearchResults().getModel();
        searchResultsTableModel.addRows(logEntryList.toArray(new LogEntry[]{}));
        getLabelNumHits().setText(searchResultsTableModel.getRowCount() + "");
    }

    public void doSearch() {
        List<SearchableLogFile> activelogFiles = logFilesTableModel.getActiveRows();
        String actionCommand = getButtonSearch().getText();
        if (actionCommand.contains("start")) {
            getButtonSearch().setText("Suche abbrechen");
            GUIStaticUtils.setWaitCursor(GUIStaticUtils.getParentFrame(this), true);
            initModel();
            theWorker = new LogSearchSwingWorker(this, activelogFiles, searchCriteria);
            theWorker.execute();
            initControlsState();
        } else {
            theWorker = null;
        }
    }

    public void setLogFilesTableModel(LogFilesTableModel logFilesTableModel) {
        this.logFilesTableModel = logFilesTableModel;
        initControlsState();
    }

    public void setSearchCriteria(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
        initControlsState();
    }

    /*******************************************************************************************
     ***********************             IWorkerlListener                ***********************
     *******************************************************************************************/
    @Override
    public void updateProgress(Object dataObject, int progressStep) {
        if (dataObject instanceof LogSearchSwingWorker) {
            getProgressBarSummary().setValue(progressStep);
            getProgressBarFile().setValue(0);
        } else if (dataObject instanceof SearchableLogFile) {
            getProgressBarFile().setValue(progressStep);
        }
    }

    @Override
    public void updateData(Object dataObject) {
        try {
            if (dataObject instanceof LogEntry) {
                addSearchResult((LogEntry) dataObject);
            } else if (dataObject instanceof SearchableLogFile) {
                AbstractSearchableLogFile searchableLogFile = (AbstractSearchableLogFile) dataObject;
                String path = searchableLogFile.getLogFile().getPath();
            }
        } catch (Exception ex) {
            GUIStaticUtils.setWaitCursor(GUIStaticUtils.getParentFrame(this), false);
            String errMsg = "Fehler beim Laden der LOG-Datei!\n" + ex.getLocalizedMessage();
            GUIStaticUtils.showExceptionMessage(this, errMsg, ex);
        }
    }

    @Override
    public void updateTaskState(TASK_STATE taskState) {
        switch (taskState) {
            case DONE: {
            }
            case CANCELLED: {
                getTableSearchResults().getRowSorter().toggleSortOrder(0);
                getTableSearchResults().getRowSorter().toggleSortOrder(0);
                getProgressBarFile().setVisible(false);
                getProgressBarSummary().setVisible(false);
                getTableSearchResults().setEnabled(true);
                GUIStaticUtils.setWaitCursor(GUIStaticUtils.getParentFrame(this), false);
                theWorker = null;
                getButtonSearch().setText("Suche starten");
                initControlsState();
            }
            break;
            default: {
                break;
            }
        }
    }

    @Override
    public boolean isCanceled() {
        return theWorker == null;
    }
    /*********************************   IWorkerListener   *************************************/

}
