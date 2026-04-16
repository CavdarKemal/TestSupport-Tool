package de.creditreform.crefoteam.cte.testsupporttool.resume;

/**
 * Keys, unter denen Resume-Steuer-Variablen im {@code ProcessContext}
 * abgelegt werden. Tool-intern — im {@code TesunClientJobListener}-Interface
 * bewusst nicht sichtbar, weil Handler sie nur ueber die Basisklasse
 * {@code AbstractUserTaskRunnable} verwenden sollen.
 */
public final class ResumeMarker {

    /** Wert: {@code int[]} — der Index-Pfad, an dem der Resume starten soll. */
    public static final String RESUME_INDEX_PATH = "__RESUME_INDEX_PATH__";

    /**
     * Wert: {@code Boolean} — wird auf {@code true} gesetzt, sobald der
     * Resume-Step erreicht ist. Ab dort werden alle folgenden Steps wieder
     * regulaer ausgefuehrt.
     */
    public static final String RESUME_REACHED = "__RESUME_REACHED__";

    private ResumeMarker() { }
}
