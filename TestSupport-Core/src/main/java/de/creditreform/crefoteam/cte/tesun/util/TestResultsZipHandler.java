package de.creditreform.crefoteam.cte.tesun.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Hilfsklasse zum Schreiben/Lesen von TestResults-Zips und zum Zippen von
 * Output-Verzeichnissen.
 *
 * <p>Port aus {@code testsupport_client.tesun_util.TestResultsZipHandler}. Der
 * Parser-Teil ({@link CustomerTestResultsParser}) ist schlank portiert — die
 * xmlunit-basierte Diff-Analyse fehlt (siehe dortige CLAUDE_MODE-Notiz).
 */
public class TestResultsZipHandler {

    private final CustomerTestResultsParser customerTestResultsParser;

    public TestResultsZipHandler() {
        this.customerTestResultsParser = new CustomerTestResultsParser();
    }

    /**
     * Zippt das gesamte {@code sourceDir} (rekursiv) in eine Zip-Datei unter
     * {@code zipFile}. Eintraege werden mit dem Verzeichnisnamen von
     * {@code sourceDir} als Prefix abgelegt.
     */
    public static void zipDirectory(Path sourceDir, Path zipFile) throws IOException {
        if (!Files.exists(zipFile.getParent())) {
            Files.createDirectory(zipFile.getParent());
        }
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            Files.walk(sourceDir)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        try {
                            Path pathEntry = Path.of(sourceDir.getFileName().toString(),
                                    sourceDir.relativize(path).toString());
                            ZipEntry zipEntry = new ZipEntry(pathEntry.toString());
                            zipOutputStream.putNextEntry(zipEntry);
                            Files.copy(path, zipOutputStream);
                            zipOutputStream.closeEntry();
                        } catch (IOException e) {
                            System.err.println("Fehler beim Zippen von " + path + ": " + e.getMessage());
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    /**
     * Schreibt fuer jeden {@link TestCustomer} im uebergebenen Map die
     * TestResults-Datei via {@link TestCustomer#dumpResultsToFile()}.
     */
    public void writeTestResultsToFile(Map<String, TestCustomer> testCustomersMap) {
        testCustomersMap.entrySet().forEach(testCustomerEntry -> {
            try {
                TestCustomer testCustomer = testCustomerEntry.getValue();
                testCustomer.dumpResultsToFile();
            } catch (Exception e) {
                throw new RuntimeException("\n!!! Fehler beim Speichern der Test-Results-Datei!\n" + e.getMessage());
            }
        });
    }

    /**
     * Entpackt ein Zip in ein Geschwister-Verzeichnis mit Suffix {@code -UNZIPPED}.
     * Zip-Slip-Schutz per Pfad-Normalisierung.
     */
    public Path unzipRecursive(Path testResultsZipFile) throws IOException {
        Path otuputPath = testResultsZipFile.resolveSibling(testResultsZipFile.getFileName() + "-UNZIPPED");
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(testResultsZipFile.toFile()))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path resolvedPath = otuputPath.resolve(entry.getName()).normalize();
                if (!resolvedPath.startsWith(otuputPath)) {
                    throw new IOException("Entry is outside of target dir: " + entry.getName());
                }
                if (entry.isDirectory()) {
                    Files.createDirectories(resolvedPath);
                } else {
                    Files.createDirectories(resolvedPath.getParent());
                    Files.copy(zis, resolvedPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zis.closeEntry();
            }
        }
        return otuputPath;
    }

    /**
     * Parst {@code <unzipped>/CHECKED/<subDirName>/TestResults.txt} zu einer
     * Customer-Map. Liefert {@code null}, wenn die Datei fehlt.
     */
    public Map<String, TestCustomer> initalizeTestCustomersMapFromDir(Path unzippedPath, String subDirName) throws Exception {
        Path resultsFilePath = unzippedPath.resolve(TestSupportClientKonstanten.CHECKED).resolve(subDirName).resolve("TestResults.txt");
        if (resultsFilePath.toFile().exists()) {
            return customerTestResultsParser.parseTestResultsFile(resultsFilePath.toFile());
        }
        return null;
    }
}
