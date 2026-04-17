package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.model;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipEntryInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipFileInfo;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipSearcResult;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsTreeModel extends DefaultTreeModel {
    List<IZipSearcResult> zipSearcResultList = new ArrayList<>();
    private boolean showEmptyNodes = true;

    public SearchResultsTreeModel() {
        super(new DefaultMutableTreeNode("Suchergebnisse"));
    }

    public List<IZipSearcResult> getZipSearcResultList() {
        return zipSearcResultList;
    }

    public void addNode(Object dataObject) {
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) super.getRoot();
        if (dataObject instanceof IZipSearcResult) {
            IZipSearcResult IZipSearcResult = (IZipSearcResult) dataObject;
            ZipSearcResultTreeNode zipSearcResultTreeNode = new ZipSearcResultTreeNode(IZipSearcResult);
            rootNode.add(zipSearcResultTreeNode);
            nodeStructureChanged(zipSearcResultTreeNode.getParent());
            zipSearcResultList.add(IZipSearcResult);
            // System.out.println( "ZipSearcResultTreeNode:" + zipSearcResultTreeNode );
        } else if (dataObject instanceof IZipFileInfo) {
            IZipFileInfo zipFileInfo = (IZipFileInfo) dataObject;
            ZipFileInfoTreeNode zipFileInfoTreeNode = new ZipFileInfoTreeNode(zipFileInfo);
            ZipSearcResultTreeNode parentNode = findZipSearcResultTreeNode(zipFileInfo);
            parentNode.add(zipFileInfoTreeNode);
            nodeStructureChanged(parentNode);
            // System.out.println( "\tZipFileInfoTreeNode:" + zipFileInfoTreeNode );
        } else if (dataObject instanceof IZipEntryInfo) {
            IZipEntryInfo zipEntryInfo = (IZipEntryInfo) dataObject;
            ZipEntryInfoTreeNode zipEntryInfoTreeNode = new ZipEntryInfoTreeNode(zipEntryInfo);
            ZipFileInfoTreeNode parentNode = findZipFileInfoTreeNode(zipEntryInfo);
            parentNode.add(zipEntryInfoTreeNode);
            nodeStructureChanged(parentNode);
            // System.out.println( "\t\tZipEntryInfoTreeNode:" + zipEntryInfoTreeNode );
        }
    }

    private ZipSearcResultTreeNode findZipSearcResultTreeNode(IZipFileInfo zipFileInfo) {
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) super.getRoot();
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            ZipSearcResultTreeNode zipSearcResultTreeNode = (ZipSearcResultTreeNode) rootNode.getChildAt(i);
            IZipSearcResult IZipSearcResult = (IZipSearcResult) zipSearcResultTreeNode.getUserObject();
            if (IZipSearcResult.equals(zipFileInfo.getParent())) {
                return zipSearcResultTreeNode;
            }
        }
        return null;
    }

    private ZipFileInfoTreeNode findZipFileInfoTreeNode(IZipEntryInfo zipEntryInfo) {
        IZipFileInfo parentZipFileInfo = zipEntryInfo.getParent();
        ZipSearcResultTreeNode zipSearcResultTreeNode = findZipSearcResultTreeNode(parentZipFileInfo);
        for (int j = 0; j < zipSearcResultTreeNode.getChildCount(); j++) {
            ZipFileInfoTreeNode zipFileInfoTreeNode = (ZipFileInfoTreeNode) zipSearcResultTreeNode.getChildAt(j);
            IZipFileInfo zipFileInfo = (IZipFileInfo) zipFileInfoTreeNode.getUserObject();
            if (zipFileInfo.equals(zipEntryInfo.getParent())) {
                return zipFileInfoTreeNode;
            }
        }
        return null;
    }

    public void setShowEmptyNodes(boolean is) {
        if (is == showEmptyNodes) {
            return;
        }
        showEmptyNodes = is;
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) super.getRoot();
        printNode("", rootNode);
    }

    public void printNode(String strPre, DefaultMutableTreeNode node) {
        int childCount = node.getChildCount();
        System.out.println(strPre + node);
        for (int i = 0; i < childCount; i++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
            if (childNode.getChildCount() > 0) {
                printNode(strPre + "\t", childNode);
            } else {
                System.out.println(strPre + "\t" + childNode);
            }
        }
    }
}
