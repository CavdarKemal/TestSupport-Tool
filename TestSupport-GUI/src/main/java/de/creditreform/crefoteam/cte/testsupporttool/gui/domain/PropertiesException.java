package de.creditreform.crefoteam.cte.testsupporttool.gui.domain;

/**
 * 1:1-Port aus {@code testsupport_client.tesun.gui.domain.PropertiesException}.
 *
 * <p>Bewusst getrennt von {@code de.creditreform.crefoteam.cte.tesun.util.PropertiesException}
 * im Core — das Original hatte ebenfalls beide parallel.
 */
public class PropertiesException extends Exception {
    /**
     * Use serialVersionUID for interoperability.
     */
    private final static long serialVersionUID = 2868255910742924901L;

    public PropertiesException(String exceptionMessage, Throwable throwable) {
        super(exceptionMessage, throwable);
    }

    public PropertiesException(String exceptionMessage) {
        this(exceptionMessage, null);
    }


}
