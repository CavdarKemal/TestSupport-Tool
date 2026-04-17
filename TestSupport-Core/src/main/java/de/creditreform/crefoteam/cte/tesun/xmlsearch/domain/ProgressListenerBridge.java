package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.handler.LogInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipEntryInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipFileInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipSearcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProgressListenerBridge
implements ProgressListenerIF {
   private final Logger logger;
   private final ProgressListenerGUI progressListenerGUI;

   public ProgressListenerBridge(ProgressListenerGUI progressListenerGUI) {
      this(null, progressListenerGUI);
   }

   public ProgressListenerBridge(Logger logger, ProgressListenerGUI progressListenerGUI) {
      this.logger = (logger!=null) ? logger : LoggerFactory.getLogger(getClass());
      this.progressListenerGUI = (progressListenerGUI!=null) ? progressListenerGUI : new ProgressListenerGUINop();
   }

   @Override
   public void updateProgress(List<Object> chunks) {
      try {
         this.progressListenerGUI.updateProgress(chunks);
      } catch (Exception e) {
         logger.error("Exception in ProgressListenerGUI#updateProgress", e);
      }
   }

   @Override
   public void updateData(Object dataObject) {
      updateAnyData("LogInfo", dataObject);
   }

   @Override
   public void updateData(LogInfo logInfo) {
      updateAnyData("LogInfo", logInfo);
   }

   @Override
   public void updateData(IZipSearcResult zipSearcResult) {
      updateAnyData("ZipSearcResult", zipSearcResult);
   }

   @Override
   public void updateData(IZipFileInfo zipFileInfo) {
      updateAnyData("ZipFileInfo", zipFileInfo);
   }

   @Override
   public void updateData(IZipEntryInfo zipEntryInfo) {
      updateAnyData("ZipEntryInfo", zipEntryInfo);
   }

   protected void updateAnyData(String whichData, Object dataObject) {
      try {
         this.progressListenerGUI.updateData(dataObject);
      } catch (Exception e) {
         logger.error("Exception in ProgressListenerGUI#updateData ({})", whichData, e);
      }
   }

   @Override
   public void updateTaskState(TASK_STATE taskState) {
      try {
         this.progressListenerGUI.updateTaskState(taskState);
      } catch (Exception e) {
         logger.error("Exception in ProgressListenerGUI#updateTaskState", e);
      }
   }

   @Override
   public boolean isCanceled() {
      return progressListenerGUI.isCanceled();
   }

}
