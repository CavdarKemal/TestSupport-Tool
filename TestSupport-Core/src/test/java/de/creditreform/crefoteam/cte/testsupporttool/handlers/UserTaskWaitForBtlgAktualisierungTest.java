package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit-Test fuer den frisch portierten {@link UserTaskWaitForBtlgAktualisierung}.
 * Verifiziert, dass der Handler im DemoMode den Task-Variables-Map
 * ohne REST-Aufruf zurueckgibt (kein Polling, kein Timeout).
 */
class UserTaskWaitForBtlgAktualisierungTest {

    @Test
    void runTask_inDemoMode_returnsMapUnchangedWithoutRest() throws Exception {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        UserTaskWaitForBtlgAktualisierung handler = new UserTaskWaitForBtlgAktualisierung(env, null);

        Map<String, Object> vars = new HashMap<>();
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE, Boolean.TRUE);
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE,
                TestSupportClientKonstanten.TEST_PHASE.PHASE_1);

        Map<String, Object> result = handler.runTask(vars);

        assertThat(result).isSameAs(vars);
        assertThat(result).hasSize(2);
    }
}
