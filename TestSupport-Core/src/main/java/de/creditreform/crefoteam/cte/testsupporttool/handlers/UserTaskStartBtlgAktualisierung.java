package de.creditreform.crefoteam.cte.testsupporttool.handlers;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.testsupporttool.handlers.base.AbstractJvmJobStarter;

/** Literal-Port aus {@code testsupport_client.tesun_activiti.handlers}. */
public class UserTaskStartBtlgAktualisierung extends AbstractJvmJobStarter {
    public UserTaskStartBtlgAktualisierung(EnvironmentConfig environmentConfig, TesunClientJobListener listener) throws PropertiesException {
        super(environmentConfig.getJobInfoForBtlgAktualisierung(),
                TestSupportClientKonstanten.BTLG_UPDATE_TRIGGER_STARTET_AT,
                environmentConfig, listener);
    }
}
