package de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch;

import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.view.SearchLogsView;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIFrame;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;

import javax.swing.*;

public class SearchLOGsGUI extends GUIFrame {
    private static final EnvironmentConfig environmentConfig = new EnvironmentConfig("ENE");

    public SearchLOGsGUI() throws Exception {
        super(environmentConfig);
        SearchLogsView searchDefinitionView = new SearchLogsView(this);
        getContentPane().add(searchDefinitionView);
        setVisible(true);
    }

    public static void main(String[] cmdArgs) {
        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        new SearchLOGsGUI();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.exit(-1);
                    }
                }
            });
        } catch (Exception ex) {
            new RuntimeException(ex);
        }
    }

}
