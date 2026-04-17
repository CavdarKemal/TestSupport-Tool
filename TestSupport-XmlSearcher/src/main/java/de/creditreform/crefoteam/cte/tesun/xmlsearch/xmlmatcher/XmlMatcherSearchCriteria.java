package de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.XmlSearchCursor;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;

import javax.xml.stream.XMLStreamException;
import java.util.regex.Pattern;

public class XmlMatcherSearchCriteria extends XmlMatcherAbstract
{
  /**
   * Trennzeichen zwischen Attribut-Name und gesuchtem Inhalt ist entweder der Doppelpunkt oder das Gleichheitszeichen
   */
  protected static final Pattern attrNameValuePattern = Pattern.compile("[=:]");

  @FunctionalInterface
  protected interface InternalMatcher {
    boolean isMatch(XmlSearchCursor elementCursor, IPerEntryListener perEntryListener) throws XMLStreamException;
  }

  /**
   * innere Utility-Klasse für den Vergleich mit einem Element-Wert
   */
  protected static class InternalMatcherValue
  implements InternalMatcher {
    private final String xmlTagName;
    private final Pattern elementValuePattern;

    public InternalMatcherValue(String xmlTagName, String xmlTagValueToWatch) {
      this.xmlTagName = xmlTagName;
      this.elementValuePattern = Pattern.compile(xmlTagValueToWatch);
    }

    @Override
    public boolean isMatch(XmlSearchCursor elementCursor, IPerEntryListener perEntryListener)
    throws XMLStreamException{
      String tagValue = elementCursor.getElemStringValue();
      if( tagValue != null && elementValuePattern.matcher(tagValue).matches() )
      {
        String strInfo = String.format("Treffer beim Element: %s = %s", xmlTagName, tagValue);
        logger.info( "\t\t\t*** " + strInfo);
        perEntryListener.addMatchingCriterion(strInfo);
        return true;
      }
      return false;
    }
  }

  /**
   * innere Utility-Klasse für den Vergleich mit dem Attribut eines Elementes
   */
  protected static class InternalMatcherAttribute
  implements InternalMatcher {
    private final String xmlTagName;
    private final String attrName;
    private final Pattern attributeValuePattern;

    public InternalMatcherAttribute(String xmlTagName, String attrName, String expectedAttrValue) {
      this.xmlTagName = xmlTagName;
      this.attrName = attrName;
      this.attributeValuePattern = Pattern.compile(expectedAttrValue);
    }

    @Override
    public boolean isMatch(XmlSearchCursor elementCursor, IPerEntryListener perEntryListener)
    throws XMLStreamException{
      String attrValue = elementCursor.getAttrValue( attrName );
      if( attrValue!=null && attributeValuePattern.matcher(attrValue).matches() )
      {
        String strInfo = String.format("Treffer beim Element-Attribut: %s.%s = %s", xmlTagName, attrName, attrValue);
        logger.info(  "\t\t\t*** " + strInfo);
        perEntryListener.addMatchingCriterion(strInfo);
        return true;
      }
      return false;
    }

  }

  protected static InternalMatcher createInternalMatcher(String xmlTagName, String xmlTagValueToWatch) {
    // Wir suchen nur das erste Trennzeichen, danach können alle Zeichen für ein Pattern genutzt werden
    String valueOhneProlog = xmlTagValueToWatch;
    String [] attrPair = attrNameValuePattern.split( xmlTagValueToWatch,2 );
    if( attrPair.length > 1 ) {
      // Falls wir auf die Suche nach Element-Inhalt zurück fallen, muss der Prolog entfernt werden.
      valueOhneProlog = attrPair[1];
      if (attrPair[0]!=null && attrPair[0].trim().length()>0) {
        // Attribut-Suche nur mit gefülltem Attibut-Name
        return new InternalMatcherAttribute(xmlTagName, attrPair[0], valueOhneProlog);
      }
    }
    return new InternalMatcherValue(xmlTagName, valueOhneProlog);
  }

  private final InternalMatcher internalMatcher;
  private boolean               satisfied;

  public XmlMatcherSearchCriteria( MatcherParameterTag matcherParameterTag, String xmlTagValueToWatch )
  {
    super( matcherParameterTag );
    this.internalMatcher = createInternalMatcher(matcherParameterTag.getXmlTagName(), xmlTagValueToWatch);
  }

  protected boolean isMatchInternal(XmlSearchCursor elementCursor, IPerEntryListener matchListener ) throws XMLStreamException
  {
    String localName = elementCursor.getLocalName();
    logger.debug( "Prüfe Element: {} für  {}" , localName , getXmlTagName() );
    if( localName.equals( getXmlTagName() ) )
    {
      return internalMatcher.isMatch(elementCursor, matchListener);
    }
    return false;
  }

  /*********************************************************************************************/
  /*************************************      XmlMatcher   *************************************/
  /*********************************************************************************************/
  @Override public XmlMatcherSearchCriteria matchCursor(XmlSearchCursor elementCursor, IPerEntryListener perEntryListener) throws XMLStreamException
  {
    boolean matchHere = isMatchInternal(elementCursor, perEntryListener);
    if (matchHere) {
      satisfied = true;
    }
    return this;
  }

  @Override
  public void notifyExitElement(XmlSearchCursor childCursor, IPerEntryListener perEntryListener) throws XMLStreamException {
    // intentionally empty
  }

  @Override public boolean isSatisfied()
  {
    return satisfied;
  }

  @Override public XmlMatcherSearchCriteria reset()
  {
    satisfied = false;
    return this;
  }

  @Override
  public void notifyZipEntryCompleted(boolean success, IPerEntryListener perEntryListener) {
    // intentionally empty
  }

}
