package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.ColumnsInfo;
import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design.SearcResultDetailPanel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.model.ZipSearcResultTableModel;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipFileInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipSearcResult;

import javax.swing.table.TableColumnModel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SearcResultDetailView extends SearcResultDetailPanel {
    // "ZIP-Datei", "Anzahl ZIP-Einträge"
    private final static ColumnsInfo[] columnsInfos;

    static {
        columnsInfos = new ColumnsInfo[]{
                new ColumnsInfo(200, 260, 0), // ZIP-Datei
                new ColumnsInfo(120, 120, 120), // Anzahl ZIP-Einträge
        };
    }

    private final IZipSearcResult zipSearcResult;

    public SearcResultDetailView(IZipSearcResult zipSearcResult) {
        super();
        this.zipSearcResult = zipSearcResult;
        initControls();
        initListeners();
    }

    private void initListeners() {
    }

    private void initControls() {
        getTextFieldSearchName().setText(zipSearcResult.getSearchName());
        getTextFieldNumZipFiles().setText(zipSearcResult.getNumZipFiles() + "");
        getTextFieldNumZipEntries().setText(zipSearcResult.getNumZipEntries() + "");
        Map<Path, IZipFileInfo> zipFileInfoMap = zipSearcResult.getZipFileInfoMap();
        List<IZipFileInfo> zipFileInfoList = new ArrayList<>();
        Iterator<Entry<Path, IZipFileInfo>> iterator = zipFileInfoMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<Path, IZipFileInfo> nextEntry = iterator.next();
            zipFileInfoList.add(nextEntry.getValue());
        }
        ZipSearcResultTableModel theModel = new ZipSearcResultTableModel(zipFileInfoList);
        getTableZipFileInfos().setModel(theModel);
        getTextFieldSearchName().setEditable(false);
        TableColumnModel columnModel = getTableZipFileInfos().getColumnModel();
        ColumnsInfo.setColumnsInfos(columnModel, columnsInfos);
    }

}
