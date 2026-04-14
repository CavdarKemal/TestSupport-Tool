package de.creditreform.crefoteam.cte.testsupporttool.handlers.base;

import java.util.Map;

/**
 * Interface-Port von {@code testsupport_client.tesun_activiti.handlers.UserTaskRunnable}.
 * Neu-Implementierungen nutzen direkt {@link de.creditreform.crefoteam.cte.statemachine.Step};
 * dieses Interface existiert, damit die ~32 portierten Handler der ursprünglichen
 * Signatur entsprechen.
 */
public interface UserTaskRunnable {
    Map<String, Object> runTask(Map<String, Object> taskVariablesMap) throws Exception;

    void cancel();
}
