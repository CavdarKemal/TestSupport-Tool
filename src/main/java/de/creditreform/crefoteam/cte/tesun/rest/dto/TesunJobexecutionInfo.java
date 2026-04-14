package de.creditreform.crefoteam.cte.tesun.rest.dto;

import java.util.Calendar;

/**
 * DTO für eine Job-Execution-Information. Vereinfachter Nachbau des
 * gleichnamigen JAXB-Typs aus {@code restservices.tesun.xmlbinding.jobexecution}
 * (extern, aus dem CTE-Maven-Repo). Feldnamen identisch, damit Handler-Code
 * unverändert übernommen werden kann.
 */
public final class TesunJobexecutionInfo {

    private String jobStatus;
    private Calendar lastStartDate;
    private Calendar lastCompletitionDate;

    public String getJobStatus() { return jobStatus; }
    public void setJobStatus(String jobStatus) { this.jobStatus = jobStatus; }

    public Calendar getLastStartDate() { return lastStartDate; }
    public void setLastStartDate(Calendar lastStartDate) { this.lastStartDate = lastStartDate; }

    public Calendar getLastCompletitionDate() { return lastCompletitionDate; }
    public void setLastCompletitionDate(Calendar lastCompletitionDate) { this.lastCompletitionDate = lastCompletitionDate; }
}
