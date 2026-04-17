package de.creditreform.crefoteam.cte.tesun.rest.dto;

import java.util.Collections;
import java.util.List;

public class TesunPendingJobs {
    private final List<TesunPendingJob> jobs;

    public TesunPendingJobs(List<TesunPendingJob> jobs) {
        this.jobs = jobs == null ? Collections.emptyList() : jobs;
    }

    public List<TesunPendingJob> getJobs() { return jobs; }
}
