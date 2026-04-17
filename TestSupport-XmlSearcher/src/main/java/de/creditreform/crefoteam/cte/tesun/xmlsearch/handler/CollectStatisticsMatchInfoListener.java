package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IGroupByRow;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.ISavedStreamContent;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryStatistics;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipEntryInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher.XmlMatcherAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ableitung von {@link AbstractMatchInfoListener} für das Sammeln statistischer Auswertungen
 */
public class CollectStatisticsMatchInfoListener
extends AbstractMatchInfoListener {

   protected static final char SEPARATOR_COLUMNS = ';';
   protected static final char SEPARATOR_LINES = '\n';
   protected static final String FILE_NAME_RAW_STATS = "collected-statistcs-raw.csv";
   protected static final String FILE_NAME_COUNTRY_STATS = "collected-statistcs-country.csv";

   protected static final Pattern CRF_PATTERN = Pattern.compile("\\d{10}");
   protected static final String CN_VVC = "VVC";
   protected static final String CN_DE = "DE";
   protected static final String CN_AT = "AT";
   protected static final String CN_LU = "LU";
   protected static final String CN_OTHER = "SONSTIGE";

   protected static final List<List<String>> CLZ_MAPPINGS;

   static {
      List<List<String>> cm = new ArrayList<>();
      cm.add( Collections.unmodifiableList(Arrays.asList(CN_DE, "200", "835")) );
      cm.add( Collections.unmodifiableList(Arrays.asList(CN_AT, "900", "915")) );
      cm.add( Collections.unmodifiableList(Arrays.asList(CN_LU, "937", "937")) );
      cm.add( Collections.unmodifiableList(Arrays.asList(CN_VVC, "100", "100")) );
      CLZ_MAPPINGS = Collections.unmodifiableList(cm);
   }

   protected static class CreatePerFileMapFunction
   implements Function<String, Map<IGroupByRow, Integer>> {
      @Override
      public Map<IGroupByRow, Integer> apply(String s) {
         return new ConcurrentHashMap<>();
      }
   }

   private final Logger logger;
   private final TestSupportClientKonstanten.SEARCH_RESULT_TYPE searchResultType;
   private final File resultsBaseDir;

   private final Map<String, Map<IGroupByRow, Integer>> perFileResults;
   private final CreatePerFileMapFunction createMissingEntryFunction;

   public CollectStatisticsMatchInfoListener(TestSupportClientKonstanten.SEARCH_RESULT_TYPE searchResultType,
                                             String searchResultsPath, String searchConfigurationName) {
      super(null);
      this.logger = LoggerFactory.getLogger(getClass() );
      this.searchResultType = searchResultType;
      this.resultsBaseDir = getBaseDir(searchResultsPath, searchConfigurationName);

      this.perFileResults = new ConcurrentHashMap<>();
      createMissingEntryFunction = new CreatePerFileMapFunction();
   }

   @Override
   public boolean isThreadSafe() {
      return true;
   }

   @Override
   public void notifyEntryMatched(ISavedStreamContent savedStreamContent, IZipEntryInfo zipEntryInfo, IPerEntryStatistics xmlMatchStatistics) throws Exception {
      if (xmlMatchStatistics!=null) {
         Map<IGroupByRow, Integer> perEntryStats = xmlMatchStatistics.getZipEntryStatistics();
         if (perEntryStats!=null && !perEntryStats.isEmpty()) {
            // Für den Namen der Zip-Datei ermitteln wir die zugehörige Map, sie wird ggf. neu angelegt
            Map<IGroupByRow, Integer> perFileMap = perFileResults.computeIfAbsent(zipEntryInfo.getZipFileName(), createMissingEntryFunction);
            // Für das Update von 'perFileMap' verwenden wir wieder eine Function...
            XmlMatcherAbstract.CountValuesUpdateVariableFunction<IGroupByRow> statsUpdateFunction = new XmlMatcherAbstract.CountValuesUpdateVariableFunction<>();
            for (Map.Entry<IGroupByRow, Integer> e : perEntryStats.entrySet()) {
               statsUpdateFunction.setNonNegativeIncrement(e.getValue());
               perFileMap.compute(e.getKey(), statsUpdateFunction);
            }
         }
      }
   }

   @Override
   public void close() throws IOException {
      // Logging der gesammelten Statistiken
      ResultOutputUtil utl = new ResultOutputUtil(logger, resultsBaseDir,  searchResultType.name());
      try(OutputStream outRawStats = utl.createOutputStream(FILE_NAME_RAW_STATS, false);
          OutputStream outCountryStats = utl.createOutputStream(FILE_NAME_COUNTRY_STATS, false)) {
         // Vorbereitung der Writer-Instanzen
         List<Writer> writerList = new ArrayList<>();
         Writer wrRawStats = utl.prepareWriter(writerList, outRawStats, "; === Roh-Ergebnisse der statistischen Auswertung ===\n");
         Writer wrCountryStats = utl.prepareWriter(writerList, outCountryStats, "; === Ergebnisse der statistischen Auswertung gruppiert nach Land ===\n");
         // Sortierte Liste der Dateinamen...
         List<String> fileNames = new ArrayList<>(perFileResults.keySet());
         Collections.sort(fileNames);
         // Die für jeweils ein Land aggregierten Daten werden in 'mapPerCountry' vorbereitet
         Map<String, Map<IGroupByRow, Integer>> mapPerCountry = new TreeMap<>();
         // Erster Durchlauf und Ausgabe der Rohdaten sortiert nach Dateiname...
         for (String fn : fileNames) {
            // Sammle alle Zeilen für eine Datei in einer Liste...
            List<String> perFileOutputs = collectRawDataForFile(fn, mapPerCountry);
            // Schreibe die sortierte Liste
            for (String line : perFileOutputs) {
               wrRawStats.write(fn);
               wrRawStats.write(SEPARATOR_COLUMNS);
               wrRawStats.write(line);
            }
         } // Schleife über die sortierten Datei-Namen
         // Ausgabe der Daten nach Ländern...
         StringBuilder sb = new StringBuilder();
         List<String> linesAllCountries = new ArrayList<>();
         for (Map.Entry<String, Map<IGroupByRow, Integer>> proLand : mapPerCountry.entrySet()) {
            for (Map.Entry<IGroupByRow, Integer> r : proLand.getValue().entrySet()) {
               sb.append(proLand.getKey()).append(SEPARATOR_COLUMNS);
               linesAllCountries.add( createLine(sb, r) );
            }
         }
         Collections.sort(linesAllCountries);
         for (String l : linesAllCountries) {
            wrCountryStats.write(l);
         }
         utl.flushWriters(writerList);
      }
   }

   /**
    * erzeuge die (sortierte) Liste der auszugebenden Strings für den vorgegebenen Dateinamen, zusätzlich werden die Daten
    * nach Ländern gruppiert und in mapPerCountry gespeichert
    * @param fileName Dateiname
    * @param mapPerCountry Map für die nach Land gruppierten Ergebnisse
    * @return Liste der Strings mit den Rohdaten
    */
   protected List<String> collectRawDataForFile(String fileName, Map<String, Map<IGroupByRow, Integer>> mapPerCountry) {
      // Lese die Roh-Daten zum angegebenen Dateinamen
      Map<IGroupByRow, Integer> perFile = perFileResults.get(fileName);
      // in mapPerCountry wird eine neue Map angelegt...
      Map<IGroupByRow, Integer> aggPerCountry = mapPerCountry.computeIfAbsent(getCountryFromFileName(fileName), createMissingEntryFunction);
      List<String> perFileOutputs = new ArrayList<>();
      StringBuilder sb = new StringBuilder();
      XmlMatcherAbstract.CountValuesUpdateVariableFunction<IGroupByRow> statsUpdateFunction = new XmlMatcherAbstract.CountValuesUpdateVariableFunction<>();
      for (Map.Entry<IGroupByRow, Integer> insideFile : perFile.entrySet()) {
         // Übernahme der Daten in die aggrgierte Menge
         statsUpdateFunction.setNonNegativeIncrement(insideFile.getValue());
         aggPerCountry.compute(insideFile.getKey(), statsUpdateFunction);
         // Erzeuge die Zeile für die Ausgabe
         perFileOutputs.add( createLine(sb, insideFile) );
      } // Schleife über die Ergebnisse in einer Datei
      Collections.sort(perFileOutputs);
      return perFileOutputs;
   }

   protected String createLine(StringBuilder sb, Map.Entry<IGroupByRow, Integer> insideFile) {
      for (String s : insideFile.getKey().getComponentsOfKey()) {
         sb.append(s).append(SEPARATOR_COLUMNS);
      }
      sb.append(insideFile.getValue()).append(SEPARATOR_LINES);
      String line = sb.toString();
      sb.setLength(0);
      return line;
   }

   protected String getCountryFromFileName(String fileName) {
      if (fileName!=null) {
         Matcher m = CRF_PATTERN.matcher(fileName);
         if (m.find()) {
            String tenDigits = m.group();
            String clz = tenDigits.substring(0,3);
            for (List<String> cm : CLZ_MAPPINGS) {
               if (cm.get(1).compareTo(clz)<=0 && clz.compareTo(cm.get(2))<=0) {
                  return cm.get(0);
               }
            }
            return clz;
         }
      }
      return CN_OTHER;
   }

}
