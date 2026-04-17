package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IGroupByRow;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;

import java.util.*;

public class PerEntryListenerDefaultImpl
implements IPerEntryListener {

   private final Set<String> matchingCriteriaSet = new LinkedHashSet<>();
   private int matchingCriteriaCount=0;
   private final Map<IGroupByRow, Integer> mapEntryStatistics=new HashMap<>();

   @Override
   public void addMatchingCriterion(String matchingCriterion) {
      if (matchingCriterion!=null) {
         matchingCriteriaSet.add(matchingCriterion);
         matchingCriteriaCount++;
      }
   }

   @Override
   public int getMatchingCriteriaCount() {
      return matchingCriteriaCount;
   }

   @Override
   public boolean isEmptyMatchingCriteriaList() {
      return matchingCriteriaSet.isEmpty();
   }

   @Override
   public void resetMatchingCriteriaList() {
      matchingCriteriaSet.clear();
      matchingCriteriaCount = 0;
      mapEntryStatistics.clear();
   }

   @Override
   public List<String> getMatchingCriteriaList() {
      // Wir liefern eine Kopie! Es ist dem Aufrufer überlassen, ob er sich den Rückgabewert als Ganzes speichert
      // oder nur die Inhalte ausliest. In keinem Fall darf sich der Inhalt nachträglich ändern.
      List<String> currentState = new ArrayList<>(matchingCriteriaSet);
      return Collections.unmodifiableList(currentState);
   }

   @Override
   public void notifyZipEntryStatistics(Map<IGroupByRow, Integer> zipEntryStatistics) {
      if (zipEntryStatistics!=null && !zipEntryStatistics.isEmpty()) {
         // Es ist legal, diese Methode mehrfach aufzurufen, trotzdem ist ein Aufaddieren der einzelnen
         // Werte nicht erforderlich: jeder Aufruf liefert die vollständigen Daten zu einer Statistik, weitere
         // Aufrufe beziehen sich auf andere Statistiken.
         mapEntryStatistics.putAll(zipEntryStatistics);
      }
   }

   @Override
   public Map<IGroupByRow, Integer> getZipEntryStatistics() {
      return Collections.unmodifiableMap(mapEntryStatistics);
   }

}
