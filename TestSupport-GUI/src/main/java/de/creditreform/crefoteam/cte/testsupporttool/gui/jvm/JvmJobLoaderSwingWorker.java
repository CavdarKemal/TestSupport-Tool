package de.creditreform.crefoteam.cte.testsupporttool.gui.jvm;

import de.creditreform.crefoteam.cte.jvmclient.JvmInstallation;
import de.creditreform.crefoteam.cte.jvmclient.JvmJobInfo;
import de.creditreform.crefoteam.cte.jvmclient.JvmRestClient;
import de.creditreform.crefoteam.cte.jvmclient.JvmRestClientImpl;
import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.AbstractMutableTreeNode;
import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.ColumnsInfo;
import de.creditreform.crefoteam.cte.testsupporttool.gui.base.view.TableWithButtonsView;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class JvmJobLoaderSwingWorker extends SwingWorker<List<JvmJobInfo>, Void> {
    private final static ColumnsInfo[] table_ColumnsInfo;

    static {
        table_ColumnsInfo = new ColumnsInfo[]{
                new ColumnsInfo(40, 40, 40), // #
                new ColumnsInfo(140, 140, 0), // Name
                new ColumnsInfo(80, 80, 80), // Exec-Count
        };
    }

    private final List<JvmJobSwingWorkerListener> listenersList = new ArrayList<>();
    private final Component parent;
    private final TableWithButtonsView tableView;
    private final AbstractMutableTreeNode parentNode;
    private final Map<String, JvmInstallation> jvmInstallationMap;
    private final Predicate<String> groupPredicate;

    private final AtomicBoolean abortFlag = new AtomicBoolean();

    public JvmJobLoaderSwingWorker(Component parent,
                                   TableWithButtonsView tableView,
                                   AbstractMutableTreeNode parentNode,
                                   Map<String, JvmInstallation> jvmInstallationMap,
                                   Predicate<String> groupPredicate) {
        this.parent = parent;
        this.tableView = tableView;
        this.parentNode = parentNode;
        this.jvmInstallationMap = jvmInstallationMap;
        this.groupPredicate = groupPredicate;
    }

    public void addJvmJobSwingWorkerListener(JvmJobSwingWorkerListener listener) {
        listenersList.add(listener);
    }

    @Override
    protected List<JvmJobInfo> doInBackground() throws Exception {
        GUIStaticUtils.setWaitCursor(parent, true);
        List<JvmJobInfo> jvmJobInfosList = new ArrayList<>();
        JobJobInfoTableModel tableModel = new JobJobInfoTableModel(jvmJobInfosList);
        tableView.setModel(null, tableModel, table_ColumnsInfo);
        try {
            parentNode.removeAllChildren();
            parentNode.setAllowsChildren(true);
            int progressVal = 0;
            for (Map.Entry<String, JvmInstallation> jvmInstallatiEnentry : jvmInstallationMap.entrySet()) {
                JvmInstallation jvmInstallation = jvmInstallatiEnentry.getValue();
                JvmRestClient jvmRestClient = new JvmRestClientImpl(jvmInstallation.getJvmUrl(), abortFlag);
                notifyListenerForInfo(String.format("\nLese Job-Liste aus '%s'...", jvmInstallation.getJvmName()));
                try {
                    List<JvmJobInfo> jvmJobInfoList = jvmRestClient.readJvmJobInfos(jvmInstallation.getJvmName());
                    notifyListenerForProgress(++progressVal);
                    jvmInstallation.getJvmJobInfosList().clear();
                    jvmInstallation.getJvmJobInfosList().addAll(jvmJobInfoList);
                    notifyListenerForInfo("\n\tInitialisiere die JvmJobInfoTreeNode's...");
                    for (JvmJobInfo jvmJobInfo : jvmJobInfoList) {
                        if (groupPredicate.test(jvmJobInfo.getJobName())) {
                            JvmJobInfoTreeNode jvmJobInfoTreeNode = new JvmJobInfoTreeNode(jvmJobInfo);
                            jvmJobInfoTreeNode.add(new JvmJobExecutionTreeNode(null)); // temporäres Node-Element, wird bei Lazy-Loading entfernt!
                            parentNode.add(jvmJobInfoTreeNode);
                            jvmJobInfosList.add(jvmJobInfo);
                        }
                    }
                } catch (Exception ex) {
                    notifyListenerForInfo(String.format("\n\tFehler beim Initialisieren der JvmJobInfoTreeNode für '" + jvmInstallation.getJvmName() + "'!"));
                }
            }
        } catch (Exception ex) {
            GUIStaticUtils.showExceptionMessage(parent, "Fehler beim Starten des JVM-Jobs", ex);
        }
        return jvmJobInfosList;
    }

    @Override
    protected void done() {
        try {
            List<JvmJobInfo> jvmJobInfosList = get();
            notifyListenerForInfo("\nInitialisiere Tabelle...");
            JobJobInfoTableModel tableModel = new JobJobInfoTableModel(jvmJobInfosList);
            tableView.setModel(null, tableModel, table_ColumnsInfo);
        } catch (Exception ex) {
            GUIStaticUtils.showExceptionMessage(parent, "Fehler beim Starten des JVM-Jobs", ex);
        }
        super.done();
        notifyListenerForProgress(0);
        notifyListenerForFinish();
        GUIStaticUtils.setWaitCursor(parent, false);
    }

    private void notifyListenerForInfo(String strInfo) {
        for (JvmJobSwingWorkerListener listener : listenersList) {
            listener.notifyForInfo(strInfo);
        }
    }

    private void notifyListenerForProgress(int progressVal) {
        for (JvmJobSwingWorkerListener listener : listenersList) {
            listener.notifyForProgress(++progressVal % 100);
        }
    }

    private void notifyListenerForFinish() {
        for (JvmJobSwingWorkerListener listener : listenersList) {
            listener.notifyFinished();
        }
    }
}
