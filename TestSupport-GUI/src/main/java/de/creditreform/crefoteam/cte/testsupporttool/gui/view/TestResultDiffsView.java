package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.design.TestResultDiffsPanel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.tesun.util.TestResults;
import de.creditreform.crefoteam.cte.tesun.util.TesunUtilites;
import org.apache.commons.io.FileUtils;

import java.io.IOException;

public class TestResultDiffsView extends TestResultDiffsPanel {

    TestResults.DiffenrenceInfo diffenrenceInfo;

    public TestResultDiffsView() {
        super();
    }

    public void setDiffenrenceInfo(TestResults.DiffenrenceInfo diffenrenceInfo) {
        this.diffenrenceInfo = diffenrenceInfo;
        String diffFileAbsolutePath = TesunUtilites.shortPath(diffenrenceInfo.getDiffFile().getAbsolutePath(), 40);
        String srcXmlFileAbsolutePath = TesunUtilites.shortPath(diffenrenceInfo.getXmlFileSrc().getAbsolutePath(), 40);
        String dstXmlFileAbsolutePath = TesunUtilites.shortPath(diffenrenceInfo.getXmlFileDst().getAbsolutePath(), 40);
        getTextFieldDiffFilePath().setText(diffFileAbsolutePath);
        try {
            getLabelScfFilePath().setText(srcXmlFileAbsolutePath);
            String fileToString = FileUtils.readFileToString(diffenrenceInfo.getXmlFileSrc());
            getTextAreaFileSrc().setText(fileToString);
        } catch (IOException ex) {
            GUIStaticUtils.showExceptionMessage(this, "Fehler beim Laden der Datei\n\t" + srcXmlFileAbsolutePath, ex);
        }
        try {
            getLabelDstFilePath().setText(dstXmlFileAbsolutePath);
            String fileToString = FileUtils.readFileToString(diffenrenceInfo.getXmlFileDst());
            getTextAreaFileDst().setText(fileToString);
        } catch (IOException ex) {
            GUIStaticUtils.showExceptionMessage(this, "Fehler beim Laden der Datei\n\t" + dstXmlFileAbsolutePath, ex);
        }
    }

}
