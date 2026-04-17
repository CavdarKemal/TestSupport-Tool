package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryStatistics;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.ISavedStreamContent;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipEntryInfo;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.commons.io.FileUtils;

public class SaveXmlsMatchInfoListener
extends AbstractMatchInfoListener {

  private final File resultsBaseDir;

  public SaveXmlsMatchInfoListener(String optName, String searchResultsPath, String searchConfigurationName)
  {
    super(optName);
    this.resultsBaseDir = getBaseDir(searchResultsPath, searchConfigurationName);
  }

  @Override
  public boolean isThreadSafe() {
    return false;
  }

  @Override public void notifyEntryMatched(ISavedStreamContent savedStreamContent, IZipEntryInfo zipEntryInfo, IPerEntryStatistics xmlMatchStatistics) throws Exception
  {
    File fileWithOptName = childOf(this.resultsBaseDir, getOptName());
    // ==> E:\Projekte\CTE\testsupport_client\TestSupportGUI\X-TESTS\ExportSuche\EH-Results\Löschsätze\CREFOS_XML

    File zipFile = new File(zipEntryInfo.getZipFileName());
    // ==> E:\Projekte\CTE\testsupport_client\TestSupportGUI\X-TESTS\ExportSuche\EH\2024-01-09_09-57\abCrefo_4120000000.zip

    File fileWithOptNameAndSrcPath = new File(fileWithOptName, zipFile.getParentFile().getName());
    // ==> E:\Projekte\CTE\testsupport_client\TestSupportGUI\X-TESTS\ExportSuche\EH-Results\Löschsätze\CREFOS_XML\2024-01-09_09-57

    File fileWithOptNameAndSrcPathAndX = new File(fileWithOptNameAndSrcPath, zipFile.getName());
    // ==> E:\Projekte\CTE\testsupport_client\TestSupportGUI\X-TESTS\ExportSuche\EH-Results\Löschsätze\CREFOS_XML\2024-01-09_09-57\abCrefo_4120000000.zip

    File fileWithOptNameAndSrcPathAndXAndEntry = new File(fileWithOptNameAndSrcPathAndX, zipEntryInfo.getZipEntryName());
    // ==> E:\Projekte\CTE\testsupport_client\TestSupportGUI\X-TESTS\ExportSuche\EH-Results\Löschsätze\CREFOS_XML\2024-01-09_09-57\abCrefo_4120000000.zip\loeschsatz_4120029659.xml

    if( !fileWithOptNameAndSrcPathAndX.exists() )
    {
      fileWithOptNameAndSrcPathAndX.mkdirs();
    }
    String xmlString = savedStreamContent.getSavedContentAsString(true );
    if(fileWithOptNameAndSrcPathAndXAndEntry.exists()) {
      System.out.println("Überschreibe die Datei " + fileWithOptNameAndSrcPathAndXAndEntry.getPath());
    }
    FileUtils.writeStringToFile(fileWithOptNameAndSrcPathAndXAndEntry, xmlString, Charset.forName("UTF-8"));
    zipEntryInfo.setResultFileName( fileWithOptNameAndSrcPathAndXAndEntry.getPath() );
  }

  @Override public void close() throws IOException
  {
    // intentionally empty
  }

  @Override public String toString()
  {
    return getOptName();
  }

}
