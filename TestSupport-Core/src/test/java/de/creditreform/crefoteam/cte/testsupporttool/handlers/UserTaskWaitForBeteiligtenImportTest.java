package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.statemachine.ProcessContext;
import de.creditreform.crefoteam.cte.statemachine.StepResult;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit-Test fuer den frisch portierten {@link UserTaskWaitForBeteiligtenImport}.
 * Verifiziert, dass der Handler im DemoMode ohne REST-Aufruf zurueckkehrt
 * (kein Polling, kein Timeout) — der Demo-Mode-Check findet in
 * {@code AbstractUserTaskRunnable.execute()} statt und skippt runTask.
 */
class UserTaskWaitForBeteiligtenImportTest {

    @Test
    void execute_inDemoMode_returnsNextWithoutRest() throws Exception {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        UserTaskWaitForBeteiligtenImport handler = new UserTaskWaitForBeteiligtenImport(env, null);

        Map<String, Object> vars = new HashMap<>();
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE, Boolean.TRUE);
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE,
                TestSupportClientKonstanten.TEST_PHASE.PHASE_1);

        ProcessContext ctx = ProcessContext.create(vars, null);
        StepResult result = handler.execute(ctx);

        assertThat(result).isEqualTo(StepResult.NEXT);
        assertThat(ctx.variables()).containsAllEntriesOf(vars);
    }
}
