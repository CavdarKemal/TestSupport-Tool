package de.creditreform.crefoteam.cte.jvmclient;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Minimal-Ersatz für den {@code JvmRestClient} aus {@code batch_jvm_client}.
 * Definiert die Methoden, die von den GUI-Views und Handlern aufgerufen werden.
 */
public interface JvmRestClient {

    JobStartResponse startJob(String jobName, Properties queryParameters) throws Exception;

    List<JvmJobInfo> readJvmJobInfos(String jvmName) throws IOException, InterruptedException;

    List<JvmJobExecutionInfo> readJobExecutions(String jobName) throws IOException, InterruptedException;

    void abortJob(String jobName, String jobId) throws IOException, InterruptedException;

    JobStatusResponse getJobStatus(String jobName, String processId) throws IOException, InterruptedException;
}
