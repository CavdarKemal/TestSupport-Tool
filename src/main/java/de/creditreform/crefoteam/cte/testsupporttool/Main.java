package de.creditreform.crefoteam.cte.testsupporttool;

import de.creditreform.crefoteam.cte.statemachine.ProcessDefinition;
import de.creditreform.crefoteam.cte.statemachine.ProcessOutcome;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.env.EnvironmentLockManager;
import de.creditreform.crefoteam.cte.testsupporttool.env.TestEnvironmentManager;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;
import de.creditreform.crefoteam.cte.testsupporttool.process.CteAutomatedTestProcess;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Demo-Einstieg ohne GUI. Startet die volle CTE Test-Automatisierung im
 * Demo-Mode (alle externen Aufrufe werden simuliert).
 */
public final class Main {

    public static void main(String[] args) throws PropertiesException {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);

        EnvironmentConfig env = (args.length >= 1)
                ? new EnvironmentConfig(args[0])
                : EnvironmentConfig.forDemo("http://unused-in-demo");

        EnvironmentLockManager.registerShutdownHook();
        if (!TestEnvironmentManager.switchEnvironment(env)) {
            TimelineLogger.error(Main.class, "Konnte Umgebung {} nicht aktivieren — Abbruch.", env.getCurrentEnvName());
            System.exit(2);
        }

        try (TimelineLogger.Action action = TimelineLogger.action("CteAutomatedTestProcess", env.getCurrentEnvName())) {
            ConsoleProcessListener listener = new ConsoleProcessListener();
            ProcessDefinition definition = CteAutomatedTestProcess.build(env, null);

            Map<String, Object> initialVariables = new HashMap<>();
            initialVariables.put(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE,
                    TestSupportClientKonstanten.TEST_PHASE.PHASE_2);
            initialVariables.put(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_TYPE,
                    CteAutomatedTestProcess.TEST_TYPE_PHASE1_AND_PHASE2);
            initialVariables.put(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE,
                    args.length == 0 ? Boolean.TRUE : Boolean.FALSE);

            ProcessOutcome outcome = new ProcessRunner().run(definition, initialVariables);
            action.result(outcome.name());
            TimelineLogger.info(Main.class, "Endzustand: {}", outcome);
        } finally {
            TestEnvironmentManager.reset();
        }
    }

    private Main() { }
}
