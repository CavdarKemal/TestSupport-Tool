package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import org.codehaus.staxmate.in.SMEvent;

import javax.xml.stream.XMLStreamException;

public interface ISMInputCursor
extends XmlSearchCursor {

  int getCurrEventCode();

  SMEvent getNext() throws XMLStreamException;

  ISMInputCursor childElementCursor(String currentParent) throws XMLStreamException;

  void exitElement();

}
