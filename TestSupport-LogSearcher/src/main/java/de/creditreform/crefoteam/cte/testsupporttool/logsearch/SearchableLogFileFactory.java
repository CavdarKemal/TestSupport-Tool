package de.creditreform.crefoteam.cte.testsupporttool.logsearch;

import java.io.FileNotFoundException;

public class SearchableLogFileFactory {
    public static SearchableLogFile createInstanceFor(String logFileName) throws FileNotFoundException {
        if (logFileName.endsWith(".log")) {
            return new NormalLogFile(logFileName);
        }
        if (logFileName.endsWith(".gz") || logFileName.endsWith(".zip")) {
            return new ZippedLogFile(logFileName);
        }
        throw new UnsupportedOperationException(String.format("Typ der Log-Datei '%s' unbekannt!", logFileName));
    }
}
