package de.creditreform.crefoteam.cte.rest;

import java.util.Objects;

/**
 * Verbindungs-DTO für einen REST-Service-Endpoint. Spike-Äquivalent zur
 * gleichnamigen Klasse aus der externen {@code restinvoker}-Library.
 * Enthält nur die Felder, die {@code EnvironmentConfig} setzt und liest.
 */
public final class RestInvokerConfig {

    private final String serviceURI;
    private final String serviceUser;
    private final String servicePassword;

    public RestInvokerConfig(String serviceURI, String serviceUser, String servicePassword) {
        this.serviceURI = Objects.requireNonNull(serviceURI, "serviceURI");
        this.serviceUser = serviceUser == null ? "" : serviceUser;
        this.servicePassword = servicePassword == null ? "" : servicePassword;
    }

    public String getServiceURI() { return serviceURI; }
    public String getServiceUser() { return serviceUser; }
    public String getServicePassword() { return servicePassword; }

    @Override
    public String toString() {
        return "RestInvokerConfig[" + serviceURI + "]";
    }
}
