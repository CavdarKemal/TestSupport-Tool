package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.ColumnsInfo;
import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design.SearcResultsDetailPanel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.model.ZipSearcResultsTableModel;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipSearcResult;

import javax.swing.table.TableColumnModel;
import java.util.List;

public class SearcResultsDetailView extends SearcResultsDetailPanel {
    private final static ColumnsInfo[] columnsInfos;

    static {
        // "Suche", "Anzahl ZIP-Dateien"
        columnsInfos = new ColumnsInfo[]{
                new ColumnsInfo(200, 260, 0), // Suche
                new ColumnsInfo(120, 120, 120), // Anzahl ZIP-Dateien
                new ColumnsInfo(120, 120, 120), // Anzahl ZIP-Einträge
        };
    }

    private final List<IZipSearcResult> zipSearcResultList;

    public SearcResultsDetailView(List<IZipSearcResult> zipSearcResultList) {
        super();
        this.zipSearcResultList = zipSearcResultList;
        initControls();
        initListeners();
    }

    private void initListeners() {
    }

    private void initControls() {
        ZipSearcResultsTableModel theModel = new ZipSearcResultsTableModel(zipSearcResultList);
        getTableSearchResults().setModel(theModel);
        TableColumnModel columnModel = getTableSearchResults().getColumnModel();
        ColumnsInfo.setColumnsInfos(columnModel, columnsInfos);
    }

}
