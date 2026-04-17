package de.creditreform.crefoteam.cte.testsupporttool.resume;

import de.creditreform.crefoteam.cte.statemachine.ProcessContext;
import de.creditreform.crefoteam.cte.statemachine.ProcessDefinition;
import de.creditreform.crefoteam.cte.statemachine.Step;
import de.creditreform.crefoteam.cte.statemachine.StepResult;
import de.creditreform.crefoteam.cte.statemachine.steps.SubProcessStep;
import org.apache.log4j.Level;

/**
 * Resume-bewusster Wrapper um {@link SubProcessStep}: Liegt der gesamte
 * Sub-Prozess VOR dem Resume-Zielpunkt, wird der Sub-Prozess-Run komplett
 * übersprungen (kein "Prozess gestartet"-Log, keine Schritt-Iteration).
 *
 * <p>Liegt der Sub-Prozess BEIM oder NACH dem Resume-Zielpunkt, läuft er
 * normal — die einzelnen Steps überspringen sich intern selbst via
 * {@link de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskRunnable#shouldSkipForResume}.
 */
public final class ResumeAwareSubProcessStep implements Step {

    private final SubProcessStep delegate;

    public ResumeAwareSubProcessStep(ProcessDefinition subDefinition) {
        this.delegate = new SubProcessStep(subDefinition);
    }

    @Override
    public StepResult execute(ProcessContext context) throws Exception {
        if (shouldSkipEntireSubProcess(context)) {
            return StepResult.NEXT;
        }
        return delegate.execute(context);
    }

    /**
     * Gibt true zurück wenn der erste Index-Eintrag dieses Sub-Prozesses
     * KLEINER ist als der erste Index-Eintrag des Resume-Pfades —
     * d.h. die gesamte Phase liegt VOR dem Wiederaufnahme-Punkt.
     */
    private boolean shouldSkipEntireSubProcess(ProcessContext context) {
        if (Boolean.TRUE.equals(context.get(ResumeMarker.RESUME_REACHED))) {
            return false;
        }
        int[] resumePath = (int[]) context.get(ResumeMarker.RESUME_INDEX_PATH);
        if (resumePath == null || resumePath.length == 0) {
            return false;
        }
        int[] currentPath = ResumePathUtil.computeIndexPath(context);
        if (currentPath.length == 0) {
            return false;
        }
        return currentPath[0] < resumePath[0];
    }

    @Override
    public String name() {
        return delegate.name();
    }

    /** Gibt die interne SubProcessStep-Instanz zurück (für DiagramImageListener.bind). */
    public SubProcessStep delegate() {
        return delegate;
    }
}
