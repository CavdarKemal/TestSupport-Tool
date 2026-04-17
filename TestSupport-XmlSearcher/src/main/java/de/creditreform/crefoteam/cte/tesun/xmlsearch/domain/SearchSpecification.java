package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten.*;

/**
 * Parsing der vom Anwender vorgegebenen Such-Spezifikation und Erzeugung der dazu passenden XmlMatcher.
 * Die Anwendungsfall-spezifischen Ergänzungen an den XmlMatchern werden hier nicht berücksichtigt
 */
public class SearchSpecification {
   static Logger logger = LoggerFactory.getLogger(SearchSpecification.class);

   private XML_STREAM_PROCESSOR usedXmlStreamProcessor = XML_STREAM_PROCESSOR.LINEAR; // Default

   private boolean dirty;
   private boolean activated = true;
   private String name;
   private File sourceFile;
   private boolean invertedResults;
   private String crefoNrTagName = null;
   private SEARCH_RESULT_TYPE searchResultsType = SEARCH_RESULT_TYPE.CREFOS_XML;
   private LOGICAL_CONNECTION logicalConnection = LOGICAL_CONNECTION.LOGIC_OR;
   private List<SearchCriteria> searchCriteriasList = new ArrayList<>();

   public SearchSpecification(SearchSpecification cloneDefinition) {
      setName(cloneDefinition.getName());
      setActivated(cloneDefinition.isActivated());
      setCrefoNrTagName(cloneDefinition.getCrefoNrTagName());
      setLogicalConnection(cloneDefinition.getLogicalConnection());
      setInvertedResults(cloneDefinition.isInvertedResults());
      setSourceFile(cloneDefinition.getSourceFile());
      setSearchResultsType(cloneDefinition.getSearchResultsType());
      setSearchCriteriasList(cloneDefinition.getSearchCriteriasList());
      setDirty(false);
   }

   public SearchSpecification(String name) {
      this(name, Collections.emptyList());
   }

   public SearchSpecification(String name, List<String> linesToParse) {
      this.name = name;
      if ((name == null) || name.isEmpty() || (linesToParse == null)) {
         throw new IllegalArgumentException("Parameter dürfen nicht NULL oder leer sein!");
      }
      initModelFromLines(linesToParse);
   }

   public void initModelFromLines(List<String> linesToParse) {
      setDirty(false);
      for (String lineToParse : linesToParse) {
         String[] lineParts = lineToParse.split("=");
         String propName = lineParts[0];
         if (lineParts.length < 2) {
            throw new IllegalArgumentException(propName + " wurde nicht gesetzt!");
         }
         String searchTag = lineParts[1];
         if (propName.equals(PROP_NAME_XML_STREAM_PROCESSOR)) {
            usedXmlStreamProcessor = XML_STREAM_PROCESSOR.valueOf(searchTag);
         } else if (propName.equals(PROP_NAME_XML_SEARCH_SOURCE)) {
            sourceFile = new File(assignIniVariable(lineParts, PROP_NAME_XML_SEARCH_SOURCE));
         } else if (propName.equals(PROP_NAME_XML_CREFO_TAGNAME)) {
            crefoNrTagName = assignIniVariable(lineParts, PROP_NAME_XML_CREFO_TAGNAME);
         } else if (propName.equals(PROP_NAME_XML_INVERT_RESULTS)) {
            invertedResults = !"false".equals(assignIniVariable(lineParts, PROP_NAME_XML_INVERT_RESULTS));
         } else if (propName.equals(PROP_NAME_LOGICAL_CONNECTION)) {
            logicalConnection = LOGICAL_CONNECTION.valueOf(assignIniVariable(lineParts, PROP_NAME_LOGICAL_CONNECTION));
         } else if (propName.equals(PROP_NAME_XML_RESULT_TYPE)) {
            searchResultsType = SEARCH_RESULT_TYPE.valueOf(searchTag);
         } else if (propName.startsWith(PROP_NAME_XML_SEARCH_CRITERIA)) {
            final String searchValue;
            if (lineParts.length > 2) {
               searchValue = lineParts[2].trim().replaceAll("\"", "");
            } else {
               searchValue = null;
            }
            searchCriteriasList.add(new SearchCriteria(searchTag, searchValue));
         }
      }
   }

