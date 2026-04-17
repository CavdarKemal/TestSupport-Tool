package de.creditreform.crefoteam.cte.testsupporttool.logsearch;

import java.util.Date;

import de.creditreform.crefoteam.cte.testsupporttool.logsearch.LogEntry.ENTRY_TYPE;

public class SearchCriteria {
    private ENTRY_TYPE logEntryType = LogEntry.ENTRY_TYPE.ALL;
    private Date logDateFrom;
    private Date logDateTo;
    private String logPackage;
    private String logInfo;

    public SearchCriteria() {
        this(LogEntry.ENTRY_TYPE.ALL);
    }

    public SearchCriteria(ENTRY_TYPE logEntryType) {
        this.logEntryType = logEntryType;
    }

    public ENTRY_TYPE getLogEntryType() { return logEntryType; }
    public void setLogEntryType(ENTRY_TYPE logEntryType) { this.logEntryType = logEntryType; }

    public Date getLogDateFrom() { return logDateFrom; }
    public void setLogDateFrom(Date logDateFrom) { this.logDateFrom = logDateFrom; }

    public Date getLogDateTo() { return logDateTo; }
    public void setLogDateTo(Date logDateTo) { this.logDateTo = logDateTo; }

    public String getLogPackage() { return logPackage; }
    public void setLogPackage(String logPackage) { this.logPackage = logPackage; }

    public String getLogInfo() { return logInfo; }
    public void setLogInfo(String logInfo) { this.logInfo = logInfo; }
}
