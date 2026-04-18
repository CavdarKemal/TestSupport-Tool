package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskRunnable;
import org.apache.log4j.Level;

import java.util.Map;

/** Schalen-Port: SFTP-Uploads benötigen SFTP-Client + JCH (im Spike nicht enthalten). */
public class UserTaskStartSftpUploads extends AbstractUserTaskRunnable {
    public UserTaskStartSftpUploads(EnvironmentConfig environmentConfig, TesunClientJobListener listener) {
        super(environmentConfig, listener);
    }
    @Override
    public Map<String, Object> runTask(Map<String, Object> taskVariablesMap) {
        // TODO aus Original wiederherstellen!
        throw new UnsupportedOperationException("StartSftpUploads im Real-Mode erfordert SFTP-Mechanik (jsch, noch nicht portiert).");
    }
}
