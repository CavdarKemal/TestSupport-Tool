package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.model;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.model.AbstractMutableTreeNode;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipEntryInfo;

import java.util.List;

public class ZipEntryInfoTreeNode extends AbstractMutableTreeNode {

    public ZipEntryInfoTreeNode(IZipEntryInfo zipEntryInfo) {
        super(zipEntryInfo);
        publishNode();
    }

    private void publishNode() {
        IZipEntryInfo zipEntryInfo = (IZipEntryInfo) getUserObject();
        String zipEntryName = zipEntryInfo.getZipEntryName();
        String crefonummer = zipEntryInfo.getCrefonummer();
        List<String> matchesList = zipEntryInfo.getMatchesList();
/* ?????
    for( String match : matchesList )
    {
      System.out.println( "\t\tMatfch: " + match );
    }
*/
    }

    @Override
    public int compareTo(Object refObj) {
        IZipEntryInfo zipEntryInfo = (IZipEntryInfo) getUserObject();
        zipEntryInfo.getCrefonummer().compareTo(((IZipEntryInfo) refObj).getCrefonummer());
        return 0;
    }

    @Override
    public String toString() {
        IZipEntryInfo zipEntryInfo = (IZipEntryInfo) getUserObject();
        return zipEntryInfo.getCrefonummer();
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
