package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.model;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.CteAbstractTableModel;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipEntryInfo;

import java.util.List;

public class ZipFileInfoTableModel extends CteAbstractTableModel {
    private static final long serialVersionUID = 1L;
    public static final String[] COLUMNS_FOR_TABLE = new String[]
            {
                    "Crefonummer", "ZIP-Eintrag", "Dateiname"
            };

    public ZipFileInfoTableModel(List<? extends IZipEntryInfo> zipEntryInfoList) {
        super(zipEntryInfoList);
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
        IZipEntryInfo zipEntryInfo = (IZipEntryInfo) getRow(rowIndex);
        switch (columnIndex) {
            case 2: {
                return zipEntryInfo.getResultFileName();
            }
            case 1: {
                return zipEntryInfo.getZipEntryName();
            }
            case 0: {
                return zipEntryInfo.getCrefonummer();
            }
        }
        return "";
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return;
        }
        IZipEntryInfo zipEntryInfo = (IZipEntryInfo) getRow(rowIndex);
        try {
            switch (columnIndex) {
                case 0: {
                }
                break;
                case 1: {
                }
            }
        } catch (Exception ex) {
            new RuntimeException(ex);
        }
    }
}
