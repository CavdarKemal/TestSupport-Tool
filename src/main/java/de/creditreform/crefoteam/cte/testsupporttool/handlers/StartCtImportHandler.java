package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.statemachine.ProcessContext;
import de.creditreform.crefoteam.cte.statemachine.Step;
import de.creditreform.crefoteam.cte.statemachine.StepResult;
import de.creditreform.crefoteam.cte.testsupporttool.config.TestSupportConstants;
import de.creditreform.crefoteam.cte.testsupporttool.rest.TesunRestService;
import org.apache.log4j.Logger;

import java.time.Instant;
import java.util.Objects;

/**
 * Demo-Pendant zu {@code UserTaskStartCtImport}: stößt das Starten eines
 * JVM-Jobs an und merkt sich den Startzeitpunkt im Context.
 *
 * <p>Demonstriert das Handler-Muster „REST-Aufruf zum Starten externer
 * Arbeit". Der echte Handler ergänzt Retry-Logik bei
 * Connection-Problemen — der Spike verlässt sich auf den
 * Engine-Retry-Mechanismus per {@code ProcessListener.askForRetry}.
 */
public final class StartCtImportHandler implements Step {

    private static final Logger LOG = Logger.getLogger(StartCtImportHandler.class);

    /** Variable, in die der Startzeitpunkt geschrieben wird. */
    public static final String VAR_CT_IMPORT_STARTED_AT = "CT_IMPORT_STARTED_AT";

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
        if (Boolean.TRUE.equals(context.get(TestSupportConstants.VAR_DEMO_MODE, Boolean.class))) {
            LOG.info("StartCtImport [Demo-Mode]: würde Job '" + jobName + "' auf JVM '" + jvmName + "' starten.");
            context.put(VAR_CT_IMPORT_STARTED_AT, Instant.now());
            return StepResult.NEXT;
        }

        LOG.info("StartCtImport: starte JVM-Job '" + jobName + "' auf '" + jvmName + "'");
        Instant startedAt = Instant.now();
        restService.startJob(jvmName, jobName);
        context.put(VAR_CT_IMPORT_STARTED_AT, startedAt);
        LOG.info("  Job gestartet um " + startedAt);
        return StepResult.NEXT;
    }

    @Override
    public String name() {
        return "StartCtImport";
    }
}
