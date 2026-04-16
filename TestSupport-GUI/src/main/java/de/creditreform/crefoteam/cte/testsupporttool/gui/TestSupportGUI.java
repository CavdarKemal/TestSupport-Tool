package de.creditreform.crefoteam.cte.testsupporttool.gui;

import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIFrame;
import de.creditreform.crefoteam.cte.testsupporttool.gui.view.TestSupportView;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;

import javax.swing.*;

/**
 * Entry-Point der Test-Support-GUI — Port aus
 * {@code testsupport_client.tesun.gui.TestSupportGUI}.
 *
 * <p>Stützt sich auf das {@link GUIFrame}-Framework und wickelt nur Laden
 * der Default-Umgebung + Instanziierung von {@link TestSupportView} ab.
 */
public final class TestSupportGUI extends GUIFrame {

    public TestSupportGUI(EnvironmentConfig environmentConfig) {
        super(environmentConfig);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TestSupportGUI frame = null;
            try {
                EnvironmentConfig environmentConfig = new EnvironmentConfig();
                frame = new TestSupportGUI(environmentConfig);
                TestSupportView view = new TestSupportView(frame);
                frame.setContentPane(view);
                frame.setTitle("CTE-Testautomatisierung — Tool");
                frame.setSize(1400, 900);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,
                        "Fehler beim Starten:\n" + ex.getMessage(),
                        "Start-Fehler", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
