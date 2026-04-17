package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.model;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.CteAbstractTableModel;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipSearcResult;

import java.util.List;

public class ZipSearcResultsTableModel extends CteAbstractTableModel {
    private static final long serialVersionUID = 1L;
    public static final String[] COLUMNS_FOR_TABLE = new String[]
            {
                    "Suche", "Anzahl ZIP-Dateien", "Anzahl ZIP-Eintr.",
            };

    public ZipSearcResultsTableModel(List<IZipSearcResult> zipSearcResultList) {
        super(zipSearcResultList);
        COL_NAMES = COLUMNS_FOR_TABLE;
        fireTableDataChanged(); // Tabelle nach dem Laden der Daten aktualisieren
    }

    @Override
    public boolean isRowActivated(int rowIndex) {
        return false;
    }

    @Override
    public void setRowActivated(int rowIndex, boolean isActivated) {
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return "";
        }
        IZipSearcResult zipSearcResult = (IZipSearcResult) getRow(rowIndex);
        switch (columnIndex) {
            case 0: {
                return zipSearcResult.getSearchName();
            }
            case 1: {
                return zipSearcResult.getNumZipFiles();
            }
            case 2: {
                return zipSearcResult.getNumZipEntries();
            }
        }
        return "";
    }

}
