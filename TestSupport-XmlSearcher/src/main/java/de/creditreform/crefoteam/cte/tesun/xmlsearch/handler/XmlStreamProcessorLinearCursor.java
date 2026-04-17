package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.XmlSearchCursor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.*;

/**
 * Cursor-Objekt für die XML-Verarbeitung durch {@link XmlStreamProcessorLinear}
 */
class XmlStreamProcessorLinearCursor
implements XmlSearchCursor {
   // Der Platzhalter für 'kein local name' ist hier zentral definiert. Aktuell nicht null, sondern ein leerer String
   public static final String LOCAL_NAME_EMPTY = "";
   public static final Map<String,String> ATTR_MAP_EMPTY = Collections.emptyMap();

   private String localName=LOCAL_NAME_EMPTY;
   private String onExitName =LOCAL_NAME_EMPTY;
   private final List<String> positionTrace = new ArrayList<>();
   private final List<String> publicPath = Collections.unmodifiableList(positionTrace);
   private List<String> publicPathOhneLocalName = null;
   private String elemStringValue;
   private Map<String,String> attrMap=ATTR_MAP_EMPTY;

   public XmlStreamProcessorLinearCursor() {
      // intentionally empty
   }

   public void enterElement(XMLStreamReader xmlStreamReader)
   throws XMLStreamException {
      onExitName = LOCAL_NAME_EMPTY;
      localName = xmlStreamReader.getLocalName();
      positionTrace.add(localName);
      publicPathOhneLocalName=null;
      elemStringValue = null;
      attrMap = collectAttributes(xmlStreamReader);
   }

   public void elementContent(XMLStreamReader xmlStreamReader) {
      // localName bleibt unverändert
      // positionTrace bleibt unverändert
      this.elemStringValue = xmlStreamReader.getText();
   }

   public void exitElement(XMLStreamReader xmlStreamReader) {
      onExitName = xmlStreamReader.getLocalName();
      localName = LOCAL_NAME_EMPTY;
      positionTrace.remove(positionTrace.size()-1);
      publicPathOhneLocalName=null;
      elemStringValue = null;
      attrMap = ATTR_MAP_EMPTY;
   }

   public boolean hasContent() {
      return !attrMap.isEmpty() || elemStringValue!=null;
   }

   public int getAttributeCount() {
      return attrMap.size();
   }

   @Override
   public String getLocalName()
   throws XMLStreamException {
      return localName;
   }

   @Override
   public String getOnExitName()
   throws XMLStreamException {
      return onExitName;
   }

   @Override
   public List<String> getOnExitPath() {
      return publicPath;
   }

   @Override
   public String getElemStringValue()
   throws XMLStreamException {
      return elemStringValue;
   }

   protected Map<String,String> collectAttributes(XMLStreamReader xmlStreamReader) throws XMLStreamException
   {
      Map<String,String> newAttrMap = new TreeMap<>();
      final int anzAttr = xmlStreamReader.getAttributeCount();
      for (int i = 0; i < anzAttr; i++) {
         final String attrName = xmlStreamReader.getAttributeLocalName(i);
         final String attrValue = xmlStreamReader.getAttributeValue(i);
         newAttrMap.put(attrName, attrValue);
      }
      return newAttrMap;
   }

   @Override
   public String getAttrValue(String attrName)
   throws XMLStreamException {
      return attrMap.get(attrName);
   }

   @Override
   public List<String> getPathsList() {
      if (publicPathOhneLocalName==null) {
         if (publicPath.isEmpty()) {
            publicPathOhneLocalName = Collections.emptyList();
         }
         else {
            publicPathOhneLocalName = publicPath.subList(0, publicPath.size()-1);
         }
      }
      return publicPathOhneLocalName;
   }

}
