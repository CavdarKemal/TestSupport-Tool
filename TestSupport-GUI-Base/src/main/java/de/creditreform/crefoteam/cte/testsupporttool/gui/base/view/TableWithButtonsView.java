package de.creditreform.crefoteam.cte.testsupporttool.gui.base.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.design.TableWithButtonsPanel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.CteAbstractTableModel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.ColumnsInfo;

import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 1:1-Port aus {@code testsupport_client.tesun.gui.base.view.TableWithButtonsView}.
 *
 * <p>View-Schicht ueber dem JFormDesigner-Panel
 * {@link TableWithButtonsPanel} — verdrahtet die SelectAll/None/Invert-
 * Buttons mit dem gewaehlten {@link CteAbstractTableModel} und
 * delegiert TableColumn-Konfiguration an {@link ColumnsInfo}.
 */
public class TableWithButtonsView extends TableWithButtonsPanel {
    private ListSelectionListener listSelectionListener;

    public TableWithButtonsView() {
        super();
        initListeners();
    }

    private void initListeners() {
        getButtonSelectAll().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                activateRows(1);
            }
        });
        getButtonSelectNone().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                activateRows(0);
            }
        });
        getButtonSelectInvert().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                activateRows(-1);
            }
        });
    }

    private void activateRows(int activeMode) {
        CteAbstractTableModel tableModel = (CteAbstractTableModel) getTable().getModel();
        int rowCount = tableModel.getRowCount();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            boolean isActivated = tableModel.isRowActivated(rowIndex);
            if (activeMode == 0) {
                isActivated = Boolean.FALSE;
            } else if (activeMode == -1) {
                isActivated = !isActivated;
            } else  // 1
            {
                isActivated = Boolean.TRUE;
            }
            tableModel.setRowActivated(rowIndex, isActivated);
        }
        tableModel.fireTableDataChanged();
    }

    public void setTableCellRenderer(Class clazz, DefaultTableCellRenderer defaultTableCellRenderer) {
        getTable().setDefaultRenderer(clazz, defaultTableCellRenderer);
    }

    public void setModel(String strTitle, CteAbstractTableModel tableModel, ColumnsInfo[] colsInfo) {
        if (strTitle == null || strTitle.isEmpty()) {
            showButtons(false);
        } else {
            getLabelTitle().setText(strTitle);
        }
        getTable().setModel(tableModel);
        TableColumnModel columnModel = getTable().getColumnModel();
        ColumnsInfo.setColumnsInfos(columnModel, colsInfo);
        if (tableModel.getRowCount() > 0) {
            getTable().setRowSelectionInterval(0, 0);
        }
    }

    public void addListSelectionListener(ListSelectionListener listSelectionListener) {
        getTable().getSelectionModel().removeListSelectionListener(this.listSelectionListener);
        getTable().getSelectionModel().addListSelectionListener(listSelectionListener);
        this.listSelectionListener = listSelectionListener;
    }

    public int getSelectedRow() {
        int rowInModel = -1;
        int selectedIndex = getTable().getSelectedRow();
        if (selectedIndex > -1) {
            rowInModel = getTable().convertRowIndexToModel(selectedIndex);
        }
        return rowInModel;
    }

    public TableModel getModel() {
        return getTable().getModel();
    }

    public void enableButtons(boolean enable) {
        getPanelButtons().setEnabled(enable);
    }

    public void showButtons(boolean show) {
        getPanelButtons().setVisible(show);
        getPanelButtons().setEnabled(show);
    }
}
