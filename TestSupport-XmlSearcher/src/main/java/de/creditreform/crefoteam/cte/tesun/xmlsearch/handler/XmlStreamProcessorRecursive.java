package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import com.ctc.wstx.stax.WstxInputFactory;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.DefaultSMInputCursor;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ISMInputCursor;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ProgressListenerIF;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ProgressListenerIF.LOG_LEVEL;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher.XmlMatcher;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.util.Collections;

public class XmlStreamProcessorRecursive
implements XmlStreamProcessorIF {
  private static final SMInputFactory FACTORY;

  static {
    final WstxInputFactory wstxInputFactory = new WstxInputFactory();
    wstxInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
    wstxInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
    FACTORY            = new SMInputFactory(wstxInputFactory);
  }

  static Logger logger             = LoggerFactory.getLogger(XmlStreamProcessorRecursive.class);

  private final XmlMatcher      xmlMatcher;
  private final String searchName;

  public XmlStreamProcessorRecursive(XmlMatcher xmlMatcher, String searchName)
  {
    this.xmlMatcher = xmlMatcher;
    this.searchName = searchName;
  }


  @Override
  public boolean handleForListeners(InputStream inputStream, IPerEntryListener perEntryListener, ProgressListenerIF progressListener) throws XMLStreamException {
    SMHierarchicCursor rootCursor = FACTORY.rootElementCursor(inputStream);
    rootCursor.advance();
    SMHierarchicCursor childCursor = (SMHierarchicCursor)rootCursor.childElementCursor();
    ISMInputCursor iSMHierarchicCursor = new DefaultSMInputCursor( childCursor );
    return handleForListeners(iSMHierarchicCursor, perEntryListener, progressListener );

  }

  protected boolean handleForListeners(ISMInputCursor elementCursor, IPerEntryListener perEntryListener, ProgressListenerIF workerListener) throws XMLStreamException
  {
    if( elementCursor == null )
    {
      throw new XMLStreamException( "childElementCursor ist null!" );
    }
    boolean success=false;
    try {
      xmlMatcher.reset();
      searchForElement( elementCursor, perEntryListener, workerListener );
      success=true;
      return xmlMatcher.isSatisfied();
    } finally {
      xmlMatcher.notifyZipEntryCompleted(success, perEntryListener);
    }
  }

  private void searchForElement(ISMInputCursor elementCursor, IPerEntryListener perEntryListener, ProgressListenerIF workerListener) throws XMLStreamException
  {
    while( elementCursor != null && elementCursor.getNext() != null )
    {
      final String currentLocalName = elementCursor.getLocalName();
      String strInfo = "\t\t\t\tLocal name: "+ currentLocalName;
      reportListeners( new LogInfo(searchName, LOG_LEVEL.DEBUG, strInfo, null), workerListener );
      final int eventCode = elementCursor.getCurrEventCode();
      if( eventCode == XMLStreamConstants.START_ELEMENT )
      {
        xmlMatcher.matchCursor( elementCursor, perEntryListener);
        if( XMLStreamConstants.START_ELEMENT == elementCursor.getCurrEventCode() )
        {
          final ISMInputCursor childElementCursor = elementCursor.childElementCursor( currentLocalName );
          searchForElement( childElementCursor, perEntryListener, workerListener );
          reportListeners( null, workerListener );
        }
      }
    } // Schleife über alle Elemente 'auf einer 'Ebene'
    elementCursor.exitElement();
    xmlMatcher.notifyExitElement(elementCursor, perEntryListener);
  }

  private void reportListeners( LogInfo logInfo, ProgressListenerIF workerListener)
  {
    if (workerListener!=null) {
      if(logInfo == null) {
        workerListener.updateProgress(Collections.EMPTY_LIST);
      }
      else {
        workerListener.updateData(logInfo);
      }
    }
  }

}
