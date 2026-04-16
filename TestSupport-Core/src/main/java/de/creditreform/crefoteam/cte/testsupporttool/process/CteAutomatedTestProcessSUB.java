package de.creditreform.crefoteam.cte.testsupporttool.process;

import de.creditreform.crefoteam.cte.statemachine.ProcessDefinition;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskCheckCollects;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskCheckExportProtokoll;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskCheckRefExports;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskCheckSftpUploads;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskStartBeteiligtenImport;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskStartBtlgAktualisierung;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskStartCollect;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskStartCtImport;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskStartEntgBerechnung;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskStartExports;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskStartRestore;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskStartSftpUploads;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskStartUploads;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskWaitBeforBeteiligtenImport;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskWaitBeforeCtImport;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskWaitBeforeExport;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskWaitForBeteiligtenImport;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskWaitForBtlgAktualisierung;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskWaitForCtImport;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskWaitForEntgBerechnung;

/**
 * State-Machine-Factory für den Sub-Prozess. Spiegelt den Original-BPMN
 * {@code CteAutomatedTestProcessSUB.bpmn} (20 Steps, exakte BPMN-Reihenfolge):
 *
 * <pre>
 *   Start → StartUploads
 *         → WaitBeforBeteiligtenImport → StartBeteiligtenImport → WaitForBeteiligtenImport
 *         → StartEntgBerechnung    → WaitForEntgBerechnung
 *         → StartBtlgAktualisierung → WaitForBtlgAktualisierung
 *         → WaitBeforeCtImport → StartCtImport → WaitForCtImport
 *         → WaitBeforeExport → StartExports
 *         → StartCollect → CheckCollects
 *         → StartRestore → CheckRefExports
 *         → CheckExportProtokoll
 *         → StartSftpUploads → CheckSftpUploads → End
 * </pre>
 *
 * <p>Klassenname-Tippfehler "WaitBefor" (statt "WaitBefore") stammt aus dem
 * Original-BPMN und wird 1:1 übernommen.
 */
public final class CteAutomatedTestProcessSUB {

    private CteAutomatedTestProcessSUB() { }

    public static ProcessDefinition build(EnvironmentConfig env, TesunClientJobListener listener) throws PropertiesException {
        return ProcessDefinition.builder("CteAutomatedTestProcessSUB")
                .step(new UserTaskStartUploads(env, listener))
                .step(new UserTaskWaitBeforBeteiligtenImport(env, listener))
                .step(new UserTaskStartBeteiligtenImport(env, listener))
                .step(new UserTaskWaitForBeteiligtenImport(env, listener))
                .step(new UserTaskStartEntgBerechnung(env, listener))
                .step(new UserTaskWaitForEntgBerechnung(env, listener))
                .step(new UserTaskStartBtlgAktualisierung(env, listener))
                .step(new UserTaskWaitForBtlgAktualisierung(env, listener))
                .step(new UserTaskWaitBeforeCtImport(env, listener))
                .step(new UserTaskStartCtImport(env, listener))
                .step(new UserTaskWaitForCtImport(env, listener))
                .step(new UserTaskWaitBeforeExport(env, listener))
                .step(new UserTaskStartExports(env, listener))
                .step(new UserTaskStartCollect(env, listener))
                .step(new UserTaskCheckCollects(env, listener))
                .step(new UserTaskStartRestore(env, listener))
                .step(new UserTaskCheckRefExports(env, listener))
                .step(new UserTaskCheckExportProtokoll(env, listener))
                .step(new UserTaskStartSftpUploads(env, listener))
                .step(new UserTaskCheckSftpUploads(env, listener))
                .build();
    }
}
