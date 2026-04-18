package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.exports_checker.CollectsChecker;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskRunnable;
import org.apache.log4j.Level;

import java.util.Map;

public class UserTaskCheckCollects extends AbstractUserTaskRunnable {

    public UserTaskCheckCollects(final EnvironmentConfig environmentConfig, TesunClientJobListener tesunClientJobListener) {
        super(environmentConfig, tesunClientJobListener);
    }

    @Override
    public Map<String, Object> runTask(Map<String, Object> taskVariablesMap) throws Exception {
        @SuppressWarnings("unchecked")
        Map<String, TestCustomer> activeCustomersMap = (Map<String, TestCustomer>) taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_ACTIVE_CUSTOMERS);
        CollectsChecker collectsChecker = new CollectsChecker(environmentConfig, tesunClientJobListener);
        activeCustomersMap.forEach((key, testCustomer) -> {
            if (testCustomer.isActivated()) {
                testCustomer.addTestResultsForCommand(TestSupportClientKonstanten.CHECK_COLLECTS_COMMAND);
                testCustomer.refreshCollecteds();
                collectsChecker.checkTestCustomerCollects(testCustomer);
                notifyUserTask(Level.INFO, ".");
            }
        });
        return taskVariablesMap;
    }
}
