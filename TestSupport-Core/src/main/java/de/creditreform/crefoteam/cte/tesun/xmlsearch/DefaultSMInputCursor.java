package de.creditreform.crefoteam.cte.tesun.xmlsearch;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ISMInputCursor;
import org.codehaus.staxmate.in.SMEvent;
import org.codehaus.staxmate.in.SMInputCursor;

import javax.xml.stream.XMLStreamException;
import java.util.*;

public class DefaultSMInputCursor implements ISMInputCursor
{
  private final SMInputCursor childCursor;
  private final List<String> pathsList;
  private String              elementText;
  private String              localName;
  private String onExitName;
  private Map<String, String> attrMap;

  public DefaultSMInputCursor( SMInputCursor childCursor )
  {
    this(childCursor, Collections.emptyList(), null);
  }

  private DefaultSMInputCursor(SMInputCursor childCursor, List<String> pathsList, String currentParent )
  {
    this.childCursor = childCursor;
    List<String> newPath = new ArrayList<>( pathsList.size() + 1 );
    newPath.addAll(pathsList);
    if (currentParent!=null) {
      newPath.add(currentParent);
    }
    this.pathsList = Collections.unmodifiableList( newPath );
  }

  protected final void reset()
  {
    elementText = null;
    localName = null;
    onExitName = null;
    attrMap = null;
  }

  protected final void collectAttributes() throws XMLStreamException
  {
    if( attrMap == null )
    {
      attrMap = new TreeMap<>();
      final int anzAttr = childCursor.getAttrCount();
      for( int i = 0; i < anzAttr; i++ )
      {
        final String attrName = childCursor.getAttrLocalName( i );
        final String attrValue = childCursor.getAttrValue( i );
        attrMap.put( attrName, attrValue );
      }
    }
  }

  @Override public SMEvent getNext() throws XMLStreamException
  {
    reset();
    return childCursor.getNext();
  }

  @Override public int getCurrEventCode()
  {
    return childCursor.getCurrEventCode();
  }

  @Override public final String getLocalName() throws XMLStreamException
  {
    if( localName == null )
    {
      localName = childCursor.getLocalName();
    }
    return localName;
  }

  @Override
  public String getOnExitName() throws XMLStreamException {
    return onExitName;
  }

  @Override public String getAttrValue(String localName ) throws XMLStreamException
  {
    collectAttributes();
    return attrMap.get( localName );
  }

  @Override public String getElemStringValue() throws XMLStreamException
  {
    if( elementText == null )
    {
      getLocalName();
      collectAttributes();
      try
      {
        elementText = childCursor.getElemStringValue();
      }
      catch( XMLStreamException e )
      {
        elementText = "";
      }
    }
    return elementText;
  }

  @Override public final ISMInputCursor childElementCursor( String currentParent ) throws XMLStreamException
  {
    SMInputCursor childElementCursor = childCursor.childElementCursor();
    ISMInputCursor childCursor = new DefaultSMInputCursor( childElementCursor, this.pathsList, currentParent );
    return childCursor;
  }

  @Override
  public void exitElement() {
    if (pathsList.isEmpty()) {
      this.onExitName = null;
    }
    else {
      this.onExitName = pathsList.get(pathsList.size()-1);
    }
    this.localName = null;
  }

  @Override public List<String> getPathsList()
  {
    return pathsList;
  }

  @Override
  public List<String> getOnExitPath() {
    if (pathsList.isEmpty()) {
      return pathsList;
    }
    else {
      return pathsList.subList(0, pathsList.size()-1);
    }
  }

}
