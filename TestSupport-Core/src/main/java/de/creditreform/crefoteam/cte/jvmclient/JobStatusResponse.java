package de.creditreform.crefoteam.cte.jvmclient;

/** Port aus {@code batch_jvm_client.jvm_client.domain.JobStatusResponse}. */
public final class JobStatusResponse {

    private String running;
    private String status;
    private String exitCode;

    public String getRunning() { return running; }
    public void setRunning(String running) { this.running = running; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getExitCode() { return exitCode; }
    public void setExitCode(String exitCode) { this.exitCode = exitCode; }
}
