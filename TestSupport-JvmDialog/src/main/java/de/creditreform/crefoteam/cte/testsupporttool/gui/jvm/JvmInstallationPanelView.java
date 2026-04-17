package de.creditreform.crefoteam.cte.testsupporttool.gui.jvm;

import de.creditreform.crefoteam.cte.jvmclient.JvmInstallation;
import de.creditreform.crefoteam.cte.jvmclient.JvmJobExecutionInfo;
import de.creditreform.crefoteam.cte.jvmclient.JvmJobInfo;
import de.creditreform.crefoteam.cte.testsupporttool.gui.jvm.design.JvmInstallationPanel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

public class JvmInstallationPanelView extends JvmInstallationPanel {

    private final JTree treeJVMs;
    private final JvmInstallationTreeNode jvmInstallationTreeNode;
    private final JvmInstallation jvmInstallation;

    public JvmInstallationPanelView(JTree treeJVMs, JvmInstallationTreeNode jvmInstallationTreeNode) {
        super();
        this.treeJVMs = treeJVMs;
        this.jvmInstallationTreeNode = jvmInstallationTreeNode;
        this.jvmInstallation = (JvmInstallation) this.jvmInstallationTreeNode.getUserObject();
        initControls();
        doRefreshJvmJobs();
        initListeners();
    }

    private void initControls() {
        getTextFieldJvmName().setText(jvmInstallation.getJvmName());
        getTextFieldJvmURL().setText(jvmInstallation.getJvmUrl());
        getProgressBar().setMinimum(0);
        getProgressBar().setMaximum(100);
        getProgressBar().setValue(0);
        getProgressBar().setStringPainted(true);
        getSplitPaneJobs().setDividerLocation(320);
        getPanelJvmJobs().getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        getPanelJvmJobs().getTable().setDefaultRenderer(JvmJobInfo.class, new JvmJobInfoTableCellRenderer());
        getTextAreaInfo().setLineWrap(true);
    }

    private void initListeners() {
        getButtonStartJobs().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                doStartJobs();
            }
        });
        getButtonRefreshJvmJobs().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                doRefreshJvmJobs();
            }
        });
    }

    private void doRefreshJvmJobs() {
        getButtonRefreshJvmJobs().setEnabled(false);
        getButtonStartJobs().setEnabled(false);
        Map<String, JvmInstallation> jvmInstallationMap = new TreeMap<>();
        jvmInstallationMap.put(jvmInstallation.getJvmName(), jvmInstallation);
        JvmJobLoaderSwingWorker jvmJobLoaderSwingWorker = new JvmJobLoaderSwingWorker(this, getPanelJvmJobs(), jvmInstallationTreeNode, jvmInstallationMap, new Predicate<String>() {
            @Override
            public boolean test(String input) {
                return true;
            }
        });
        jvmJobLoaderSwingWorker.addJvmJobSwingWorkerListener(new JvmJobSwingWorkerListener() {
            @Override
            public void notifyForProgress(int progressValue) {
                getProgressBar().setValue(progressValue);
            }

            @Override
            public void notifyForInfo(String strInfo) {
                getTextAreaInfo().append(strInfo);
            }

            @Override
            public void notifyFinished() {
                getButtonRefreshJvmJobs().setEnabled(true);
                getButtonStartJobs().setEnabled(true);
                TableModel model = getPanelJvmJobs().getModel();
                if (model.getRowCount() > 0) {
                    getPanelJvmJobs().getTable().setRowSelectionInterval(0, 0);
                }
                treeJVMs.setSelectionPath(new TreePath(treeJVMs.getModel().getRoot()));
                treeJVMs.setSelectionPath(new TreePath(jvmInstallationTreeNode.getPath()));
                getProgressBar().setValue(0);
            }
        });
        jvmJobLoaderSwingWorker.execute();
    }

    private void doStartJobs() {
        getButtonRefreshJvmJobs().setEnabled(false);
        getButtonStartJobs().setEnabled(false);
        GUIStaticUtils.setWaitCursor(this, true);
        JobJobInfoTableModel tableModel = (JobJobInfoTableModel) getPanelJvmJobs().getModel();
        int[] selectedRows = getPanelJvmJobs().getTable().getSelectedRows();
        List<JvmJobInfo> jvmJobInfoList = new ArrayList<>();
        for (int selectedRow : selectedRows) {
            int rowInModel = getPanelJvmJobs().getTable().convertRowIndexToModel(selectedRow);
            JvmJobInfo jvmJobInfo = (JvmJobInfo) tableModel.getRow(rowInModel);
            jvmJobInfoList.add(jvmJobInfo);
        }
        Map<String, JvmInstallation> jvmInstallationMap = new TreeMap<>();
        jvmInstallationMap.put(jvmInstallation.getJvmName(), jvmInstallation);
        JvmJobStarterSwingWorker jvmJobStarterSwingWorker = new JvmJobStarterSwingWorker(this, jvmInstallationMap, jvmJobInfoList);
        jvmJobStarterSwingWorker.addJvmJobSwingWorkerListener(new JvmJobSwingWorkerListener() {
            @Override
            public void notifyForProgress(int progressValue) {
                getProgressBar().setValue(progressValue);
            }

            @Override
            public void notifyForInfo(String strInfo) {
                getTextAreaInfo().append(strInfo);
            }

            @Override
            public void notifyFinished() {
                getButtonRefreshJvmJobs().setEnabled(true);
                getButtonStartJobs().setEnabled(true);
            }
        });
        jvmJobStarterSwingWorker.execute();
    }

    private class JvmJobInfoTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            component.setForeground(JvmConstants.DEFAULT_COLOR);
            final JvmJobInfo jvmJobInfo = (JvmJobInfo) value;
            List<JvmJobExecutionInfo> jobExecutionsList = jvmJobInfo.getJobExecutionsList();
            for (JvmJobExecutionInfo jvmJobExecutionInfo : jobExecutionsList) {
                if (jvmJobExecutionInfo.getRunning().equals("true")) {
                    component.setForeground(JvmConstants.RUNNING_COLOR);
                    break;
                }
            }
            table.repaint();
            return component;
        }
    }
}
