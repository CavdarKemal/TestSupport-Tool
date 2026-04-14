package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.statemachine.ProcessContext;
import de.creditreform.crefoteam.cte.statemachine.Step;
import de.creditreform.crefoteam.cte.statemachine.StepResult;
import de.creditreform.crefoteam.cte.testsupporttool.config.EnvironmentConfig;
import de.creditreform.crefoteam.cte.testsupporttool.config.TestSupportConstants;
import de.creditreform.crefoteam.cte.testsupporttool.rest.JobExecutionInfo;
import de.creditreform.crefoteam.cte.testsupporttool.rest.TesunRestService;
import org.apache.log4j.Logger;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * Demo-Pendant zu {@code UserTaskWaitForCtImport}: pollt den REST-Service,
 * bis der Job einen {@code COMPLETED}-Status mit
 * {@code lastCompletionDate >= startedAt} liefert oder ein Timeout greift.
 *
 * <p>Demonstriert das Handler-Muster „Polling auf externes System". Cancel
 * wird zwischen den Polls geprüft, sodass der Prozess auch während eines
 * langen Waits sauber abbricht.
 */
public final class WaitForCtImportHandler implements Step {

    private static final Logger LOG = Logger.getLogger(WaitForCtImportHandler.class);

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
        if (Boolean.TRUE.equals(context.get(TestSupportConstants.VAR_DEMO_MODE, Boolean.class))) {
            LOG.info("WaitForCtImport [Demo-Mode]: simuliere sofortiges COMPLETED.");
            return StepResult.NEXT;
        }

        Instant startedAt = context.get(StartCtImportHandler.VAR_CT_IMPORT_STARTED_AT, Instant.class);
        if (startedAt == null) {
            throw new IllegalStateException(
                    "Variable '" + StartCtImportHandler.VAR_CT_IMPORT_STARTED_AT + "' fehlt im Context.");
        }

        long deadline = System.currentTimeMillis() + environmentConfig.getJobTimeoutMillis();
        long pollMillis = environmentConfig.getJobStatusPollingMillis();

        LOG.info("WaitForCtImport: pollt '" + processIdentifier + "' (Start=" + startedAt + ")");

        while (System.currentTimeMillis() < deadline) {
            if (context.isCancelled()) {
                LOG.warn("  Cancel während Polling — breche ab.");
                return StepResult.ABORT;
            }

            JobExecutionInfo info = restService.getJobExecutionInfo(processIdentifier);
            if (info.getJobStatus() == null) {
                throw new IllegalStateException("REST lieferte keinen Job-Status.");
            }
            Instant lastCompletion = info.getLastCompletionDate();
            if (lastCompletion != null && !lastCompletion.isBefore(startedAt)) {
                if (TestSupportConstants.JOB_STATUS_COMPLETED.equals(info.getJobStatus())) {
                    LOG.info("  Prozess '" + processIdentifier + "' wurde beendet (" + lastCompletion + ")");
                    return StepResult.NEXT;
                }
                throw new IllegalStateException(
                        "Prozess '" + processIdentifier + "' endete mit Status " + info.getJobStatus());
            }
            Thread.sleep(pollMillis);
        }

        throw new TimeoutException("Timeout beim Warten auf Prozess '" + processIdentifier + "'");
    }

    @Override
    public String name() {
        return "WaitForCtImport";
    }
}
