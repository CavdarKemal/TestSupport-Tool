package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.*;

import java.io.IOException;
import java.util.*;

/**
 * Wrapper für mehrere Instanzen von {@link IMatchInfoListener}
 */
public class MatchInfoListenerCollection
extends AbstractMatchInfoListener {

   private final List<IMatchInfoListener> listenerList;

   public MatchInfoListenerCollection() {
      super( null );
      listenerList = new ArrayList<>();
   }

   public MatchInfoListenerCollection addListeners(IMatchInfoListener... listenerArray) {
      if (listenerArray!=null) {
         addListeners(Arrays.asList(listenerArray));
      }
      return this;
   }

   public MatchInfoListenerCollection addListeners(Collection<IMatchInfoListener> listenerCollection) {
      addNonNull(this.listenerList, listenerCollection);
      return this;
   }

   protected final <T> void addNonNull(List<T> targetList, Collection<T> newValuesCollection) {
      if (newValuesCollection!=null) {
         for (T newValue : newValuesCollection) {
            if (newValue!=null) {
               targetList.add(newValue);
            }
         }
      }
   }

   /**
    * Lese die Liste der internen Instanzen von {@link IMatchInfoListener}, der Rückgabewert ist nicht änderbar
    */
   public List<? extends IMatchInfoListener> getListenerList() {
      return Collections.unmodifiableList(listenerList);
   }

   @Override
   public boolean isThreadSafe() {
      for (IMatchInfoListener infoListener : this.listenerList) {
         if (!infoListener.isThreadSafe()) {
            return false; // Ende mit dem ersten 'NEIN'
         }
      }
      return true;
   }

   @Override
   public void notifyEntryMatched(ISavedStreamContent savedStreamContent, IZipEntryInfo zipEntryInfo, IPerEntryStatistics xmlMatchStatistics) throws Exception {
      for (IMatchInfoListener infoListener : this.listenerList) {
         infoListener.notifyEntryMatched(savedStreamContent, zipEntryInfo, xmlMatchStatistics);
      }
   }

   @Override
   public void notifyEntryNotMatched(ISavedStreamContent savedStreamContent, IZipEntryInfo zipEntryInfo) throws Exception {
      for (IMatchInfoListener infoListener : this.listenerList) {
         infoListener.notifyEntryNotMatched(savedStreamContent, zipEntryInfo);
      }
   }

   @Override
   public void close() throws IOException {
      for (IMatchInfoListener infoListener : this.listenerList) {
         infoListener.close();
      }
   }

}
