package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public abstract class ThreadedSearchAbstract
implements Callable<ThreadedSearchPerZipResult> {
   private final Logger logger;

   public ThreadedSearchAbstract() {
      this.logger = LoggerFactory.getLogger(getClass());
   }

   @Override
   public ThreadedSearchPerZipResult call() {
      try {

         return doInterruptible();

      } catch (InterruptedException e) {
         getLogger().warn("{} interrupted", getClass().getSimpleName(), e);
         throw new RuntimeException(e); // TODO
      }

   }

   /**
    * bearbeite die eigentliche Aufgabe des {@link Runnable}, um die {@link InterruptedException} kümmert sich
    * diese Basis-Klasse
    */
   protected abstract ThreadedSearchPerZipResult doInterruptible() throws InterruptedException;

   protected Logger getLogger() {
      return logger;
   }

}
