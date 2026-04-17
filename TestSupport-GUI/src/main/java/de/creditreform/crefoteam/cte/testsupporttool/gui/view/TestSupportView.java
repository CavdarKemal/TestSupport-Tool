package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.rest.RestInvokerConfig;
import de.creditreform.crefoteam.cte.testsupporttool.gui.jvm.ManageJvmsDlgView;
import de.creditreform.crefoteam.cte.statemachine.ProcessOutcome;
import de.creditreform.crefoteam.cte.testsupporttool.gui.design.TestSupportPanel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.model.TestJobsComboBoxItem;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.CommandExecutorListener;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIFrame;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.TestSupportHelper;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;
import de.creditreform.crefoteam.cte.testsupporttool.resume.ResumeState;
import de.creditreform.crefoteam.cte.testsupporttool.util.ExceptionUtils;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;

import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.UserTaskRunnable;
import de.creditreform.crefoteam.cte.restservices.tesun.xmlbinding.systeminfo.TesunSystemInfo;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import org.apache.log4j.Level;

/**
 * Haupt-View der Test-Support-GUI — Port aus
 * {@code testsupport_client.tesun.gui.view.TestSupportView}.
 *
 * <p>Anpassungen ggü. Original:
 * <ul>
 *   <li>{@code ActivitiProcessController} → {@link ProcessController} (um
 *       {@code ProcessEngine} + {@code DiagramImageListener}).</li>
 *   <li>{@code ActivitiTestSupport} komplett entfernt — die Task-Variablen
 *       werden inline via {@link #buildTaskVariablesMap(boolean, Map)}
 *       zusammengebaut.</li>
 *   <li>{@code CteActivitiTask}-Notify-Branch in {@link #notifyClientJob}
 *       entfällt — der StateMachine-Weg liefert nur {@code InputStream}
 *       (Diagramm-PNG) und {@code String} (Log-Meldungen).</li>
 *   <li>Dynamisches {@code Class.forName(UserTaskRunnable)} für einzelne
 *       Test-Jobs ist als CLAUDE_MODE-Stub auskommentiert — im Tool-Modus
 *       zeigt die Methode eine Info-MessageBox.</li>
 *   <li>{@code ManageJvmsDlgView} ist als CLAUDE_MODE-Stub — Phase J.</li>
 * </ul>
 */
public class TestSupportView extends TestSupportPanel implements TesunClientJobListener, CommandExecutorListener {

    private static final String APP_TITLE = "CTE-Testautomatisierung";
    private static final int MAIN_DIVIDER_POSITION = 500;
    private static final int EXIT_CODE_CONFIG_MISSING = -1;
    private static final int JVM_DIALOG_OPEN_DELAY_MS = 1000;

    private ProcessController processController;
    List<JComponent> componentsToOnOff;

    final GUIFrame guiFrame;
    TestSupportHelper testSupportHelper;
    private final TestResultsView viewTestResults;
    EnvironmentConfig currentEnvironment;

    private final EnvironmentSwitchHandler environmentSwitchHandler;
    private final CustomerInitializer customerInitializer;

    public TestSupportView(GUIFrame guiFrame) {
        super();
        this.guiFrame = guiFrame;
        currentEnvironment = guiFrame.getEnvironmentConfig();
        this.viewTestResults = getTabbedPaneMonitor().getViewTestResults();
        this.environmentSwitchHandler = new EnvironmentSwitchHandler(this);
        this.customerInitializer = new CustomerInitializer(this);

        initEnvironmentsComboBox();
        initITSQRevisionsComboBox();
        initTestSourcesComboBox();
        initTestTypesComboBox();
        initTestJobsCombo();
        initHostsFields();
        initTestPhasesComboBox();

        getViewTestSupportMainControls().init(this, this::doChangeComboBoxesHost, this::initForEnvironment, this::doManageJVMs, environmentSwitchHandler::doChangeEnvironment);
        getViewTestSupportMainProcess().init(this::startStateEngineProcess, this::stopStateEngineProcess, this::startSelectedTestJob, this::doChangeTestResources, this::doChangeITSQRevision, this::doChangeTestType,
                () -> currentEnvironment.setLastUseOnlyTestClz(getViewTestSupportMainProcess().isUseOnlyTestCLZs()),
                currentEnvironment);

        getTabbedPaneMonitor().init(() -> testSupportHelper);

        componentsToOnOff = new ArrayList<>();
        componentsToOnOff.addAll(getViewTestSupportMainControls().getComponentsToOnOff());
        componentsToOnOff.addAll(getViewTestSupportMainProcess().getComponentsToOnOff());

        enableComponentsToOnOff(false);

        getSplitPaneMain().setDividerLocation(MAIN_DIVIDER_POSITION);
        getTabbedPaneMonitor().getCheckBoxScrollToEnd().setSelected(true);

        initForEnvironment();
        initListeners();
        processController = new ProcessController(this);
    }

