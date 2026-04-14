package de.creditreform.crefoteam.cte.tesun.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Schalen-Port aus {@code testsupport_client.tesun_util}. Nur die API-
 * Oberfläche, die von {@link TestCustomer} und
 * {@code EnvironmentConfig.initTestScenarios} aufgerufen wird.
 *
 * <p>Komplexe Operationen wie {@code refreshCollecteds()},
 * {@code refreshPseudoRefExports()} etc. sind No-Ops. Sie werden
 * implementiert, wenn die Check-/Collect-Tasks portiert sind.
 */
public class TestScenario {

    private final String scenarioName;
    private boolean activated = true;
    private final Map<String, TestCrefo> testFallNameToTestCrefoMap = new HashMap<>();
    private final Map<String, TestResults> testResultsMapForCommands = new HashMap<>();

    /** Leichter Konstruktor — für Tests und einfache Szenarien. */
    public TestScenario(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    /**
     * Konstruktor, den {@code EnvironmentConfig.initTestScenarios} aufruft.
     * Im Schalen-Port werden {@code testCustomer} und {@code archivBestandXmls}
     * vermerkt, aber nicht weiter verarbeitet.
     */
    public TestScenario(TestCustomer testCustomer, String scenarioName, List<File> archivBestandXmls) {
        this.scenarioName = scenarioName;
    }

    /** Clone-Konstruktor, den {@code TestCustomer} benutzt. */
    public TestScenario(TestScenario toClone) {
        this.scenarioName = toClone.scenarioName;
        this.activated = toClone.activated;
    }

    public String getScenarioName() { return scenarioName; }

    public boolean isActivated() { return activated; }

    public void setActivated(boolean activated) { this.activated = activated; }

    public Map<String, TestCrefo> getTestFallNameToTestCrefoMap() { return testFallNameToTestCrefoMap; }

    public void refreshCollecteds() { /* TODO: wird mit Collect-Tasks portiert */ }

    public void refreshPseudoRefExports() { /* TODO: wird mit PseudoRefExports-Tasks portiert */ }

    public void refreshRestoredCollects() { /* TODO: wird mit Restore-Tasks portiert */ }

    public void removeResultInfoForCommand(String command) {
        testResultsMapForCommands.remove(command);
    }

    public void clrearTestResults() {
        testResultsMapForCommands.values().forEach(r -> r.getResultInfosList().clear());
    }

    public void dumpResults(String command, StringBuilder sb, String prefix) {
        TestResults results = testResultsMapForCommands.get(command);
        if (results != null) {
            results.dumpResults(sb, prefix);
        }
    }

    public StringBuilder dump(String prefix) {
        return new StringBuilder(prefix).append("Scenario ").append(scenarioName);
    }
}
