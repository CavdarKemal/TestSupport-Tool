package de.creditreform.crefoteam.cte.testsupporttool.config;

/**
 * Variablen-Schlüssel für den {@link de.creditreform.crefoteam.cte.statemachine.ProcessContext}.
 * Entspricht den {@code TesunClientJobListener.UT_TASK_PARAM_*}-Konstanten
 * im bestehenden Projekt — bewusst auf das reduziert, was der Spike nutzt.
 */
public final class TestSupportConstants {

    private TestSupportConstants() { }

    /** Test-Phase (PHASE_1 / PHASE_2). */
    public static final String VAR_TEST_PHASE = "TEST_PHASE";

    /** Test-Typ (z. B. PHASE1_AND_PHASE2 / PHASE1_ONLY). Steuert das Gateway. */
    public static final String VAR_TEST_TYPE = "TEST_TYPE";

    /** {@code true} = Aktionen werden nur simuliert, kein tatsächlicher Aufruf. */
    public static final String VAR_DEMO_MODE = "DEMO_MODE";

    /** Identifier des CT-Imports (Polling-Ziel). */
    public static final String CT_IMPORT_PROCESS = "FROM_STAGING_INTO_CTE";

    /** Wert für {@link #VAR_TEST_TYPE}, der den Vollablauf triggert. */
    public static final String TEST_TYPE_PHASE1_AND_PHASE2 = "PHASE1_AND_PHASE2";

    /** REST-Job-Status, der eine erfolgreiche Beendigung signalisiert. */
    public static final String JOB_STATUS_COMPLETED = "COMPLETED";
}
