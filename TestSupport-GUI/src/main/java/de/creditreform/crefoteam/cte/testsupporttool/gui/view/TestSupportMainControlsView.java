package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.rest.RestInvokerConfig;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.testsupporttool.gui.design.TestSupportMainControlsPanel;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestSupportMainControlsView extends TestSupportMainControlsPanel {

    private Runnable onHostChanged;
    private Runnable onRefreshEnvironment;
    private Runnable onManageJVMs;
    private Runnable onEnvironmentChanged;

    public TestSupportMainControlsView() {
        super();
    }

    public void init(TesunClientJobListener listener,
                     Runnable onHostChanged,
                     Runnable onRefreshEnvironment,
                     Runnable onManageJVMs,
                     Runnable onEnvironmentChanged) {
        this.onHostChanged = onHostChanged;
        this.onRefreshEnvironment = onRefreshEnvironment;
        this.onManageJVMs = onManageJVMs;
        this.onEnvironmentChanged = onEnvironmentChanged;
        initListeners();
    }

    private void initListeners() {
        getComboBoxEnvironment().addActionListener(e -> onEnvironmentChanged.run());
        getComboBoxStateEngineHost().addActionListener(e -> onHostChanged.run());
        getComboBoxImpCycleHost().addActionListener(e -> onHostChanged.run());
        getComboBoxRestServicesHost().addActionListener(e -> onHostChanged.run());
        getButtonRefreshEnvironment().addActionListener(e -> onRefreshEnvironment.run());
        getButtonManageJVMs().addActionListener(e -> onManageJVMs.run());
    }

    public void initEnvironmentsComboBox(EnvironmentConfig currentEnvironment) {
        DefaultComboBoxModel<String> environmentsModel = new DefaultComboBoxModel<>();
        Map<String, File> environmentsMap = currentEnvironment.getEnvironmentsMap();
        for (String env : environmentsMap.keySet()) {
            environmentsModel.addElement(env);
        }
        getComboBoxEnvironment().setModel(environmentsModel);
        getComboBoxEnvironment().setSelectedItem(currentEnvironment.getCurrentEnvName());
    }

    public void initHostsFields(EnvironmentConfig currentEnvironment) throws PropertiesException {
        getComboBoxStateEngineHost().setModel(new DefaultComboBoxModel<RestInvokerConfigCbItem>());

        getComboBoxRestServicesHost().setModel(new DefaultComboBoxModel<RestInvokerConfigCbItem>());
        currentEnvironment.getRestServiceConfigsForMasterkonsole().forEach(c ->
                getComboBoxRestServicesHost().addItem(new RestInvokerConfigCbItem(c.getServiceURI(), c)));

        getComboBoxBatchGUIHost().setModel(new DefaultComboBoxModel<RestInvokerConfigCbItem>());
        currentEnvironment.getRestServiceConfigsForBatchGUI().forEach(c ->
                getComboBoxBatchGUIHost().addItem(new RestInvokerConfigCbItem(c.getServiceURI(), c)));

        getComboBoxImpCycleHost().setModel(new DefaultComboBoxModel<RestInvokerConfigCbItem>());
        currentEnvironment.getRestServiceConfigsForJvmImpCycle().forEach(c ->
                getComboBoxImpCycleHost().addItem(new RestInvokerConfigCbItem(c.getServiceURI(), c)));

        getComboBoxInsoHost().setModel(new DefaultComboBoxModel<RestInvokerConfigCbItem>());
        currentEnvironment.getRestServiceConfigsForJvmInso().forEach(c ->
                getComboBoxInsoHost().addItem(new RestInvokerConfigCbItem(c.getServiceURI(), c)));

        getComboBoxInsoBackEndHost().setModel(new DefaultComboBoxModel<RestInvokerConfigCbItem>());
        currentEnvironment.getRestServiceConfigsForJvmInsoBackend().forEach(c ->
                getComboBoxInsoBackEndHost().addItem(new RestInvokerConfigCbItem(c.getServiceURI(), c)));
    }

    public RestInvokerConfig getSelectedStateEngineConfig() {
        RestInvokerConfigCbItem item = (RestInvokerConfigCbItem) getComboBoxStateEngineHost().getSelectedItem();
        return item != null ? item.getRestInvokerConfig() : null;
    }

    public RestInvokerConfig getSelectedRestServicesConfig() {
        RestInvokerConfigCbItem item = (RestInvokerConfigCbItem) getComboBoxRestServicesHost().getSelectedItem();
        return item != null ? item.getRestInvokerConfig() : null;
    }

    public RestInvokerConfig getSelectedImpCycleConfig() {
        RestInvokerConfigCbItem item = (RestInvokerConfigCbItem) getComboBoxImpCycleHost().getSelectedItem();
        return item != null ? item.getRestInvokerConfig() : null;
    }

    public String getSelectedEnvironmentName() {
        return getComboBoxEnvironment().getSelectedItem().toString();
    }

    public void setSelectedEnvironment(String envName) {
        ActionListener[] listeners = disableCbListeners(getComboBoxEnvironment());
        getComboBoxEnvironment().setSelectedItem(envName);
        enableCbListeners(getComboBoxEnvironment(), listeners);
    }

    public List<JComponent> getComponentsToOnOff() {
        List<JComponent> list = new ArrayList<>();
        list.add(getComboBoxStateEngineHost());
        list.add(getComboBoxRestServicesHost());
        list.add(getComboBoxBatchGUIHost());
        list.add(getComboBoxImpCycleHost());
        list.add(getComboBoxInsoHost());
        list.add(getComboBoxInsoBackEndHost());
        list.add(getButtonManageJVMs());
        list.add(getComboBoxEnvironment());
        list.add(getButtonRefreshEnvironment());
        return list;
    }

    public void updateAdminButtonState(EnvironmentConfig currentEnvironment) throws PropertiesException {
        // Tippfehler aus Original (îsAdminFuncsEnabled) im Core-Port zu isAdminFuncsEnabled korrigiert.
        getButtonManageJVMs().setEnabled(currentEnvironment.isAdminFuncsEnabled());
    }

    private void enableCbListeners(JComboBox comboBox, ActionListener[] actionListeners) {
        for (ActionListener al : actionListeners) comboBox.addActionListener(al);
    }

    private ActionListener[] disableCbListeners(JComboBox comboBox) {
        ActionListener[] listeners = comboBox.getActionListeners();
        for (ActionListener al : listeners) comboBox.removeActionListener(al);
        return listeners;
    }

    static class RestInvokerConfigCbItem {
        private final String serviceURL;
        private final RestInvokerConfig restInvokerConfig;

        RestInvokerConfigCbItem(String serviceURL, RestInvokerConfig restInvokerConfig) {
            this.serviceURL = serviceURL;
            this.restInvokerConfig = restInvokerConfig;
        }

        public String getServiceURL() { return serviceURL; }

        public RestInvokerConfig getRestInvokerConfig() { return restInvokerConfig; }

        @Override
        public String toString() { return serviceURL; }
    }
}
