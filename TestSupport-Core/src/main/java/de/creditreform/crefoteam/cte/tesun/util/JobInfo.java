package de.creditreform.crefoteam.cte.tesun.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/** Literal-Port aus {@code testsupport_client.tesun_util}. */
public class JobInfo {

    private final String jvmName;
    private String jobName;
    private final List<String> processNamesList = new ArrayList<>();
    private Properties queryParameters;

    public JobInfo(String jvmName, String jobName, Properties queryParameters, String processName) {
        this.jvmName = jvmName;
        this.jobName = jobName;
        this.queryParameters = queryParameters;
        if (!processName.isBlank()) {
            processNamesList.add(processName);
        }
    }

    public JobInfo(String propValue) {
        String[] split1 = propValue.split(";");
        if (split1.length < 3) {
            throw new IllegalArgumentException("Job-Info in der Konfiguration hat falsches Format!\n" + propValue);
        }
        this.jvmName = split1[0];
        this.jobName = split1[1];
        processNamesList.addAll(Arrays.asList(split1[2].split(",")));
    }

    public JobInfo(TestCustomer testCustomer) {
        this.jvmName = testCustomer.getJvmName();
        this.jobName = testCustomer.getExportJobName();
        processNamesList.add(testCustomer.getProcessIdentifier());
    }

    public String getJobName() { return jobName; }
    public String getJvmName() { return jvmName; }
    public List<String> getProcessNamesList() { return processNamesList; }

    public Properties getQueryParameters() {
        if (queryParameters == null) {
            queryParameters = new Properties();
        }
        return queryParameters;
    }

    public JobInfo setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }
}
