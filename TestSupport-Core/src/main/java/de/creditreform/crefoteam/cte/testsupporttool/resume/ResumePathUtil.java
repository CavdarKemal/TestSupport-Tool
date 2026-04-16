package de.creditreform.crefoteam.cte.testsupporttool.resume;

import de.creditreform.crefoteam.cte.statemachine.ProcessContext;
import de.creditreform.crefoteam.cte.statemachine.ProcessDefinition;
import de.creditreform.crefoteam.cte.statemachine.Step;
import de.creditreform.crefoteam.cte.statemachine.steps.SubProcessStep;

import java.util.List;

/**
 * Berechnet aus einem {@link ProcessContext#activePath()} den Index-Pfad
 * bezueglich der {@link ProcessContext#rootDefinition()}. Der Index-Pfad
 * ist ein {@code int[]}, das jeden Step im activePath eindeutig lokalisiert:
 *
 * <pre>
 *   activePath = [ phase2-SubProcessStep, UserTaskStartCtImport ]
 *   rootDef.steps()     = [ PrepareTestSystem, Gateway, phase1, phase2, SuccessMail, Restore ]
 *                                                          ^^^^ index 3
 *   phase2.subDefinition().steps() = [ StartUploads, WaitBefor..., ..., StartCtImport, ... ]
 *                                                                       ^^^^^^^^^^^^ index 9
 *   → indexPath = [3, 9]
 * </pre>
 *
 * <p>Nutzt Identity-Vergleich ({@code ==}) fuer Step-Lookup, weil zwei
 * {@code SubProcessStep}-Instanzen, die dieselbe {@code ProcessDefinition}
 * kapseln (Phase 1 vs. Phase 2), Namens-identisch sind.
 */
public final class ResumePathUtil {

    private ResumePathUtil() { }

    /** @return Index-Pfad des aktuellen activePath, ausgerichtet an der rootDefinition. */
    public static int[] computeIndexPath(ProcessContext ctx) {
        if (ctx == null) return new int[0];
        List<Step> path = ctx.activePath();
        ProcessDefinition def = ctx.rootDefinition();
        if (def == null || path.isEmpty()) return new int[0];
        int[] indices = new int[path.size()];
        ProcessDefinition current = def;
        for (int i = 0; i < path.size(); i++) {
            Step s = path.get(i);
            indices[i] = indexOfIdentity(current.steps(), s);
            if (s instanceof SubProcessStep && i < path.size() - 1) {
                current = ((SubProcessStep) s).subDefinition();
            }
        }
        return indices;
    }

    private static int indexOfIdentity(List<Step> list, Step target) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == target) return i;
        }
        return -1;
    }
}
