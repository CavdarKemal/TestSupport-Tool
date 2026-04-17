package de.creditreform.crefoteam.cte.tesun.rest.dto;

public class TesunSystemInfo {
    private final String cteVersion;

    public TesunSystemInfo(String cteVersion) {
        this.cteVersion = cteVersion;
    }

    public String getCteVersion() { return cteVersion; }
}
