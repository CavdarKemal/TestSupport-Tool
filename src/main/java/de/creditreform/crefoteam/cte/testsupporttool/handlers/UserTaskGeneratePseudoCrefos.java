package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskRunnable;
import org.apache.log4j.Level;

import java.util.Map;

/**
 * Port aus {@code testsupport_client.tesun_activiti.handlers}.
 *
 * <p><b>Spike-Hinweis:</b> Der ursprüngliche Handler ruft
 * {@code TestFallGeneratePseudoCrefos} auf — eine eigene Generator-Klasse mit
 * weiteren Abhängigkeiten (Replacer, ZIP-Handling etc.). Im Spike wird in
 * Demo-Mode nur protokolliert; im Real-Mode wird eine
 * {@link UnsupportedOperationException} geworfen, bis der Generator portiert
 * ist.
 */
public class UserTaskGeneratePseudoCrefos extends AbstractUserTaskRunnable {

    public UserTaskGeneratePseudoCrefos(EnvironmentConfig environmentConfig, TesunClientJobListener listener) {
        super(environmentConfig, listener);
    }

    @Override
    public Map<String, Object> runTask(Map<String, Object> taskVariablesMap) {
        TestSupportClientKonstanten.TEST_PHASE phase = (TestSupportClientKonstanten.TEST_PHASE)
                taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE);
        notifyUserTask(Level.INFO, buildNotifyStringForClassName(phase));
        if (checkDemoMode((Boolean) taskVariablesMap.get(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE))) {
            return taskVariablesMap;
        }
        throw new UnsupportedOperationException(
                "GeneratePseudoCrefos im Real-Mode benötigt den Pseudo-Crefo-Generator " +
                "(TestFallGeneratePseudoCrefos), der noch nicht portiert ist. " +
                "Aktuell nur Demo-Mode unterstützt.");
    }
}
