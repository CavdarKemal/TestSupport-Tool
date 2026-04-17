package de.creditreform.crefoteam.cte.tesun.xmlsearch;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.XmlSearchCursor;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.XmlMatcherWrapperFactory;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.handler.GroupByResultsAggregator;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IGroupByRow;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher.*;

import javax.xml.stream.XMLStreamException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class XmlMatcherWrapperFactoryCrefoExport implements XmlMatcherWrapperFactory
{
  /**
   * Wrapper für die Kombination von {@link XmlMatcherCrefo} mit einem Gesamt-Matcher. Dies ist bewusst keine Variante
   * einer auf Listen basierenden {@link XmlMatcher}-Implementierung. Für die eigentliche Analyse der gelesenen
   * Daten ist allein der Gesamt-Matcher zuständig, dieser muss also den {@link XmlMatcherCrefo} bereits beinhalten.
   * Einziger Zweck des {@link XmlMatcherWrapperThisFactory} ist, dass später auf die ermittelte Crefonummer zugegriffen
   * werden kann.
   */
  public static class XmlMatcherWrapperThisFactory
      implements XmlMatcher
  {
    private final XmlMatcherCrefo xmlMatcherCrefo;
    private final XmlMatcher      xmlMatcherComplete;
    private final List<XmlMatcherGroupBySingle> groupBySingleMatchers;
    private final XmlMatcherGroupByCountValues groupByCountValuesMatcher;

    public XmlMatcherWrapperThisFactory(XmlMatcherCrefo xmlMatcherCrefo, XmlMatcher xmlMatcherComplete )
    {
      this.xmlMatcherCrefo = xmlMatcherCrefo;
      this.xmlMatcherComplete = xmlMatcherComplete;
      this.groupBySingleMatchers = Collections.emptyList();
      this.groupByCountValuesMatcher = null;
    }

    @Override public XmlMatcherWrapperThisFactory matchCursor(XmlSearchCursor childCursor, IPerEntryListener perEntryListener)
        throws XMLStreamException
    {
      // hier ist der Aufruf des xmlMatcherCrefo nicht sinnvoll
      xmlMatcherComplete.matchCursor( childCursor, perEntryListener );
      return this;
    }

    @Override
    public void notifyExitElement(XmlSearchCursor childCursor, IPerEntryListener perEntryListener) throws XMLStreamException {
      xmlMatcherComplete.notifyExitElement(childCursor, perEntryListener);
    }

    @Override public boolean isSatisfied()
    {
      // allein der xmlMatcherComplete ist für das Ergebnis der Suche verantwortlich
      return xmlMatcherComplete.isSatisfied();
    }

    @Override public XmlMatcherWrapperThisFactory reset()
    {
      // Der Aufruf der Reset-Methode ist der einzige, der auch an den xmlMatcherCrefo durchgereicht werden darf!
      getXmlMatcherCrefo().reset(); // vermutlich unnötig...
      getXmlMatcherComplete().reset();
      return this;
    }

    @Override
    public void notifyZipEntryCompleted(boolean success, IPerEntryListener perEntryListener) {
      getXmlMatcherCrefo().notifyZipEntryCompleted(success, perEntryListener);
      getXmlMatcherComplete().notifyZipEntryCompleted(success, perEntryListener);
    }

    protected String getMatcherResult()
    {
      return getXmlMatcherCrefo().getMatcherResult();
    }

    protected XmlMatcherCrefo getXmlMatcherCrefo()
    {
      return xmlMatcherCrefo;
    }

    protected XmlMatcher getXmlMatcherComplete()
    {
      return xmlMatcherComplete;
    }

    protected List<XmlMatcherGroupBySingle> getGroupBySingleMatchers() {
      return groupBySingleMatchers;
    }

    protected XmlMatcherGroupByCountValues getGroupByCountValuesMatcher() {
      return groupByCountValuesMatcher;
    }
  }

  /*********************************************************************************************/
  /*****************************          XmlMatcherFactory         ****************************/
  /*********************************************************************************************/
  @Override public XmlMatcher wrapXmlMatcher( String crefoNrTagName, XmlMatcher xmlMatcherSpec )
  {
    final MatcherParameterTag parameterTagCrefo;
    if( crefoNrTagName == null )
    {
      parameterTagCrefo = null;
    }
    else
    {
      parameterTagCrefo = new MatcherParameterTag( crefoNrTagName );
    }

    final XmlMatcherCrefo xmlMatcherCrefo = new XmlMatcherCrefo( parameterTagCrefo );
//    final XmlMatcherFindFirst mmlMatcherFindFirst = new XmlMatcherFindFirst( searchSpecificationProcessor.wrapForPath( parameterTagCrefo, xmlMatcherCrefo ) );
    final XmlMatcher xmlMatcherComplete = buildUnwrappedMatcherOthers(xmlMatcherSpec, xmlMatcherCrefo );
    return new XmlMatcherWrapperThisFactory(xmlMatcherCrefo, xmlMatcherComplete );
  }

  /**
   * Factory-Methode für XmlMatcher zu allen Suchkriterien, die vom Anwender
   * in der Parameter-Datei spezifiziert wurden. Kriterien, die aus der Art der
   * durchsuchten Daten abgeleitet sind schliesst dies explizit aus. Für den
   * aktuellen Anwendungsfall sind also die Kriterien bezüglich der Crefonummer
   * nicht enthalten
   *
   * @param xmlMatcherCrefo XmlMatcher für die Suche nach der Crefonummer
   */
  protected XmlMatcher buildUnwrappedMatcherOthers(XmlMatcher xmlMatcherSpec, XmlMatcher xmlMatcherCrefo )
  {
    if(xmlMatcherSpec!=null) {
      return new XmlMatcherLogicAnd(xmlMatcherCrefo, xmlMatcherSpec);
    }
    else {
      return xmlMatcherCrefo;
    }
  }

  protected XmlMatcherWrapperThisFactory castWrapper(XmlMatcher xmlMatcher) {
    XmlMatcherWrapperThisFactory myWrapper;
    if( xmlMatcher == null )
    {
      throw new NullPointerException( "Parameter 'xmlMatcher' darf nicht NULL sein" );
    }
    else if( !( xmlMatcher instanceof XmlMatcherWrapperThisFactory) )
    {
      throw new IllegalArgumentException( "Parameter 'xmlMatcher' wurde nicht durch diese Factory erzeugt" );
    }
    else
    {
      myWrapper = (XmlMatcherWrapperThisFactory) xmlMatcher;
    }
    return myWrapper;
  }

  @Override public String getResultIdentification( XmlMatcher xmlMatcher )
  {
    return castWrapper(xmlMatcher).getMatcherResult();
  }

  @Override
  public Map<IGroupByRow, Integer> getGroupByResults(XmlMatcher xmlMatcher) {
    XmlMatcherWrapperThisFactory myWrapper = castWrapper(xmlMatcher);
    GroupByResultsAggregator aggregator = new GroupByResultsAggregator().addResultsSingle(myWrapper.getGroupBySingleMatchers());
    return aggregator.setCounters( myWrapper.getGroupByCountValuesMatcher() );
  }

}
