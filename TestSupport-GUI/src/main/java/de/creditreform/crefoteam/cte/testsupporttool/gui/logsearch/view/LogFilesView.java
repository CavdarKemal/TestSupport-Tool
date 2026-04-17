package de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.ColumnsInfo;
import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.design.LogFilesPanel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.model.LogFilesTableModel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIFrame;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.testsupporttool.logsearch.AbstractSearchableLogFile;
import de.creditreform.crefoteam.cte.testsupporttool.logsearch.SearchableLogFile;
import de.creditreform.crefoteam.cte.testsupporttool.logsearch.SearchableLogFileFactory;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LogFilesView extends LogFilesPanel {
    private final static ColumnsInfo[] columnsInfos = new ColumnsInfo[]{
            new ColumnsInfo(25, 25, 25),                // ""
            new ColumnsInfo(65, 255, -1),               // "Dateiname"
    };

    private GUIFrame guiFrame;

    public LogFilesView() {
        super();
        initListeners();
        initModels();
    }

    private void initModels() {
        LogFilesTableModel logFilesTableModel = new LogFilesTableModel(new ArrayList<>());
        getTableLogFiles().setModel(logFilesTableModel);
        TableColumnModel columnModel = getTableLogFiles().getColumnModel();
        ColumnsInfo.setColumnsInfos(columnModel, columnsInfos);
    }

    private void initListeners() {
        getTableLogFiles().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                doRowClicked(mouseEvent);
            }
        });
        getButtonAdd().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLogFile();
            }
        });
        getButtonRemove().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeLogFile();
            }
        });
        getButtonRemoveAll().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getTableLogFiles().getSelectionModel().setSelectionInterval(0, getTableLogFiles().getRowCount() - 1);
                removeLogFile();
            }
        });
        getButtonSelectAll().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                activateRows(1);
            }
        });
        getButtonSelectNone().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                activateRows(0);
            }
        });
        getButtonInvertSelection().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                activateRows(-1);
            }
        });
    }

    private void doRowClicked(MouseEvent mouseEvent) {
        JTable theTable = (JTable) mouseEvent.getSource();
        int selectedRow = theTable.getSelectedRow();
        LogFilesTableModel logFilesTableModel = (LogFilesTableModel) theTable.getModel();
        AbstractSearchableLogFile searchableLogFile = (AbstractSearchableLogFile) logFilesTableModel.getRow(selectedRow);
        if ((mouseEvent.getClickCount() == 2)) {
        }
    }

    public GUIFrame getGuiFrame() {
        return guiFrame;
    }

    public void setGuiFrame(GUIFrame guiFrame) {
        this.guiFrame = guiFrame;
    }

    public void activateRows(int activeMode) {
        int selectedRow = getTableLogFiles().getSelectedRow();
        LogFilesTableModel logFilesTableModel = (LogFilesTableModel) getTableLogFiles().getModel();
        int rowCount = logFilesTableModel.getRowCount();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            AbstractSearchableLogFile searchableLogFile = (AbstractSearchableLogFile) logFilesTableModel.getRow(rowIndex);
            boolean isSelected = true; // activeMode = 1
            if (activeMode == 0) {
                isSelected = false;
            } else if (activeMode == -1) {
                isSelected = !searchableLogFile.isActivated();
            }
            searchableLogFile.setActivated(isSelected);
        }
        logFilesTableModel.fireTableDataChanged();
        getTableLogFiles().getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
        initControlsState();
    }

    protected void removeLogFile() {
        GUIStaticUtils.setWaitCursor(this, true);
        LogFilesTableModel tableModel = (LogFilesTableModel) getTableLogFiles().getModel();
        int[] selectedRows = getTableLogFiles().getSelectedRows();
        for (int i = selectedRows.length - 1; i > -1; i--) {
            int rowInModel = getTableLogFiles().convertRowIndexToModel(selectedRows[i]);
            tableModel.removeRow(rowInModel);
        }
        int rowCount = tableModel.getRowCount();
        getTableLogFiles().getSelectionModel().setSelectionInterval(rowCount - 1, rowCount - 1);
        initControlsState();
        GUIStaticUtils.setWaitCursor(this, false);
    }

    protected void addLogFile() {
        String logFilesPath = getGuiFrame().getEnvironmentConfig().getLastLoadPath();
        if ((logFilesPath == null) || logFilesPath.isEmpty()) {
            logFilesPath = System.getProperty("user.dir");
        }
        GUIStaticUtils.setWaitCursor(this, true);
        LogFilesTableModel model = (LogFilesTableModel) getTableLogFiles().getModel();
        List<String> logFileNames = GUIStaticUtils.chooseFileNames(this, logFilesPath, "log|zip|gz", true);
        File logFile = new File("");
        try {
            for (String logFileName : logFileNames) {
                SearchableLogFile searchableLogFile = SearchableLogFileFactory.createInstanceFor(logFileName);
                model.addRow(searchableLogFile);
                logFile = ((AbstractSearchableLogFile) searchableLogFile).getLogFile();
            }
            int rowCount = model.getRowCount();
            if (rowCount > 0) {
                getGuiFrame().getEnvironmentConfig().setLastLoadPath(logFile.getParent());
            }
            getTableLogFiles().getSelectionModel().setSelectionInterval(rowCount - 1, rowCount - 1);
        } catch (Throwable ex) {
            String errMsg = "Fehler beim Laden der LOG-Datei " + logFile.getPath() + "\n";
            GUIStaticUtils.showExceptionMessage(this, errMsg, ex);
        }
        GUIStaticUtils.setWaitCursor(this, false);
        initControlsState();
    }

    private void initControlsState() {
        int selectedRow = getTableLogFiles().getSelectedRow();
        boolean isSelected = selectedRow >= 0;
        if (isSelected) {
            getTableLogFiles().getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
            Rectangle cellRect = getTableLogFiles().getCellRect(selectedRow, selectedRow, true);
            getTableLogFiles().scrollRectToVisible(cellRect);
        }
        getButtonRemove().setEnabled(isSelected);
        getButtonRemoveAll().setEnabled(isSelected);
        getButtonSelectAll().setEnabled(isSelected);
        getButtonSelectNone().setEnabled(isSelected);
        getButtonInvertSelection().setEnabled(isSelected);
    }

    public JProgressBar getProgressBar(SearchableLogFile searchableLogFile) {
        LogFilesTableModel model = (LogFilesTableModel) getTableLogFiles().getModel();
        return null;
    }

}
