package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskRunnable;
import org.apache.log4j.Level;

import java.util.Map;

/**
 * Schalen-Port. Im Original sehr komplex (paketweises Starten der Exports
 * für alle aktiven Kunden + ExportsWaiter). Im Spike: Demo-Mode-tauglich,
 * Real-Mode wird in einer späteren Stufe aktiviert.
 */
public class UserTaskStartExports extends AbstractUserTaskRunnable {
    public UserTaskStartExports(EnvironmentConfig environmentConfig, TesunClientJobListener listener) {
        super(environmentConfig, listener);
    }
    @Override
    public Map<String, Object> runTask(Map<String, Object> taskVariablesMap) {
        // TODO aus Original wiederherstellen!
        throw new UnsupportedOperationException("StartExports im Real-Mode erfordert ExportsWaiter und INSO-Phase-2-Logik (noch nicht portiert).");
    }
}
