package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.ISavedStreamContent;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryStatistics;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipEntryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CountCrefosMatchInfoListener
extends AbstractMatchInfoListener {

   /**
    * Container für die statistischen Angaben zu einer Zip-Datei
    */
  private static class PerFileStats {
     private int anzahlEntriesMitTreffer=0;
     private int anzahlTreffer=0;

     public PerFileStats(int anzahlEntriesMitTreffer, int anzahlTreffer) {
        this.anzahlEntriesMitTreffer = anzahlEntriesMitTreffer;
        this.anzahlTreffer = anzahlTreffer;
     }

     public PerFileStats addEintragMitTreffer(int anzahlTrefferImEintrag) {
        if (anzahlTrefferImEintrag>=0) {
           anzahlEntriesMitTreffer++;
           anzahlTreffer += anzahlTrefferImEintrag;
        }
        return this;
     }
  }

  private AtomicInteger anzahlZipEntries = new AtomicInteger(0);
  private final ConcurrentHashMap<String, PerFileStats> perFileMap;

  public CountCrefosMatchInfoListener( String optName )
  {
    super(optName);
    this.perFileMap = new ConcurrentHashMap<>();
  }

  @Override
  public boolean isThreadSafe() {
    return true;
  }

  @Override public void notifyEntryMatched(ISavedStreamContent savedStreamContent, IZipEntryInfo zipEntryInfo, IPerEntryStatistics xmlMatchStatistics)
  {
    anzahlZipEntries.incrementAndGet();
    // Berechne die Anzahl der Treffer, die der angegebene ZipEntry beinhaltet...
    final int anzahlTrefferImEintrag;
    if (xmlMatchStatistics==null || xmlMatchStatistics.getMatchingCriteriaCount()<=0) {
      anzahlTrefferImEintrag=1;
    }
    else {
      anzahlTrefferImEintrag = xmlMatchStatistics.getMatchingCriteriaCount();
    }
    // aktualisiere die Map
    this.perFileMap.compute(zipEntryInfo.getZipFileName(), (String s, PerFileStats previousValue)->{
      if (previousValue==null) {
        return new PerFileStats(1, anzahlTrefferImEintrag);
      }
      else {
        return previousValue.addEintragMitTreffer(anzahlTrefferImEintrag);
      }
    });
  }

  protected int getAnzahlZipEntries()
  {
    return anzahlZipEntries.get();
  }

  protected String buildOnCloseMessage() {
    StringBuilder sb = new StringBuilder();
    sb.append("=== Anzahl der Zip-Entries mit mindestens einem Treffer: ").append(getAnzahlZipEntries()).append(" ===\n");
    sb.append("=== Statistik der gefundenen Treffer (Zip-Datei,Einträge mit Treffer, Anzahl Treffer in der Datei) ===\n");
    Map<String, PerFileStats> sortedMap = new TreeMap<>(perFileMap);
    int anzahlTrefferGesamt=0;
    for (Map.Entry<String, PerFileStats> e : sortedMap.entrySet()) {
      anzahlTrefferGesamt+=e.getValue().anzahlTreffer;
      sb.append(e.getKey()).append(',').append(e.getValue().anzahlEntriesMitTreffer).append(',').append(e.getValue().anzahlTreffer).append("\n");
    }
    sb.append("=== Anzahl der gefundenen Treffer insgesamt: ").append(anzahlTrefferGesamt);
    return sb.toString();
  }

  @Override public void close()
  {
    Logger logger = LoggerFactory.getLogger( TestSupportClientKonstanten.LOW_THRESHOLD_LOGGER );
    if (logger.isInfoEnabled()) {
      logger.info(buildOnCloseMessage() );
    }


  }

  @Override public String toString()
  {
    return getOptName();
  }

}
