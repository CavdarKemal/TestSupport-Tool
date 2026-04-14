package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.statemachine.ProcessContext;
import de.creditreform.crefoteam.cte.statemachine.Step;
import de.creditreform.crefoteam.cte.statemachine.StepResult;
import de.creditreform.crefoteam.cte.testsupporttool.config.EnvironmentConfig;
import de.creditreform.crefoteam.cte.testsupporttool.config.TestSupportConstants;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;

/**
 * Demo-Pendant zu {@code UserTaskPrepareTestSystem}: führt eine
 * vorbereitende Aktion aus. Im Spike ohne echten REST-Aufruf — protokolliert
 * nur, was passieren würde.
 *
 * <p>Demonstriert das Handler-Muster „einfache Aktion" (kein Warten, kein
 * Job-Start). Echte Handler erweitern dies um den Aufruf von
 * {@code tesunRestServiceWLS.setEnvironmentProperties(...)}.
 */
public final class PrepareTestSystemHandler implements Step {

    private final EnvironmentConfig environmentConfig;

    public PrepareTestSystemHandler(EnvironmentConfig environmentConfig) {
        this.environmentConfig = environmentConfig;
    }

    @Override
    public StepResult execute(ProcessContext context) {
        Object phase = context.get(TestSupportConstants.VAR_TEST_PHASE);
        Boolean demoMode = context.get(TestSupportConstants.VAR_DEMO_MODE, Boolean.class);

        TimelineLogger.info(PrepareTestSystemHandler.class,
                "PrepareTestSystem für Phase={}, env={}", phase, environmentConfig.getCurrentEnvName());

        if (Boolean.TRUE.equals(demoMode)) {
            TimelineLogger.info(PrepareTestSystemHandler.class,
                    "  Demo-Mode: Properties-Setup wird simuliert.");
            return StepResult.NEXT;
        }

        // Hier würde der echte Handler die Environment-Properties in die
        // CTE-Datenbank schreiben. Im Spike: nur Marker ablegen, damit Tests
        // die Ausführung nachweisen können.
        context.put("preparedAt", System.currentTimeMillis());
        TimelineLogger.info(PrepareTestSystemHandler.class, "  System vorbereitet.");
        return StepResult.NEXT;
    }

    @Override
    public String name() {
        return "PrepareTestSystem";
    }
}
