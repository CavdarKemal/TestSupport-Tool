package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.*;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ProgressListenerIF.LOG_LEVEL;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ZipSearcResult.ZipFileInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IMatchInfoListener;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.ISavedStreamContent;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipFileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ExportedZipFileHandler
{
  static Logger                              logger = LoggerFactory.getLogger( ExportedZipFileHandler.class );

  private final AtomicInteger                zipFileNumber;

  public ExportedZipFileHandler()
  {
    zipFileNumber = new AtomicInteger(0);
  }

  /**
   * Iteriert über die rootChildCursor aus dem übergebenen copyContentInputStream
   */
  protected <S extends InputStream & ISavedStreamContent> void handleZipEntryForListeners(XmlMatcherThreadLocals xmlMatcherThreadLocals,
                                                                                          IZipFileInfo zipFileInfo, S copyContentInputStream, String entryName,
                                                                                          IMatchInfoListener matchListener,
                                                                                          XmlStreamListenerGroup listenerGroup) throws Exception
  {
    listenerGroup.getProgressListener().updateProgress(Collections.EMPTY_LIST);
    String strInfo = "\t\t\t*** Untersuche ZIP-Entry '"+ entryName +"'...";
    listenerGroup.updateData( new LogInfo(zipFileInfo.getSearchName(), LOG_LEVEL.DEBUG, strInfo, null) );
//    List<String> matchingCriteriaList = listenerGroup.getMatchingCriterionListener().getMatchingCriteriaList();
//    matchingCriteriaList.clear(); // TODO: klären mit der vorangegangenen Zeile
    // Die eigentliche Suche übernimmt das Callable...
    SingleEntrySearchCallable<S> callable = new SingleEntrySearchCallable(xmlMatcherThreadLocals,
                                                                          zipFileInfo, entryName, copyContentInputStream,
                                                                          matchListener);
    SingleEntrySearchResult result = callable.call();
    listenerGroup.notifyMatchingCriteria(result.getMatchingCriteriaList());

    // Listener über das Ergebnis informieren
    if (matchListener != null) {
      result.getZipEntryInfo().addMatches(result.getMatchingCriteriaList());
      if( result.isMatch() ) {
        strInfo = "\t\t\t\tTreffer in ZIP-Entry '" + entryName +"'";
        listenerGroup.updateData( new LogInfo(zipFileInfo.getSearchName(), LOG_LEVEL.DEBUG, strInfo, null) );
        zipFileInfo.addZipEntryInfo( result.getZipEntryInfo() );
        listenerGroup.updateData( result.getZipEntryInfo() );
      }
    } // matchListener != null
  }

  /**
   * Iteriert über die Zipentry-Einträge aus dem übergebenen zipInputStream
   */
  public void handleZipForListeners(XmlMatcherThreadLocals xmlMatcherThreadLocals,
                                    IZipFileInfo zipFileInfo,
                                    IMatchInfoListener matchListener, XmlStreamListenerGroup listenerGroup) throws Exception
  {
    String strInfo = String.format( "\t\t*** Untersuche ZIP-Datei '%s'...", zipFileInfo.getZipFilePath() );
    listenerGroup.updateData( new LogInfo(zipFileInfo.getSearchName(), LOG_LEVEL.DEBUG, strInfo, null) );
    listenerGroup.updateData( zipFileInfo );
    ZipInputStream zipInputStream = getZipInputStreamForZipFile( ((ZipFileInfo) zipFileInfo).getZipFile() );
    try
    {
      int numEntries = 0;
      ZipEntry zipEntry = zipInputStream.getNextEntry();
      while( zipEntry != null )
      {
        if(listenerGroup.getProgressListener().isCanceled())
        {
          return;
        }
        numEntries++;
        listenerGroup.getProgressListener().updateProgress(Collections.EMPTY_LIST);
        BufferedContentInputStream inputStream = BufferedContentInputStream.create(zipInputStream);
        handleZipEntryForListeners(xmlMatcherThreadLocals, zipFileInfo, inputStream, zipEntry.getName(), matchListener, listenerGroup);
        zipEntry = zipInputStream.getNextEntry();
      }
      int myFileNumber = zipFileNumber.incrementAndGet();
      strInfo = String.format( "\t\t\t\t-> Zip-Datei %s (Nr. %d) mit %d Entries, davon %d mit Treffern", zipFileInfo.getZipFileName(), myFileNumber, numEntries, zipFileInfo.getZipEntryInfoList().size() );
      listenerGroup.updateData( new LogInfo(zipFileInfo.getSearchName(), LOG_LEVEL.INFO, strInfo, null) );
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
    finally
    {
      zipInputStream.close();
    }
  }

  private ZipInputStream getZipInputStreamForZipFile( File zipFile ) throws FileNotFoundException
  {
    FileInputStream fileInputStream = new FileInputStream( zipFile );
    BufferedInputStream bufferedInputStream = new BufferedInputStream( fileInputStream );
    ZipInputStream zipInputStream = new ZipInputStream( bufferedInputStream );
    return zipInputStream;
  }

}
