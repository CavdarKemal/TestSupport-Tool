package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.handler.*;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IMatchInfoListener;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * Container für die zur Laufzeit benötigten Daten, berechnet aus einer vorgegebenen {@link SearchSpecification}
 */
public class RuntimeSearchSpec {

   protected static final List<String> knownStatisticsTags = Collections.unmodifiableList(Arrays.asList("STATISTICS"));

   private final Logger logger;
   private SearchSpecification copyOfSearchSpecification;
   private IMatchInfoListener  matchInfoListener;

   public RuntimeSearchSpec() {
      this.logger = LoggerFactory.getLogger( getClass() );
   }

   public RuntimeSearchSpec configure(SearchSpecification searchSpecification) {
      this.copyOfSearchSpecification = new SearchSpecification( searchSpecification );
      matchInfoListener = createMatchInfoListener();
      return this;
   }

   /**
    * Erzeuge eine Instanz des Listeners für die Such-Ergebnisse
    */
   protected IMatchInfoListener createMatchInfoListener()
   {
      boolean enableStatistics = false;
      for( SearchCriteria searchCriteria : getCopyOfSearchSpecification().getSearchCriteriasList()) {
         if(searchCriteria.getSearchTag().equals(SearchCriteria.XML_MATCHER_STATISTICS_TAG_NAME)) {
            enableStatistics = true;
         }
      }

      final List<IMatchInfoListener> listenerList = new ArrayList<>();
      final TestSupportClientKonstanten.SEARCH_RESULT_TYPE selectedResultType;
      TestSupportClientKonstanten.SEARCH_RESULT_TYPE searchResultsType = getCopyOfSearchSpecification().getSearchResultsType();
      if( TestSupportClientKonstanten.SEARCH_RESULT_TYPE.CREFOS_COUNT.equals(searchResultsType) )
      {
         selectedResultType = TestSupportClientKonstanten.SEARCH_RESULT_TYPE.CREFOS_COUNT;
         listenerList.add( new CountCrefosMatchInfoListener(searchResultsType.name() ) );
      }
      else if( TestSupportClientKonstanten.SEARCH_RESULT_TYPE.CREFOS_LIST.equals(searchResultsType) )
      {
         selectedResultType = TestSupportClientKonstanten.SEARCH_RESULT_TYPE.CREFOS_LIST;
         listenerList.add( new SaveCrefosListMatchInfoListener(selectedResultType.name(),
                                                               getCopyOfSearchSpecification().getSearchResultsPath(),
                                                               getCopyOfSearchSpecification().getName()) );
      }
      else
      {
         selectedResultType = TestSupportClientKonstanten.SEARCH_RESULT_TYPE.CREFOS_XML;
         listenerList.add( new SaveXmlsMatchInfoListener( selectedResultType.name(),
                                                          getCopyOfSearchSpecification().getSearchResultsPath(),
                                                          getCopyOfSearchSpecification().getName()) );
      }
      getCopyOfSearchSpecification().setSearchResultsType(selectedResultType);

      // gegebenenfalls muss ein zweiter Listener für den Abgleich mit einer CSV-Datei ergänzt werden...
      String pfadCsvAbgleichDatei = "./crefos-zum-abgleich.csv"; // TODO: Dateiname variabel (Pfad identisch mit Zip-Verzeichnis)
      CsvAbgleichMatchInfoListener abgleichMatchInfoListener = new CsvAbgleichMatchInfoListener(getCopyOfSearchSpecification().getSourceFile().getAbsolutePath(),
                                                                                                pfadCsvAbgleichDatei, selectedResultType,
                                                                                                getCopyOfSearchSpecification().getSearchResultsPath(),
                                                                                                getCopyOfSearchSpecification().getName() );
      if (abgleichMatchInfoListener.isActive()) {
         listenerList.add( abgleichMatchInfoListener);
      }

      // gegebenenfalls benötigen wir einen dritten Listener für die Auswertung der Statistiken
      if (enableStatistics) {
         listenerList.add( new CollectStatisticsMatchInfoListener( selectedResultType,
                                                                   getCopyOfSearchSpecification().getSearchResultsPath(),
                                                                   getCopyOfSearchSpecification().getName()) );
      }

      // abhängig von der Anzahl der erforderlichen Listener-Instanzen liefern wir MatchInfoListenerCollection
      MatchInfoListenerCollection listenerCollection = new MatchInfoListenerCollection();
      listenerCollection.addListeners(listenerList);
      return listenerCollection;

   }

   public String getName() {
      return getCopyOfSearchSpecification().getName();
   }

   public File getSourceFile() {
      return getCopyOfSearchSpecification().getSourceFile();
   }

   public void setSourceFile(File sourceFile) {
      getCopyOfSearchSpecification().setSourceFile(sourceFile);
   }

   public IMatchInfoListener getMatchInfoListener()
   {
      return matchInfoListener;
   }

   public XmlMatcher buildXmlMatcherSearch(XmlMatcherWrapperFactory wrapperFactory) {
      XmlMatcher plainMatcher = buildXmlMatcherSearch();
      if (wrapperFactory!=null) {
         return wrapperFactory.wrapXmlMatcher(getCopyOfSearchSpecification().getCrefoNrTagName(), plainMatcher);
      }
      else {
         return plainMatcher;
      }
   }

