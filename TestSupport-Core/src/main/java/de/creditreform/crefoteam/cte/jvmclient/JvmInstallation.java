package de.creditreform.crefoteam.cte.jvmclient;

/** Minimal-Port aus {@code batch_jvm_client.jvm_client.domain}. */
public final class JvmInstallation {

    private String jvmName;
    private String jvmUrl;

    public String getJvmName() { return jvmName; }
    public void setJvmName(String jvmName) { this.jvmName = jvmName; }

    public String getJvmUrl() { return jvmUrl; }
    public void setJvmUrl(String jvmUrl) { this.jvmUrl = jvmUrl; }
}
