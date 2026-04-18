package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskRunnable;
import org.apache.log4j.Level;

import java.util.Map;

/** Schalen-Port: Restore startet im Original einen TestFall-Restore. */
public class UserTaskStartRestore extends AbstractUserTaskRunnable {
    public UserTaskStartRestore(EnvironmentConfig environmentConfig, TesunClientJobListener listener) {
        super(environmentConfig, listener);
    }
    @Override
    public Map<String, Object> runTask(Map<String, Object> taskVariablesMap) {
        // TODO aus Original wiederherstellen!
        throw new UnsupportedOperationException( "StartRestore im Real-Mode erfordert TestFallRestoreCrefos (noch nicht portiert).");
    }
}