   public XmlStreamProcessorIF createXmlStreamProcessor(XmlMatcher xmlMatcher) {
      final XmlStreamProcessorIF xmlStreamProcessor;
      switch (getCopyOfSearchSpecification().getUsedXmlStreamProcessor()) {
         case RECURSIVE: {
            xmlStreamProcessor = new XmlStreamProcessorRecursive(xmlMatcher, getCopyOfSearchSpecification().getName());
         }
         break;
         case LINEAR:
         default: {
            xmlStreamProcessor = new XmlStreamProcessorLinear(xmlMatcher, getCopyOfSearchSpecification().getName());
         }
      }
      getLogger().debug("*** RuntimeSearchSpec benutzt {} ***", getCopyOfSearchSpecification().getUsedXmlStreamProcessor() );
      return xmlStreamProcessor;
   }

   /**
    * Erzeuge einen {@link XmlMatcher} für das Abarbeiten der Such-Kriterien. Darin (noch) nicht enthalten sind
    * Matcher für die Crefonummer oder Gruppierungs-Funktionen
    */
   protected XmlMatcher buildXmlMatcherSearch()
   {
      SearchSpecification searchSpecification = getCopyOfSearchSpecification();
      List<XmlMatcher> searchCriteriaMatcherList = new ArrayList<>(searchSpecification.getSearchCriteriasList().size() );
      for( SearchCriteria searchCriteria : searchSpecification.getSearchCriteriasList() )
      {
         if( searchCriteria.isActivated() )
         {
            final XmlMatcher xmlMatcher;
            if(knownStatisticsTags.contains(searchCriteria.getSearchTag())) {
               xmlMatcher = new XmlMatcherBilanzStatistik();
            }
            else {
               xmlMatcher = createXmlMatcherSearchCriteria(searchCriteria.getSearchTag(), searchCriteria.getSearchValue());
            }
            searchCriteriaMatcherList.add(xmlMatcher);
         }
      }
      final XmlMatcherLogicAbstract xmlMatcherSearch;
      if(searchSpecification.isLogicalConnectionOr()) {
         xmlMatcherSearch = new XmlMatcherLogicOr(searchCriteriaMatcherList );
      }
      else {
         xmlMatcherSearch = new XmlMatcherLogicAnd(searchCriteriaMatcherList );
      }
      if( searchSpecification.isInvertedResults() )
      {
         return new XmlMatcherLogicNot(xmlMatcherSearch );
      }
      else
      {
         return xmlMatcherSearch;
      }
   }

   protected XmlMatcher createXmlMatcherSearchCriteria(String xmlTagName, String xmlTagValue) {
      if ((xmlTagValue != null) && !xmlTagValue.isEmpty()) {
         xmlTagValue = xmlTagValue.replaceAll("\"", "");
      }
      XmlMatcher xmlMatcher;
      if(xmlTagName.startsWith(SearchCriteria.XML_MATCHER_ONE_OF_LIST_TAG_NAME)) {
         XmlMatcherOneOfList xmlMatcherOneOfList = new XmlMatcherOneOfList(null, XmlMatcherCrefo.DEFAULT_TAGNAMES_CREFO);
         File theFile = new File(xmlTagValue);
         if (!theFile.isAbsolute()) {
            theFile = new File(getCopyOfSearchSpecification().getSourceFile(), xmlTagValue);
         }
         xmlMatcher = xmlMatcherOneOfList.initFromFile(theFile.getAbsolutePath());
         // hat das Ding auch Parameter? (Tag-Name hinter dem ::)
         String[] split = xmlTagName.split("::");
         if(split.length == 2) {
            XmlMatcherTagNameOnly xmlMatcherTagNameOnly = new XmlMatcherTagNameOnly(new MatcherParameterTag(split[1]));
            xmlMatcher = new XmlMatcherLogicAnd(xmlMatcher, xmlMatcherTagNameOnly);
         }
         return xmlMatcher;
      }
      else {
         MatcherParameterTag parameterTag = new MatcherParameterTag(xmlTagName);
         if (xmlTagValue == null || xmlTagValue.length()==0) {
            xmlMatcher = new XmlMatcherTagNameOnly(parameterTag, Collections.EMPTY_LIST);
         } else {
            xmlMatcher = new XmlMatcherSearchCriteria(parameterTag, xmlTagValue);
         }
         return wrapForPath(parameterTag, xmlMatcher);
      }
   }

   protected XmlMatcher wrapForPath( MatcherParameterTag parameterTag, XmlMatcher matcherOhnePfad )
   {
      if( parameterTag == null || parameterTag.isPathEmpty() )
      {
         return matcherOhnePfad;
      }
      else
      {
         return new XmlMatcherPath( parameterTag, matcherOhnePfad );
      }
   }

   protected Logger getLogger() {
      return logger;
   }

   public SearchSpecification getCopyOfSearchSpecification() {
      if (copyOfSearchSpecification ==null) {
         throw new IllegalStateException("Methode kann nicht vor dem 'configure' aufgerufen werden");
      }
      return copyOfSearchSpecification;
   }
}
