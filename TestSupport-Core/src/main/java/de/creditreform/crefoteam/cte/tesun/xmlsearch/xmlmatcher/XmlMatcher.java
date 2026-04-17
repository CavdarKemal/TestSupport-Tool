package de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher;

import javax.xml.stream.XMLStreamException;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.XmlSearchCursor;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;

public interface XmlMatcher
{
  /**
   * Prüfe, ob für die aktuelle Position / den aktuellen Cursor ein Treffer gefunden wurde
   * @param childCursor Informationen über die aktuelle Position innerhalb eines XML
   * @param perEntryListener Listener für gefundene Treffer
   */
  XmlMatcher matchCursor(XmlSearchCursor childCursor, IPerEntryListener perEntryListener) throws XMLStreamException;

  void notifyExitElement(XmlSearchCursor childCursor, IPerEntryListener perEntryListener) throws XMLStreamException;

  /**
   * Prüfe, ob die Gesamtheit aller Kriterien erfüllt wurde
   */
  boolean isSatisfied();

  /**
   * Callback für den Start der Analyse eines Datensatzen
   */
  XmlMatcher reset();

  /**
   * Callback für das Ende der Analyse eines Datensatzen
   * @param success true, wenn die Verarbeitung ohne Exception durchgeführt wurde
   * @param perEntryListener Listener für gefundene Treffer
   */
  void notifyZipEntryCompleted(boolean success, IPerEntryListener perEntryListener);

}
