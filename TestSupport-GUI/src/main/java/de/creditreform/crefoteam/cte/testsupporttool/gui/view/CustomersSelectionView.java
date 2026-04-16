package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.AbstractMutableTreeNode;
import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.ColumnsInfo;
import de.creditreform.crefoteam.cte.testsupporttool.gui.base.view.TableWithButtonsView;
import de.creditreform.crefoteam.cte.testsupporttool.gui.design.CustomersSelectionPanel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.model.TestCrefosTableModel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.model.TestCustomersTableModel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.model.TestScenariosTableModel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.tesun.util.TestCrefo;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.tesun.util.TestScenario;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.table.TableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class CustomersSelectionView extends CustomersSelectionPanel {
    private final static ColumnsInfo[] testCustomer_ColumnsInfo;
    private final static ColumnsInfo[] testScenario_ColumnsInfo;
    private final static ColumnsInfo[] testCrefo_ColumnsInfo;

    static {
        testCustomer_ColumnsInfo = new ColumnsInfo[]{new ColumnsInfo(40, 40, 40), // #
                new ColumnsInfo(90, 100, 0), // Kunde
        };
    }

    static {
        testScenario_ColumnsInfo = new ColumnsInfo[]{new ColumnsInfo(40, 40, 40), // #
                new ColumnsInfo(90, 100, 0), // Scenario
                new ColumnsInfo(60, 60, 60), // Crefos
        };
    }

    static {
        testCrefo_ColumnsInfo = new ColumnsInfo[]{new ColumnsInfo(40, 40, 40), // #
                new ColumnsInfo(60, 80, 90), // Test-Fallname
                new ColumnsInfo(60, 80, 90), // Original-Crefo-Nr
                new ColumnsInfo(60, 80, 90), // Pseudo-Crefo-Nr
                new ColumnsInfo(60, 80, 0),  // Crefo-Info
                new ColumnsInfo(90, 90, 90), // Soll Export
                new ColumnsInfo(90, 90, 90), // Ist Export
        };
    }

    public CustomersSelectionView() {
        getSplitPaneMain().setDividerLocation(200);
        getSplitPaneCustomerTrees().setDividerLocation(200);
        Border titleP1 = BorderFactory.createTitledBorder("Phase-1");
        getTableWithButtonsViewP1().setBorder(titleP1);
        Border titleP2 = BorderFactory.createTitledBorder("Phase-2");
        getTableWithButtonsViewP2().setBorder(titleP2);
        getPanelTestCrefos().getPanelButtons().setVisible(true);
        initListeners();
    }

    public Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> getActiveTestCustomersMapMap() {
        Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> activeTestCustomersMapMap = new TreeMap<>();
        activeTestCustomersMapMap.put(TestSupportClientKonstanten.TEST_PHASE.PHASE_1, getActiveTestCustomersMap(getTableWithButtonsViewP1().getModel()));
        activeTestCustomersMapMap.put(TestSupportClientKonstanten.TEST_PHASE.PHASE_2, getActiveTestCustomersMap(getTableWithButtonsViewP2().getModel()));
        return activeTestCustomersMapMap;
    }

    private Map<String, TestCustomer> getActiveTestCustomersMap(TableModel model) {
        Map<String, TestCustomer> activeTestCustomersMap = new TreeMap<>();
        if (model instanceof TestCustomersTableModel) {
            TestCustomersTableModel testCustomersTableModel = (TestCustomersTableModel) model;
            int rowCount = testCustomersTableModel.getRowCount();
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                TestCustomer testCustomer = (TestCustomer) testCustomersTableModel.getRow(rowIndex);
                if (testCustomer.isActivated()) {
                    activeTestCustomersMap.put(testCustomer.getCustomerKey(), testCustomer);
                }
            }
        }
        return activeTestCustomersMap;
    }

    public void setTestCustomersTableModelMap(Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> testCustomerMapMap) {
        Iterator<TestSupportClientKonstanten.TEST_PHASE> iterator = testCustomerMapMap.keySet().iterator();
        while (iterator.hasNext()) {
            TestSupportClientKonstanten.TEST_PHASE testPhase = iterator.next();
            setTestCustomersTableModel(testCustomerMapMap.get(testPhase), testPhase.equals(TestSupportClientKonstanten.TEST_PHASE.PHASE_1) ? getTableWithButtonsViewP1() : getTableWithButtonsViewP2());
        }
    }

    public void setTestCustomersTableModel(Map<String, TestCustomer> testCustomerMap, TableWithButtonsView tableWithButtonsView) {
        List<TestCustomer> testCustomerList = new ArrayList<>(testCustomerMap.values());
        final TestCustomersTableModel testCustomersTableModel = new TestCustomersTableModel(testCustomerList);
        tableWithButtonsView.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (listSelectionEvent.getValueIsAdjusting()) {
                    return;
                }
                int rowInModel = tableWithButtonsView.getSelectedRow();
                if (rowInModel > -1) {
                    TestCustomer testCustomer = (TestCustomer) testCustomersTableModel.getRow(rowInModel);
                    setTestScenariosTableModel(testCustomer.getTestScenariosList());
                }
            }
        });
        setTestScenariosTableModel(new ArrayList<>());
        tableWithButtonsView.setModel("Kunden", testCustomersTableModel, testCustomer_ColumnsInfo);
    }

    private void setTestScenariosTableModel(List<TestScenario> testScenariosList) {
        final TestScenariosTableModel testScenariosTableModel = new TestScenariosTableModel(testScenariosList);
        getPanelScenarios().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (listSelectionEvent.getValueIsAdjusting()) {
                    return;
                }
                int rowInModel = getPanelScenarios().getSelectedRow();
                if (rowInModel > -1) {
                    TestScenario testScenario = (TestScenario) testScenariosTableModel.getRow(rowInModel);
                    setTestCrefosTableModel(testScenario.getTestCrefosAsList());
                }
            }
        });
        setTestCrefosTableModel(new ArrayList<>());
        getPanelScenarios().setModel("Scenarien", testScenariosTableModel, testScenario_ColumnsInfo);
    }

    private void setTestCrefosTableModel(List<TestCrefo> testCrefosList) {
        GUIStaticUtils.setWaitCursor(this, true);
        TestCrefosTableModel testCrefosTableModel = new TestCrefosTableModel(testCrefosList);
        getPanelTestCrefos().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (listSelectionEvent.getValueIsAdjusting()) {
                    return;
                }
                int rowInModel = getPanelScenarios().getSelectedRow();
                if (rowInModel > -1) {
                }
            }
        });
        getPanelTestCrefos().getTable().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                JTable theTable = (JTable) mouseEvent.getSource();
                int selectedRow = theTable.getSelectedRow();
                if (selectedRow < 0) {
                } else {
                    if (mouseEvent.getClickCount() == 2) {
                        // Double-click detected
                    } else if (mouseEvent.getClickCount() == 3) {
                        // Triple-click detected
                    }
                }
            }
        });
        getPanelTestCrefos().setModel("Crefos", testCrefosTableModel, testCrefo_ColumnsInfo);
        GUIStaticUtils.setWaitCursor(this, false);
    }

    private void initListeners() {
        getTreeCustomersPhase1().addTreeSelectionListener(treeSelectionEvent -> doSelectCustomerInTree(treeSelectionEvent));
        getTreeCustomersPhase2().addTreeSelectionListener(treeSelectionEvent -> doSelectCustomerInTree(treeSelectionEvent));
    }

    protected void doSelectCustomerInTree(TreeSelectionEvent treeSelectionEvent) {
        Object treeSelectionEventSource = treeSelectionEvent.getSource();
        if (treeSelectionEventSource != null) {
            AbstractMutableTreeNode selectedNode = (AbstractMutableTreeNode) getTreeCustomersPhase2().getLastSelectedPathComponent();
            if (selectedNode == null) {
                return;
            }
            Object userObject = selectedNode.getUserObject();
            if (userObject instanceof TestCustomer) {
                TestCustomer testCustomer = (TestCustomer) userObject;
                setTestScenariosTableModel(testCustomer.getTestScenariosList());
            } else if (userObject instanceof TestScenario) {

            } else {

            }
        }
    }

}
