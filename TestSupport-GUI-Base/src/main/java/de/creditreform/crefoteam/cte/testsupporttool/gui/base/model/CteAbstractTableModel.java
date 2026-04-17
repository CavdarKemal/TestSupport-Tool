package de.creditreform.crefoteam.cte.testsupporttool.gui.base.model;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 1:1-Port aus {@code testsupport_client.tesun.gui.base.model.CteAbstractTableModel}.
 *
 * <p>Abstrakte {@link DefaultTableModel}-Basis mit eigener Daten-Liste fuer
 * Row-Objekte (statt der DefaultTableModel-internen Vektor-Struktur).
 * Sub-Klassen implementieren {@link #isRowActivated(int)} und
 * {@link #setRowActivated(int, boolean)}.
 */
abstract public class CteAbstractTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 1L;
    protected String[] COL_NAMES;

    // *** KORREKTUR: Interne Liste für die Datenobjekte. Keine direkte Initialisierung hier! ***
    protected List<Object> data;

    // Konstruktor ohne Initialdaten
    public CteAbstractTableModel() {
        // RUFE super() ZUERST AUF, damit DefaultTableModel seine Initialisierung abschließt.
        // DIES IST IMMER DER ALLERERSTE SCHRITT, WENN KEIN THIS() AUFGERUFEN WIRD.
        super();

        // DANACH initialisiere unsere interne Datenstruktur.
        // Jetzt ist sichergestellt, dass super() durchgelaufen ist und 'data' nicht 'null' ist,
        // wenn später darauf zugegriffen wird (z.B. durch andere Methoden).
        this.data = new ArrayList<>();
    }

    // Konstruktor mit Initialdaten
    public CteAbstractTableModel(List<?> tableelementsList) {
        this(); // Ruft den parameterlosen Konstruktor auf (dieser ruft super() auf und initialisiert this.data)

        if (tableelementsList != null) {
            this.data.addAll(tableelementsList);
        }
        // fireTableDataChanged() wird weiterhin vom Sub-Modell aufgerufen.
    }

    abstract public boolean isRowActivated(int rowIndex);
    abstract public void setRowActivated(int rowIndex, boolean isActivated);

    @Override
    public int getColumnCount() {
        if (COL_NAMES == null) {
            return 0; // Oder eine feste Anzahl, z.B. 3 für JvmJobInfo, bis COL_NAMES gesetzt ist
        }
        return COL_NAMES.length;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (getRowCount() > 0 && columnIndex >= 0 && columnIndex < getColumnCount()) {
            Object valueAt = getValueAt(0, columnIndex);
            if (valueAt != null) {
                return valueAt.getClass();
            }
        }
        return String.class;
    }

    @Override
    public String getColumnName(int index) {
        if (COL_NAMES == null || index < 0 || index >= COL_NAMES.length) {
            return "Col " + index;
        }
        return COL_NAMES[index];
    }

    @Override
    public int getRowCount() {
        // *** KORREKTUR: Hier eine Null-Prüfung einbauen! ***
        // Während der Initialisierungsphase ist 'data' null.
        // Wir müssen sicherstellen, dass getRowCount() immer einen gültigen Wert zurückgibt.
        return (data == null) ? 0 : data.size();
    }

    public Object getRow(int row) {
        if (data != null && row >= 0 && row < data.size()) { // Auch hier Null-Prüfung
            return data.get(row);
        }
        return null;
    }

    public void addRow(Object theData) {
        if (theData != null) {
            // Sicherstellen, dass data initialisiert ist, falls diese Methode vor dem Konstruktorende aufgerufen wird
            if (this.data == null) this.data = new ArrayList<>();
            int oldSize = data.size();
            data.add(theData);
            fireTableRowsInserted(oldSize, oldSize);
        }
    }

    public void addRows(Object[] theData) {
        if (theData != null && theData.length > 0) {
            if (this.data == null) this.data = new ArrayList<>();
            int oldSize = data.size();
            data.addAll(Arrays.asList(theData));
            fireTableRowsInserted(oldSize, data.size() - 1);
        }
    }

    public void addRows(List<?> theDataList) {
        if (theDataList != null && !theDataList.isEmpty()) {
            if (this.data == null) this.data = new ArrayList<>();
            int oldSize = data.size();
            data.addAll(theDataList);
            fireTableRowsInserted(oldSize, data.size() - 1);
        }
    }

    public void removeRow(Object theData) {
        if (theData != null && data != null) { // Null-Prüfung
            int index = data.indexOf(theData);
            if (index != -1) {
                data.remove(index);
                fireTableRowsDeleted(index, index);
            }
        }
    }

    public void replaceRow(Object oldRowData, Object newRowData) {
        if (oldRowData != null && newRowData != null && data != null) { // Null-Prüfung
            int index = data.indexOf(oldRowData);
            if (index != -1) {
                data.set(index, newRowData);
                fireTableRowsUpdated(index, index);
            }
        }
    }

    public void clearTable() {
        if (data != null) { // Null-Prüfung
            int oldSize = data.size();
            if (oldSize > 0) {
                data.clear();
                fireTableRowsDeleted(0, oldSize - 1);
            }
        }
    }

    public void setObjectAt(Object obj, int row) {
        if (data != null && row >= 0 && row < data.size()) { // Null-Prüfung
            data.set(row, obj);
            fireTableRowsUpdated(row, row);
        }
    }
}
