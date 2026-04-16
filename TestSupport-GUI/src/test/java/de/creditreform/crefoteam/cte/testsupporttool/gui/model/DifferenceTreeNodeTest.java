package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import de.creditreform.crefoteam.cte.tesun.util.TestResults;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class DifferenceTreeNodeTest {

    private static TestResults.DiffenrenceInfo buildInfo(String testfall) {
        return new TestResults.DiffenrenceInfo(testfall,
                new File("src.xml"), new File("dst.xml"), new File("diff.xml"));
    }

    @Test
    void getDiffenrenceInfo_returnsStoredUserObject() {
        TestResults.DiffenrenceInfo info = buildInfo("p001");
        DifferenceTreeNode node = new DifferenceTreeNode(info);
        assertThat(node.getDiffenrenceInfo()).isSameAs(info);
    }

    @Test
    void toString_formatsWithTestfallPrefix() {
        DifferenceTreeNode node = new DifferenceTreeNode(buildInfo("p042_19"));
        assertThat(node.toString()).isEqualTo("Testfall: p042_19");
    }

    @Test
    void activatedFlag_alwaysFalse() {
        DifferenceTreeNode node = new DifferenceTreeNode(buildInfo("x"));
        node.setActivated(true);
        assertThat(node.isActivated()).isFalse();
    }

    @Test
    void compareTo_returnsZero() {
        DifferenceTreeNode a = new DifferenceTreeNode(buildInfo("a"));
        DifferenceTreeNode b = new DifferenceTreeNode(buildInfo("b"));
        assertThat(a.compareTo(b)).isZero();
    }
}
