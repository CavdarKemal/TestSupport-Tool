package de.creditreform.crefoteam.cte.testsupporttool.logsearch;

public interface IWorkerListener {
    enum LOG_LEVEL { DEBUG, INFO, WARN, ERROR }
    enum TASK_STATE { IDLE, RUNNING, CANCELLED, ABORTED, PAUSED, DONE }

    void updateProgress(Object dataObject, int progressStep);

    void updateData(Object dataObject);

    void updateTaskState(TASK_STATE taskState);

    boolean isCanceled();
}
