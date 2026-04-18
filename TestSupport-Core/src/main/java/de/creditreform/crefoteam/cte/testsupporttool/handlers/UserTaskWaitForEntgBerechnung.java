package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractUserTaskWaiter;

/**
 * Wartet, bis der vom {@link UserTaskStartEntgBerechnung} angestossene
 * Sub-Prozess {@code ENTSCHEIDUNGSTRAEGER_BERECHNUNG} fertig ist.
 *
 * <p>Literal-Port aus {@code testsupport_client.tesun_activiti.handlers.UserTaskWaitForEntgBerechnung}.
 */
public class UserTaskWaitForEntgBerechnung extends AbstractUserTaskWaiter {

    public UserTaskWaitForEntgBerechnung(EnvironmentConfig environmentConfig, TesunClientJobListener listener) {
        super("ENTSCHEIDUNGSTRAEGER_BERECHNUNG", TestSupportClientKonstanten.ENTG_BERECHNUNG_STARTET_AT, environmentConfig, listener);
    }
}
