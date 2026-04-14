package de.creditreform.crefoteam.cte.testsupporttool;

import de.creditreform.crefoteam.cte.statemachine.ProcessOutcome;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.testsupporttool.auto.CteTestAutomatisierung;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Konsolen-Einstieg ohne GUI — instanziiert {@link CteTestAutomatisierung}
 * und startet den Prozess. Initialisierungs- und Cleanup-Logik liegt
 * komplett in der {@code CteTestAutomatisierung}.
 *
 * <p>Aufruf:
 * <pre>
 *   java -cp testsupport-tool.jar:lib/* \
 *        de.creditreform.crefoteam.cte.testsupporttool.Main [envName]
 * </pre>
 * Ohne Argument läuft Demo-Mode mit der in-memory {@code forDemo}-Config;
 * mit {@code envName} (z. B. {@code ENE}) wird die zugehörige
 * {@code <envName>-config.properties} geladen und Real-Mode aktiviert.
 */
public final class Main {

    public static void main(String[] args) throws PropertiesException {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);

        boolean isDemoMode = args.length == 0;
        EnvironmentConfig env = isDemoMode
                ? EnvironmentConfig.forDemo("http://unused-in-demo")
                : new EnvironmentConfig(args[0]);

        CteTestAutomatisierung runner = new CteTestAutomatisierung(env);
        try {
            ProcessOutcome outcome = runner.startProcess(isDemoMode);
            System.exit(outcome == ProcessOutcome.COMPLETED ? 0 : 1);
        } finally {
            runner.shutdown();
        }
    }

    private Main() { }
}
