package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IGroupByRow;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryStatistics;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipEntryInfo;

import java.util.*;

/**
 * Container für das Ergebnis aus der Suche in einem einzelnen Zip-Entry
 */
public class SingleEntrySearchResult
implements IPerEntryStatistics {
   private final boolean match;
   private final IZipEntryInfo zipEntryInfo;
   private final int matchingCriteriaCount;
   private final List<String> matchingCriteriaList;
   private final Map<IGroupByRow, Integer> zipEntryStatistics;

   /**
    * Standard-Konstruktor
    * @param match true, wenn ein Treffer erzielt werden konnte
    * @param zipEntryInfo Angaben zum ZipEntry
    * @param matchingCriteriaStatistics Statistik der Treffer
    */
   public SingleEntrySearchResult(boolean match, IZipEntryInfo zipEntryInfo,
                                  IPerEntryStatistics matchingCriteriaStatistics) {
      this.match = match;
      this.zipEntryInfo = zipEntryInfo;
      if (matchingCriteriaStatistics==null) {
         this.matchingCriteriaCount = 0;
         this.matchingCriteriaList = Collections.emptyList();
         this.zipEntryStatistics = Collections.emptyMap();
      }
      else {
         this.matchingCriteriaCount = matchingCriteriaStatistics.getMatchingCriteriaCount();
         Collection<String> newCriteriaList=matchingCriteriaStatistics.getMatchingCriteriaList();
         this.matchingCriteriaList = (newCriteriaList == null) ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<>(newCriteriaList));
         this.zipEntryStatistics = Collections.unmodifiableMap(new HashMap<>(matchingCriteriaStatistics.getZipEntryStatistics()));
      }
   }

   public boolean isMatch() {
      return match;
   }

   public String getZipEntryName() {
      return getZipEntryInfo().getZipEntryName();
   }

   public String getStrCrefoNummer() {
      return getZipEntryInfo().getCrefonummer();
   }

   public IZipEntryInfo getZipEntryInfo() {
      return zipEntryInfo;
   }

   @Override
   public int getMatchingCriteriaCount() {
      return matchingCriteriaCount;
   }

   @Override
   public List<String> getMatchingCriteriaList() {
      return matchingCriteriaList;
   }

   @Override
   public Map<IGroupByRow, Integer> getZipEntryStatistics() {
      return zipEntryStatistics;
   }

}
