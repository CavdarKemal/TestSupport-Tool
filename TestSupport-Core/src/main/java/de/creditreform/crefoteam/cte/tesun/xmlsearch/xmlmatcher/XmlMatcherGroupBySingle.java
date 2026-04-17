package de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.XmlSearchCursor;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;

import javax.xml.stream.XMLStreamException;
import java.util.Collection;

/**
 * {@link XmlMatcher}-Implementierung für die Ermittlung einzelner Werte im Rahmen eines Group-By. Im Gegensatz zu
 * {@link XmlMatcherGroupByCountValues} können mehrere Instanzen dieser Klasse in einer Suche bzw. einem
 * Group-By verwendet werden. Zusätzlich dient diese Klasse der Ermittlung der Crefonummer.
 */
public class XmlMatcherGroupBySingle
extends XmlMatcherTagNameOnly {

   private final boolean onMatchIsSatisfied;
   protected String matcherResult;

   public XmlMatcherGroupBySingle(boolean onMatchIsSatisfied, MatcherParameterTag matcherParameterTag, Collection<String> alternateTagNames) {
      super(matcherParameterTag, alternateTagNames);
      this.onMatchIsSatisfied = onMatchIsSatisfied;
   }

   @Override
   protected boolean onMatchFound(XmlSearchCursor elementCursor, IPerEntryListener perEntryListener)
   throws XMLStreamException {
       matcherResult = elementCursor.getElemStringValue();
       logger.debug("\t\t\t*** Treffer bei XmlMatcherGroupBySingle: {} = {}", getXmlTagName(), matcherResult);
       return onMatchIsSatisfied;
   }

   public String getMatcherResult() {
       return matcherResult;
   }

   @Override
   public XmlMatcherGroupBySingle reset() {
      super.reset();
      this.matcherResult = null;
      return this;
   }

}
