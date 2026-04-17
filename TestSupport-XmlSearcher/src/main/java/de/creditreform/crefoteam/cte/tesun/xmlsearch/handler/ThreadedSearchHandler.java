package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.*;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IMatchInfoListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility-Klasse für die Handhabung der Multi-Threaded Suche
 */
public class ThreadedSearchHandler {

   protected ExecutorService getSearcherService() {
      return searcherService;
   }

   protected XmlMatcherThreadLocals getThreadLocals() {
      return threadLocals;
   }

   protected IMatchInfoListener getMatchInfoListener() {
      return matchInfoListener;
   }

   protected XmlStreamListenerGroup getStreamListenerGroup() {
      return streamListenerGroup;
   }

   /**
    * Implementierung von {@link ThreadFactory} mit dem Ziel einer sinnvollen Benennung der Threads
    */
   private static class NamedThreadFactory
   implements ThreadFactory {
      private final String namePrefix;
      private final ThreadGroup group;
      private final AtomicInteger threadNumber = new AtomicInteger(1);

      public NamedThreadFactory(String namePrefix) {
         this.namePrefix = namePrefix;
         SecurityManager s = System.getSecurityManager();
         group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
      }

      protected String getNamePrefix() {
         return namePrefix;
      }

      protected ThreadGroup getThreadGroup() {
         return group;
      }

      @Override
      public Thread newThread(Runnable r) {
         Thread t = new Thread(getThreadGroup(), r,
                               getNamePrefix() + threadNumber.getAndIncrement(),
                               0);
         if (t.isDaemon())
         { t.setDaemon(false); }
         if (t.getPriority() != Thread.NORM_PRIORITY)
         { t.setPriority(Thread.NORM_PRIORITY); }
         return t;
      }

   }

   // Anzahl der Lese-/Schreib-Threads, dies steuert die Anzahl der parallel verarbeiteten Zip-Dateien
   private final int numParallelZipFiles = 2;
   // Anzahl der Threads für die Suche in einzelnen Zip-Entries
   private final int numParallelSearchers = 8;
   // Die Queue für Such-Tasks hat in Relation zu der Anzahl der Such-Threads die 2-fache Größe. Die Anzahl der
   // im Speicher vorhandenen Such-Tasks bzw. Zip-Entries ist damit auf diesen Wert begrenzt.
   private final int sizeSearcherQueue = 2*numParallelSearchers;

   private final Logger logger;
   private final AtomicBoolean stopFlag;
   private final ExecutorService producerService;
   private final ExecutorService searcherService;
   private final BlockingQueue<Runnable> searcherQueue;

   private final XmlMatcherThreadLocals threadLocals;
   private final IMatchInfoListener matchInfoListener;
   private final XmlStreamListenerGroup streamListenerGroup;

   private final List<Future<ThreadedSearchPerZipResult>> producerFutures;
   private final AtomicInteger zipFileNumber = new AtomicInteger(1);
   private int numEntriesProcessed;
   private int numMatches;
   private int sleepingRounds;
   private ThreadedSearchPerZipResult.StateAtExit aggregatedState;

   public ThreadedSearchHandler(RuntimeSearchSpec runtimeSearchSpec,
                                XmlMatcherWrapperFactory xmlMatcherWrapperFactory,
                                XmlStreamListenerGroup streamListenerGroup,
                                IMatchInfoListener matchInfoListener) {
      this.logger = LoggerFactory.getLogger( getClass() );
      this.stopFlag = new AtomicBoolean(false);
      this.threadLocals = new XmlMatcherThreadLocals(runtimeSearchSpec, xmlMatcherWrapperFactory, streamListenerGroup);
      this.matchInfoListener = matchInfoListener;
      this.streamListenerGroup = streamListenerGroup;
      producerService = Executors.newFixedThreadPool(numParallelZipFiles, new NamedThreadFactory("XML-Search-Producer-"));
      ThreadFactory searcherThreadFactory = new NamedThreadFactory("XML-Search-Searcher-");
      searcherQueue = new ArrayBlockingQueue<>(sizeSearcherQueue);
      searcherService = new ThreadPoolExecutor(numParallelSearchers, numParallelSearchers,
                                               0L, TimeUnit.MILLISECONDS,
                                               searcherQueue,
                                               searcherThreadFactory);
      producerFutures = new ArrayList<>();
   }

