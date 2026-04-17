package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

/**
 * Container für eine Menge von {@link ProgressListenerIF}-Instanzen
 */
public class ProgressListenerCollection
//implements ProgressListenerIF
{
/*

   private final List<ProgressListenerIF> listenerCollection = new ArrayList<>();

   public ProgressListenerCollection addWorkerListener(ProgressListenerIF workerListener)
   {
      listenerCollection.add( workerListener );
      return this;
   }

   public ProgressListenerCollection addWorkerListeners(List<ProgressListenerIF> theWorkerListenerList)
   {
      listenerCollection.addAll( theWorkerListenerList );
      return this;
   }

   @Override
   public void updateProgress(int progressStep) {
      for (ProgressListenerIF iwl : listenerCollection) {
         iwl.updateProgress(progressStep);
      }
   }

   @Override
   public void updateData(Object dataObject) {
      for (ProgressListenerIF iwl : listenerCollection) {
         iwl.updateData(dataObject);
      }
   }

   @Override
   public void updateTaskState(TASK_STATE taskState) {
      for (ProgressListenerIF iwl : listenerCollection) {
         iwl.updateTaskState(taskState);
      }
   }

   @Override
   public boolean isCanceled() {
      for( ProgressListenerIF workerListener : listenerCollection )
      {
         if( workerListener.isCanceled() )
         {
            return true;
         }
      }
      return false;
   }

*/
}
