package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.exports_checker.ExportContentsComparator;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskRunnable;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserTaskCheckRefExports extends AbstractUserTaskRunnable {

    public UserTaskCheckRefExports(final EnvironmentConfig environmentConfig, TesunClientJobListener tesunClientJobListener) {
        super(environmentConfig, tesunClientJobListener);
    }

    @Override
    public Map<String, Object> runTask(Map<String, Object> taskVariablesMap) throws Exception {
        TestSupportClientKonstanten.TEST_PHASE testPhase = (TestSupportClientKonstanten.TEST_PHASE) taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE);
        notifyUserTask(Level.INFO, buildNotifyStringForClassName(testPhase));
        if (checkDemoMode((Boolean) taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE))) {
            return taskVariablesMap;
        }

        @SuppressWarnings("unchecked")
        Map<String, TestCustomer> activeCustomersMap = (Map<String, TestCustomer>) taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_ACTIVE_CUSTOMERS);
        String strIgnorableXPaths = environmentConfig.getProperty(TestSupportClientKonstanten.OPT_IGNORABLE_XPATHS, false,
                "firma-bonitaet/ratingstufe;auftrags-referenz;identification-number");
        List<String> ignorableXPaths = parseIgnorablePaths(strIgnorableXPaths);

        notifyUserTask(Level.INFO, String.format("\nVergleiche XML's für Phase %s...", testPhase.getDirName()));
        ExportContentsComparator comparator = new ExportContentsComparator(ignorableXPaths, tesunClientJobListener);
        activeCustomersMap.forEach((key, testCustomer) -> {
            testCustomer.addTestResultsForCommand(TestSupportClientKonstanten.CHECK_REF_EXPORTS_COMMAND);
            testCustomer.refreshRestoredCollects();
            comparator.compareFileContents(testCustomer);
            notifyUserTask(Level.INFO, ".");
        });

        return taskVariablesMap;
    }

    static List<String> parseIgnorablePaths(String strIgnorableXPaths) {
        List<String> ignorableXPaths = new ArrayList<>();
        if (strIgnorableXPaths == null || strIgnorableXPaths.isEmpty()) {
            return ignorableXPaths;
        }
        for (String ignorableXPath : strIgnorableXPaths.split(";")) {
            if (!ignorableXPath.startsWith("/")) {
                ignorableXPath = "/" + ignorableXPath;
            }
            String[] pieces = ignorableXPath.split("/");
            for (int i = 1; i < pieces.length; i++) {
                if (!pieces[i].endsWith("]") && !pieces[i].startsWith("@")) {
                    ignorableXPath = StringUtils.replaceOnce(ignorableXPath, pieces[i], pieces[i] + "[1]");
                }
            }
            ignorableXPaths.add(ignorableXPath);
        }
        return ignorableXPaths;
    }
}
