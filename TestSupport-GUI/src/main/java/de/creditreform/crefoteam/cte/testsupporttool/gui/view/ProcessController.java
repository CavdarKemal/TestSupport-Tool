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
import de.creditreform.crefoteam.cte.testsupporttool.process.PhaseTrackingListener;
import de.creditreform.crefoteam.cte.testsupporttool.resume.ResumeMarker;
import de.creditreform.crefoteam.cte.testsupporttool.resume.ResumeState;
import de.creditreform.crefoteam.cte.testsupporttool.resume.ResumeStateWriter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Level;

/**
 * Pendant zum {@code ActivitiProcessController} aus {@code testsupport_client},
 * aber ohne Activiti-Server — der Prozess läuft direkt in der
 * {@code testsupport-statemachine}-Library.
 *
 * <p>Kombiniert {@link ConsoleProcessListener} + {@link DiagramImageListener}
 * + {@link ResumeStateWriter} in einer Listener-Kette:
 * <ul>
 *   <li>Pro Step-Start geht ein PNG via {@code notifyClientJob} an die GUI.</li>
 *   <li>Pro Step-Start wird der aktuelle Index-Pfad in
 *       {@code <testOutputsRoot>/resume.properties} persistiert — bei
 *       Abbruch bleibt die Datei stehen und kann beim naechsten Start
 *       fuer "Fortsetzen?" genutzt werden.</li>
 * </ul>
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

    /** Siehe {@link #runProcess(EnvironmentConfig, Map, int[])} — Kurzform ohne Resume. */
    public ProcessOutcome runProcess(EnvironmentConfig env, Map<String, Object> taskVariablesMap) throws Exception {
        return runProcess(env, taskVariablesMap, null);
    }

    /**
     * Führt den CteAutomatedTestProcess synchron im aufrufenden Thread aus.
     * Bei Abbruch via {@link #stop()} kehrt die Engine mit
     * {@link ProcessOutcome#ABORTED} zurück.
     *
     * <p>Wenn {@code resumeIndexPath != null && length > 0}, setzt er den
     * Marker in die Task-Variables-Map, sodass die Handler-Basisklasse
     * alle Steps vor diesem Pfad ueberspringt. Ab dem Resume-Step laeuft
     * alles regulaer.
     */
    public ProcessOutcome runProcess(EnvironmentConfig env,
                                     Map<String, Object> taskVariablesMap,
                                     int[] resumeIndexPath) throws Exception {
        if (resumeIndexPath != null && resumeIndexPath.length > 0) {
            taskVariablesMap.put(ResumeMarker.RESUME_INDEX_PATH, resumeIndexPath);
        }
        File resumeFile = resolveResumeFile(env);
        CteAutomatedTestProcess.Assembly assembly = CteAutomatedTestProcess.build(env, listener);
        ProcessListener engineListener = buildEngineListener(assembly, resumeFile);
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

    private static File resolveResumeFile(EnvironmentConfig env) {
        try {
            return new File(env.getTestOutputsRoot(), ResumeState.FILE_NAME);
        } catch (Exception ex) {
            return null;
        }
    }

    private ProcessListener buildEngineListener(CteAutomatedTestProcess.Assembly assembly, File resumeFile) {
        ConsoleProcessListener consoleListener = new ConsoleProcessListener();
        // PhaseTracking muss VOR allem anderen laufen, damit die TEST_PHASE-
        // Variable korrekt gesetzt ist, bevor der erste Handler sie fuer
        // buildNotifyStringForClassName() liest oder der ResumeStateWriter
        // sie fuer den Snapshot persistiert.
        PhaseTrackingListener phaseTracker = new PhaseTrackingListener(assembly.phase1(), assembly.phase2());
        ProcessListener resumeWriter = resumeFile != null ? new ResumeStateWriter(resumeFile) : null;
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
            return resumeWriter != null
                    ? ProcessListener.compose(phaseTracker, consoleListener, images, resumeWriter)
                    : ProcessListener.compose(phaseTracker, consoleListener, images);
        } catch (Exception ex) {
            listener.notifyClientJob(Level.WARN, "Prozess-Diagramm deaktiviert (Template nicht ladbar): " + ex.getMessage());
            return resumeWriter != null
                    ? ProcessListener.compose(phaseTracker, consoleListener, resumeWriter)
                    : ProcessListener.compose(phaseTracker, consoleListener);
        }
    }
}
