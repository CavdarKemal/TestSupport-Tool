package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.ColumnsInfo;
import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design.SearchResultZipFileDetailPanel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.model.ZipFileInfoTableModel;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipFileInfo;

import javax.swing.table.TableColumnModel;

public class SearchResultZipFileDetailView extends SearchResultZipFileDetailPanel {
    private final static ColumnsInfo[] columnsInfos;

    static {
        // "Crefonummer", "ZIP-Eintrag", "Dateiname"
        columnsInfos = new ColumnsInfo[]{
                new ColumnsInfo(90, 90, 90), // Crefonummer
                new ColumnsInfo(160, 180, 400), // ZIP-Eintrag
                new ColumnsInfo(200, 260, 0), // Dateiname
        };
    }

    private final IZipFileInfo zipFileInfo;

    public SearchResultZipFileDetailView(IZipFileInfo zipFileInfo) {
        super();
        this.zipFileInfo = zipFileInfo;
        initControls();
        initListeners();
    }

    private void initListeners() {
    }

    private void initControls() {
        getTextFieldZipFileName().setText(zipFileInfo.getZipFileName());
        ZipFileInfoTableModel theModel = new ZipFileInfoTableModel(zipFileInfo.getZipEntryInfoList());
        getTableZipEntries().setModel(theModel);
        getTextFieldZipFileName().setEditable(false);
        TableColumnModel columnModel = getTableZipEntries().getColumnModel();
        ColumnsInfo.setColumnsInfos(columnModel, columnsInfos);
    }

}
