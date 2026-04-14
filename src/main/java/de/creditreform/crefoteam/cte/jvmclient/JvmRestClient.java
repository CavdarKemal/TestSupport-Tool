package de.creditreform.crefoteam.cte.jvmclient;

import java.util.Properties;

/**
 * Minimal-Ersatz für den {@code JvmRestClient} aus {@code batch_jvm_client}.
 * Definiert genau die eine Methode, die {@link de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractJvmJobStarter}
 * aufruft.
 */
public interface JvmRestClient {

    JobStartResponse startJob(String jobName, Properties queryParameters) throws Exception;
}
