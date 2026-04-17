package de.creditreform.crefoteam.cte.tesun.exports_checker;

import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestCrefo;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.tesun.util.TestResults;
import de.creditreform.crefoteam.cte.tesun.util.TestScenario;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.tesun.util.TesunUtilites;
import org.apache.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class CollectsChecker {

    private List<Long> deletedCrefosList;
    private final TesunClientJobListener tesunClientJobListener;
    private final EnvironmentConfig environmentConfig;

    public CollectsChecker(EnvironmentConfig environmentConfig, TesunClientJobListener tesunClientJobListener) {
        this.environmentConfig = environmentConfig;
        this.tesunClientJobListener = tesunClientJobListener;
    }

    public void checkTestCustomerCollects(TestCustomer testCustomer) {
        deletedCrefosList = null;
        notifyTesunClientJobListener(Level.INFO, "\nPrüfe die COLLECTS des Kunden " + testCustomer.getCustomerKey() + "...");
        Map<String, TestScenario> testScenariosMap = testCustomer.getTestScenariosMap();
        testScenariosMap.entrySet().stream().forEach(entry -> {
            TestScenario testScenario = entry.getValue();
            if (testScenario.isActivated()) {
                try {
                    checkTestScenarioCollects(testScenario);
                } catch (Exception ex) {
                    testScenario.addResultInfo(TestSupportClientKonstanten.CHECK_COLLECTS_COMMAND, new TestResults.ResultInfo(ex.getMessage()));
                    notifyTesunClientJobListener(Level.ERROR, "\n" + ex.getMessage());
                }
            }
        });
    }

    public void checkTestScenarioCollects(TestScenario testScenario) throws IOException {
        notifyTesunClientJobListener(Level.INFO, "\n\tPrüfe die COLLECTS des Scenarios '" + testScenario.getScenarioName() + "' für den Kunden '" + testScenario.getTestCustomer().getCustomerKey() + "'...");
        List<File> collectedXmlsFilesList = TesunUtilites.getFilesFromDir(testScenario.getCollectedsFile(), TestSupportClientKonstanten.SUPPORTED_EXPORT_MATCHER);
        List<File> pseudoRefExportsXmlsFilesList = TesunUtilites.getFilesFromDir(testScenario.getPseudoRefExportsFile(), TestSupportClientKonstanten.SUPPORTED_EXPORT_MATCHER);
        deletedCrefosList = null;
        for (TestCrefo testCrefo : testScenario.getTestCrefosAsList()) {
            File xmlFile = findXmlFileForCrefo(collectedXmlsFilesList, testCrefo);
            boolean deletedCrefo = isDeletedCrefo(pseudoRefExportsXmlsFilesList, testCrefo.getPseudoCrefoNr());
            if (testCrefo.getTestFallName().startsWith("p")) {
                checkPositifTestfall(testScenario, testCrefo, xmlFile, deletedCrefo);
            } else if (testCrefo.getTestFallName().startsWith("n")) {
                checkNegativTestfals(testScenario, testCrefo, xmlFile, deletedCrefo);
            } else if (testCrefo.getTestFallName().startsWith("x")) {
                checkXFall(testScenario, testCrefo, xmlFile, deletedCrefo);
            }
        }
    }

    private void checkXFall(TestScenario testScenario, TestCrefo testCrefo, File xmlFile, boolean deletedCrefo) {
        if (xmlFile == null) {
            String strInfo = String.format("Für den Testfall %s wurde kein Löschsatz exportiert!", testCrefo);
            testScenario.addResultInfo(TestSupportClientKonstanten.CHECK_COLLECTS_COMMAND, new TestResults.ResultInfo(testCrefo.getPseudoCrefoNr(), strInfo));
            notifyTesunClientJobListener(Level.ERROR, "\n\t\t" + strInfo);
        } else {
            if (deletedCrefo) {
                notifyTesunClientJobListener(Level.INFO, "\n\t\t" + String.format("Für den Testfall %s wurde ERWARTUNGSGEMÄß ein Löschsatz '%s' exportiert.", testCrefo, xmlFile.getName()));
            } else {
                String strInfo = String.format("Für den Testfall %s wurde UNERWARTETERWEISE ein Exportsatz '%s' exportiert!", testCrefo, xmlFile.getName());
                testScenario.addResultInfo(TestSupportClientKonstanten.CHECK_COLLECTS_COMMAND, new TestResults.ResultInfo(testCrefo.getPseudoCrefoNr(), strInfo));
                notifyTesunClientJobListener(Level.ERROR, "\n\t\t" + strInfo);
            }
        }
    }

    private void checkNegativTestfals(TestScenario testScenario, TestCrefo testCrefo, File xmlFile, boolean deletedCrefo) {
        if (xmlFile == null) {
            notifyTesunClientJobListener(Level.INFO, "\n\t\t" + String.format("Für den Testfall %s wurde ERWARTUNGSGEMÄß nichts exportiert.", testCrefo));
        } else {
            if (deletedCrefo) {
                notifyTesunClientJobListener(Level.INFO, "\n\t\t" + String.format("Für den Testfall %s wurde ERWARTUNGSGEMÄß ein Löschsatz '%s' exportiert.", testCrefo, xmlFile.getName()));
            } else {
                String strInfo = String.format("Für den Testfall %s wurde UNERWARTETERWEISE ein Exportsatz '%s' exportiert!", testCrefo, xmlFile.getName());
                testScenario.addResultInfo(TestSupportClientKonstanten.CHECK_COLLECTS_COMMAND, new TestResults.ResultInfo(testCrefo.getPseudoCrefoNr(), strInfo));
                notifyTesunClientJobListener(Level.ERROR, "\n\t\t" + strInfo);
            }
        }
    }

    private void checkPositifTestfall(TestScenario testScenario, TestCrefo testCrefo, File xmlFile, boolean deletedCrefo) {
        if (xmlFile == null) {
            String strInfo = String.format("!Testfall %s MÜSSTE exportiert werden!", testCrefo);
            testScenario.addResultInfo(TestSupportClientKonstanten.CHECK_COLLECTS_COMMAND, new TestResults.ResultInfo(testCrefo.getPseudoCrefoNr(), strInfo));
            notifyTesunClientJobListener(Level.ERROR, "\n\t\t" + strInfo);
        } else {
            if (deletedCrefo && !testCrefo.getTestFallName().startsWith("p")) {
                String strInfo = String.format("!Für den Testfall %s wurde UNERWARTETERWEISE ein Löschsatz '%s' exportiert!", testCrefo, xmlFile.getName());
                testScenario.addResultInfo(TestSupportClientKonstanten.CHECK_COLLECTS_COMMAND, new TestResults.ResultInfo(testCrefo.getPseudoCrefoNr(), strInfo));
                notifyTesunClientJobListener(Level.ERROR, "\n\t\t" + strInfo);
            } else {
                notifyTesunClientJobListener(Level.INFO, "\n\t\t" + String.format("Für den Testfall %s wurde Exportsatz '%s' exportiert.", testCrefo, xmlFile.getName()));
            }
        }
    }

    protected File findXmlFileForCrefo(List<File> xmlsFilesList, TestCrefo testCrefo) {
        for (File xmlFile : xmlsFilesList) {
            Long pseudoCrefoNr = testCrefo.getPseudoCrefoNr();
            String xmlFileName = xmlFile.getName();
            boolean crefoOK = xmlFileName.contains(pseudoCrefoNr + ".");
            if (crefoOK) {
                if (xmlFileName.contains("loesch") || xmlFileName.contains("delete") || xmlFileName.contains("stopmessage")) {
                    if (testCrefo.getTestFallName().startsWith("x")) {
                        return xmlFile;
                    }
                } else {
                    if (testCrefo.getTestFallName().startsWith("p") || testCrefo.getTestFallName().startsWith("n")) {
                        return xmlFile;
                    }
                }
            }
        }
        return null;
    }

    protected List<Long> getDeletedCrefosList(List<File> xmlsFilesList) {
        if (deletedCrefosList == null) {
            deletedCrefosList = new ArrayList<>();
            for (File xmlFile : xmlsFilesList) {
                String xmlName = xmlFile.getName();
                for (String prefix : TestSupportClientKonstanten.LOESCHSATZ_FILENAMES_PREFIX) {
                    if (xmlName.contains(prefix)) {
                        Matcher matcher = TestSupportClientKonstanten.CREFO_NUMBER_PATTERN.matcher(xmlName);
                        if (matcher.find()) {
                            String crefoNr = matcher.group(0);
                            deletedCrefosList.add(Long.valueOf(crefoNr.substring(0, 10)));
                        }
                    }
                }
            }
        }
        return deletedCrefosList;
    }

    protected boolean isDeletedCrefo(List<File> xmlsFilesList, Long crefo) {
        return getDeletedCrefosList(xmlsFilesList).contains(crefo);
    }

    protected void notifyTesunClientJobListener(Level level, String strInfo) {
        if (tesunClientJobListener != null) {
            tesunClientJobListener.notifyClientJob(level, strInfo);
        }
    }
}
