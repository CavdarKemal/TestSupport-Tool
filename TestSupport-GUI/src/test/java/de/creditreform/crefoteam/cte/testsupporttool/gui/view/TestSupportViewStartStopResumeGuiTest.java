package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.testsupporttool.env.EnvironmentLockManager;
import de.creditreform.crefoteam.cte.testsupporttool.gui.BaseGUITest;
import de.creditreform.crefoteam.cte.testsupporttool.gui.TestSupportGUI;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;
import de.creditreform.crefoteam.cte.testsupporttool.resume.ResumeState;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JDialogOperator;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Jemmy-End-to-End-Test für den ECHTEN Start-Stop-Resume-Ablauf:
 * Prozess starten → mitten in Phase 1 stoppen → neu starten → "Ja"
 * im Resume-Dialog drücken → verifizieren dass fruehe Steps uebersprungen
 * werden.
 *
 * <p>Unterschied zu {@link TestSupportViewResumeGuiTest}: dieser Test
 * schreibt KEINEN künstlichen Resume-State, sondern erzeugt ihn durch
 * echten Stop-Click. Damit deckt er das realistische User-Verhalten ab,
 * das der kuenstliche Snapshot-Test nicht faengt.
 */
@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class TestSupportViewStartStopResumeGuiTest extends BaseGUITest {

    private static final String APP_TITLE = "CTE-Testautomatisierung";

    private File resumeFile;

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
        resumeFile = new File(env.getTestOutputsRoot(), ResumeState.FILE_NAME);
        // Saubere Ausgangssituation: kein Resume-State von vorherigen Tests
        ResumeState.delete(resumeFile);

        TestSupportGUI gui = createOnEdt(() -> {
            TestSupportGUI g = new TestSupportGUI(env);
            g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            g.setVisible(true);
            return g;
        });
        setGuiFrame(gui);
        waitForStartButtonEnabled(getView(), 30_000);

        // TEST_TYPE setzen — sonst routet der Gateway in den Failure-Branch.
        TestSupportView view = getView();
        SwingUtilities.invokeAndWait(() ->
                view.getViewTestSupportMainProcess().getComboBoxTestType()
                        .setSelectedItem(TestSupportClientKonstanten.TEST_TYPES.PHASE1_AND_PHASE2));
    }

    @AfterEach
    void cleanupResume() {
        ResumeState.delete(resumeFile);
    }

    @Test
    @Timeout(300)
    void startThenStopMidPhase1_thenResumeYes_skipsEarlierSteps() throws Exception {
        TestSupportView view = getView();

        // === 1. Durchlauf: Prozess starten, mitten in Phase 1 abbrechen ===
        pushSafely(frameOperator, "Prozess starten...");
        waitForStopButtonEnabled(view, 30_000);

        // Warten bis wir mitten in Phase 1 sind (StartBeteiligtenImport ist
        // Step 2 im Phase-1-Sub — lang genug nach dem Start, dass die
        // vorherigen Steps sauber als "ausgefuehrt" im Resume-State landen).
        waitForConsoleContains(view, "@UserTaskStartBeteiligtenImport", 60_000);

        // Stop
        pushSafely(new JButtonOperator(frameOperator, new StopButtonFinder(view)));
        waitForStartButtonEnabled(view, 30_000);

        // Resume-State muss existieren und einen nicht-leeren Index-Pfad haben
        assertThat(resumeFile).as("resume.properties muss nach Stop existieren").exists();
        ResumeState state = ResumeState.load(resumeFile);
        assertThat(state).as("geladener ResumeState").isNotNull();
        assertThat(state.indexPath()).as("indexPath nach Stop").isNotEmpty();
        assertThat(state.indexPath()[0]).as("1. Index-Eintrag zeigt auf Phase 1 (root index 2)").isEqualTo(2);

        // Console-Snapshot VOR dem Resume, damit wir spaeter nur den
        // Delta-Teil (was im 2. Durchlauf passiert) pruefen koennen.
        String beforeResume = getConsoleText(view);

        // === 2. Durchlauf: Resume mit "Ja" ===
        pushSafely(frameOperator, "Prozess starten...");

        JDialogOperator dialog = new JDialogOperator(APP_TITLE);
        pushSafely(new JButtonOperator(dialog, "Ja"));

        waitForStopButtonEnabled(view, 30_000);
        waitForStartButtonEnabled(view, 240_000);

        String afterResume = getConsoleText(view);
        String resumeDelta = afterResume.substring(beforeResume.length());

        // Resume-Banner muss vor den Step-Meldungen stehen
        assertThat(resumeDelta).as("Resume-Banner fehlt")
                .contains("RESUME-Modus");

        // "Resume erreicht bei ..." muss im Delta auftauchen
        assertThat(resumeDelta).as("Resume-Reached-Marker fehlt")
                .contains("→ Resume erreicht bei");

        // ZENTRALER Check: Steps VOR dem Resume-Punkt duerfen im Delta
        // NICHT als "@..." (runTask-Aufrufe) auftauchen — sie wurden ja
        // schon im 1. Durchlauf ausgefuehrt, oder sie werden via
        // shouldSkipForResume uebersprungen. Wenn sie hier auftauchen,
        // bedeutet das: der Prozess ist neu gestartet, nicht fortgesetzt.
        assertThat(resumeDelta).as("PrepareTestSystem darf im Resume NICHT erneut laufen")
                .doesNotContain("@UserTaskPrepareTestSystem");
        assertThat(resumeDelta).as("GeneratePseudoCrefos darf im Resume NICHT erneut laufen")
                .doesNotContain("@UserTaskGeneratePseudoCrefos");
        assertThat(resumeDelta).as("StartUploads (Phase-1, Step 0) darf im Resume NICHT erneut laufen")
                .doesNotContain("@UserTaskStartUploads für PHASE_1");

        // Nach erfolgreichem Abschluss muss die Datei geloescht sein
        assertThat(resumeFile).as("resume.properties nach Abschluss").doesNotExist();
    }

    // ---------------------------------------------------------------
    // Helpers (teils kopiert aus TestSupportViewGuiTest)
    // ---------------------------------------------------------------

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

    private void waitForConsoleContains(TestSupportView view, String needle, long timeoutMs) throws Exception {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            if (getConsoleText(view).contains(needle)) return;
            Thread.sleep(100);
        }
        throw new AssertionError("Konsole enthielt '" + needle + "' nicht rechtzeitig (Timeout " + timeoutMs + " ms)");
    }

    private String getConsoleText(TestSupportView view) throws Exception {
        String[] text = {""};
        SwingUtilities.invokeAndWait(() ->
                text[0] = view.getTabbedPaneMonitor().getTextAreaTaskListenerInfo().getText());
        return text[0];
    }

    private static class StopButtonFinder implements ComponentChooser {
        private final TestSupportView view;
        StopButtonFinder(TestSupportView view) { this.view = view; }
        @Override public boolean checkComponent(Component comp) {
            return comp == view.getViewTestSupportMainProcess().getButtonStopUserTasksThread();
        }
        @Override public String getDescription() { return "Stop-Button"; }
    }
}
