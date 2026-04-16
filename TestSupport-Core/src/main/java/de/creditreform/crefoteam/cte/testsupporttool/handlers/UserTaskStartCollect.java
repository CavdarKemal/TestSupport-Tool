package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskRunnable;
import org.apache.log4j.Level;

import java.util.Map;

/**
 * Schalen-Port. Im Original sammelt der Handler die exportierten Crefos
 * und ergänzt für INSO-Kunden zusätzliche Snippets — beides nutzt
 * {@code TestFallCollectExportedCrefos}, {@code TestFallFileUtil} und
 * INSO-XML-Bindings, die im Spike noch nicht portiert sind.
 */
public class UserTaskStartCollect extends AbstractUserTaskRunnable {
    public UserTaskStartCollect(EnvironmentConfig environmentConfig, TesunClientJobListener listener) {
        super(environmentConfig, listener);
    }
    @Override
    public Map<String, Object> runTask(Map<String, Object> taskVariablesMap) {
        TestSupportClientKonstanten.TEST_PHASE phase = (TestSupportClientKonstanten.TEST_PHASE)
                taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE);
        notifyUserTask(Level.INFO, buildNotifyStringForClassName(phase));
        if (checkDemoMode((Boolean) taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE))) {
            return taskVariablesMap;
        }
        throw new UnsupportedOperationException(
                "StartCollect im Real-Mode erfordert TestFallCollectExportedCrefos und INSO-Snippets (noch nicht portiert).");
    }
}
