package de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch;

import de.creditreform.crefoteam.cte.testsupporttool.logsearch.IWorkerListener;
import de.creditreform.crefoteam.cte.testsupporttool.logsearch.LogEntry;
import de.creditreform.crefoteam.cte.testsupporttool.logsearch.SearchCriteria;
import de.creditreform.crefoteam.cte.testsupporttool.logsearch.SearchableLogFile;

import javax.swing.SwingWorker;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LogSearchSwingWorker extends SwingWorker<Integer, LogEntry> {
    private final IWorkerListener workerListener;
    private final List<SearchableLogFile> logFiles;
    private final SearchCriteria searchCriteria;
    private int nCount = 0;
    private boolean isPaused;
    private SearchableLogFile currSearchableLogFile;

    public LogSearchSwingWorker(IWorkerListener workerListener, List<SearchableLogFile> logFiles, SearchCriteria searchCriteria) {
        this.workerListener = workerListener;
        this.logFiles = logFiles;
        this.searchCriteria = searchCriteria;
        this.isPaused = false;
        nCount = logFiles.size();
    }

    public void setPaused(boolean isPaused) { this.isPaused = isPaused; }
    public boolean isPaused() { return isPaused; }

    @Override
    protected Integer doInBackground() throws Exception {
        workerListener.updateProgress(this, 0);
        workerListener.updateTaskState(IWorkerListener.TASK_STATE.RUNNING);
        int dataIndex = 0;
        Iterator<SearchableLogFile> iterator = logFiles.iterator();
        while (!isCancelled() && iterator.hasNext()) {
            currSearchableLogFile = iterator.next();
            currSearchableLogFile.addWorkerListener(workerListener);

            while (isPaused() && !isCancelled()) {
                workerListener.updateTaskState(IWorkerListener.TASK_STATE.PAUSED);
                Thread.sleep(500);
            }
            try {
                List<LogEntry> logEntries = currSearchableLogFile.getLogEntries(searchCriteria);
                workerListener.updateProgress(this, (dataIndex * 100 / logFiles.size()));
                process(logEntries);
                dataIndex++;
                Thread.yield();
            } catch (Exception ex) {
                new RuntimeException(ex);
            }
        }
        return logFiles.size();
    }

    @Override
    protected void process(List<LogEntry> chunks) {
        workerListener.updateData(currSearchableLogFile);
    }

    @Override
    protected void done() {
        if (isCancelled()) {
            workerListener.updateTaskState(IWorkerListener.TASK_STATE.CANCELLED);
        } else {
            try {
                Integer numElements = get();
                workerListener.updateProgress(this, 100);
                if (numElements == 0) {
                    workerListener.updateTaskState(IWorkerListener.TASK_STATE.ABORTED);
                }
            } catch (InterruptedException ex) {
                workerListener.updateTaskState(IWorkerListener.TASK_STATE.CANCELLED);
            } catch (ExecutionException ex) {
                workerListener.updateTaskState(IWorkerListener.TASK_STATE.ABORTED);
            }
            workerListener.updateTaskState(IWorkerListener.TASK_STATE.DONE);
        }
    }
}
