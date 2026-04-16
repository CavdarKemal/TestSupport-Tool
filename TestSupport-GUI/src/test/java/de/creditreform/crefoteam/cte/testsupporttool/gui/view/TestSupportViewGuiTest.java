package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.BaseGUITest;
import de.creditreform.crefoteam.cte.testsupporttool.gui.TestSupportGUI;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;
import de.creditreform.crefoteam.cte.testsupporttool.env.EnvironmentLockManager;
import de.creditreform.crefoteam.cte.testsupporttool.resume.ResumeState;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Jemmy-basierter GUI-Test fuer {@link TestSupportView}. Pendant zum
 * {@code TestSupportViewActivitiTest} aus dem Original — hier aber gegen
 * die {@code ProcessController}-basierte StateMachine, d.h. ohne Activiti-
 * Docker-Container + ohne Persistenz / "Fortsetzen" (diese Szenarien
 * machen im StateMachine-Modell keinen Sinn und wurden weggelassen).
 *
 * <p>Der Test verifiziert, dass ein kompletter Demo-Mode-Durchlauf
 * sauber durch die GUI anstoßbar ist und die Console alle erwarteten
 * Step-Namen in der korrekten Reihenfolge zeigt.
 *
 * <p>Voraussetzung: {@code X-TESTS/ITSQ/REF-EXPORTS/...} im Projekt-Root
 * sowie {@code ENE-config.properties} im user.dir (auch fuer
 * {@code CteTestAutomatisierungTest} noetig).
 */
