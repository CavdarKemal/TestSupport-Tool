package de.creditreform.crefoteam.cte.testsupporttool.logsearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

public class ZippedLogFile extends AbstractSearchableLogFile {
    public ZippedLogFile(String logFileName) throws FileNotFoundException {
        super(logFileName);
    }

    @Override
    protected BufferedReader getBufferedReaderForLogFile() throws Exception {
        File logFile = getLogFile();
        InputStream theInputStream;
        if (logFile.getName().endsWith(".gz")) {
            theInputStream = new GZIPInputStream(new FileInputStream(logFile));
        } else if (logFile.getName().endsWith(".zip")) {
            theInputStream = new ZipInputStream(new FileInputStream(logFile));
            ((ZipInputStream) theInputStream).getNextEntry();
        } else {
            throw new IllegalArgumentException("Ungültige ZIP-Date!" + logFile.getPath());
        }
        InputStreamReader isr = new InputStreamReader(theInputStream, "UTF-8");
        return new BufferedReader(isr);
    }

    @Override
    public LogSearchResults search(SearchCriteria searchCriteria) throws IOException {
        return new LogSearchResults();
    }
}
