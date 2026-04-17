package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.model;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.CteAbstractTableModel;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.SearchCriteria;

import java.util.List;

public class SearchCriteriasTableModel extends CteAbstractTableModel {
    private static final long serialVersionUID = 1L;
    public static final String[] COLUMNS_FOR_TABLE = new String[]
            {
                    "#", "Name", "Wert",
            };

    public SearchCriteriasTableModel(List<SearchCriteria> theEntryList) {
        super(theEntryList);
        COL_NAMES = COLUMNS_FOR_TABLE;
        fireTableDataChanged(); // Tabelle nach dem Laden der Daten aktualisieren
    }

    @Override
    public boolean isRowActivated(int rowIndex) {
        SearchCriteria theEntry = (SearchCriteria) getRow(rowIndex);
        return theEntry.isActivated();
    }

    @Override
    public void setRowActivated(int rowIndex, boolean isActivated) {
        if (!isCellEditable(rowIndex, 0)) {
            return;
        }
        SearchCriteria theEntry = (SearchCriteria) getRow(rowIndex);
        theEntry.setActivated(isActivated);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return "";
        }
        SearchCriteria theEntry = (SearchCriteria) getRow(rowIndex);
        switch (columnIndex) {
            case 0: {
                return theEntry.isActivated();
            }
            case 1: {
                return theEntry.getSearchTag();
            }
            case 2: {
                return theEntry.getSearchValue();
            }
        }
        return "";
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return;
        }
        SearchCriteria theEntry = (SearchCriteria) getRow(rowIndex);
        switch (columnIndex) {
            case 0: {
                Boolean oldValue = theEntry.isActivated();
                Boolean newValue = Boolean.parseBoolean(aValue.toString());
                theEntry.setActivated(newValue); // Aktiviert?
                if (oldValue != newValue) {
                    fireTableRowsUpdated(rowIndex, rowIndex);
                }
            }
            break;
            case 1: {
                theEntry.setSearchTag((String) aValue);
            }
            break;
            case 2: {
                theEntry.setSearchValue((String) aValue);
            }
            break;
        }
    }
}
