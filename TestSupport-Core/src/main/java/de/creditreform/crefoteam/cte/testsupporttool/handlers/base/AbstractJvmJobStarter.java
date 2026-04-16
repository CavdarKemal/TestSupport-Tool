package de.creditreform.crefoteam.cte.testsupporttool.handlers.base;

import de.creditreform.crefoteam.cte.jvmclient.JvmInstallation;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.JobInfo;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import org.apache.log4j.Level;

import java.time.Instant;
import java.util.Map;

/**
 * Port der gleichnamigen Basisklasse. Startet einen JVM-Job und hinterlegt
 * den Startzeitpunkt unter der übergebenen Context-Variable.
 */
public abstract class AbstractJvmJobStarter extends AbstractUserTaskRunnable {

    protected final JobInfo jobInfo;
    protected final String startDateVariable;

    protected AbstractJvmJobStarter(JobInfo jobInfo, String startDateVariable,
                                    EnvironmentConfig environmentConfig, TesunClientJobListener listener) {
        super(environmentConfig, listener);
        this.jobInfo = jobInfo;
        this.startDateVariable = startDateVariable;
    }

    @Override
    public Map<String, Object> runTask(Map<String, Object> taskVariablesMap) throws Exception {
        TestSupportClientKonstanten.TEST_PHASE phase =
                (TestSupportClientKonstanten.TEST_PHASE) taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE);
        notifyUserTask(Level.INFO, buildNotifyStringForClassName(phase));
        if (checkDemoMode((Boolean) taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE))) {
            taskVariablesMap.put(startDateVariable, Instant.now().toString());
            return taskVariablesMap;
        }
        Map<String, JvmInstallation> jvms = getJvmInstallationsMap();
        Instant startedAt = Instant.now();
        doStartJvmJob(jobInfo, jvms, phase);
        taskVariablesMap.put(startDateVariable, startedAt.toString());
        return taskVariablesMap;
    }
}