@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class TestSupportViewGuiTest extends BaseGUITest {

    @BeforeAll
    static void assumeNotHeadless() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM — Jemmy braucht echtes Display");
    }

    @AfterAll
    static void releaseGlobal() {
        try { TimelineLogger.close(); } catch (Exception ignored) { }
        try { EnvironmentLockManager.releaseLock(); } catch (Exception ignored) { }
    }

    @BeforeEach
    void openGui() throws Exception {
        try { EnvironmentLockManager.releaseLock(); } catch (Exception ignored) { }
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        // Saubere Ausgangssituation: Resume-State eines vorherigen Tests
        // (z.B. ProcessControllerTest.stop_*) loeschen, sonst blockiert der
        // Fortsetzen-Dialog den Testlauf.
        ResumeState.delete(new File(env.getTestOutputsRoot(), ResumeState.FILE_NAME));
        TestSupportGUI gui = createOnEdt(() -> {
            TestSupportGUI g = new TestSupportGUI(env);
            g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            g.setVisible(true);
            return g;
        });
        setGuiFrame(gui);
        waitForStartButtonEnabled(getView(), 30_000);
    }

    @Test
    @Timeout(300)
    void startProzess_ganzDurchlaufen_enthaeltAlleStepsInKonsole() throws Exception {
        TestSupportView view = getView();
        // TEST_TYPE explizit setzen — sonst kann env.getLastTestType() zu FailureMail routen,
        // statt durch den Sub-Prozess zu laufen.
        SwingUtilities.invokeAndWait(() ->
                view.getViewTestSupportMainProcess().getComboBoxTestType()
                        .setSelectedItem(TestSupportClientKonstanten.TEST_TYPES.PHASE1_AND_PHASE2));

        pushSafely(frameOperator, "Prozess starten...");

        waitForStopButtonEnabled(view, 30_000);
        waitForStartButtonEnabled(view, 180_000);

        String consoleText = getConsoleText(view);
        verifyStepOrder(consoleText, ALL_STEP_TOKENS);
    }

    @Test
    @Timeout(60)
    void stopButton_brichtLaufendenProzessAb() throws Exception {
        TestSupportView view = getView();
        pushSafely(frameOperator, "Prozess starten...");
        waitForStopButtonEnabled(view, 30_000);

        // Stop-Button druecken — Engine bricht am naechsten Step ab.
        pushSafely(new org.netbeans.jemmy.operators.JButtonOperator(
                frameOperator, new StopButtonFinder(view)));
        waitForStartButtonEnabled(view, 30_000);

        String console = getConsoleText(view);
        assertThat(console).contains("Prozess beendet");
    }

    // ------------------------------------------------------------------
    // Erwartete Step-Reihenfolge (Haupt + 2× SUB + Endsteps)
    //
    // Die @-Token kommen aus AbstractUserTaskRunnable.buildNotifyStringForClassName:
    //   "\n@" + getClass().getSimpleName()
    // ------------------------------------------------------------------

    private static final String[] MAIN_START = {
            "@UserTaskPrepareTestSystem",
            "@UserTaskGeneratePseudoCrefos"
    };

    private static final String[] SUB_PHASE = {
            "@UserTaskStartUploads",
            "@UserTaskWaitBeforBeteiligtenImport",
            "@UserTaskStartBeteiligtenImport",
            "@UserTaskWaitForBeteiligtenImport",
            "@UserTaskStartEntgBerechnung",
            "@UserTaskWaitForEntgBerechnung",
            "@UserTaskStartBtlgAktualisierung",
            "@UserTaskWaitForBtlgAktualisierung",
            "@UserTaskWaitBeforeCtImport",
            "@UserTaskStartCtImport",
            "@UserTaskWaitForCtImport",
            "@UserTaskWaitBeforeExport",
            "@UserTaskStartExports",
            "@UserTaskStartCollect",
            "@UserTaskCheckCollects",
            "@UserTaskStartRestore",
            "@UserTaskCheckRefExports",
            "@UserTaskStartSftpUploads",
            "@UserTaskCheckSftpUploads"
    };

    private static final String[] MAIN_END = {
            "@UserTaskSuccessMail",
            "@UserTaskRestoreTestSystem"
    };

    private static final List<String> ALL_STEP_TOKENS;
    static {
        java.util.List<String> list = new java.util.ArrayList<>();
        list.addAll(Arrays.asList(MAIN_START));
        list.addAll(Arrays.asList(SUB_PHASE)); // Phase 1
        list.addAll(Arrays.asList(SUB_PHASE)); // Phase 2
        list.addAll(Arrays.asList(MAIN_END));
        ALL_STEP_TOKENS = java.util.Collections.unmodifiableList(list);
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private TestSupportView getView() {
        return ((TestSupportGUI) guiFrame).getTestSupportView();
    }

    private void waitForStartButtonEnabled(TestSupportView view, long timeoutMs) {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            try {
                boolean[] enabled = {false};
                SwingUtilities.invokeAndWait(() ->
                        enabled[0] = view.getViewTestSupportMainProcess().getButtonStartProcess().isEnabled());
                if (enabled[0]) return;
                Thread.sleep(200);
            } catch (Exception ignored) { }
        }
        throw new AssertionError("Start-Button wurde nicht rechtzeitig aktiviert (Timeout " + timeoutMs + " ms)");
    }

    private void waitForStopButtonEnabled(TestSupportView view, long timeoutMs) throws Exception {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            boolean[] enabled = {false};
            SwingUtilities.invokeAndWait(() ->
                    enabled[0] = view.getViewTestSupportMainProcess().getButtonStopUserTasksThread().isEnabled());
            if (enabled[0]) return;
            Thread.sleep(100);
        }
        throw new AssertionError("Stop-Button wurde nicht rechtzeitig aktiviert (Timeout " + timeoutMs + " ms)");
    }

    private String getConsoleText(TestSupportView view) throws Exception {
        String[] text = {""};
        SwingUtilities.invokeAndWait(() ->
                text[0] = view.getTabbedPaneMonitor().getTextAreaTaskListenerInfo().getText());
        return text[0];
    }

    private void verifyStepOrder(String consoleText, List<String> tokens) {
        int searchFrom = 0;
        for (String token : tokens) {
            int pos = consoleText.indexOf(token, searchFrom);
            if (pos < 0) {
                String snippet = consoleText.length() > 3000
                        ? consoleText.substring(0, 3000) + "..."
                        : consoleText;
                throw new AssertionError("Token '" + token + "' nicht in Konsole nach Position " + searchFrom
                        + " gefunden.\nKonsoleninhalt:\n" + snippet);
            }
            searchFrom = pos + token.length();
        }
    }

    /** Finder, der den Stop-Button-JButton in der Komponenten-Hierarchie anhand des Icon-Pfads lokalisiert. */
    private static class StopButtonFinder implements org.netbeans.jemmy.ComponentChooser {
        private final TestSupportView view;
        StopButtonFinder(TestSupportView view) { this.view = view; }
        @Override public boolean checkComponent(java.awt.Component comp) {
            return comp == view.getViewTestSupportMainProcess().getButtonStopUserTasksThread();
        }
        @Override public String getDescription() { return "Stop-Button"; }
    }
}
