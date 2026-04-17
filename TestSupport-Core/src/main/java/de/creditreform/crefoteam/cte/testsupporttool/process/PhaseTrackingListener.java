package de.creditreform.crefoteam.cte.testsupporttool.process;

import de.creditreform.crefoteam.cte.statemachine.ProcessContext;
import de.creditreform.crefoteam.cte.statemachine.ProcessListener;
import de.creditreform.crefoteam.cte.statemachine.Step;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;

import java.util.List;

/**
 * {@link ProcessListener}, der vor jedem Step-Start die Variable
 * {@link TesunClientJobListener#UT_TASK_PARAM_NAME_TEST_PHASE} passend
 * zur aktuell ausgefuehrten Phase (Phase-1- oder Phase-2-SubProcess)
 * setzt.
 *
 * <p>Hintergrund: der Haupt-Prozess legt den initialen Wert (PHASE_1) in
 * die TaskVariablesMap; die StateMachine-Library setzt die Variable beim
 * Eintritt in einen SubProcessStep nicht neu — alle Logs der Phase-2-Steps
 * wuerden sonst faelschlich "fuer PHASE_1" zeigen, und der
 * {@code ResumeStateWriter} wuerde fuer einen Abbruch in Phase-2 ebenfalls
 * PHASE_1 persistieren.
 *
 * <p>Identity-Check: der Konstruktor nimmt die konkreten {@code phase1}/{@code phase2}
 * {@code SubProcessStep}-Instanzen aus der {@link CteAutomatedTestProcess.Assembly};
 * die Listener-Abfrage vergleicht per {@code ==} im activePath.
 */
public final class PhaseTrackingListener implements ProcessListener {

    private final Step phase1;
    private final Step phase2;

    public PhaseTrackingListener(Step phase1, Step phase2) {
        this.phase1 = phase1;
        this.phase2 = phase2;
    }

    @Override
    public void onStepStarted(ProcessContext context, Step step) {
        TestSupportClientKonstanten.TEST_PHASE detected = null;
        List<Step> path = context.activePath();
        for (Step s : path) {
            if (s == phase1) { detected = TestSupportClientKonstanten.TEST_PHASE.PHASE_1; break; }
            if (s == phase2) { detected = TestSupportClientKonstanten.TEST_PHASE.PHASE_2; break; }
        }
        if (detected != null) {
            context.variables().put(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE, detected);
        }
        // Ausserhalb der SubProcesses (Main-Start/End) bleibt die Variable
        // auf ihrem vorherigen Wert — PrepareTestSystem laeuft damit mit dem
        // initialen GUI-Wert (meist PHASE_1), SuccessMail/Restore bleiben
        // auf dem zuletzt gesetzten PHASE_2.
    }
}
