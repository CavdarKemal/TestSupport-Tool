package de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher;

import javax.xml.stream.XMLStreamException;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.XmlSearchCursor;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper für eine Instanz von {@link XmlMatcher}, der nur Ergebnisse mit einem passenden Pfad an den
 * 'wrappedMatcher' weiter reicht. Dabei gelten folgende Regeln:
 * - Der Vergleich der Pfad-Teile einspricht {@link String#endsWith(String)}. Zu dem Filter 'U.V.W.?' passt daher
 *   'T.U.V.W', 'K.U.V.W' oder 'U.V.W' nicht aber 'U.V.X' oder 'U.V.W.K'.
 * - Der vorgegebene Pfad wird zerlegt: für den letzten Teil bleibt der innere Matcher verantwortlich, alle Teile
 *   davor prüft der {@link XmlMatcherPath}. Bei der Vorgabe 'A.B.C' filtert diese Klasse somit den Pfad 'A.B'
 *   heraus, für 'C' bleibt der innere Matcher zuständig.
 */
public class XmlMatcherPath implements XmlMatcher
{
  protected static Logger    logger = LoggerFactory.getLogger( XmlMatcherPath.class );
  private final XmlMatcher   wrappedMatcher;
  private final XmlPathPredicate pathPredicate;

  public XmlMatcherPath( MatcherParameterTag matcherParameterTag, XmlMatcher wrappedMatcher )
  {
    this.wrappedMatcher = wrappedMatcher;
    this.pathPredicate = new XmlPathPredicate(matcherParameterTag);
  }

  /*********************************************************************************************/
  /********************************       XmlMatcher        ************************************/
  /*********************************************************************************************/
  @Override public XmlMatcherPath matchCursor(XmlSearchCursor childCursor, IPerEntryListener perEntryListener) throws XMLStreamException
  {
    if( pathPredicate.test( childCursor ) )
    {
      wrappedMatcher.matchCursor(childCursor, perEntryListener);
    }
    return this;
  }

  @Override
  public void notifyExitElement(XmlSearchCursor childCursor, IPerEntryListener perEntryListener) throws XMLStreamException {
    if( pathPredicate.test( childCursor ) )
    {
      wrappedMatcher.notifyExitElement(childCursor, perEntryListener);
    }
  }

  @Override public boolean isSatisfied()
  {
    return wrappedMatcher.isSatisfied();
  }

  @Override public XmlMatcherPath reset()
  {
    wrappedMatcher.reset();
    return this;
  }

  @Override
  public void notifyZipEntryCompleted(boolean success, IPerEntryListener perEntryListener) {
    // intentionally empty
  }

}
