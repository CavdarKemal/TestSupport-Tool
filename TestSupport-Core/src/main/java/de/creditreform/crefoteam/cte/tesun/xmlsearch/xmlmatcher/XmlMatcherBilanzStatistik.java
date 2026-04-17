package de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.PerEntryListenerNop;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.XmlSearchCursor;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.handler.GroupByRowDefaultImpl;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IGroupByRow;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;

import javax.xml.stream.XMLStreamException;
import java.util.*;
import java.util.function.BiFunction;

public class XmlMatcherBilanzStatistik
extends XmlMatcherLogicAbstract<XmlMatcherGroupBySingle> {

   protected static final String DEFAULT_TAGNAME_OUTER = "bilanzen";
   protected static final List<String> DEFAULT_TAGNAMES_INNER = Collections.unmodifiableList(Arrays.asList("stichtag","quelle"));

   protected static List<XmlMatcherGroupBySingle> createMatchersForTagnames(List<String> innerMatcherTagNames) {
      List<XmlMatcherGroupBySingle> matcherList = new ArrayList<>();
      if (innerMatcherTagNames!=null) {
         for (String imtn : innerMatcherTagNames) {
            if (imtn==null) {
               throw new IllegalArgumentException("Kein Element der Liste von Tag-Namen darf null sein");
            }
            matcherList.add(new XmlMatcherGroupBySingle(true, new MatcherParameterTag(imtn), null));
         }
      }
      return matcherList;
   }

   /**
    * Utility-Klasse für das Ausfiltern der Jahreszahlen in den gefundenen Werten. Mit Hilfe einer Ableitung
    * dieser Klasse können nach Wunsch Typ-Umwandlungen implementiert werden.
    */
   protected static class JahresZahlConverter
   implements BiFunction<Integer, String, String> {
      protected static final int MIN_LENGTH = 5;

      private final char[] chars = new char[MIN_LENGTH];

      /**
       * übersetze den tatsächlich gefundenen String in denjenigen Wert, der in der Statistik gespeichert
       * werden soll
       * @param offsetWithinInnerTagNames offset des Wertes in Relation zu den Tag-Namen der inneren Matcher
       * @param treffer tatsächlich gefundener String
       * @return Wert für die Statistik
       */
      public String apply(Integer offsetWithinInnerTagNames, String treffer) {
         // Hier ist nur Offset 0 innerhalb der Tag-Namen relevant ('stichtag')
         if (offsetWithinInnerTagNames !=null && offsetWithinInnerTagNames==0 &&
             treffer!=null && treffer.length()>= MIN_LENGTH) {
            // wir kopieren die ersten 5 Zeichen
            treffer.getChars(0, MIN_LENGTH, chars, 0);
            // sind das 4 Ziffern únd ein Trennstrich?
            if (isDigit(chars[0], '1') &&
                isDigit(chars[1], '0') &&
                isDigit(chars[2], '0') &&
                isDigit(chars[3], '0') &&
                chars[4]=='-') {
               return treffer.substring(0, 4);
            }
         }
         return treffer;
      }

      public boolean isDigit(char c, char minDigit) {
         return c >= minDigit && c <='9';
      }

   }

   private final XmlPathPredicate outerPathPredicate;
   private final String outerTagName;
   private final Map<IGroupByRow, Integer> mapZwischenErgebnisse;
   private final CountValuesUpdateFixedFunction<IGroupByRow> updateFunction;
   private final IPerEntryListener perEntryListenerNop;
   private final BiFunction<Integer, String, String> jahresZahlConverter;
   private boolean satisfied;

   public XmlMatcherBilanzStatistik() {
      this(DEFAULT_TAGNAME_OUTER, DEFAULT_TAGNAMES_INNER);
   }

   public XmlMatcherBilanzStatistik(String outerTagName, List<String> innerMatcherTagNames) {
      this(new MatcherParameterTag(outerTagName), innerMatcherTagNames);
   }

   public XmlMatcherBilanzStatistik(MatcherParameterTag outerParameterTag, List<String> innerMatcherTagNames) {
      this(new JahresZahlConverter(), outerParameterTag, innerMatcherTagNames);
   }

   protected XmlMatcherBilanzStatistik(BiFunction<Integer, String, String> jahresZahlConverter,
                                       MatcherParameterTag outerParameterTag, List<String> innerMatcherTagNames) {
      super( createMatchersForTagnames(innerMatcherTagNames) );
      this.outerPathPredicate = new XmlPathPredicate(outerParameterTag);
      this.outerTagName = outerParameterTag.getXmlTagName();
      this.mapZwischenErgebnisse = new HashMap<>();
      this.updateFunction = new CountValuesUpdateFixedFunction<>();
      this.perEntryListenerNop = new PerEntryListenerNop();
      this.jahresZahlConverter = jahresZahlConverter;
   }

   @Override
   public XmlMatcherBilanzStatistik reset() {
      // ein reset() wird zunächst an die interne Liste von XmlMatcher-Instanzen durchgereicht
      super.reset();
      mapZwischenErgebnisse.clear();
      // unabhängig von den intern verwendeten XmlMatcher-Instanzen wird ein separates 'satisfied' Flag verwaltet
      this.satisfied = false;
      return this;
   }

   @Override
   public boolean isSatisfied() {
      return satisfied;
   }

   @Override
   public XmlMatcherBilanzStatistik matchCursor(XmlSearchCursor childCursor, IPerEntryListener perEntryListener)
   throws XMLStreamException {
      // Die Protokoll-Einträge aller inneren Matcher werden unterdrückt...
      super.matchCursor(childCursor, perEntryListenerNop);
      return this;
   }

   @Override
   public void notifyExitElement(XmlSearchCursor childCursor, IPerEntryListener perEntryListener) throws XMLStreamException {
      // notifyExitElement wird zunächst an die inneren Matcher durchgereicht
      super.notifyExitElement(childCursor, perEntryListener);
      // der outerMatcher wird beim Exit befragt...
      if (outerTagName.equals(childCursor.getOnExitName()) && outerPathPredicate.isPathMatch(childCursor.getOnExitPath())) {
            satisfied=true;
            // passend zu dem 'outerPathPredicate' wird ein Protokoll-Eintrag erzeugt...
            perEntryListener.addMatchingCriterion("Treffer beim Exit aus "+ childCursor.getOnExitName() );
            // Treffer im äußeren Matcher erkannt, Zwischen-Ergebnisse zu einem String kombinieren
            final List<String> valueList = new ArrayList<>();
            final List<XmlMatcherGroupBySingle> childXmlMatchers = getChildXmlMatchers();
            if (childXmlMatchers!=null && !childXmlMatchers.isEmpty()) {
               int offset=0;
               for (XmlMatcherGroupBySingle mgbs : childXmlMatchers) {
                  valueList.add(jahresZahlConverter.apply(offset, mgbs.getMatcherResult()) );
               }
            }
            // Zwischen-Ergebnisse in der Map protokollieren...
            mapZwischenErgebnisse.compute(new GroupByRowDefaultImpl(valueList), updateFunction);
            // Die 'inneren' Matcher werden hier zurück gesetzt, um einen weiteren Treffer vorzubereiten
            super.reset();
         }
      }

   @Override
   public void notifyZipEntryCompleted(boolean success, IPerEntryListener perEntryListener) {
      if (success) {
         // Zwischen-Ergebnisse weiter reichen
         perEntryListener.notifyZipEntryStatistics(this.getMapZwischenErgebnisse());
      }
      // TODO: klären, ob die 'inneren' Matcher benachrichtigt werden müssen/sollen/können
      super.notifyZipEntryCompleted(success, perEntryListenerNop);
   }

   protected Map<IGroupByRow, Integer> getMapZwischenErgebnisse() {
      return mapZwischenErgebnisse;
   }

}
