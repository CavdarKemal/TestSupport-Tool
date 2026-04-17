package de.creditreform.crefoteam.cte.testsupporttool.gui.jvm;

import de.creditreform.crefoteam.cte.jvmclient.JvmJobExecutionInfo;
import de.creditreform.crefoteam.cte.jvmclient.JvmJobInfo;
import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.CteAbstractTableModel;

import java.util.Date;
import java.util.List;

public class JobExecutionTableModel extends CteAbstractTableModel {
    private static final long serialVersionUID = 1L;
    public static final String[] COLUMNS_FOR_TABLE = new String[]{"#", "Id|Job-ID", "Running", "StartDate", "EndDate", "Status", "ExitCode"};

    public JobExecutionTableModel(List<JvmJobExecutionInfo> jvmJobExecutionInfoList) {
        super(jvmJobExecutionInfoList);
        COL_NAMES = COLUMNS_FOR_TABLE;
        fireTableDataChanged(); // Tabelle nach dem Laden der Daten aktualisieren
    }

    @Override
    public boolean isRowActivated(int rowIndex) {
        // Greift über getRow() auf das JvmJobInfo-Objekt zu
        JvmJobExecutionInfo jvmJobExecutionInfo = (JvmJobExecutionInfo) getRow(rowIndex);
        return true;
    }

    @Override
    public void setRowActivated(int rowIndex, boolean isActivated) {
        if (!isCellEditable(rowIndex, 0)) {
            return;
        }
        // Greift über getRow() auf das JvmJobInfo-Objekt zu
        JvmJobExecutionInfo jvmJobExecutionInfo = (JvmJobExecutionInfo) getRow(rowIndex);
        if (jvmJobExecutionInfo != null) {
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
        JvmJobExecutionInfo jvmJobExecutionInfo = (JvmJobExecutionInfo) getRow(rowIndex);
        switch (columnIndex) {
            case 0: {
                return Boolean.TRUE;
            }
            case 1: {
                return jvmJobExecutionInfo;
            }
            case 2: {
                return jvmJobExecutionInfo.getRunning();
            }
            case 3: {
                return JvmJobInfo.formatDateTime(jvmJobExecutionInfo.getStartDate());
            }
            case 4: {
                return JvmJobInfo.formatDateTime(jvmJobExecutionInfo.getEndDate());
            }
            case 5: {
                return jvmJobExecutionInfo.getStatus();
            }
            case 6: {
                return jvmJobExecutionInfo.getExitCode();
            }
        }
        return "";
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return;
        }
        JvmJobExecutionInfo jvmJobExecutionInfo = (JvmJobExecutionInfo) getRow(rowIndex);
        if (jvmJobExecutionInfo == null) {
            return;
        }
        try {
            switch (columnIndex) {
                case 0: {
                }
                break;
                case 1: {
                    // jvmJobExecutionInfo.setId( aValue.toString() );
                }
                break;
                case 2: {
                    jvmJobExecutionInfo.setRunning(aValue.toString());
                }
                break;
                case 3: {
                    jvmJobExecutionInfo.setStartDate(aValue.toString());
                }
                case 4: {
                    jvmJobExecutionInfo.setEndDate(aValue.toString());
                }
                case 5: {
                    jvmJobExecutionInfo.setStatus(aValue.toString());
                }
                break;
                case 6: {
                    jvmJobExecutionInfo.setExitCode(aValue.toString());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: {
            }
            case 1: {
                // jvmJobExecutionInfo.setId( aValue.toString() );
            }
            case 2: {
                return Boolean.class; // Für Checkbox
            }
            case 3: {
                return Date.class;
            }
            case 4: {
                return String.class;
            }
            case 5: {
                return String.class;
            }
            case 6: {
                return String.class;
            }
            default: {
                return super.getColumnClass(columnIndex);
            }
        }
    }
}
