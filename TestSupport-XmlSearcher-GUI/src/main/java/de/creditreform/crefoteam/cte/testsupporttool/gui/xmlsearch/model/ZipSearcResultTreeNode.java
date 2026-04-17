package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.model;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.AbstractMutableTreeNode;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipFileInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipSearcResult;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;

public class ZipSearcResultTreeNode extends AbstractMutableTreeNode {

    public ZipSearcResultTreeNode(IZipSearcResult IZipSearcResult) {
        super(IZipSearcResult);
        publishNode();
    }

    private void publishNode() {
        IZipSearcResult IZipSearcResult = (IZipSearcResult) getUserObject();
        Map<Path, IZipFileInfo> zipFileInfoMap = IZipSearcResult.getZipFileInfoMap();
        Iterator<Path> iterator = zipFileInfoMap.keySet().iterator();
        while (iterator.hasNext()) {
            Path thewKey = iterator.next();
            IZipFileInfo IZipFileInfo = zipFileInfoMap.get(thewKey);
            ZipFileInfoTreeNode zipFileInfoTreeNode = new ZipFileInfoTreeNode(IZipFileInfo);
            insert(zipFileInfoTreeNode, getChildCount());
        }
    }

    @Override
    public int compareTo(Object refObj) {
        IZipSearcResult IZipSearcResult = (IZipSearcResult) getUserObject();
        IZipSearcResult.getSearchName().compareTo(((IZipSearcResult) refObj).getSearchName());
        return 0;
    }

    @Override
    public String toString() {
        IZipSearcResult IZipSearcResult = (IZipSearcResult) getUserObject();
        String nodeInfo = String.format("%s {%d}", IZipSearcResult.getSearchName(), IZipSearcResult.getZipFileInfoMap().size());
        return nodeInfo;
    }

    @Override
    public void setActivated(boolean activated) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isActivated() {
        // TODO Auto-generated method stub
        return false;
    }
}
