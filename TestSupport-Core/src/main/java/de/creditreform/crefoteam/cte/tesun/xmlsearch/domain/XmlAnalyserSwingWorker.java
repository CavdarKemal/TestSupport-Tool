package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import de.creditreform.crefoteam.cte.tesun.util.TesunDateUtils;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.XmlMatcherWrapperFactoryCrefoExport;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ProgressListenerIF.LOG_LEVEL;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.handler.ExportedZipFilesHandler;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.handler.LogInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipEntryInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipFileInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipSearcResult;
import org.apache.commons.collections.list.TreeList;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class XmlAnalyserSwingWorker extends SwingWorker<DuplicateExportInfo, Object> {
    private final XmlStreamListenerGroup listenerGroup;
    private final ExportResultsAnalyseStrategy analyseStrategy;
    private SearchSpecification searchSpecification;
    private long startTimeMillis;

    public XmlAnalyserSwingWorker(ExportResultsAnalyseStrategy analyseStrategy, XmlStreamListenerGroup listenerGroup) {
        this.analyseStrategy = analyseStrategy;
        this.listenerGroup = listenerGroup;
    }

    @Override
    protected DuplicateExportInfo doInBackground() throws Exception {
        FileUtils.deleteQuietly(new File(searchSpecification.getSearchResultsPath()));
        LogInfo logInfo = new LogInfo(searchSpecification.getName(), LOG_LEVEL.INFO, "\n============================================================================================================================", null);
        listenerGroup.updateData(logInfo);
        logInfo = new LogInfo(searchSpecification.getName(), LOG_LEVEL.INFO, "Suche für '" + searchSpecification.getName() + "' gestartet. Startzeit:  " + TesunDateUtils.formatCalendar(Calendar.getInstance()), null);
        listenerGroup.updateData(logInfo);
        startTimeMillis = System.currentTimeMillis();
        listenerGroup.getProgressListener().updateProgress(Collections.EMPTY_LIST);
        listenerGroup.getProgressListener().updateTaskState(ProgressListenerIF.TASK_STATE.RUNNING);

        // 1. Führe eine Such durch, um die Treffer für "Analyse" zu ermitteln... --> das Ergebnis wird in einem Verzeichnis "...-Results/Analyse" abgelegt
        RuntimeSearchSpec runtimeSearchSpec = searchSpecification.getRuntimeSearchSpec();
        File sourceFile = runtimeSearchSpec.getSourceFile();
        runtimeSearchSpec.setSourceFile(sourceFile);
        ExportedZipFilesHandler exportedZipFilesHandler = new ExportedZipFilesHandler(new XmlMatcherWrapperFactoryCrefoExport());
        IZipSearcResult zipSearcResult = exportedZipFilesHandler.doWork(runtimeSearchSpec, listenerGroup);
        ZipSearcResult.dumpZipSearcResult("Ergebnisse der Suche " + searchSpecification.getName(), zipSearcResult);
        ZipSearcResult.displayDirectory(new File(searchSpecification.getSearchResultsPath()), "");

        // 2. ermittle Duplikate der Treffer aus der obigen Suche... Quelle ist  "...-Results/Analyse", das Ergebnis wird als DuplicateExportInfo zurückgeliefert
        final DuplicateExportInfo duplicateExportInfo = analyseStrategy.doAnalyse(searchSpecification);

        // 3. Führe eine Such mit den Crefos aus der Duplikat-Liste durch... --> das Ergebnis wird in einem Verzeichnis "...-Results/CrefoListe" abgelegt
        SearchSpecification clonedSearchSpecification = new SearchSpecification(searchSpecification);
        clonedSearchSpecification.setName("CrefoListe");
        SearchCriteria searchCriteria = new SearchCriteria("file::", createCrefoListFile(searchSpecification.getSourceFile(), duplicateExportInfo.getDuplicateCrefoNummernList()).getAbsolutePath());
        clonedSearchSpecification.getSearchCriteriasList().add(searchCriteria);
        runtimeSearchSpec = clonedSearchSpecification.getRuntimeSearchSpec();
        sourceFile = runtimeSearchSpec.getSourceFile();
        runtimeSearchSpec.setSourceFile(sourceFile);
        exportedZipFilesHandler = new ExportedZipFilesHandler(new XmlMatcherWrapperFactoryCrefoExport());
        zipSearcResult = exportedZipFilesHandler.doWork(runtimeSearchSpec, listenerGroup);
        ZipSearcResult.dumpZipSearcResult("Ergebnisse der Suche " + clonedSearchSpecification.getName(), zipSearcResult);
        ZipSearcResult.displayDirectory(new File(clonedSearchSpecification.getSearchResultsPath()), "");

        // 4. Analysiere die gefundenen XMLS in  Verbindung der Dupolicate...
        checkDuplicates(duplicateExportInfo, zipSearcResult);

        return duplicateExportInfo;
    }

    private void checkDuplicates(final DuplicateExportInfo duplicateExportInfo, IZipSearcResult zipSearcResult) throws Exception {
        // System.out.println(duplicateExportInfo);
        File sourceFile = new File(searchSpecification.getSearchResultsPath(), "Analyse.csv");
        FileUtils.writeStringToFile(sourceFile, duplicateExportInfo.toString());

        Map<Long, List<File>> potentialDuplicatesMap = new TreeMap<>();
        Map<Path, IZipFileInfo> zipFileInfoMap = zipSearcResult.getZipFileInfoMap();
        Iterator<Path> iterator = zipFileInfoMap.keySet().iterator();
        while (iterator.hasNext()) {
            Path key = iterator.next();
            IZipFileInfo iZipFileInfo = zipFileInfoMap.get(key);
            List<? extends IZipEntryInfo> zipEntryInfoList = iZipFileInfo.getZipEntryInfoList();
            for (IZipEntryInfo iZipEntryInfo : zipEntryInfoList) {
                Long crefoNummer = Long.parseLong(iZipEntryInfo.getCrefonummer());
                List<File> crefoNummerInDuplicateList = findTheCorrespondingList(duplicateExportInfo, crefoNummer);
                if (crefoNummerInDuplicateList != null) {
                    List<File> fileList = duplicateExportInfo.getCrefoToFileMap().get(crefoNummer);
                    File potentialDuplicateFile = new File(iZipEntryInfo.getZipFileName(), iZipEntryInfo.getZipEntryName());
                    fileList.add(potentialDuplicateFile);
                    List<File> potentialDuplicatesList = potentialDuplicatesMap.get(crefoNummer);
                    if (potentialDuplicatesList == null) {
                        potentialDuplicatesList = new TreeList();
                    }
                    potentialDuplicatesList.add(potentialDuplicateFile);
                    potentialDuplicatesMap.put(crefoNummer, potentialDuplicatesList);
                }
            }
        }
        sourceFile = new File(searchSpecification.getSearchResultsPath(), "CheckedAnalyse.csv");
        FileUtils.writeStringToFile(sourceFile, duplicateExportInfo.toString());

        Map<Long, List<File>> duplicatesMap = filterDuplicates(potentialDuplicatesMap);
        sourceFile = new File(searchSpecification.getSearchResultsPath(), "DuplicateFiles.csv");
        FileUtils.writeStringToFile(sourceFile, duplicatesMap.toString());
    }

    private Map<Long, List<File>> filterDuplicates(Map<Long, List<File>> potentialDuplicatesMap) throws IOException {
        Map<Long, List<File>> duplicatesMap = new TreeMap<>();
        Iterator<Long> iterator = potentialDuplicatesMap.keySet().iterator();
        while (iterator.hasNext()) {
            Long crefoNummer = iterator.next();
            List<File> duplicateFileList = new TreeList();
            duplicatesMap.put(crefoNummer, duplicateFileList);
            List<File> fileList = potentialDuplicatesMap.get(crefoNummer);
            for (File file : fileList) {
                if (checkIfLoeschSatzVorhanden(fileList, file)) {
                    duplicateFileList.add(file);
                }
            }
        }
        return duplicatesMap;
    }

    private boolean checkIfLoeschSatzVorhanden(List<File> fileList, File file) {
        if (!file.getName().contains("loesch")) {
            return false;
        }
        int count = 0;
        for (File curFile : fileList) {
            String name = curFile.getName();
            boolean currLoeschSatz = name.startsWith("loesch");
            if (currLoeschSatz) {
                count++;
            } else {
                count = 0;
            }
            if (count > 1) {
                return true;
            }
        }
        return false;
    }

    private List<File> findTheCorrespondingList(DuplicateExportInfo duplicLongListMap, Long crefoNummer) {
        List<Long> duplicateCrefoNummernList = duplicLongListMap.getDuplicateCrefoNummernList();
        for (Long theCrefo : duplicateCrefoNummernList) {
            if (theCrefo.longValue() == crefoNummer.longValue()) {
                return duplicLongListMap.getCrefoToFileMap().get(theCrefo);
            }
        }
        return null;
    }


    private File createCrefoListFile(File sourceFile, List<Long> duplicateCrefoNummernList) throws IOException {
        File crefoListFile = new File(sourceFile, "CrefoListe.txt");
        StringBuilder stringBuilder = new StringBuilder();
        duplicateCrefoNummernList.stream().forEach(crefoNr -> {
            stringBuilder.append(crefoNr + "\n");
        });
        FileUtils.writeStringToFile(crefoListFile, stringBuilder.toString());
        return crefoListFile;
    }


    @Override
    protected void process(List<Object> chunks) {
        listenerGroup.updateProgress(chunks);
    }

    @Override
    protected void done() {
        long stopTimeMillis = System.currentTimeMillis() - startTimeMillis;
        if (isCancelled()) {
            LogInfo logInfo = new LogInfo(searchSpecification.getName(), LOG_LEVEL.INFO, "Suche für '" + searchSpecification.getName() + "' abgebrochen! Dauer: " + stopTimeMillis + " ms\n", null);
            listenerGroup.updateData(logInfo);

            logInfo = new LogInfo(searchSpecification.getName(), LOG_LEVEL.INFO, "----------------------------------------------------------------------------------------------------------------------------", null);
            listenerGroup.updateData(logInfo);

            listenerGroup.getProgressListener().updateTaskState(ProgressListenerIF.TASK_STATE.CANCELLED);
        } else {
            try {
                get();

                LogInfo logInfo = new LogInfo(searchSpecification.getName(), LOG_LEVEL.INFO, "Suche für '" + searchSpecification.getName() + "' beendet.Dauer: " + stopTimeMillis + " ms\n", null);
                listenerGroup.updateData(logInfo);

                logInfo = new LogInfo(searchSpecification.getName(), LOG_LEVEL.INFO, "----------------------------------------------------------------------------------------------------------------------------", null);
                listenerGroup.updateData(logInfo);

                listenerGroup.getProgressListener().updateTaskState(ProgressListenerIF.TASK_STATE.DONE);
            } catch (Exception ex) {
                LogInfo logInfo = new LogInfo(searchSpecification.getName(), LOG_LEVEL.ERROR, "Exception!", ex);
                listenerGroup.updateData(logInfo);
                listenerGroup.getProgressListener().updateTaskState(ProgressListenerIF.TASK_STATE.ABORTED);
            }
        }
    }

    public SearchSpecification getSearchSpecification() {
        return searchSpecification;
    }

    public void setSearchSpecification(SearchSpecification searchSpecification) {
        this.searchSpecification = searchSpecification;
    }

    public void notifyListeners(LOG_LEVEL logLevel, String strInfo, Throwable throwable) {
        LogInfo logInfo = new LogInfo(searchSpecification.getName(), logLevel, strInfo, throwable);
        listenerGroup.updateData(logInfo);
    }

}
