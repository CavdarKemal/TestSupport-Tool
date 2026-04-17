package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import de.creditreform.crefoteam.cte.tesun.util.TesunDateUtils;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.XmlMatcherWrapperFactoryCrefoExport;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ProgressListenerIF.LOG_LEVEL;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.handler.ExportedZipFilesHandler;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.handler.LogInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipSearcResult;

import javax.swing.*;
import java.io.File;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class XmlSearchSwingWorker extends SwingWorker<IZipSearcResult, Object> {
    private final String PASS_PHRASE = "Meine geheime Passphrase CTE";
    private final String PRIVATE_KEY_FILENAME = "cte_private_key_lieferant.asc";
    private final XmlStreamListenerGroup listenerGroup;
    private SearchSpecification searchSpecification;
    private ExportedZipFilesHandler exportedZipFilesHandler;
    private long startTimeMillis;

    public XmlSearchSwingWorker(XmlStreamListenerGroup listenerGroup) {
        this.listenerGroup = listenerGroup;
    }

    @Override
    protected IZipSearcResult doInBackground() throws Exception {
        LogInfo logInfo = new LogInfo(searchSpecification.getName(), LOG_LEVEL.INFO, "\n============================================================================================================================", null);
        listenerGroup.updateData(logInfo);
        logInfo = new LogInfo(searchSpecification.getName(), LOG_LEVEL.INFO, "Suche für '" + searchSpecification.getName() + "' gestartet. Startzeit:  " + TesunDateUtils.formatCalendar(Calendar.getInstance()), null);
        listenerGroup.updateData(logInfo);
        startTimeMillis = System.currentTimeMillis();
        listenerGroup.getProgressListener().updateProgress(Collections.EMPTY_LIST);
        listenerGroup.getProgressListener().updateTaskState(ProgressListenerIF.TASK_STATE.RUNNING);
        RuntimeSearchSpec runtimeSearchSpec = searchSpecification.getRuntimeSearchSpec();
        File sourceFile = runtimeSearchSpec.getSourceFile();
        if (sourceFile.isFile()) {
            final File extractedPath;
            if (sourceFile.getName().endsWith(".gpg")) {
                // CLAUDE_MODE: PGP-Entschlüsselung nicht portiert (ctreader nicht verfügbar)
                File zipFile = decryptPGPFile(sourceFile);
                if (zipFile == null) {
                    throw new IllegalArgumentException("Source \n" + sourceFile + " konnte nicht entschlüsselt werden!");
                }
                extractedPath = extractZIPFile(zipFile);
            } else if (sourceFile.getName().endsWith(".zip")) {
                extractedPath = extractZIPFile(sourceFile);
            } else {
                throw new IllegalArgumentException("Source \n" + sourceFile + " wird nicht unterstützt!");
            }
            if (extractedPath == null) {
                throw new IllegalArgumentException("Source \n" + sourceFile + " konnte nicht entpackt werden!");
            }
            runtimeSearchSpec.setSourceFile(extractedPath);
        }
        exportedZipFilesHandler = new ExportedZipFilesHandler(new XmlMatcherWrapperFactoryCrefoExport());
        final IZipSearcResult zipSearcResult = exportedZipFilesHandler.doWork(runtimeSearchSpec, listenerGroup);
        ZipSearcResult.dumpZipSearcResult("Ergebnisse der Suche " + searchSpecification.getName(), zipSearcResult);
        ZipSearcResult.displayDirectory(new File(searchSpecification.getSearchResultsPath()), "");
        return zipSearcResult;
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

    private File extractZIPFile(File decryptedFile) {
        final UnpackHandler unpackHandler = new UnpackHandler(listenerGroup, searchSpecification.getName());
        try {
            List<File> allClzZipFileList = unpackHandler.unzipForClzList(decryptedFile, null, false);
            if (!allClzZipFileList.isEmpty()) {
                return allClzZipFileList.get(0).getParentFile();
            }
            return null;
        } catch (Exception ex) {
            LogInfo logInfo = new LogInfo(searchSpecification.getName(), LOG_LEVEL.ERROR, "Exception beim Entpacken der ZIP-Datei" + decryptedFile.getAbsolutePath(), ex);
            listenerGroup.updateData(logInfo);
        }
        return null;
    }

    private File decryptPGPFile(File gpgFile) {
        // CLAUDE_MODE: PGP-Entschlüsselung nicht portiert (ctreader nicht verfügbar)
        DecryptHandler decryptHandler = new DecryptHandler(listenerGroup, PASS_PHRASE, searchSpecification.getName());
        try {
            return decryptHandler.decryptFile(gpgFile, new File(PRIVATE_KEY_FILENAME), false);
        } catch (Exception ex) {
            notifyListeners(LOG_LEVEL.ERROR, "Exception beim Entschlüsseln der GPG-Datei" + gpgFile.getAbsolutePath(), ex);
        }
        return null;
    }

    public void notifyListeners(LOG_LEVEL logLevel, String strInfo, Throwable throwable) {
        LogInfo logInfo = new LogInfo(searchSpecification.getName(), logLevel, strInfo, throwable);
        listenerGroup.updateData(logInfo);
    }
}
