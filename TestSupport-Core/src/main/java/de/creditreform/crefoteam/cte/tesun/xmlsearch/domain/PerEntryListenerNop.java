package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IGroupByRow;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class PerEntryListenerNop
implements IPerEntryListener {

   @Override
   public int getMatchingCriteriaCount() {
      return 0;
   }

   @Override
   public Collection<String> getMatchingCriteriaList() {
      return Collections.emptyList();
   }

   @Override
   public boolean isEmptyMatchingCriteriaList() {
      return true;
   }

   @Override
   public void resetMatchingCriteriaList() {
      // intentionally empty
   }

   @Override
   public void addMatchingCriterion(String matchingCriterion) {
      // intentionally empty
   }

   @Override
   public void notifyZipEntryStatistics(Map<IGroupByRow, Integer> zipEntryStatistics) {
      // intentionally empty
   }

   @Override
   public Map<IGroupByRow, Integer> getZipEntryStatistics() {
      return Collections.emptyMap();
   }

}
