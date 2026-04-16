package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.awt.GraphicsEnvironment;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class TestSupportMainControlsViewTest {

    @Test
    void initEnvironmentsComboBox_populatesFromEnvironmentConfig() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        TestSupportMainControlsView view = new TestSupportMainControlsView();
        view.initEnvironmentsComboBox(env);

        assertThat(view.getComboBoxEnvironment().getItemCount()).isGreaterThan(0);
        assertThat(view.getSelectedEnvironmentName()).isEqualTo(env.getCurrentEnvName());
    }

    @Test
    void init_wiresRefreshButtonToRunnable() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        AtomicInteger refreshCount = new AtomicInteger();
        TestSupportMainControlsView view = new TestSupportMainControlsView();
        view.init(null, () -> {}, refreshCount::incrementAndGet, () -> {}, () -> {});

        view.getButtonRefreshEnvironment().doClick();
        assertThat(refreshCount.get()).isEqualTo(1);
    }

    @Test
    void init_wiresManageJVMsButton() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        AtomicInteger jvmCount = new AtomicInteger();
        TestSupportMainControlsView view = new TestSupportMainControlsView();
        view.init(null, () -> {}, () -> {}, jvmCount::incrementAndGet, () -> {});

        view.getButtonManageJVMs().doClick();
        assertThat(jvmCount.get()).isEqualTo(1);
    }

    @Test
    void getComponentsToOnOff_containsExpectedCount() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestSupportMainControlsView view = new TestSupportMainControlsView();
        // 6 Host-ComboBoxes + Environment-CB + Refresh-Button + Manage-JVM = 9
        assertThat(view.getComponentsToOnOff()).hasSize(9);
    }

    @Test
    void setSelectedEnvironment_switchesWithoutFiringListeners() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        AtomicInteger envChangeCount = new AtomicInteger();
        TestSupportMainControlsView view = new TestSupportMainControlsView();
        view.initEnvironmentsComboBox(env);
        view.init(null, () -> {}, () -> {}, () -> {}, envChangeCount::incrementAndGet);

        int before = envChangeCount.get();
        view.setSelectedEnvironment(env.getCurrentEnvName());
        assertThat(envChangeCount.get()).isEqualTo(before);
    }
}
