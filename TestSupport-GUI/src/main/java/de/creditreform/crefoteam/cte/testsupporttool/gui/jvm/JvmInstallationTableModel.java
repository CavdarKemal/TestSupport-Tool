package de.creditreform.crefoteam.cte.testsupporttool.gui.jvm;

import de.creditreform.crefoteam.cte.jvmclient.JvmInstallation;
import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.CteAbstractTableModel;

import java.util.List;

public class JvmInstallationTableModel extends CteAbstractTableModel {
    private static final long serialVersionUID = 1L;
    public static final String[] COLUMNS_FOR_TABLE = new String[]{"#", "Name", "URL",};

    public JvmInstallationTableModel(List<JvmInstallation> jvmInstallationList) {
        super(jvmInstallationList);
        COL_NAMES = COLUMNS_FOR_TABLE;
        fireTableDataChanged(); // Tabelle nach dem Laden der Daten aktualisieren
    }

    @Override
    public boolean isRowActivated(int rowIndex) {
        JvmInstallation jvmInstallation = (JvmInstallation) getRow(rowIndex);
        return jvmInstallation.isActivated();
    }

    @Override
    public void setRowActivated(int rowIndex, boolean isActivated) {
        if (!isCellEditable(rowIndex, 0)) {
            return;
        }
        JvmInstallation jvmInstallation = (JvmInstallation) getRow(rowIndex);
        jvmInstallation.setActivated(isActivated);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (columnIndex == 0);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) { // Sicherstellen, dass der Index gültig ist
            return ""; // Oder ein leerer String, je nach gewünschtem Verhalten
        }
        JvmInstallation jvmInstallation = (JvmInstallation) getRow(rowIndex);
        switch (columnIndex) {
            case 0: {
                return jvmInstallation.isActivated();
            }
            case 1: {
                return jvmInstallation;
            }
            case 2: {
                return jvmInstallation.getJvmUrl();
            }
        }
        return "";
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return;
        }
        JvmInstallation jvmInstallation = (JvmInstallation) getRow(rowIndex);
        try {
            switch (columnIndex) {
                case 0: {
                    Boolean oldValue = jvmInstallation.isActivated();
                    Boolean newValue = Boolean.parseBoolean(aValue.toString());
                    jvmInstallation.setActivated(newValue); // Aktiviert?
                    if (oldValue != newValue) {
                        fireTableRowsUpdated(rowIndex, rowIndex);
                    }
                }
                break;
                case 1: {
                    //jvmInstallation.setJvmName( aValue.toString() );
                }
                break;
                case 2: {
                    jvmInstallation.setJvmUrl(aValue.toString());
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
                return Boolean.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            default:
                return super.getColumnClass(columnIndex);
        }
    }
}
