package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.tesun.util.TestResults;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.io.TempDir;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class TestResultDiffsViewTest {

    @Test
    void setDiffenrenceInfo_loadsXmlContentsAndPaths(@TempDir Path tmp) throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        File src = new File(tmp.toFile(), "src.xml");
        File dst = new File(tmp.toFile(), "dst.xml");
        File diff = new File(tmp.toFile(), "diff.xml");
        FileUtils.writeStringToFile(src, "<root>src-content</root>");
        FileUtils.writeStringToFile(dst, "<root>dst-content</root>");
        FileUtils.writeStringToFile(diff, "empty");

        TestResultDiffsView view = new TestResultDiffsView();
        view.setDiffenrenceInfo(new TestResults.DiffenrenceInfo("p001", src, dst, diff));

        assertThat(view.getTextAreaFileSrc().getText()).contains("src-content");
        assertThat(view.getTextAreaFileDst().getText()).contains("dst-content");
        assertThat(view.getTextFieldDiffFilePath().getText()).contains("diff.xml");
        assertThat(view.getLabelScfFilePath().getText()).contains("src.xml");
        assertThat(view.getLabelDstFilePath().getText()).contains("dst.xml");
    }

    @Test
    void constructor_buildsCleanView() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestResultDiffsView view = new TestResultDiffsView();
        assertThat(view.getTextAreaFileSrc().getText()).isEmpty();
        assertThat(view.getTextAreaFileDst().getText()).isEmpty();
    }
}
