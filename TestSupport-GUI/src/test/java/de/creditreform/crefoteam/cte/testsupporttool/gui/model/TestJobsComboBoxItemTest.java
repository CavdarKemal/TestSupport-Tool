package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TestJobsComboBoxItemTest {

    @Test
    void constructor_parsesSortFieldAndDisplayName() {
        TestJobsComboBoxItem item = new TestJobsComboBoxItem(
                "14:Exports starten", Collections.singletonList("UserTaskStartExports"));
        assertThat(item.getSortField()).isEqualTo(14);
        assertThat(item.getDisplayName()).isEqualTo("Exports starten");
    }

    @Test
    void taskVariablesMap_initializedWithNullValuesForEachKey() {
        TestJobsComboBoxItem item = new TestJobsComboBoxItem(
                "16:Collect starten", Collections.singletonList("UserTaskStartCollect"),
                "VAR_A", "VAR_B");
        assertThat(item.getTaskVariablesMap())
                .containsOnlyKeys("VAR_A", "VAR_B")
                .containsValue(null);
    }

    @Test
    void taskVariablesMap_emptyWhenNoVarArgs() {
        TestJobsComboBoxItem item = new TestJobsComboBoxItem(
                "2:Pseudo", Collections.singletonList("UserTaskGeneratePseudoCrefos"));
        assertThat(item.getTaskVariablesMap()).isEmpty();
    }

    @Test
    void setTaskVariables_parsesKeyValuePairsAndIgnoresUnknown() {
        TestJobsComboBoxItem item = new TestJobsComboBoxItem(
                "42:X", Collections.singletonList("UserTask"), "KEY");
        item.setTaskVariables("KEY = 42; UNKNOWN = ignored");
        assertThat(item.getTaskVariablesMap()).containsEntry("KEY", "42");
        assertThat(item.getTaskVariablesMap()).doesNotContainKey("UNKNOWN");
    }

    @Test
    void compareTo_ordersBySortField() {
        TestJobsComboBoxItem a = new TestJobsComboBoxItem("10:a", Collections.emptyList());
        TestJobsComboBoxItem b = new TestJobsComboBoxItem("20:b", Collections.emptyList());
        assertThat(a.compareTo(b)).isNegative();
        assertThat(b.compareTo(a)).isPositive();
    }

    @Test
    void toString_containsDisplayNameAndJobNames() {
        TestJobsComboBoxItem item = new TestJobsComboBoxItem(
                "2:Display", Arrays.asList("JobA", "JobB"));
        assertThat(item.toString()).contains("Display").contains("JobA").contains("JobB");
    }

    @Test
    void getTaskVariablesMapAsFieldText_producesKeyEqualsValueSemicolonFormat() {
        TestJobsComboBoxItem item = new TestJobsComboBoxItem(
                "2:X", Collections.emptyList(), "KEY");
        item.setTaskVariables("KEY = hello");
        assertThat(item.getTaskVariablesMapAsFieldText()).contains("KEY").contains("hello").contains(";");
    }

    @Test
    void getTestJobNamesList_returnsPassedList() {
        List<String> jobs = Arrays.asList("A", "B", "C");
        TestJobsComboBoxItem item = new TestJobsComboBoxItem("1:x", jobs);
        assertThat(item.getTestJobNamesList()).containsExactlyElementsOf(jobs);
    }
}
