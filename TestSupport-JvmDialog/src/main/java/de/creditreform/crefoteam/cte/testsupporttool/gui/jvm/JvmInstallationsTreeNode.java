package de.creditreform.crefoteam.cte.testsupporttool.gui.jvm;

import de.creditreform.crefoteam.cte.jvmclient.JvmInstallation;
import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.AbstractMutableTreeNode;

import java.util.Map;

public class JvmInstallationsTreeNode extends AbstractMutableTreeNode {
    public JvmInstallationsTreeNode(Map<String, JvmInstallation> jvmInstallationMap) {
        super(jvmInstallationMap);
    }

    @Override
    public void setActivated(boolean activated) {

    }

    @Override
    public boolean isActivated() {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
