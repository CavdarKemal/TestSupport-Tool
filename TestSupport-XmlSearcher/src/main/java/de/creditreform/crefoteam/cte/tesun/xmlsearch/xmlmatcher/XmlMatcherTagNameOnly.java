package de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.XmlSearchCursor;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;

import javax.xml.stream.XMLStreamException;
import java.util.*;

public class XmlMatcherTagNameOnly extends XmlMatcherAbstract
{
  private final List<String> allTagNames;
  private boolean satisfied;

  public XmlMatcherTagNameOnly( MatcherParameterTag matcherParameterTag, String... alternateTagNames )
  {
    this(matcherParameterTag, Arrays.asList(alternateTagNames));
  }

  public XmlMatcherTagNameOnly( MatcherParameterTag matcherParameterTag, Collection<String> alternateTagNames )
  {
    super( matcherParameterTag );
    final ArrayList<String> newTagList;
    if (alternateTagNames==null || alternateTagNames.isEmpty()) {
        newTagList = new ArrayList<>(1);
    }
    else {
        newTagList = new ArrayList<>(1+alternateTagNames.size());
        for (String ap : alternateTagNames) {
            if (ap != null) {
                newTagList.add(ap);
            }
        }
    }
    final String tagFromPath = super.getXmlTagName();
    if (tagFromPath!=null) {
        newTagList.add(0, tagFromPath);
    }
    this.allTagNames = Collections.unmodifiableList(newTagList);
  }

    public List<String> getAllTagNames() {
        return allTagNames;
    }

  /*************************************************************************************************/
  /********************************       XmlMatcherAbstract       *********************************/
  /*************************************************************************************************/
  @Override public XmlMatcherTagNameOnly matchCursor(XmlSearchCursor elementCursor, IPerEntryListener perEntryListener) throws XMLStreamException
  {
    String localName = elementCursor.getLocalName();
    logger.debug( "Prüfe Element: {} für  {}", localName, getXmlTagName() );
    if( allTagNames.contains(localName) )
    {
      satisfied |= onMatchFound(elementCursor, perEntryListener);
    }
    return this;
  }

  @Override
  public void notifyExitElement(XmlSearchCursor childCursor, IPerEntryListener perEntryListener) throws XMLStreamException {
    // intentionally empty
  }

    /**
     * Hook für abgeleitete Klassen. Diese Methode wird aufgerufen, sobald
     * die Suchkriterien getroffen haben
     * @param elementCursor Cursor-Position mit dem Treffer
     * @throws XMLStreamException
     * @return true, wenn die Suche als 'satisfied' betrachtet werden soll
     */
  protected boolean onMatchFound(XmlSearchCursor elementCursor, IPerEntryListener perEntryListener)
  throws XMLStreamException {
      perEntryListener.addMatchingCriterion( "Treffer bei LocalName "+ elementCursor.getLocalName() );
      return true;
  }

  @Override public boolean isSatisfied()
  {
    return satisfied;
  }

  @Override public XmlMatcherTagNameOnly reset()
  {
    satisfied = false;
    return this;
  }

  @Override
  public void notifyZipEntryCompleted(boolean success, IPerEntryListener perEntryListener) {
    // intentionally empty
  }

}
