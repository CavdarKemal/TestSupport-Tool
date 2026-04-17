package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 * 1:1-Port aus {@code testsupport_client.tesun.gui.utils.GUIFrame}.
 *
 * <p>Abstrakte JFrame-Basis fuer alle TestSupport-Frames. Erledigt:
 * <ul>
 *   <li>Wiederherstellen + Validieren der persistierten Fenster-Bounds
 *       gegen aktuell verfuegbare Monitore (via {@link WindowBoundsValidator}).</li>
 *   <li>Aufbau der Menue-Bar (Datei/Beenden + Look&amp;Feel-Auswahl).</li>
 *   <li>Beim Schliessen: Fenster-Bounds + L&amp;F in {@link EnvironmentConfig}
 *       zuruecksichern, {@link TimelineLogger#close()} aufrufen.</li>
 * </ul>
 */
public abstract class GUIFrame extends JFrame implements ActionListener {
    EnvironmentConfig environmentConfig;

    public GUIFrame(EnvironmentConfig environmentConfig) {
        this.environmentConfig = environmentConfig;

        // Validiere die persistierten Bounds gegen die aktuell verfügbaren
        // Monitore. Ohne diese Prüfung landet ein Fenster, dessen Position auf
        // einem anderen Monitor-Setup gespeichert wurde (z.B. Dual-Monitor mit
        // X=2702), komplett off-screen — User sehen die App nicht, und
        // Jemmy-basierte GUI-Tests schlagen sporadisch fehl, weil der Robot
        // die Komponenten nicht erreicht.
        Rectangle requested = new Rectangle(
                environmentConfig.getLastWindowXPos(),
                environmentConfig.getLastWindowYPos(),
                environmentConfig.getLastWindowWidth(),
                environmentConfig.getLastWindowHeight());
        Rectangle validated = WindowBoundsValidator.validate(requested);
        setBounds(validated);
        // Korrigierte Werte zurück in die Config schreiben, damit der nächste
        // Start nicht wieder den gleichen invaliden Zustand produziert
        if (!validated.equals(requested)) {
            environmentConfig.setLastWindowXPos(validated.x);
            environmentConfig.setLastWindowYPos(validated.y);
            environmentConfig.setLastWindowWidth(validated.width);
            environmentConfig.setLastWindowHeight(validated.height);
        }

        JMenuBar theJMenuBar = createMenuBar();
        setJMenuBar(theJMenuBar);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent componentEvent) {
                shutDown();
            }
        });
    }

    public EnvironmentConfig getEnvironmentConfig() {
        return environmentConfig;
    }

    public void setEnvironmentConfig(EnvironmentConfig environmentConfig) {
        this.environmentConfig = environmentConfig;
    }

    public void setVersionsInfoInTitle(String versionsInfoInTitle) {
        setTitle(versionsInfoInTitle);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JRadioButtonMenuItem menuItem = (JRadioButtonMenuItem) actionEvent.getSource();
        String actionCommand = menuItem.getActionCommand();
        doChangeLookAndFeel(actionCommand);
    }

    private void doChangeLookAndFeel(String lookAndFeel) {
        try {
            Dimension size = getSize();
            UIManager.setLookAndFeel(lookAndFeel);
            SwingUtilities.updateComponentTreeUI(this);
            pack();
            setSize(size);
            environmentConfig.setLastLookAndFeelClass(lookAndFeel);
        } catch (Exception ex) {
            new RuntimeException(ex);
        }
    }

    /**
     * <p><b>CLAUDE_MODE:</b> Originalmethode liest die Maven-Coordinates aus
     * {@code pomx.xml} (sic) per {@code MavenXpp3Reader}. Im Spike vermeiden
     * wir die {@code maven-model}-Dependency — die bevorzugte Quelle ist
     * jetzt {@code EnvironmentConfig.getVersionFromBuildInfo()}, das die
     * {@code META-INF/buildinfo/.../buildinfo.properties} liest.
     */
    public static String getVersionFromPOM() {
        return new de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig((String) null).getVersionFromBuildInfo();
    }

    public JRadioButtonMenuItem createToLookAndFeelMenu(final UIManager.LookAndFeelInfo lookAndFeelInfo) {
        JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(lookAndFeelInfo.getName());
        menuItem.setActionCommand(lookAndFeelInfo.getClassName());
        menuItem.addActionListener(this);
        return menuItem;
    }

    private JMenuBar createMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Datei");
        JMenuItem exitMenuItem = new JMenuItem("Beenden");
        exitMenuItem.setMnemonic(KeyEvent.VK_B);
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shutDown();
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        jMenuBar.add(fileMenu);

        final JMenu lookAndFeelsMenu = new JMenu("Look & Feels");
        jMenuBar.add(lookAndFeelsMenu);
        ButtonGroup buttonGroup = new ButtonGroup();
        UIManager.LookAndFeelInfo[] lookAndFeels = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo lookAndFeelInfo : lookAndFeels) {
            final JRadioButtonMenuItem toLookAndFeelMenu = createToLookAndFeelMenu(lookAndFeelInfo);
            buttonGroup.add(toLookAndFeelMenu);
            lookAndFeelsMenu.add(toLookAndFeelMenu);
        }
        if (environmentConfig.getLastLookAndFeelClass() == null) {
            environmentConfig.setLastLookAndFeelClass(lookAndFeels[0].getClassName());
        }
        doChangeLookAndFeel(environmentConfig.getLastLookAndFeelClass());
        return jMenuBar;
    }

    private void shutDown() {
        Dimension size = getSize();
        Point location = getLocation();
        environmentConfig.setLastWindowXPos(location.x);
        environmentConfig.setLastWindowYPos(location.y);
        environmentConfig.setLastWindowWidth(size.width);
        environmentConfig.setLastWindowHeight(size.height);
        try {
            environmentConfig.updateEnvironmentConfig();
            TimelineLogger.close();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
