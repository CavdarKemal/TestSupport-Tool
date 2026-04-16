package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskRunnable;
import org.apache.log4j.Level;

import java.util.Map;

/**
 * Schalen-Port. Original sammelt die SFTP-Uploads via
 * {@code TestFallCollectSftpUploads} und prüft sie. Im Spike:
 * Demo-Mode-tauglich, Real-Mode noch nicht portiert.
 *
 * <p>Wird übersprungen, wenn die Umgebung {@code SFTP_UPLOAD_ACTIVE=false}
 * konfiguriert hat (Original-Verhalten).
 */
public class UserTaskCheckSftpUploads extends AbstractUserTaskRunnable {
    public UserTaskCheckSftpUploads(final EnvironmentConfig environmentConfig, TesunClientJobListener tesunClientJobListener) {
        super(environmentConfig, tesunClientJobListener);
    }

    @Override
    public Map<String, Object> runTask(Map<String, Object> taskVariablesMap) throws PropertiesException {
        TestSupportClientKonstanten.TEST_PHASE testPhase = (TestSupportClientKonstanten.TEST_PHASE) taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE);
        notifyUserTask(Level.INFO, buildNotifyStringForClassName(testPhase));
        if (checkDemoMode((Boolean) taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE))) {
            return taskVariablesMap;
        }
        if (!environmentConfig.isSftpUploadEnabled()) {
            notifyUserTask(Level.INFO, buildNotifyStringForClassName(testPhase) + " wird übersprungen, da deaktiviert!");
            return taskVariablesMap;
        }
        throw new UnsupportedOperationException(
                "CheckSftpUploads im Real-Mode erfordert TestFallCollectSftpUploads (noch nicht portiert).");
    }
}
