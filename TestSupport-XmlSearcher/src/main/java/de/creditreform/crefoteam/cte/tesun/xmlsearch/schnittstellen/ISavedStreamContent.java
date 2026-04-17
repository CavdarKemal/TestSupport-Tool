package de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen;

import java.io.IOException;

/**
 * Schnittstelle für das Abfragen des gepufferten Stream-Inhaltes. Hintergrund ist, dass die Daten
 * aus einem Zip-InputStream nur einmalig gelesen werden können. Um sie nach Feststellung eines Treffers
 * erneut verwenden zu können, werden die Daten gepuffert und über dieses Interface zur Verfügung gestellt.
 */
public interface ISavedStreamContent {
   /**
    * Lese die gepufferten Inhalte als XML-String. Bei Bedarf kann ein Pretty-Printing aktiviert werden.
    * @param formatXML true, um das Pretty-Printing zu aktivieren
    */
   String getSavedContentAsString(boolean formatXML) throws Exception;

   /**
    * Lese die gepufferten Daten als unverändertes Byte-Array
    */
   byte [] getSavedContent() throws IOException;

}
