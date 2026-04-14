package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.statemachine.ProcessContext;
import de.creditreform.crefoteam.cte.statemachine.Step;
import de.creditreform.crefoteam.cte.statemachine.StepResult;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;

import java.util.Objects;

/**
 * Trivialer Step für Erfolgs-/Fehler-Benachrichtigungen — Platzhalter für
 * die echten {@code UserTaskSuccessMail} / {@code UserTaskFailureMail}.
 */
public final class NotifyHandler implements Step {

    private final String name;
    private final String message;

    public NotifyHandler(String name, String message) {
        this.name = Objects.requireNonNull(name, "name");
        this.message = Objects.requireNonNull(message, "message");
    }

    @Override
    public StepResult execute(ProcessContext context) {
        TimelineLogger.info(NotifyHandler.class, "[{}] {}", name, message);
        TimelineLogger.event(name, message);
        return StepResult.NEXT;
    }

    @Override
    public String name() {
        return name;
    }
}
