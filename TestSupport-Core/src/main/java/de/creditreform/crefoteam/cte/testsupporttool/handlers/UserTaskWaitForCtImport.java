package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskWaiter;

/**
 * Wartet, bis der vom {@link UserTaskStartCtImport} angestossene
 * Sub-Prozess {@code FROM_STAGING_INTO_CTE} fertig ist.
 *
 * <p>Literal-Port aus {@code testsupport_client.tesun_activiti.handlers.UserTaskWaitForCtImport}.
 */
public class UserTaskWaitForCtImport extends AbstractUserTaskWaiter {

    public UserTaskWaitForCtImport(EnvironmentConfig environmentConfig, TesunClientJobListener listener) {
        super("FROM_STAGING_INTO_CTE",
                TestSupportClientKonstanten.CT_IMPORT_STARTET_AT,
                environmentConfig, listener);
    }
}
