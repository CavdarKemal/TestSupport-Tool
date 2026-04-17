package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design.SearchResultsPanel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.model.SearchResultsTreeModel;
import de.creditreform.crefoteam.cte.tesun.util.TesunUtilites;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.config.SearchConfigurationFactory;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.*;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ZipSearcResult.ZipEntryInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ZipSearcResult.ZipFileInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.handler.LogInfo;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

import static de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ProgressListenerIF.LOG_LEVEL.DEBUG;
import static de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ProgressListenerIF.LOG_LEVEL.INFO;

public class SearchResultsView extends SearchResultsPanel implements ProgressListenerGUI {
    private List<SwingWorker> xmlSearchSwingWorkerList;
    private final Map<String, File> searchLogFilesMap = new TreeMap<>();
    private SearchDefinitionsView viewSearchDefinitions;
    private ProgressListenerIF.LOG_LEVEL currentLogLevel = INFO;
    private final AtomicLong timestampLastUpdate;
    List<JComponent> componentsToOnOff;

    public SearchResultsView() {
        super();
        this.timestampLastUpdate = new AtomicLong(-1);
        getRadioButtonLogLevelINFO().setSelected(true);

        SearchResultsTreeModel theTreeModel = new SearchResultsTreeModel();
        getTreeResults().setModel(theTreeModel);

        ComboBoxModel analyseTypesModel = new DefaultComboBoxModel(ExportResultsAnalyseStrategy.values());
        getComboBoxAnalyse().setModel(analyseTypesModel);

        intiListeners();

        initEnableDisanbleComponentsList();
    }

    private void initEnableDisanbleComponentsList() {
        componentsToOnOff = new ArrayList<>();
        componentsToOnOff.add(getButtonStartStop());
        componentsToOnOff.add(getButtonAnalyse());
        componentsToOnOff.add(getComboBoxAnalyse());
        componentsToOnOff.add(getCheckBoxShowEmptyNodes());
    }

