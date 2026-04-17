package de.creditreform.crefoteam.cte.tesun.util;

import de.creditreform.crefoteam.cte.tesun.exports_checker.TextFileComparator;
import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CustomerTestResultsParser {

    public Map<String, TestCustomer> parseTestResultsFile(File testResultsFile) throws Exception {
        Map<String, TestCustomer> testCustomerMap = new TreeMap<>();
        List<String> linesList = FileUtils.readLines(testResultsFile);
        String strLine = getNextLine(linesList);
        String command = "";
        TestCustomer testCustomer = null;
        while (strLine != null) {
            if (strLine.startsWith("Test-Results für den Kunden")) {
                String customerKey = strLine.split("'")[1];
                testCustomer = new TestCustomer(customerKey, customerKey);
                strLine = checkLineForCustomer(testCustomer, strLine, linesList);
                testCustomerMap.put(customerKey, testCustomer);
            } else if ((strLine != null) && strLine.startsWith("Test-Results für UserTask")) {
                command = strLine.split("'")[1].trim();
                strLine = addTestResultsForCommand(testCustomer, strLine, linesList);
            } else if ((strLine != null) && strLine.startsWith("Test-Results für Test-Scenario")) {
                String scenarioName = strLine.split("'")[1].trim();
                strLine = addTestResultsForScenrio(testCustomer, command, scenarioName, linesList, testResultsFile);
            }
        }
        return testCustomerMap;
    }

    public void parseTestResultsFile(File testResultsFile, TestCustomer testCustomer) throws Exception {
        List<String> linesList = FileUtils.readLines(testResultsFile);
        String strLine = getNextLine(linesList);
        String command = "";
        while ((strLine != null) && !linesList.isEmpty()) {
            if (strLine.startsWith("Test-Results für den Kunden")) {
                strLine = checkLineForCustomer(testCustomer, strLine, linesList);
            } else if ((strLine != null) && strLine.startsWith("Test-Results für UserTask")) {
                command = strLine.split("'")[1].trim();
                strLine = addTestResultsForCommand(testCustomer, strLine, linesList);
            } else if ((strLine != null) && strLine.startsWith("Test-Results für Test-Scenario")) {
                String scenarioName = strLine.split("'")[1].trim();
                strLine = addTestResultsForScenrio(testCustomer, command, scenarioName, linesList, testResultsFile);
            }
        }
    }

    private String getNextLine(List<String> linesList) {
        if (linesList.isEmpty()) {
            return null;
        }
        if (linesList.get(0).isBlank()) {
            linesList.remove(0);
        }
        return linesList.remove(0).trim();
    }

    private String addTestResultsForScenrio(TestCustomer testCustomer, String command, String scenarioName, List<String> linesList, File testResultsFile) throws Exception {
        String strLine = getNextLine(linesList);
        TestScenario testScenario = testCustomer.getTestScenariosMap().get(scenarioName);
        if (testScenario == null) {
            testScenario = new TestScenario(testCustomer, scenarioName);
            testCustomer.addTestScenario(testScenario);
        }
        while ((strLine != null) && !strLine.startsWith("Test-Results für den Kunden") && !strLine.startsWith("Test-Results für UserTask") && !strLine.startsWith("Test-Results für Test-Scenario")) {
            TestResults.ResultInfo resultInfo = new TestResults.ResultInfo(strLine);
            testScenario.addResultInfo(command, resultInfo);
            if (strLine.startsWith("Unterschiede beim Testfall")) {
                strLine = addTestResultsForDiffInfo(resultInfo, strLine, linesList, testResultsFile);
            } else {
                strLine = getNextLine(linesList);
            }
        }
        return strLine;
    }

    private String addTestResultsForDiffInfo(TestResults.ResultInfo resultInfo, String strLine, List<String> linesList, File testResultsFile) throws Exception {
        TestResults.DiffenrenceInfo diffenrenceInfo = paserseDiffFromErrorString(strLine, testResultsFile);
        if (diffenrenceInfo != null) {
            resultInfo.addDifferences(diffenrenceInfo);
        }
        strLine = getNextLine(linesList);
        return strLine;
    }

    protected TestResults.DiffenrenceInfo paserseDiffFromErrorString(String errorStr, File testResultsFile) throws Exception {
        String strTemp1 = testResultsFile.getAbsolutePath().split(TestSupportClientKonstanten.CHECKED)[0];
        String strTemp2 = errorStr.split(TestSupportClientKonstanten.CHECKED)[1];
        File diffFile = new File(new File(strTemp1, TestSupportClientKonstanten.CHECKED), strTemp2);

        String[] splitMain = errorStr.split("'");
        String[] splitTestfall = splitMain[1].split(":");
        String testFallName = splitTestfall[0];
        // Original nutzt commons-lang StringUtils.split(str, "[]"); JDK-split
        // auf Charklasse [\\[\\]] liefert dasselbe Ergebnis fuer den hier
        // erwarteten Input "<crefo1>[<crefo2>]".
        String[] splitCrefos = splitTestfall[1].split("[\\[\\]]");

        File xmlFileSrc = buildXmlFileFor(diffFile, testFallName, splitCrefos[0], "REF-EXPORTS");
        File xmlFileDst = buildXmlFileFor(diffFile, testFallName, splitCrefos[0], "RESTORED-COLLECTS");

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        TextFileComparator textFileComparator = new TextFileComparator();
        List<Difference> differenceList = textFileComparator.compareContent(
                xmlFileSrc.getName(),
                FileUtils.readFileToString(xmlFileSrc),
                FileUtils.readFileToString(xmlFileDst),
                null);
        return new TestResults.DiffenrenceInfo(testFallName, xmlFileSrc, xmlFileDst, diffFile, differenceList);
    }

    private File buildXmlFileFor(File diffFile, String testFallName, String strCrefo, String subDirName) {
        File file = new File(diffFile.getParentFile(), subDirName);
        Collection<File> fileCollection = FileUtils.listFiles(file, new String[]{"xml"}, false);
        List<File> collected = fileCollection.stream().filter(xmlFile -> xmlFile.getName().contains(testFallName) && xmlFile.getName().contains(strCrefo)).collect(Collectors.toList());
        if (collected != null && !collected.isEmpty()) {
            return collected.get(0);
        }
        throw new IllegalStateException("Crefo-File nicht gefunden!");
    }

    private String checkLineForCustomer(TestCustomer testCustomer, String strLine, List<String> linesList) {
        String[] split = strLine.split("'");
        String customerKey = split[1].trim();
        if (!testCustomer.getCustomerKey().equalsIgnoreCase(customerKey)) {
            throw new IllegalStateException("Customer-Key '" + customerKey + "' passt nicht dem übergebenen TestCustomer '" + testCustomer + "'!");
        }
        strLine = getNextLine(linesList);
        return strLine;
    }

    private String addTestResultsForCommand(TestCustomer testCustomer, String strLine, List<String> linesList) {
        String command = strLine.split("'")[1].trim();
        testCustomer.addTestResultsForCommand(command);
        strLine = getNextLine(linesList);
        StringBuilder errorBuilder = new StringBuilder();
        while ((strLine != null) && !strLine.startsWith("Test-Results für den Kunden") && !strLine.startsWith("Test-Results für UserTask") && !strLine.startsWith("Test-Results für Test-Scenario")) {
            errorBuilder.append(strLine.trim() + "\n");
            strLine = getNextLine(linesList);
        }
        if (errorBuilder.length() > 0) {
            TestResults.ResultInfo resultInfo = new TestResults.ResultInfo(errorBuilder.toString());
            testCustomer.addResultInfo(command, resultInfo);
        }
        return strLine;
    }

}
