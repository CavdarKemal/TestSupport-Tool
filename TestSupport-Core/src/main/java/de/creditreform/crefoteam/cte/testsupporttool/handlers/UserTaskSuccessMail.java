package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskRunnable;
import de.creditreform.crefoteam.cte.tesun.util.TesunUtilites;
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
        try {
            String subject = "CTE Test-Automatisierung: " + environmentConfig.getCurrentEnvName();
            String content = "CTE Test-Automatisierung für " + environmentConfig.getCurrentEnvName() + " erfolgreich ausgeführt.";
            String from = environmentConfig.getStateEngineEmailFrom();
            String to   = environmentConfig.getStateEngineSuccessEmailTo();
            TimelineLogger.info(getClass(), "Sende Erfolgs-Mail: subject='{}', from='{}', to='{}'", subject, from, to);
            TesunUtilites.sendEmail(environmentConfig.getSmtpHost(), environmentConfig.getSmtpPort(), from, to, subject, content, null);
        } catch (Exception ex) {
            notifyUserTask(Level.WARN, "\nWARNING: Erfolgs-Mail fehlgeschlagen: " + ex.getMessage());
        }
        return taskVariablesMap;
    }
}
