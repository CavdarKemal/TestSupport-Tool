package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.statemachine.ProcessContext;
import de.creditreform.crefoteam.cte.statemachine.Step;
import de.creditreform.crefoteam.cte.statemachine.StepResult;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;

/**
 * Demo-Pendant zu {@code UserTaskPrepareTestSystem}: führt eine
 * vorbereitende Aktion aus. Im Spike ohne echten REST-Aufruf — protokolliert
 * nur, was passieren würde.
 */
public final class PrepareTestSystemHandler implements Step {

    private final EnvironmentConfig environmentConfig;

    public PrepareTestSystemHandler(EnvironmentConfig environmentConfig) {
        this.environmentConfig = environmentConfig;
    }

    @Override
    public StepResult execute(ProcessContext context) {
        Object phase = context.get(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE);
        Boolean demoMode = context.get(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE, Boolean.class);

        TimelineLogger.info(PrepareTestSystemHandler.class,
                "PrepareTestSystem für Phase={}, env={}", phase, environmentConfig.getCurrentEnvName());

        if (Boolean.TRUE.equals(demoMode)) {
            TimelineLogger.info(PrepareTestSystemHandler.class,
                    "  Demo-Mode: Properties-Setup wird simuliert.");
            return StepResult.NEXT;
        }
        context.put("preparedAt", System.currentTimeMillis());
        TimelineLogger.info(PrepareTestSystemHandler.class, "  System vorbereitet.");
        return StepResult.NEXT;
    }

    @Override
    public String name() {
        return "PrepareTestSystem";
    }
}
