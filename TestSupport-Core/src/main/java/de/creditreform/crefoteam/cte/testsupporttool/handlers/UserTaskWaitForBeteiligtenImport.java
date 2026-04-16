package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskWaiter;

/**
 * Wartet, bis der vom {@link UserTaskStartBeteiligtenImport} angestossene
 * Sub-Prozess {@code BETEILIGUNGEN_IMPORT} fertig ist.
 *
 * <p>Literal-Port aus {@code testsupport_client.tesun_activiti.handlers.UserTaskWaitForBeteiligtenImport}.
 */
public class UserTaskWaitForBeteiligtenImport extends AbstractUserTaskWaiter {

    public UserTaskWaitForBeteiligtenImport(EnvironmentConfig environmentConfig, TesunClientJobListener listener) {
        super("BETEILIGUNGEN_IMPORT",
                TestSupportClientKonstanten.BTLG_IMPORT_STARTET_AT,
                environmentConfig, listener);
    }
}
