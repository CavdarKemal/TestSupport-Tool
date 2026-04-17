package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.model;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.CteAbstractTableModel;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten.SEARCH_RESULT_TYPE;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.SearchSpecification;

import java.io.File;
import java.util.List;

public class SearchDefinitionsTableModel extends CteAbstractTableModel {
    private static final long serialVersionUID = 1L;
    public static final String[] COLUMNS_FOR_TABLE = new String[]
            {
                    "#", "Name", "Ident", "Typ", "PROCESSOR", "Inv", "Quellverzeichnis",
            };

    public SearchDefinitionsTableModel(List<SearchSpecification> searchDefinitionList) {
        super(searchDefinitionList);
        COL_NAMES = COLUMNS_FOR_TABLE;
        fireTableDataChanged(); // Tabelle nach dem Laden der Daten aktualisieren
    }

    @Override
    public boolean isRowActivated(int rowIndex) {
        SearchSpecification searchDefinition = (SearchSpecification) getRow(rowIndex);
        return searchDefinition.isActivated();
    }

    @Override
    public void setRowActivated(int rowIndex, boolean isActivated) {
        if (!isCellEditable(rowIndex, 0)) {
            return;
        }
        SearchSpecification searchDefinition = (SearchSpecification) getRow(rowIndex);
        searchDefinition.setActivated(isActivated);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return "";
        }
        SearchSpecification searchDefinition = (SearchSpecification) getRow(rowIndex);
        // "#", "Name", "Ident", "Typ", "Invert", "Quellverzeichnis", "Ausgabeverzeichnis",
        switch (columnIndex) {
            case 0: {
                return searchDefinition.isActivated();
            }
            case 1: {
                return searchDefinition.getName();
            }
            case 2: {
                return searchDefinition.getCrefoNrTagName();
            }
            case 3: {
                return searchDefinition.getSearchResultsType();
            }
            case 4: {
                return searchDefinition.getUsedXmlStreamProcessor();
            }
            case 5: {
                return searchDefinition.isInvertedResults();
            }
            case 6: {
                return searchDefinition.getSourceFile().getAbsolutePath();
            }
        }
        return "";
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return;
        }
        SearchSpecification searchDefinition = (SearchSpecification) getRow(rowIndex);
        try {
            // "#", "Name", "Ident", "Typ", "Invert", "Quellverzeichnis", "Ausgabeverzeichnis",
            switch (columnIndex) {
                case 0: {
                    Boolean oldValue = searchDefinition.isActivated();
                    Boolean newValue = Boolean.parseBoolean(aValue.toString());
                    searchDefinition.setActivated(newValue); // Aktiviert?
                    if (oldValue != newValue) {
                        fireTableRowsUpdated(rowIndex, rowIndex);
                    }
                }
                break;
                case 1: {
                    searchDefinition.setName((String) aValue);
                }
                break;
                case 2: {
                    searchDefinition.setCrefoNrTagName((String) aValue);
                }
                break;
                case 3: {
                    searchDefinition.setSearchResultsType((SEARCH_RESULT_TYPE) aValue);
                }
                break;
                case 4: {
                    searchDefinition.setUsedXmlStreamProcessor((TestSupportClientKonstanten.XML_STREAM_PROCESSOR) aValue);
                }
                break;
                case 5: {
                    Boolean oldValue = searchDefinition.isInvertedResults();
                    Boolean newValue = Boolean.parseBoolean(aValue.toString());
                    searchDefinition.setInvertedResults(newValue); // Aktiviert?
                    if (oldValue != newValue) {
                        fireTableRowsUpdated(rowIndex, rowIndex);
                    }
                }
                break;
                case 6: {
                    searchDefinition.setSourceFile((File) aValue);
                }
                break;
            }
        } catch (Exception ex) {
            new RuntimeException(ex);
        }
    }
}
