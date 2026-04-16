package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.design.TestResultsTabPanel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.model.DifferenceTreeNode;
import de.creditreform.crefoteam.cte.testsupporttool.gui.model.TestResultsTreeModel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.tesun.util.TestFallFileUtil;
import de.creditreform.crefoteam.cte.tesun.util.TestResults;
import de.creditreform.crefoteam.cte.tesun.util.TestResultsZipHandler;
import de.creditreform.crefoteam.cte.tesun.util.TestScenario;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.apache.commons.io.FileUtils;

public class TestResultsTabView extends TestResultsTabPanel implements TreeSelectionListener {
    private TestResultsView testResultsView;
    private EnvironmentConfig environmentConfig;
    private TestResultsZipHandler testResultsZipHandler;
    private Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> testCustomerMapMap;
    private final Map<String, Component> cardViewsMap = new TreeMap<>();
    Component currentCardView;
    private TestResults.DiffenrenceInfo diffenrenceInfo;

    public TestResultsTabView() {
        initListeners();
        getButtonSaveTestResults().setEnabled(false);
        getSplitPaneCustomerTrees().setDividerLocation(200);

        TestCustomerTreeCellRenderer customerTreeCellRenderer = new TestCustomerTreeCellRenderer();
        customerTreeCellRenderer.setFont(getTreeCustomersPhase1().getFont()); // Übernimmt die im Designer gesetzte Font

        getTreeCustomersPhase1().setModel(new TestResultsTreeModel(new HashMap<>()));
        getTreeCustomersPhase1().setCellRenderer(customerTreeCellRenderer);
        Border titleP1 = BorderFactory.createTitledBorder("Phase-1");
        getPanelCustomerP1().setBorder(titleP1);

        getTreeCustomersPhase2().setModel(new TestResultsTreeModel(new HashMap<>()));
        getTreeCustomersPhase2().setCellRenderer(customerTreeCellRenderer);
        Border titleP2 = BorderFactory.createTitledBorder("Phase-2");
        getPanelCustomerP2().setBorder(titleP2);

    }

    public TestResultsTabView(TestResultsView testResultsView, EnvironmentConfig environmentConfig) {
        this();
        this.testResultsView = testResultsView;
        this.environmentConfig = environmentConfig;
        this.testResultsZipHandler = new TestResultsZipHandler();
        initControls();
    }

    private void initControls() {
        try {
            getButtonStartDifTool().setEnabled(false);
            List<File> diffToolsList = environmentConfig.getDiffToolsList();
            diffToolsList.forEach(diffTool -> {
                getComboBoxDiffTools().addItem(diffTool);
            });
        } catch (Exception ex) {
            GUIStaticUtils.showExceptionMessage(this, "Fehler beim Initialisieren!", ex);
        }
    }

    private void initListeners() {
        getButtonLoadTestResults().addActionListener(event -> {
            testResultsView.doLoadTestResultsFromFile();
        });
        getButtonSaveTestResults().addActionListener(event -> {
            try {
                String newChoosenFileName = GUIStaticUtils.chooseFileName(this, environmentConfig.getTestResultsRoot().getAbsolutePath(), "*.zip", true);
                if ((newChoosenFileName == null)) {
                    return;
                }
                Path testResultsFile = Paths.get(newChoosenFileName);
                doSaveTestResults(testResultsFile);
            } catch (PropertiesException e) {
                GUIStaticUtils.showExceptionMessage(TestResultsTabView.this, "Fehler beim Speicher der Results", null);
            }
        });
        getButtonRefreshTestResults().addActionListener(event -> {
            if (testCustomerMapMap != null) {
                initTreeForMap(testCustomerMapMap);
            }
        });
        getButtonStartDifTool().addActionListener(e -> doStartDiffTool());
    }

    private void setDiffenrenceInfo(TestResults.DiffenrenceInfo diffenrenceInfo) {
        this.diffenrenceInfo = diffenrenceInfo;
        getButtonStartDifTool().setEnabled(diffenrenceInfo != null);
    }

