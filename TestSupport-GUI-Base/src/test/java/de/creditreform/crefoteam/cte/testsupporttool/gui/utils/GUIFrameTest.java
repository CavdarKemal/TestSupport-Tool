package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import javax.swing.JMenuBar;
import javax.swing.UIManager;
import java.awt.GraphicsEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class GUIFrameTest {

    /** Konkrete Sub-Klasse fuer den Test (GUIFrame ist abstract). */
    private static final class TestFrame extends GUIFrame {
        TestFrame(EnvironmentConfig env) {
            super(env);
        }
    }

    @Test
    void getVersionFromPOM_returnsStringOrNull() {
        // Liest buildinfo.properties aus dem Classpath — null wenn nicht vorhanden.
        String version = GUIFrame.getVersionFromPOM();
        // Kein assert auf null: in Tests ohne buildinfo.properties ist null erwartet.
        assertThat(version == null || !version.isBlank()).isTrue();
    }

    @Test
    @DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
    void constructor_buildsFrameWithExpectedMenuBar() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM — Swing-Frame nicht testbar");
        String savedLaf = UIManager.getLookAndFeel().getClass().getName();
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        TestFrame frame = null;
        try {
            frame = new TestFrame(env);

            JMenuBar menuBar = frame.getJMenuBar();
            assertThat(menuBar).isNotNull();
            assertThat(menuBar.getMenuCount()).isEqualTo(2);
            assertThat(menuBar.getMenu(0).getText()).isEqualTo("Datei");
            assertThat(menuBar.getMenu(1).getText()).isEqualTo("Look & Feels");
        } finally {
            if (frame != null) frame.dispose();
            try { UIManager.setLookAndFeel(savedLaf); } catch (Exception ignored) { }
        }
    }

    @Test
    @DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
    void getterAndSetterForEnvironmentConfig_roundtrip() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM — Swing-Frame nicht testbar");
        String savedLaf = UIManager.getLookAndFeel().getClass().getName();
        EnvironmentConfig env1 = new EnvironmentConfig("ENE");
        EnvironmentConfig env2 = new EnvironmentConfig("ENE");
        TestFrame frame = null;
        try {
            frame = new TestFrame(env1);
            assertThat(frame.getEnvironmentConfig()).isSameAs(env1);

            frame.setEnvironmentConfig(env2);
            assertThat(frame.getEnvironmentConfig()).isSameAs(env2);
        } finally {
            if (frame != null) frame.dispose();
            try { UIManager.setLookAndFeel(savedLaf); } catch (Exception ignored) { }
        }
    }

    @Test
    @DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
    void setVersionsInfoInTitle_updatesFrameTitle() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM — Swing-Frame nicht testbar");
        String savedLaf = UIManager.getLookAndFeel().getClass().getName();
        TestFrame frame = null;
        try {
            frame = new TestFrame(new EnvironmentConfig("ENE"));
            frame.setVersionsInfoInTitle("Version 1.2.3");
            assertThat(frame.getTitle()).isEqualTo("Version 1.2.3");
        } finally {
            if (frame != null) frame.dispose();
            try { UIManager.setLookAndFeel(savedLaf); } catch (Exception ignored) { }
        }
    }
}
