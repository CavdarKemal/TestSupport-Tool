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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration-Test fuer {@link ProcessController} — StateMachine-Pendant zum
 * frueheren {@code ActivitiProcessControllerTest}.
 *
 * <p>Voraussetzung wie bei {@code CteTestAutomatisierungTest}: {@code
 * X-TESTS/ITSQ/REF-EXPORTS/PHASE-{1,2}} im Projekt-Root (per
 * maven-dependency-plugin in {@code generate-resources} entpackt).
 */
class ProcessControllerTest {

    private CteTestAutomatisierung runner;

    @BeforeEach
    void clearResumeBefore() throws Exception {
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
    void runProcess_inDemoMode_completesSuccessfully() throws Exception {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);

        CollectingListener listener = new CollectingListener();
        ProcessController controller = new ProcessController(listener);

        Map<String, Object> vars = runner.buildTaskVariablesMap(
                true, // Demo-Mode — Handler loggen nur, keine Server-Calls
                runner.getTestCustomerMapMap(),
                TestSupportClientKonstanten.TEST_TYPES.PHASE1_AND_PHASE2,
                TestSupportClientKonstanten.TEST_PHASE.PHASE_1,
                true, false);

        ProcessOutcome outcome = controller.runProcess(env, vars);

        assertThat(outcome).isEqualTo(ProcessOutcome.COMPLETED);
    }

    @Test
    void runProcess_emitsDiagramImages() throws Exception {
        // Der DiagramImageListener rendert pro Step ein PNG und pusht es an
        // den Listener als InputStream — im Laufe des Prozesses müssen
        // mehrere Images fliessen.
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);

        CollectingListener listener = new CollectingListener();
        ProcessController controller = new ProcessController(listener);

        Map<String, Object> vars = runner.buildTaskVariablesMap(
                true,
                runner.getTestCustomerMapMap(),
                TestSupportClientKonstanten.TEST_TYPES.PHASE1_AND_PHASE2,
                TestSupportClientKonstanten.TEST_PHASE.PHASE_1,
                true, false);

        controller.runProcess(env, vars);
        assertThat(listener.imageCount).isGreaterThan(5);
    }

    @Test
    void stop_cancelsRunningProcessBeforeCompletion() throws Exception {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        runner = new CteTestAutomatisierung(env);

        CancelAfterFirstImageListener listener = new CancelAfterFirstImageListener();
        ProcessController controller = new ProcessController(listener);
        listener.controller = controller;

        Map<String, Object> vars = runner.buildTaskVariablesMap(
                true,
                runner.getTestCustomerMapMap(),
                TestSupportClientKonstanten.TEST_TYPES.PHASE1_AND_PHASE2,
                TestSupportClientKonstanten.TEST_PHASE.PHASE_1,
                true, false);

        ProcessOutcome outcome = controller.runProcess(env, vars);
        assertThat(outcome).isEqualTo(ProcessOutcome.ABORTED);
    }

    // ------------------------------------------------------------------

    private static class CollectingListener implements TesunClientJobListener {
        final List<String> messages = new ArrayList<>();
        int imageCount;

        @Override
        public void notifyClientJob(Level level, Object notifyObject) {
            if (notifyObject instanceof InputStream) {
                imageCount++;
            } else if (notifyObject != null) {
                messages.add(notifyObject.toString());
            }
        }

        @Override
        public Object askClientJob(ASK_FOR askFor, Object userObject) {
            return Boolean.FALSE;
        }
    }

    private static class CancelAfterFirstImageListener implements TesunClientJobListener {
        ProcessController controller;
        AtomicBoolean cancelled = new AtomicBoolean();

        @Override
        public void notifyClientJob(Level level, Object notifyObject) {
            if (notifyObject instanceof InputStream && cancelled.compareAndSet(false, true)) {
                controller.stop();
            }
        }

        @Override
        public Object askClientJob(ASK_FOR askFor, Object userObject) {
            return Boolean.FALSE;
        }
    }
}
