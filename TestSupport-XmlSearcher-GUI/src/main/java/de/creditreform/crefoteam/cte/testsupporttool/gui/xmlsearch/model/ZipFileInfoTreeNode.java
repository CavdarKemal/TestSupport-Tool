package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.model;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.AbstractMutableTreeNode;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipEntryInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipFileInfo;

import java.util.List;

public class ZipFileInfoTreeNode extends AbstractMutableTreeNode {

    public ZipFileInfoTreeNode(IZipFileInfo zipFileInfo) {
        super(zipFileInfo);
        publishNode();
    }

    private void publishNode() {
        IZipFileInfo zipFileInfo = (IZipFileInfo) getUserObject();
        List<? extends IZipEntryInfo> zipEntryInfoList = zipFileInfo.getZipEntryInfoList();
        for (IZipEntryInfo zipEntryInfo : zipEntryInfoList) {
            if (zipEntryInfo.getResultFileName() != null) {
                ZipEntryInfoTreeNode zipEntryInfoTreeNode = new ZipEntryInfoTreeNode(zipEntryInfo);
                insert(zipEntryInfoTreeNode, getChildCount());
            }
        }
    }

    @Override
    public int compareTo(Object refObj) {
        IZipFileInfo zipFileInfo = (IZipFileInfo) getUserObject();
        String name = zipFileInfo.getZipFileName();
        name.compareTo(((IZipFileInfo) refObj).getZipFileName());
        return 0;
    }

    @Override
    public String toString() {
        IZipFileInfo zipFileInfo = (IZipFileInfo) getUserObject();
        String nodeInfo = String.format("%s {%d}", zipFileInfo.getZipFilePath(), zipFileInfo.getZipEntryInfoList().size());
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
