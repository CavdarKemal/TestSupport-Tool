package de.creditreform.crefoteam.cte.testsupporttool.logsearch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class NormalLogFile extends AbstractSearchableLogFile {
    public NormalLogFile(String logFileName) throws FileNotFoundException {
        super(logFileName);
    }

    @Override
    protected BufferedReader getBufferedReaderForLogFile() throws Exception {
        FileInputStream fis = new FileInputStream(getLogFile());
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        return new BufferedReader(isr);
    }

    @Override
    public LogSearchResults search(SearchCriteria searchCriteria) throws IOException {
        return new LogSearchResults();
    }
}
