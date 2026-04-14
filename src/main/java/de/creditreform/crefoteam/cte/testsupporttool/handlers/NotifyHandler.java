package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.statemachine.ProcessContext;
import de.creditreform.crefoteam.cte.statemachine.Step;
import de.creditreform.crefoteam.cte.statemachine.StepResult;
import org.apache.log4j.Logger;

import java.util.Objects;

/**
 * Trivialer Step für Erfolgs-/Fehler-Benachrichtigungen — Platzhalter für
 * die echten {@code UserTaskSuccessMail} / {@code UserTaskFailureMail}.
 */
public final class NotifyHandler implements Step {

    private static final Logger LOG = Logger.getLogger(NotifyHandler.class);

    private final String name;
    private final String message;

    public NotifyHandler(String name, String message) {
        this.name = Objects.requireNonNull(name, "name");
        this.message = Objects.requireNonNull(message, "message");
    }

    @Override
    public StepResult execute(ProcessContext context) {
        LOG.info("[" + name + "] " + message);
        return StepResult.NEXT;
    }

    @Override
    public String name() {
        return name;
    }
}
