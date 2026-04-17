package de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.XmlSearchCursor;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class XmlMatcherLogicAbstract<M extends XmlMatcher>
extends XmlMatcherAbstract
{
  private final List<M> internalMatcherList;

  public XmlMatcherLogicAbstract( List<M> matcherList )
  {
    super( null );
    if( matcherList == null || matcherList.isEmpty() )
    {
      throw new IllegalArgumentException( "Liste der verknüpften Instanzen von XmlMatcher darf nicht leer sein" );
    }
    this.internalMatcherList = new ArrayList<>(matcherList);
  }

  /**
   * Zugriff für abgeleitete Klassen auf die interne, modifizierbare Liste von XmlMatcher-Instanzen
   */
  protected List<M> getInternalMatcherList()
  {
    return internalMatcherList;
  }

  public boolean add( M e )
  {
    return getInternalMatcherList().add( e );
  }

  /**
   * öffentlicher Zugriff auf die (unveränderbare) Liste von XmlMatcher-Instanzen
   */
  public List<M> getChildXmlMatchers()
  {
    return Collections.unmodifiableList(getInternalMatcherList());
  }

  /****************************************************************************************/
  /************************            XmlMatcherAbstract       ***************************/
  /****************************************************************************************/
  @Override public XmlMatcherLogicAbstract reset()
  {
    for( XmlMatcher xm : getInternalMatcherList())
    {
      xm.reset();
    }
    return this;
  }

  @Override public void notifyZipEntryCompleted(boolean success, IPerEntryListener perEntryListener)
  {
    for( XmlMatcher xm : getInternalMatcherList())
    {
      xm.notifyZipEntryCompleted(success, perEntryListener);
    }
  }

  @Override public XmlMatcherLogicAbstract matchCursor(XmlSearchCursor childCursor, IPerEntryListener perEntryListener) throws XMLStreamException
  {
    for( XmlMatcher xm : getInternalMatcherList())
    {
      xm.matchCursor(childCursor, perEntryListener);
    }
    return this;
  }

  @Override
  public void notifyExitElement(XmlSearchCursor childCursor, IPerEntryListener perEntryListener) throws XMLStreamException {
    for( XmlMatcher xm : getInternalMatcherList())
    {
      xm.notifyExitElement(childCursor, perEntryListener);
    }
  }

}
