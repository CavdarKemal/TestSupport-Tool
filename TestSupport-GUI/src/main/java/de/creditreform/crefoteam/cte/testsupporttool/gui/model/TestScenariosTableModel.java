package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.CteAbstractTableModel;
import de.creditreform.crefoteam.cte.tesun.util.TestScenario;

import java.util.List;

public class TestScenariosTableModel extends CteAbstractTableModel {
    private static final long serialVersionUID = 1L;
    public static final String[] COLUMNS_FOR_TABLE = new String[]{
            "#",
            "Scenario",
            "Crefos",
    };

    public TestScenariosTableModel(List<TestScenario> testScenarioList) {
        super(testScenarioList);
        COL_NAMES = COLUMNS_FOR_TABLE;
        fireTableDataChanged(); // Tabelle nach dem Laden der Daten aktualisieren
    }

    @Override
    public boolean isRowActivated(int rowIndex) {
        TestScenario testScenario = (TestScenario) getRow(rowIndex);
        return testScenario.isActivated();
    }

    @Override
    public void setRowActivated(int rowIndex, boolean isActivated) {
        if (!isCellEditable(rowIndex, 0)) {
            return;
        }
        TestScenario testScenario = (TestScenario) getRow(rowIndex);
        testScenario.setActivated(isActivated);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return "";
        }
        TestScenario testScenario = (TestScenario) getRow(rowIndex);
        switch (columnIndex) {
            case 0: {
                return testScenario.isActivated();
            }
            case 1: {
                return testScenario.getScenarioName();
            }
            case 2: {
                return testScenario.getTestCrefosAsList().size();
            }
        }
        return "";
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return;
        }
        TestScenario testScenario = (TestScenario) getRow(rowIndex);
        try {
            switch (columnIndex) {
                case 0: {
                    Boolean oldValue = testScenario.isActivated();
                    Boolean newValue = Boolean.parseBoolean(aValue.toString());
                    testScenario.setActivated(newValue); // Aktiviert?
                    if (oldValue != newValue) {
                        fireTableRowsUpdated(rowIndex, rowIndex);
                    }
                }
                break;
                case 1: {
                    testScenario.setScenarioName(aValue.toString());
                }
                break;
                case 2: {
                    // Nichts!
                }
                break;
            }
        } catch (Exception ex) {
            new RuntimeException(ex);
        }
    }
}
