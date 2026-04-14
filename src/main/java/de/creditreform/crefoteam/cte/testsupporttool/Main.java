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
 * <p>{@code e:<env>} ist Pflicht — die Umgebungs-Properties werden immer
 * geladen, analog zum Original. Demo-Mode wirkt ausschließlich in den
 * Handlern (via {@code checkDemoMode}).
 */
public final class Main {

    public static void main(String[] args) throws PropertiesException {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);

        CliArgs cli;
        try {
            cli = CliArgs.parse(args);
            cli.requireValid();
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
            System.err.println();
            System.err.println(CliArgs.usage());
            System.exit(64); // EX_USAGE
            return;
        }

        EnvironmentConfig env = new EnvironmentConfig(cli.getEnvName());

        CteTestAutomatisierung runner = new CteTestAutomatisierung(env);
        try {
            ProcessOutcome outcome = runner.startProcess(cli.isDemoMode());
            System.exit(runner.computeExitCode(outcome));
        } finally {
            runner.shutdown();
        }
    }

    private Main() { }
}
