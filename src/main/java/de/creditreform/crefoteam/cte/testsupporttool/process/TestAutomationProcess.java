package de.creditreform.crefoteam.cte.testsupporttool.process;

import de.creditreform.crefoteam.cte.statemachine.ProcessDefinition;
import de.creditreform.crefoteam.cte.statemachine.steps.ConditionalStep;
import de.creditreform.crefoteam.cte.testsupporttool.config.EnvironmentConfig;
import de.creditreform.crefoteam.cte.testsupporttool.config.TestSupportConstants;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.NotifyHandler;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.PrepareTestSystemHandler;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.StartCtImportHandler;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.WaitForCtImportHandler;
import de.creditreform.crefoteam.cte.testsupporttool.rest.TesunRestService;

/**
 * Factory für die Spike-Prozess-Definition. Spiegelt den Kern von
 * {@code CteAutomatedTestProcess.bpmn} in stark reduzierter Form:
 *
 * <pre>
 *   PrepareTestSystem
 *        │
 *        ▼
 *   Gateway: TEST_TYPE == "PHASE1_AND_PHASE2"
 *     ├── true ──▶ StartCtImport ──▶ WaitForCtImport ──▶ SuccessMail
 *     └── false ─▶ FailureMail
 *
 *   onFailure: FailureMail
 * </pre>
 *
 * Im echten Tool kommen hier die 35 Handler + zwei Sub-Prozesse hinzu —
 * jedes weitere Handler-Tripel folgt demselben Muster.
 */
public final class TestAutomationProcess {

    private TestAutomationProcess() { }

    public static ProcessDefinition build(EnvironmentConfig env, TesunRestService rest) {
        return ProcessDefinition.builder("TestAutomationProcess")
                .step(new PrepareTestSystemHandler(env))
                .step(new ConditionalStep(
                        "TestTypeGateway",
                        ctx -> TestSupportConstants.TEST_TYPE_PHASE1_AND_PHASE2
                                .equals(ctx.get(TestSupportConstants.VAR_TEST_TYPE)),
                        // True-Branch: voller Ablauf
                        new StartCtImportHandler(rest, "ImportJVM", "FROM_STAGING_INTO_CTE"),
                        // False-Branch: direkt zur Fehler-Mail
                        new NotifyHandler("FailureMail", "Test-Typ wird nicht unterstützt.")
                ))
                .step(new WaitForCtImportHandler(rest, env, TestSupportConstants.CT_IMPORT_PROCESS))
                .step(new NotifyHandler("SuccessMail", "Test-Automation erfolgreich beendet."))
                .onFailure(new NotifyHandler("FailureMail", "Test-Automation fehlgeschlagen."))
                .build();
    }
}
