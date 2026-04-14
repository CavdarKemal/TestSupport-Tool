package de.creditreform.crefoteam.cte.testsupporttool.process;

import de.creditreform.crefoteam.cte.statemachine.ProcessDefinition;
import de.creditreform.crefoteam.cte.statemachine.steps.ConditionalStep;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.NotifyHandler;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.PrepareTestSystemHandler;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.StartCtImportHandler;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.WaitForCtImportHandler;
import de.creditreform.crefoteam.cte.testsupporttool.rest.TesunRestService;

/**
 * Factory für die Spike-Prozess-Definition. Spiegelt den Kern von
 * {@code CteAutomatedTestProcess.bpmn} in stark reduzierter Form.
 */
public final class TestAutomationProcess {

    public static final String TEST_TYPE_PHASE1_AND_PHASE2 = "PHASE1_AND_PHASE2";

    private TestAutomationProcess() { }

    public static ProcessDefinition build(EnvironmentConfig env, TesunRestService rest) {
        return ProcessDefinition.builder("TestAutomationProcess")
                .step(new PrepareTestSystemHandler(env))
                .step(new ConditionalStep(
                        "TestTypeGateway",
                        ctx -> TEST_TYPE_PHASE1_AND_PHASE2
                                .equals(ctx.get(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_TYPE)),
                        new StartCtImportHandler(rest, "ImportJVM", "FROM_STAGING_INTO_CTE"),
                        new NotifyHandler("FailureMail", "Test-Typ wird nicht unterstützt.")
                ))
                .step(new WaitForCtImportHandler(rest, env, "FROM_STAGING_INTO_CTE"))
                .step(new NotifyHandler("SuccessMail", "Test-Automation erfolgreich beendet."))
                .onFailure(new NotifyHandler("FailureMail", "Test-Automation fehlgeschlagen."))
                .build();
    }
}
