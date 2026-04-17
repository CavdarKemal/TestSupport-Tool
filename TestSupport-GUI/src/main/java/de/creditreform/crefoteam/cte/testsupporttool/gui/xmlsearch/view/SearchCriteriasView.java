package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.ColumnsInfo;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design.SearchCriteriasPanel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.model.SearchCriteriasTableModel;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.SearchCriteria;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class SearchCriteriasView extends SearchCriteriasPanel {
    private List<SearchCriteria> searchCriteriasList;
    private final static ColumnsInfo[] columnsInfos;

    static {
        // "#", "Name", "Wert",
        columnsInfos = new ColumnsInfo[]{
                new ColumnsInfo(20, 20, 20), // #
                new ColumnsInfo(120, 200, 640), // Name
                new ColumnsInfo(200, 260, 0), // Wert
        };
    }

    public SearchCriteriasView() {
        initListeners();
        getRadioButtonLogicOr().setSelected(true);
    }

    private void initListeners() {
        getButtonClone().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cloneRow();
            }
        });
        getButtonAdd().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                addRow();
            }
        });
        getButtonRemove().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                removeRow();
            }
        });
        getButtonSelectAll().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                activateRows(1);
            }
        });
        getButtonSelectNone().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                activateRows(0);
            }
        });
        getButtonSelectInvert().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                activateRows(-1);
            }
        });
        getTableSearchCriterias().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                handleSelectionEvent(listSelectionEvent);
            }
        });
        getTableSearchCriterias().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                }
            }
        });
    }

    protected void handleSelectionEvent(ListSelectionEvent listSelectionEvent) {
        if (listSelectionEvent.getValueIsAdjusting()) {
            return;
        }
        int selectedIndex = getTableSearchCriterias().getSelectedRow();
        if (selectedIndex < 0) {
            return;
        }
        int modelRowIndex = getTableSearchCriterias().convertRowIndexToModel(selectedIndex);
        SearchCriteriasTableModel tableModel = (SearchCriteriasTableModel) getTableSearchCriterias().getModel();
        SearchCriteria searchCriteria = (SearchCriteria) tableModel.getRow(modelRowIndex);
    }

    protected void cloneRow() {
        JTable tableSearchCriterias = getTableSearchCriterias();
        int selectedRow = tableSearchCriterias.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        SearchCriteriasTableModel tableModel = (SearchCriteriasTableModel) tableSearchCriterias.getModel();
        int modelRowIndex = tableSearchCriterias.convertRowIndexToModel(selectedRow);
        SearchCriteria searchCriteria = (SearchCriteria) tableModel.getRow(modelRowIndex);
        SearchCriteria clonedSearchCriteria = new SearchCriteria(searchCriteria);
        tableModel.addRow(clonedSearchCriteria);
        searchCriteriasList.add(clonedSearchCriteria);
        GUIStaticUtils.selectRowInTable(tableSearchCriterias, tableModel.getRowCount() - 1);
    }

    protected void addRow() {
        SearchCriteriasTableModel tableModel = (SearchCriteriasTableModel) getTableSearchCriterias().getModel();
        SearchCriteria searchCriteria = new SearchCriteria("searchTag", "searchValue");
        tableModel.addRow(searchCriteria);
        searchCriteriasList.add(searchCriteria);
    }

    private void removeRow() {
        int selectedRow = getTableSearchCriterias().getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        SearchCriteriasTableModel tableModel = (SearchCriteriasTableModel) getTableSearchCriterias().getModel();
        int modelRowIndex = getTableSearchCriterias().convertRowIndexToModel(selectedRow);
        SearchCriteria searchCriteria = (SearchCriteria) tableModel.getRow(modelRowIndex);
        tableModel.removeRow(searchCriteria);
        searchCriteriasList.remove(searchCriteria);
        if (tableModel.getRowCount() > 0) {
            getTableSearchCriterias().setRowSelectionInterval(0, 0);
        }
    }

    private void activateRows(int activeMode) {
        SearchCriteriasTableModel tableModel = (SearchCriteriasTableModel) getTableSearchCriterias().getModel();
        int rowCount = tableModel.getRowCount();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            SearchCriteria searchCriteria = (SearchCriteria) tableModel.getRow(rowIndex);
            boolean isSelected = true; // activeMode = 1
            if (activeMode == 0) {
                isSelected = false;
            } else if (activeMode == -1) {
                isSelected = !searchCriteria.isActivated();
            }
            searchCriteria.setActivated(isSelected);
        }
        tableModel.fireTableDataChanged();
    }

    public void setModel(List<SearchCriteria> searchCriteriasList, boolean isLogicalOr, boolean isInverted) {
        if (searchCriteriasList == null) {
            searchCriteriasList = new ArrayList<>();
        }
        this.searchCriteriasList = searchCriteriasList;
        getRadioButtonLogicOr().setSelected(isLogicalOr);
        getRadioButtonLogicAnd().setSelected(!isLogicalOr);
        getCheckBoxInverted().setSelected(isInverted);
        SearchCriteriasTableModel searchCritsModel = new SearchCriteriasTableModel(searchCriteriasList);
        getTableSearchCriterias().setModel(searchCritsModel);
        TableColumnModel columnModel = getTableSearchCriterias().getColumnModel();
        ColumnsInfo.setColumnsInfos(columnModel, columnsInfos);
        if (searchCritsModel.getRowCount() > 0) {
            getTableSearchCriterias().setRowSelectionInterval(0, 0);
        }
    }
}
