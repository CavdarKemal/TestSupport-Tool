package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.testsupporttool.env.EnvironmentLockManager;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;

import java.io.File;
import org.apache.log4j.Level;

/**
 * Kapselt die Logik für den Umgebungswechsel (Environment-Switch).
 * Port aus {@code testsupport_client.tesun.gui.view.EnvironmentSwitchHandler}.
 *
 * <p>Abweichend vom Original wird {@code ActivitiTestSupport} nicht neu
 * instanziiert — der StateMachine-Prozess holt sich die Konfiguration direkt
 * aus {@link EnvironmentConfig}.
 */
class EnvironmentSwitchHandler {

    private final TestSupportView view;

    EnvironmentSwitchHandler(TestSupportView view) {
        this.view = view;
    }

    void doChangeEnvironment() {
        String newEnv = view.getViewTestSupportMainControls().getSelectedEnvironmentName();
        try {
            EnvironmentConfig environmentConfig = new EnvironmentConfig(newEnv);
            checkEnvironmentLock(environmentConfig, newEnv);
            TestSupportClientKonstanten.TEST_TYPES selectedTestType = view.getViewTestSupportMainProcess().getSelectedTestType();
            if (!TimelineLogger.configure(
                    view.currentEnvironment.getLogOutputsRootForEnv(view.getViewTestSupportMainControls().getSelectedEnvironmentName()),
                    (selectedTestType + ".log"), "TimeLine.log")) {
                view.notifyClientJob(Level.ERROR, "Exception beim Konfigurieren der LOG-Dateien!\n");
            }
            String testSetSource = view.getViewTestSupportMainProcess().getSelectedTestSource();
            environmentConfig.setLastTestSource(testSetSource);
            environmentConfig.setLastItsqRevision(view.getViewTestSupportMainProcess().getSelectedITSQRevision());
            environmentConfig.setLastTestType(selectedTestType);
            environmentConfig.setLastUseOnlyTestClz(view.getViewTestSupportMainProcess().isUseOnlyTestCLZs());
            environmentConfig.setLastUploadSynthetics(view.getViewTestSupportMainProcess().isUploadSynthetics());
            view.guiFrame.setEnvironmentConfig(environmentConfig);
            view.currentEnvironment = view.guiFrame.getEnvironmentConfig();
            /* CLAUDE_MODE
             * Original: view.activitiTestSupport = new ActivitiTestSupport(view.currentEnvironment, view);
             * Im Tool entfällt — StateMachine/CteAutomatedTestProcess nimmt die
             * EnvironmentConfig direkt beim Prozess-Start entgegen.
             */
            view.initForEnvironment();
        } catch (Exception ex) {
            RuntimeException runtimeException = new RuntimeException(("Exception beim Initialisieren der Umgebung " + newEnv + "!"), ex);
            view.notifyClientJob(Level.ERROR, GUIStaticUtils.showExceptionMessage(view, "Initialisierung", runtimeException));
            view.getViewTestSupportMainControls().setSelectedEnvironment(view.currentEnvironment.getCurrentEnvName());
        }
    }

    static void checkEnvironmentLock(EnvironmentConfig environmentConfig, String newEnv) throws PropertiesException {
        File lockDir = environmentConfig.getLogOutputsRootForEnv(newEnv);
        if (EnvironmentLockManager.isLocked(lockDir)) {
            throw new RuntimeException("Die Umgebung " + environmentConfig.getCurrentEnvName() + " ist gesperrt, da eine andere Instanz in dieser Umgebung läuft.");
        }
        EnvironmentLockManager.releaseLock();
        if (!EnvironmentLockManager.acquireLock(lockDir, environmentConfig.getCurrentEnvName())) {
            throw new RuntimeException("Konnte Lock für die Umgebung " + environmentConfig.getCurrentEnvName() + " nicht erwerben!");
        }
    }
}
