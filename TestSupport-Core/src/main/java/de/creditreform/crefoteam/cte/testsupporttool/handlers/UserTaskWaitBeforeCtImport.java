package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskRunnable;
import org.apache.log4j.Level;

import java.util.Map;

/**
 * Wartet vor dem CT-Import.
 *
 * <p>Original nutzt hier {@code TIME_BEFORE_BTLG_IMPORT} — vermutlich
 * Copy-Paste-Fehler im Original. Wir bewahren das Verhalten 1:1.
 */
public class UserTaskWaitBeforeCtImport extends AbstractUserTaskRunnable {
    public UserTaskWaitBeforeCtImport(final EnvironmentConfig environmentConfig, TesunClientJobListener tesunClientJobListener) {
        super(environmentConfig, tesunClientJobListener);
    }

    @Override
    public Map<String, Object> runTask(Map<String, Object> taskVariablesMap) throws Exception {
        checkForWait(taskVariablesMap, TesunClientJobListener.UT_TASK_PARAM_NAME_TIME_BEFORE_BTLG_IMPORT);
        return taskVariablesMap;
    }
}
