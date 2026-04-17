package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.ExportedZipsSearcherUtils;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.*;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ProgressListenerIF.LOG_LEVEL;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ZipSearcResult.ZipFileInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IMatchInfoListener;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipFileInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipSearcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ExportedZipFilesHandler
{
  private final Logger logger;
  private final XmlMatcherWrapperFactory  xmlMatcherWrapperFactory;

  public ExportedZipFilesHandler( XmlMatcherWrapperFactory xmlMatcherWrapperFactory )
  {
    logger = LoggerFactory.getLogger( getClass() );
    this.xmlMatcherWrapperFactory = xmlMatcherWrapperFactory;
  }

  public IZipSearcResult doWork(RuntimeSearchSpec runtimeSearchSpec, XmlStreamListenerGroup listenerGroup) throws Exception
  {
    String strInfo = "";
    long nanoTimeStartAll = System.nanoTime();

    String searchName = runtimeSearchSpec.getName();
    strInfo = String.format("Starte Suche '%s'\n\tim Verzeichnis %s...", searchName, runtimeSearchSpec.getSourceFile() );
    listenerGroup.updateData( new LogInfo(searchName, LOG_LEVEL.INFO, strInfo, null) ); // Logging

    List<File> zipFilesList = ExportedZipsSearcherUtils.getFilesList(runtimeSearchSpec.getSourceFile(), ".zip", true );
    strInfo = String.format( "Verzeichnis enthält %d ZIP-Dateien.", zipFilesList.size() );
    listenerGroup.updateData( new LogInfo(searchName, LOG_LEVEL.INFO, strInfo, null) );
    IMatchInfoListener matchInfoListener = runtimeSearchSpec.getMatchInfoListener();

    IZipSearcResult zipSearcResult;
    try {
      if (matchInfoListener.isThreadSafe()) {
        logger.info("Starte Suche mit mehreren Threads...");
        zipSearcResult = searchMultiThreaded(searchName, runtimeSearchSpec, listenerGroup, matchInfoListener, zipFilesList);
      }
      else {
        logger.warn("MatchInfoListener ist nicht threadsafe, starte Suche mit nur einem Thread...");
        zipSearcResult = searchSingleThreaded(searchName, runtimeSearchSpec, listenerGroup, matchInfoListener, zipFilesList);
      }
    } finally {
      matchInfoListener.close();
    }

    long nanoTimeEndAll = System.nanoTime();
    strInfo = String.format( "Anzahl Treffer bei insgesamt %d ZIP-Dateien: %d\n", zipSearcResult.getZipFileInfoMap().size(), zipSearcResult.getNumZipEntries() );
    listenerGroup.updateData( new LogInfo(searchName, LOG_LEVEL.INFO, strInfo, null) );
    strInfo = String.format( "Zeitverbrauch: %s ms.\n", ( ( nanoTimeEndAll - nanoTimeStartAll ) / 1000000 ) );
    listenerGroup.updateData( new LogInfo(searchName, LOG_LEVEL.INFO, strInfo, null) );
    zipSearcResult.setSearchResultsPath(runtimeSearchSpec.getCopyOfSearchSpecification().getSearchResultsPath());
    return zipSearcResult;
  }

  protected IZipSearcResult searchSingleThreaded(String searchName, RuntimeSearchSpec runtimeSearchSpec, XmlStreamListenerGroup listenerGroup, IMatchInfoListener matchInfoListener, List<File> zipFilesList) throws Exception {
    IZipSearcResult zipSearcResult = new ZipSearcResult(searchName);
    listenerGroup.updateData(zipSearcResult); // Tree
    int numMatches = 0;

    ExportedZipFileHandler exportedZipFileHandler = new ExportedZipFileHandler();
    // Eine Instanz von XmlMatcherThreadLocals für den kompletten Such-Lauf
    XmlMatcherThreadLocals xmlMatcherThreadLocals = new XmlMatcherThreadLocals(runtimeSearchSpec, xmlMatcherWrapperFactory, listenerGroup);
    for (File zipFile : zipFilesList) {
      listenerGroup.getProgressListener().updateProgress(Collections.EMPTY_LIST);
      if (listenerGroup.getProgressListener().isCanceled()) {
        matchInfoListener.close();
        return zipSearcResult;
      }
      IZipFileInfo zipFileInfo = new ZipFileInfo(zipSearcResult, searchName, zipFile);
      File file = new File(zipFile.getAbsolutePath());
      zipSearcResult.addZipFileInfo(file.toPath(), zipFileInfo);
      exportedZipFileHandler.handleZipForListeners(xmlMatcherThreadLocals, zipFileInfo, matchInfoListener, listenerGroup);

      numMatches += zipFileInfo.getZipEntryInfoList().size();
    }
    logger.info("Suche mit nur einem Thread ergab {} Treffer", numMatches);
    return zipSearcResult;
  }

  protected IZipSearcResult searchMultiThreaded(String searchName, RuntimeSearchSpec runtimeSearchSpec, XmlStreamListenerGroup listenerGroup, IMatchInfoListener matchInfoListener, List<File> zipFilesList) throws IOException {
    ZipSearcResult zipSearcResult = new ZipSearcResult( searchName );
    listenerGroup.updateData( zipSearcResult ); // Tree

    ThreadedSearchHandler threadedSearchHandler = new ThreadedSearchHandler(runtimeSearchSpec, xmlMatcherWrapperFactory, listenerGroup, matchInfoListener);
    try {
      for( File zipFile : zipFilesList ) {
        listenerGroup.getProgressListener().updateProgress(Collections.EMPTY_LIST);
        if( listenerGroup.getProgressListener().isCanceled() ) {
          break;
        }
        ZipFileInfo zipFileInfo = new ZipFileInfo(zipSearcResult, searchName, zipFile );
        File file = new File(zipFile.getAbsolutePath());
        zipSearcResult.addZipFileInfo(file.toPath(), zipFileInfo );
        threadedSearchHandler.submitZipFile(zipFileInfo);
      }
      threadedSearchHandler.waitForCompletion();
    } finally {
      threadedSearchHandler.shutdownNow();
    }
    return zipSearcResult;
  }

}
