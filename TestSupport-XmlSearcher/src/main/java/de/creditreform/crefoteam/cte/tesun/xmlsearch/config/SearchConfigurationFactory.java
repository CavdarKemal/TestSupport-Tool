package de.creditreform.crefoteam.cte.tesun.xmlsearch.config;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.SearchCriteria;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.SearchSpecification;
import org.apache.commons.configuration.*;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten.*;

public class SearchConfigurationFactory
{
  public static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

  public static SearchConfiguration createSearchConfiguration( String configFileName ) throws ConfigurationException
  {
    if( ( configFileName == null ) || configFileName.isEmpty() )
    {
      throw new ConfigurationException( "Konfigurationsdateiname darf nicht NULL oder leer sein!" );
    }
    if( !configFileName.endsWith( ".properties" ) )
    {
      configFileName += ".properties";
    }
    File thefile = new File( configFileName );
    if( !thefile.exists() )
    {
      throw new ConfigurationException( "Die Konfigurationsdatei '" + configFileName + "' existiert nicht!" );
    }
    final FileConfiguration theConfiguration = new PropertiesConfiguration();
    theConfiguration.setEncoding(DEFAULT_CHARSET.name());
    theConfiguration.load(configFileName);
    return new SearchConfiguration( theConfiguration );
  }

  public static void saveSearchConfiguration(Map<String, SearchSpecification> searcDataMap, String configFileName ) throws ConfigurationException
  {
    final FileConfiguration theConfiguration = new PropertiesConfiguration();
    theConfiguration.setEncoding(DEFAULT_CHARSET.name());
    Iterator<Entry<String, SearchSpecification>> iterator = searcDataMap.entrySet().iterator();
    while( iterator.hasNext() ) {
      Entry<String, SearchSpecification> nextEntry = iterator.next();
      SearchSpecification searchSpecification = nextEntry.getValue();
      String sectionName = searchSpecification.getName() + ".";
      theConfiguration.addProperty(sectionName + PROP_NAME_XML_STREAM_PROCESSOR, searchSpecification.getUsedXmlStreamProcessor() );
      theConfiguration.addProperty(sectionName + PROP_NAME_XML_SEARCH_SOURCE, searchSpecification.getSourceFile() );
      theConfiguration.addProperty(sectionName + PROP_NAME_XML_CREFO_TAGNAME, searchSpecification.getCrefoNrTagName() );
      theConfiguration.addProperty(sectionName + PROP_NAME_LOGICAL_CONNECTION, searchSpecification.getLogicalConnection().name() );
      theConfiguration.addProperty(sectionName + PROP_NAME_XML_INVERT_RESULTS, searchSpecification.isInvertedResults() );
      theConfiguration.addProperty(sectionName + PROP_NAME_XML_RESULT_TYPE, searchSpecification.getSearchResultsType() );

      int nIndex = 1;
      List<SearchCriteria> searchCriteriasList = searchSpecification.getSearchCriteriasList();
      for( SearchCriteria searchCriteria : searchCriteriasList )
      {
        String value = searchCriteria.getSearchTag() + "=";
        String searchValue = searchCriteria.getSearchValue();
        if(searchValue != null) {
          value += searchValue;
        }
        theConfiguration.addProperty( ( sectionName + "SEARCH" + nIndex++ ), value );
      }
      searchSpecification.setDirty(false);
    }
    theConfiguration.save( configFileName );
  }

  public static void validate(SearchSpecification searchSpecification) {
    List<SearchCriteria> searchCriteriasList = searchSpecification.getSearchCriteriasList();
    for(SearchCriteria searchCriteria : searchCriteriasList ) {
      String searchTag = searchCriteria.getSearchTag();
      if(searchTag.equals(SearchCriteria.XML_MATCHER_ONE_OF_LIST_TAG_NAME)) {
        String searchValue = searchCriteria.getSearchValue();
        File theFile = new File(searchValue);
        if (!theFile.isAbsolute()) {
          theFile = new File(searchSpecification.getSourceFile(), searchValue);
          if(!theFile.exists() || !theFile.canRead()) {
            String strErr = "Die Datei '" + theFile.getAbsolutePath() + "'\nfür das Suchkriterium '" + searchSpecification.getName() + ".file::'\nexistiert nicht!";
            strErr += "\nDiese muss im Verzeichnis \n'" + searchSpecification.getSourceFile() + "'\nexistieren!";
            throw new IllegalStateException(strErr);
          }
        }
      }
    }
  }
}
