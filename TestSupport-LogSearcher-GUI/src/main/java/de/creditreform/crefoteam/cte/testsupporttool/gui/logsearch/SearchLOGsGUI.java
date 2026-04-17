package de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch;

import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.view.SearchLogsView;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIFrame;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;

import javax.swing.*;

public class SearchLOGsGUI extends GUIFrame {

    public SearchLOGsGUI() {
        this(new EnvironmentConfig("ENE"));
    }

    public SearchLOGsGUI(EnvironmentConfig environmentConfig) {
        super(environmentConfig);
        // Wenn aus dem Haupt-Frame geoeffnet, darf das Schliessen nicht die JVM
        // beenden — GUIFrame setzt EXIT_ON_CLOSE, wir korrigieren das hier.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("LOG-Suche");
        SearchLogsView searchDefinitionView = new SearchLogsView(this);
        getContentPane().add(searchDefinitionView);
    }

    /** Zugriff für Jemmy-basierte GUI-Tests. */
    public SearchLogsView getSearchLogsView() {
        return (SearchLogsView) getContentPane().getComponent(0);
    }

    public static void main(String[] cmdArgs) {
        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
            SwingUtilities.invokeLater(() -> {
                try {
                    SearchLOGsGUI gui = new SearchLOGsGUI();
                    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    gui.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.exit(-1);
                }
            });
        } catch (Exception ex) {
            new RuntimeException(ex);
        }
    }

}
