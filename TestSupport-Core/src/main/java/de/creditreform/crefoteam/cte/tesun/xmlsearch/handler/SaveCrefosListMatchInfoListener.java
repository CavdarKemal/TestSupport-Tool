package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.ISavedStreamContent;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryStatistics;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipEntryInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveCrefosListMatchInfoListener
extends AbstractMatchInfoListener {
  private FileWriter fileWriter = null;
  private final File resultsBaseDir;

  public SaveCrefosListMatchInfoListener(String optName, String searchResultsPath, String searchConfigurationName)
  {
    super(optName);
    resultsBaseDir = getBaseDir(searchResultsPath, searchConfigurationName);
  }

  @Override
  public boolean isThreadSafe() {
    return false;
  }

  @Override public void notifyEntryMatched(ISavedStreamContent savedStreamContent, IZipEntryInfo zipEntryInfo, IPerEntryStatistics xmlMatchStatistics) throws Exception
  {
    File outDir = childOf(resultsBaseDir, getOptName() );
    File crefosFile = childOf(outDir, "crefos.txt" );
    if( fileWriter == null )
    {
      if( !outDir.exists() )
      {
        outDir.mkdirs();
      }
      fileWriter = new FileWriter( crefosFile );
    }
    fileWriter.write( zipEntryInfo.getCrefonummer() );
    fileWriter.write( "\n" );
    zipEntryInfo.setResultFileName( crefosFile.getPath() );
  }

  @Override public void close() throws IOException
  {
    if( fileWriter != null )
    {
      fileWriter.flush();
      fileWriter.close();
      fileWriter = null;
    }
  }

  @Override public String toString()
  {
    return getOptName();
  }

}
