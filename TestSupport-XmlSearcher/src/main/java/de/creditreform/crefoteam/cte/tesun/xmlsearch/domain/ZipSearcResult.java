package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipEntryInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipFileInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipSearcResult;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class ZipSearcResult implements IZipSearcResult {
    private Map<Path, IZipFileInfo> zipFileInfoMap = new TreeMap<>();
    private String searchResultsPath;

    private final String searchName;

    public ZipSearcResult(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchName() {
        return searchName;
    }

    @Override
    public void setSearchResultsPath(String searchResultsPath) {
        this.searchResultsPath = searchResultsPath;
    }

    @Override
    public String getSearchResultsPath() {
        return searchResultsPath;
    }

    public void addZipFileInfo(Path key, IZipFileInfo zipFileInfo) {
        zipFileInfoMap.put(key, zipFileInfo);
    }

    @Override
    public Map<Path, IZipFileInfo> getZipFileInfoMap() {
        return zipFileInfoMap;
    }

    @Override
    public int getNumZipFiles() {
        return zipFileInfoMap.size();
    }

    @Override
    public int getNumZipEntries() {
        int numZipEntries = 0;
        for (Map.Entry<Path, IZipFileInfo> entry : zipFileInfoMap.entrySet()) {
            numZipEntries += entry.getValue().getZipEntryInfoList().size();
        }
        return numZipEntries;
    }

    @Override
    public String toString() {
        return "ZipSearcResult\n{" +
                "\n\tzipFileInfoMap=" + zipFileInfoMap +
                "\n\tsearchName='" + searchName + '\'' +
                "\n}";
    }

    /*************************************************************************************************/
    public static class ZipFileInfo
            implements IZipFileInfo {

        private final IZipSearcResult parent;
        private final String searchName;
        private final File zipFile;
        private final List<IZipEntryInfo> zipEntryInfoList = new ArrayList<>();

        public ZipFileInfo(IZipSearcResult parent, String searchName, File zipFile) {
            this.parent = parent;
            this.searchName = searchName;
            this.zipFile = zipFile;
        }

        @Override
        public IZipSearcResult getParent() {
            return parent;
        }

        public File getZipFile() {
            return zipFile;
        }

        @Override
        public String getZipFileName() {
            return getZipFile().getName();
        }

        @Override
        public String getZipFilePath() {
            return getZipFile().getPath();
        }

        @Override
        public String getSearchName() {
            return searchName;
        }

        @Override
        public void addZipEntryInfo(IZipEntryInfo zipEntryInfo) {
            zipEntryInfoList.add(zipEntryInfo);
        }

        @Override
        public List<? extends IZipEntryInfo> getZipEntryInfoList() {
            return zipEntryInfoList;
        }

        @Override
        public String toString() {
            return "ZipFileInfo\n{" +
                    //"\n\tparent=" + ((parent != null) ? parent.getSearchName():"null") +
                    "\n\tsearchName='" + searchName + '\'' +
                    "\n\tzipFile=" + getZipFileName() +
                    "\n\tzipEntryInfoList=" + zipEntryInfoList +
                    "\n}";
        }

    }

    /*************************************************************************************************/
    public static class ZipEntryInfo
            implements IZipEntryInfo {

        private final IZipFileInfo parent;
        private final String zipFileName;
        private final String zipEntryName;
        private String resultFileName;
        private String crefonummer;
        private List<String> matchesList = new ArrayList<>();

        public ZipEntryInfo(IZipFileInfo parent, String zipFileName, String zipEntryName) {
            this.parent = parent;
            this.zipFileName = zipFileName;
            this.zipEntryName = zipEntryName;
        }

        @Override
        public IZipFileInfo getParent() {
            return parent;
        }

        @Override
        public void setResultFileName(String resultFileName)
                throws IllegalStateException {
            // TODO: Die Methode 'setResultFileName' passt nicht zusammen mit dem Konzept der Listener und sollte ersetzt werden
            if (this.resultFileName != null && !this.resultFileName.equals(resultFileName)) {
                throw new IllegalStateException("Der Name der Ergebnis-Datei wurde bereits gesetzt auf: " + resultFileName);
            }
            this.resultFileName = resultFileName;
        }

        public void setCrefonummer(String crefonummer) {
            this.crefonummer = crefonummer;
        }

        public void addMatches(String strMatch) {
            matchesList.add(strMatch);
        }

        @Override
        public void addMatches(List<String> matchesList) {
            this.matchesList.addAll(matchesList);
        }

        @Override
        public List<String> getMatchesList() {
            return matchesList;
        }

        @Override
        public String getZipEntryName() {
            return zipEntryName;
        }

        @Override
        public String getResultFileName() {
            return resultFileName;
        }

        @Override
        public String getCrefonummer() {
            return crefonummer;
        }

        @Override
        public String getZipFileName() {
            return zipFileName;
        }

        @Override
        public String toString() {
            return "ZipEntryInfo\n{" +
                    "\n\tzipFileName\t= '" + zipFileName + '\'' +
                    "\n\tzipEntryNam\t= '" + zipEntryName + '\'' +
                    "\n\tresultFileName\t= '" + resultFileName + '\'' +
                    "\n\tcrefonummer\t= '" + crefonummer + '\'' +
                    "\n\tmatchesList\t= " + matchesList +
                    "\n}";
        }
    }

    public static void displayDirectory(File dir, String indent) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println(indent + file.getName());
                displayDirectory(file, indent + "  ");
            } else {
                System.out.println(indent + "   " + file.getName());
            }
        }
    }

    public static void dumpZipSearcResult(String searchStr, IZipSearcResult zipSearcResult) {
        System.out.println("\n" + searchStr + " aus ZipSearcResult \n---------------------------------");
        Map<Path, IZipFileInfo> zipFileInfoMap = zipSearcResult.getZipFileInfoMap();
        Iterator<Path> iterator = zipFileInfoMap.keySet().iterator();
        while (iterator.hasNext()) {
            Path key = iterator.next();
            IZipFileInfo iZipFileInfo = zipFileInfoMap.get(key);
            List<? extends IZipEntryInfo> zipEntryInfoList = iZipFileInfo.getZipEntryInfoList();
            for (IZipEntryInfo iZipEntryInfo : zipEntryInfoList) {
                String resultFileName = iZipEntryInfo.getResultFileName();
                if(resultFileName != null) {
                    System.out.println(resultFileName);
                    File resultFile = new File(resultFileName);
                }
            }
        }
    }

}
