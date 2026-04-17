package de.creditreform.crefoteam.cte.testsupporttool.gui.jvm;

import de.creditreform.crefoteam.cte.jvmclient.JobStartResponse;
import de.creditreform.crefoteam.cte.jvmclient.JobStatusResponse;
import de.creditreform.crefoteam.cte.jvmclient.JvmInstallation;
import de.creditreform.crefoteam.cte.jvmclient.JvmJobInfo;
import de.creditreform.crefoteam.cte.jvmclient.JvmRestClient;
import de.creditreform.crefoteam.cte.jvmclient.JvmRestClientImpl;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class JvmJobStarterSwingWorker extends SwingWorker<List<JvmJobInfo>, Void> {

    private final List<JvmJobSwingWorkerListener> listenersList = new ArrayList<>();

    private final Component parent;
    private final Map<String, JvmInstallation> jvmInstallationMap;
    private final List<JvmJobInfo> jvmJobInfosList;

    public JvmJobStarterSwingWorker(Component parent, Map<String, JvmInstallation> jvmInstallationMap, List<JvmJobInfo> jvmJobInfosList) {
        this.parent = parent;
        this.jvmInstallationMap = jvmInstallationMap;
        this.jvmJobInfosList = jvmJobInfosList;
    }

    public void addJvmJobSwingWorkerListener(JvmJobSwingWorkerListener listener) {
        listenersList.add(listener);
    }

    @Override
    protected List<JvmJobInfo> doInBackground() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        try {
            Map<String, FutureTask<JvmJobInfo>> jobExecInfoMap = new HashMap<>();
            for (JvmJobInfo jvmJobInfo : jvmJobInfosList) {
                // Den Job-Status abfragen und warten bis fertich...
                String jobName = jvmJobInfo.getJobName();
                JvmInstallation jvmInstallation = jvmInstallationMap.get(jvmJobInfo.getJvmName());
                AtomicBoolean abortFlag = new AtomicBoolean();
                JvmRestClient jvmRestClient = new JvmRestClientImpl(jvmInstallation.getJvmUrl(), abortFlag);
                JobExecutionInfoCallable<JvmJobInfo> callable = new JobExecutionInfoCallable<>(jvmJobInfo, jvmRestClient, new JobExecutionInfoCallableListener() {
                    @Override
                    public void notifyForInfo(String strInfo) {
                        notifyListenerForInfo(strInfo);
                    }

                    @Override
                    public void notifyForProgress(int progressValue) {
                        notifyListenerForProgress(progressValue);
                    }
                });
                FutureTask<JvmJobInfo> futureTask = new FutureTask<>(callable);
                jobExecInfoMap.put(jobName, futureTask);
                executor.execute(futureTask);
                Thread.sleep(100);
            }
            // FutureTask's holen...
            for (Map.Entry<String, FutureTask<JvmJobInfo>> entry : jobExecInfoMap.entrySet()) {
                FutureTask<JvmJobInfo> futureTask = entry.getValue();
                JvmJobInfo jvmJobInfo = futureTask.get();
                notifyListenerForInfo(String.format("\nJVM-Job '%s' ist beendet.", jvmJobInfo.getJobName()));
            }
        } catch (Exception ex) {
            GUIStaticUtils.showExceptionMessage(parent, "Fehler beim Starten des JVM-Jobs", ex);
        } finally {
            executor.shutdown();
        }
        return jvmJobInfosList;
    }

    @Override
    protected void done() {
        notifyListenerForProgress(0);
        notifyListenerForFinish();
        GUIStaticUtils.setWaitCursor(parent, false);
        super.done();
    }

    protected void notifyListenerForInfo(String strInfo) {
        for (JvmJobSwingWorkerListener listener : listenersList) {
            listener.notifyForInfo(strInfo);
        }
    }

    protected void notifyListenerForProgress(int progressVal) {
        for (JvmJobSwingWorkerListener listener : listenersList) {
            listener.notifyForProgress(++progressVal % 100);
        }
    }

    protected void notifyListenerForFinish() {
        for (JvmJobSwingWorkerListener listener : listenersList) {
            listener.notifyFinished();
        }
    }

    protected interface JobExecutionInfoCallableListener {
        void notifyForInfo(String strInfo);

        void notifyForProgress(int progressValue);
    }

    protected static class JobExecutionInfoCallable<E> implements Callable<JvmJobInfo> {
        private final JvmJobInfo jvmJobInfo;
        private final JvmRestClient jvmRestClient;
        private final JobExecutionInfoCallableListener listener;

        public JobExecutionInfoCallable(JvmJobInfo jvmJobInfo, JvmRestClient jvmRestClient, JobExecutionInfoCallableListener listener) {
            this.jvmJobInfo = jvmJobInfo;
            this.jvmRestClient = jvmRestClient;
            this.listener = listener;
        }

        @Override
        public JvmJobInfo call() throws Exception {
            // Job starten...
            String jobName = jvmJobInfo.getJobName();
            listener.notifyForInfo(String.format("\nStarte JVM-Job '%s'...", jobName));
            JobStartResponse jobStartResponse = jvmRestClient.startJob(jobName, null);
            jvmJobInfo.setProcessId(jobStartResponse.getJobId());
            listener.notifyForInfo(String.format("\n\tJVM-Job '%s' gestartet; Prozess-ID ist %s", jobName, jobStartResponse.getJobId()));

            // Den Job-Status abfragen und warten bis fertich...
            JobStatusResponse jvmJobStatus = jvmRestClient.getJobStatus(jvmJobInfo.getJobName(), jvmJobInfo.getProcessId());
            listener.notifyForInfo(String.format("\nWarte, bis der JVM-Job '%s' beendet ist...", jvmJobInfo.getJobName()));
            int progressVal = 0;
            while (jvmJobStatus.getRunning().equals("true")) {
                Thread.sleep(500);
                listener.notifyForProgress(++progressVal);
                listener.notifyForInfo(".");
                jvmJobStatus = jvmRestClient.getJobStatus(jvmJobInfo.getJobName(), jvmJobInfo.getProcessId());
            }
            return jvmJobInfo;
        }
    }
}
