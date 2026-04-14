package de.creditreform.crefoteam.cte.tesun.util;

/** Literal-Port aus {@code testsupport_client.tesun_util}. */
public class PropertiesException extends Exception {
    private static final long serialVersionUID = 2868255910742924901L;

    public PropertiesException(String exceptionMessage, Throwable throwable) {
        super(exceptionMessage, throwable);
    }

    public PropertiesException(String exceptionMessage) {
        this(exceptionMessage, null);
    }
}
