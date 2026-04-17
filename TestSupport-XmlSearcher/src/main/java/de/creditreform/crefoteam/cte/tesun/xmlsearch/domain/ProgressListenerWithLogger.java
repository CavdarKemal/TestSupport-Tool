package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import org.slf4j.Logger;

/**
 * Logger-Implementierung von {@link ProgressListenerIF}
 */
public class ProgressListenerWithLogger
extends ProgressListenerBridge {
   private final Logger logger;

   public ProgressListenerWithLogger(Logger logger) {
      super(null);
      this.logger = logger;
   }

   @Override
   protected void updateAnyData(String whichData, Object dataObject) {
      if( (dataObject != null) && (logger != null) ) {
         logger.info(dataObject.toString());
      }
   }

   @Override
   public void updateTaskState(TASK_STATE taskState) {
      // intentionally empty
   }

   @Override
   public boolean isCanceled() {
      return false;
   }

}
