package de.creditreform.crefoteam.cte.testsupporttool.logsearch;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import de.creditreform.crefoteam.cte.tesun.util.TesunDateUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NormalLogFileTest {

    private SearchableLogFile getSearchableLogFile(String testName, String fileName) throws URISyntaxException, FileNotFoundException {
        String logFileName = NormalLogFileTest.class.getResource(fileName).toURI().getPath();
        SearchableLogFile logFile = SearchableLogFileFactory.createInstanceFor(logFileName);
        logFile.addWorkerListener(new CollectingListener(testName, fileName));
        return logFile;
    }

    @Test
    void normalLogFile_forLogTypeOnly() throws Exception {
        SearchableLogFile logFile = getSearchableLogFile("forLogTypeOnly", "/all_flux_out.log");
        SearchCriteria searchCriteriaALL = new SearchCriteria(LogEntry.ENTRY_TYPE.ALL);
        List<LogEntry> logEntries = logFile.getLogEntries(searchCriteriaALL);
        LogEntryTest.checkLogEntries(logEntries, 69, searchCriteriaALL);

        SearchCriteria searchCriteriaINFO = new SearchCriteria(LogEntry.ENTRY_TYPE.INFO);
        logEntries = logFile.getLogEntries(searchCriteriaINFO);
        LogEntryTest.checkLogEntries(logEntries, 60, searchCriteriaINFO);

        SearchCriteria searchCriteriaWARN = new SearchCriteria(LogEntry.ENTRY_TYPE.WARN);
        logEntries = logFile.getLogEntries(searchCriteriaWARN);
        LogEntryTest.checkLogEntries(logEntries, 6, searchCriteriaWARN);

        SearchCriteria searchCriteriaERROR = new SearchCriteria(LogEntry.ENTRY_TYPE.ERROR);
        logEntries = logFile.getLogEntries(searchCriteriaERROR);
        LogEntryTest.checkLogEntries(logEntries, 3, searchCriteriaERROR);
    }

    @Test
    void normalLogFile_forLogDateOnly() throws Exception {
        SearchableLogFile logFile = getSearchableLogFile("forLogDateOnly", "/all_flux_out.log");

        SearchCriteria searchCriteriaFrom = new SearchCriteria();
        Date logDateFrom = TesunDateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS.parse("2015-06-22 11:01:40");
        searchCriteriaFrom.setLogDateFrom(logDateFrom);
        List<LogEntry> logEntries = logFile.getLogEntries(searchCriteriaFrom);
        LogEntryTest.checkLogEntries(logEntries, 68, searchCriteriaFrom);

        logDateFrom = TesunDateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS.parse("2015-06-22 11:02:52");
        searchCriteriaFrom.setLogDateFrom(logDateFrom);
        Date logDateTo = TesunDateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS.parse("2015-06-22 11:56:12");
        searchCriteriaFrom.setLogDateTo(logDateTo);
        logEntries = logFile.getLogEntries(searchCriteriaFrom);
        LogEntryTest.checkLogEntries(logEntries, 29, searchCriteriaFrom);

        searchCriteriaFrom.setLogDateFrom(null);
        logDateTo = TesunDateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS.parse("2015-06-22 11:01:47");
        searchCriteriaFrom.setLogDateTo(logDateTo);
        logEntries = logFile.getLogEntries(searchCriteriaFrom);
        LogEntryTest.checkLogEntries(logEntries, 3, searchCriteriaFrom);
    }

    @Test
    void normalLogFile_forLogPackageOnly() throws Exception {
        SearchableLogFile logFile = getSearchableLogFile("forLogPackageOnly", "/all_flux_out.log");

        SearchCriteria searchCriteriaFrom = new SearchCriteria();
        searchCriteriaFrom.setLogPackage("cte_flux_monitoring.ctecexport.xmlexport");
        List<LogEntry> logEntries = logFile.getLogEntries(searchCriteriaFrom);
        LogEntryTest.checkLogEntries(logEntries, 7, searchCriteriaFrom);

        searchCriteriaFrom.setLogPackage("cte_flux_monitoring.");
        logEntries = logFile.getLogEntries(searchCriteriaFrom);
        LogEntryTest.checkLogEntries(logEntries, 7, searchCriteriaFrom);

        searchCriteriaFrom.setLogPackage("com.mchange");
        logEntries = logFile.getLogEntries(searchCriteriaFrom);
        LogEntryTest.checkLogEntries(logEntries, 5, searchCriteriaFrom);

        searchCriteriaFrom.setLogPackage("de.creditreform.crefoteam.fluxsupport");
        logEntries = logFile.getLogEntries(searchCriteriaFrom);
        LogEntryTest.checkLogEntries(logEntries, 27, searchCriteriaFrom);

        searchCriteriaFrom.setLogPackage("de.creditreform.crefoteam");
        logEntries = logFile.getLogEntries(searchCriteriaFrom);
        LogEntryTest.checkLogEntries(logEntries, 56, searchCriteriaFrom);
    }

    @Test
    void normalLogFile_forErrorsInPackage() throws Exception {
        SearchableLogFile logFile = getSearchableLogFile("forErrorsInPackage", "/all_flux_out.log");

        SearchCriteria searchCriteriaERROR = new SearchCriteria(LogEntry.ENTRY_TYPE.ERROR);
        searchCriteriaERROR.setLogPackage("de.creditreform.crefoteam.ctertnexport.rtnexport");
        List<LogEntry> logEntries = logFile.getLogEntries(searchCriteriaERROR);
        LogEntryTest.checkLogEntries(logEntries, 3, searchCriteriaERROR);
    }

    @Test
    void normalLogFile_forWarningsInPackage() throws Exception {
        SearchableLogFile logFile = getSearchableLogFile("forWarningsInPackage", "/all_flux_out.log");

        SearchCriteria searchCriteriaWARN = new SearchCriteria(LogEntry.ENTRY_TYPE.WARN);
        searchCriteriaWARN.setLogPackage("cte_flux_monitoring.ctecexport");
        List<LogEntry> logEntries = logFile.getLogEntries(searchCriteriaWARN);
        LogEntryTest.checkLogEntries(logEntries, 1, searchCriteriaWARN);
    }

    @Test
    void normalLogFile_forInfoFromDateInPackage() throws Exception {
        SearchableLogFile logFile = getSearchableLogFile("forInfoFromDateInPackage", "/all_flux_out.log");

        SearchCriteria searchCriteriaINFO = new SearchCriteria(LogEntry.ENTRY_TYPE.INFO);
        Date logDateFrom = TesunDateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS.parse("2015-06-22 11:56:13");
        searchCriteriaINFO.setLogDateFrom(logDateFrom);
        searchCriteriaINFO.setLogPackage("com.mchange.v2.c3p0");
        List<LogEntry> logEntries = logFile.getLogEntries(searchCriteriaINFO);
        LogEntryTest.checkLogEntries(logEntries, 4, searchCriteriaINFO);

        logDateFrom = TesunDateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS.parse("2015-06-22 11:02:52");
        searchCriteriaINFO.setLogDateFrom(logDateFrom);
        Date logDateTo = TesunDateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS.parse("2015-06-22 11:56:12");
        searchCriteriaINFO.setLogDateTo(logDateTo);
        searchCriteriaINFO.setLogPackage("de.creditreform.crefoteam");
        logEntries = logFile.getLogEntries(searchCriteriaINFO);
        LogEntryTest.checkLogEntries(logEntries, 18, searchCriteriaINFO);
    }

    @Test
    void normalLogFile_forLogInfoOnly() throws Exception {
        SearchableLogFile logFile = getSearchableLogFile("forLogInfoOnly", "/all_flux_out.log");

        SearchCriteria searchCriteriaALL = new SearchCriteria();
        searchCriteriaALL.setLogInfo("durch Post-Marshalling-Filter unterdrückt");
        List<LogEntry> logEntries = logFile.getLogEntries(searchCriteriaALL);
        LogEntryTest.checkLogEntries(logEntries, 7, searchCriteriaALL);
    }

    @Test
    void normalLogFile_forLogErrorWithRuntimeException() throws Exception {
        SearchableLogFile logFile = getSearchableLogFile("forLogErrorWithRuntimeException", "/all_flux_out.log");

        SearchCriteria searchCriteriaERROR = new SearchCriteria(LogEntry.ENTRY_TYPE.ERROR);
        searchCriteriaERROR.setLogInfo("RuntimeException beim XML-Export der Crefo");
        List<LogEntry> logEntries = logFile.getLogEntries(searchCriteriaERROR);
        LogEntryTest.checkLogEntries(logEntries, 1, searchCriteriaERROR);

        LogEntry logEntry0 = logEntries.get(0);
        Matcher matcher = logEntry0.searchInInfosList("Crefo (\\d{10})");
        assertThat(matcher).isNotNull();
        Long crefo = Long.valueOf(matcher.group(1));
        assertThat(crefo).isEqualTo(6250301832L);
    }

    private static class CollectingListener implements IWorkerListener {
        final String testName;
        final String fileName;

        CollectingListener(String testName, String fileName) {
            this.testName = testName;
            this.fileName = fileName;
        }

        @Override public void updateProgress(Object dataObject, int progressStep) { }
        @Override public void updateData(Object dataObject) { }
        @Override public void updateTaskState(TASK_STATE taskState) { }
        @Override public boolean isCanceled() { return false; }
    }
}
