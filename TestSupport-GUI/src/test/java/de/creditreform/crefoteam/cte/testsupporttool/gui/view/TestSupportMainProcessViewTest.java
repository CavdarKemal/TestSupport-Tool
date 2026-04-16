package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.awt.GraphicsEnvironment;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class TestSupportMainProcessViewTest {

    @Test
    void initTestPhasesComboBox_populatesBothPhasesAndSelectsFirst() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestSupportMainProcessView view = new TestSupportMainProcessView();
        view.initTestPhasesComboBox();

        assertThat(view.getComboBoxTestPhase().getItemCount()).isEqualTo(2);
        assertThat(view.getSelectedTestPhase()).isEqualTo(TestSupportClientKonstanten.TEST_PHASE.PHASE_1);
    }

    @Test
    void initTestJobsCombo_populatesManyItems() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestSupportMainProcessView view = new TestSupportMainProcessView();
        view.initTestJobsCombo();
        // Definiert in initTestJobsCombo: 21 sortierte Einträge.
        assertThat(view.getComboBoxTestJobs().getItemCount()).isEqualTo(21);
    }

    @Test
    void initUiDefaults_hidesFwAndExportFormatAndForcesDemoMode() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        TestSupportMainProcessView view = new TestSupportMainProcessView();
        view.initUiDefaults(env);
        assertThat(view.getLabelFachwertConfig().isVisible()).isFalse();
        assertThat(view.getRadioButtonFWConfigNewest().isVisible()).isFalse();
        assertThat(view.getLabelExportFormat().isVisible()).isFalse();
        // CLAUDE_MODE: Demo-Mode immer aktiviert + disabled.
        assertThat(view.isDemoMode()).isTrue();
        assertThat(view.getCheckBoxDemoMode().isEnabled()).isFalse();
    }

    @Test
    void init_wiresStartAndStopAndTestJobRunnables() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        AtomicInteger startCount = new AtomicInteger();
        AtomicInteger stopCount = new AtomicInteger();
        AtomicInteger testJobCount = new AtomicInteger();
        TestSupportMainProcessView view = new TestSupportMainProcessView();
        view.init(startCount::incrementAndGet, stopCount::incrementAndGet, testJobCount::incrementAndGet,
                () -> {}, () -> {}, () -> {}, () -> {}, env);

        view.getButtonStartProcess().doClick();
        view.getButtonStopUserTasksThread().doClick();
        view.getButtonStartTestJob().doClick();
        assertThat(startCount.get()).isEqualTo(1);
        assertThat(stopCount.get()).isEqualTo(1);
        assertThat(testJobCount.get()).isEqualTo(1);
    }

    @Test
    void setStopButtonEnabled_togglesFlag() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestSupportMainProcessView view = new TestSupportMainProcessView();
        view.setStopButtonEnabled(false);
        assertThat(view.getButtonStopUserTasksThread().isEnabled()).isFalse();
        view.setStopButtonEnabled(true);
        assertThat(view.getButtonStopUserTasksThread().isEnabled()).isTrue();
    }

    @Test
    void testCasesPath_setterAndGetterRoundtrip() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        TestSupportMainProcessView view = new TestSupportMainProcessView();
        view.setTestCasesPath("X:/mein/pfad");
        assertThat(view.getTestCasesPath()).isEqualTo("X:/mein/pfad");
    }
}
