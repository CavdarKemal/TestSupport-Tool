package de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen;

import java.util.Map;

/**
 * Schnittstelle für das Sammeln und Protokollieren der Suchtreffer durch einen XmlMatcher. Implementierungen
 * dieses Interface sind _nicht_ threadsafe! Der Aufruf beinhaltet keine Angaben zum gelesenen Inhalt oder zum
 * verarbeiteten Zip-Eintrag. Implementierungen dieses Interface dienen somit als Brücke zwischen einem
 * XmlMatcher und einem {@link IMatchInfoListener}.
 */
public interface IPerEntryListener
extends IPerEntryStatistics {
   /**
    * Speichere eine Beschreibung des getroffenen Such-Kriteriums, mehrere Aufrufe mit dem
    * gleichen Such-Kriterium werden durch den Listener zu einem Eintrag zusammengefasst.
    */
   void addMatchingCriterion(String matchingCriterion);

   /**
    * true, wenn bisher keine Suchkriterien getroffen wurden
    */
   boolean isEmptyMatchingCriteriaList();

   /**
    * lösche die interne Liste von getroffenen Suchkriterien sowie die Zähler der Aufrufe
    */
   void resetMatchingCriteriaList();

   /**
    * speichere/aggregiere die Zwischen-Ergebnisse einer Statistik zum Zip-Entry
    */
   void notifyZipEntryStatistics(Map<IGroupByRow, Integer> zipEntryStatistics);

}
