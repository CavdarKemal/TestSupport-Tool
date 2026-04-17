package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import javax.xml.stream.XMLStreamException;
import java.util.List;

/**
 * Schnittstelle für die Angaben zur aktuellen Position im XML
 */
public interface XmlSearchCursor {

   /**
    * Lese die Bezeichnung des aktuellen Elementes im XML
    */
   String getLocalName() throws XMLStreamException;

   /**
    * Lese die Bezeichnung des Elementes im XML, welches gerade verlassen wird
    */
   String getOnExitName() throws XMLStreamException;

   /**
    * Lese den Pfad, der beim Verlassen eines Elementes für den Vergleich verwendet werden kann
    */
   List<String> getOnExitPath();

   /**
    * Lese den Wert des aktuellen Elementes im XML, nullable
    */
   String getElemStringValue() throws XMLStreamException;

   /**
    * Lese den Wert des angegebenen Attributes, wiederum bezogen auf das aktuelle Element im XML
    */
   String getAttrValue(String attrName) throws XMLStreamException;

   /**
    * Lese den Pfad zwischen beginnend mit der Wurzel des XML-Dokumentes bis zum aktuellen Element
    */
   List<String> getPathsList();

}
