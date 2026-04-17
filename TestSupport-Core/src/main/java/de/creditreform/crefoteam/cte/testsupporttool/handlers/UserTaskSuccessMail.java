package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskRunnable;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;
import org.apache.log4j.Level;

import java.util.Map;

/** Pendant zu {@link UserTaskFailureMail} mit Erfolgs-Nachricht. */
public class UserTaskSuccessMail extends AbstractUserTaskRunnable {

    public UserTaskSuccessMail(EnvironmentConfig environmentConfig, TesunClientJobListener listener) {
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
        try {
            String subject = "CTE Test-Automatisierung: " + environmentConfig.getCurrentEnvName();
            String content = "CTE Test-Automatisierung für " + environmentConfig.getCurrentEnvName() + " erfolgreich ausgeführt.";
            TimelineLogger.info(getClass(),
                    "[Mail-Stub: subject='{}', from='{}', to='{}', content='{}']",
                    subject, environmentConfig.getStateEngineEmailFrom(),
                    environmentConfig.getStateEngineSuccessEmailTo(), content);
        } catch (Exception ex) {
            notifyUserTask(Level.WARN, "\nWARNING: Mail-Stub fehlgeschlagen: " + ex.getMessage());
        }
        return taskVariablesMap;
    }
}
