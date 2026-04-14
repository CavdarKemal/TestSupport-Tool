package de.creditreform.crefoteam.cte.testsupporttool;

import de.creditreform.crefoteam.cte.statemachine.ProcessOutcome;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.testsupporttool.auto.CteTestAutomatisierung;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Konsolen-Einstieg ohne GUI — Pendant zu
 * {@code testsupport_client.TestSupportGUI...ActivitiTestAutomatisierung}.
 *
 * <p>Argumente: siehe {@link CliArgs#usage()}.
 *
 * <p>Aufrufbeispiele:
 * <pre>
 *   Main                       → Demo-Mode, in-memory Config
 *   Main e:ENE                 → Real-Mode gegen ENE-config.properties
 *   Main e:ENE -Demo:true      → ENE-Config geladen, aber Demo-Mode aktiv
 * </pre>
 */
public final class Main {

    public static void main(String[] args) throws PropertiesException {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);

        CliArgs cli;
        try {
            cli = CliArgs.parse(args);
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
            System.err.println();
            System.err.println(CliArgs.usage());
            System.exit(64); // EX_USAGE
            return;
        }

        EnvironmentConfig env = (cli.getEnvName() == null)
                ? EnvironmentConfig.forDemo("http://unused-in-demo")
                : new EnvironmentConfig(cli.getEnvName());

        CteTestAutomatisierung runner = new CteTestAutomatisierung(env);
        try {
            ProcessOutcome outcome = runner.startProcess(cli.isDemoMode());
            System.exit(outcome == ProcessOutcome.COMPLETED ? 0 : 1);
        } finally {
            runner.shutdown();
        }
    }

    private Main() { }
}
