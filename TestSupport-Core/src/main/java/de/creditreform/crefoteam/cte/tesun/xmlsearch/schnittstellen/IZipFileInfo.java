package de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen;

import java.util.List;

/**
 * Schnittstelle für die Beschreibung einer Zip-Datei
 */
public interface IZipFileInfo {

   IZipSearcResult getParent();

   /**
    * Lese den Namen der Zip-Datei
    */
   String getZipFileName();

   /**
    * Lese den Pfad der Zip-Datei
    */
   String getZipFilePath();

   String getSearchName();

   /**
    * Füge der internen Liste von Zip-Entries einen neuen Eintrag hinzu
    */
   void addZipEntryInfo(IZipEntryInfo zipEntryInfo);

   /**
    * Lese die Liste der enthaltenen Zip-Einträge
    */
   List<? extends IZipEntryInfo> getZipEntryInfoList();

}
