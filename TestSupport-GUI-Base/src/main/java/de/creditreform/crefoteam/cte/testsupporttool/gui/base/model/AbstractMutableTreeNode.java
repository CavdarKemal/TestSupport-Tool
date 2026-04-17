package de.creditreform.crefoteam.cte.testsupporttool.gui.base.model;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * 1:1-Port aus {@code testsupport_client.tesun.gui.base.model.AbstractMutableTreeNode}.
 *
 * <p>Basisklasse fuer alle Tree-Knoten in der TestSupport-GUI:
 * {@link DefaultMutableTreeNode} + {@link Comparable} + Activated-Flag-API.
 */
public abstract class AbstractMutableTreeNode extends DefaultMutableTreeNode implements Comparable {
    /**
     * Use serialVersionUID for interoperability.
     */
    private static final long serialVersionUID = -5064380133239367664L;

    public AbstractMutableTreeNode(Object userObject) {
        this.setUserObject(userObject);
    }

    @Override
    public String toString() {
        return getUserObject().toString();
    }

    public Object[] getChildrensUserObjects() {
        List userObjectsList = new ArrayList();
        if (getChildCount() > 0) {
            Enumeration childList = children();
            while (childList.hasMoreElements()) {
                AbstractMutableTreeNode childNode = (AbstractMutableTreeNode) childList.nextElement();
                userObjectsList.add(childNode.getUserObject());
                Object[] childUserOPbjects = childNode.getChildrensUserObjects();
                Collections.addAll(userObjectsList, childUserOPbjects);
            }
            return userObjectsList.toArray(new Object[userObjectsList.size()]);
        }
        return new Object[]{};
    }

    public abstract void setActivated(boolean activated);

    public abstract boolean isActivated();
}
