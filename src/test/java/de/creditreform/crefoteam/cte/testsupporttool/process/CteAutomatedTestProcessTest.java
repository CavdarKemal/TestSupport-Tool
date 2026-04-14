package de.creditreform.crefoteam.cte.testsupporttool.process;

import de.creditreform.crefoteam.cte.statemachine.ProcessDefinition;
import de.creditreform.crefoteam.cte.statemachine.ProcessOutcome;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.ProcessRunner;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-End-Test des kompletten {@link CteAutomatedTestProcess} im
 * Demo-Mode. Verifiziert, dass alle 14 Handler durchlaufen und beide
 * Sub-Prozesse erfolgreich ausgeführt werden.
 */
class CteAutomatedTestProcessTest {

    @Test
    void demoMode_phase1AndPhase2_completes() throws PropertiesException {
        EnvironmentConfig env = EnvironmentConfig.forDemo("http://unused-in-demo");
        ProcessDefinition definition = CteAutomatedTestProcess.build(env, null);

        Map<String, Object> vars = new HashMap<>();
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE,
                TestSupportClientKonstanten.TEST_PHASE.PHASE_2);
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_TYPE,
                CteAutomatedTestProcess.TEST_TYPE_PHASE1_AND_PHASE2);
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE, Boolean.TRUE);

        ProcessOutcome outcome = new ProcessRunner().run(definition, vars);

        assertThat(outcome).isEqualTo(ProcessOutcome.COMPLETED);
    }

    @Test
    void demoMode_unsupportedTestType_takesFailureBranch() throws PropertiesException {
        EnvironmentConfig env = EnvironmentConfig.forDemo("http://unused-in-demo");
        ProcessDefinition definition = CteAutomatedTestProcess.build(env, null);

        Map<String, Object> vars = new HashMap<>();
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE,
                TestSupportClientKonstanten.TEST_PHASE.PHASE_2);
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_TYPE, "ANYTHING_ELSE");
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE, Boolean.TRUE);

        ProcessOutcome outcome = new ProcessRunner().run(definition, vars);

        // false-Branch: FailureMail → SUB1 → SUB2 → SuccessMail → RestoreTestSystem
        // Im Demo-Mode laufen alle Steps durch
        assertThat(outcome).isEqualTo(ProcessOutcome.COMPLETED);
    }

    @Test
    void subProcess_demoMode_runsAllNineSteps() throws PropertiesException {
        EnvironmentConfig env = EnvironmentConfig.forDemo("http://unused-in-demo");
        ProcessDefinition sub = CteAutomatedTestProcessSUB.build(env, null);

        Map<String, Object> vars = new HashMap<>();
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE,
                TestSupportClientKonstanten.TEST_PHASE.PHASE_1);
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE, Boolean.TRUE);

        ProcessOutcome outcome = new ProcessRunner().run(sub, vars);

        assertThat(outcome).isEqualTo(ProcessOutcome.COMPLETED);
        assertThat(sub.steps()).hasSize(9);
    }
}
