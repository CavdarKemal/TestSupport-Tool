package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ProgressListenerIF;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.XmlSearchCursor;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher.XmlMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.Collections;

public class XmlStreamProcessorLinear
implements XmlStreamProcessorIF {

   private static final XMLInputFactory xmlInputFactory;

   static {
      // Absicherung gegen XXE Sicherheitslücke
      // siehe https://github.com/OWASP/CheatSheetSeries/blob/master/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.md
      xmlInputFactory = XMLInputFactory.newInstance();
      xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
      xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
   }

   private final Logger logger;
   private final XmlMatcher xmlMatcher;
   private final String searchName;

   public XmlStreamProcessorLinear(XmlMatcher xmlMatcher, String searchName) {
      this.logger = LoggerFactory.getLogger( getClass() );
      this.xmlMatcher = xmlMatcher;
      this.searchName = searchName;
   }

   @Override
   public boolean handleForListeners(InputStream inputStream, IPerEntryListener perEntryListener, ProgressListenerIF progressListener)
   throws XMLStreamException {
      boolean success=false;
      try {
         xmlMatcher.reset();
         XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(inputStream);
         XmlStreamProcessorLinearCursor streamCursor = new XmlStreamProcessorLinearCursor();
         boolean elementNotYetHandled = false;
         while(xmlStreamReader.hasNext()){
            reportListeners(null, progressListener );
            progressListener.updateProgress(Collections.EMPTY_LIST);
            int eventType = xmlStreamReader.next();
            final String currentLocalName = streamCursor.getLocalName();
            switch (eventType) {
               case XMLEvent.START_ELEMENT:
                  if (elementNotYetHandled) {
                     // vorangegangenes Element ist noch nicht abgearbeitet...
                     handleContent(streamCursor, perEntryListener);
                  }
                  streamCursor.enterElement(xmlStreamReader);
                  String strInfo = "\t\t\t\tLocal name: "+ currentLocalName; // String.format ist hier nicht sinnvoll (zu langsam)
                  logger.debug("XMLEvent.START_ELEMENT:: {}",currentLocalName);
                  reportListeners(new LogInfo(searchName, ProgressListenerIF.LOG_LEVEL.DEBUG, strInfo, null), progressListener );
                  elementNotYetHandled = true;
                  break;
               case XMLEvent.CHARACTERS:
                  logger.debug("XMLEvent.CHARACTERS:: {}",currentLocalName);
                  streamCursor.elementContent(xmlStreamReader);
                  handleContent(streamCursor, perEntryListener);
                  elementNotYetHandled = false;
                  break;
               case XMLEvent.END_ELEMENT:
                  logger.debug("XMLEvent.END_ELEMENT:: {}",currentLocalName);
                  if (elementNotYetHandled) {
                     // vorangegangenes Element ist noch nicht abgearbeitet...
                     handleContent(streamCursor, perEntryListener);
                     elementNotYetHandled = false;
                  }
                  // ACHTUNG: Der obige Aufruf von 'handleContent' verarbeitet aktuell noch offene Inhalte. Aus Sicht
                  //          des aufgerufenen XmlMatcher muss dies erfolgen, _bevor_ eben diese Informationen durch
                  //          'streamCursor.exitElement' verändert werden.
                  streamCursor.exitElement(xmlStreamReader);
                  handleExit(streamCursor, perEntryListener);
                  break;
               default:
                  //do nothing
                  break;
            }
            // TODO: klären, ob und wie das XML-Parsing frühzeitig abgebrochen werden kann
         } // Schleife über alle Events, die der XMLStreamReader liefert
         success=true;
         return xmlMatcher.isSatisfied();
      } finally {
         xmlMatcher.notifyZipEntryCompleted(success, perEntryListener);
      }
   }

   private void handleContent(XmlSearchCursor xmlMatcherInput, IPerEntryListener perEntryListener)
   throws XMLStreamException {
      if (xmlMatcher!=null) {
         xmlMatcher.matchCursor(xmlMatcherInput, perEntryListener);
      }
   }

   private void handleExit(XmlSearchCursor xmlMatcherInput, IPerEntryListener perEntryListener)
   throws XMLStreamException {
      if (xmlMatcher!=null) {
         xmlMatcher.notifyExitElement(xmlMatcherInput, perEntryListener);
      }
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
