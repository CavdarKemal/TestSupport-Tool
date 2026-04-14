package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.statemachine.ProcessContext;
import de.creditreform.crefoteam.cte.statemachine.Step;
import de.creditreform.crefoteam.cte.statemachine.StepResult;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;
import de.creditreform.crefoteam.cte.testsupporttool.rest.JobExecutionInfo;
import de.creditreform.crefoteam.cte.testsupporttool.rest.TesunRestService;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * Demo-Pendant zu {@code UserTaskWaitForCtImport}: pollt den REST-Service,
 * bis der Job einen {@code COMPLETED}-Status mit
 * {@code lastCompletionDate >= startedAt} liefert oder ein Timeout greift.
 * Timeout und Polling-Intervall kommen aus {@link EnvironmentConfig}.
 */
public final class WaitForCtImportHandler implements Step {

    private static final String JOB_STATUS_COMPLETED = "COMPLETED";

    private final TesunRestService restService;
    private final EnvironmentConfig environmentConfig;
    private final String processIdentifier;

    public WaitForCtImportHandler(TesunRestService restService,
                                  EnvironmentConfig environmentConfig,
                                  String processIdentifier) {
        this.restService = Objects.requireNonNull(restService, "restService");
        this.environmentConfig = Objects.requireNonNull(environmentConfig, "environmentConfig");
        this.processIdentifier = Objects.requireNonNull(processIdentifier, "processIdentifier");
    }

    @Override
    public StepResult execute(ProcessContext context) throws Exception {
        if (Boolean.TRUE.equals(context.get(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE, Boolean.class))) {
            TimelineLogger.info(WaitForCtImportHandler.class,
                    "WaitForCtImport [Demo-Mode]: simuliere sofortiges COMPLETED.");
            return StepResult.NEXT;
        }

        Instant startedAt = context.get(TestSupportClientKonstanten.CT_IMPORT_STARTET_AT, Instant.class);
        if (startedAt == null) {
            throw new IllegalStateException(
                    "Variable '" + TestSupportClientKonstanten.CT_IMPORT_STARTET_AT + "' fehlt im Context.");
        }

        long deadline = System.currentTimeMillis() + timeoutMillis();
        long pollMillis = pollingMillis();

        TimelineLogger.info(WaitForCtImportHandler.class,
                "WaitForCtImport: pollt '{}' (Start={})", processIdentifier, startedAt);

        try (TimelineLogger.Action a = TimelineLogger.action("waitForJob", processIdentifier)) {
            while (System.currentTimeMillis() < deadline) {
                if (context.isCancelled()) {
                    TimelineLogger.warn(WaitForCtImportHandler.class,
                            "  Cancel während Polling — breche ab.");
                    a.result("ABORT");
                    return StepResult.ABORT;
                }

                JobExecutionInfo info = restService.getJobExecutionInfo(processIdentifier);
                if (info.getJobStatus() == null) {
                    throw new IllegalStateException("REST lieferte keinen Job-Status.");
                }
                Instant lastCompletion = info.getLastCompletionDate();
                if (lastCompletion != null && !lastCompletion.isBefore(startedAt)) {
                    if (JOB_STATUS_COMPLETED.equals(info.getJobStatus())) {
                        TimelineLogger.info(WaitForCtImportHandler.class,
                                "  Prozess '{}' wurde beendet ({})", processIdentifier, lastCompletion);
                        a.result("COMPLETED");
                        return StepResult.NEXT;
                    }
                    throw new IllegalStateException(
                            "Prozess '" + processIdentifier + "' endete mit Status " + info.getJobStatus());
                }
                Thread.sleep(pollMillis);
            }
            a.result("TIMEOUT");
            throw new TimeoutException("Timeout beim Warten auf Prozess '" + processIdentifier + "'");
        }
    }

    private long pollingMillis() {
        try {
            return environmentConfig.getMillisForJobStatusQuerySleepTime();
        } catch (PropertiesException ex) {
            return 2_000L;
        }
    }

    private long timeoutMillis() {
        try {
            return environmentConfig.getMillisForImportCycleTimeOut();
        } catch (PropertiesException ex) {
            return 1_800_000L;
        }
    }

    @Override
    public String name() {
        return "WaitForCtImport";
    }
}
