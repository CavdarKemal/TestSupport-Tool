package de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen;

import java.nio.file.Path;
import java.util.Map;

public interface IZipSearcResult {

    String getSearchName();

    void setSearchResultsPath(String searchResultsPath);

    String getSearchResultsPath();

    void addZipFileInfo(Path key, IZipFileInfo zipFileInfo);

    Map<Path, IZipFileInfo> getZipFileInfoMap();

    int getNumZipFiles();

    int getNumZipEntries();

}
