package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskRunnable;
import de.creditreform.crefoteam.cte.tesun.util.TesunUtilites;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;
import org.apache.log4j.Level;

import java.util.Map;

/**
 * Port aus {@code testsupport_client.tesun_activiti.handlers}. Im Original
 * via {@code TesunUtilites.sendEmail} (commons-email) verschickt — im Spike
 * nur via {@link TimelineLogger} protokolliert, um die commons-email-Dependency
 * zu vermeiden. Der reale E-Mail-Versand kann mit dem späteren Port von
 * {@code MailService} ergänzt werden.
 */
public class UserTaskFailureMail extends AbstractUserTaskRunnable {

    public UserTaskFailureMail(EnvironmentConfig environmentConfig, TesunClientJobListener listener) {
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
            String content = "Fehler beim Ausführen des Test-Automatisierungs-Prozesses!";
            String from = environmentConfig.getStateEngineEmailFrom();
            String to   = environmentConfig.getStateEngineFailureEmailTo();
            TimelineLogger.warn(getClass(), "Sende Fehler-Mail: subject='{}', from='{}', to='{}'", subject, from, to);
            TesunUtilites.sendEmail(environmentConfig.getSmtpHost(), environmentConfig.getSmtpPort(),
                    from, to, subject, content, null);
        } catch (Exception ex) {
            notifyUserTask(Level.WARN, "\nWARNING: Fehler-Mail fehlgeschlagen: " + ex.getMessage());
        }
        return taskVariablesMap;
    }
}
