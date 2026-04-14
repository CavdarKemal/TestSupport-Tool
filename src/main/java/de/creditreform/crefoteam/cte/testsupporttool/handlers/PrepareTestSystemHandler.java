package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.statemachine.ProcessContext;
import de.creditreform.crefoteam.cte.statemachine.Step;
import de.creditreform.crefoteam.cte.statemachine.StepResult;
import de.creditreform.crefoteam.cte.testsupporttool.config.EnvironmentConfig;
import de.creditreform.crefoteam.cte.testsupporttool.config.TestSupportConstants;
import org.apache.log4j.Logger;

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

    private static final Logger LOG = Logger.getLogger(PrepareTestSystemHandler.class);

    private final EnvironmentConfig environmentConfig;

    public PrepareTestSystemHandler(EnvironmentConfig environmentConfig) {
        this.environmentConfig = environmentConfig;
    }

    @Override
    public StepResult execute(ProcessContext context) {
        Object phase = context.get(TestSupportConstants.VAR_TEST_PHASE);
        Boolean demoMode = context.get(TestSupportConstants.VAR_DEMO_MODE, Boolean.class);

        LOG.info("PrepareTestSystem für Phase=" + phase + ", env=" + environmentConfig.getEnvName());

        if (Boolean.TRUE.equals(demoMode)) {
            LOG.info("  Demo-Mode: Properties-Setup wird simuliert.");
            return StepResult.NEXT;
        }

        // Hier würde der echte Handler die Environment-Properties in die
        // CTE-Datenbank schreiben. Im Spike: nur Marker ablegen, damit Tests
        // die Ausführung nachweisen können.
        context.put("preparedAt", System.currentTimeMillis());
        LOG.info("  System vorbereitet.");
        return StepResult.NEXT;
    }

    @Override
    public String name() {
        return "PrepareTestSystem";
    }
}
