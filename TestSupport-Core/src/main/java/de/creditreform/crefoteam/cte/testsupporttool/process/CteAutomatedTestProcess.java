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

    /**
     * Baut den Hauptprozess. Liefert sowohl die {@link ProcessDefinition} als
     * auch die beiden {@link SubProcessStep}-Instanzen — diese werden vom
     * {@code DiagramImageListener} per {@code .bind(...)} an die BPMN-IDs
     * {@code CallActivityRepeatableTestAutomationProcess2SUB1}/{@code ...SUB2}
     * gebunden, damit in beiden Call-Activities die richtige Hervorhebung
     * erscheint (dieselbe Sub-Definition wird zweimal verwendet).
     */
    public static Assembly build(EnvironmentConfig env, TesunClientJobListener listener) throws PropertiesException {
        ProcessDefinition sub = CteAutomatedTestProcessSUB.build(env, listener);
        SubProcessStep phase1 = new SubProcessStep(sub);
        SubProcessStep phase2 = new SubProcessStep(sub);

        ProcessDefinition definition = ProcessDefinition.builder("CteAutomatedTestProcess")
                .step(new UserTaskPrepareTestSystem(env, listener))
                .step(new ConditionalStep(
                        "TestTypeGateway",
                        ctx -> {
                            // TEST_TYPE kommt aus der Variablen-Map als TEST_TYPES-Enum
                            // (CteTestAutomatisierung + TestSupportView setzen ein Enum,
                            // nicht einen String). Der frühere String-equals-Vergleich
                            // mit "PHASE1_AND_PHASE2" lieferte daher immer false und
                            // routete zwangsweise in den Failure-Branch.
                            Object v = ctx.get(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_TYPE);
                            return v != null && TEST_TYPE_PHASE1_AND_PHASE2.equals(v.toString());
                        },
                        new UserTaskGeneratePseudoCrefos(env, listener),
                        new UserTaskFailureMail(env, listener)
                ))
                .step(phase1)
                .step(phase2)
                .step(new UserTaskSuccessMail(env, listener))
                .step(new UserTaskRestoreTestSystem(env, listener))
                .onFailure(new UserTaskFailureMail(env, listener))
                .onFailure(new UserTaskRestoreTestSystem(env, listener))
                .build();

        return new Assembly(definition, phase1, phase2);
    }

    /** Tuple: Definition + Sub-Step-Referenzen für Diagramm-Bindings. */
    public static final class Assembly {
        private final ProcessDefinition definition;
        private final SubProcessStep phase1;
        private final SubProcessStep phase2;

        Assembly(ProcessDefinition definition, SubProcessStep phase1, SubProcessStep phase2) {
            this.definition = definition;
            this.phase1 = phase1;
            this.phase2 = phase2;
        }

        public ProcessDefinition definition() { return definition; }
        public SubProcessStep phase1() { return phase1; }
        public SubProcessStep phase2() { return phase2; }
    }
}
