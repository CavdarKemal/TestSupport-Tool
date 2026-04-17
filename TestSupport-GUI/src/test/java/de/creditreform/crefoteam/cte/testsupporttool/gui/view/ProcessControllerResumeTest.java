package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.statemachine.ProcessOutcome;
import de.creditreform.crefoteam.cte.testsupporttool.auto.CteTestAutomatisierung;
import de.creditreform.crefoteam.cte.testsupporttool.resume.ResumeState;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import org.apache.log4j.Level;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration-Test fuer {@link ProcessController#runProcess(EnvironmentConfig, Map, int[])}
 * mit Resume-Pfad. Verifiziert, dass Handler vor dem Resume-Punkt tatsaechlich
 * uebersprungen werden und der Prozess regulaer beendet wird.
 */
class ProcessControllerResumeTest {

    private CteTestAutomatisierung runner;

    @BeforeEach
    void clearResume() throws Exception {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        ResumeState.delete(new File(env.getTestOutputsRoot(), ResumeState.FILE_NAME));
    }

    @AfterEach
    void cleanup() throws Exception {
        if (runner != null) {
            runner.shutdown();
            runner = null;
        }
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        ResumeState.delete(new File(env.getTestOutputsRoot(), ResumeState.FILE_NAME));
    }

    @Test
    void resumeInPhase2_skipsAllEarlierSteps() throws Exception {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);
        MessageCollector listener = new MessageCollector();

        ProcessController controller = new ProcessController(listener);
        Map<String, Object> vars = runner.buildTaskVariablesMap(
                true,
                runner.getTestCustomerMapMap(),
                TestSupportClientKonstanten.TEST_TYPES.PHASE1_AND_PHASE2,
                TestSupportClientKonstanten.TEST_PHASE.PHASE_1,
                true, false);

        // Resume bei Phase-2 (Main-Index 3), Sub-Step 19 (= UserTaskCheckSftpUploads).
        int[] resumePath = {3, 19};
        ProcessOutcome outcome = controller.runProcess(env, vars, resumePath);

        assertThat(outcome).isEqualTo(ProcessOutcome.COMPLETED);
        // Vor Phase-2 duerfen keine @Handler-Meldungen vorkommen.
        // Hinweis: Die TEST_PHASE-Variable wird in der StateMachine nicht
        // pro SubProcess umgesetzt — daher steht "für PHASE_1" auch fuer
        // Steps, die in Phase-2 laufen. Wir pruefen Handler-Namen, nicht
        // Phase-Labels.
        assertThat(listener.joined())
                .doesNotContain("@UserTaskPrepareTestSystem")
                .doesNotContain("@UserTaskGeneratePseudoCrefos")
                .doesNotContain("@UserTaskStartUploads")
                .doesNotContain("@UserTaskStartCollect")
                .doesNotContain("@UserTaskCheckCollects")
                .doesNotContain("@UserTaskStartSftpUploads");
        // Ab dem Resume-Step muessen die Meldungen da sein.
        assertThat(listener.joined())
                .contains("@UserTaskCheckSftpUploads")
                .contains("@UserTaskSuccessMail")
                .contains("@UserTaskRestoreTestSystem")
                .contains("→ Resume erreicht bei UserTaskCheckSftpUploads");
    }

    @Test
    void nullResumePath_runsCompleteProcessWithAllSteps() throws Exception {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);
        MessageCollector listener = new MessageCollector();

        ProcessController controller = new ProcessController(listener);
        Map<String, Object> vars = runner.buildTaskVariablesMap(
                true,
                runner.getTestCustomerMapMap(),
                TestSupportClientKonstanten.TEST_TYPES.PHASE1_AND_PHASE2,
                TestSupportClientKonstanten.TEST_PHASE.PHASE_1,
                true, false);

        ProcessOutcome outcome = controller.runProcess(env, vars, null);

        assertThat(outcome).isEqualTo(ProcessOutcome.COMPLETED);
        // StartUploads laeuft je einmal in Phase-1 und Phase-2 → 2 Vorkommen,
        // jeweils mit dem korrekten Phase-Label (via PhaseTrackingListener).
        assertThat(listener.joined())
                .contains("@UserTaskPrepareTestSystem")
                .contains("@UserTaskGeneratePseudoCrefos")
                .contains("@UserTaskStartUploads für PHASE_1")
                .contains("@UserTaskStartUploads für PHASE_2")
                .contains("@UserTaskSuccessMail")
                .doesNotContain("→ Resume erreicht");
        long startUploadsCount = countOccurrences(listener.joined(), "@UserTaskStartUploads");
        assertThat(startUploadsCount).isEqualTo(2L);
    }

    @Test
    void phaseTracking_updatesTestPhaseVariable_atSubProcessEntry() throws Exception {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);
        MessageCollector listener = new MessageCollector();

        ProcessController controller = new ProcessController(listener);
        Map<String, Object> vars = runner.buildTaskVariablesMap(
                true,
                runner.getTestCustomerMapMap(),
                TestSupportClientKonstanten.TEST_TYPES.PHASE1_AND_PHASE2,
                TestSupportClientKonstanten.TEST_PHASE.PHASE_1,
                true, false);

        controller.runProcess(env, vars, null);
        // Jeder Sub-Step muss unter dem richtigen Phase-Label erscheinen —
        // nicht alle mit PHASE_1.
        String console = listener.joined();
        assertThat(console).contains("@UserTaskWaitForBeteiligtenImport für PHASE_1");
        assertThat(console).contains("@UserTaskWaitForBeteiligtenImport für PHASE_2");
        assertThat(console).contains("@UserTaskCheckSftpUploads für PHASE_1");
        assertThat(console).contains("@UserTaskCheckSftpUploads für PHASE_2");
    }

    @Test
    void phaseTracking_resumeSnapshotRecordsCorrectPhase() throws Exception {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);
        File resumeFile = new File(env.getTestOutputsRoot(), ResumeState.FILE_NAME);

        // Abbruch in Phase-2 — der Listener muss PHASE_2 in den Snapshot schreiben.
        CancelWhenPhase2Listener listener = new CancelWhenPhase2Listener();
        ProcessController controller = new ProcessController(listener);
        listener.controller = controller;

        Map<String, Object> vars = runner.buildTaskVariablesMap(
                true,
                runner.getTestCustomerMapMap(),
                TestSupportClientKonstanten.TEST_TYPES.PHASE1_AND_PHASE2,
                TestSupportClientKonstanten.TEST_PHASE.PHASE_1,
                true, false);

        controller.runProcess(env, vars, null);
        assertThat(resumeFile).exists();
        ResumeState state = ResumeState.load(resumeFile);
        assertThat(state).isNotNull();
        assertThat(state.testPhase()).isEqualTo("PHASE_2");
    }

    @Test
    void abortedRunLeavesResumeFileBehind() throws Exception {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);
        File resumeFile = new File(env.getTestOutputsRoot(), ResumeState.FILE_NAME);
        assertThat(resumeFile).doesNotExist();

        CancelOnFirstMessageListener listener = new CancelOnFirstMessageListener();
        ProcessController controller = new ProcessController(listener);
        listener.controller = controller;

        Map<String, Object> vars = runner.buildTaskVariablesMap(
                true,
                runner.getTestCustomerMapMap(),
                TestSupportClientKonstanten.TEST_TYPES.PHASE1_AND_PHASE2,
                TestSupportClientKonstanten.TEST_PHASE.PHASE_1,
                true, false);

        controller.runProcess(env, vars, null);
        // Nach Abbruch muss die Resume-Datei stehen bleiben fuer den naechsten Start.
        assertThat(resumeFile).exists();
        ResumeState state = ResumeState.load(resumeFile);
        assertThat(state).isNotNull();
        assertThat(state.indexPath()).isNotEmpty();
    }

    @Test
    void completedRunDeletesResumeFile() throws Exception {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);
        File resumeFile = new File(env.getTestOutputsRoot(), ResumeState.FILE_NAME);

        ProcessController controller = new ProcessController(new MessageCollector());
        Map<String, Object> vars = runner.buildTaskVariablesMap(
                true,
                runner.getTestCustomerMapMap(),
                TestSupportClientKonstanten.TEST_TYPES.PHASE1_AND_PHASE2,
                TestSupportClientKonstanten.TEST_PHASE.PHASE_1,
                true, false);

        controller.runProcess(env, vars, null);
        assertThat(resumeFile).doesNotExist();
    }

    private static long countOccurrences(String haystack, String needle) {
        int count = 0;
        int idx = 0;
        while ((idx = haystack.indexOf(needle, idx)) != -1) {
            count++;
            idx += needle.length();
        }
        return count;
    }

    // ---- Helpers -----------------------------------------------------

    private static class MessageCollector implements TesunClientJobListener {
        private final List<String> messages = new ArrayList<>();
        @Override public void notifyClientJob(Level level, Object notifyObject) {
            if (notifyObject instanceof String) {
                messages.add((String) notifyObject);
            }
        }
        @Override public Object askClientJob(ASK_FOR askFor, Object userObject) { return Boolean.FALSE; }
        String joined() { return String.join("\n", messages); }
    }

    private static class CancelOnFirstMessageListener implements TesunClientJobListener {
        ProcessController controller;
        boolean cancelled;
        @Override public void notifyClientJob(Level level, Object notifyObject) {
            if (!cancelled && notifyObject instanceof String && notifyObject.toString().contains("@UserTask")) {
                cancelled = true;
                controller.stop();
            }
        }
        @Override public Object askClientJob(ASK_FOR askFor, Object userObject) { return Boolean.FALSE; }
    }

    private static class CancelWhenPhase2Listener implements TesunClientJobListener {
        ProcessController controller;
        boolean cancelled;
        @Override public void notifyClientJob(Level level, Object notifyObject) {
            if (!cancelled && notifyObject instanceof String && notifyObject.toString().contains("für PHASE_2")) {
                cancelled = true;
                controller.stop();
            }
        }
        @Override public Object askClientJob(ASK_FOR askFor, Object userObject) { return Boolean.FALSE; }
    }
}
