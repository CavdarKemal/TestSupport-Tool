package de.creditreform.crefoteam.cte.testsupporttool.resume;

import de.creditreform.crefoteam.cte.statemachine.ProcessContext;
import de.creditreform.crefoteam.cte.statemachine.ProcessListener;
import de.creditreform.crefoteam.cte.statemachine.Step;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;

import java.io.File;
import java.io.IOException;

/**
 * {@link ProcessListener}, der pro Step-Start den aktuellen Index-Pfad in
 * eine {@link ResumeState}-Datei schreibt. Bei erfolgreichem Prozess-Ende
 * wird die Datei geloescht; bei Abbruch bleibt sie stehen und dient dem
 * GUI-Dialog als "Fortsetzen?"-Grundlage.
 *
 * <p>Die Datei liegt unter
 * {@code <testOutputsRoot>/resume.properties} (umgebungs-spezifisch).
 */
public final class ResumeStateWriter implements ProcessListener {

    private final File resumeFile;

    public ResumeStateWriter(File resumeFile) {
        this.resumeFile = resumeFile;
    }

    @Override
    public void onStepStarted(ProcessContext context, Step step) {
        try {
            int[] indexPath = ResumePathUtil.computeIndexPath(context);
            if (indexPath.length == 0) return;
            Object phase = context.get(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE);
            String phaseStr = phase instanceof TestSupportClientKonstanten.TEST_PHASE
                    ? ((TestSupportClientKonstanten.TEST_PHASE) phase).name()
                    : (phase != null ? phase.toString() : null);
            new ResumeState(indexPath, step.name(), phaseStr).save(resumeFile);
        } catch (IOException ex) {
            TimelineLogger.warn(getClass(), "Resume-State konnte nicht geschrieben werden: {}", ex.getMessage());
        }
    }

    @Override
    public void onProcessCompleted(String processName) {
        ResumeState.delete(resumeFile);
    }

    // onProcessFailed/Aborted: Datei bewusst stehen lassen — GUI bietet dann
    // beim naechsten Start das Fortsetzen-Menue an.
}
