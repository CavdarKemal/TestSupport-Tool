package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.BufferedContentInputStream;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ProgressListenerIF;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.XmlStreamListenerGroup;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ZipSearcResult;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IMatchInfoListener;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static de.creditreform.crefoteam.cte.tesun.xmlsearch.handler.ThreadedSearchPerZipResult.StateAtExit.*;

/**
 * Runnable für das Erzeugen neuer Such-Tasks
 */
public class ThreadedSearchProducer
extends ThreadedSearchAbstract {

   /**
    * Interface mit den hier verwendeten Zugriffen auf einen {@link ZipInputStream}
    */
   protected interface ZipStreamIF
   extends Closeable {
      ZipEntry getNextEntry() throws IOException;

      InputStream getInputStream();

      void close() throws IOException;

   }

   /**
    * Wrapper für einen {@link ZipInputStream}, erleichtert den Test
    */
   private static class ZipStreamWrapper
   implements ZipStreamIF {
      private final ZipInputStream zipInputStream;

      public ZipStreamWrapper(ZipInputStream zipInputStream) {
         this.zipInputStream = zipInputStream;
      }

      @Override
      public ZipEntry getNextEntry() throws IOException {
         return zipInputStream.getNextEntry();
      }

      @Override
      public InputStream getInputStream() {
         return zipInputStream;
      }

      @Override
      public void close() throws IOException {
         zipInputStream.close();
      }

   }

   private final BlockingQueue<Runnable> searcherQueue;
   private final ExecutorService searchExecutorService;
   private final XmlMatcherThreadLocals xmlMatcherThreadLocals;
   private final ZipSearcResult.ZipFileInfo zipFileInfo;
   private final IMatchInfoListener matchListener;
   private final XmlStreamListenerGroup listenerGroup;
   private final List<Future<SingleEntrySearchResult>> openTasks;
   private final GroupByResultsAggregator groupByResultsAggregator;
   private int numEntriesProcessed;
   private int numMatches;

   public ThreadedSearchProducer(BlockingQueue<Runnable> searcherQueue, ExecutorService searchExecutorService,
                                 ZipSearcResult.ZipFileInfo zipFileInfo, XmlMatcherThreadLocals xmlMatcherThreadLocals,
                                 IMatchInfoListener matchListener, XmlStreamListenerGroup listenerGroup) {
      super();
      this.searcherQueue = searcherQueue;
      this.searchExecutorService = searchExecutorService;
      this.xmlMatcherThreadLocals = xmlMatcherThreadLocals;
      this.zipFileInfo = zipFileInfo;
      this.matchListener = matchListener;
      this.listenerGroup = listenerGroup;
      this.openTasks = new ArrayList<>(2*searcherQueue.remainingCapacity());
      this.groupByResultsAggregator = new GroupByResultsAggregator();
   }

   @Override
   protected ThreadedSearchPerZipResult doInterruptible() throws InterruptedException {

      String strInfo = String.format("\t\t*** Untersuche ZIP-Datei '%s'...", zipFileInfo.getZipFile().getPath());
      listenerGroup.updateData(new LogInfo(zipFileInfo.getSearchName(), ProgressListenerIF.LOG_LEVEL.DEBUG, strInfo, null));
      listenerGroup.updateData(zipFileInfo);
      int sleepingRounds = 0; // Anzahl der Runden, in denen der Producer pausieren musste
      ThreadedSearchPerZipResult.StateAtExit stateAtExit;
      ZipStreamIF zipStreamWrapper=null;
      final String zipFileName = zipFileInfo.getZipFile().getName();
      try {
         zipStreamWrapper = getZipStreamWrapperForZipFile(zipFileInfo.getZipFile());
         ZipEntry nextZipEntry = zipStreamWrapper.getNextEntry();
         while (true) {
            if (listenerGroup.getProgressListener().isCanceled()) {
               stateAtExit = ABORTED;
               break;
            }
            // Mit 'actionsThisRound' zählen wir mit, wie viele Aktionen in einem Schleifendurchlauf erfolgen konnten.
            int actionsThisRound=0;
            // Die Verwendung von remainingCapacity ist hier in Ordnung, weil nur dieser Thread in die Queue schreibt.
            if (nextZipEntry!=null && searcherQueue.remainingCapacity()>1) {
               numEntriesProcessed++;
               listenerGroup.getProgressListener().updateProgress(Collections.EMPTY_LIST);
               BufferedContentInputStream inputStream = BufferedContentInputStream.create(zipStreamWrapper.getInputStream());
               listenerGroup.getProgressListener().updateProgress(Collections.EMPTY_LIST);
               String entryName = nextZipEntry.getName();
               strInfo = "\t\t\t*** Untersuche ZIP-Entry '"+ entryName +"'...";
               listenerGroup.updateData( new LogInfo(zipFileInfo.getSearchName(), ProgressListenerIF.LOG_LEVEL.DEBUG, strInfo, null) );
               // Die eigentliche Suche übernimmt das Callable...
               Future<SingleEntrySearchResult> future = searchExecutorService.submit(createSearchTask(entryName, inputStream));
               this.openTasks.add(future);
               nextZipEntry = zipStreamWrapper.getNextEntry();
               actionsThisRound++;
            }
            actionsThisRound+=scanOpenTasks();
            if (nextZipEntry==null && openTasks.isEmpty()) {
               stateAtExit = SUCCESS;
               break;
            }
            else if (actionsThisRound==0) {
               // Keine Arbeit in dieser Runde, wir schlafen besser ein wenig...
               sleepingRounds++;
               Thread.sleep(200);
            }
         } // Verarbeitungs-Schleife

      } catch (FileNotFoundException e) {
         getLogger().error("Zip-Datei nicht gefunden: {}", zipFileName, e);
         stateAtExit = ERROR;
      } catch (IOException e) {
         getLogger().error("Fehler beim Lesen der Zip-Datei {}", zipFileName, e);
         stateAtExit = ERROR;
      } catch (ExecutionException e) {
         getLogger().error("SearchCallable hat eine Exception geworfen, durchsuchte Datei: {}", zipFileName, e);
         stateAtExit = ERROR;
      } finally {
         IOUtils.closeQuietly(zipStreamWrapper);
      }
      getLogger().info("Verarbeitungs-Schleife für die Zip-Datei {} wurde mit dem Status {} abgeschlossen", zipFileName, stateAtExit);
      getLogger().debug("Die Verarbeitungs-Schleife musste in {} Runden pausieren.", sleepingRounds);
      return new ThreadedSearchPerZipResult(stateAtExit, getNumEntriesProcessed(), getNumMatches(), sleepingRounds);
   }

   public int getNumEntriesProcessed() {
      return numEntriesProcessed;
   }

   public int getNumMatches() {
      return numMatches;
   }

   protected Callable<SingleEntrySearchResult> createSearchTask(String entryName, BufferedContentInputStream inputStream) {
      return new SingleEntrySearchCallable<>(xmlMatcherThreadLocals, zipFileInfo, entryName, inputStream, matchListener);
   }

   protected ZipStreamIF getZipStreamWrapperForZipFile(File zipFile) throws FileNotFoundException {
      FileInputStream fileInputStream = new FileInputStream(zipFile);
      BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
      ZipInputStream zipInputStream = new ZipInputStream(bufferedInputStream);
      return new ZipStreamWrapper(zipInputStream);
   }

   protected int scanOpenTasks() throws InterruptedException, ExecutionException {
      List<Future<SingleEntrySearchResult>> doneTasks = new ArrayList<>();
      for (Future<SingleEntrySearchResult> f : openTasks) {
         if (f.isDone()) {
            // Erst mit dem Abruf des Ergebnisses können wir feststellen, ob im Such-Thread eine Exception geflogen ist
            SingleEntrySearchResult result = f.get();
            listenerGroup.notifyMatchingCriteria(result.getMatchingCriteriaList());
            if( result.isMatch() ) {
               numMatches++;
            }
            // Listener über das Ergebnis informieren
            if (matchListener != null) {
               result.getZipEntryInfo().addMatches(result.getMatchingCriteriaList());
               if( result.isMatch() ) {
                  String strInfo = "\t\t\t\tTreffer in ZIP-Entry '" + result.getZipEntryInfo().getZipEntryName() +"'";
                  listenerGroup.updateData( new LogInfo(zipFileInfo.getSearchName(), ProgressListenerIF.LOG_LEVEL.DEBUG, strInfo, null) );
                  zipFileInfo.addZipEntryInfo( result.getZipEntryInfo() );
                  listenerGroup.updateData( result.getZipEntryInfo() );
               }
            } // matchListener != null
            doneTasks.add(f);
         }
      }
      openTasks.removeAll(doneTasks);
      return doneTasks.size();
   }

}
