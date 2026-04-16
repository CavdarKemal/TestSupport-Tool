package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

/**
 * 1:1-Port aus {@code testsupport_client.tesun.gui.utils.CommandExecutorListener}.
 *
 * <p>Listener-Interface, ueber das langlaufende GUI-Operationen (Search,
 * Generate, Restore, ...) ihren Fortschritt an die UI melden.
 */
public interface CommandExecutorListener {
    void progress(String strInfo);
}
