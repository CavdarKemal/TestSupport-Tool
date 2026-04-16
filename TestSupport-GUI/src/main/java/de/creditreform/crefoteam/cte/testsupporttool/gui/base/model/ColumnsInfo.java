package de.creditreform.crefoteam.cte.testsupporttool.gui.base.model;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * 1:1-Port aus {@code testsupport_client.tesun.gui.base.model.ColumnsInfo}.
 *
 * <p>Datenklasse mit Min/Pref/Max-Width fuer eine JTable-Spalte. Stellt
 * sowohl Instanz- als auch eine statische Variante bereit, um ein
 * komplettes {@link TableColumnModel} mit einem Array von {@link ColumnsInfo}
 * zu konfigurieren.
 */
public class ColumnsInfo {
    int minWidth;
    int prefferedWidth;
    int maxWidth;

    public ColumnsInfo(int minWidth, int prefferedWidth, int maxWidth) {
        this.minWidth = minWidth;
        this.prefferedWidth = prefferedWidth;
        this.maxWidth = maxWidth;
    }

    public void setColumnsInfos(TableColumn column) {
        column.setMinWidth(minWidth);
        column.setPreferredWidth(prefferedWidth);
        if (maxWidth > 0) {
            column.setMaxWidth(maxWidth);
        }
    }

    public static void setColumnsInfos(TableColumnModel columnModel, ColumnsInfo[] columnsInfos) {
        for (int columnIndex = 0; columnIndex < columnsInfos.length; columnIndex++) {
            columnsInfos[columnIndex].setColumnsInfos(columnModel.getColumn(columnIndex));
        }
    }

}
