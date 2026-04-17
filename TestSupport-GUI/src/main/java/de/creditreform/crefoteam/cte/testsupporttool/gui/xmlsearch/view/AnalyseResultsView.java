package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.ColumnsInfo;
import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design.SearchResultZipFileDetailPanel;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ExportResultsAnalyseStrategy;

public class AnalyseResultsView extends SearchResultZipFileDetailPanel {
    private final static ColumnsInfo[] columnsInfos;

    static {
        // "Crefonummer", "ZIP-Eintrag", "Dateiname"
        columnsInfos = new ColumnsInfo[]{
                new ColumnsInfo(90, 90, 90), // Crefonummer
                new ColumnsInfo(160, 180, 400), // ZIP-Eintrag
                new ColumnsInfo(200, 260, 0), // Dateiname
        };
    }

    private final ExportResultsAnalyseStrategy exportResultsAnalyseStrategy;

    public AnalyseResultsView(ExportResultsAnalyseStrategy exportResultsAnalyseStrategy) {
        super();
        this.exportResultsAnalyseStrategy = exportResultsAnalyseStrategy;
        initControls();
        initListeners();
    }

    private void initListeners() {
    }

    private void initControls() {
    }

}
