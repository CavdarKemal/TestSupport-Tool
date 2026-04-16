package de.creditreform.crefoteam.cte.tesun.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Hilfsklasse zum Schreiben von TestResults-Dateien und Zippen von
 * Output-Verzeichnissen.
 *
 * <p>Minimal-Port aus {@code testsupport_client.tesun_util.TestResultsZipHandler}.
 * Die parser-basierten Methoden des Originals ({@code unzipRecursive},
 * {@code initalizeTestCustomersMapFromDir}) sind hier <b>bewusst nicht</b>
 * portiert — sie wuerden den {@code CustomerTestResultsParser} und damit
 * eine {@code xmlunit}-Dependency mitschleifen. Ergaenzen, sobald ein
 * Aufrufer sie konkret braucht.
 */
public class TestResultsZipHandler {

    /**
     * Zippt das gesamte {@code sourceDir} (rekursiv) in eine Zip-Datei
     * unter {@code zipFile}. Eintraege werden mit dem Verzeichnisnamen
     * von {@code sourceDir} als Prefix abgelegt.
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
                            // Relativer Pfad in der Zip-Datei.
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
}
