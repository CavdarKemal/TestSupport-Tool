package de.creditreform.crefoteam.cte.testsupporttool.logsearch;

import java.util.Date;
import java.util.List;

import de.creditreform.crefoteam.cte.testsupporttool.logsearch.LogEntry.ENTRY_TYPE;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Hilfs-Assertions — kein eigener @Test, wird von {@link NormalLogFileTest}
 * aufgerufen. Muss ausserhalb als public-Helper stehen (package-visible haette
 * auch gereicht; wir behalten den Original-Scope).
 */
public class LogEntryTest {

    public static void checkLogEntries(List<LogEntry> logEntries, int expectedSize, SearchCriteria expectedSearchCriteria) {
        assertThat(logEntries).isNotNull();
        AbstractSearchableLogFile.dumpLogEntries(logEntries);
        assertThat(logEntries).hasSize(expectedSize);
        for (LogEntry logEntry : logEntries) {
            ENTRY_TYPE expectedLogEntryType = expectedSearchCriteria.getLogEntryType();
            if (!expectedLogEntryType.equals(ENTRY_TYPE.ALL)) {
                assertThat(logEntry.getType()).isEqualTo(expectedLogEntryType);
            }
            Date expectedLogDateFrom = expectedSearchCriteria.getLogDateFrom();
            if (expectedLogDateFrom != null) {
                assertThat(logEntry.getLogDate()).isAfterOrEqualTo(expectedLogDateFrom);
            }
            Date expectedLogDateTo = expectedSearchCriteria.getLogDateTo();
            if (expectedLogDateTo != null) {
                assertThat(logEntry.getLogDate()).isBeforeOrEqualTo(expectedLogDateTo);
            }
            String expectedLogPackage = expectedSearchCriteria.getLogPackage();
            if (expectedLogPackage != null) {
                assertThat(logEntry.getPackg()).startsWith(expectedLogPackage);
            }
            String expectedLogInfo = expectedSearchCriteria.getLogInfo();
            if (expectedLogInfo != null) {
                assertThat(logEntry.getInfoList().get(0)).contains(expectedLogInfo);
            }
        }
    }
}
