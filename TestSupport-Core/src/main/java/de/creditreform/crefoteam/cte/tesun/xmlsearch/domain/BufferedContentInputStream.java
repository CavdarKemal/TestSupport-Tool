package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import de.creditreform.crefoteam.cte.tesun.util.TesunUtilites;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.ExportedZipsSearcherUtils;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.ISavedStreamContent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;

/**
 * Erweiterung von {@link ByteArrayInputStream}, der Inhalt kann nachträglich über die Schnittstelle
 * {@link ISavedStreamContent} ausgelesen werden
 */
public class BufferedContentInputStream
extends ByteArrayInputStream
implements ISavedStreamContent {

  public static BufferedContentInputStream create(InputStream inputStream) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    IOUtils.copy(inputStream, baos);
    // Aktuell erfolgt (noch?) ein entbehrlicher Kopiervorgang: toByteArray liefert eine Kopie
    return new BufferedContentInputStream(baos.toByteArray());
  }

  /**
   * Der Konstruktor ist nicht öffentlich. Bis jetzt ist noch nicht klar, ob stattdessen ein Konstruktor
   * mit Offset und Länge erforderlich ist.
   */
  protected BufferedContentInputStream(byte[] buf) {
    super(buf);
  }

  @Override
  public String getSavedContentAsString(boolean formatXML) throws Exception {
    String xmlFragment = new String( getBufferedBytesInternal(), "UTF-8" );
    xmlFragment = ExportedZipsSearcherUtils.ampRep.matcher(xmlFragment ).replaceAll("&amp;" );
    if( formatXML )
    {
      xmlFragment = TesunUtilites.toPrettyString( xmlFragment, 2 );
    }
    return xmlFragment;
  }

  protected byte [] getBufferedBytesInternal() {
    return buf;
  }

  @Override
  public byte [] getSavedContent() {
    byte[] bufferedBytesInternal = getBufferedBytesInternal();
    return Arrays.copyOf(bufferedBytesInternal, bufferedBytesInternal.length);
  }

}
