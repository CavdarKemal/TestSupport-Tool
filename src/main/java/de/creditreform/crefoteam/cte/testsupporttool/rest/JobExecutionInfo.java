package de.creditreform.crefoteam.cte.testsupporttool.rest;

import java.time.Instant;

/**
 * Reduziertes Pendant zu {@code TesunJobexecutionInfo} aus dem
 * Original-Projekt — nur die Felder, die der Spike braucht.
 */
public final class JobExecutionInfo {

    private final String jobStatus;
    private final Instant lastStartDate;
    private final Instant lastCompletionDate;

    public JobExecutionInfo(String jobStatus, Instant lastStartDate, Instant lastCompletionDate) {
        this.jobStatus = jobStatus;
        this.lastStartDate = lastStartDate;
        this.lastCompletionDate = lastCompletionDate;
    }

    public String getJobStatus() { return jobStatus; }

    public Instant getLastStartDate() { return lastStartDate; }

    public Instant getLastCompletionDate() { return lastCompletionDate; }
}
