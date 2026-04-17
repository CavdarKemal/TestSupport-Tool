package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.*;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IMatchInfoListener;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.ISavedStreamContent;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipFileInfo;

import java.io.InputStream;
import java.util.concurrent.Callable;

public class SingleEntrySearchCallable<S extends InputStream & ISavedStreamContent>
implements Callable<SingleEntrySearchResult> {

   private final IZipFileInfo zipFileInfo;
   private final S inputStream;
   private final String entryName;
   private final IMatchInfoListener matchInfoListener;
   private final XmlMatcherThreadLocals threadLocals;

   public SingleEntrySearchCallable(XmlMatcherThreadLocals threadLocals, IZipFileInfo zipFileInfo, String entryName,
                                    S inputStream,
                                    IMatchInfoListener matchInfoListener) {
      this.threadLocals = threadLocals;
      this.zipFileInfo = zipFileInfo;
      this.inputStream = inputStream;
      this.entryName = entryName;
      this.matchInfoListener = matchInfoListener;
   }


   @Override
   public SingleEntrySearchResult call() throws Exception {
      XmlMatcherThreadLocals.Instances tl = threadLocals.get();
      // Suche ausführen
      // xmlMatcher.reset() wird durch den XmlStreamProcessor aufgerufen
      final IPerEntryListener perEntryListener = tl.getPerEntryListener();
      boolean isMatch = tl.getXmlStreamProcessor().handleForListeners(inputStream, perEntryListener, tl.getProgressListener());
      // Ergebnis-Container anlegen
      ZipSearcResult.ZipEntryInfo zipEntryInfo = new ZipSearcResult.ZipEntryInfo(zipFileInfo, zipFileInfo.getZipFilePath(), entryName );
      String strCrefoNummer = tl.getResultIdentification();
      zipEntryInfo.setCrefonummer( strCrefoNummer );
      SingleEntrySearchResult result = new SingleEntrySearchResult(isMatch, zipEntryInfo,
                                                                   perEntryListener);
      // Nachbereitung: Lösche die interne Liste der Treffer (Ergebnisse wurden zuvor in 'result' gespeichert
      perEntryListener.resetMatchingCriteriaList();
      // Listener über das Ergebnis informieren
      if (this.matchInfoListener != null) {
         if( !isMatch ) {
            this.matchInfoListener.notifyEntryNotMatched(inputStream, zipEntryInfo);
         }
         else {
            // Wir übergeben aus zwei Gründen das Search-Result als Detail-Info:
            // 1. Der MatchingCriterionListener erstellt mit jedem Aufruf von 'getMatchingCriteriaList' eine neue
            //    Kopie. Für das Search-Result ist das Verhalten in dieser Hinsicht bekannt und unkritisch.
            // 2. Die Inhalte des MatchingCriterionListener wurden mittels 'resetMatchingCriteriaList' gelöscht.
            //    Wir müssen also keine Einschränkungen in Bezug auf die Reihenfolge beachten.
            this.matchInfoListener.notifyEntryMatched(inputStream, zipEntryInfo, result);
         }
      }
      return result;
   }

}
