package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.handler.LogInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipEntryInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipFileInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipSearcResult;

import java.util.List;

/**
 * Schnittstelle für die Rückmeldung des Fortschrittes in der Verarbeitung
 */
public interface ProgressListenerIF
{
  enum LOG_LEVEL { DEBUG, INFO, WARN, ERROR }
  enum TASK_STATE { IDLE, RUNNING, CANCELLED, ABORTED, PAUSED, DONE }

  void updateProgress(List<Object> chunks);

  void updateData( Object dataObject );
  void updateData(LogInfo logInfo);

  void updateData(IZipSearcResult zipSearcResult);

  void updateData(IZipFileInfo zipFileInfo);

  void updateData(IZipEntryInfo zipEntryInfo);

  void updateTaskState( TASK_STATE taskState );

  boolean isCanceled();

}
