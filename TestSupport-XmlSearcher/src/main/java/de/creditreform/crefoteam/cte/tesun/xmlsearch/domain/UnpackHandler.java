package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import de.creditreform.crefoteam.cte.tesun.util.TesunDateUtils;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.handler.LogInfo;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnpackHandler {
    private XmlStreamListenerGroup listenerGroup;
    private String searchName;

    public UnpackHandler(XmlStreamListenerGroup listenerGroup, String searchName) {
        this.listenerGroup = listenerGroup;
        this.searchName = searchName;
    }

    public List<File> unzipForClzList(File zipFile, List<Long> clzList, boolean force) throws Exception {
        final String strInfo = "\tEntpacke die entschlüsselten ZIP-Datei '" + zipFile.getAbsolutePath() + "'";
        long millisStart = System.currentTimeMillis();
        ZipExtractor zipExtractor = new ZipExtractor();
        ClzZipFileFilter clzZipFileFilter = new ClzZipFileFilter(clzList);
        List<File> zipFilesList;
        File dirFile = new File(zipFile.getAbsolutePath().replace(".zip", ""));
        if (force || !dirFile.exists()) {
            listenerGroup.updateData(new LogInfo(searchName, ProgressListenerIF.LOG_LEVEL.INFO, strInfo, null));
            zipFilesList = zipExtractor.extractFilesFromZip(zipFile, clzZipFileFilter);
        } else {
            final Collection<File> fileCollection = FileUtils.listFiles(dirFile, new String[]{"zip"}, true);
            zipFilesList = new ArrayList<>(fileCollection);
        }
        long millisEnd = System.currentTimeMillis();
        listenerGroup.updateData(new LogInfo(searchName, ProgressListenerIF.LOG_LEVEL.INFO, strInfo, null));
        listenerGroup.updateData(new LogInfo(searchName, ProgressListenerIF.LOG_LEVEL.INFO, TesunDateUtils.formatElapsedTime("\tEntpacken der entschlüsselten ZIP-Datei '" + zipFile.getAbsolutePath() + "'", millisStart, millisEnd), null));
        return zipFilesList;
    }

    public List<File> unzipForCrefoList(File zipFile, List<Long> crefoList) throws Exception {
        long millisStart = System.currentTimeMillis();

        ZipExtractor zipExtractor = new ZipExtractor();
        CrefoXmlFileFilter crefoXmlFileFilter = new CrefoXmlFileFilter(crefoList);
//      notifyListeners(Level.INFO, "\tEntpacke die ZIP-Datei '" + zipFile.getAbsolutePath() + "'...");
        final List<File> xmlFilesList = zipExtractor.extractFilesFromZip(zipFile, crefoXmlFileFilter);

        long millisEnd = System.currentTimeMillis();
//      notifyListeners(Level.INFO, Utils.formatElapsedTime("\tEntpacken der ZIP-Datei '" + zipFile.getAbsolutePath() + "'", millisStart, millisEnd));
        return xmlFilesList;
    }

    /********************************************************************************************/
    class ClzZipFileFilter implements FileFilter {
        final Pattern[] clzZipFileNamePatterns = {
                Pattern.compile("abCrefo_\\d{3}.*zip"),    // "abCrefo_4120000000.zip"                                 bei den meisten
                Pattern.compile("CLZ_\\d{3}_ab_.*zip"),    // "411\CLZ_411_ab_0500000.zip"                             bei VSD/VSH/VSO
                Pattern.compile("_abCrefo\\d{3}.*zip"),    // "20201202_0236_abCrefo412.0000000_createAndUpdate.zip"   bie DRD
                Pattern.compile("_abCrefo\\d{3}.*zip"),    // "BedirectExport_abCrefo411.2000000.zip"                  bie BDR
        };
        final List<Long> clzList;

        ClzZipFileFilter(List<Long> clzList) {
            this.clzList = clzList;
        }

        @Override
        public boolean accept(File theFile) {
            final boolean extensionOK = checkFileName(theFile);
            if (clzList == null || clzList.isEmpty()) {
                return extensionOK;
            }
            if (extensionOK) {
                for (Long clz : clzList) {
                    if (theFile.getName().contains("" + clz.intValue())) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean checkFileName(File theFile) {
            for (Pattern clzZipFileNamePattern : clzZipFileNamePatterns) {
                Matcher m = clzZipFileNamePattern.matcher(theFile.getName());
                if (m.find()) {
                    return true;
                }
            }
            return false;
        }
    }

    class CrefoXmlFileFilter implements FileFilter {
        private final List<Long> crefoList;

        CrefoXmlFileFilter(List<Long> crefoList) {
            this.crefoList = crefoList;
        }

        @Override
        public boolean accept(File theFile) {
            final boolean extensionOK = theFile.getName().endsWith(".xml");
            if (crefoList == null || crefoList.isEmpty()) {
                return extensionOK;
            }
            if (extensionOK) {
                for (Long crefo : crefoList) {
                    if (theFile.getName().contains("" + crefo.longValue())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

}
