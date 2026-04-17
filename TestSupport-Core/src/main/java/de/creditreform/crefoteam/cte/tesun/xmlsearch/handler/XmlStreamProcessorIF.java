package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ProgressListenerIF;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;

/**
 * Schnittstelle für die Verarbeitung von XML-Daten aus einem {@link InputStream}
 */
public interface XmlStreamProcessorIF {

   /**
    * verarbeite die XML-Daten, der workerListener erhält Methoden-Aufrufe für verschiedene Zwischenschritte
    * der Verarbeitung
    * @param inputStream {@link InputStream} mit den zu verarbeitenden Daten
    * @param perEntryListener Listener, der die Ergebnisse zu einem Zip-Entry sammeln soll
    * @param progressListener {@link ProgressListenerIF} wird über die Fortschritte informiert
    * @return Rückgabewert der Methode 'isSatisfied' des intern gespeicherten XmlMatcher
    */
   boolean handleForListeners(InputStream inputStream, IPerEntryListener perEntryListener, ProgressListenerIF progressListener) throws XMLStreamException;

}
