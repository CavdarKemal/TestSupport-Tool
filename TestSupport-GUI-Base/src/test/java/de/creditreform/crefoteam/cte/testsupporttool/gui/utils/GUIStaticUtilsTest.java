package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Container;
import java.awt.Frame;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GUIStaticUtilsTest {

    @Test
    void isEmpty_handlesNullAndBlankAndContent() {
        assertThat(GUIStaticUtils.isEmpty(null)).isTrue();
        assertThat(GUIStaticUtils.isEmpty("")).isTrue();
        assertThat(GUIStaticUtils.isEmpty("x")).isFalse();
    }

    @Test
    void isValue_treatsNullAndEmptyToStringAsNoValue() {
        assertThat(GUIStaticUtils.isValue(null)).isFalse();
        assertThat(GUIStaticUtils.isValue("")).isFalse();
        assertThat(GUIStaticUtils.isValue("x")).isTrue();
        assertThat(GUIStaticUtils.isValue(42)).isTrue();
    }

    @Test
    void parseBoolean_parsesValueOrReturnsNull() {
        assertThat(GUIStaticUtils.parseBoolean("true")).isTrue();
        assertThat(GUIStaticUtils.parseBoolean("false")).isFalse();
        assertThat(GUIStaticUtils.parseBoolean(null)).isNull();
        assertThat(GUIStaticUtils.parseBoolean("")).isNull();
    }

    @Test
    void parseInt_parsesValueOrReturnsNull() {
        assertThat(GUIStaticUtils.parseInt("42")).isEqualTo(42);
        assertThat(GUIStaticUtils.parseInt(null)).isNull();
        assertThat(GUIStaticUtils.parseInt("")).isNull();
    }

    @Test
    void parseLong_parsesValueOrReturnsNull() {
        assertThat(GUIStaticUtils.parseLong("100000000000")).isEqualTo(100000000000L);
        assertThat(GUIStaticUtils.parseLong(null)).isNull();
        assertThat(GUIStaticUtils.parseLong("")).isNull();
    }

    @Test
    void concatenate_mergesTwoArraysInOrderAndKeepsType() {
        String[] a = {"a", "b"};
        String[] b = {"c", "d", "e"};

        String[] merged = GUIStaticUtils.concatenate(a, b);
        assertThat(merged).containsExactly("a", "b", "c", "d", "e");
    }

    @Test
    void findFirstFile_returnsAbsolutePathOrEmpty(@TempDir Path tmp) throws Exception {
        Files.write(tmp.resolve("alpha.txt"), new byte[]{1});
        Files.write(tmp.resolve("beta.xml"), new byte[]{2});

        String found = GUIStaticUtils.findFirstFile(tmp.toString(), ".txt");
        assertThat(found).endsWith("alpha.txt");

        String notFound = GUIStaticUtils.findFirstFile(tmp.toString(), ".doesnotexist");
        assertThat(notFound).isEmpty();
    }

    @Test
    void getParentFrame_climbsUntilFrame_orReturnsNull() {
        JFrame frame = new JFrame();
        try {
            JPanel inner = new JPanel();
            JPanel outer = new JPanel();
            frame.add(outer);
            outer.add(inner);

            assertThat(GUIStaticUtils.getParentFrame(inner)).isSameAs(frame);
            assertThat(GUIStaticUtils.getParentFrame((Container) null)).isNull();
        } finally {
            frame.dispose();
        }
    }

    @Test
    void prepareBpmnFileForEnvironment_replacesEnvPlaceholder(@TempDir Path tmp) throws Exception {
        Path src = tmp.resolve("CteAutomatedTestProcess.bpmn");
        Path dst = tmp.resolve("ENE-CteAutomatedTestProcess.bpmn");
        Files.writeString(src, "<process id=\"%ENV%-Sample\" name=\"%ENV%\"/>");

        File written = GUIStaticUtils.prepareBpmnFileForEnvironment(src.toFile(), dst.toFile(), "ENE");

        assertThat(written).isEqualTo(dst.toFile());
        assertThat(Files.readString(dst)).isEqualTo("<process id=\"ENE-Sample\" name=\"ENE\"/>");
    }

    @Test
    void wildcardToRegex_acceptsMatchingFiles_andRejectsNonMatching() {
        // Inner-Class TheFileFilter: accept(File) ueber wildcardToRegex.
        GUIStaticUtils.TheFileFilter filter = new GUIStaticUtils.TheFileFilter("*.xml");

        // Datei-Existenz nicht relevant — accept() schaut auf Name-Pattern und isDirectory().
        // Wir koennen ein File-Objekt mit beliebigem Namen verwenden.
        assertThat(filter.accept(new File("foo.xml"))).isTrue();
        assertThat(filter.accept(new File("foo.txt"))).isFalse();
        assertThat(filter.getDescription()).isEqualTo("*.xml-Dateien");
    }

    @Test
    void claudeModeStubs_throwOrReturnNull() {
        assertThatThrownBy(() -> GUIStaticUtils.checkIfBpmnFileExists(null, "ENE", "x.bpmn", false))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("CLAUDE_MODE");
        assertThatThrownBy(() -> GUIStaticUtils.uploadStateEngineProcessesFromClassPath(null, "ENE"))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("CLAUDE_MODE");
        assertThat(GUIStaticUtils.getVersionFromPOM("pom.xml")).isNull();
    }
}
