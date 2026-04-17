package de.creditreform.crefoteam.cte.tesun.xmlsearch;

import de.creditreform.crefoteam.cte.tesun.JobExecutionException;
import de.creditreform.crefoteam.cte.tesun.TesunClientJob;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.config.SearchConfiguration;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.config.SearchConfigurationFactory;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ProgressListenerIF.LOG_LEVEL;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.RuntimeSearchSpec;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.SearchSpecification;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.XmlStreamListenerGroup;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.handler.ExportedZipFilesHandler;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.handler.LogInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipSearcResult;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class SearchInExportedZips implements TesunClientJob
{
  private static Logger          logger = LoggerFactory.getLogger( SearchInExportedZips.class );
  private static final String [] resNames;
  static
  {
    resNames = new String []
    {
      "/GEE.SearchItems.ini",
      "/GEE.SearchItems.properties",
      "/PRE.SearchItems.ini",
      "/PRE.SearchItems.properties",
    };
  }

  public static final String       COMMAND                    = "xmlsearch";
  public static final String       DESCRIPTION                = "Durchsuche die XMLs innerhalb von Exporten";
  private final XmlStreamListenerGroup listenerGroup;
  private String                   pathCriteriaFile;

  public SearchInExportedZips(XmlStreamListenerGroup listenerGroup)
  {
    this.listenerGroup = listenerGroup;
  }

  public JOB_RESULT handleMapSearchCallable(Map<String, SearchSpecification> zipFileParsersMap) throws Exception
  {
    List<IZipSearcResult> zipSearcResultList = new ArrayList<>();
    for( Map.Entry<String, SearchSpecification> e : zipFileParsersMap.entrySet() )
    {
      final SearchSpecification searchSpecification = e.getValue();
      SearchCallable searchCallable = new SearchCallable(searchSpecification.getRuntimeSearchSpec(), listenerGroup );
      IZipSearcResult zipSearcResult = searchCallable.call();
      zipSearcResultList.add( zipSearcResult );
    }
    return JOB_RESULT.OK.setUserObject(zipSearcResultList);
  }

  /********************************************************************************************/
  /*****************************   Interface TesunClientJob    ********************************/
  /********************************************************************************************/
  @Override
  public String getJobCommandName()
  {
    return COMMAND;
  }

  @Override
  public String getJobCommandDescription()
  {
    return DESCRIPTION;
  }

  @Override
  public void init( EnvironmentConfig envConfig ) throws Exception
  {
    extractResourcesFromJar();
    pathCriteriaFile = envConfig.getProperty( TestSupportClientKonstanten.OPT_PATH_CRITERIA, true, "");
  }

  private void extractResourcesFromJar()
  {
    for( int i = 0; i < resNames.length; i++ )
    {
      String name = resNames[i];
      File file = new File( System.getProperty( "user.dir" ), name );
      logger.debug( "Prüfe, ob die Resource '{}' in JAR vorhanden ist...", name );
      if( file.exists() )
      {
        logger.debug( "Die Resource '{}' existiert schon.", name );
        continue;
      }
      InputStream resourceAsStream = this.getClass().getResourceAsStream( name );
      if( resourceAsStream != null )
      {
        try
        {
          IOUtils.copy( resourceAsStream, new FileOutputStream( file ) );
          logger.debug( "Die Resource '{}' wurde aus JAR extrahiert.", name );
        }
        catch (Exception ex)
        {
        }
      }
    }
  }

  @Override
  public JOB_RESULT call() throws Exception
  {
    String strInfo = String.format("Starte die Suche entsprechend Suchkriterien aus der Datei %s...", pathCriteriaFile);
    File configFileDir = new File( pathCriteriaFile );
    listenerGroup.updateData( new LogInfo(configFileDir.getName(), LOG_LEVEL.INFO, strInfo, null));
    if( !configFileDir.exists() )
    {
      String strErr = String.format( "Datei %s existiert nicht!", configFileDir.getPath() );
      listenerGroup.updateData( new LogInfo(configFileDir.getName(), LOG_LEVEL.ERROR, strErr, null));
      return JOB_RESULT.ERROR.setUserObject( new JobExecutionException( strErr ) );
    }
    SearchConfiguration searchConfiguration = SearchConfigurationFactory.createSearchConfiguration( pathCriteriaFile );
    return JOB_RESULT.OK.setUserObject( searchConfiguration.getZipSearcDataMap() );
  }

  /********************************************************************************************/
  /****************************    class SearchCallable    ************************************/
  /********************************************************************************************/
  public static class SearchCallable implements Callable<IZipSearcResult>, Runnable
  {
    private final RuntimeSearchSpec runtimeSearchSpec;
    private final XmlStreamListenerGroup listenerGroup;

    public SearchCallable(RuntimeSearchSpec runtimeSearchSpec, XmlStreamListenerGroup listenerGroup)
    {
      this.runtimeSearchSpec = runtimeSearchSpec;
      this.listenerGroup = listenerGroup;
    }

    @Override
    public void run()
    {
      try
      {
        call();
      }
      catch (Exception e)
      {
        // intentionally ignored
      }
    }

    @Override
    public IZipSearcResult call() throws Exception
    {
      ExportedZipFilesHandler exportedZipFilesHandler = new ExportedZipFilesHandler(new XmlMatcherWrapperFactoryCrefoExport());
      try
      {
        return exportedZipFilesHandler.doWork(runtimeSearchSpec, listenerGroup);
      }
      catch (Exception ex)
      {
        logger.error( "Fehler bei der Abarbeitung der Suchkriterien!\n{}", ex );
        throw ex;
      }
    }

  }
}
