package de.creditreform.crefoteam.cte.testsupporttool;

import de.creditreform.crefoteam.cte.statemachine.ProcessDefinition;
import de.creditreform.crefoteam.cte.statemachine.ProcessOutcome;
import de.creditreform.crefoteam.cte.testsupporttool.config.EnvironmentConfig;
import de.creditreform.crefoteam.cte.testsupporttool.config.TestSupportConstants;
import de.creditreform.crefoteam.cte.testsupporttool.env.EnvironmentLockManager;
import de.creditreform.crefoteam.cte.testsupporttool.env.TestEnvironmentManager;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;
import de.creditreform.crefoteam.cte.testsupporttool.process.TestAutomationProcess;
import de.creditreform.crefoteam.cte.testsupporttool.rest.TesunRestService;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Demo-Einstieg ohne GUI. Demonstriert das vollständige Bootstrap:
 *
 * <ol>
 *   <li>Console-Logging als Fallback aktivieren.</li>
 *   <li>{@link EnvironmentConfig} laden (per Name oder via {@link EnvironmentConfig#forDemo}).</li>
 *   <li>{@link TestEnvironmentManager#switchEnvironment} — sperrt die Umgebung
 *       und konfiguriert den {@link TimelineLogger} auf das Env-Logs-Verzeichnis.</li>
 *   <li>Shutdown-Hook registrieren, damit der Lock auch bei Abbruch freigegeben wird.</li>
 *   <li>Prozess starten.</li>
 *   <li>Im finally: Lock + Logger schließen.</li>
 * </ol>
 *
 * Aufruf:
 * <pre>
 *   java -cp target/testsupport-tool-0.1.0-SNAPSHOT.jar:lib/* \
 *        de.creditreform.crefoteam.cte.testsupporttool.Main [envName]
 * </pre>
 */
public final class Main {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);

        EnvironmentConfig env = (args.length >= 1)
                ? EnvironmentConfig.loadByName(args[0])
                : EnvironmentConfig.forDemo("http://unused-in-demo");

        EnvironmentLockManager.registerShutdownHook();

        if (!TestEnvironmentManager.switchEnvironment(env)) {
            TimelineLogger.error(Main.class,
                    "Konnte Umgebung {} nicht aktivieren — Abbruch.", env.getCurrentEnvName());
            System.exit(2);
        }

        try (TimelineLogger.Action overall =
                     TimelineLogger.action("TestAutomationProcess", env.getCurrentEnvName())) {

            TesunRestService rest = new TesunRestService(env.getTesunRestBaseUrl());
            ProcessDefinition definition = TestAutomationProcess.build(env, rest);

            Map<String, Object> initialVariables = new HashMap<>();
            initialVariables.put(TestSupportConstants.VAR_TEST_PHASE, "PHASE_2");
            initialVariables.put(TestSupportConstants.VAR_TEST_TYPE, TestSupportConstants.TEST_TYPE_PHASE1_AND_PHASE2);
            initialVariables.put(TestSupportConstants.VAR_DEMO_MODE, args.length == 0 ? Boolean.TRUE : Boolean.FALSE);

            ProcessOutcome outcome = new ProcessRunner().run(definition, initialVariables);
            overall.result(outcome.name());
            TimelineLogger.info(Main.class, "Endzustand: {}", outcome);
        } finally {
            TestEnvironmentManager.reset();
        }
    }

    private Main() { }
}
