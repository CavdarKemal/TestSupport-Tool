package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.CteAbstractTableModel;
import de.creditreform.crefoteam.cte.tesun.util.TestCrefo;

import java.util.List;

public class TestCrefosTableModel extends CteAbstractTableModel {
    private static final long serialVersionUID = 1L;
    public static final String[] COLUMNS_FOR_TABLE = new String[]
            {
                    "#",
                    "Testfall",
                    "Ori-Crefo",
                    "Pseu-Crefo",
                    "Info",
                    "Soll Exp",
                    "Exprted",
            };

    public TestCrefosTableModel(List<TestCrefo> testCrefosList) {
        super(testCrefosList);
        COL_NAMES = COLUMNS_FOR_TABLE;
        fireTableDataChanged(); // Tabelle nach dem Laden der Daten aktualisieren
    }

    @Override
    public boolean isRowActivated(int rowIndex) {
        TestCrefo testCrefo = (TestCrefo) getRow(rowIndex);
        return testCrefo.isActivated();
    }

    @Override
    public void setRowActivated(int rowIndex, boolean isActivated) {
        if (!isCellEditable(rowIndex, 0)) {
            return;
        }
        TestCrefo testCrefo = (TestCrefo) getRow(rowIndex);
        testCrefo.setActivated(isActivated);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return "";
        }
        TestCrefo testCrefo = (TestCrefo) getRow(rowIndex);
        switch (columnIndex) {
            case 0: {
                return testCrefo.isActivated();
            }
            case 1: {
                return testCrefo.getTestFallName();
            }
            case 2: {
                return testCrefo.getItsqTestCrefoNr();
            }
            case 3: {
                return testCrefo.getPseudoCrefoNr();
            }
            case 4: {
                return testCrefo.getTestFallInfo();
            }
            case 5: {
                return testCrefo.isShouldBeExported();
            }
            case 6: {
                return testCrefo.isExported();
            }
        }
        return "";
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return;
        }
        TestCrefo testCrefo = (TestCrefo) getRow(rowIndex);
        try {
            switch (columnIndex) {
                case 0: {
                    Boolean oldValue = testCrefo.isActivated();
                    Boolean newValue = Boolean.parseBoolean(aValue.toString());
                    testCrefo.setActivated(newValue); // Aktiviert?
                    if (oldValue != newValue) {
                        fireTableRowsUpdated(rowIndex, rowIndex);
                    }
                }
                break;
                case 1: {
                    testCrefo.setTestFallName(aValue.toString());
                }
                break;
                case 2: {
                    testCrefo.setItsqTestCrefoNr(Long.parseLong(aValue.toString()));
                }
                break;
                case 3: {
                    testCrefo.setPseudoCrefoNr(Long.parseLong(aValue.toString()));
                }
                break;
                case 4: {
                    testCrefo.setTestFallInfo(aValue.toString());
                }
                break;
                case 5: {
                    Boolean newValue = Boolean.parseBoolean(aValue.toString());
                    testCrefo.setShouldBeExported(newValue);
                }
                break;
                case 6: {
                    Boolean newValue = Boolean.parseBoolean(aValue.toString());
                    testCrefo.setExported(newValue);
                }
                break;
            }
        } catch (Exception ex) {
            new RuntimeException(ex);
        }
    }
}
