package de.creditreform.crefoteam.cte.jvmclient;

/** Port aus {@code batch_jvm_client.jvm_client.domain.JvmJobExecutionInfo}. */
public final class JvmJobExecutionInfo {

    private String id;
    private String jobId;
    private String jobName;
    private String status;
    private String exitCode;
    private String running;
    private String startDate;
    private String endDate;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }

    public String getJobName() { return jobName; }
    public void setJobName(String jobName) { this.jobName = jobName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getExitCode() { return exitCode; }
    public void setExitCode(String exitCode) { this.exitCode = exitCode; }

    public String getRunning() { return running; }
    public void setRunning(String running) { this.running = running; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    @Override
    public String toString() {
        return id + "|" + jobId + " [" + status + "/" + exitCode + "]";
    }
}
