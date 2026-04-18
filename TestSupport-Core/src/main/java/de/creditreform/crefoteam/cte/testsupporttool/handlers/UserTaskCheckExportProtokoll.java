package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.statemachine.ProcessContext;
import de.creditreform.crefoteam.cte.statemachine.StepResult;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskRunnable;
import org.apache.log4j.Level;

import java.util.Map;

/**
 * Schalen-Port. Original ruft gegen die Masterkonsole den Export-Tracking-
 * Endpunkt ({@code TesunRestService.getExportTrackingInfo}) und prüft pro
 * Kunde, ob alle Crefos im Export-Protokoll auftauchen. Im Spike:
 * Demo-Mode-tauglich, Real-Mode noch nicht portiert.
 *
 * <p>Wird übersprungen, wenn die Umgebung
 * {@code CHECK-EXPORT-PROTOKOLL-ACTIVE=false} konfiguriert hat (Original).
 */
public class UserTaskCheckExportProtokoll extends AbstractUserTaskRunnable {
    public UserTaskCheckExportProtokoll(final EnvironmentConfig environmentConfig, TesunClientJobListener tesunClientJobListener) {
        super(environmentConfig, tesunClientJobListener);
    }

    // Sonderfall: Wenn disabled, wird komplett anstelle des Standard-Preambles
    // geloggt (ersetzt die Standard-Notify, kommt nicht zusätzlich).
    @Override
    public StepResult execute(ProcessContext context) throws Exception {
        if (!environmentConfig.isCheckExportProtokollEnabled()) {
            TestSupportClientKonstanten.TEST_PHASE phase = (TestSupportClientKonstanten.TEST_PHASE) context.variables().get(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE);
            notifyUserTask(Level.INFO, buildNotifyStringForClassName(phase) + " wird übersprungen, da deaktiviert!");
            return StepResult.NEXT;
        }
        return super.execute(context);
    }

    @Override
    public Map<String, Object> runTask(Map<String, Object> taskVariablesMap) {
        // TODO aus Original wiederherstellen!
        throw new UnsupportedOperationException("CheckExportProtokoll im Real-Mode erfordert TesunRestService.getExportTrackingInfo + xmlbinding.trackingexport (noch nicht portiert).");
    }
}
