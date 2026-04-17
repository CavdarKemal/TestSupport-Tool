package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.handler.LogInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipEntryInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipFileInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipSearcResult;

import java.util.List;

/**
 * Nop-Implementierung von {@link ProgressListenerIF}
 */
public class ProgressListenerNop
implements ProgressListenerIF {

   @Override
   public void updateProgress(List<Object> chunks) {
      // intentionally empty
   }

   @Override
   public void updateData(Object dataObject) {
      // intentionally empty
   }

   @Override
   public void updateData(LogInfo logInfo) {
      // intentionally empty
   }

   @Override
   public void updateData(IZipSearcResult zipSearcResult) {
      // intentionally empty
   }

   @Override
   public void updateData(IZipFileInfo zipFileInfo) {
      // intentionally empty
   }

   @Override
   public void updateData(IZipEntryInfo zipEntryInfo) {
      // intentionally empty
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
