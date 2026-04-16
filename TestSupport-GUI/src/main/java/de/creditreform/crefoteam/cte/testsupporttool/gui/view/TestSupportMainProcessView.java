package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.design.TestSupportMainProcessPanel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.model.TestJobsComboBoxItem;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TestSupportMainProcessView extends TestSupportMainProcessPanel {

    private Runnable onStartProcess;
    private Runnable onStopProcess;
    private Runnable onStartTestJob;
    private Runnable onTestSourceChanged;
    private Runnable onITSQRevisionChanged;
    private Runnable onTestTypeChanged;
    private Runnable onUseOnlyTestCLZsChanged;

    public TestSupportMainProcessView() {
        super();
    }

    public void init(Runnable onStartProcess,
                     Runnable onStopProcess,
                     Runnable onStartTestJob,
                     Runnable onTestSourceChanged,
                     Runnable onITSQRevisionChanged,
                     Runnable onTestTypeChanged,
                     Runnable onUseOnlyTestCLZsChanged,
                     EnvironmentConfig currentEnvironment) {
        this.onStartProcess = onStartProcess;
        this.onStopProcess = onStopProcess;
        this.onStartTestJob = onStartTestJob;
        this.onTestSourceChanged = onTestSourceChanged;
        this.onITSQRevisionChanged = onITSQRevisionChanged;
        this.onTestTypeChanged = onTestTypeChanged;
        this.onUseOnlyTestCLZsChanged = onUseOnlyTestCLZsChanged;
        initUiDefaults(currentEnvironment);
        initListeners();
    }

    private void initListeners() {
        getComboBoxTestType().addActionListener(e -> onTestTypeChanged.run());
        getComboBoxTestSource().addActionListener(e -> onTestSourceChanged.run());
        getComboBoxITSQRevision().addActionListener(e -> onITSQRevisionChanged.run());
        getComboBoxTestJobs().addActionListener(e -> doChangeTestJob());
        getCheckBoxUseOnlyTestCLZs().addActionListener(e -> onUseOnlyTestCLZsChanged.run());
        getButtonStartProcess().addActionListener(e -> onStartProcess.run());
        getButtonStopUserTasksThread().addActionListener(e -> onStopProcess.run());
        getButtonStartTestJob().addActionListener(e -> onStartTestJob.run());
    }

    public void initUiDefaults(EnvironmentConfig currentEnvironment) {
        getLabelFachwertConfig().setVisible(false);
        getRadioButtonFWConfigNewest().setVisible(false);
        getRadioButtonFWConfigLikePRE().setVisible(false);
        getLabelExportFormat().setVisible(false);
        getRadioButtonExportFormatNewest().setVisible(false);
        getRadioButtonExportFormatLikePRE().setVisible(false);

        getCheckBoxDemoMode().setSelected(true);  // CLAUDE_MODE
        getCheckBoxDemoMode().setEnabled(false);  // CLAUDE_MODE

        getCheckBoxUseOnlyTestCLZs().setSelected(currentEnvironment.isLastUseOnlyTestClz());
        getCheckBoxUploadSynthetics().setSelected(currentEnvironment.isLastUploadSynthetics());
    }

    public void initTestTypesComboBox(EnvironmentConfig currentEnvironment) throws Exception {
        List<TestSupportClientKonstanten.TEST_TYPES> testTypesList = currentEnvironment.getTestTypes();
        DefaultComboBoxModel testTypesModel = new DefaultComboBoxModel();
        for (TestSupportClientKonstanten.TEST_TYPES testType : testTypesList) {
            testTypesModel.addElement(testType);
        }
        getComboBoxTestType().setModel(testTypesModel);
        getComboBoxTestType().setSelectedItem(currentEnvironment.getLastTestType());
    }

    public void initTestPhasesComboBox() {
        getComboBoxTestPhase().setModel(new DefaultComboBoxModel());
        getComboBoxTestPhase().addItem(TestSupportClientKonstanten.TEST_PHASE.PHASE_1);
        getComboBoxTestPhase().addItem(TestSupportClientKonstanten.TEST_PHASE.PHASE_2);
        getComboBoxTestPhase().setSelectedItem(TestSupportClientKonstanten.TEST_PHASE.PHASE_1);
    }

    public void initTestJobsCombo() {
        List<TestJobsComboBoxItem> testJobsList = new ArrayList<>();
        testJobsList.add(new TestJobsComboBoxItem("2:Generate Pseudo-Crefos", Collections.singletonList("UserTaskGeneratePseudoCrefos")));
        testJobsList.add(new TestJobsComboBoxItem("3:Systemeinstellungen setzen", Collections.singletonList("UserTaskPrepareTestSystem")));
        testJobsList.add(new TestJobsComboBoxItem("4:Upload nach STAGING", Collections.singletonList("UserTaskStartUploads")));
        testJobsList.add(new TestJobsComboBoxItem("6:Beteiligten-Import starten", Arrays.asList("UserTaskStartBeteiligtenImport", "UserTaskWaitForBeteiligtenImport")));
        testJobsList.add(new TestJobsComboBoxItem("8:ENTG-Berechnung starten", Arrays.asList("UserTaskStartEntgBerechnung", "UserTaskWaitForEntgBerechnung")));
        testJobsList.add(new TestJobsComboBoxItem("10:BTLG-Aktualisierung starten", Arrays.asList("UserTaskStartBtlgAktualisierung", "UserTaskWaitForBtlgAktualisierung")));
        testJobsList.add(new TestJobsComboBoxItem("12:CT-Import starten", Arrays.asList("UserTaskStartCtImport", "UserTaskWaitForCtImport")));
        testJobsList.add(new TestJobsComboBoxItem("13:Import-Cycle starten", Arrays.asList("UserTaskStartImports", "UserTaskWaitForCtImport")));
        testJobsList.add(new TestJobsComboBoxItem("14:Exports starten", Collections.singletonList("UserTaskStartExports")));
        testJobsList.add(new TestJobsComboBoxItem("16:Collect starten", Collections.singletonList("UserTaskStartCollect"), TestSupportClientKonstanten.LAST_COMPLETITION_TIME));
        testJobsList.add(new TestJobsComboBoxItem("17:Collects überprüfen", Collections.singletonList("UserTaskCheckCollects")));
        testJobsList.add(new TestJobsComboBoxItem("18:SFTP-Uploads starten", Collections.singletonList("UserTaskStartSftpUploads")));
        testJobsList.add(new TestJobsComboBoxItem("19:SFTP-Uploads prüfen", Collections.singletonList("UserTaskCheckSftpUploads"), TestSupportClientKonstanten.LAST_COMPLETITION_TIME));
        testJobsList.add(new TestJobsComboBoxItem("20:Restore Collects(Exports)", Collections.singletonList("UserTaskStartRestore")));
        testJobsList.add(new TestJobsComboBoxItem("21:REF/COLLECT-Checks astarten", Collections.singletonList("UserTaskCheckRefExports")));
        testJobsList.add(new TestJobsComboBoxItem("22:Export-Protokoll-Checks starten", Collections.singletonList("UserTaskCheckExportProtokoll"), TestSupportClientKonstanten.LAST_COMPLETITION_TIME));
        testJobsList.add(new TestJobsComboBoxItem("25:System-Einstellungen Restaurieren", Collections.singletonList("UserTaskRestoreTestSystem")));
        testJobsList.add(new TestJobsComboBoxItem("30:BIC Jobs testen", Collections.singletonList("UserTaskBicJobs"), TestSupportClientKonstanten.UPLOAD_EMPTY_PAYLOAD));
        testJobsList.add(new TestJobsComboBoxItem("40:CrefoAnalyseErgebnisse sammeln", Collections.singletonList("UserTaskCrefoAnalyseErgebnisse")));
        testJobsList.add(new TestJobsComboBoxItem("41:Fachwertkonfig sammeln", Collections.singletonList("UserTaskCollectFWConfigs")));
        testJobsList.add(new TestJobsComboBoxItem("42:Erneute Lieferung beantragen", Collections.singletonList("UserTaskErneuteLieferungBeantragen")));
        testJobsList.sort(Comparator.naturalOrder());
        getComboBoxTestJobs().setModel(new DefaultComboBoxModel(testJobsList.toArray()));
        doChangeTestJob();
    }

    public void initTestSourcesComboBox(EnvironmentConfig currentEnvironment) throws Exception {
        ActionListener[] listeners = disableCbListeners(getComboBoxTestSource());
        getComboBoxTestSource().setModel(new DefaultComboBoxModel());
        currentEnvironment.getTestSetSources().forEach(s -> getComboBoxTestSource().addItem(s));
        getComboBoxTestSource().setSelectedItem(currentEnvironment.getLastTestSource());
        enableCbListeners(getComboBoxTestSource(), listeners);
    }

    public void initITSQRevisionsComboBox(EnvironmentConfig currentEnvironment) throws Exception {
        ActionListener[] listeners = disableCbListeners(getComboBoxITSQRevision());
        List<String> itsqRevisionsList = currentEnvironment.getItsqRevisions();
        getComboBoxITSQRevision().setModel(new DefaultComboBoxModel());
        itsqRevisionsList.forEach(r -> getComboBoxITSQRevision().addItem(r));
        String lastItsqRevision = currentEnvironment.getLastItsqRevision();
        if (!itsqRevisionsList.contains(lastItsqRevision)) {
            getComboBoxITSQRevision().addItem(lastItsqRevision);
        }
        getComboBoxITSQRevision().setSelectedItem(lastItsqRevision);
        enableCbListeners(getComboBoxITSQRevision(), listeners);
    }

    private void doChangeTestJob() {
        final TestJobsComboBoxItem item = (TestJobsComboBoxItem) getComboBoxTestJobs().getSelectedItem();
        if (item == null) return;
        final Map<String, Object> taskVariablesMap = item.getTaskVariablesMap();
        final boolean isEmpty = taskVariablesMap.isEmpty();
        getLabelJobParams().setVisible(!isEmpty);
        getTextFieldJobParams().setVisible(!isEmpty);
        getTextFieldJobParams().setEditable(!isEmpty);
        if (!isEmpty) {
            getTextFieldJobParams().setText(item.getTaskVariablesMapAsFieldText());
            getTextFieldJobParams().addActionListener(e -> item.setTaskVariables(getTextFieldJobParams().getText()));
            getTextFieldJobParams().addFocusListener(new FocusListener() {
                @Override public void focusGained(FocusEvent e) {}
                @Override public void focusLost(FocusEvent e) {
                    item.setTaskVariables(getTextFieldJobParams().getText());
                }
            });
        }
    }

    // --- State getters ---

    public boolean isDemoMode() {
        return getCheckBoxDemoMode().isSelected();
    }

    public TestSupportClientKonstanten.TEST_TYPES getSelectedTestType() {
        return (TestSupportClientKonstanten.TEST_TYPES) getComboBoxTestType().getSelectedItem();
    }

    public TestSupportClientKonstanten.TEST_PHASE getSelectedTestPhase() {
        return (TestSupportClientKonstanten.TEST_PHASE) getComboBoxTestPhase().getSelectedItem();
    }

    public String getSelectedTestSource() {
        Object selected = getComboBoxTestSource().getSelectedItem();
        return selected != null ? selected.toString() : null;
    }

    public String getSelectedITSQRevision() {
        Object selected = getComboBoxITSQRevision().getSelectedItem();
        return selected != null ? selected.toString() : null;
    }

    public boolean isUseOnlyTestCLZs() {
        return getCheckBoxUseOnlyTestCLZs().isSelected();
    }

    public boolean isUploadSynthetics() {
        return getCheckBoxUploadSynthetics().isSelected();
    }

    public String getTestCasesPath() {
        return getTextFieldTestCasesPath().getText();
    }

    public void setTestCasesPath(String path) {
        getTextFieldTestCasesPath().setText(path);
    }

    public TestJobsComboBoxItem getSelectedTestJob() {
        return (TestJobsComboBoxItem) getComboBoxTestJobs().getSelectedItem();
    }

    public void setStopButtonEnabled(boolean enabled) {
        getButtonStopUserTasksThread().setEnabled(enabled);
    }

    public void updateITSQRevisionEnabled() {
        Object selected = getComboBoxTestSource().getSelectedItem();
        getComboBoxITSQRevision().setEnabled(selected != null && selected.equals("ITSQ"));
    }

    public List<JComponent> getComponentsToOnOff() {
        List<JComponent> list = new ArrayList<>();
        list.add(getComboBoxTestSource());
        list.add(getCheckBoxUseOnlyTestCLZs());
        list.add(getComboBoxITSQRevision());
        list.add(getComboBoxTestPhase());
        list.add(getButtonStopUserTasksThread());
        list.add(getButtonStartTestJob());
        list.add(getButtonStartProcess());
        list.add(getComboBoxTestJobs());
        list.add(getTextFieldJobParams());
        list.add(getComboBoxTestType());
        list.add(getCheckBoxDemoMode());
        list.add(getCheckBoxUploadSynthetics());
        return list;
    }

    private void enableCbListeners(JComboBox comboBox, ActionListener[] actionListeners) {
        for (ActionListener al : actionListeners) comboBox.addActionListener(al);
    }

    private ActionListener[] disableCbListeners(JComboBox comboBox) {
        ActionListener[] listeners = comboBox.getActionListeners();
        for (ActionListener al : listeners) comboBox.removeActionListener(al);
        return listeners;
    }
}
