package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskWaiter;

/**
 * Wartet, bis der vom {@link UserTaskStartBtlgAktualisierung} angestossene
 * Sub-Prozess {@code BTLG_UPDATE_TRIGGER} fertig ist.
 *
 * <p>Literal-Port aus {@code testsupport_client.tesun_activiti.handlers.UserTaskWaitForBtlgAktualisierung}.
 */
public class UserTaskWaitForBtlgAktualisierung extends AbstractUserTaskWaiter {

    public UserTaskWaitForBtlgAktualisierung(EnvironmentConfig environmentConfig, TesunClientJobListener listener) {
        super("BTLG_UPDATE_TRIGGER",
                TestSupportClientKonstanten.BTLG_UPDATE_TRIGGER_STARTET_AT,
                environmentConfig, listener);
    }
}
