package de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen;

import java.util.Collection;
import java.util.Map;

/**
 * Schnittstelle für den Zugriff auf Anzahl und Beschreibung von Suchtreffern. Die Angaben beziehen
 * sich auf eiunen einzelnen Eintrag in einer Zip-Datei.
 */
public interface IPerEntryStatistics {
   /**
    * Lese die Anzahl der gemeldeten Treffer. Durch die De-Duplizierung kann diese Anzahl höher sein, als
    * die Anzahl der Einträge von {@link #getMatchingCriteriaList()}.
    */
   int getMatchingCriteriaCount();

   /**
    * Lese die (unveränderbare) Menge der getroffenen Suchkriterien
    */
   Collection<String> getMatchingCriteriaList();

   /**
    * Lese die Statistik zum aktuellen Zip-Eintrag
    */
   Map<IGroupByRow, Integer> getZipEntryStatistics();

}
