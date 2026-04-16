package de.creditreform.crefoteam.cte.tesun.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Literal-Port aus {@code testsupport_client.tesun_util}. Arbeitet mit den
 * Schalen-Ports von {@link TestScenario}, {@link TestResults} und
 * {@link TestCrefo}.
 */
public class TestCustomer {
    private String customerKey;
    private String jvmName;
    private String customerName;
    private TestSupportClientKonstanten.TEST_PHASE testPhase;
    private Calendar lastJobStartetAt;
    private String exportUrl;
    private String uploadUrl;
    private String exportJobName;
    private String uploadJobName;
    private String processIdentifier;
    private File itsqAB30XmlsDir;
    private File itsqRefExportsDir;
    private File pseudoRefExportsDir;
    private File collectedsDir;
    private File restoredCollectedsDir;
    private File checksDir;
    private File sftpUploadsDir;
    private File testResultsFile;
    private boolean activated = true;
    private String customerPropertyPrefix;
    private String fwAktualisierungsdatum;
    private String pdVersion;

    private final Map<String, TestResults> testResultsMapForCommands = new HashMap<>();
    private final Map<String, TestScenario> testScenariosMap = new HashMap<>();

    private final ArrayList<MutablePair<String, String>> properyPairsList = new ArrayList<>() {{
        add(new MutablePair<>("%.vc", ""));
        add(new MutablePair<>("%.exportFormat", ""));
        add(new MutablePair<>("%.exportFormat.branchen", ""));
        add(new MutablePair<>("%.exportFormat.options", ""));
        add(new MutablePair<>("%.extra_xml_features", ""));
        add(new MutablePair<>("%.extra_xml_features.branchen", ""));
        add(new MutablePair<>("VerarbeitungBereich.vc", ""));
        add(new MutablePair<>("beteiligungenImport.beteiligungen_import.vc", ""));
        add(new MutablePair<>("beteiligungenImportDelta.beteiligungen_import.vc", ""));
        add(new MutablePair<>("beteiligungenImportFull.beteiligungen_import.vc", ""));
        add(new MutablePair<>("beteiligungen_import.vc", ""));
        add(new MutablePair<>("ctImportDelta.ctimport.vc", ""));
        add(new MutablePair<>("ctImportFull.ctimport.vc", ""));
        add(new MutablePair<>("ctimport.vc", ""));
        add(new MutablePair<>("deltaImport.ctimport.vc", ""));
        add(new MutablePair<>("importCycle.beteiligungen_import.vc", ""));
        add(new MutablePair<>("importCycle.ctimport.vc", ""));
        add(new MutablePair<>("relevanzMigrationDelta.relevanzmigration.vc", ""));
        add(new MutablePair<>("relevanzMigrationFull.relevanzmigration.vc", ""));
    }};

    public TestCustomer(String customerKey, String customerName) {
        this.customerKey = customerKey;
        this.customerName = customerName;
        this.testPhase = TestSupportClientKonstanten.TEST_PHASE.values()[0];
    }

    public TestCustomer(String customerKey, File itsqRoot, File testOutputsFile, TestSupportClientKonstanten.TEST_PHASE testPhase) {
        this.customerKey = customerKey;
        this.customerName = customerKey;
        this.testPhase = testPhase;

        File archivBestandRoot = new File(itsqRoot, TestSupportClientKonstanten.ARCHIV_BESTAND);
        this.itsqAB30XmlsDir = new File(archivBestandRoot, testPhase.getDirName());

        File itsqRefExportsRoot = new File(itsqRoot, TestSupportClientKonstanten.REF_EXPORTS);
        File itsqRefExportsPhase = new File(itsqRefExportsRoot, testPhase.getDirName());
        this.itsqRefExportsDir = new File(itsqRefExportsPhase, customerKey.toLowerCase(Locale.ROOT));

        File pseudoRefExportsPhase = new File(new File(testOutputsFile, TestSupportClientKonstanten.PSEUDO_REF_EXPORTS), testPhase.getDirName());
        this.pseudoRefExportsDir = new File(pseudoRefExportsPhase, customerKey.toLowerCase(Locale.ROOT));

        File checksPhase = new File(new File(testOutputsFile, TestSupportClientKonstanten.CHECKED), testPhase.getDirName());
        this.checksDir = new File(checksPhase, customerKey.toLowerCase(Locale.ROOT));

        File collectedsPhase = new File(new File(testOutputsFile, TestSupportClientKonstanten.COLLECTED), testPhase.getDirName());
        this.collectedsDir = new File(collectedsPhase, customerKey.toLowerCase(Locale.ROOT));

        File restoredCollectedsPhase = new File(new File(testOutputsFile, TestSupportClientKonstanten.RESTORED_COLLECTS), testPhase.getDirName());
        this.restoredCollectedsDir = new File(restoredCollectedsPhase, customerKey.toLowerCase(Locale.ROOT));

        File sftpUploadsPhase = new File(new File(testOutputsFile, TestSupportClientKonstanten.SFTP_UPLOADS), testPhase.getDirName());
        this.sftpUploadsDir = new File(sftpUploadsPhase, customerKey.toLowerCase(Locale.ROOT));
    }

