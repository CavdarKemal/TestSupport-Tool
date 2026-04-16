package de.creditreform.crefoteam.cte.tesun.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class TestResultsZipHandlerTest {

    @Test
    void writeTestResultsToFile_emptyMap_isNoOp() {
        TestResultsZipHandler handler = new TestResultsZipHandler();
        Map<String, TestCustomer> empty = new HashMap<>();

        // Leere Map -> keinerlei IO, keine Exception
        assertThatCode(() -> handler.writeTestResultsToFile(empty))
                .doesNotThrowAnyException();
    }

    @Test
    void zipDirectory_writesAllRegularFiles_underSourceDirNameAsPrefix(@TempDir Path tmp) throws IOException {
        Path src = tmp.resolve("payload");
        Files.createDirectories(src.resolve("sub"));
        Files.writeString(src.resolve("a.txt"), "AAA");
        Files.writeString(src.resolve("sub").resolve("b.txt"), "BBB");

        Path zipFile = tmp.resolve("out").resolve("payload.zip");

        TestResultsZipHandler.zipDirectory(src, zipFile);

        assertThat(zipFile).exists();
        try (ZipFile zf = new ZipFile(zipFile.toFile())) {
            var entries = zf.stream().map(ZipEntry::getName).collect(toList());
            // Eintraege werden mit dem Source-Verzeichnisnamen als Prefix abgelegt;
            // das tatsaechliche Trennzeichen haengt vom OS ab.
            assertThat(entries)
                    .anyMatch(n -> n.endsWith("a.txt") && n.startsWith("payload"))
                    .anyMatch(n -> n.endsWith("b.txt") && n.contains("sub"));
        }
    }
}
