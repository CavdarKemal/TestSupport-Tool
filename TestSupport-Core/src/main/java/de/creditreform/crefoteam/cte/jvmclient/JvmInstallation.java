package de.creditreform.crefoteam.cte.jvmclient;

import java.util.ArrayList;
import java.util.List;

/** Minimal-Port aus {@code batch_jvm_client.jvm_client.domain}. */
public final class JvmInstallation {

    private String jvmName;
    private String jvmUrl;
    private boolean activated;
    private List<JvmJobInfo> jvmJobInfosList = new ArrayList<>();

    public String getJvmName() { return jvmName; }
    public void setJvmName(String jvmName) { this.jvmName = jvmName; }

    public String getJvmUrl() { return jvmUrl; }
    public void setJvmUrl(String jvmUrl) { this.jvmUrl = jvmUrl; }

    public boolean isActivated() { return activated; }
    public void setActivated(boolean activated) { this.activated = activated; }

    public List<JvmJobInfo> getJvmJobInfosList() { return jvmJobInfosList; }
    public void setJvmJobInfosList(List<JvmJobInfo> jvmJobInfosList) { this.jvmJobInfosList = jvmJobInfosList; }
}
