package de.creditreform.crefoteam.cte.testsupporttool;

import de.creditreform.crefoteam.cte.statemachine.ProcessContext;
import de.creditreform.crefoteam.cte.statemachine.ProcessDefinition;
import de.creditreform.crefoteam.cte.statemachine.ProcessEngine;
import de.creditreform.crefoteam.cte.statemachine.ProcessListener;
import de.creditreform.crefoteam.cte.statemachine.ProcessOutcome;

import java.util.Map;
import java.util.Objects;

/**
 * Schlanker Wrapper um die {@link ProcessEngine} — Pendant zum bisherigen
 * {@code ActivitiProcessController}.
 *
 * <p>Hält die Engine-Instanz, baut den {@link ProcessContext} aus
 * Initialvariablen und setzt einen Default-Listener, falls keiner übergeben
 * wird.
 */
public final class ProcessRunner {

    private final ProcessEngine engine;
    private final ProcessListener defaultListener;

    public ProcessRunner() {
        this(new ProcessEngine(), new ConsoleProcessListener());
    }

    public ProcessRunner(ProcessEngine engine, ProcessListener defaultListener) {
        this.engine = Objects.requireNonNull(engine, "engine");
        this.defaultListener = Objects.requireNonNull(defaultListener, "defaultListener");
    }

    /** Führt den Prozess mit den Initialvariablen + Default-Listener aus. */
    public ProcessOutcome run(ProcessDefinition definition, Map<String, Object> initialVariables) {
        ProcessContext ctx = ProcessContext.create(initialVariables, defaultListener);
        return engine.run(definition, ctx);
    }

    /** Führt den Prozess mit einem explizit übergebenen Context aus. */
    public ProcessOutcome run(ProcessDefinition definition, ProcessContext context) {
        return engine.run(definition, context);
    }
}
