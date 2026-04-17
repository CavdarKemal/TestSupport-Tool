package de.creditreform.crefoteam.cte.testsupporttool.gui.jvm;

import de.creditreform.crefoteam.cte.jvmclient.JobStartResponse;
import de.creditreform.crefoteam.cte.jvmclient.JvmInstallation;
import de.creditreform.crefoteam.cte.jvmclient.JvmJobExecutionInfo;
import de.creditreform.crefoteam.cte.jvmclient.JvmJobInfo;
import de.creditreform.crefoteam.cte.jvmclient.JvmRestClient;
import de.creditreform.crefoteam.cte.jvmclient.JvmRestClientImpl;
import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.ColumnsInfo;
import de.creditreform.crefoteam.cte.testsupporttool.gui.jvm.design.JvmJobInfoPanel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class JvmJobInfoPanelView extends JvmJobInfoPanel implements ListSelectionListener {
    private final static ColumnsInfo[] table_ColumnsInfo;

    static {
        table_ColumnsInfo = new ColumnsInfo[]{
                new ColumnsInfo(40, 40, 40), // #
                new ColumnsInfo(150, 150, 0), // iD | JobID
                new ColumnsInfo(70, 70, 70), // Running
                new ColumnsInfo(130, 130, 130), // StartDate
                new ColumnsInfo(130, 130, 130), // EndDate
                new ColumnsInfo(90, 90, 90), // Status
                new ColumnsInfo(90, 90, 90), // ExitCode
        };
    }

    private final JvmJobInfoTreeNode jvmJobInfoTreeNode;
    private final JvmJobInfo jvmJobInfo;
    private final JvmInstallation jvmInstallation;

    public JvmJobInfoPanelView(JvmJobInfoTreeNode jvmJobInfoTreeNode, JvmInstallation jvmInstallation) {
        super();
        this.jvmJobInfoTreeNode = jvmJobInfoTreeNode;
        this.jvmInstallation = jvmInstallation;
        this.jvmJobInfo = (JvmJobInfo) jvmJobInfoTreeNode.getUserObject();
        initControls();
        initListeners();
    }

    private void initControls() {
        doRefreshJobExecs();
        getTextFieldJobName().setText(jvmJobInfo.getJobName());
        getProgressBar().setMinimum(0);
        getProgressBar().setMaximum(100);
        getProgressBar().setValue(0);
        getSplitPaneJobExecutions().setDividerLocation(320);
        getPaneJobExecutions().getTable().setDefaultRenderer(JvmJobExecutionInfo.class, new JvmJobExecutionInfoTableCellRenderer());
        getPaneJobExecutions().addListSelectionListener(this);
        getTextAreaInfo().setLineWrap(true);
        getButtonAbortJob().setEnabled(false);
    }

    private void initListeners() {
        getButtonStartJob().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                doStartJob();
            }
        });
        getButtonAbortJob().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                doCancelJob();
            }
        });
        getButtonRefreshJobExecs().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                doRefreshJobExecs();
            }
        });
    }

    private void doRefreshJobExecs() {
        GUIStaticUtils.setWaitCursor(this, true);
        try {
            jvmJobInfoTreeNode.removeAllChildren();
            jvmJobInfoTreeNode.setAllowsChildren(true);
            JvmRestClient jvmRestClient = new JvmRestClientImpl(jvmInstallation.getJvmUrl(), new AtomicBoolean());
            List<JvmJobExecutionInfo> jvmJobExecutionInfos = jvmRestClient.readJobExecutions(jvmJobInfo.getJobName());
            List<JvmJobExecutionInfo> jobExecutionsList = jvmJobInfo.getJobExecutionsList();
            jobExecutionsList.clear();
            jobExecutionsList.addAll(jvmJobExecutionInfos);
            for (JvmJobExecutionInfo jobExecutionInfo : jobExecutionsList) {
                JvmJobExecutionTreeNode jvmJobExecutionTreeNode = new JvmJobExecutionTreeNode(jobExecutionInfo);
                jvmJobInfoTreeNode.add(jvmJobExecutionTreeNode);
            }
            getTextFieldExecCnt().setText(jvmJobInfo.getExecutionCount() + "");
            JobExecutionTableModel tableModel = new JobExecutionTableModel(jobExecutionsList);
            getPaneJobExecutions().setModel(null, tableModel, table_ColumnsInfo);
            Thread.sleep(100);
        } catch (Exception ex) {
            GUIStaticUtils.showExceptionMessage(this, "Fehler beim Starten des JVM-Jobs", ex);
        }
        GUIStaticUtils.setWaitCursor(this, false);
    }

    private void doCancelJob() {
        int selectedRow = getPaneJobExecutions().getSelectedRow();
        JobExecutionTableModel jobExecutionTableModel = (JobExecutionTableModel) getPaneJobExecutions().getModel();
        JvmJobExecutionInfo jvmJobExecutionInfo = (JvmJobExecutionInfo) jobExecutionTableModel.getRow(selectedRow);
        if (!"true".equals(jvmJobExecutionInfo.getRunning())) {
            return;
        }
        Thread abortThread = new Thread(new Runnable() {
            @Override
            public void run() {
                GUIStaticUtils.setWaitCursor(JvmJobInfoPanelView.this, true);
                getButtonRefreshJobExecs().setEnabled(false);
                getButtonStartJob().setEnabled(false);
                getButtonAbortJob().setEnabled(false);
                String jobName = jvmJobInfo.getJobName();
                String jobId = jvmJobExecutionInfo.getId();
                getTextAreaInfo().append(String.format("\nSende Abbruch für den JVM-Job '%s{%s}'...", jobName, jobId));
                JvmRestClient jvmRestClient = new JvmRestClientImpl(jvmInstallation.getJvmUrl(), new AtomicBoolean());
                try {
                    jvmRestClient.abortJob(jobName, jobId);
                    getTextAreaInfo().append(String.format("\nJVM-Job '%s{%s}' wurde abgebrochen", jobName, jobId));
                } catch (Exception ex) {
                    GUIStaticUtils.showExceptionMessage(JvmJobInfoPanelView.this, "Fehler beim Abbrechen des JVM-Jobs", ex);
                }
                initControls();
                GUIStaticUtils.setWaitCursor(JvmJobInfoPanelView.this, false);
                getButtonRefreshJobExecs().setEnabled(true);
                getButtonStartJob().setEnabled(true);
                getButtonAbortJob().setEnabled(true);
            }
        });
        abortThread.start();
    }

    private void doStartJob() {
        Thread startThread = new Thread(new Runnable() {
            @Override
            public void run() {
                GUIStaticUtils.setWaitCursor(JvmJobInfoPanelView.this, true);
                getButtonRefreshJobExecs().setEnabled(false);
                getButtonStartJob().setEnabled(false);
                getButtonAbortJob().setEnabled(true);
                String jobName = jvmJobInfo.getJobName();
                getTextAreaInfo().append(String.format("\nStarte JVM-Job '%s'...", jobName));
                JvmRestClient jvmRestClient = new JvmRestClientImpl(jvmInstallation.getJvmUrl(), new AtomicBoolean());
                try {
                    JobStartResponse jobStartResponse = jvmRestClient.startJob(jobName, null);
                    String jobId = jobStartResponse.getJobId();
                    if (jobId != null) {
                        getTextAreaInfo().append(String.format("\n\tJVM-Job '%s' gestartet; Prozess-ID ist %s", jobName, jobId));
                    }
                } catch (Exception ex) {
                    GUIStaticUtils.showExceptionMessage(JvmJobInfoPanelView.this, "Fehler beim Abbrechen des JVM-Jobs", ex);
                }
                initControls();
                GUIStaticUtils.setWaitCursor(JvmJobInfoPanelView.this, false);
                getButtonRefreshJobExecs().setEnabled(true);
                getButtonStartJob().setEnabled(true);
                getButtonAbortJob().setEnabled(true);
            }
        });
        startThread.start();
    }

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (listSelectionEvent.getValueIsAdjusting()) {
            return;
        }
        int selectedRow = getPaneJobExecutions().getSelectedRow();
        if (selectedRow > -1) {
            JobExecutionTableModel jobExecutionTableModel = (JobExecutionTableModel) getPaneJobExecutions().getModel();
            JvmJobExecutionInfo jvmJobExecutionInfo = (JvmJobExecutionInfo) jobExecutionTableModel.getRow(selectedRow);
            getButtonAbortJob().setEnabled("true".equals(jvmJobExecutionInfo.getRunning()));
        }
    }

    private class JvmJobExecutionInfoTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            component.setFont(new Font("Verdana", Font.ITALIC, 11));
            JvmJobExecutionInfo jvmJobExecutionInfo = (JvmJobExecutionInfo) value;
            if ("true".equals(jvmJobExecutionInfo.getRunning())) {
                component.setForeground(JvmConstants.RUNNING_COLOR);
            } else {
                component.setForeground(JvmInstallationsTreeCellRenderer.calcColorForJvmJobExecutionInfo(jvmJobExecutionInfo));
            }
            table.repaint();
            return component;
        }
    }
}
