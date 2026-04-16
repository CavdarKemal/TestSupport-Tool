package de.creditreform.crefoteam.cte.tesun.rest.dto;

/** DTO-Pendant aus {@code restservices.tesun.xmlbinding.environmentproperties}. */
public final class CteEnvironmentPropertiesTupel {

    private String key;
    private String value;
    private boolean dbOverride;

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public boolean isDbOverride() { return dbOverride; }
    public void setDbOverride(boolean dbOverride) { this.dbOverride = dbOverride; }
}
