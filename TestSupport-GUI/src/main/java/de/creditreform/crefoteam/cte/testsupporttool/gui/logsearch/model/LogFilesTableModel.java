package de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.model;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.CteAbstractTableModel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.testsupporttool.logsearch.AbstractSearchableLogFile;
import de.creditreform.crefoteam.cte.testsupporttool.logsearch.SearchableLogFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LogFilesTableModel extends CteAbstractTableModel {
    public static final String[] COLUMNS_FOR_TABLE = new String[]{
            "#", "Dateiname"
    };

    public LogFilesTableModel(List<AbstractSearchableLogFile> logFiles) {
        super(logFiles);
        COL_NAMES = COLUMNS_FOR_TABLE;
        fireTableDataChanged();
    }

    @Override
    public boolean isRowActivated(int rowIndex) {
        AbstractSearchableLogFile logFile = (AbstractSearchableLogFile) getRow(rowIndex);
        return logFile.isActivated();
    }

    @Override
    public void setRowActivated(int rowIndex, boolean isActivated) {
        if (!isCellEditable(rowIndex, 0)) {
            return;
        }
        AbstractSearchableLogFile logFile = (AbstractSearchableLogFile) getRow(rowIndex);
        logFile.setActivated(isActivated);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Nur die erste Spalte (Checkbox) soll editierbar sein
        return columnIndex == 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return "";
        }
        AbstractSearchableLogFile logFile = (AbstractSearchableLogFile) getRow(rowIndex);
        switch (columnIndex) {
            case 0:
                return logFile.isActivated();
            case 1:
                return logFile.getLogFile().getPath();
        }
        return "";
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return;
        }
        AbstractSearchableLogFile logFile = (AbstractSearchableLogFile) getRow(rowIndex);
        try {
            switch (columnIndex) {
                case 0: {
                    Boolean oldValue = logFile.isActivated();
                    Boolean newValue = GUIStaticUtils.parseBoolean(aValue.toString());
                    logFile.setActivated(newValue);
                    if (oldValue != newValue) {
                        fireTableRowsUpdated(rowIndex, rowIndex);
                    }
                }
                break;
                case 1: {
                    if (aValue instanceof File) {
                        logFile.setLogFile((File) aValue);
                    } else {
                        logFile.setLogFile(new File((String) aValue));
                    }
                }
                break;
            }
        } catch (Exception ex) {
            new RuntimeException(ex);
        }
    }

    public List<SearchableLogFile> getActiveRows() {
        List<SearchableLogFile> result = new ArrayList<>();
        int rowCount = getRowCount();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            AbstractSearchableLogFile logFile = (AbstractSearchableLogFile) getRow(rowIndex);
            if (logFile.isActivated()) {
                result.add(logFile);
            }
        }
        return result;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
                return Boolean.class;
            default:
                return super.getColumnClass(columnIndex);
        }
    }
}
