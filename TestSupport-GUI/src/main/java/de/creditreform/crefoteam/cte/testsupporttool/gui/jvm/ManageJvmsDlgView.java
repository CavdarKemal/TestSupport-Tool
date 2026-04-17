package de.creditreform.crefoteam.cte.testsupporttool.gui.jvm;

import de.creditreform.crefoteam.cte.jvmclient.JvmInstallation;
import de.creditreform.crefoteam.cte.jvmclient.JvmJobInfo;
import de.creditreform.crefoteam.cte.testsupporttool.gui.jvm.design.ManageJvmsDlg;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.rest.TesunRestService;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import org.apache.log4j.Level;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ManageJvmsDlgView extends ManageJvmsDlg implements TreeSelectionListener, TreeWillExpandListener {

    private final EnvironmentConfig environmentConfig;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    private Map<String, JvmInstallation> jvmInstallationsMap;
    private JvmInstallationsTreeModel jvmInstallationsTreeModel;
    private final Map<String, Component> viewsMap = new TreeMap<>();
    private JPanel theCardPanel;

    public ManageJvmsDlgView(Frame owner, String strTitle, EnvironmentConfig environmentConfig) {
        super(owner);
        this.environmentConfig = environmentConfig;
        setTitle(strTitle);
        setModal(true);
        setSize(1064, 800);
        getSpinnerAutoRefreshRate().setValue(Integer.valueOf(10));
        getSplitPaneTreeView().setDividerLocation(0.8);
        getSpinnerAutoRefreshRate().setValue(Long.valueOf(3));
        getCheckAutoBoxRefresh().setSelected(Boolean.TRUE);
        setLocationRelativeTo(owner);
        initEnvironmentsCombo();
        getCheckAutoBoxRefresh().setSelected(false);
        initListeners();
        doRefreshJVMs();
    }

    private void initListeners() {
        getButtonClose().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                doClose();
            }
        });
        getButtonRefreshJVMs().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                doRefreshJVMs();
            }
        });
        getComboBoxEnvironment().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doRefreshJVMs();
            }
        });
        getTreeJVMs().addTreeSelectionListener(this);
    }

    private void initEnvironmentsCombo() {
        DefaultComboBoxModel environmentsModel = new DefaultComboBoxModel();
        Map<String, File> environmentsMap = EnvironmentConfig.getEnvironmentsMap();
        if ((environmentsMap == null) || (environmentsMap.isEmpty())) {
            String exceptionErr = "Es konnten im aktuellen Verzeichnis '" + System.getProperty("user.dir") + "'\nkeine Konfigurationsdateien '{ENE|GEE|ABE}-config.properties' gefunden werden!";
            GUIStaticUtils.showExceptionMessage(this, "Konfiguration laden", new RuntimeException(exceptionErr));
            System.exit(-1);
        }
        Iterator<String> envNamesIterator = environmentsMap.keySet().iterator();
        while (envNamesIterator.hasNext()) {
            String envName = envNamesIterator.next();
            environmentsModel.addElement(envName);
        }
        getComboBoxEnvironment().setModel(environmentsModel);
        String selectedEnvironment = environmentConfig.getCurrentEnvName();
        if ((selectedEnvironment != null) && environmentsMap.containsKey(selectedEnvironment)) {
            getComboBoxEnvironment().setSelectedItem(selectedEnvironment);
        } else {
            getComboBoxEnvironment().setSelectedIndex(0);
        }
    }

    protected void doRefreshJVMs() {
        this.setEnabled(false);
        GUIStaticUtils.setWaitCursor(this, true);
        try {
            String currEnvName = (String) getComboBoxEnvironment().getSelectedItem();
            environmentConfig.loadEnvironmentConfig(currEnvName);
            TesunRestService tesunRestServiceCteBatchGUI = new TesunRestService(environmentConfig.getRestServiceConfigsForBatchGUI().get(0), new TesunClientJobListener() {
                @Override
                public void notifyClientJob(Level level, Object notifyObject) {
                    // TODO
                }

                @Override
                public Object askClientJob(ASK_FOR askFor, Object userObject) {
                    return null;
                }
            });
            // Neue Liste der JVM's auslesen und Tree-Model aufbauen
            jvmInstallationsMap = new TreeMap<>();
            Map<String, String> jvmNameToUrlMap = tesunRestServiceCteBatchGUI.getJvmInstallationMap();
            for (Map.Entry<String, String> entry : jvmNameToUrlMap.entrySet()) {
                JvmInstallation jvmInstallation = new JvmInstallation();
                jvmInstallation.setJvmName(entry.getKey());
                jvmInstallation.setJvmUrl(entry.getValue());
                jvmInstallationsMap.put(jvmInstallation.getJvmName(), jvmInstallation);
            }
            jvmInstallationsTreeModel = new JvmInstallationsTreeModel(jvmInstallationsMap);
            getTreeJVMs().setModel(jvmInstallationsTreeModel);
            getTreeJVMs().setRootVisible(false);
            getTreeJVMs().setShowsRootHandles(true);
            getTreeJVMs().addTreeWillExpandListener(this);
            getTreeJVMs().setSelectionRow(0);
            getTreeJVMs().setCellRenderer(new JvmInstallationsTreeCellRenderer());
            viewsMap.clear();
            theCardPanel = (JPanel) getSplitPaneTreeView().getRightComponent();
            theCardPanel.removeAll();
            getTreeJVMs().setSelectionPath(null);
            getTreeJVMs().setSelectionPath(new TreePath(jvmInstallationsTreeModel.getRoot()));
        } catch (Exception ex) {
            GUIStaticUtils.showExceptionMessage(this, "Aktualisieren der JVM-Liste", ex);
        }
        GUIStaticUtils.setWaitCursor(this, false);
        this.setEnabled(true);
    }

    private void doClose() {
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
        this.dispose();
        this.setVisible(false);
    }

    @Override
    public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
        if (theCardPanel == null) {
            return;
        }
        int dividerLocation = getSplitPaneTreeView().getDividerLocation();
        final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) getTreeJVMs().getLastSelectedPathComponent();
        if (selectedNode == null) {
            return;
        }
        GUIStaticUtils.setWaitCursor(this, true);
        String key = selectedNode.toString();
        Component theView = viewsMap.get(key);
        if (theView == null) {
            if (selectedNode instanceof JvmInstallationsTreeNode) {
                theView = new JvmInstallationsPanelView((JvmInstallationsTreeNode) selectedNode);
            } else if (selectedNode instanceof JvmInstallationTreeNode) {
                JvmInstallation jvmInstallation = (JvmInstallation) selectedNode.getUserObject();
                theView = new JvmInstallationPanelView(getTreeJVMs(), (JvmInstallationTreeNode) selectedNode);
            } else if (selectedNode instanceof JvmJobInfoTreeNode) {
                JvmJobInfo jvmJobInfo = (JvmJobInfo) selectedNode.getUserObject();
                JvmInstallation jvmInstallation = jvmInstallationsMap.get(jvmJobInfo.getJvmName());
                theView = new JvmJobInfoPanelView((JvmJobInfoTreeNode) selectedNode, jvmInstallation);
            } else if (selectedNode instanceof JvmJobExecutionTreeNode) {
                theView = new JvmJobExecutionsPanelView((JvmJobExecutionTreeNode) selectedNode);
            } else if (selectedNode instanceof JvmSpecialJobsTreeNode) {
                theView = new JvmSpecialJobsPanelView(getTreeJVMs(), (JvmSpecialJobsTreeNode) selectedNode);
            }
            if (theView != null) {
                viewsMap.put(key, theView);
                theCardPanel.add(key, theView);
            }
        }
        if (theView != null) {
            CardLayout cardLayout = (CardLayout) (theCardPanel.getLayout());
            cardLayout.show(theCardPanel, key);
            getSplitPaneTreeView().setDividerLocation(dividerLocation);
        }
        GUIStaticUtils.setWaitCursor(this, false);
    }

    @Override
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        getTreeJVMs().setSelectionPath(event.getPath());
        valueChanged(null);
    }

    @Override
    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
    }
}