   public void setUsedXmlStreamProcessor(XML_STREAM_PROCESSOR usedXmlStreamProcessor) {
      setDirty(!Objects.equals(usedXmlStreamProcessor, this.usedXmlStreamProcessor));
      this.usedXmlStreamProcessor = usedXmlStreamProcessor;
   }

   public XML_STREAM_PROCESSOR getUsedXmlStreamProcessor() {
      return usedXmlStreamProcessor;
   }

   public RuntimeSearchSpec getRuntimeSearchSpec() {
      return new RuntimeSearchSpec().configure(this);
   }

   private String assignIniVariable(String[] lineParts, String varName) {
      if (lineParts.length != 2) {
         throw new IllegalArgumentException(varName + " wurde nicht gesetzt!");
      }
      return lineParts[1].trim();
   }

   public boolean isActivated() {
      return activated;
   }

   public void setActivated(boolean activated) {
      setDirty(activated == this.activated);
      this.activated = activated;
   }

   public File getSourceFile() {
      return sourceFile;
   }

   public void setSourceFile(File sourceFile) {
      setDirty(this.sourceFile != null && !Objects.equals(sourceFile.getAbsolutePath(), this.sourceFile.getAbsolutePath()));
      this.sourceFile = sourceFile;
   }

   public boolean isInvertedResults() {
      return invertedResults;
   }

   public void setInvertedResults(boolean invertedResults) {
      setDirty(invertedResults == this.invertedResults);
      this.invertedResults = invertedResults;
   }

   public String getCrefoNrTagName() {
      return crefoNrTagName;
   }

   public void setCrefoNrTagName(String crefoNrTagName) {
      setDirty(!Objects.equals(crefoNrTagName, this.crefoNrTagName));
      this.crefoNrTagName = crefoNrTagName;
   }

   public String getSearchResultsPath() {
      return new File(sourceFile.getAbsolutePath() + "-Results").getAbsolutePath();
   }

   public SEARCH_RESULT_TYPE getSearchResultsType() {
      return searchResultsType;
   }

   public void setSearchResultsType(SEARCH_RESULT_TYPE searchResultsType) {
      setDirty(!Objects.equals(searchResultsType, this.searchResultsType));
      this.searchResultsType = searchResultsType;
   }

   public LOGICAL_CONNECTION getLogicalConnection() {
      return logicalConnection;
   }

   public boolean isLogicalConnectionOr() {
      return logicalConnection.equals(LOGICAL_CONNECTION.LOGIC_OR);
   }

   public boolean isLogicalConnectionAnd() {
      return logicalConnection.equals(LOGICAL_CONNECTION.LOGIC_AND);
   }

   public void setLogicalConnection(LOGICAL_CONNECTION logicalConnection) {
      this.logicalConnection = logicalConnection;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      setDirty(!Objects.equals(name, this.name));
      this.name = name;
   }

   public List<SearchCriteria> getSearchCriteriasList() {
      return searchCriteriasList;
   }

   private void setSearchCriteriasList(List<SearchCriteria> searchCriteriasList) {
      setDirty(true);
      this.searchCriteriasList.clear();
      for (SearchCriteria searchCriteria : searchCriteriasList) {
         this.searchCriteriasList.add(new SearchCriteria(searchCriteria));
      }
   }

   public boolean isDirty() {
      if (dirty) {
         return true;
      }
      ;
      for (SearchCriteria searchCriteria : searchCriteriasList) {
         if (searchCriteria.isDirty()) {
            return true;
         }
      }
      return false;
   }

   public void setDirty(boolean dirty) {
      this.dirty = dirty;
   }

}
