package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.ISavedStreamContent;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryStatistics;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipEntryInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvAbgleichMatchInfoListener
extends AbstractMatchInfoListener {

   protected static Charset ioCharset = Charset.forName("UTF-8");
   protected static Pattern csvSeparators = Pattern.compile("[,;#]");

   protected enum CsvAbgleichErgebnis {
      GEFUNDEN(true, true /*inQuelldatenEnthalten*/),
      NICHT_GEFUNDEN(false, false /*inQuelldatenEnthalten*/),
      GEFUNDEN_KEIN_TREFFER(false, true /*inQuelldatenEnthalten*/),
      TREFFER_NICHT_IM_CSV(false, true /*inQuelldatenEnthalten*/)

      ;
      final boolean trefferZuCsvEintrag;
      final boolean inQuelldatenEnthalten;

      CsvAbgleichErgebnis(boolean trefferZuCsvEintrag, boolean inQuelldatenEnthalten) {
         this.trefferZuCsvEintrag = trefferZuCsvEintrag;
         this.inQuelldatenEnthalten = inQuelldatenEnthalten;
      }

      public boolean isTrefferZuCsvEintrag() {
         return trefferZuCsvEintrag;
      }

      public boolean isInQuelldatenEnthalten() {
         return inQuelldatenEnthalten;
      }

   }

   /**
    * {@link BiFunction} für das Update bestehender Einträge in der {@link ConcurrentHashMap}
    */
   protected static class MapUpdateFunction
   implements BiFunction<String, CsvAbgleichErgebnis, CsvAbgleichErgebnis> {
      private final CsvAbgleichErgebnis neuesErgebnis;

      public MapUpdateFunction(CsvAbgleichErgebnis neuesErgebnis) {
         this.neuesErgebnis = neuesErgebnis;
      }

      @Override
      public CsvAbgleichErgebnis apply(String s, CsvAbgleichErgebnis mapValue) {
         if (mapValue==null) {
            return null; // Key in der Map aktuell nicht enthalten
         }
         else {
            return neuesErgebnis;
         }
      }
   }

   private final Logger logger;
   private final TestSupportClientKonstanten.SEARCH_RESULT_TYPE searchResultType;
   private final File csvFile;
   private final ConcurrentHashMap<String, CsvAbgleichErgebnis> abgleichErgebnisMap;
   private final ConcurrentHashMap<String, CsvAbgleichErgebnis> nichtImCsv;
   private final File resultsBaseDir;
   private final MapUpdateFunction mapUpdateFunctionFound;
   private final MapUpdateFunction mapUpdateFunctionNotFound;
   private Boolean activeFlag; // null bedeutet 'Initialisierung noch nicht erfolgt'

   /**
    * Standard-Konstruktor
    * @param sourcePath
    * @param csvFileName Name (Pfad) der CSV-Datei für einen Abgleich mit den Suchtreffern
    * @param searchResultType Typ der Suche
    * @param searchResultsPath Verzeichnis für alle Suchergebnisse
    * @param searchConfigurationName Name der Such-Konfiguration
    */
   public CsvAbgleichMatchInfoListener(String sourcePath, String csvFileName,
                                       TestSupportClientKonstanten.SEARCH_RESULT_TYPE searchResultType,
                                       String searchResultsPath, String searchConfigurationName) {
      super(null);
      this.searchResultType = searchResultType;
      this.logger = LoggerFactory.getLogger( getClass() );
      File sourcePathFile = childOf(null, sourcePath);
      this.csvFile = childOf(sourcePathFile, csvFileName);
      this.abgleichErgebnisMap = new ConcurrentHashMap<>();
      this.nichtImCsv = new ConcurrentHashMap<>();
      this.resultsBaseDir = getBaseDir(searchResultsPath, searchConfigurationName);
      this.mapUpdateFunctionFound = new MapUpdateFunction(CsvAbgleichErgebnis.GEFUNDEN);
      this.mapUpdateFunctionNotFound = new MapUpdateFunction(CsvAbgleichErgebnis.GEFUNDEN_KEIN_TREFFER);
   }

   /**
    * true, wenn der Name der CSV-Datei einen gültigen Pfad ergibt und der Inhalt lesbar ist
    */
   public boolean isActive() {
      if (activeFlag==null) {
         activeFlag = initCheckFile();
         logger.info("CsvAbgleichMatchInfoListener aktiv: {}", activeFlag);
      }
      return activeFlag;
   }

   protected int getAnzahlCrefosZumAbgleich() {
      return abgleichErgebnisMap.size();
   }

   /**
    * Prüfe die vorgegebene CSV-Datei und lade die Daten. Der Rückgabewert ist 'true', wenn dies erfolgreich war.
    */
   protected boolean initCheckFile() {
      // frühere Zwischenergebnisse können jetzt gelöscht werden, auch wenn die CSV-Datei nicht existiert
      abgleichErgebnisMap.clear();
      nichtImCsv.clear();
      if (csvFile!=null) {
         if (csvFile.isDirectory() || !csvFile.canRead()) {
            logger.warn("CSV-Datei zum Abgleich mit den Suchtreffern kann nicht gelesen werden: {}", csvFile.getAbsolutePath());
         }
         else {
            logger.info("Lade Crefonummern zum Abgleich mit den Suchtreffern aus der Datei: {}", csvFile.getAbsolutePath());
            try (InputStream inputStream = new BufferedInputStream(new FileInputStream(csvFile))) {
               // Lese die einzelnen (!) Zeilen aus der Datei
               LineIterator lineIterator = IOUtils.lineIterator(inputStream, ioCharset);
               while (lineIterator.hasNext()) {
                  String trimmed = csvFirstColumn(lineIterator.next());
                  if (trimmed!=null) {
                     abgleichErgebnisMap.put(trimmed, CsvAbgleichErgebnis.NICHT_GEFUNDEN);
                  }
               }
               logger.info("CSV-Datei zum Abgleich geladen, {} Einträge gefunden", abgleichErgebnisMap.size());
               return !abgleichErgebnisMap.isEmpty();
            } catch (FileNotFoundException e) {
               // es ist normal, wenn die Datei fehlt, daher nur Info-Level
               logger.info("Datei für den Abgleich nicht gefunden: {}", csvFile, e);
            } catch (IOException e) {
               // wenn die Datei vorhanden, aber nicht lesbar ist, soll eine Warnung ausgegeben werden
               logger.warn("Fehler beim Laden der Datei für den Abgleich ({})", csvFile, e);
            }
         }
      }
      return false;
   }

   protected String csvFirstColumn(String zeile) {
      if (zeile==null) {
         return null;
      }
      else {
         // Leerzeichen entfernen, #1
         String sanitized = zeile.trim();
         if (sanitized.length()==0 || sanitized.startsWith(";") || sanitized.startsWith("#")) {
            return null;
         }
         else {
            // Nur die erste Spalte...
            Matcher matcher = csvSeparators.matcher(zeile);
            if (matcher.find()) {
               int columnSeparatorAt = matcher.start();
               sanitized = sanitized.substring(0, columnSeparatorAt);
            }
            // Leerzeichen entfernen, #2
            sanitized = sanitized.trim();
            // doppelte Anführungszeichen entfernen
            if (sanitized.startsWith("\"") && sanitized.endsWith("\"")) {
               sanitized = sanitized.substring(1, sanitized.length() - 1).trim();
            }
            // einfache Anführungszeichen entfernen
            if (sanitized.startsWith("'") && sanitized.endsWith("'")) {
               sanitized = sanitized.substring(1, sanitized.length() - 1).trim();
            }
            // Leerzeichen entfernen, #3
            return (sanitized.length()==0) ? null : sanitized;
         }
      }
   }

   @Override
   public boolean isThreadSafe() {
      return true;
   }

   @Override
   public void notifyEntryMatched(ISavedStreamContent savedStreamContent, IZipEntryInfo zipEntryInfo, IPerEntryStatistics xmlMatchStatistics) {
      // Alle Ergebnisse werden mit Bezug auf die Crefonummer ermittelt. Eine vorherige Umwandlung in einen numerischen
      // Wert ist nicht sinnvoll.
      String crfAsString = zipEntryInfo.getCrefonummer();
      if (crfAsString!=null) {
         CsvAbgleichErgebnis newValue = abgleichErgebnisMap.computeIfPresent(crfAsString, mapUpdateFunctionFound);
         if (newValue==null) {
            // Crefo nicht in der CSV-Datei
            nichtImCsv.put(crfAsString, CsvAbgleichErgebnis.TREFFER_NICHT_IM_CSV);
         }
      }
   }

   @Override
   public void notifyEntryNotMatched(ISavedStreamContent savedStreamContent, IZipEntryInfo zipEntryInfo) throws Exception {
      String crfAsString = zipEntryInfo.getCrefonummer();
      if (crfAsString!=null) {
         abgleichErgebnisMap.computeIfPresent(crfAsString, mapUpdateFunctionNotFound);
      }
   }

   @Override
   public void close() throws IOException {
      // Ausgabe-Dateien werden nur dann geschrieben, wenn die Initialisierung erfolgreich war
      if (Boolean.TRUE.equals(activeFlag)) {
         // wir benötigen 4 Dateien für die Ausgabe...
         ResultOutputUtil utl = new ResultOutputUtil(logger, resultsBaseDir,  searchResultType.name());
         final boolean doNotAppend=false;
         try (OutputStream outIsMatch = utl.createOutputStream("abgleich-matched.csv", doNotAppend );
              OutputStream outNoMatchFound = utl.createOutputStream("abgleich-not-matched-exported.csv", doNotAppend );
              OutputStream outNoMatchNotFound = utl.createOutputStream("abgleich-not-matched-missing.csv", doNotAppend );
              OutputStream outNotInCsv = utl.createOutputStream("abgleich-not-in-csv.csv", doNotAppend )
         ) {
            // Vorbereitung der Writer-Instanzen
            List<Writer> writerList = new ArrayList<>();
            Writer wrIsMatch = utl.prepareWriter(writerList, outIsMatch, "; === Liste der Such-Treffer, die auch in der CSV-Datei zum Abgleich enthalten sind ===\n");
            Writer wrNoMatchFound = utl.prepareWriter(writerList, outNoMatchFound, "; === Zu folgenden Zeilen in der CSV-Datei zum Abgleich gibt es keinen Such-Treffer, aber einen passenden Datensatz: ===\n");
            Writer wrNoMatchNotFound = utl.prepareWriter(writerList, outNoMatchNotFound, "; === Zu folgenden Zeilen in der CSV-Datei zum Abgleich gibt es keinen Such-Treffer und keinen passenden Datensatz: ===\n");
            Writer wrNotInCsv = utl.prepareWriter(writerList, outNotInCsv, "; === Liste der Such-Treffer, die nicht in der CSV-Datei zum Abgleich genannt sind ===");

            // Schreibe die Daten zu den Angaben in der CSV-Datei
            int anzMatching=0;
            int anzNotMatchingFound=0;
            int anzNotMatchingNotFound=0;
            // Ergebnisse werden zunächst sortiert...
            Map<String,CsvAbgleichErgebnis> abgleichErgebnisSorted = new TreeMap<>(this.abgleichErgebnisMap);
            for (Map.Entry<String, CsvAbgleichErgebnis> e : abgleichErgebnisSorted.entrySet()) {
               String infoZeile = formatResult(e.getKey());
               if (e.getValue().isTrefferZuCsvEintrag()) {
                  anzMatching++;
                  wrIsMatch.write(infoZeile);
               }
               else if (e.getValue().isInQuelldatenEnthalten()){
                  anzNotMatchingFound++;
                  wrNoMatchFound.write(infoZeile);
               }
               else {
                  anzNotMatchingNotFound++;
                  wrNoMatchNotFound.write(infoZeile);
               }
            }
            // Schreibe die Treffer, die nicht in der CSV-Datei auftauchen
            Set<String> nichtImCsvSorted = new TreeSet<>(nichtImCsv.keySet());
            for (String crf : nichtImCsvSorted) {
               wrNotInCsv.write(formatResult(crf));
            }
            logger.info("=== Ergebnisse CsvAbgleichMatchInfoListener ===");
            logger.info("==> CSV-Datei zum Abgleich enthält {} Zeilen", abgleichErgebnisMap.size());
            logger.info("==> bei {} Zeilen aus der CSV-Datei gab es einen Treffer", anzMatching);
            logger.info("==> zu {} Zeilen aus der CSV-Datei wurde kein Treffer, aber ein passender Datensatz gefunden", anzNotMatchingFound);
            logger.info("==> zu {} Zeilen aus der CSV-Datei konnte kein Treffer und kein passender Datensatz gefunden werden", anzNotMatchingNotFound);
            logger.info("==> daneben gab es {} Treffer, die nicht in der CSV-Datei genannt waren", nichtImCsv.size());
            utl.flushWriters(writerList);
         } // try with resources
      }
   }

   private String formatResult(String result) {
      return result+'\n';
   }


}
