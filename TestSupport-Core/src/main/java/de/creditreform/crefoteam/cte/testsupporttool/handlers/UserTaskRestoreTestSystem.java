package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.rest.RestInvokerConfig;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.rest.TesunRestService;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskRunnable;
import org.apache.log4j.Level;

import java.util.Map;

/** Literal-Port aus {@code testsupport_client.tesun_activiti.handlers}. */
public class UserTaskRestoreTestSystem extends AbstractUserTaskRunnable {

    public UserTaskRestoreTestSystem(EnvironmentConfig environmentConfig, TesunClientJobListener listener) {
        super(environmentConfig, listener);
    }

    @Override
    public Map<String, Object> runTask(Map<String, Object> taskVariablesMap) throws Exception {
        TestSupportClientKonstanten.TEST_PHASE phase = (TestSupportClientKonstanten.TEST_PHASE)
                taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE);
        notifyUserTask(Level.INFO, buildNotifyStringForClassName(phase));
        if (checkDemoMode((Boolean) taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE))) {
            return taskVariablesMap;
        }
        RestInvokerConfig cfg = environmentConfig.getRestServiceConfigsForMasterkonsole().get(0);
        TesunRestService rest = getTesunRestServiceInstance(cfg);
        rest.restoreEnvironmentProperties();
        notifyUserTask(Level.INFO, "\n\t\tDie CLZ-Liste in Master-Console-Properties wieder restauriert.");
        return taskVariablesMap;
    }

    protected TesunRestService getTesunRestServiceInstance(RestInvokerConfig cfg) throws PropertiesException {
        return new TesunRestService(cfg, tesunClientJobListener);
    }
}
