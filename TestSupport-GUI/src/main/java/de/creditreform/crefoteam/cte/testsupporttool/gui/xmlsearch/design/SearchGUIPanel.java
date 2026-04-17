/*
 * Created by JFormDesigner on Mon Nov 24 10:35:53 CET 2014
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design;

import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.view.SearchDefinitionsView;
import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.view.SearchResultsView;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * @author Uzayli Temel
 */
public class SearchGUIPanel extends JPanel {
    public SearchGUIPanel() {
        initComponents();
    }

    public JPanel getViewlSettings() {
        return viewlSettings;
    }

    public JLabel getLabelConfigFileName() {
        return labelConfigFileName;
    }

    public JButton getButtonSelectConfigFile() {
        return buttonSelectConfigFile;
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }

    public SearchResultsView getViewSearchResults() {
        return viewSearchResults;
    }

    public SearchDefinitionsView getViewSearchDefinitions() {
        return viewSearchDefinitions;
    }

    public JComboBox getComboBoxConfigFileName() {
        return comboBoxConfigFileName;
    }

    public JButton getButtonSaveConfigFile() {
        return buttonSaveConfigFile;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        viewlSettings = new JPanel();
        labelConfigFileName = new JLabel();
        comboBoxConfigFileName = new JComboBox();
        buttonSelectConfigFile = new JButton();
        buttonSaveConfigFile = new JButton();
        splitPane = new JSplitPane();
        viewSearchDefinitions = new SearchDefinitionsView();
        viewSearchResults = new SearchResultsView();

        //======== this ========
        setName("this");
        setLayout(new BorderLayout());

        //======== viewlSettings ========
        {
            viewlSettings.setName("viewlSettings");
            viewlSettings.setLayout(new GridBagLayout());
            ((GridBagLayout) viewlSettings.getLayout()).columnWidths = new int[]{0, 78, 38, 14, 25, 0};
            ((GridBagLayout) viewlSettings.getLayout()).rowHeights = new int[]{0, 0};
            ((GridBagLayout) viewlSettings.getLayout()).columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 1.0E-4};
            ((GridBagLayout) viewlSettings.getLayout()).rowWeights = new double[]{0.0, 1.0E-4};

            //---- labelConfigFileName ----
            labelConfigFileName.setText("Konfiguration:");
            labelConfigFileName.setName("labelConfigFileName");
            viewlSettings.add(labelConfigFileName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 7), 0, 0));

            //---- comboBoxConfigFileName ----
            comboBoxConfigFileName.setName("comboBoxConfigFileName");
            viewlSettings.add(comboBoxConfigFileName, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

            //---- buttonSelectConfigFile ----
            buttonSelectConfigFile.setIcon(new ImageIcon(getClass().getResource("/icons/folder_view.png")));
            buttonSelectConfigFile.setName("buttonSelectConfigFile");
            viewlSettings.add(buttonSelectConfigFile, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 7), 0, 0));

            //---- buttonSaveConfigFile ----
            buttonSaveConfigFile.setIcon(new ImageIcon(getClass().getResource("/icons/save_16x16.png")));
            buttonSaveConfigFile.setName("buttonSaveConfigFile");
            viewlSettings.add(buttonSaveConfigFile, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 2), 0, 0));
        }
        add(viewlSettings, BorderLayout.NORTH);

        //======== splitPane ========
        {
            splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            splitPane.setDividerLocation(300);
            splitPane.setName("splitPane");

            //---- viewSearchDefinitions ----
            viewSearchDefinitions.setBorder(new EtchedBorder(EtchedBorder.RAISED));
            viewSearchDefinitions.setName("viewSearchDefinitions");
            splitPane.setTopComponent(viewSearchDefinitions);

            //---- viewSearchResults ----
            viewSearchResults.setBorder(new EtchedBorder(EtchedBorder.RAISED));
            viewSearchResults.setName("viewSearchResults");
            splitPane.setBottomComponent(viewSearchResults);
        }
        add(splitPane, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel viewlSettings;
    private JLabel labelConfigFileName;
    private JComboBox comboBoxConfigFileName;
    private JButton buttonSelectConfigFile;
    private JButton buttonSaveConfigFile;
    private JSplitPane splitPane;
    private SearchDefinitionsView viewSearchDefinitions;
    private SearchResultsView viewSearchResults;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
