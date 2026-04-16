package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.CteAbstractTableModel;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;

import java.util.List;

public class TestCustomersTableModel extends CteAbstractTableModel {
    private static final long serialVersionUID = 1L;
    public static final String[] COLUMNS_FOR_TABLE = new String[]
            {
                    "#",
                    "Kunde",
            };

    public TestCustomersTableModel(List<TestCustomer> testCustomerList) {
        super(testCustomerList);
        COL_NAMES = COLUMNS_FOR_TABLE;
        fireTableDataChanged(); // Tabelle nach dem Laden der Daten aktualisieren
    }

    @Override
    public boolean isRowActivated(int rowIndex) {
        TestCustomer testCustomer = (TestCustomer) getRow(rowIndex);
        return testCustomer.isActivated();
    }

    @Override
    public void setRowActivated(int rowIndex, boolean isActivated) {
        if (!isCellEditable(rowIndex, 0)) {
            return;
        }
        TestCustomer testCustomer = (TestCustomer) getRow(rowIndex);
        testCustomer.setActivated(isActivated);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return "";
        }
        TestCustomer testCustomer = (TestCustomer) getRow(rowIndex);
        switch (columnIndex) {
            case 0: {
                return testCustomer.isActivated();
            }
            case 1: {
                return testCustomer.getCustomerName();
            }
        }
        return "";
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return;
        }
        TestCustomer testCustomer = (TestCustomer) getRow(rowIndex);
        try {
            switch (columnIndex) {
                case 0: {
                    Boolean oldValue = testCustomer.isActivated();
                    Boolean newValue = Boolean.parseBoolean(aValue.toString());
                    testCustomer.setActivated(newValue); // Aktiviert?
                    if (oldValue != newValue) {
                        fireTableRowsUpdated(rowIndex, rowIndex);
                    }
                }
                break;
                case 1: {
                    testCustomer.setCustomerName(aValue.toString());
                }
                break;
            }
        } catch (Exception ex) {
            new RuntimeException(ex);
        }
    }
}
