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
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JDialogOperator;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Jemmy-Tests fuer den Resume-Dialog in {@link TestSupportView#startActivitiProcess()}.
 *
 * <p>Setup pro Test: es wird ein kuenstlicher {@code resume.properties}-
 * Snapshot in {@code <testOutputsRoot>/resume.properties} geschrieben,
 * die GUI geoeffnet und dann "Prozess starten..." geklickt. Erwartung:
 * der YES/NO/CANCEL-Dialog erscheint — pro Test wird ein anderer Button
 * gedrueckt und der Folgezustand verifiziert.
 */
@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class TestSupportViewResumeGuiTest extends BaseGUITest {

    private static final String APP_TITLE = "CTE-Testautomatisierung";
    /** Resume-Punkt kurz vor dem Prozess-Ende: Phase-2, letzter Sub-Step (CheckSftpUploads). */
    private static final int[] RESUME_INDEX_PATH = {3, 19};

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
    void prepareSnapshotAndOpenGui() throws Exception {
        try { EnvironmentLockManager.releaseLock(); } catch (Exception ignored) { }
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        resumeFile = new File(env.getTestOutputsRoot(), ResumeState.FILE_NAME);

        // Kuenstlichen Resume-Snapshot hinterlegen — triggered den Dialog
        // beim ersten Klick auf "Prozess starten...".
        new ResumeState(RESUME_INDEX_PATH, "UserTaskCheckSftpUploads", "PHASE_2").save(resumeFile);
        assertThat(resumeFile).as("Snapshot-Vorbereitung").exists();

        TestSupportGUI gui = createOnEdt(() -> {
            TestSupportGUI g = new TestSupportGUI(env);
            g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            g.setVisible(true);
            return g;
        });
        setGuiFrame(gui);
        waitForStartButtonEnabled(getView(), 30_000);

        // TEST_TYPE explizit setzen (sonst routet env.getLastTestType() ggf. den Failure-Branch).
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
    @Timeout(120)
    void dialog_yesButton_fortsetzenAbResumeStep() throws Exception {
        TestSupportView view = getView();
        pushSafely(frameOperator, "Prozess starten...");

        // Dialog erscheint, "Ja" druecken.
        JDialogOperator dialog = new JDialogOperator(APP_TITLE);
        pushSafely(new JButtonOperator(dialog, "Ja"));

        waitForStopButtonEnabled(view, 30_000);
        waitForStartButtonEnabled(view, 60_000);

        String console = getConsoleText(view);
        // Phase-1 und die fruehen Phase-2-Steps werden uebersprungen —
        // nur ab UserTaskCheckSftpUploads werden Handler ausgefuehrt.
        assertThat(console)
                .contains("→ Resume erreicht bei UserTaskCheckSftpUploads")
                .contains("@UserTaskCheckSftpUploads für PHASE_2")
                .contains("@UserTaskSuccessMail")
                .contains("@UserTaskRestoreTestSystem")
                .doesNotContain("@UserTaskPrepareTestSystem")
                .doesNotContain("@UserTaskStartUploads");

        // Nach Normal-Ende ist die Datei wieder weg.
        assertThat(resumeFile).doesNotExist();
    }

    @Test
    @Timeout(300)
    void dialog_noButton_neustartVonVorn() throws Exception {
        TestSupportView view = getView();
        pushSafely(frameOperator, "Prozess starten...");

        // Dialog erscheint, "Nein" druecken — kompletter Neustart.
        JDialogOperator dialog = new JDialogOperator(APP_TITLE);
        pushSafely(new JButtonOperator(dialog, "Nein"));

        waitForStopButtonEnabled(view, 30_000);
        waitForStartButtonEnabled(view, 240_000);

        String console = getConsoleText(view);
        // Kompletter Durchlauf — kein "Resume erreicht", dafuer alle Start-Handler.
        assertThat(console)
                .doesNotContain("→ Resume erreicht")
                .contains("@UserTaskPrepareTestSystem")
                .contains("@UserTaskStartUploads für PHASE_1")
                .contains("@UserTaskStartUploads für PHASE_2")
                .contains("@UserTaskRestoreTestSystem");
        // Nach Normal-Ende ist die Datei wieder weg.
        assertThat(resumeFile).doesNotExist();
    }

    @Test
    @Timeout(30)
    void dialog_cancelButton_keinProzessLauft() throws Exception {
        TestSupportView view = getView();
        pushSafely(frameOperator, "Prozess starten...");

        // Dialog erscheint, "Abbrechen" druecken.
        JDialogOperator dialog = new JDialogOperator(APP_TITLE);
        pushSafely(new JButtonOperator(dialog, "Abbrechen"));

        // Start-Button bleibt enabled (kein Prozess laeuft), Stop-Button bleibt disabled.
        Thread.sleep(1000); // Kurz warten, bis potenzielle EDT-Events durch sind.
        final boolean[] stopEnabled = {false};
        SwingUtilities.invokeAndWait(() ->
                stopEnabled[0] = view.getViewTestSupportMainProcess().getButtonStopUserTasksThread().isEnabled());
        assertThat(stopEnabled[0]).as("Stop-Button darf nach Cancel NICHT enabled sein").isFalse();

        String console = getConsoleText(view);
        assertThat(console).doesNotContain("@UserTaskPrepareTestSystem");

        // Datei bleibt unveraendert stehen — beim naechsten Start waere sie wieder da.
        assertThat(resumeFile).exists();
        ResumeState stillThere = ResumeState.load(resumeFile);
        assertThat(stillThere).isNotNull();
        assertThat(stillThere.testPhase()).isEqualTo("PHASE_2");
    }

    // ----------- Helpers (copy of TestSupportViewGuiTest) -------------

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
}
