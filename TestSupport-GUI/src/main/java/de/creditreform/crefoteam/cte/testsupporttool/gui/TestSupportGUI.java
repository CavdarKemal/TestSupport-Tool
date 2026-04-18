package de.creditreform.crefoteam.cte.testsupporttool.gui;

import de.creditreform.crefoteam.cte.testsupporttool.CliArgs;
import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.SearchLOGsGUI;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIFrame;
import de.creditreform.crefoteam.cte.testsupporttool.gui.view.TestSupportView;
import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.SearchXMLsGUI;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;
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

    private final TestSupportView testSupportView;

    public TestSupportGUI(EnvironmentConfig environmentConfig) {
        super(environmentConfig);
        this.testSupportView = new TestSupportView(this);
        setContentPane(testSupportView);
        setTitle("CTE-Testautomatisierung — Tool");
        setSize(1400, 900);
        setLocationRelativeTo(null);
        addToolsMenu();
    }

    private void addToolsMenu() {
        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.setName("menuTools");
        JMenuItem logSearchItem = new JMenuItem("LOG-Suche...");
        logSearchItem.setName("menuItemLogSearch");
        logSearchItem.addActionListener(e -> openLogSearch());
        toolsMenu.add(logSearchItem);

        JMenuItem xmlSearchItem = new JMenuItem("XML-Suche...");
        xmlSearchItem.setName("menuItemXmlSearch");
        xmlSearchItem.addActionListener(e -> doSearchXMLs());
        toolsMenu.add(xmlSearchItem);

        getJMenuBar().add(toolsMenu);
    }

    private void openLogSearch() {
        SearchLOGsGUI logSearchGui = new SearchLOGsGUI(getEnvironmentConfig());
        logSearchGui.setVisible(true);
    }

    private void doSearchXMLs() {
        try {
            SearchXMLsGUI xmlSearchGui = new SearchXMLsGUI(null);
            xmlSearchGui.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Fehler beim Öffnen der XML-Suche:\n" + ex.getMessage(), "XML-Suche", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Zugriff für Jemmy-basierte GUI-Tests. */
    public TestSupportView getTestSupportView() {
        return testSupportView;
    }

    public static void main(String[] args) {
        CliArgs cli;
        try {
            cli = CliArgs.parse(args);
            cli.requireValid();
        } catch (IllegalArgumentException ex) {
            TimelineLogger.error(TestSupportGUI.class, ex.getMessage());
            TimelineLogger.info(TestSupportGUI.class, CliArgs.usage());
            System.exit(64); // EX_USAGE
            return;
        }
        EnvironmentConfig environmentConfig = new EnvironmentConfig(cli.getEnvName());
        SwingUtilities.invokeLater(() -> {
            TestSupportGUI frame = null;
            try {
                frame = new TestSupportGUI(environmentConfig);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Fehler beim Starten:\n" + ex.getMessage(), "Start-Fehler", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