    private void initListeners() {
        getTabbedPaneMonitor().addChangeListener(this::doTabChangeEvent);
    }

    private void stopStateEngineProcess() {
        processController.stop();
        getViewTestSupportMainProcess().setStopButtonEnabled(false);
    }

    private void startSelectedTestJob() {
        startUserTaskRunnable(getViewTestSupportMainProcess().getSelectedTestJob());
    }

    protected void startStateEngineProcess() {
        // Resume-Dialog: wenn resume.properties existiert, fragen, ob
        // fortgesetzt oder komplett neu gestartet werden soll.
        int[] resumeIndexPath = checkAndHandleResumeState();
        if (resumeIndexPath != null && resumeIndexPath.length == 0) {
            // Sentinel fuer CANCEL — nichts tun.
            return;
        }

        Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> activeTestCustomersMapMap = getViewCustomersSelection().getActiveTestCustomersMapMap();
        activeTestCustomersMapMap.keySet().forEach(testPhase -> activeTestCustomersMapMap.get(testPhase).values().forEach(TestCustomer::emptyTestResultsMapForCommands));

        enableComponentsToOnOff(false);
        GUIStaticUtils.setWaitCursor(TestSupportView.this, true);

        final boolean isDemoMode = getViewTestSupportMainProcess().isDemoMode();
        final Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> activeCustomers = getViewCustomersSelection().getActiveTestCustomersMapMap();
        final int[] finalResumePath = resumeIndexPath;

        new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                GUIStaticUtils.setWaitCursor(TestSupportView.this, true);
                getViewTestSupportMainProcess().setStopButtonEnabled(true);
            });
            try {
                if (finalResumePath != null && finalResumePath.length > 0) {
                    notifyClientJob(Level.INFO,
                            "\n▶ RESUME-Modus: Überspringe Steps bis Index-Pfad "
                            + java.util.Arrays.toString(finalResumePath)
                            + " — übersprungene Steps erscheinen im Log, führen aber keine Aktionen aus.");
                }
                Map<String, Object> taskVariablesMap = buildTaskVariablesMap(isDemoMode, activeCustomers);
                ProcessOutcome outcome = processController.runProcess(currentEnvironment, taskVariablesMap, finalResumePath);
                notifyClientJob(Level.INFO, "\nProzess beendet: " + outcome);
                notifyProcessComplete();
            } catch (Exception ex) {
                notifyClientJob(Level.ERROR, GUIStaticUtils.showExceptionMessage(TestSupportView.this, "Fehler beim Starten des Prozesses!", ex));
                SwingUtilities.invokeLater(() -> {
                    enableComponentsToOnOff(true);
                    GUIStaticUtils.setWaitCursor(TestSupportView.this, false);
                });
            }
        }, "statemachine-process-runner").start();
    }

    /**
     * @return {@code null} fuer Neustart (kein oder geloeschter State),
     *         leeres Array als Sentinel fuer "User hat Cancel gewaehlt",
     *         gefuelltes Array = Resume-Index-Pfad.
     */
    private int[] checkAndHandleResumeState() {
        File resumeFile;
        try {
            resumeFile = new File(currentEnvironment.getTestOutputsRoot(), ResumeState.FILE_NAME);
        } catch (Exception ex) {
            return null;
        }
        ResumeState state;
        try {
            state = ResumeState.load(resumeFile);
        } catch (Exception ex) {
            TimelineLogger.warn(getClass(), "Resume-State beschaedigt — wird ignoriert: {}", ex.getMessage());
            ResumeState.delete(resumeFile);
            return null;
        }
        if (state == null) return null;
        String message = "Ein unterbrochener Prozess wurde gefunden:\n\n"
                + "  Phase: " + (state.testPhase() != null ? state.testPhase() : "-") + "\n"
                + "  Step:  " + (state.lastStepName() != null ? state.lastStepName() : "-") + "\n"
                + "  Zeit:  " + state.savedAt() + "\n\n"
                + "Ja        = beim letzten Step fortsetzen\n"
                + "Nein      = alten Stand loeschen und komplett neu starten\n"
                + "Abbrechen = keinen Prozess starten";
        int choice = JOptionPane.showConfirmDialog(this, message, APP_TITLE, JOptionPane.YES_NO_CANCEL_OPTION);
        if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) {
            return new int[0]; // Sentinel: Cancel
        }
        if (choice == JOptionPane.YES_OPTION) {
            return state.indexPath();
        }
        // NO_OPTION → alten State verwerfen, frisch starten
        ResumeState.delete(resumeFile);
        return null;
    }

    private void showAndRethrow(String message, Exception ex) {
        GUIStaticUtils.showExceptionMessage(this, message, ex);
        throw new RuntimeException(ex.getMessage());
    }

    private void doChangeComboBoxesHost() {
        try {
            testSupportHelper = getTestSupportHelper();
        } catch (Exception ex) {
            showAndRethrow("Fehler beim Wechseln des Hosts!", ex);
        }
    }

    private void doTabChangeEvent(ChangeEvent changeEvent) {
        JTabbedPane jTabbedPane = (JTabbedPane) changeEvent.getSource();
        Component selectedComponent = jTabbedPane.getSelectedComponent();
        if (selectedComponent instanceof TestResultsView) {
            Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> activeTestCustomersMapMap = getAndCheckActiveCustomers();
            viewTestResults.refreshTestResultsForMap(activeTestCustomersMapMap, false);
        }
    }

    private void initTestTypesComboBox() {
        try {
            getViewTestSupportMainProcess().initTestTypesComboBox(currentEnvironment);
            getViewCustomersSelection().setTestCustomersTableModelMap(new HashMap<>());
        } catch (Exception ex) {
            showAndRethrow("Fehler beim Initialisieren der Test-Typen!", ex);
        }
    }

    private void initTestPhasesComboBox() {
        getViewTestSupportMainProcess().initTestPhasesComboBox();
    }

    private void initTestJobsCombo() {
        getViewTestSupportMainProcess().initTestJobsCombo();
    }

    private void initHostsFields() {
        try {
            getViewTestSupportMainControls().initHostsFields(currentEnvironment);
        } catch (Exception ex) {
            showAndRethrow("Fehler beim Initialisieren der Hosts!", ex);
        }
    }

    private void initTestSourcesComboBox() {
        try {
            getViewTestSupportMainProcess().initTestSourcesComboBox(currentEnvironment);
        } catch (Exception ex) {
            showAndRethrow("Fehler beim Initialisieren der Test-Sourcen!", ex);
        }
    }

    private void initITSQRevisionsComboBox() {
        try {
            getViewTestSupportMainProcess().initITSQRevisionsComboBox(currentEnvironment);
        } catch (Exception ex) {
            showAndRethrow("Fehler beim Initialisieren der ITSQ-Revisions!", ex);
        }
    }

    private void initEnvironmentsComboBox() {
        Map<String, File> environmentsMap = currentEnvironment.getEnvironmentsMap();
        if ((environmentsMap == null) || (environmentsMap.isEmpty())) {
            String exceptionErr = "Es konnten im aktuellen Verzeichnis '" + System.getProperty("user.dir") + "'\nkeine Konfigurationsdateien '{ENE|GEE|ABE}-config.properties' gefunden werden!";
            notifyClientJob(Level.ERROR, GUIStaticUtils.showExceptionMessage(this, "Konfiguration laden", new RuntimeException(exceptionErr)));
            System.exit(EXIT_CODE_CONFIG_MISSING);
        }
        getViewTestSupportMainControls().initEnvironmentsComboBox(currentEnvironment);
    }

    public void initForEnvironment() {
        enableComponentsToOnOff(false);
        GUIStaticUtils.setWaitCursor(TestSupportView.this, true);
        viewTestResults.setEnvironmentConfig(currentEnvironment);
        notifyClientJob(Level.INFO, String.format("\nInitialisiere für die Umgebung %s...", currentEnvironment.getCurrentEnvName()));
        getTabbedPaneMonitor().getTextAreaTaskListenerInfo().setText("");
        initHostsFields();
        new Thread(() -> {
            try {
                notifyClientJob(Level.INFO, String.format("\nInitialisiere Test-Resourcen für die Umgebung %s...", getViewTestSupportMainControls().getSelectedEnvironmentName()));
                testSupportHelper = getTestSupportHelper();
                if (!getViewTestSupportMainProcess().isDemoMode() && testSupportHelper != null
                        && testSupportHelper.getTesunRestServiceWLS() != null) {
                    try {
                        TesunSystemInfo sysInfo = testSupportHelper.getTesunRestServiceWLS().getTesunSystemInfo();
                        String versionsInfo = String.format("[ %s ] - [ CTE-Version: %s ]",
                                currentEnvironment.getAppVersionsInfo(), sysInfo.getCteVersion());
                        guiFrame.setVersionsInfoInTitle(versionsInfo);
                    } catch (Exception ex) {
                        notifyClientJob(Level.WARN, "\nSysteminfo nicht verfügbar: " + ex.getMessage());
                    }
                }
                customerInitializer.initCustomers();
            } catch (Throwable ex) {
                notifyClientJob(Level.ERROR, GUIStaticUtils.showExceptionMessage(TestSupportView.this, "Konfiguration laden", ex instanceof Exception ? (Exception) ex : new RuntimeException(ex)));
            } finally {
                enableComponentsToOnOff(true);
                SwingUtilities.invokeLater(() -> GUIStaticUtils.setWaitCursor(TestSupportView.this, false));
            }
        }, "environment-init").start();
    }

    private TestSupportHelper getTestSupportHelper() throws Exception {
        RestInvokerConfig restServicesConfig = getViewTestSupportMainControls().getSelectedRestServicesConfig();
        RestInvokerConfig impCycleConfig = getViewTestSupportMainControls().getSelectedImpCycleConfig();
        if (restServicesConfig == null || impCycleConfig == null) {
            return null;
        }
        return new TestSupportHelper(currentEnvironment, restServicesConfig, impCycleConfig, TestSupportView.this);
    }

    private void doManageJVMs() {
        GUIStaticUtils.setWaitCursor(this, true);
        ManageJvmsDlgView manageJvmsDialog = new ManageJvmsDlgView(GUIStaticUtils.getParentFrame(this), "Verfügbare JVMs und Jobs", currentEnvironment);
        GUIStaticUtils.warteBisken(JVM_DIALOG_OPEN_DELAY_MS);
        manageJvmsDialog.setVisible(true);
        GUIStaticUtils.setWaitCursor(this, false);
    }

    private Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> getAndCheckActiveCustomers() {
        Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> resultMap = new HashMap<>();
        Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> activeTestCustomersMapMap = getViewCustomersSelection().getActiveTestCustomersMapMap();
        if (activeTestCustomersMapMap.isEmpty()) {
            RuntimeException ex = new RuntimeException("Es sind keine Kunden aktiviert!\nBitte zuerst mindestens einen Kunden mit mindestens einen Terst-Scenario aktivieren.");
            GUIStaticUtils.showExceptionMessage(this, "Fehler beim Initialisieren der Kunden!", ex);
            throw ex;
        }
        Map<String, TestCustomer> selectedCustomersMapPhase1 = activeTestCustomersMapMap.get(TestSupportClientKonstanten.TEST_PHASE.PHASE_1);
        Map<String, TestCustomer> selectedCustomersMapPhase2 = activeTestCustomersMapMap.get(TestSupportClientKonstanten.TEST_PHASE.PHASE_2);
        if (getViewTestSupportMainProcess().getSelectedTestType().equals(TestSupportClientKonstanten.TEST_TYPES.PHASE1_AND_PHASE2)) {
            selectedCustomersMapPhase1.keySet().forEach(customerKey -> {
                TestCustomer testCustomer = selectedCustomersMapPhase2.get(customerKey);
                if (testCustomer == null || !testCustomer.isActivated()) {
                    RuntimeException ex = new RuntimeException("Kunde " + customerKey + " aus der PHASE-1 muss auch für PHASE-2 selektiert werden!");
                    GUIStaticUtils.showExceptionMessage(this, "Fehler beim Initialisieren Kunden!", ex);
                    throw ex;
                }
            });
            resultMap.put(TestSupportClientKonstanten.TEST_PHASE.PHASE_1, selectedCustomersMapPhase1);
            resultMap.put(TestSupportClientKonstanten.TEST_PHASE.PHASE_2, selectedCustomersMapPhase2);
        } else {
            Iterator<Map.Entry<String, TestCustomer>> it = selectedCustomersMapPhase2.entrySet().iterator();
            while (it.hasNext()) {
                TestCustomer testCustomer = it.next().getValue();
                if (testCustomer.isActivated()) {
                    break;
                }
            }
            if (!it.hasNext()) {
                RuntimeException ex = new RuntimeException("PHASE-2 muss mindestens einen aktiven Kunden haben!");
                GUIStaticUtils.showExceptionMessage(this, "Fehler beim Initialisieren der Kunden!", ex);
                throw ex;
            }
            resultMap.put(TestSupportClientKonstanten.TEST_PHASE.PHASE_2, selectedCustomersMapPhase2);
        }
        return resultMap;
    }

    @SuppressWarnings("unchecked")
    private void startUserTaskRunnable(final TestJobsComboBoxItem testJobsComboBoxItem) {
        try {
            GUIStaticUtils.setWaitCursor(this, true);
            enableComponentsToOnOff(false);
            final boolean isDemoMode = getViewTestSupportMainProcess().isDemoMode();
            new Thread(() -> {
                try {
                    Map<String, Object> lastVars = new HashMap<>();
                    for (String jobName : testJobsComboBoxItem.getTestJobNamesList()) {
                        String className = "de.creditreform.crefoteam.cte.testsupporttool.handlers." + jobName;
                        Class<UserTaskRunnable> cls = (Class<UserTaskRunnable>) Class.forName(className);
                        Constructor<UserTaskRunnable> ctor = cls.getConstructor(EnvironmentConfig.class, TesunClientJobListener.class);
                        UserTaskRunnable task = ctor.newInstance(currentEnvironment, TestSupportView.this);
                        Map<String, Object> vars = buildTaskVariablesMap(isDemoMode, getViewCustomersSelection().getActiveTestCustomersMapMap());
                        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_MANUEL_USER_TASK, Boolean.TRUE);
                        vars.putAll(lastVars);
                        vars.putAll(testJobsComboBoxItem.getTaskVariablesMap());
                        lastVars = task.runTask(vars);
                    }
                } catch (Exception ex) {
                    notifyClientJob(Level.ERROR, GUIStaticUtils.showExceptionMessage(TestSupportView.this,
                            "Fehler beim Start des User-Tasks: " + testJobsComboBoxItem.getTestJobNamesList(), ex));
                } finally {
                    SwingUtilities.invokeLater(() -> {
                        GUIStaticUtils.setWaitCursor(TestSupportView.this, false);
                        enableComponentsToOnOff(true);
                    });
                }
            }, "user-task-runner").start();
        } catch (Exception ex) {
            notifyClientJob(Level.ERROR, GUIStaticUtils.showExceptionMessage(this, "UserTask starten!", ex));
            enableComponentsToOnOff(true);
        }
    }

    /**
     * Baut die Task-Variablen-Map für den State-Machine-Prozess — inlined aus
     * dem ehemaligen {@code ActivitiTestSupport#buildTaskVariablesMap}.
     */
    private Map<String, Object> buildTaskVariablesMap(boolean isDemoMode,
                                                      Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> activeCustomers) throws PropertiesException {
        Map<String, Object> vars = new HashMap<>();
        vars.put(UT_TASK_PARAM_NAME_DEMO_MODE, isDemoMode);
        vars.put(UT_TASK_PARAM_NAME_MEIN_KEY, currentEnvironment.getActivitProcessKey());
        vars.put(UT_TASK_PARAM_NAME_STATE_ENGINE_PROCESS_NAME, currentEnvironment.getStateEngineProcessName());
        vars.put(UT_TASK_PARAM_NAME_TIME_BEFORE_BTLG_IMPORT, currentEnvironment.getMillisBeforeBtlgImport(isDemoMode));
        vars.put(UT_TASK_PARAM_NAME_TIME_BEFORE_CT_IMPORT, currentEnvironment.getMillisBeforeCtImport(isDemoMode));
        vars.put(UT_TASK_PARAM_NAME_TIME_BEFORE_EXPORT, currentEnvironment.getMillisBeforeExports(isDemoMode));
        vars.put(UT_TASK_PARAM_NAME_TIME_BEFORE_EXPORTS_COLLECT, currentEnvironment.getMillisBeforeCollectExports(isDemoMode));
        vars.put(UT_TASK_PARAM_NAME_TIME_BEFORE_SFTP_COLLECT, currentEnvironment.getMillisBeforeCollectSftpUploads(isDemoMode));
        vars.put(UT_TASK_PARAM_NAME_EMAIL_FROM, currentEnvironment.getStateEngineEmailFrom());
        vars.put(UT_TASK_PARAM_NAME_SUCCESS_EMAIL_TO, currentEnvironment.getStateEngineSuccessEmailTo());
        vars.put(UT_TASK_PARAM_NAME_FAILURE_EMAIL_TO, currentEnvironment.getStateEngineFailureEmailTo());
        vars.put(UT_TASK_PARAM_NAME_ACTIVE_CUSTOMERS, activeCustomers);
        vars.put(UT_TASK_PARAM_NAME_TEST_TYPE, getViewTestSupportMainProcess().getSelectedTestType());
        vars.put(UT_TASK_PARAM_NAME_TEST_PHASE, getViewTestSupportMainProcess().getSelectedTestPhase());
        vars.put(UT_TASK_PARAM_USE_ONLY_TEST_CLZ, getViewTestSupportMainProcess().isUseOnlyTestCLZs());
        vars.put(UT_TASK_PARAM_UPLOAD_SYNTH_TEST_CREFOS, getViewTestSupportMainProcess().isUploadSynthetics());
        return vars;
    }

    private void runInWorkerThread(String threadName, ThrowingRunnable task, Consumer<Exception> onError) {
        new Thread(() -> {
            enableComponentsToOnOff(false);
            SwingUtilities.invokeLater(() -> GUIStaticUtils.setWaitCursor(TestSupportView.this, true));
            try {
                task.run();
            } catch (Exception ex) {
                if (onError != null) onError.accept(ex);
            } finally {
                SwingUtilities.invokeLater(() -> GUIStaticUtils.setWaitCursor(TestSupportView.this, false));
                enableComponentsToOnOff(true);
            }
        }, threadName).start();
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }

    private void doChangeTestResources() {
        runInWorkerThread("test-resources-change", () -> {
            customerInitializer.initCustomers();
            currentEnvironment.setLastTestSource(getViewTestSupportMainProcess().getSelectedTestSource());
        }, ex -> notifyClientJob(Level.ERROR, GUIStaticUtils.showExceptionMessage(TestSupportView.this, "Quelle für Test-Resourcen ändern", ex)));
    }

    private void doChangeITSQRevision() {
        runInWorkerThread("itsq-revision-change", customerInitializer::initCustomers, null);
    }

    protected void doChangeTestType() {
        try {
            TestSupportClientKonstanten.TEST_TYPES selectedTestType = getViewTestSupportMainProcess().getSelectedTestType();
            if (!TimelineLogger.configure(currentEnvironment.getLogOutputsRootForEnv(getViewTestSupportMainControls().getSelectedEnvironmentName()), (selectedTestType + ".log"), "TimeLine.log")) {
                notifyClientJob(Level.ERROR, "Exception beim Konfigurieren der LOG-Dateien!\n");
            }
            notifyClientJob(Level.INFO, String.format("\nInitialisiere für den Test-Typ %s...", selectedTestType.getDescription()));
            getViewTestSupportMainProcess().initTestJobsCombo();
        } catch (Exception ex) {
            RuntimeException runtimeException = new RuntimeException("Exception beim Wechseln des Test-Typs!", ex);
            notifyClientJob(Level.ERROR, GUIStaticUtils.showExceptionMessage(this, "Initialisierung", runtimeException));
        }
    }

    void enableComponentsToOnOff(boolean enable) {
        Runnable task = () -> {
            for (JComponent component : componentsToOnOff) {
                component.setEnabled(enable);
            }
            if (enable) {
                try {
                    getViewTestSupportMainControls().updateAdminButtonState(currentEnvironment);
                    getViewTestSupportMainProcess().updateITSQRevisionEnabled();
                } catch (Exception ex) {
                    TimelineLogger.error(getClass(), "Fehler beim Aktivieren der GUI-Elemente", ex);
                }
                getViewTestSupportMainProcess().setStopButtonEnabled(false);
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            task.run();
        } else {
            SwingUtilities.invokeLater(task);
        }
    }

    private String createErrorFilesInfo(List<File> errorTxtFiles) {
        StringBuilder strErrBuilder = new StringBuilder("Folgende Fehlerdateien wurden erstellt:");
        for (File errorTxtFile : errorTxtFiles) {
            strErrBuilder.append("\n\t->").append(errorTxtFile.getAbsolutePath());
        }
        strErrBuilder.append("\nBitte überprüfen und OK wenn trotzdem weiter?");
        return strErrBuilder.toString();
    }

    private void appendToConsole(String message) {
        getTabbedPaneMonitor().appendToConsole(message);
    }

    /***********************************************************************************************************/
    /***************************************** TesunClientJobListener ******************************************/

    @Override
    public void notifyClientJob(Level level, Object notifyObject) {
        /* CLAUDE_MODE
         * Original: if (notifyObject instanceof CteActivitiTask) { notifyTask(...); }
         * Der CteActivitiTask-Branch entfällt im Tool — die StateMachine kennt
         * keine User-Tasks, nur Step-Namen, die als String-Meldungen kommen.
         */
        if (notifyObject instanceof InputStream) {
            notifyImage((InputStream) notifyObject);
        } else if (notifyObject instanceof String) {
            notifyMessage((String) notifyObject);
        } else if (notifyObject == null) {
            notifyProcessComplete();
        } else if (notifyObject instanceof Exception) {
            notifyError((Exception) notifyObject);
        } else {
            appendToConsole("?! Unbekanntes Notify-Objekt: " + notifyObject.getClass().getName() + " !?");
        }
    }

    private void notifyImage(InputStream imageStream) {
        getTabbedPaneMonitor().setProcessImage(imageStream);
    }

    private void notifyMessage(String msg) {
        if (!msg.equals(".")) {
            appendToConsole(msg);
        }
    }

    private void notifyProcessComplete() {
        TimelineLogger.info(this.getClass(), "===========    Prozess beendet.    ===========");
        String msg = "\n***********    Prozess-Thread beendet.    ***********\n===========    Prozess beendet.    ===========\nTest-Results sind im Output-Ordner gespeichert";
        processController.stop();
        /* CLAUDE_MODE
         * Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> activeTestCustomersMapMap = getAndCheckActiveCustomers();
         * viewTestResults.refreshTestResultsForMap(activeTestCustomersMapMap, true);
         */
        TimelineLogger.info(TestSupportView.class, msg);
        getTabbedPaneMonitor().appendToConsole(msg);
        SwingUtilities.invokeLater(() -> {
            enableComponentsToOnOff(true);
            GUIStaticUtils.setWaitCursor(this, false);
        });
    }

    private void notifyError(Exception ex) {
        appendToConsole(ex.getMessage());
    }

    @Override
    public Object askClientJob(TesunClientJobListener.ASK_FOR askFor, Object userObject) {
        try {
            switch (askFor) {
                case ASK_OBJECT_RETRY:
                    return GUIStaticUtils.showConfirmDialog(this, (userObject.toString() + "\nErneut versuchen?"), APP_TITLE);
                case ASK_OBJECT_CONTINUE:
                    return GUIStaticUtils.showConfirmDialog(this, userObject.toString(), APP_TITLE, JOptionPane.YES_NO_CANCEL_OPTION);
                case ASK_OBJECT_CTE_VERSION:
                    return Integer.valueOf(currentEnvironment.getCteVersion());
                case ASK_OBJECT_TEST_TYPE:
                    return getViewTestSupportMainProcess().getSelectedTestType();
                case ASK_OBJECT_USE_ITSQ_TEST_RESOURCES:
                    return getViewTestSupportMainProcess().getSelectedTestSource();
                case ASK_REF_EXPORTS_PATH:
                    return handleAskRefExportsPath();
                case ASK_TEST_CASES_PATH:
                    return GUIStaticUtils.chooseDirectory(this, currentEnvironment.getItsqRefExportsRoot().getAbsolutePath(), "Verzeichnis für die Testfälle angeben");
                case ASK_NEW_TEST_CASES_PATH:
                    return getViewTestSupportMainProcess().getTestCasesPath();
                case ASK_OBJECT_CHECK_DOWNLOADS:
                    return handleAskCheckDownloads(userObject);
                case ASK_WAIT_FOR_TEST_SYSTEM:
                    return JOptionPane.showConfirmDialog(this, "Sind die Testfälle im Test-System abgearbeitet?", APP_TITLE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                case ASK_OBJECT_COPY_EXPORTS_TO_INPUTS:
                    return handleAskCopyExports();
                case ASK_OBJECT_CREATE_NEW_SOLL:
                    return JOptionPane.showConfirmDialog(this, "Sollen neue SOLL Dateien generiert werden?", APP_TITLE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                case ASK_OBJECT_ANAYLSE_CHECKS:
                    return handleAskAnalyseChecks();
                case ASK_CHECK_COLLECTS:
                    return handleAskCheckCollects(userObject);
                case ASK_OBJECT_EXCEPTION:
                    return handleAskException(userObject);
                default:
                    throw new PropertiesException("Unbekannte Rückfrage: " + askFor + "!");
            }
        } catch (Exception ex) {
            GUIStaticUtils.showExceptionMessage(this, "! Fehler !", ex);
        }
        return null;
    }

    private String handleAskRefExportsPath() {
        File tmpFile = new File(getViewTestSupportMainProcess().getTestCasesPath());
        tmpFile = new File(tmpFile.getParentFile(), TestSupportClientKonstanten.REF_EXPORTS);
        return tmpFile.getAbsolutePath();
    }

    @SuppressWarnings("unchecked")
    private boolean handleAskCheckDownloads(Object userObject) {
        String strErr = createErrorFilesInfo((List<File>) userObject);
        return JOptionPane.showConfirmDialog(this, ("Fehler bei Download!\n" + strErr), APP_TITLE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private boolean handleAskCopyExports() {
        String strInfo = String.format("Bitte die Kunden-Exporte nach \n\t%s\nim jewewiligen Unterverzeichnis kopieren...", currentEnvironment.getItsqRefExportsRoot());
        strInfo += String.format("\nz.B für 'bvd' das Verzeichnis\n'y:/bvd/export/delta/2016-02-23_15-28'\nnach\n'%s/EXPORTS/bvd/export/delta/'\nkopieren.", currentEnvironment.getItsqRefExportsRoot());
        JOptionPane.showMessageDialog(this, strInfo, APP_TITLE, JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    private boolean handleAskAnalyseChecks() {
        JOptionPane.showMessageDialog(this, "Bitte Check-Ergebnisse prüfen...", APP_TITLE, JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    @SuppressWarnings("unchecked")
    private boolean handleAskCheckCollects(Object userObject) {
        String strErr = createErrorFilesInfo((List<File>) userObject);
        return JOptionPane.showConfirmDialog(this, ("Fehler bei Collect!\n" + strErr), APP_TITLE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private boolean handleAskException(Object userObject) {
        Throwable throwable = (Throwable) userObject;
        String errMsg = ExceptionUtils.buildExceptionMessage(throwable, 10);
        String s = errMsg != null ? errMsg.replaceAll("\\n", "") : throwable.getClass().getName();
        String strLog = "\n!!!\n\t" + s + "\n!!!\n";
        TimelineLogger.error(this.getClass(), strLog);
        getTabbedPaneMonitor().appendToConsole(strLog);
        return true;
    }

    /***************************************** TesunClientJobListener ******************************************/

    /***************************************** CommandExecutorListener *****************************************/
    @Override
    public void progress(String strInfo) {
        notifyClientJob(Level.INFO, strInfo);
    }
    /***************************************** CommandExecutorListener *****************************************/
}
