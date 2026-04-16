package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
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

    @Override
    public Map<String, Object> runTask(Map<String, Object> taskVariablesMap) throws PropertiesException {
        TestSupportClientKonstanten.TEST_PHASE testPhase = (TestSupportClientKonstanten.TEST_PHASE) taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE);
        if (!environmentConfig.isCheckExportProtokollEnabled()) {
            notifyUserTask(Level.INFO, buildNotifyStringForClassName(testPhase) + " wird übersprungen, da deaktiviert!");
            return taskVariablesMap;
        }
        notifyUserTask(Level.INFO, buildNotifyStringForClassName(testPhase));
        if (checkDemoMode((Boolean) taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE))) {
            return taskVariablesMap;
        }
        throw new UnsupportedOperationException(
                "CheckExportProtokoll im Real-Mode erfordert TesunRestService.getExportTrackingInfo + xmlbinding.trackingexport (noch nicht portiert).");
    }
}
