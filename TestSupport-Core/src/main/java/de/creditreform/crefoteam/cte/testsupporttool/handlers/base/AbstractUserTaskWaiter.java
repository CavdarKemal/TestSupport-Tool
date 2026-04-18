package de.creditreform.crefoteam.cte.testsupporttool.handlers.base;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.rest.TesunRestService;
import de.creditreform.crefoteam.cte.tesun.rest.dto.TesunJobexecutionInfo;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.tesun.util.TesunDateUtils;
import org.apache.log4j.Level;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Port der gleichnamigen Basisklasse aus
 * {@code testsupport_client.tesun_activiti.handlers}. Wartet per Polling
 * auf die Beendigung eines Jobs mit {@link TesunJobexecutionInfo#getJobStatus()} ==
 * {@code "COMPLETED"} und {@code lastCompletitionDate >= jobStartedAt}.
 */
public abstract class AbstractUserTaskWaiter extends AbstractUserTaskRunnable {

    private static final int MAX_RETRIES = 5;

    protected final String processIdentifier;
    protected final String lastStartDateVariable;

    protected AbstractUserTaskWaiter(String processIdentifier, String lastStartDateVariable, EnvironmentConfig environmentConfig, TesunClientJobListener listener) {
        super(environmentConfig, listener);
        this.processIdentifier = processIdentifier;
        this.lastStartDateVariable = lastStartDateVariable;
    }

    @Override
    public Map<String, Object> runTask(Map<String, Object> taskVariablesMap) throws Exception {
        Calendar jobStartedCal = extractCalendarFromMap(taskVariablesMap, lastStartDateVariable);
        doWaitForFinish(jobStartedCal);
        return taskVariablesMap;
    }

    protected void doWaitForFinish(Calendar jobStartedCal) throws Exception {
        TesunRestService rest = getTesunRestService();
        long timeoutMillis = environmentConfig.getMillisForImportCycleTimeOut();
        long sleepMillis = environmentConfig.getMillisForJobStatusQuerySleepTime();
        int retries = 0;
        while (true) {
            try {
                waitForJob(rest, jobStartedCal, timeoutMillis, sleepMillis);
                return;
            } catch (Exception ex) {
                if (++retries > MAX_RETRIES || !askForRetryUserTask(getClass().getSimpleName(), ex)) {
                    throw ex;
                }
            }
        }
    }

    private void waitForJob(TesunRestService rest, Calendar jobStartedCal, long timeoutMillis, long sleepMillis) throws Exception {
        String startedStr = TesunDateUtils.DATE_FORMAT_DD_MM_YYYY_HH_MM_SS.format(jobStartedCal.getTime());
        long deadline = System.currentTimeMillis() + timeoutMillis;
        notifyUserTask(Level.INFO, "\n\tWarte auf Beendigung des Prozesses '" + processIdentifier + "'...");
        while (System.currentTimeMillis() < deadline) {
            if (canceled) return;
            TesunJobexecutionInfo info = rest.getTesunJobExecutionInfo(processIdentifier);
            String status = info.getJobStatus();
            if (status == null) {
                throw new RuntimeException("Der Status des Prozesses '" + processIdentifier + "' konnte nicht ermittelt werden!");
            }
            Calendar completion = info.getLastCompletitionDate();
            if (completion != null) {
                String completionStr = TesunDateUtils.DATE_FORMAT_DD_MM_YYYY_HH_MM_SS.format(completion.getTime());
                notifyUserTask(Level.INFO, "\n\t\tCheck ob lastCompletitionDate '" + completionStr + "' nach " + startedStr + " ist...");
                if (TesunDateUtils.isSameOrAfter(completion, jobStartedCal)) {
                    if ("COMPLETED".equals(status)) {
                        notifyUserTask(Level.INFO, "\nProzess '" + processIdentifier + "' wurde beendet.");
                        return;
                    }
                    throw new TimeoutException("Der Prozess '" + processIdentifier + "' wurde mit dem Status " + status + " abgebrochen!");
                }
            }
            notifyUserTask(Level.INFO, ".");
            Thread.sleep(sleepMillis);
        }
        throw new TimeoutException("TimeOut beim Prozess '" + processIdentifier + "'!");
    }
}
