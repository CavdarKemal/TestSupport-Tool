package de.creditreform.crefoteam.cte.testsupporttool.process;

import de.creditreform.crefoteam.cte.statemachine.ProcessDefinition;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskStartBeteiligtenImport;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskStartBtlgAktualisierung;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskStartCollect;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskStartCtImport;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskStartEntgBerechnung;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskStartExports;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskStartRestore;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskStartSftpUploads;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskStartUploads;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskWaitForBeteiligtenImport;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.UserTaskWaitForCtImport;

/**
 * State-Machine-Factory für den Sub-Prozess. Spiegelt den Original-BPMN
 * {@code CteAutomatedTestProcessSUB.bpmn}:
 *
 * <pre>
 *   Start → StartUploads → StartBeteiligtenImport → WaitForBeteiligtenImport
 *         → StartEntgBerechnung → StartBtlgAktualisierung
 *         → StartCtImport → WaitForCtImport
 *         → StartExports → StartCollect → StartRestore → StartSftpUploads → End
 * </pre>
 *
 * <p>Wait-Pendants zu den uebrigen Start-Handlern werden schrittweise
 * portiert (siehe Original {@code activiti-tests/UserTaskStart*.bpmn} —
 * dort folgt jedem Start ein Wait).
 */
public final class CteAutomatedTestProcessSUB {

    private CteAutomatedTestProcessSUB() { }

    public static ProcessDefinition build(EnvironmentConfig env, TesunClientJobListener listener) throws PropertiesException {
        return ProcessDefinition.builder("CteAutomatedTestProcessSUB")
                .step(new UserTaskStartUploads(env, listener))
                .step(new UserTaskStartBeteiligtenImport(env, listener))
                .step(new UserTaskWaitForBeteiligtenImport(env, listener))
                .step(new UserTaskStartEntgBerechnung(env, listener))
                .step(new UserTaskStartBtlgAktualisierung(env, listener))
                .step(new UserTaskStartCtImport(env, listener))
                .step(new UserTaskWaitForCtImport(env, listener))
                .step(new UserTaskStartExports(env, listener))
                .step(new UserTaskStartCollect(env, listener))
                .step(new UserTaskStartRestore(env, listener))
                .step(new UserTaskStartSftpUploads(env, listener))
                .build();
    }
}
