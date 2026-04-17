package de.creditreform.crefoteam.cte.tesun.xmlsearch;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExportedZipsSearcherUtils
{
  static Logger         logger    = LoggerFactory.getLogger( ExportedZipsSearcherUtils.class );

  public static Pattern dPunktRep = Pattern.compile( ":" );
  public static Pattern starRep   = Pattern.compile( "\\*" );
  public static Pattern ampRep    = Pattern.compile( "&" );
  public static Pattern ltRep     = Pattern.compile( "<" );
  public static Pattern gtRep     = Pattern.compile( ">" );

  public static List<File> getFilesList( final File dirFile, final String fileExt, boolean isRecursive )
  {
    List<File> theFilesList = new ArrayList<>();
    File [] listFiles = dirFile.listFiles( new FileFilter()
    {
      public boolean accept( File theFile )
      {
        return theFile.isDirectory() || theFile.getName().endsWith( fileExt );
      }
    } );
    if( listFiles != null )
    {
      for( File theFile : listFiles )
      {
        if( isRecursive && theFile.isDirectory() )
        {
          theFilesList.addAll( getFilesList( theFile, fileExt, isRecursive ) );
          logger.debug( "Dir:" + theFile.getAbsoluteFile() );
        }
        else
        {
          theFilesList.add( theFile );
          logger.debug( "\tFile:" + theFile.getAbsoluteFile() );
        }
      }
    }
    return theFilesList;
  }

  public static String makeFileNameConform( String strValue )
  {
    String replacedStr = dPunktRep.matcher( strValue ).replaceAll( "_" );
    replacedStr = starRep.matcher( replacedStr ).replaceAll( "~" );
    return replacedStr;
  }

}
