package de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen;

import java.io.Closeable;

/**
 * Schnittstelle für die Benachrichtigung über erzielte Treffer in Kombination mit den Inhalten und einer
 * Angabe zum verarbeiteten Zip-Eintrag. Im Gegensatz zum {@link IPerEntryListener} werden hier Angaben
 * zum tatsächlichen Inhalt sowie zum verarbeiteten Zip-Eintrag übergeben.
 */
public interface IMatchInfoListener extends Closeable
{
  /**
   * true, wenn die Instanz von mehreren Threads gleichzeitig genutzt werden darf
   */
  boolean isThreadSafe();

  /**
   * Protokolliere einen Treffer mit den angegebenen Daten. Der Aufruf erfolgt gegebenenfalls für jeden
   * einzelnen der untersuchten Zip-Entries.
   * @param savedStreamContent Daten, die beim Lesen des InputStream zwischengespeichert wurden
   * @param zipEntryInfo Angaben zum Eintrag in der Zip-Datei
   * @param xmlMatchStatistics Detail-Informationen zu den erzielten Treffern, nullable
   */
  void notifyEntryMatched(ISavedStreamContent savedStreamContent, IZipEntryInfo zipEntryInfo, IPerEntryStatistics xmlMatchStatistics) throws Exception;

  /**
   * Protokolliere einen Zip-Eintrag, für den kein Treffer erzielt wurde. Der Aufruf erfolgt gegebenenfalls für jeden
   * einzelnen der untersuchten Zip-Entries.
   * @param savedStreamContent Daten, die beim Lesen des InputStream zwischengespeichert wurden
   * @param zipEntryInfo Angaben zum Eintrag in der Zip-Datei
   */
  void notifyEntryNotMatched(ISavedStreamContent savedStreamContent, IZipEntryInfo zipEntryInfo) throws Exception;

}
