package de.creditreform.crefoteam.cte.testsupporttool.auto;

import de.creditreform.crefoteam.cte.statemachine.ProcessOutcome;
import de.creditreform.crefoteam.cte.testsupporttool.CliArgs;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;

public final class CteTestAutomatisierungMain {

    public static void main(String[] args) throws PropertiesException {
        CliArgs cli;
        try {
            cli = CliArgs.parse(args);
            cli.requireValid();
        } catch (IllegalArgumentException ex) {
            TimelineLogger.error(CteTestAutomatisierungMain.class, ex.getMessage());
            TimelineLogger.info(CteTestAutomatisierungMain.class, CliArgs.usage());
            System.exit(64); // EX_USAGE
            return;
        }
        EnvironmentConfig env = new EnvironmentConfig(cli.getEnvName());
        CteTestAutomatisierung cteTestAutomatisierung = new CteTestAutomatisierung(env);
        try {
            ProcessOutcome outcome = cteTestAutomatisierung.startProcess(cli.isDemoMode());
            System.exit(cteTestAutomatisierung.computeExitCode(outcome));
        } finally {
            cteTestAutomatisierung.shutdown();
        }
    }

    private CteTestAutomatisierungMain() { }
}
