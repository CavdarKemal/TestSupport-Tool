package de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.XmlSearchCursor;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlMatcherOneOfList extends XmlMatcherTagNameOnly {

   protected static final Logger logger                = LoggerFactory.getLogger( XmlMatcherOneOfList.class );
   protected static final String DEFAULT_RESOURCE_NAME = "/find-one-of.config";

   private final Set<String> stringsToFind;
   private String resourceName;
   private boolean crefoInList;

   public XmlMatcherOneOfList(MatcherParameterTag matcherParameterTag, Collection<String> alternateTagNames) {
      super(matcherParameterTag, alternateTagNames);
      this.stringsToFind = new HashSet<>();
   }

   public XmlMatcherOneOfList initFromResource(String resourceName) {
      if (resourceName==null) {
         this.resourceName = DEFAULT_RESOURCE_NAME;
      }
      else {
         this.resourceName = resourceName;
      }
      try (InputStream inputStream = getClass().getResourceAsStream(this.resourceName)) {
         return initFromStream(inputStream);
      }
      catch (IOException e) {
         throw new IllegalStateException("ClassPath-Resource zur Konfiguration des "+getClass().getSimpleName()+" nicht lesbar", e);
      }
   }

   public XmlMatcherOneOfList initFromFile(String fileName) {
      if (fileName==null) {
         throw new IllegalArgumentException("Konfiguration aus einer Datei ohne Angabe von 'filename'");
      }
      else {
         this.resourceName = "file://"+fileName;
      }
      try (FileInputStream inputStream = new FileInputStream(fileName)) {
         return initFromStream(inputStream);
      }
      catch (IOException e) {
         throw new IllegalStateException("Datei zur Konfiguration des "+getClass().getSimpleName()+" nicht lesbar", e);
      }
   }

   public XmlMatcherOneOfList initFromStream(InputStream inputStream)
   throws IOException {
      List<String> lines = IOUtils.readLines(inputStream);
      for (String l : lines) {
         if (l!=null && l.trim().length()>0) {
            stringsToFind.add(l.trim());
         }
      }
      logger.info("Aus der Datei '" + resourceName + "' wurden " + stringsToFind.size() + " Crefos ausgelesen.");
      return this;
   }

   protected String getResourceName() {
      return resourceName;
   }

   protected Set<String> getStringsToFind() {
      return Collections.unmodifiableSet(stringsToFind);
   }

   @Override public boolean isSatisfied()
   {
      return crefoInList;
   }

   @Override public XmlMatcherOneOfList matchCursor(XmlSearchCursor childCursor,   IPerEntryListener perEntryListener) throws XMLStreamException {
      super.reset();
      super.matchCursor(childCursor, perEntryListener);
      if(super.isSatisfied()) {
         String tagValue = childCursor.getElemStringValue();
         if (tagValue!=null) {
            boolean contains = stringsToFind.contains(tagValue.trim());
            if(contains) {
               String strInfo = "Crefo aus der Liste gefunden: "+ tagValue;
               logger.info( "\t\t\t*** " + strInfo);
               perEntryListener.addMatchingCriterion(strInfo);
            }
            crefoInList |= contains;
         }
      }
      return this;
   }

   @Override public XmlMatcherTagNameOnly reset()
   {
      crefoInList = false;
      return this;
   }


}
