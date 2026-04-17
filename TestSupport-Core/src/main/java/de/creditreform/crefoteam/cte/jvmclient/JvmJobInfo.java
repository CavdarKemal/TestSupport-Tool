package de.creditreform.crefoteam.cte.jvmclient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** Port aus {@code batch_jvm_client.jvm_client.domain.JvmJobInfo}. */
public final class JvmJobInfo {

    private String jobName;
    private int executionCount;
    private List<JvmJobExecutionInfo> jobExecutionsList = new ArrayList<>();
    private String jvmName;
    private String processId;
    private boolean activated;

    public String getJobName() { return jobName; }
    public void setJobName(String jobName) { this.jobName = jobName; }

    public int getExecutionCount() { return executionCount; }
    public void setExecutionCount(int executionCount) { this.executionCount = executionCount; }

    public List<JvmJobExecutionInfo> getJobExecutionsList() { return jobExecutionsList; }
    public void setJobExecutionsList(List<JvmJobExecutionInfo> jobExecutionsList) { this.jobExecutionsList = jobExecutionsList; }

    public String getJvmName() { return jvmName; }
    public void setJvmName(String jvmName) { this.jvmName = jvmName; }

    public String getProcessId() { return processId; }
    public void setProcessId(String processId) { this.processId = processId; }

    public boolean isActivated() { return activated; }
    public void setActivated(boolean activated) { this.activated = activated; }

    /**
     * Parst einen ISO-8601-Datum-Zeit-String und gibt ihn formatiert zurück.
     * Bei Parsing-Fehler wird der Original-String zurückgegeben.
     */
    public static String formatDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return dateTimeStr;
        }
        try {
            LocalDateTime ldt = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_DATE_TIME);
            return ldt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        } catch (Exception e) {
            return dateTimeStr;
        }
    }
}
