package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskRunnable;
import org.apache.log4j.Level;

import java.util.Map;

/** Schalen-Port: Demo-Mode loggt nur, Real-Mode wirft NotImplemented. */
public class UserTaskStartUploads extends AbstractUserTaskRunnable {
    public UserTaskStartUploads(EnvironmentConfig environmentConfig, TesunClientJobListener listener) {
        super(environmentConfig, listener);
    }
    @Override
    public Map<String, Object> runTask(Map<String, Object> taskVariablesMap) {
        // TODO aus Original wiederherstellen!
        throw new UnsupportedOperationException("StartUploads im Real-Mode erfordert TestFallUploadCrefos / Upload-Mechanik (noch nicht portiert).");
    }
}
