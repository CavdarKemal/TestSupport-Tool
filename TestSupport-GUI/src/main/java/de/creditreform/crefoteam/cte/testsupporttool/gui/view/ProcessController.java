package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.statemachine.ProcessContext;
import de.creditreform.crefoteam.cte.statemachine.ProcessEngine;
import de.creditreform.crefoteam.cte.statemachine.ProcessListener;
import de.creditreform.crefoteam.cte.statemachine.ProcessOutcome;
import de.creditreform.crefoteam.cte.statemachine.diagram.DiagramImageListener;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.testsupporttool.ConsoleProcessListener;
import de.creditreform.crefoteam.cte.testsupporttool.process.CteAutomatedTestProcess;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Level;

/**
 * Pendant zum {@code ActivitiProcessController} aus {@code testsupport_client},
 * aber ohne Activiti-Server — der Prozess läuft direkt in der
 * {@code testsupport-statemachine}-Library.
 *
 * <p>Kombiniert {@link ConsoleProcessListener} mit
 * {@link DiagramImageListener}, sodass pro Step-Start ein PNG gerendert und
 * via {@link TesunClientJobListener#notifyClientJob(Level, Object)} als
 * {@code InputStream} an die GUI weitergereicht wird (1:1-Verhalten zum
 * Activiti-Weg).
 *
 * <p>Keine {@code prepareStart}-Startbedingungen wie im Original (das Tool
 * prüft nicht gegen einen echten Server).
 */
public final class ProcessController {

    private final TesunClientJobListener listener;
    private final AtomicReference<ProcessContext> currentCtx = new AtomicReference<>();

    public ProcessController(TesunClientJobListener listener) {
        this.listener = listener;
    }

    /**
     * Führt den CteAutomatedTestProcess synchron im aufrufenden Thread aus.
     * Bei Abbruch via {@link #stop()} kehrt die Engine mit
     * {@link ProcessOutcome#ABORTED} zurück.
     */
    public ProcessOutcome runProcess(EnvironmentConfig env, Map<String, Object> taskVariablesMap) throws Exception {
        CteAutomatedTestProcess.Assembly assembly = CteAutomatedTestProcess.build(env, listener);
        ProcessListener engineListener = buildEngineListener(assembly);
        ProcessContext ctx = ProcessContext.create(taskVariablesMap, engineListener);
        currentCtx.set(ctx);
        try {
            return new ProcessEngine().run(assembly.definition(), ctx);
        } finally {
            currentCtx.set(null);
        }
    }

    /** Fordert Abbruch an — der aktuell laufende Prozess bricht am nächsten Step ab. */
    public void stop() {
        ProcessContext ctx = currentCtx.get();
        if (ctx != null) {
            ctx.cancel();
        }
    }

    private ProcessListener buildEngineListener(CteAutomatedTestProcess.Assembly assembly) {
        ConsoleProcessListener consoleListener = new ConsoleProcessListener();
        try {
            DiagramImageListener images = DiagramImageListener.builder()
                    .template("CteAutomatedTestProcess",
                            "CteAutomatedTestProcess.jpg", "CteAutomatedTestProcess.bpmn")
                    .template("CteAutomatedTestProcessSUB",
                            "CteAutomatedTestProcessSUB.jpg", "CteAutomatedTestProcessSUB.bpmn")
                    .bind(assembly.phase1(), "CallActivityRepeatableTestAutomationProcess2SUB1")
                    .bind(assembly.phase2(), "CallActivityRepeatableTestAutomationProcess2SUB2")
                    .onImage(png -> listener.notifyClientJob(Level.INFO, new ByteArrayInputStream(png)))
                    .forProcess(assembly.definition());
            return ProcessListener.compose(consoleListener, images);
        } catch (Exception ex) {
            listener.notifyClientJob(Level.WARN, "Prozess-Diagramm deaktiviert (Template nicht ladbar): " + ex.getMessage());
            return consoleListener;
        }
    }
}
