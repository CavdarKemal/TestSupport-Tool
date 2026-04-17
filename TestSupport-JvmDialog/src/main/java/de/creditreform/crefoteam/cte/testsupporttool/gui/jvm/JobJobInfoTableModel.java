package de.creditreform.crefoteam.cte.testsupporttool.gui.jvm;

import de.creditreform.crefoteam.cte.jvmclient.JvmJobInfo;
import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.CteAbstractTableModel;

import java.util.List;

public class JobJobInfoTableModel extends CteAbstractTableModel {
    private static final long serialVersionUID = 1L;
    public static final String[] COLUMNS_FOR_TABLE = new String[]{"#", "Name", "Exec-Count"};

    public JobJobInfoTableModel(List<JvmJobInfo> jvmJobInfosList) {
        // Ruft den Konstruktor von CteAbstractTableModel auf,
        // der die jvmJobInfosList in unsere interne 'data' Liste kopiert.
        super(jvmJobInfosList);
        COL_NAMES = COLUMNS_FOR_TABLE;
        fireTableDataChanged(); // Tabelle nach dem Laden der Daten aktualisieren
    }

    @Override
    public boolean isRowActivated(int rowIndex) {
        // Greift über getRow() auf das JvmJobInfo-Objekt zu
        JvmJobInfo jvmJobInfo = (JvmJobInfo) getRow(rowIndex);
        return jvmJobInfo != null && jvmJobInfo.isActivated(); // Sicherer Zugriff und korrekter Wert
    }

    @Override
    public void setRowActivated(int rowIndex, boolean isActivated) {
        if (!isCellEditable(rowIndex, 0)) {
            return;
        }
        // Greift über getRow() auf das JvmJobInfo-Objekt zu
        JvmJobInfo jvmJobInfo = (JvmJobInfo) getRow(rowIndex);
        if (jvmJobInfo != null) {
            boolean oldValue = jvmJobInfo.isActivated();
            jvmJobInfo.setActivated(isActivated);
            if (oldValue != isActivated) {
                fireTableCellUpdated(rowIndex, 0); // Nur die betroffene Zelle aktualisieren
            }
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Nur die erste Spalte (Checkbox) soll editierbar sein
        return columnIndex == 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) { // Sicherstellen, dass der Index gültig ist
            return ""; // Oder ein leerer String, je nach gewünschtem Verhalten
        }
        // Greift über getRow() auf das JvmJobInfo-Objekt zu
        JvmJobInfo jvmJobInfo = (JvmJobInfo) getRow(rowIndex);
        switch (columnIndex) {
            case 0: {
                return jvmJobInfo.isActivated(); // Boolean für Checkbox
            }
            case 1: {
                return jvmJobInfo; // Das JvmJobInfo-Objekt selbst
            }
            case 2: {
                return jvmJobInfo.getExecutionCount();
            }
        }
        return null; // Sollte nie erreicht werden, wenn alle Spalten behandelt sind
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return;
        }
        JvmJobInfo jvmJobInfo = (JvmJobInfo) getRow(rowIndex);
        try {
            switch (columnIndex) {
                case 0: {
                    // aValue ist ein Boolean, direkt casten
                    Boolean oldValue = jvmJobInfo.isActivated();
                    Boolean newValue = (Boolean) aValue;
                    jvmJobInfo.setActivated(newValue);
                    if (oldValue != newValue) {
                        fireTableCellUpdated(rowIndex, columnIndex); // Spezifische Zelle aktualisieren
                    }
                }
                break;
                case 1: {
                    // Normalerweise nicht editierbar, falls doch:
                    // jvmJobInfo.setJobName(aValue.toString());
                    // fireTableCellUpdated(rowIndex, columnIndex);
                }
                break;
                case 2: {
                    // aValue ist Integer, direkt casten oder parsen
                    Integer oldValue = jvmJobInfo.getExecutionCount();
                    Integer newValue = (aValue instanceof Integer) ? (Integer) aValue : Integer.valueOf(aValue.toString());
                    jvmJobInfo.setExecutionCount(newValue);
                    if (!oldValue.equals(newValue)) {
                        fireTableCellUpdated(rowIndex, columnIndex); // Spezifische Zelle aktualisieren
                    }
                }
                break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Boolean.class; // Für Checkbox
            case 1:
                return JvmJobInfo.class; // Oder String.class, wenn toString() des JvmJobInfo angezeigt wird
            case 2:
                return Integer.class;
            default:
                return super.getColumnClass(columnIndex);
        }
    }
}