   /**
    * Lese die Summe der Anzahlen von Einträgen in den verarbeiteten Zip-Dateien
    */
   public int getNumEntriesProcessed() {
      return numEntriesProcessed;
   }

   /**
    * Lese die Summe der Anzahlen von Einträgen mit Treffern
    */
   public int getNumMatches() {
      return numMatches;
   }

   public int getSleepingRounds() {
      return sleepingRounds;
   }

   public ThreadedSearchPerZipResult.StateAtExit getAggregatedState() {
      return aggregatedState;
   }

   public void submitZipFile(ZipSearcResult.ZipFileInfo zipFileInfo) {
      final int zipFileNumber = this.zipFileNumber.getAndIncrement();
      ThreadedSearchAbstract producer = getProducer(searcherQueue, zipFileNumber, zipFileInfo);
      producerFutures.add(producerService.submit(producer));
   }

   protected ThreadedSearchAbstract getProducer(BlockingQueue<Runnable> searchTaskQueue, int zipFileNumber, ZipSearcResult.ZipFileInfo zipFileInfo) {
      return new ThreadedSearchProducer(searchTaskQueue, getSearcherService(),
                                        zipFileInfo,
                                        getThreadLocals(), getMatchInfoListener(), getStreamListenerGroup());
   }

   public boolean isCompleted() {
      if (stopFlag.get() || getStreamListenerGroup().getProgressListener().isCanceled()) {
         shutdownNow();
         return true; // eventuell nicht erfolgreich, aber fertig
      }
      else {
         return isDone(producerFutures);
      }
   }

   protected boolean isDone(List<Future<ThreadedSearchPerZipResult>> allFutures) {
      if (stopFlag.get()) {
         logger.info("Suche mit ThreadedSearchProducer wurde manuell gestoppt");
         return true;
      }
      else {
         // Durchsuche alle Futures und sammle die erledigten Tasks...
         boolean allDone = true;
         List<Future<ThreadedSearchPerZipResult>> doneFutures = new ArrayList<>();
         for (Future<ThreadedSearchPerZipResult> f : allFutures) {
            if (!f.isDone()) {
               allDone = false;
            } else {
               doneFutures.add(f);
            }
         }
         // Die erledigten Futures werden nicht erneut betrachtet...
         allFutures.removeAll(doneFutures);
         // Alle Ergebnisse aus erledigten Runnables werden abgefragt, um auf eventuelle Fehler zu testen.
         try {
            for (Future<ThreadedSearchPerZipResult> df : doneFutures) {
               ThreadedSearchPerZipResult result = df.get();
               this.numEntriesProcessed += result.getNumEntriesProcessed();
               this.numMatches += result.getNumMatches();
               this.sleepingRounds += result.getSleepingRounds();
               this.aggregatedState = result.getStateAtExit().aggregateState(this.aggregatedState);
               // TODO: Abbruch bei 'StateAtExit!=SUCCESS'
            }
            return allDone;
         } catch (InterruptedException e) {
            logger.warn("Suche mit ThreadedSearchProducer wurde abgebrochen", e);
            shutdownNow();
            Thread.currentThread().interrupt();
            return true; // nicht erfolgreich, aber fertig
         } catch (ExecutionException e) {
            logger.error("Suche mit ThreadedSearchProducer hat Exception geworfen", e);
            shutdownNow();
            return true; // nicht erfolgreich, aber fertig
         }
      }
   }

   public void shutdownNow() {
      boolean wasStopped = stopFlag.getAndSet(true);
      if (!wasStopped) {
         producerService.shutdownNow();
         getSearcherService().shutdownNow();
      }
   }

   public void waitForCompletion() {
      try {
         while ( !isCompleted() ) {
            Thread.sleep(500);
         }
         logger.info("Es wurden insgesamt {} Zip-Entries verarbeitet", getNumEntriesProcessed());
         logger.info("Treffer wurden bei {} Einträgen gefunden", getNumMatches());
         logger.info("Der Producer musste {} mal auf Ergebnisse warten (pausieren)", getSleepingRounds());
      } catch (InterruptedException e) {
         logger.warn("Suche wurde abgebrochen", e);
         shutdownNow();
         Thread.currentThread().interrupt();
      }
      logger.info("Status nach Beendigung: ", getAggregatedState());
   }

}
