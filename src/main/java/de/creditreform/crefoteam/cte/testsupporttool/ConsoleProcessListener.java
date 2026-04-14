package de.creditreform.crefoteam.cte.testsupporttool;

import de.creditreform.crefoteam.cte.statemachine.ProcessListener;
import de.creditreform.crefoteam.cte.statemachine.StepResult;
import org.apache.log4j.Logger;

/**
 * Lifecycle-Listener für die Engine — protokolliert Prozess- und
 * Step-Ereignisse über log4j. Pendant zum produktiven
 * {@code TesunClientJobListener}, der zusätzlich GUI-Komponenten benachrichtigt.
 */
public final class ConsoleProcessListener implements ProcessListener {

    private static final Logger LOG = Logger.getLogger(ConsoleProcessListener.class);

    @Override public void onProcessStarted(String processName) {
        LOG.info("===== Prozess gestartet: " + processName + " =====");
    }

    @Override public void onProcessCompleted(String processName) {
        LOG.info("===== Prozess erfolgreich: " + processName + " =====");
    }

    @Override public void onProcessFailed(String processName, Throwable cause) {
        LOG.error("===== Prozess FEHLGESCHLAGEN: " + processName + " =====",
                cause);
    }

    @Override public void onProcessAborted(String processName, String reason) {
        LOG.warn("===== Prozess ABGEBROCHEN: " + processName + " — " + reason + " =====");
    }

    @Override public void onStepStarted(String stepName) {
        LOG.info("--> Step: " + stepName);
    }

    @Override public void onStepCompleted(String stepName, StepResult result) {
        LOG.debug("    Step '" + stepName + "' fertig: " + result);
    }

    @Override public void onStepFailed(String stepName, Throwable cause) {
        LOG.error("    Step '" + stepName + "' fehlgeschlagen: "
                + (cause != null ? cause.getMessage() : "FAIL-Result"));
    }

    @Override public boolean askForRetry(String stepName, Throwable cause) {
        // Im Spike: kein automatischer Retry. Die GUI-Variante würde hier
        // einen modalen Dialog anzeigen.
        return false;
    }
}
