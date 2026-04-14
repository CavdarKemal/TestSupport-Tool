package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.statemachine.ProcessContext;
import de.creditreform.crefoteam.cte.statemachine.Step;
import de.creditreform.crefoteam.cte.statemachine.StepResult;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;
import de.creditreform.crefoteam.cte.testsupporttool.rest.TesunRestService;

import java.time.Instant;
import java.util.Objects;

/**
 * Demo-Pendant zu {@code UserTaskStartCtImport}: stößt das Starten eines
 * JVM-Jobs an und merkt sich den Startzeitpunkt im Context unter dem
 * Key {@link TestSupportClientKonstanten#CT_IMPORT_STARTET_AT}.
 */
public final class StartCtImportHandler implements Step {

    private final TesunRestService restService;
    private final String jvmName;
    private final String jobName;

    public StartCtImportHandler(TesunRestService restService, String jvmName, String jobName) {
        this.restService = Objects.requireNonNull(restService, "restService");
        this.jvmName = Objects.requireNonNull(jvmName, "jvmName");
        this.jobName = Objects.requireNonNull(jobName, "jobName");
    }

    @Override
    public StepResult execute(ProcessContext context) throws Exception {
        if (Boolean.TRUE.equals(context.get(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE, Boolean.class))) {
            TimelineLogger.info(StartCtImportHandler.class,
                    "StartCtImport [Demo-Mode]: würde Job '{}' auf JVM '{}' starten.", jobName, jvmName);
            context.put(TestSupportClientKonstanten.CT_IMPORT_STARTET_AT, Instant.now());
            return StepResult.NEXT;
        }

        TimelineLogger.info(StartCtImportHandler.class,
                "StartCtImport: starte JVM-Job '{}' auf '{}'", jobName, jvmName);
        try (TimelineLogger.Action a = TimelineLogger.action("startJob", jobName)) {
            Instant startedAt = Instant.now();
            restService.startJob(jvmName, jobName);
            context.put(TestSupportClientKonstanten.CT_IMPORT_STARTET_AT, startedAt);
            a.result("startedAt=" + startedAt);
        }
        return StepResult.NEXT;
    }

    @Override
    public String name() {
        return "StartCtImport";
    }
}
