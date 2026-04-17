package de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.XmlSearchCursor;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;

import javax.xml.stream.XMLStreamException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementierung von XmlMatcher für das Zählen verschiedener Ausprägungen im Rahmen des Group-By. Gezählt werden
 * die Treffer für verschiedene Element-Werte.
 * Für jedes Group-By kann es maximal eine Instanz dieser Klasse geben. Hintergrund ist, dass die Ergebnisse
 * mehrerer Instanzen nicht sinnvoll miteinander kombiniert werden können.
 */
public class XmlMatcherGroupByCountValues
extends XmlMatcherTagNameOnly {

   private static CountValuesUpdateFixedFunction<String> updateFunction = new CountValuesUpdateFixedFunction<>();

   private final Map<String, Integer> resultMap;

   public XmlMatcherGroupByCountValues(MatcherParameterTag matcherParameterTag) {
      super(matcherParameterTag);
      this.resultMap = new HashMap<>();
   }

   @Override
   protected boolean onMatchFound(XmlSearchCursor elementCursor, IPerEntryListener perEntryListener) throws XMLStreamException {
      // Anzahl für diese Ausprägung erhöhen...
      String valueFound = elementCursor.getElemStringValue();
      resultMap.compute(valueFound, updateFunction);
      return false;
   }

   @Override
   public XmlMatcherGroupByCountValues reset() {
      resultMap.clear();
      super.reset();
      return this;
   }

   public boolean isEmpty() {
      return resultMap.isEmpty();
   }

   public Map<String, Integer> getResultMap() {
      return Collections.unmodifiableMap(resultMap);
   }

}
