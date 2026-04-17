package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.ColumnsInfo;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design.SearchDefinitionsPanel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.model.SearchDefinitionsTableModel;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten.SEARCH_RESULT_TYPE;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.config.SearchConfiguration;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.config.SearchConfigurationFactory;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.SearchCriteria;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.SearchSpecification;
import org.apache.commons.configuration.ConfigurationException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.*;
import java.util.Map.Entry;

public class SearchDefinitionsView extends SearchDefinitionsPanel {
    private Frame parentFrame;
    private final static ColumnsInfo[] columnsInfos;

    static {
        // "#", "Name", "Ident", "Typ", "Invert", "Quellverzeichnis", "Ausgabeverzeichnis",
        columnsInfos = new ColumnsInfo[]{
                new ColumnsInfo(20, 20, 20), // #
                new ColumnsInfo(120, 200, 240), // Name
                new ColumnsInfo(70, 120, 140), // Ident
                new ColumnsInfo(80, 110, 120), // Typ
                new ColumnsInfo(80, 80, 80), // Processor
                new ColumnsInfo(30, 30, 30), // Invert
                new ColumnsInfo(260, 260, 0), // Quellverzeichnis
        };
    }

    public SearchDefinitionsView() {
        initControls();
        initListeners();
    }

    private void initControls() {
        List<SearchSpecification> theList = new ArrayList<>();
        SearchDefinitionsTableModel searchDefsTableModel = new SearchDefinitionsTableModel(theList);
        getTableSearchDefs().setModel(searchDefsTableModel);
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
                addRow(-1);
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
        getTableSearchDefs().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                handleSelectionEvent(listSelectionEvent);
            }
        });
        getTableSearchDefs().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    addRow(getTableSearchDefs().getSelectedRow());
                }
            }
        });

    }

    protected void cloneRow() {
        int selectedRow = getTableSearchDefs().getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        final SearchDefinitionsTableModel searchDefsTableModel = (SearchDefinitionsTableModel) getTableSearchDefs().getModel();
        int modelRowIndex = getTableSearchDefs().convertRowIndexToModel(selectedRow);
        SearchSpecification searchDefinition = (SearchSpecification) searchDefsTableModel.getRow(modelRowIndex);
        SearchSpecification clonedSearchDefinition = new SearchSpecification(searchDefinition);
        clonedSearchDefinition.setName(clonedSearchDefinition.getName() + "_Clone");
        openInDialog(clonedSearchDefinition, false, searchDefsTableModel);
    }

    protected void addRow(int selectedIndex) {
        final SearchSpecification searchDefinition;
        boolean isEdit = (selectedIndex > -1);
        final SearchDefinitionsTableModel searchDefsTableModel = (SearchDefinitionsTableModel) getTableSearchDefs().getModel();
        if (isEdit) {
            int modelRowIndex = getTableSearchDefs().convertRowIndexToModel(selectedIndex);
            searchDefinition = (SearchSpecification) searchDefsTableModel.getRow(modelRowIndex);
        } else {
            searchDefinition = new SearchSpecification("Neue Suche");
            searchDefinition.setCrefoNrTagName("crefonummer");
            searchDefinition.setSearchResultsType(SEARCH_RESULT_TYPE.CREFOS_XML);
        }
        openInDialog(searchDefinition, isEdit, searchDefsTableModel);
    }

    private void openInDialog(SearchSpecification searchDefinition, boolean isEdit, final SearchDefinitionsTableModel searchDefsTableModel) {
        final String strTitle = isEdit ? "Suchdefinition ändern" : "Neue Suchdefinition anlegen";
        SearchDefinitionsDialogView theDialogView = new SearchDefinitionsDialogView(parentFrame, strTitle);
        theDialogView.setModel(searchDefinition);
        theDialogView.setVisible(true);
        SearchSpecification modifiedSearchDefinition = theDialogView.getSearchDefinition();
        int rowIndex = getTableSearchDefs().getSelectedRow();
        if (modifiedSearchDefinition != null) {
            if (!isEdit) {
                searchDefsTableModel.addRow(modifiedSearchDefinition);
                rowIndex = searchDefsTableModel.getRowCount() - 1;
            }
            boolean isBoxSrcForAll = theDialogView.getCheckBoxSrcForAll().isSelected();
            boolean isCrfTagNameForAll = theDialogView.getCheckBoxCrfTagNameForAll().isSelected();
            boolean isInvertForAll = theDialogView.getCheckBoxInvertForAll().isSelected();
            if (isBoxSrcForAll || isCrfTagNameForAll || isInvertForAll) {
                int rowCount = searchDefsTableModel.getRowCount();
                for (int row = 0; row < rowCount; row++) {
                    searchDefinition = (SearchSpecification) searchDefsTableModel.getRow(row);
                    if (isBoxSrcForAll) {
                        searchDefinition.setSourceFile(modifiedSearchDefinition.getSourceFile());
                    }
                    if (isCrfTagNameForAll) {
                        searchDefinition.setCrefoNrTagName(modifiedSearchDefinition.getCrefoNrTagName());
                    }
                    if (isInvertForAll) {
                        searchDefinition.setInvertedResults(modifiedSearchDefinition.isInvertedResults());
                    }
                }
            }
            searchDefsTableModel.fireTableDataChanged();
            GUIStaticUtils.selectRowInTable(getTableSearchDefs(), rowIndex);
        }
    }

    private void removeRow() {
        int selectedRow = getTableSearchDefs().getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        SearchDefinitionsTableModel searchDefsTableModel = (SearchDefinitionsTableModel) getTableSearchDefs().getModel();
        int modelRowIndex = getTableSearchDefs().convertRowIndexToModel(selectedRow);
        searchDefsTableModel.removeRow(modelRowIndex);
        if (searchDefsTableModel.getRowCount() > 0) {
            getTableSearchDefs().setRowSelectionInterval(0, 0);
        } else {
            getViewlSearchCrits().setModel(null, true, false);
        }
    }

    protected void handleSelectionEvent(ListSelectionEvent listSelectionEvent) {
        if (listSelectionEvent.getValueIsAdjusting()) {
            return;
        }
        int selectedIndex = getTableSearchDefs().getSelectedRow();
        if (selectedIndex < 0) {
            return;
        }
        int modelRowIndex = getTableSearchDefs().convertRowIndexToModel(selectedIndex);
        SearchDefinitionsTableModel searchDefsTableModel = (SearchDefinitionsTableModel) getTableSearchDefs().getModel();
        SearchSpecification searchDefinition = (SearchSpecification) searchDefsTableModel.getRow(modelRowIndex);
        getViewlSearchCrits().setModel(searchDefinition.getSearchCriteriasList(), searchDefinition.isLogicalConnectionOr(), searchDefinition.isInvertedResults());
    }

    private void activateRows(int activeMode) {
        SearchDefinitionsTableModel tableModel = (SearchDefinitionsTableModel) getTableSearchDefs().getModel();
        int rowCount = tableModel.getRowCount();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            SearchSpecification searchDefinition = (SearchSpecification) tableModel.getRow(rowIndex);
            boolean isSelected = true; // activeMode = 1
            if (activeMode == 0) {
                isSelected = false;
            } else if (activeMode == -1) {
                isSelected = !searchDefinition.isActivated();
            }
            searchDefinition.setActivated(isSelected);
        }
        tableModel.fireTableDataChanged();
    }

    public SearchSpecification checkAndGetDirtyModel() {
        SearchDefinitionsTableModel searchDefsTableModel = (SearchDefinitionsTableModel) getTableSearchDefs().getModel();
        for (int rowIndex = 0; rowIndex < searchDefsTableModel.getRowCount(); rowIndex++) {
            SearchSpecification searchSpecification = (SearchSpecification) searchDefsTableModel.getRow(rowIndex);
            if (searchSpecification.isDirty()) {
                return searchSpecification;
            }
        }
        return null;
    }

    public void setModel(JFrame parentFrame, String cfgFileName) throws ConfigurationException {
        if (cfgFileName != null && !cfgFileName.isEmpty()) {
            SearchConfiguration searchConfiguration = SearchConfigurationFactory.createSearchConfiguration(cfgFileName);
            Map<String, SearchSpecification> searcDataMap = searchConfiguration.getZipSearcDataMap();
            setModel(parentFrame, searcDataMap);
        }
    }

    public void setModel(JFrame parentFrame, Map<String, SearchSpecification> searcDataMap) {
        this.parentFrame = parentFrame;
        List<SearchSpecification> theList = new ArrayList<>();
        Iterator<Entry<String, SearchSpecification>> iterator = searcDataMap.entrySet().iterator();
        while (iterator.hasNext()) {
            theList.add(iterator.next().getValue());
        }
        SearchDefinitionsTableModel searchDefsTableModel = new SearchDefinitionsTableModel(theList);
        getTableSearchDefs().setModel(searchDefsTableModel);
        getTableSearchDefs().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        getTableSearchDefs().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        TableColumnModel columnModel = getTableSearchDefs().getColumnModel();
        ColumnsInfo.setColumnsInfos(columnModel, columnsInfos);
        if (searchDefsTableModel.getRowCount() > 0) {
            getTableSearchDefs().setRowSelectionInterval(0, 0);
        }
    }

    public Map<String, SearchSpecification> getModel(boolean activeOnly) {
        Map<String, SearchSpecification> theMap = new TreeMap<>();
        SearchDefinitionsTableModel searchDefsTableModel = (SearchDefinitionsTableModel) getTableSearchDefs().getModel();
        for (int rowIndex = 0; rowIndex < searchDefsTableModel.getRowCount(); rowIndex++) {
            SearchSpecification theRow = (SearchSpecification) searchDefsTableModel.getRow(rowIndex);
            if (!activeOnly || theRow.isActivated()) {
                boolean allActive = true;
                if (activeOnly) {
                    allActive = false;
                    List<SearchCriteria> searchCriteriasList = theRow.getSearchCriteriasList();
                    for (SearchCriteria searchCriteria : searchCriteriasList) {
                        allActive |= searchCriteria.isActivated();
                    }
                }
                if (allActive) {
                    theMap.put(theRow.getName(), theRow);
                }
            }
        }
        return theMap;
    }

}
