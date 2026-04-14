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
        EnvironmentConfig env = EnvironmentConfig.forDemo("http://unused");
        ProcessDefinition def = CteAutomatedTestProcess.build(env, null);

        // Prepare → Gateway → SUB1 → SUB2 → SuccessMail → Restore  (6) + Failure-Branch (2)
        assertThat(def.steps()).hasSize(6);
        assertThat(def.failureSteps()).hasSize(2);
        assertThat(def.name()).isEqualTo("CteAutomatedTestProcess");
    }

    @Test
    void subProcess_buildsWithNineSteps() throws PropertiesException {
        EnvironmentConfig env = EnvironmentConfig.forDemo("http://unused");
        ProcessDefinition sub = CteAutomatedTestProcessSUB.build(env, null);

        assertThat(sub.steps()).hasSize(9);
        assertThat(sub.name()).isEqualTo("CteAutomatedTestProcessSUB");
    }
}
