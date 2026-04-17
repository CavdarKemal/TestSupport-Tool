package de.creditreform.crefoteam.cte.testsupporttool.logsearch;

import java.io.IOException;
import java.util.List;

public interface SearchableLogFile {
    void addWorkerListener(IWorkerListener workerListener);

    List<LogEntry> getLogEntries(SearchCriteria searchCriteria) throws Exception;

    LogSearchResults search(SearchCriteria searchCriteria) throws IOException;
}
