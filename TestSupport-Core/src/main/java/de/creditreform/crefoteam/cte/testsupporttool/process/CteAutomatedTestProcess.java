package de.creditreform.crefoteam.cte.testsupporttool.process;

import de.creditreform.crefoteam.cte.statemachine.ProcessDefinition;
import de.creditreform.crefoteam.cte.statemachine.steps.ConditionalStep;
import de.creditreform.crefoteam.cte.statemachine.steps.SubProcessStep;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskFailureMail;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskGeneratePseudoCrefos;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskPrepareTestSystem;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskRestoreTestSystem;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskSuccessMail;

/**
 * State-Machine-Factory für den Haupt-Prozess. Spiegelt den Original-BPMN
 * {@code CteAutomatedTestProcess.bpmn}:
 *
 * <pre>
 *   Start → PrepareTestSystem
 *         → Gateway: TEST_TYPE == "PHASE1_AND_PHASE2"
 *             ├── true  → GeneratePseudoCrefos → SUB-Phase-1 → SUB-Phase-2 → SuccessMail → RestoreTestSystem → End
 *             └── false → FailureMail → RestoreTestSystem → End
 *   onFailure → FailureMail → RestoreTestSystem
 * </pre>
 *
 * <p>Konfiguriert wird der Sub-Prozess separat via
 * {@link CteAutomatedTestProcessSUB#build(EnvironmentConfig, TesunClientJobListener)}.
 */
public final class CteAutomatedTestProcess {

    public static final String TEST_TYPE_PHASE1_AND_PHASE2 = "PHASE1_AND_PHASE2";

    private CteAutomatedTestProcess() { }

    public static ProcessDefinition build(EnvironmentConfig env, TesunClientJobListener listener) throws PropertiesException {
        ProcessDefinition sub = CteAutomatedTestProcessSUB.build(env, listener);

        return ProcessDefinition.builder("CteAutomatedTestProcess")
                .step(new UserTaskPrepareTestSystem(env, listener))
                .step(new ConditionalStep(
                        "TestTypeGateway",
                        ctx -> TEST_TYPE_PHASE1_AND_PHASE2.equals(
                                ctx.get(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_TYPE)),
                        new UserTaskGeneratePseudoCrefos(env, listener),
                        new UserTaskFailureMail(env, listener)
                ))
                .step(new SubProcessStep(sub))                                // Phase 1
                .step(new SubProcessStep(sub))                                // Phase 2
                .step(new UserTaskSuccessMail(env, listener))
                .step(new UserTaskRestoreTestSystem(env, listener))
                .onFailure(new UserTaskFailureMail(env, listener))
                .onFailure(new UserTaskRestoreTestSystem(env, listener))
                .build();
    }
}
