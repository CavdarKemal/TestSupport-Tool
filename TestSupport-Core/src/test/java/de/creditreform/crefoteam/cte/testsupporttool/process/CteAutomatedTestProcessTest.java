package de.creditreform.crefoteam.cte.testsupporttool.process;

import de.creditreform.crefoteam.cte.statemachine.ProcessDefinition;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Strukturtest der Prozess-Definitionen — keine Ausführung, keine
 * Initialisierung. End-to-End-Tests laufen über
 * {@link de.creditreform.crefoteam.cte.testsupporttool.auto.CteTestAutomatisierung}.
 */
class CteAutomatedTestProcessTest {

    @Test
    void mainProcess_buildsWithSevenTopLevelSteps() throws PropertiesException {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        CteAutomatedTestProcess.Assembly assembly = CteAutomatedTestProcess.build(env, null);
        ProcessDefinition def = assembly.definition();

        // Prepare → Gateway → SUB1 → SUB2 → SuccessMail → Restore  (6) + Failure-Branch (2)
        assertThat(def.steps()).hasSize(6);
        assertThat(def.failureSteps()).hasSize(2);
        assertThat(def.name()).isEqualTo("CteAutomatedTestProcess");
        assertThat(assembly.phase1()).isNotSameAs(assembly.phase2());
    }

    @Test
    void subProcess_buildsWithTwentyStepsMatchingBpmn() throws PropertiesException {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        ProcessDefinition sub = CteAutomatedTestProcessSUB.build(env, null);

        // 20 UserTasks exakt wie im Original-BPMN (CteAutomatedTestProcessSUB.bpmn):
        // 9 Start-Handler + 4 WaitFor + 3 WaitBefor(e) + 4 Check.
        assertThat(sub.steps()).hasSize(20);
        assertThat(sub.name()).isEqualTo("CteAutomatedTestProcessSUB");
    }
}
