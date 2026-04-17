package de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.model;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.CteAbstractTableModel;
import de.creditreform.crefoteam.cte.testsupporttool.logsearch.LogEntry;

import java.sql.Date;
import java.util.List;

public class SearchResultsTableModel extends CteAbstractTableModel {
    public static final String[] COLUMNS_FOR_TABLE = new String[]{
            "Datum", "Typ", "Package", "Infos", "Log-File"
    };

    public SearchResultsTableModel(List<LogEntry> logEntriesList) {
        super(logEntriesList);
        COL_NAMES = COLUMNS_FOR_TABLE;
        fireTableDataChanged();
    }

    @Override
    public boolean isRowActivated(int rowIndex) {
        return false;
    }

    @Override
    public void setRowActivated(int rowIndex, boolean isActivated) {
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Date.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return "";
        }
        LogEntry logEntry = (LogEntry) getRow(rowIndex);
        switch (columnIndex) {
            case 0: return logEntry.getLogDate();
            case 1: return logEntry.getType();
            case 2: return logEntry.getPackg();
            case 3: return logEntry.getInfoList();
            case 4: return logEntry.getLogFile().getName();
        }
        return "";
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // Tabelle ist nicht editierbar — nichts zu tun.
    }
}
