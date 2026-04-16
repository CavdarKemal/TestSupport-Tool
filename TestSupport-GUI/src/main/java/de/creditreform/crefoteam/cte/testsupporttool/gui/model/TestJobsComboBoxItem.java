package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestJobsComboBoxItem implements Comparable<TestJobsComboBoxItem> {
    private final String displayName;
    private final List<String> testJobNamesList;
    private final Map<String, Object> taskVariablesMap = new HashMap<>();
    private final Integer sortField;

    public TestJobsComboBoxItem(String displayName, List<String> testJobNamesList, String... taskVariablesList) {
        final String[] split = displayName.split(":");
        this.sortField = Integer.valueOf(split[0]);
        this.displayName = split[1];
        this.testJobNamesList = testJobNamesList;
        for (String taskVariable : taskVariablesList) {
            taskVariablesMap.put(taskVariable, null);
        }
    }

    public List<String> getTestJobNamesList() {
        return testJobNamesList;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Map<String, Object> getTaskVariablesMap() {
        return taskVariablesMap;
    }

    public Integer getSortField() {
        return sortField;
    }

    public void setTaskVariables(String fieldText) {
        final String[] tokenPairs = fieldText.split(";");
        for (String tokenPair : tokenPairs) {
            if (tokenPair.trim().isEmpty()) {
                continue;
            }
            final String[] keyValuePair = tokenPair.trim().split("=");
            final String paramName = keyValuePair[0].trim();
            final String paramValue = keyValuePair[1].trim();
            if (taskVariablesMap.containsKey(paramName)) {
                taskVariablesMap.put(paramName, paramValue);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s", getDisplayName(), testJobNamesList);
    }

    @Override
    public int compareTo(TestJobsComboBoxItem theOther) {
        return getSortField().compareTo(theOther.getSortField());
    }

    public String getTaskVariablesMapAsFieldText() {
        StringBuilder stringBuilder = new StringBuilder();
        final Set<Map.Entry<String, Object>> entries = taskVariablesMap.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append(" = ");
            if (entry.getValue() != null) {
                stringBuilder.append(entry.getValue());
            }
            stringBuilder.append("; ");
        }
        return stringBuilder.toString();
    }
}
