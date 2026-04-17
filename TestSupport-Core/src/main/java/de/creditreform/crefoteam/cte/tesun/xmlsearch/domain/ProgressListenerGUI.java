package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import java.util.List;

/**
 * Schnittstelle für die Rückmeldung des Fortschrittes in der Verarbeitung an die GUI
 */
public interface ProgressListenerGUI
{

  void updateProgress(List<Object> chunks);

  void updateData(Object dataObject);

  void updateTaskState(ProgressListenerIF.TASK_STATE taskState);

  boolean isCanceled();

}
