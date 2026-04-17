package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.model;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.CteAbstractTableModel;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipFileInfo;

import java.util.List;

public class ZipSearcResultTableModel extends CteAbstractTableModel {
    private static final long serialVersionUID = 1L;
    public static final String[] COLUMNS_FOR_TABLE = new String[]
            {
                    "ZIP-Datei", "Anzahl ZIP-Einträge"
            };

    public ZipSearcResultTableModel(List<IZipFileInfo> ZipFileInfoList) {
        super(ZipFileInfoList);
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
        IZipFileInfo zipFileInfo = (IZipFileInfo) getRow(rowIndex);
        switch (columnIndex) {
            case 0: {
                return zipFileInfo.getZipFileName();
            }
            case 1: {
                return zipFileInfo.getZipEntryInfoList().size();
            }
        }
        return "";
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return;
        }
        IZipFileInfo zipFileInfo = (IZipFileInfo) getRow(rowIndex);
        try {
            switch (columnIndex) {
                case 0: {
                }
                break;
                case 1: {
                }
                break;
                case 2: {
                }
                break;
            }
        } catch (Exception ex) {
            new RuntimeException(ex);
        }
    }
}
