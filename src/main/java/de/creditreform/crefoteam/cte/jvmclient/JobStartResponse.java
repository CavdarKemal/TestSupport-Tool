package de.creditreform.crefoteam.cte.jvmclient;

/** Minimal-Port aus {@code batch_jvm_client.jvm_client.domain}. */
public final class JobStartResponse {

    private String id;
    private String jobId;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }
}