    public void doStartDiffTool() {
        File selectedItem = (File) getComboBoxDiffTools().getSelectedItem();
        if (selectedItem == null) {
            return;
        }
        if (diffenrenceInfo == null) {
            return;
        }
        GUIStaticUtils.setWaitCursor(this, true);
        this.setEnabled(false);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                File xmlFileSrc = diffenrenceInfo.getXmlFileSrc();
                File xmlFileDst = diffenrenceInfo.getXmlFileDst();
                List<String> execCmdsList = new ArrayList<>();
                execCmdsList.add(selectedItem.getAbsolutePath());
                execCmdsList.add(xmlFileSrc.getAbsolutePath());
                execCmdsList.add(xmlFileDst.getAbsolutePath());
                ProcessBuilder builder = new ProcessBuilder(execCmdsList);
                try {
                    Process theProcess = builder.start();
                    while (theProcess.isAlive()) {
                        Thread.sleep(300);
                    }
                    theProcess.destroy();
                    String infoFromErrStream = getInfoFromErrorStream(theProcess);
                    if (!infoFromErrStream.isEmpty()) {
                        GUIStaticUtils.showExceptionMessage(TestResultsTabView.this, "Fehler beim Starten des Diff-Tools!\n\t" + infoFromErrStream, null);
                    }
                } catch (Exception ex) {
                    GUIStaticUtils.showExceptionMessage(TestResultsTabView.this, "Fehler beim Starten des Diff-Tools!\n\t", ex);
                }
            }
        });
        thread.start();
        GUIStaticUtils.setWaitCursor(this, false);
        this.setEnabled(true);
    }

    private String getInfoFromErrorStream(Process theProcess) throws IOException {
        StringBuilder strOutput = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(theProcess.getErrorStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            strOutput.append(line);
            strOutput.append("\n");
        }
        return strOutput.toString();
    }

    public void setTestCustomersTableModelMap(Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> testCustomerMapMap) {
        Iterator<TestSupportClientKonstanten.TEST_PHASE> iterator = testCustomerMapMap.keySet().iterator();
        while (iterator.hasNext()) {
            TestSupportClientKonstanten.TEST_PHASE testPhase = iterator.next();
            Map<String, TestCustomer> testCustomerMapX = testCustomerMapMap.get(testPhase);
            JTree treeCustomersPhaseX = testPhase.equals(TestSupportClientKonstanten.TEST_PHASE.PHASE_1) ? getTreeCustomersPhase1() : getTreeCustomersPhase2();
            setTestCustomersTableModel(testCustomerMapX, treeCustomersPhaseX);
        }
    }

    public void setTestCustomersTableModel(Map<String, TestCustomer> testCustomerMap, JTree customersTree) {
        TreeSelectionListener[] treeSelectionListeners = customersTree.getTreeSelectionListeners();
        if (treeSelectionListeners.length > 0) {
            customersTree.removeTreeSelectionListener(treeSelectionListeners[0]);
        }
        TestResultsTreeModel testResultsTreeModel = new TestResultsTreeModel(testCustomerMap);
        customersTree.setModel(testResultsTreeModel);
        customersTree.setSelectionRow(0);
        customersTree.setSelectionPath(null);
        customersTree.setSelectionPath(new TreePath(customersTree.getModel().getRoot()));
        customersTree.addTreeSelectionListener(this::valueChanged);
    }

    public void loadTestResultsFromZipFile(Path testResultsZipFile) {
        GUIStaticUtils.setWaitCursor(this, true);
        this.setEnabled(false);
        testCustomerMapMap = new TreeMap<>();
        try {
            Path otuputPath = testResultsZipHandler.unzipRecursive(testResultsZipFile);
            for (TestSupportClientKonstanten.TEST_PHASE testPhase : TestSupportClientKonstanten.TEST_PHASE.values()) {
                Map<String, TestCustomer> testCustomersMap = testResultsZipHandler.initalizeTestCustomersMapFromDir(otuputPath, testPhase.getDirName());
                if (testCustomersMap != null) {
                    testCustomerMapMap.put(testPhase, testCustomersMap);
                }
            }
            initTreeForMap(testCustomerMapMap);
        } catch (Exception ex) {
            GUIStaticUtils.showExceptionMessage(this, "Fehler beim Laden!", ex);
        }
        GUIStaticUtils.setWaitCursor(this, false);
        this.setEnabled(true);
    }

    public void initTreeForMap(Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> testCustomerMapMap) {
        this.testCustomerMapMap = testCustomerMapMap;
        GUIStaticUtils.setWaitCursor(this, true);
        this.setEnabled(false);
        setTestCustomersTableModelMap(testCustomerMapMap);
        this.setEnabled(true);
        GUIStaticUtils.setWaitCursor(this, false);
    }

    public void doSaveTestResults(Path testResultsZipPath) {
        GUIStaticUtils.setWaitCursor(this, true);
        this.setEnabled(false);
        try {
            Path checkedPathRoot = Paths.get(environmentConfig.getCheckedRoot().getAbsolutePath());
            Iterator<TestSupportClientKonstanten.TEST_PHASE> iterator = testCustomerMapMap.keySet().iterator();
            while (iterator.hasNext()) {
                TestSupportClientKonstanten.TEST_PHASE testPhase = iterator.next();
                Path checkedPath = checkedPathRoot.resolve(testPhase.getDirName());
                StringBuilder stringBuilder = TestFallFileUtil.dumAllCustomers(testCustomerMapMap.get(testPhase));
                try {
                    Path resolved = checkedPath.resolve("TestResults.txt");
                    FileUtils.writeStringToFile(resolved.toFile(), stringBuilder.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            testResultsZipHandler.zipDirectory(checkedPathRoot, testResultsZipPath);
            String message = "Die Test-Ergebnisse sind unter " + testResultsZipPath.toAbsolutePath() + "gespeichert.";
            JOptionPane.showMessageDialog(this, message, "Datei Speichern", JOptionPane.OK_OPTION);
        } catch (Exception ex) {
            GUIStaticUtils.showExceptionMessage(this, "Fehler beim Speichern!", ex);
        }
        GUIStaticUtils.setWaitCursor(this, false);
        this.setEnabled(true);
    }

    public void setText(String newContent) {
        JTextArea textAreaFileSrc = ((TestResultDefaultView) currentCardView).getTextAreaFileSrc();
        textAreaFileSrc.setText("");
        textAreaFileSrc.setTabSize(2);
        textAreaFileSrc.setCaretPosition(0);
        textAreaFileSrc.setText(newContent);
        getButtonSaveTestResults().setEnabled(newContent.length() > 0);
    }

    public void refresTestResultsForMap() {
        GUIStaticUtils.setWaitCursor(this, true);
        StringBuilder stringBuilderAll = new StringBuilder();
        testCustomerMapMap.entrySet().forEach(testPhaseMapEntry -> {
            stringBuilderAll.append(TestFallFileUtil.dumAllCustomers(testPhaseMapEntry.getValue()));
        });
        setText(stringBuilderAll.toString());
        GUIStaticUtils.setWaitCursor(this, false);
    }

    private void refresTestResultsForTestCustomer(TestCustomer testCustomer) {
        StringBuilder stringBuilder = new StringBuilder();
        testCustomer.dumpResults(stringBuilder, "\n");
        setText(stringBuilder.toString());
    }

    private void refresTestResultsForTestScenario(String command, TestScenario testScenario) {
        StringBuilder stringBuilder = new StringBuilder();
        testScenario.dumpResults(command, stringBuilder, "\n");
        setText(stringBuilder.toString());
    }

    private void refresTestResultsForCommand(TestResults testResultsForCommand, TestCustomer testCustomer) {
        StringBuilder stringBuilder = new StringBuilder();
        testResultsForCommand.dumpResults(stringBuilder, "\n");
        testCustomer.getTestScenariosMap().entrySet().forEach(testScenarioEntry -> {
            TestScenario testScenario = testScenarioEntry.getValue();
            testScenario.dumpResults(testResultsForCommand.getCommand(), stringBuilder, "\n");
        });
        setText(stringBuilder.toString());
    }

    private MouseInputAdapter doDoubClickOnTree() {
        return new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                if (mouseEvent.getClickCount() == 2) {
                    JTree jTree = (JTree) mouseEvent.getSource();
                    TreePath path = jTree.getSelectionPath();
                    if (path == null) return;
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                    if (selectedNode != null && selectedNode instanceof DifferenceTreeNode) {
                        getButtonStartDifTool().setEnabled(diffenrenceInfo != null);
                        doStartDiffTool();
                    }
                }
            }
        };
    }

    @Override
    public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
        JTree jTree = (JTree) treeSelectionEvent.getSource();
        TreePath path = jTree.getSelectionPath();
        if (path == null) return;
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
        Object userObject = selectedNode.getUserObject();
        String key = selectedNode.toString();
        currentCardView = cardViewsMap.get(key);
        if (currentCardView == null) {
            if (userObject instanceof Map) {
                currentCardView = new TestResultCustomersMapView();
            } else if (userObject instanceof TestCustomer) {
                currentCardView = new TestResultCustomerView();
            } else if (userObject instanceof TestScenario) {
                currentCardView = new TestResultScenarioView();
            } else if (userObject instanceof String) {
                currentCardView = new TestResultCommandView();
            } else if (userObject instanceof TestResults.DiffenrenceInfo) {
                currentCardView = new TestResultDiffsView();
            }
            cardViewsMap.put(key, currentCardView);
        }
        setDiffenrenceInfo(null);
        if (userObject instanceof Map) {
            refresTestResultsForMap();
        } else if (userObject instanceof TestCustomer) {
            TestCustomer testCustomer = (TestCustomer) userObject;
            refresTestResultsForTestCustomer(testCustomer);
        } else if (userObject instanceof TestResults.DiffenrenceInfo) {
            TestResults.DiffenrenceInfo diffenrenceInfo = (TestResults.DiffenrenceInfo) userObject;
            TestResultDiffsView resultDiffsView = (TestResultDiffsView) currentCardView;
            setDiffenrenceInfo(diffenrenceInfo);
            resultDiffsView.setDiffenrenceInfo(diffenrenceInfo);
        } else if (userObject instanceof TestScenario) {
            String command = (String) parentNode.getUserObject();
            TestScenario testScenario = (TestScenario) userObject;
            refresTestResultsForTestScenario(command, testScenario);
        } else if (userObject instanceof String) {
            TestCustomer testCustomer = (TestCustomer) parentNode.getUserObject();
            TestResults testResultsForCommand = testCustomer.getTestResultsForCommand(userObject.toString());
            refresTestResultsForCommand(testResultsForCommand, testCustomer);
        }
        JPanel theCardPanel = (JPanel) getSplitPanelTreeView().getRightComponent();
        theCardPanel.removeAll();
        theCardPanel.add(key, currentCardView);
        int dividerLocation = getSplitPanelTreeView().getDividerLocation();
        CardLayout cardLayout = (CardLayout) (theCardPanel.getLayout());
        cardLayout.show(theCardPanel, key);
        getSplitPanelTreeView().setDividerLocation(dividerLocation);
    }

}