    private void intiListeners() {
        getCheckBoxShowEmptyNodes().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                SearchResultsTreeModel theTreeModel = (SearchResultsTreeModel) getTreeResults().getModel();
                theTreeModel.setShowEmptyNodes(getCheckBoxShowEmptyNodes().isSelected());
            }
        });
        getRadioButtonLogLevelDEBUG().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                currentLogLevel = DEBUG;
            }
        });
        getRadioButtonLogLevelINFO().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                currentLogLevel = ProgressListenerIF.LOG_LEVEL.INFO;
            }
        });
        getRadioButtonLogLevelWARNING().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                currentLogLevel = ProgressListenerIF.LOG_LEVEL.WARN;
            }
        });
        getRadioButtonLogLevelERROR().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                currentLogLevel = ProgressListenerIF.LOG_LEVEL.ERROR;
            }
        });
        getButtonClear().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                getTextAreaLogs().setText("");
            }
        });
        getButtonStartStop().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                doStartStop();
            }
        });
        getButtonAnalyse().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                doStartAnalyse();
            }
        });
        getComboBoxAnalyse().addActionListener(e -> {
            doSelectAnalyseFor();
        });

        getTreeResults().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                doChangeDetailsView();
            }
        });
    }

    private void enableComponentsToOnOff(boolean isEanble) {
        for (JComponent component : componentsToOnOff) {
            component.setEnabled(isEanble);
        }
        if (isEanble) {
        }
    }

    private void doChangeDetailsView() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) getTreeResults().getLastSelectedPathComponent();
        if (selectedNode == null) {
            return;
        }
        getSplitPaneResults().remove(getViewReusltsDetails());
        JPanel theView = null;
        String tabTitle = "";
        SearchResultsTreeModel theTreeModel = (SearchResultsTreeModel) getTreeResults().getModel();
        Object userObject = selectedNode.getUserObject();
        if (userObject instanceof String) {
            theView = new SearcResultsDetailView(theTreeModel.getZipSearcResultList());
            tabTitle = "Suchergebnisse";
        } else if (userObject instanceof ZipSearcResult) {
            theView = new SearcResultDetailView((ZipSearcResult) userObject);
            tabTitle = "Suche";
        } else if (userObject instanceof ZipFileInfo) {
            theView = new SearchResultZipFileDetailView((ZipFileInfo) userObject);
            tabTitle = "ZIP-Datei";
        } else if (userObject instanceof ZipEntryInfo) {
            theView = new SearchResultZipEntryDetailView((ZipEntryInfo) userObject);
            tabTitle = "ZIP-Eintrag";
        } else {
            GUIStaticUtils.showExceptionMessage(SearchResultsView.this, "View-Typ nicht unterstützt!", null);
            return;
        }
        getTabbedPaneDetails().removeTabAt(1);
        getTabbedPaneDetails().addTab(tabTitle, theView);
        getTabbedPaneDetails().setSelectedIndex(1);
    }

    private void appendToLogPanel(String logMessage) {
        final JTextArea textAreaLogs = getTextAreaLogs();
        synchronized (textAreaLogs) {
            textAreaLogs.append(logMessage.replace("\t", "  "));
            JScrollBar verticalScrollBar = getScrollPaneLogs().getVerticalScrollBar();
            verticalScrollBar.setValue(verticalScrollBar.getMaximum());
        }
    }

    private void doStartStop() {
        String actionCommand = getButtonStartStop().getText();
        if (actionCommand.contains("start")) {
            Map<String, SearchSpecification> searcDataMap = viewSearchDefinitions.getModel(true);
            if (checkSearchSpecifications(searcDataMap)) {
                enableComponentsToOnOff(false);
                GUIStaticUtils.setWaitCursor(this, true);
                SearchResultsTreeModel theTreeModel = new SearchResultsTreeModel();
                getTreeResults().setModel(theTreeModel);
                getButtonStartStop().setText("Suche abbrechen");
                getButtonStartStop().setEnabled(true);
                getTextAreaLogs().setText("");
                getProgressBarSearch().setVisible(true);
                getProgressBarSearch().setValue(0);
                getTabbedPaneDetails().setSelectedIndex(0);
                startSwingWorkersForMap(searcDataMap);
            }
        } else {
            cancelSwingWorkers();
        }
    }

    private void doSelectAnalyseFor() {
        getButtonAnalyse().setEnabled(false);
        Map<String, SearchSpecification> searcDataMap = viewSearchDefinitions.getModel(true);
        SearchSpecification searchSpecificationAnalyse = searcDataMap.get("Analyse");
        if (searchSpecificationAnalyse == null) {
            GUIStaticUtils.showExceptionMessage(this, "Analyse der Suchergebnisse", new RuntimeException("Keine Suchdefinition 'Analyse' vorhanden!"));
            return;
        }
        File sourceFile = new File(searchSpecificationAnalyse.getSearchResultsPath(), searchSpecificationAnalyse.getName());
        sourceFile = new File(sourceFile, searchSpecificationAnalyse.getSearchResultsType().name());
        if (!sourceFile.exists()) {
            GUIStaticUtils.showExceptionMessage(this, "Analyse der Suchergebnisse", new RuntimeException("Für die Suchdefinition 'Analyse' wurde noch keine Suche gestartet!"));
            return;
        }
        getButtonAnalyse().setEnabled(true);
    }

    private void doStartAnalyse() {
        String actionCommand = getButtonAnalyse().getText();
        if (actionCommand.contains("start")) {
            Map<String, SearchSpecification> searcDataMap = viewSearchDefinitions.getModel(true);
            SearchSpecification searchSpecificationAnalyse = searcDataMap.get("Analyse");
            if (searchSpecificationAnalyse == null) {
                GUIStaticUtils.showExceptionMessage(this, "Analyse der Suchergebnisse", new RuntimeException("Es wurde keine Suchdefinition 'Analyse'gefunden!"));
                return;
            }
            try {
                enableComponentsToOnOff(false);
                GUIStaticUtils.setWaitCursor(this, true);
                getButtonAnalyse().setText("Analyse abbrechen");
                getButtonAnalyse().setEnabled(true);
                SearchResultsTreeModel theTreeModel = new SearchResultsTreeModel();
                getTreeResults().setModel(theTreeModel);
                getTextAreaLogs().setText("");
                getProgressBarSearch().setVisible(true);
                getProgressBarSearch().setValue(0);
                getTabbedPaneDetails().setSelectedIndex(0);
                ExportResultsAnalyseStrategy exportResultsAnalyseStrategy = (ExportResultsAnalyseStrategy) getComboBoxAnalyse().getSelectedItem();
                XmlStreamListenerGroup listenerGroup = new XmlStreamListenerGroup(this);
                XmlAnalyserSwingWorker xmlAnalyserSwingWorker = new XmlAnalyserSwingWorker(exportResultsAnalyseStrategy, listenerGroup);
                xmlAnalyserSwingWorker.setSearchSpecification(searchSpecificationAnalyse);
                clearXmlSearchSwingWorkerList();
                xmlSearchSwingWorkerList.add(xmlAnalyserSwingWorker);
                xmlAnalyserSwingWorker.execute();
            } catch (Exception ex) {
                GUIStaticUtils.showExceptionMessage(this, "Analyse der Suchergebnisse", ex);
            }
        } else {
            cancelSwingWorkers();
        }
    }

    private void cancelSwingWorkers() {
        xmlSearchSwingWorkerList.stream().forEach(xmlSearchSwingWorker -> {
            xmlSearchSwingWorker.cancel(true);
        });
        enableComponentsToOnOff(true);
    }

    private void startSwingWorkersForMap(Map<String, SearchSpecification> searcDataMap) {
        XmlStreamListenerGroup listenerGroup = new XmlStreamListenerGroup(this);
        clearXmlSearchSwingWorkerList();
        for (Map.Entry<String, SearchSpecification> entry : searcDataMap.entrySet()) {
            XmlSearchSwingWorker xmlSearchSwingWorker = new XmlSearchSwingWorker(listenerGroup);
            xmlSearchSwingWorker.setSearchSpecification(entry.getValue());
            xmlSearchSwingWorkerList.add(xmlSearchSwingWorker);
            createLogFileForWorker(xmlSearchSwingWorker);
            xmlSearchSwingWorker.execute();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                GUIStaticUtils.showExceptionMessage(this, "Man kann hier nicht einmal 1 Sekunde schlafen!", e);
            }
        }
    }

    private void writeLogsForSwingWorker(String searchDefName, String logMessage) {
        File searchLogFile = searchLogFilesMap.get(searchDefName);
        if (searchLogFile != null) {
            try {
                FileUtils.writeStringToFile(searchLogFile, logMessage, true);
            } catch (IOException ex) {
                GUIStaticUtils.showExceptionMessage(this, "Fehler beim Speichern der LOGs", ex);
            }
        }
    }

    private void createLogFileForWorker(XmlSearchSwingWorker xmlSearchSwingWorker) {
        String logFileName = xmlSearchSwingWorker.getSearchSpecification().getName() + ".log";
        try {
            File searchLogFile = new File(System.getProperty("user.dir"), logFileName);
            if (searchLogFile.exists()) {
                FileUtils.copyFile(searchLogFile, new File(System.getProperty("user.dir"), logFileName + ".bak"));
            }
            searchLogFilesMap.put(logFileName.split("\\.")[0], searchLogFile);
        } catch (IOException ex) {
            GUIStaticUtils.showExceptionMessage(this, "Fehler beim Umbenennen der LOG-Datei!" + logFileName, ex);
        }
    }

    private void clearXmlSearchSwingWorkerList() {
        if (xmlSearchSwingWorkerList == null) {
            xmlSearchSwingWorkerList = new ArrayList<>();
        } else {
            while (!xmlSearchSwingWorkerList.isEmpty()) {
                xmlSearchSwingWorkerList.remove(0);
            }
        }
        searchLogFilesMap.clear();
    }

    private boolean checkSearchSpecifications(Map<String, SearchSpecification> searcDataMap) {
        if (searcDataMap.isEmpty()) {
            String errMsg = "Für die Suche muss mindestens ein Suchkriterium aktiviert sein!";
            GUIStaticUtils.showExceptionMessage(this, "Suche starten", new IllegalArgumentException(errMsg));
            return false;
        }
        for (String key : searcDataMap.keySet()) {
            try {
                SearchConfigurationFactory.validate(searcDataMap.get(key));
            } catch (Exception ex) {
                GUIStaticUtils.showExceptionMessage(this, "Suche starten", ex);
                return false;
            }
        }
        return true;
    }

    public void setSearchDefinitionsView(SearchDefinitionsView viewSearchDefinitions) {
        this.viewSearchDefinitions = viewSearchDefinitions;
    }

    @Override
    public void updateProgress(List<Object> chunks) {
        long lastUpdateAt = timestampLastUpdate.get();
        long now = System.currentTimeMillis();
        long elapsed = now - lastUpdateAt;

        int value = getProgressBarSearch().getValue();
        if (value < 1 || value > 99) {
            value = 1; // wrap-around 100->1
        } else {
            value++;
        }
        if (lastUpdateAt < 0 || elapsed > 200) {
            // der erste Aufruf...
            timestampLastUpdate.set(now);
            getProgressBarSearch().setValue(value);
        }
    }

    @Override
    public void updateData(Object dataObject) {
        if (dataObject instanceof LogInfo) {
            LogInfo logInfo = (LogInfo) dataObject;
            if (logInfo.getLogLevel().ordinal() >= currentLogLevel.ordinal()) {
                String logMessage = logInfo.getLogMessage();
                if (!logMessage.endsWith("\n")) {
                    logMessage += "\n";
                }
                if (logInfo.getThrowable() != null) {
                    logMessage += TesunUtilites.buildExceptionMessage(logInfo.getThrowable(), 200);
                }
                appendToLogPanel(logMessage);
                writeLogsForSwingWorker(logInfo.getSearchName(), logMessage);
            }
        } else {
            SearchResultsTreeModel theTreeModel = (SearchResultsTreeModel) getTreeResults().getModel();
            synchronized (theTreeModel) {
                theTreeModel.addNode(dataObject);
            }
        }
    }

    @Override
    public void updateTaskState(ProgressListenerIF.TASK_STATE taskState) {
        switch (taskState) {
            case DONE:
            case CANCELLED: {
                GUIStaticUtils.setWaitCursor(this, false);
                getButtonStartStop().setText("Suche starten");
                getButtonAnalyse().setText("Analyse starten");
                getProgressBarSearch().setVisible(false);
                enableComponentsToOnOff(true);
                // GUIStaticUtils.expandToLast(getTreeResults());
            }
            break;
            case RUNNING: {
            }
            break;
            default: {
                break;
            }
        }
    }

    @Override
    public boolean isCanceled() {
        boolean isCancelled = true;
        for (SwingWorker xmlSearchSwingWorker : xmlSearchSwingWorkerList) {
            isCancelled &= xmlSearchSwingWorker.isCancelled();
        }
        return isCancelled;
    }
}
