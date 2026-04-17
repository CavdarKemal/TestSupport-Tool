package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IGroupByRow;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher.XmlMatcherGroupByCountValues;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher.XmlMatcherGroupBySingle;

import java.util.*;

/**
 * Utility-Klasse für die Berechnung der Group-By Ergebnisse. Diese Implementierung ist _nicht_ threadsafe,
 * sie speichert intern Zwischenergebnisse und vermeidet dadurch eine Reihe von Kopier-Vorgängen.
 */
public class GroupByResultsAggregator {

   /**
    * Liste der Fragmente, aus denen später ein {@link IGroupByRow} gebildet wird. Vor dem Aufruf von
    * {@link #setCounters} beinhaltet die Liste alle ausser dem letzten Fragment.
    */
   private final List<String> groupBySingleMatcherFragments;
   private final Map<IGroupByRow, Integer> resultMap;
   private final Map<IGroupByRow, Integer> unmodifiableResult;

   public GroupByResultsAggregator() {
      groupBySingleMatcherFragments = new ArrayList<>();
      resultMap = new HashMap<>();
      unmodifiableResult = Collections.unmodifiableMap(resultMap);
   }

   public GroupByResultsAggregator addResultsSingle(List<XmlMatcherGroupBySingle> groupBySingleMatchers) {
      if (groupBySingleMatchers!=null) {
         for (XmlMatcherGroupBySingle s : groupBySingleMatchers) {
            if (s!=null) {
               addSingle(s.getMatcherResult());
            }
            else {
               // Die relativen Offsets müssen erhalten bleiben. Das gilt auch dann, wenn eine Matcher-Instanz null ist.
               addSingle(null);
            }
         }
      }
      return this;
   }

   /**
    * Hook für abgeleitete Klassen und den Test, das angegebene Fragment wird der internen Liste hinzu gefügt
    * @param singleKeyFragment einzelnes Fragment
    */
   protected GroupByResultsAggregator addSingle(String singleKeyFragment) {
      groupBySingleMatcherFragments.add(singleKeyFragment);
      return this;
   }

   /**
    * überschreibe die internen Werte mit den angegebenen Daten, Rückgabewert ist die (nicht änderbare) Ergebnis-Map
    */
   public Map<IGroupByRow, Integer> setCounters(XmlMatcherGroupByCountValues countValuesMatcher) {
      Map<String,Integer> counterMap = (countValuesMatcher==null) ? null : countValuesMatcher.getResultMap();
      return setCounters(counterMap);
   }

   /**
    * überschreibe die internen Werte mit den angegebenen Daten, Rückgabewert ist die (nicht änderbare) Ergebnis-Map
    */
   public Map<IGroupByRow, Integer> setCounters(Map<String,Integer> counterMap) {
      this.resultMap.clear();
      if (counterMap!=null) {
         final int keySize = groupBySingleMatcherFragments.size()+1;
         for (Map.Entry<String, Integer> ec : counterMap.entrySet()) {
            List<String> newKey = new ArrayList<>(keySize);
            newKey.addAll(groupBySingleMatcherFragments);
            newKey.add(ec.getKey());
            resultMap.put( new GroupByRowDefaultImpl(newKey), ec.getValue() );
         }
      }
      return getCollectedResults();
   }

   /**
    * Addiere die angegebenen Ergebnisse zu den intern gespeicherten Werten, Rückgabewert ist die (nicht änderbare)
    * Ergebnis-Map
    */
   public Map<IGroupByRow, Integer> addCounters(Map<IGroupByRow, Integer> other) {
      if (other!=null) {
         mergeInto(this.resultMap, other);
      }
      return getCollectedResults();
   }

   protected void mergeInto(Map<IGroupByRow, Integer> target, Map<IGroupByRow, Integer> other) {
      for (Map.Entry<IGroupByRow, Integer> e : other.entrySet()) {
         if (e.getValue()!=null && e.getValue()>0) {
            final int numberToAdd = e.getValue();
            target.compute(e.getKey(), (IGroupByRow key, Integer prev)-> {
               if (prev==null) {
                  return numberToAdd;
               }
               else {
                  return prev+numberToAdd;
               }
            });
         }
      }
   }

   /**
    * Lese die bis hierher gesammelten Ergebnisse aus dem Group-By
    */
   public Map<IGroupByRow, Integer> getCollectedResults() {
      return unmodifiableResult;
   }

}
