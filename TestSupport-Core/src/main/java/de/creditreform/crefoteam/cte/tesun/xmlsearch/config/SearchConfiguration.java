package de.creditreform.crefoteam.cte.tesun.xmlsearch.config;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.SearchSpecification;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;

import java.io.File;
import java.util.*;

import static de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten.*;

public class SearchConfiguration {
   private final Configuration theConfiguration;

   public SearchConfiguration(Configuration theConfiguration) {
      this.theConfiguration = theConfiguration;
   }

   public Map<String, SearchSpecification> getZipSearcDataMap() throws ConfigurationException {
      Set<String> sections = new HashSet<>();
      Map<String, SearchSpecification> zipSearcDataMap = new TreeMap<>();
      Iterator<String> keys = theConfiguration.getKeys();
      while (keys.hasNext()) {
         String strKey = keys.next();
         String[] strKeyParts = strKey.split("\\.");
         if (strKeyParts.length != 2) {
            throw new ConfigurationException("Fehlerhafte Konfiguration: " + strKey);
         }
         String strSection = strKeyParts[0];
         if (!sections.contains(strSection)) {
            sections.add(strSection);
         }
      }

      XML_STREAM_PROCESSOR defaultXmlStreamProcessor = XML_STREAM_PROCESSOR.RECURSIVE;
      String defaultCrefoTagName = "crefonummer";
      File defaultXmlFilesSourcePath = new File("");
      SEARCH_RESULT_TYPE defaultResultsType = SEARCH_RESULT_TYPE.CREFOS_XML;
      LOGICAL_CONNECTION defaultLogicalConnection = LOGICAL_CONNECTION.LOGIC_OR;

      for (String strSection : sections) {
         List<String> linesToParse = new ArrayList<>();
         Configuration subset = theConfiguration.subset(strSection);
         Iterator<String> sectionElements = subset.getKeys();
         while (sectionElements.hasNext()) {
            String prop = sectionElements.next();
            String value = subset.getString(prop).trim();
            String strInfo = String.format("%s=%s", prop, value).replaceAll("null", "");
            if (strSection.equals(PROP_NAME_XML_SECTION_DEFAULT)) {
               if (prop.equals(PROP_NAME_XML_STREAM_PROCESSOR)) {
                  defaultXmlStreamProcessor = XML_STREAM_PROCESSOR.valueOf(value.trim());
               } else if (prop.equals(PROP_NAME_XML_SEARCH_SOURCE)) {
                  defaultXmlFilesSourcePath = new File(value);
               } else if (prop.equals(PROP_NAME_XML_RESULT_TYPE)) {
                  defaultResultsType = SEARCH_RESULT_TYPE.valueOf(value);
               } else if (prop.equals(PROP_NAME_XML_CREFO_TAGNAME)) {
                  defaultCrefoTagName = value;
               } else if (prop.equals(PROP_NAME_LOGICAL_CONNECTION)) {
                  defaultLogicalConnection = LOGICAL_CONNECTION.valueOf(value);
               }
            } else {
               linesToParse.add(strInfo);
            }
         }
         if (!strSection.equals(PROP_NAME_XML_SECTION_DEFAULT)) {
            SearchSpecification searchSpecification = new SearchSpecification(strSection, linesToParse);
            zipSearcDataMap.put(strSection, searchSpecification);
         }
      }
      for (String strSection : sections) {
         SearchSpecification searchSpecification = zipSearcDataMap.get(strSection);
         if (searchSpecification == null) {
            continue;
         }
         // Setze die Default-Werte...
         if (searchSpecification.getUsedXmlStreamProcessor() == null) {
            searchSpecification.setUsedXmlStreamProcessor(defaultXmlStreamProcessor);
         }
         if (searchSpecification.getSourceFile() == null) {
            searchSpecification.setSourceFile(defaultXmlFilesSourcePath);
         }
         if (searchSpecification.getCrefoNrTagName() == null) {
            searchSpecification.setCrefoNrTagName(defaultCrefoTagName);
         }
         if (searchSpecification.getSearchResultsType() == null) {
            searchSpecification.setSearchResultsType(defaultResultsType);
         }
         if (searchSpecification.getLogicalConnection() == null) {
            searchSpecification.setLogicalConnection(defaultLogicalConnection);
         }
         // Prüfe die Mindest-Parameter
         if ((searchSpecification.getSourceFile() == null) || !searchSpecification.getSourceFile().exists()) {
            searchSpecification.setSourceFile(new File("SOURCE wurde nicht gesetzt!"));
         }
      }
      return zipSearcDataMap;
   }

}
