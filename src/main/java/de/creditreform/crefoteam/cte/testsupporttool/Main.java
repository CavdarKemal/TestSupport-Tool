package de.creditreform.crefoteam.cte.testsupporttool;

import de.creditreform.crefoteam.cte.statemachine.ProcessDefinition;
import de.creditreform.crefoteam.cte.statemachine.ProcessOutcome;
import de.creditreform.crefoteam.cte.testsupporttool.config.EnvironmentConfig;
import de.creditreform.crefoteam.cte.testsupporttool.config.TestSupportConstants;
import de.creditreform.crefoteam.cte.testsupporttool.process.TestAutomationProcess;
import de.creditreform.crefoteam.cte.testsupporttool.rest.TesunRestService;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Demo-Einstieg ohne GUI: konfiguriert die Engine im Demo-Mode (alle
 * REST-Aufrufe werden simuliert) und stößt den Prozess an.
 *
 * <p>Aufruf:
 * <pre>
 *   java -cp testsupport-tool-0.1.0-SNAPSHOT.jar:... \
 *        de.creditreform.crefoteam.cte.testsupporttool.Main
 * </pre>
 */
public final class Main {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);

        EnvironmentConfig env = EnvironmentConfig.forDemo("http://unused-in-demo");
        TesunRestService rest = new TesunRestService(env.getTesunRestBaseUrl());

        ProcessDefinition definition = TestAutomationProcess.build(env, rest);

        Map<String, Object> initialVariables = new HashMap<>();
        initialVariables.put(TestSupportConstants.VAR_TEST_PHASE, "PHASE_2");
        initialVariables.put(TestSupportConstants.VAR_TEST_TYPE, TestSupportConstants.TEST_TYPE_PHASE1_AND_PHASE2);
        initialVariables.put(TestSupportConstants.VAR_DEMO_MODE, Boolean.TRUE);

        ProcessOutcome outcome = new ProcessRunner().run(definition, initialVariables);
        Logger.getLogger(Main.class).info("Endzustand: " + outcome);
    }

    private Main() { }
}
