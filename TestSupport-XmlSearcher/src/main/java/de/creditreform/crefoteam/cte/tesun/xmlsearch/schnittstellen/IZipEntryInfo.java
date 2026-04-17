package de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen;

import java.util.List;

public interface IZipEntryInfo {
   /**
    * Lese das Parent-Objekt zu diesem Zip-Eintrag
    */
   IZipFileInfo getParent();

   List<String> getMatchesList();

   String getZipEntryName();

   /**
    * Lese den vollständigen Pfad der Datei, in der die Ergebnisse gespeichert werden
    */
   String getResultFileName();

   void addMatches( List<String> matchesList );

   /**
    * Setze den Pfad der Datei, in der die Ergebnisse gespeichert werden
    * @throws IllegalStateException wenn der Pfad bereits gesetzt ist
    */
   void setResultFileName(String resultFileName) throws IllegalStateException;

   String getCrefonummer();

   String getZipFileName();

}
