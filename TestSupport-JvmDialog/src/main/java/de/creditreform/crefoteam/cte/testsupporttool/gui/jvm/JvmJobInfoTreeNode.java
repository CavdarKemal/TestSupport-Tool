package de.creditreform.crefoteam.cte.testsupporttool.gui.jvm;

import de.creditreform.crefoteam.cte.jvmclient.JvmJobInfo;
import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.AbstractMutableTreeNode;

public class JvmJobInfoTreeNode extends AbstractMutableTreeNode {
    public JvmJobInfoTreeNode(JvmJobInfo jvmJobInfo) {
        super(jvmJobInfo);
    }

    @Override
    public String toString() {
        if (getUserObject() == null) {
            return "Executions-Liste wird geladen...";
        }
        JvmJobInfo jvmJobInfo = (JvmJobInfo) getUserObject();
        return jvmJobInfo.getJobName();
    }

    @Override
    public int compareTo(Object o) {
        JvmJobInfo theObj = (JvmJobInfo) o;
        JvmJobInfo myObj = (JvmJobInfo) getUserObject();
        return theObj.getJobName().compareTo(myObj.getJobName());
    }

    @Override
    public void setActivated(boolean activated) {
        JvmJobInfo jvmJobInfo = (JvmJobInfo) getUserObject();
        // TODO
    }

    @Override
    public boolean isActivated() {
        JvmJobInfo jvmJobInfo = (JvmJobInfo) getUserObject();
        // TODO
        return true;
    }
}
