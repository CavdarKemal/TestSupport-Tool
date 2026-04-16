package de.creditreform.crefoteam.cte.testsupporttool.util;

import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.tesun.util.TestScenario;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Hilfsmethoden rund um {@link TestCustomer}. Gezogen aus
 * {@code TesunUtilites} des Original-Projekts —
 * {@code handleCustomersFiles}, {@code copyFilesForCustomer} und
 * {@code dumpCustomers}.
 */
public final class CustomerUtils {

    private CustomerUtils() { }

    public static List<File> handleCustomersFiles(Map<String, TestCustomer> testCustomersMap,
                                                  String dstPath,
                                                  String[] extensionsList,
                                                  boolean throwException) throws IOException {
        List<File> copiedFilesList = new ArrayList<>();
        for (Map.Entry<String, TestCustomer> entry : testCustomersMap.entrySet()) {
            TestCustomer testCustomer = entry.getValue();
            for (String extension : extensionsList) {
                File targetDir = new File(dstPath, testCustomer.getCustomerKey().toLowerCase());
                List<File> copiedFilesForCustomer = copyFilesForCustomer(testCustomer, targetDir, extension);
                if (throwException && copiedFilesForCustomer.isEmpty()) {
                    throw new FileNotFoundException(String.format(
                            "Das Verzeichnis \n\t%s\nenthält keine %s- Dateien!",
                            testCustomer.getItsqRefExportsDir(), extension));
                }
                copiedFilesList.addAll(copiedFilesForCustomer);
            }
        }
        return copiedFilesList;
    }

    public static List<File> copyFilesForCustomer(TestCustomer testCustomer, File customerTargetDir, String extension) throws IOException {
        List<File> copiedFilesList = new ArrayList<>();
        FileUtils.copyDirectory(testCustomer.getItsqRefExportsDir(), customerTargetDir, theFile -> {
            String name = theFile.getName();
            if (theFile.isDirectory()) {
                TestScenario scenario = testCustomer.getScenario(name);
                return scenario != null && scenario.isActivated();
            }
            boolean endsWith = name.endsWith(extension);
            if (endsWith) {
                copiedFilesList.add(theFile);
            }
            return endsWith;
        });
        return copiedFilesList;
    }

    /**
     * Aggregiert {@link TestCustomer#dumpResults} über alle Kunden einer Phase.
     * Port aus {@code TestFallFileUtil.dumAllCustomers} — nur Kunden mit
     * nicht-leerem Dump landen im Ergebnis.
     */
    public static StringBuilder dumpAllCustomersResults(Map<String, TestCustomer> testCustomerMap) {
        StringBuilder stringBuilderAll = new StringBuilder();
        testCustomerMap.values().forEach(testCustomer -> {
            StringBuilder stringBuilder = new StringBuilder();
            testCustomer.dumpResults(stringBuilder, "\n");
            if (stringBuilder.length() > 0) {
                stringBuilderAll.append(stringBuilder);
            }
        });
        return stringBuilderAll;
    }

    public static void dumpCustomers(File logsDir, String prefix, Map<String, TestCustomer> customersMap) throws IOException {
        StringBuilder sb = new StringBuilder(prefix);
        customersMap.values().forEach(testCustomer -> {
            if (!testCustomer.isActivated()) return;
            sb.append("\n\t\t").append(testCustomer.getCustomerKey());
            sb.append("\n\t\tJVM-Name:").append(testCustomer.getJvmName());
            sb.append("\n\t\t\tProcess-Identifier:").append(testCustomer.getProcessIdentifier());
            sb.append("\n\t\t\tExport-Jobname:").append(testCustomer.getExportJobName());
            sb.append("\n\t\t\tExport-URL:").append(testCustomer.getExportUrl());
            sb.append("\n\t\t\tUpload-Jobname:").append(testCustomer.getUploadJobName());
            sb.append("\n\t\t\tUpload-URL:").append(testCustomer.getUploadUrl());
            sb.append("\n\t\t\tITSQ-AB30Xmls-Dir:").append(testCustomer.getItsqAB30XmlsDir());
            sb.append("\n\t\t\tITSQ-RefExports-Dir:").append(testCustomer.getItsqRefExportsDir());
            sb.append("\n\t\t\tPseudo-RefExports-Dir:").append(testCustomer.getPseudoRefExportsDir());
            sb.append("\n\t\t\tCollects-Dir:").append(testCustomer.getCollectedsDir());
            sb.append("\n\t\t\tRestored-Collects-Dir:").append(testCustomer.getRestoredCollectedsDir());
            sb.append("\n\t\t\tChecks-Dir:").append(testCustomer.getChecksDir());
            sb.append("\n\t\t\tSFTP-Uploads-Dir:").append(testCustomer.getSftpUploadsDir());
            sb.append("\n\t\t\tFW-Aktualisierung:").append(testCustomer.getFwAktualisierungsdatum());
            sb.append("\n\t\t\tPD-Version:").append(testCustomer.getPdVersion());
            sb.append("\n\t\t\tProperties:");
            testCustomer.getPropertyPairsList().forEach(pair ->
                    sb.append("\n\t\t\t\t").append(pair.getLeft()).append("=").append(pair.getRight()));
        });
        File file = new File(logsDir, "Dump-" + prefix + ".txt");
        FileUtils.writeStringToFile(file, sb.toString(), StandardCharsets.UTF_8);
    }
}
