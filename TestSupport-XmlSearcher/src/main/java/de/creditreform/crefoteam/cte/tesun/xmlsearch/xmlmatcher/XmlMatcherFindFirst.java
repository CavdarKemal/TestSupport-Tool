package de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher;

import javax.xml.stream.XMLStreamException;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.XmlSearchCursor;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;

public class XmlMatcherFindFirst implements XmlMatcher
{

  private final XmlMatcher wrappedMatcher;
  private boolean          firstFound;
  private boolean          firstExit;

  public XmlMatcherFindFirst( XmlMatcher wrappedMatcher )
  {
    this.wrappedMatcher = wrappedMatcher;
  }

  public XmlMatcher getWrappedMatcher()
  {
    return wrappedMatcher;
  }

  /*********************************************************************************************/
  /********************************       XmlMatcher        ************************************/
  /*********************************************************************************************/
  @Override public XmlMatcherFindFirst matchCursor(XmlSearchCursor childCursor, IPerEntryListener perEntryListener) throws XMLStreamException
  {
    if( !firstFound )
    {
      wrappedMatcher.matchCursor(childCursor, perEntryListener);
      firstFound |= wrappedMatcher.isSatisfied();
    }
    return this;
  }

  @Override
  public void notifyExitElement(XmlSearchCursor childCursor, IPerEntryListener perEntryListener) throws XMLStreamException {
    // nach 'firstFound' soll maximal ein Aufruf von 'notifyExitElement' erfolgen
    if( !firstExit )
    {
      wrappedMatcher.notifyExitElement(childCursor, perEntryListener);
      firstExit |= firstFound;
    }
  }

  @Override public boolean isSatisfied()
  {
    return wrappedMatcher.isSatisfied();
  }

  @Override public XmlMatcherFindFirst reset()
  {
    wrappedMatcher.reset();
    firstFound = false;
    firstExit = false;
    return this;
  }

  @Override
  public void notifyZipEntryCompleted(boolean success, IPerEntryListener perEntryListener) {
    wrappedMatcher.notifyZipEntryCompleted(success, perEntryListener);
  }

}
