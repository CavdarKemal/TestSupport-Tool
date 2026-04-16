package de.creditreform.crefoteam.cte.tesun.rest.dto;

import java.util.ArrayList;
import java.util.List;

/** DTO-Pendant aus {@code restservices.tesun.xmlbinding.environmentproperties}. */
public final class CteEnvironmentProperties {

    private final List<CteEnvironmentPropertiesTupel> properties = new ArrayList<>();

    public List<CteEnvironmentPropertiesTupel> getProperties() { return properties; }
}
