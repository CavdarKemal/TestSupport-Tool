package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import java.util.List;

public class ProgressListenerGUINop
implements ProgressListenerGUI {

   @Override
   public void updateProgress(List<Object> chunks) {
      // intentionally empty
   }

   @Override
   public void updateData(Object dataObject) {
      // intentionally empty
   }

   @Override
   public void updateTaskState(ProgressListenerIF.TASK_STATE taskState) {
      // intentionally empty
   }

   @Override
   public boolean isCanceled() {
      return false;
   }

}