    public TestCustomer(TestCustomer toBeCloned) {
        setCustomerKey(toBeCloned.getCustomerKey());
        setCustomerName(toBeCloned.getCustomerName());
        setTestPhase(toBeCloned.getTestPhase());
        setItsqRefExportsDir(toBeCloned.getItsqRefExportsDir());
        setPseudoRefExportsDir(toBeCloned.getPseudoRefExportsDir());
        setChecksDir(toBeCloned.getChecksDir());
        setCollectedsDir(toBeCloned.getCollectedsDir());
        setRestoredCollectedsDir(toBeCloned.getRestoredCollectedsDir());
        setSftpUploadsDir(toBeCloned.getSftpUploadsDir());
        setExportUrl(toBeCloned.getExportUrl());
        setUploadUrl(toBeCloned.getUploadUrl());

        setJvmName(toBeCloned.getJvmName());
        setExportJobName(toBeCloned.getExportJobName());
        setUploadJobName(toBeCloned.getUploadJobName());
        setActivated(toBeCloned.isActivated());
        setProcessIdentifier(toBeCloned.getProcessIdentifier());
        setLastJobStartetAt(toBeCloned.getLastJobStartetAt());
        getPropertyPairsList().addAll(toBeCloned.getPropertyPairsList());
        for (TestScenario testScenario : toBeCloned.getTestScenariosList()) {
            TestScenario clonedTtestScenario = new TestScenario(testScenario);
            addTestScenario(clonedTtestScenario);
        }
    }

    public static TestCustomer cloneEhForRisksImport(TestCustomer ehTestCustomer) {
        TestCustomer tmpCustomer = new TestCustomer(ehTestCustomer);
        tmpCustomer.setExportJobName("eh.riskNotificationsImport");
        return tmpCustomer;
    }

    public static TestCustomer cloneInsoMonitorPhase2(TestCustomer insomon1TestCustomer, String exportJobName, String uploadJobName, String processIdentifier) {
        TestCustomer tmpCustomer = new TestCustomer(insomon1TestCustomer);
        tmpCustomer.setExportUrl(insomon1TestCustomer.getExportUrl());
        tmpCustomer.setUploadUrl(insomon1TestCustomer.getUploadUrl());
        tmpCustomer.setCustomerKey(insomon1TestCustomer.getCustomerKey());
        tmpCustomer.setJvmName(insomon1TestCustomer.getJvmName());
        tmpCustomer.setCustomerName("Insolvenz Monitor Phase 2");
        tmpCustomer.setExportJobName(exportJobName);
        tmpCustomer.setUploadJobName(uploadJobName);
        tmpCustomer.getPropertyPairsList().addAll(insomon1TestCustomer.getPropertyPairsList());
        tmpCustomer.setProcessIdentifier(processIdentifier);
        tmpCustomer.setLastJobStartetAt(insomon1TestCustomer.getLastJobStartetAt());
        return tmpCustomer;
    }

    public String getJvmName() { return jvmName; }
    public void setJvmName(String jvmName) { this.jvmName = jvmName; }

    public String getCustomerKey() { return customerKey; }
    public void setCustomerKey(String customerKey) { this.customerKey = customerKey; }

    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerName() { return customerName; }

    public TestSupportClientKonstanten.TEST_PHASE getTestPhase() { return testPhase; }
    public void setTestPhase(TestSupportClientKonstanten.TEST_PHASE testPhase) { this.testPhase = testPhase; }

