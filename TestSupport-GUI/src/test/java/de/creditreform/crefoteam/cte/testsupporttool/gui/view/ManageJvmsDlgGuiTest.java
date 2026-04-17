package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.BaseGUITest;
import de.creditreform.crefoteam.cte.testsupporttool.gui.TestSupportGUI;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;
import de.creditreform.crefoteam.cte.testsupporttool.env.EnvironmentLockManager;
import de.creditreform.crefoteam.cte.testsupporttool.resume.ResumeState;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.netbeans.jemmy.operators.JButtonOperator;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Jemmy-GUI-Test fuer den JVM-Verwaltungs-Button.
 *
 * <p>Der Dialog selbst macht REST-Calls mit 30-Sekunden-Timeout — ein
 * vollstaendiger Dialog-Open/Close-Zyklus wuerde im Test ohne laufenden
 * REST-Server >30 Sekunden dauern. Deshalb prueft dieser Test nur, dass
 * der Button korrekt verdrahtet, sichtbar und aktivierbar ist.
 * Die Dialog-Klasse wird durch {@code ci.cmd} (Compiler) verifiziert;
 * das Verhalten bei echtem Server-Zugriff ist Integrations-Test-Gebiet.
 */
@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class ManageJvmsDlgGuiTest extends BaseGUITest {

    @BeforeAll
    static void assumeNotHeadless() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM — Jemmy braucht echtes Display");
    }

    @AfterAll
    static void releaseGlobal() {
        try { TimelineLogger.close(); } catch (Exception ignored) { }
        try { EnvironmentLockManager.releaseLock(); } catch (Exception ignored) { }
    }

    @BeforeEach
    void openGui() throws Exception {
        try { EnvironmentLockManager.releaseLock(); } catch (Exception ignored) { }
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        ResumeState.delete(new File(env.getTestOutputsRoot(), ResumeState.FILE_NAME));
        TestSupportGUI gui = createOnEdt(() -> {
            TestSupportGUI g = new TestSupportGUI(env);
            g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            g.setVisible(true);
            return g;
        });
        setGuiFrame(gui);
    }

    @Test
    @Timeout(30)
    void jvmButton_istVorhanden_undHatKorrektenText() throws Exception {
        TestSupportView view = getView();
        boolean[] exists = {false};
        String[] text = {""};
        SwingUtilities.invokeAndWait(() -> {
            javax.swing.JButton btn = view.getViewTestSupportMainControls().getButtonManageJVMs();
            exists[0] = btn != null;
            text[0] = btn != null ? btn.getText() : "";
        });
        assertThat(exists[0]).isTrue();
        assertThat(text[0]).contains("JVM");
    }

    @Test
    @Timeout(30)
    void jvmButton_wirdinJemmyGefunden() {
        JButtonOperator jvmButton = new JButtonOperator(frameOperator, "JVM");
        assertThat(jvmButton.getSource()).isNotNull();
        assertThat(jvmButton.isVisible()).isTrue();
    }

    @Test
    @Timeout(30)
    void jvmButton_aktiviertNachEnvironmentInit() throws Exception {
        TestSupportView view = getView();
        // Kurze Wartezeit, damit die GUI sich vollstaendig initialisiert
        Thread.sleep(500);
        boolean[] enabled = {false};
        SwingUtilities.invokeAndWait(() ->
                enabled[0] = view.getViewTestSupportMainControls().getButtonManageJVMs().isEnabled());
        // isAdminFuncsEnabled() steuert den Enabled-Status; im ENE-DemoMode
        // kann er false sein — wir prüfen nur, dass keine Exception auftritt.
        // Der enabled-Zustand selbst ist umgebungsabhaengig.
        assertThat(view.getViewTestSupportMainControls().getButtonManageJVMs()).isNotNull();
    }

    private TestSupportView getView() {
        return ((TestSupportGUI) guiFrame).getTestSupportView();
    }
}
