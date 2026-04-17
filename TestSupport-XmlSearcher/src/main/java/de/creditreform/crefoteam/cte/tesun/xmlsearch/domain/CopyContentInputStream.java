package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import de.creditreform.crefoteam.cte.tesun.util.TesunUtilites;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.ExportedZipsSearcherUtils;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.ISavedStreamContent;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * {@link InputStream}, der beim Lesen die Daten speichert. Dadurch können die Inhalte nachträglich über die
 * Schnittstelle {@link ISavedStreamContent} ausgelesen werden.
 */
public class CopyContentInputStream
extends FilterInputStream
implements ISavedStreamContent {
  private final ByteArrayOutputStream baos;

  public CopyContentInputStream( InputStream in )
  {
    super( in );
    baos = new ByteArrayOutputStream();
  }

  @Override public int read( byte [] b, int off, int len ) throws IOException
  {
    int length = super.read( b, off, len );
    if( length != -1 )
    {
      baos.write( b, off, length );
    }
    return length;
  }

  @Override
  public String getSavedContentAsString(boolean formatXML) throws Exception
  {
    String xmlFragment = new String( getSavedContent(), "UTF-8" );
    xmlFragment = ExportedZipsSearcherUtils.ampRep.matcher( xmlFragment ).replaceAll( "&amp;" );
    if( formatXML )
    {
      xmlFragment = TesunUtilites.toPrettyString( xmlFragment, 2 );
    }
    return xmlFragment;
  }

  @Override
  public byte [] getSavedContent() throws IOException
  {
    int gelesen = in.read();
    while( gelesen != -1 )
    {
      baos.write( gelesen );
      gelesen = in.read();
    }
    byte [] content = baos.toByteArray();
    return content;
  }

  public void resetBuffer()
  {
    baos.reset();
  }

}