    public String getCustomerPropertyPrefix() { return customerPropertyPrefix; }
    public void setCustomerPropertyPrefix(String customerPropertyPrefix) { this.customerPropertyPrefix = customerPropertyPrefix; }

    public String getFwAktualisierungsdatum() { return fwAktualisierungsdatum; }
    public void setFwAktualisierungsdatum(String fwAktualisierungsdatum) { this.fwAktualisierungsdatum = fwAktualisierungsdatum; }

    public String getPdVersion() { return pdVersion; }
    public void setPdVersion(String pdVersion) { this.pdVersion = pdVersion; }

    public MutablePair<String, String> getProperty(String propName) {
        Optional<MutablePair<String, String>> optionalPair = getPropertyPairsList().stream()
                .filter(pair -> pair.getKey().equals(propName)).findFirst();
        return optionalPair.orElseGet(() -> new MutablePair<>(propName, ""));
    }

    public void setProperty(Pair<String, String> propertyPair) { /* no-op shell */ }

    public List<MutablePair<String, String>> getPropertyPairsList() { return properyPairsList; }

    public String getExportUrl() { return exportUrl; }
    public void setExportUrl(String exportUrl) { this.exportUrl = exportUrl; }

    public String getUploadUrl() { return uploadUrl; }
    public void setUploadUrl(String uploadUrl) { this.uploadUrl = uploadUrl; }

    public String getProcessIdentifier() { return processIdentifier; }
    public void setProcessIdentifier(String processIdentifier) { this.processIdentifier = processIdentifier; }

    public String getExportJobName() { return exportJobName; }
    public void setExportJobName(String exportJobName) { this.exportJobName = exportJobName; }

    public String getUploadJobName() { return uploadJobName; }
    public void setUploadJobName(String uploadJobName) { this.uploadJobName = uploadJobName; }

    public File getItsqAB30XmlsDir() { return itsqAB30XmlsDir; }
    public void setItsqAB30XmlsDir(File itsqAB30XmlsDir) { this.itsqAB30XmlsDir = itsqAB30XmlsDir; }

    public File getItsqRefExportsDir() { return itsqRefExportsDir; }
    public void setItsqRefExportsDir(File itsqRefExportsDir) { this.itsqRefExportsDir = itsqRefExportsDir; }

    public File getPseudoRefExportsDir() { return pseudoRefExportsDir; }
    public void setPseudoRefExportsDir(File pseudoRefExportsDir) { this.pseudoRefExportsDir = pseudoRefExportsDir; }

    public File getCollectedsDir() { return collectedsDir; }
    public void setCollectedsDir(File collectedsDir) { this.collectedsDir = collectedsDir; }

    public File getRestoredCollectedsDir() { return restoredCollectedsDir; }
    public void setRestoredCollectedsDir(File restoredCollectedsDir) { this.restoredCollectedsDir = restoredCollectedsDir; }

    public File getChecksDir() { return checksDir; }
    public void setChecksDir(File checksDir) { this.checksDir = checksDir; }

    public File getTestResultsFile() { return testResultsFile; }
    public void setTestResultsFile(File testResultsFile) { this.testResultsFile = testResultsFile; }

    public File getSftpUploadsDir() { return sftpUploadsDir; }
    public void setSftpUploadsDir(File sftpUploadsDir) { this.sftpUploadsDir = sftpUploadsDir; }

    public Calendar getLastJobStartetAt() { return lastJobStartetAt; }
    public void setLastJobStartetAt(Calendar lastJobStartetAt) { this.lastJobStartetAt = lastJobStartetAt; }

    public void addTestScenario(TestScenario testScenario) {
        testScenariosMap.put(testScenario.getScenarioName(), testScenario);
    }

    public Map<String, TestScenario> getTestScenariosMap() { return testScenariosMap; }

    public List<TestScenario> getTestScenariosList() {
        return new ArrayList<>(testScenariosMap.values());
    }

    public void setActivated(boolean activated) { this.activated = activated; }
    public boolean isActivated() { return activated; }

    public TestScenario getScenario(String scenarioName) {
        return testScenariosMap.get(scenarioName);
    }

    @Override
    public String toString() {
        return String.format("%s:%s", customerKey, customerName);
    }

