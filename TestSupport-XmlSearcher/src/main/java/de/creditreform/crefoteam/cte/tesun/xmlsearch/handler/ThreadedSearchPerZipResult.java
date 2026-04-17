package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

/**
 * Container für die Ergebnisse des {@link ThreadedSearchProducer}. Die Namensgebung geht darauf zurück, dass
 * sich dieses Ergebnis auf eine durchsuchte Zip-Datei bezieht
 */
public class ThreadedSearchPerZipResult {

   /**
    * Zustand beim Verlassen
    */
   public enum StateAtExit {
      // ACHTUNG: Die Reihenfolge der Enums bestimmt auch die Priorisierung bei der Aggregation...
      SUCCESS(true),
      ABORTED(false),
      ERROR(false)

      ;

      private final boolean successfullyCompleted;

      StateAtExit(boolean successfullyCompleted) {
         this.successfullyCompleted = successfullyCompleted;
      }

      /**
       * true, wenn die Verarbeitung erfolgreich abgeschlossen werden konnte
       */
      public boolean isSuccessfullyCompleted() {
         return successfullyCompleted;
      }

      /**
       * erzeuge die Zusammenfassung aus dem angegebenen und dem aktuellen Zustand
       */
      public StateAtExit aggregateState(StateAtExit otherState) {
         // Die Reihenfolge der Enums bestimmt auch die Priorisierung bei der Aggregation...
         if (otherState==null || otherState.ordinal()<=this.ordinal()) {
            return this;
         }
         else {
            return otherState;
         }
      }

   }

   private final StateAtExit stateAtExit;
   private final int numEntriesProcessed;
   private final int numMatches;
   private final int sleepingRounds;

   /**
    * Konstruktor für die Ende-Signalisierung zusammen mit der Anzahl der Einträge in der Zip-Datei
    */
   public ThreadedSearchPerZipResult(StateAtExit stateAtExit, int numEntriesProcessed, int numMatches, int sleepingRounds) {
      this.stateAtExit = stateAtExit;
      this.numEntriesProcessed = numEntriesProcessed;
      this.numMatches = numMatches;
      this.sleepingRounds = sleepingRounds;
   }

   /**
    * Zustand der Verarbeitung einer Zip-Datei beim Verlassen des Producers
    */
   public StateAtExit getStateAtExit() {
      return stateAtExit;
   }

   /**
    * Lese die Anzahl der Zip-Einträge, die verarbeitet wurden
    */
   public int getNumEntriesProcessed() {
      return numEntriesProcessed;
   }

   /**
    * Lese die Anzahl der Zip-Entries, für die ein Treffer erzielt werden konnte
    */
   public int getNumMatches() {
      return numMatches;
   }

   /**
    * Lese die Anzahl der Durchläufe in der Suchschleife, bei denen der Producer eine Pause eingelegen musste
    */
   public int getSleepingRounds() {
      return sleepingRounds;
   }

}
