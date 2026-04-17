package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIFrame;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design.SearchXMLsPanel;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.config.SearchConfigurationFactory;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.SearchSpecification;
import org.apache.commons.configuration.ConfigurationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchXMLsView extends SearchXMLsPanel {
    private final GUIFrame guiFrame;
    private ItemListener comboBoxConfigFileItemListener;
    private ActionListener selectConfigFileActionListener;
    private String oldSelectedConfigFile;

    public SearchXMLsView(GUIFrame guiFrame, String searchCfgPath) {
        this.guiFrame = guiFrame;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                initControls(searchCfgPath);
                doChangeSearchCfg();
                initListeners();
            }
        });
        thread.start();
    }

    private void initControls(String searchCfgPath) {
        getSplitPane().setDividerLocation(500);
        getViewSearchResults().setSearchDefinitionsView(getViewSearchDefinitions());

        // LRU: Properties-Dateinamen aus der Regsitry ermitteln und in die ComboBox...
        List<String> cfgFileNamesList = guiFrame.getEnvironmentConfig().getLastCfgFileNamesList();
        DefaultComboBoxModel aModel = new DefaultComboBoxModel(cfgFileNamesList.toArray(new String[]{}));
        getComboBoxConfigFileName().setModel(aModel);

        // Selektieren: Properties-Dateiname als Parameter übergeben?
        if (GUIStaticUtils.isEmpty(searchCfgPath)) {
            searchCfgPath = guiFrame.getEnvironmentConfig().getLastCfgFileName();
            if (GUIStaticUtils.isEmpty(searchCfgPath)) {
                searchCfgPath = (String) aModel.getElementAt(0);
            }
        }
        setSelectedConfigFile(searchCfgPath);
    }

    private void initListeners() {
        selectConfigFileActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doChooseConfigFile();
            }
        };
        getButtonSelectConfigFile().addActionListener(selectConfigFileActionListener);

        comboBoxConfigFileItemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    doChangeSearchCfg();
                }
            }
        };
        getComboBoxConfigFileName().addItemListener(comboBoxConfigFileItemListener);

        getButtonSaveConfigFile().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doSave();
            }
        });
    }

    private void doChangeSearchCfg() {
        String newSelectedConfigFile = getSelectedConfigFile();
        if (oldSelectedConfigFile != null && !oldSelectedConfigFile.equals(newSelectedConfigFile)) {
            SearchSpecification dirtyModel = getViewSearchDefinitions().checkAndGetDirtyModel();
            if (dirtyModel != null) {
                String errMsg = "Das Model '" + dirtyModel.getName() + "' wurde modifiziert!\nAlle Änderungen gehen nun verloren!";
                IllegalStateException ex = new IllegalStateException(errMsg);
                GUIStaticUtils.showExceptionMessage(this, "Warnung", ex);
            }
        }
        try {
            getViewSearchDefinitions().setModel(guiFrame, newSelectedConfigFile);
            setSelectedConfigFile(newSelectedConfigFile);
        } catch (ConfigurationException ex) {
            GUIStaticUtils.showExceptionMessage(SearchXMLsView.this, "Fehler beim Speichern der Konfiguration", ex);
            doChooseConfigFile();
            doChangeSearchCfg();
        }
    }

    private void changeControlsState(boolean modelOK) {
        String defFileName = getSelectedConfigFile();
        boolean is = modelOK && !defFileName.isEmpty();
        getComboBoxConfigFileName().setForeground(modelOK ? Color.BLACK : Color.RED);
    }

    protected void doSave() {
        Map<String, SearchSpecification> searcDataMap = getViewSearchDefinitions().getModel(false);
      /*
      if (!checkSearchSpecifications(searcDataMap)) {
         return;
      }
      */
        String selectedConfigFile = getSelectedConfigFile();
        if (selectedConfigFile.isEmpty()) {
            selectedConfigFile = System.getProperty("user.dir");
        }
        String newChoosenFileName = GUIStaticUtils.chooseFileName(this, selectedConfigFile, "*.properties", false);
        if (GUIStaticUtils.checkFileName(this, selectedConfigFile, newChoosenFileName)) {
            try {
                if (!newChoosenFileName.endsWith(".properties")) {
                    newChoosenFileName += ".properties";
                }
                SearchConfigurationFactory.saveSearchConfiguration(searcDataMap, newChoosenFileName);
                setSelectedConfigFile(newChoosenFileName);
            } catch (Exception ex) {
                GUIStaticUtils.showExceptionMessage(SearchXMLsView.this, "Fehler beim Speichern der Konfiguration", ex);
            }
        }
    }

   /*
   private boolean checkSearchSpecifications(Map<String, SearchSpecification> searcDataMap) {
      for (String key : searcDataMap.keySet()) {
         try {
            SearchConfigurationFactory.validate(searcDataMap.get(key));
         } catch (Exception ex) {
            GUIStaticUtils.showExceptionMessage(this, "Konfiguration Speichern", ex);
            return false;
         }
      }
      return true;
   }
   */

    protected void doChooseConfigFile() {
        String currentFileName = getSelectedConfigFile();
        SearchSpecification dirtyModel = getViewSearchDefinitions().checkAndGetDirtyModel();
        if (dirtyModel != null) {
            String questionMsg = "Das Model '" + dirtyModel.getName() + "' wurde modifiziert!\nSollen Änderungen verworfen werden?";
            int option = JOptionPane.showConfirmDialog(this, questionMsg, "Neue Suchdefinition laden", JOptionPane.YES_NO_OPTION);
            if (option != JOptionPane.OK_OPTION) {
                return;
            }
            // Die Änderungen sollen verworwen werden, das Model neu laden
            try {
                getViewSearchDefinitions().setModel(guiFrame, currentFileName);
            } catch (ConfigurationException ex) {
                GUIStaticUtils.showExceptionMessage(this, "Konfiguration Restaurieren", ex);
            }
        }
        String defaultPath = GUIStaticUtils.isEmpty(currentFileName) ? System.getProperty("user.dir") : new File(currentFileName).getParent();
        String choosenFileName = GUIStaticUtils.chooseFileName(this, defaultPath, "*.properties", true);
        if (GUIStaticUtils.isEmpty(choosenFileName)) {
            choosenFileName = currentFileName;
        }
        setSelectedConfigFile(choosenFileName);
    }

    private void updateRegistryForConfigFile(String choosenFileName) {
        //    getComboBoxConfigFileName().removeItemListener(comboBoxConfigFileItemListener);
        DefaultComboBoxModel theModel = (DefaultComboBoxModel) getComboBoxConfigFileName().getModel();
        int indexOf = theModel.getIndexOf(choosenFileName);
        if (indexOf < 0) {
            theModel.addElement(choosenFileName);
            List<String> cfgFileNamesList = new ArrayList<>();
            for (int i = 0; i < theModel.getSize(); i++) {
                cfgFileNamesList.add((String) theModel.getElementAt(i));
            }
            guiFrame.getEnvironmentConfig().setLastCfgFileNamesList(cfgFileNamesList);
        }
        guiFrame.getEnvironmentConfig().setLastCfgFileName(choosenFileName);
        //    getComboBoxConfigFileName().addItemListener(comboBoxConfigFileItemListener);
    }

    private void setSelectedConfigFile(String selectedConfigFile) {
        if (!GUIStaticUtils.isEmpty(selectedConfigFile)) {
            updateRegistryForConfigFile(selectedConfigFile);
            getComboBoxConfigFileName().setSelectedItem(selectedConfigFile);
            oldSelectedConfigFile = selectedConfigFile;
        }
        changeControlsState(selectedConfigFile != null);
    }

    private String getSelectedConfigFile() {
        Object selectedItem = getComboBoxConfigFileName().getModel().getSelectedItem();
        if ((selectedItem != null) && (selectedItem instanceof String)) {
            return (String) selectedItem;
        }
        return "";
    }

}