    public List<Long> getAllTestCrefosAsLongList(boolean activeOnly, boolean positiveOnly) {
        List<TestCrefo> testCrefosList = getAllTestCrefos(activeOnly, positiveOnly);
        List<Long> crefosList = new ArrayList<>();
        for (TestCrefo testCrefo : testCrefosList) {
            if (!activeOnly || testCrefo.isActivated()) {
                if (!positiveOnly || testCrefo.isShouldBeExported()) {
                    crefosList.add(testCrefo.getItsqTestCrefoNr());
                }
            }
        }
        return crefosList;
    }

    public List<TestCrefo> getAllTestCrefos(boolean activeOnly, boolean positiveOnly) {
        List<TestCrefo> testCrefosList = new ArrayList<>();
        for (TestScenario testScenario : getTestScenariosList()) {
            if (!activeOnly || testScenario.isActivated()) {
                for (TestCrefo testCrefo : testScenario.getTestFallNameToTestCrefoMap().values()) {
                    if (!activeOnly || testCrefo.isActivated()) {
                        if (!positiveOnly || testCrefo.isShouldBeExported()) {
                            testCrefosList.add(testCrefo);
                        }
                    }
                }
            }
        }
        return testCrefosList;
    }

    public void refreshCollecteds() {
        getTestScenariosList().forEach(TestScenario::refreshCollecteds);
    }

    public void refreshPseudoRefExports() {
        getTestScenariosList().forEach(TestScenario::refreshPseudoRefExports);
    }

    public void refreshRestoredCollects() {
        getTestScenariosList().forEach(TestScenario::refreshRestoredCollects);
    }

    public TestResults getTestResultsForCommand(String command) {
        return testResultsMapForCommands.computeIfAbsent(command, TestResults::new);
    }

    public Map<String, TestResults> getTestResultsMapForCommands() { return testResultsMapForCommands; }

    public void emptyTestResultsMapForCommands() {
        testResultsMapForCommands.values().forEach(r -> r.getResultInfosList().clear());
    }

    public TestResults addTestResultsForCommand(String command) {
        testResultsMapForCommands.remove(command);
        getTestScenariosMap().values().forEach(testScenario -> testScenario.removeResultInfoForCommand(command));
        return getTestResultsForCommand(command);
    }

    public void addResultInfo(String command, TestResults.ResultInfo resultInfo) {
        getTestResultsForCommand(command).addResultInfo(resultInfo);
    }

    public void dumpResults(StringBuilder sbForTestCustomer, String prefix) {
        Map<String, TestResults> testResultsMap = getTestResultsMapForCommands();
        if (!testResultsMap.isEmpty()) {
            sbForTestCustomer.append(prefix).append("Test-Results für den Kunden '").append(getCustomerKey()).append("'");
        }
        testResultsMap.forEach((command, testResults) -> {
            StringBuilder sbForCommand = new StringBuilder();
            testResults.dumpResults(sbForCommand, prefix + "\t");
            List<StringBuilder> sbForScenariosList = new ArrayList<>();
            getTestScenariosMap().values().forEach(testScenario -> {
                StringBuilder sbForScenario = new StringBuilder();
                testScenario.dumpResults(testResults.getCommand(), sbForScenario, prefix + "\t\t");
                if (sbForScenario.length() > 0) {
                    sbForScenariosList.add(sbForScenario);
                }
            });
            if (sbForCommand.length() > 0 || !sbForScenariosList.isEmpty()) {
                sbForTestCustomer.append(prefix).append("\tTest-Results für UserTask '").append(testResults.getCommand()).append("'");
                sbForTestCustomer.append(sbForCommand);
            }
            sbForScenariosList.forEach(sbForTestCustomer::append);
        });
    }

    public File dumpResultsToFile() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        dumpResults(stringBuilder, "\n");
        if (stringBuilder.length() > 0) {
            File theFile = new File(getChecksDir(), "TestResults.txt");
            setTestResultsFile(theFile);
            FileUtils.writeStringToFile(theFile, stringBuilder.toString(), StandardCharsets.UTF_8);
            return theFile;
        }
        return null;
    }

    public void clrearTestResults() {
        testResultsMapForCommands.values().forEach(r -> {
            r.getResultInfosList().clear();
            getTestScenariosMap().values().forEach(TestScenario::clrearTestResults);
        });
    }

    public StringBuilder dumpTestCustomer(String prefix) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix).append("Kunde ").append(customerKey);
        getTestScenariosMap().values().forEach(testScenario ->
                stringBuilder.append(prefix).append(testScenario.dump("\n\t")));
        return stringBuilder;
    }
}
