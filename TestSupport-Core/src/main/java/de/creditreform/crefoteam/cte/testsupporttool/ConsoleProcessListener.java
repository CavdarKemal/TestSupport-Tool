package de.creditreform.crefoteam.cte.testsupporttool;

import de.creditreform.crefoteam.cte.statemachine.ProcessListener;
import de.creditreform.crefoteam.cte.statemachine.StepResult;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;

/**
 * Lifecycle-Listener für die Engine — protokolliert Prozess- und
 * Step-Ereignisse über den {@link TimelineLogger}, sodass zusätzlich zur
 * App-Log-Datei ein Timeline-Eintrag entsteht.
 */
public final class ConsoleProcessListener implements ProcessListener {

    @Override public void onProcessStarted(String processName) {
        TimelineLogger.info(ConsoleProcessListener.class, "===== Prozess gestartet: {} =====", processName);
        TimelineLogger.event("process.started", processName);
    }

    @Override public void onProcessCompleted(String processName) {
        TimelineLogger.info(ConsoleProcessListener.class, "===== Prozess erfolgreich: {} =====", processName);
        TimelineLogger.event("process.completed", processName);
    }

    @Override public void onProcessFailed(String processName, Throwable cause) {
        TimelineLogger.error(ConsoleProcessListener.class,
                "===== Prozess FEHLGESCHLAGEN: " + processName + " =====", cause);
        TimelineLogger.event("process.failed", processName);
    }

    @Override public void onProcessAborted(String processName, String reason) {
        TimelineLogger.warn(ConsoleProcessListener.class,
                "===== Prozess ABGEBROCHEN: {} — {} =====", processName, reason);
        TimelineLogger.event("process.aborted", processName + " (" + reason + ")");
    }

    @Override public void onStepStarted(String stepName) {
        TimelineLogger.info(ConsoleProcessListener.class, "--> Step: {}", stepName);
    }

    @Override public void onStepCompleted(String stepName, StepResult result) {
        TimelineLogger.debug(ConsoleProcessListener.class, "    Step '{}' fertig: {}", stepName, result);
    }

    @Override public void onStepFailed(String stepName, Throwable cause) {
        if (cause != null) {
            TimelineLogger.error(ConsoleProcessListener.class,
                    "    Step '" + stepName + "' fehlgeschlagen", cause);
        } else {
            TimelineLogger.error(ConsoleProcessListener.class,
                    "    Step '{}' fehlgeschlagen (FAIL-Result)", stepName);
        }
    }

    @Override public boolean askForRetry(String stepName, Throwable cause) {
        // Im Spike: kein automatischer Retry. Die GUI-Variante würde hier
        // einen modalen Dialog anzeigen.
        return false;
    }
}
