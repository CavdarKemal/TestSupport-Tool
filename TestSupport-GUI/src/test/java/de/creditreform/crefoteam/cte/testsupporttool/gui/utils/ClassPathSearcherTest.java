package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import static org.assertj.core.api.Assertions.assertThat;

class ClassPathSearcherTest {

    @Test
    void findResourceInDirectory_findsMatchingFilesRecursively(@TempDir Path tmp) throws IOException {
        Path sub = tmp.resolve("sub");
        Files.createDirectories(sub);
        Path a = tmp.resolve("FoobarLookAndFeel.class");
        Path b = sub.resolve("AnotherLookAndFeel.class");
        Path c = tmp.resolve("UnrelatedFile.txt");
        Files.write(a, new byte[]{1});
        Files.write(b, new byte[]{2});
        Files.write(c, new byte[]{3});

        Map<String, InputStream> found = new ClassPathSearcher()
                .findResourceInDirectory(tmp.toFile(), ".*LookAndFeel.class");

        assertThat(found).hasSize(2);
        assertThat(found.keySet())
                .anyMatch(k -> k.endsWith("FoobarLookAndFeel.class"))
                .anyMatch(k -> k.endsWith("AnotherLookAndFeel.class"));
        // Streams schliessen
        for (InputStream is : found.values()) is.close();
    }

    @Test
    void findResourceInFile_returnsEmpty_forNonJar(@TempDir Path tmp) throws IOException {
        Path file = tmp.resolve("not-a-jar.txt");
        Files.write(file, new byte[]{0});
        Map<String, InputStream> found = new ClassPathSearcher()
                .findResourceInFile(file.toFile(), ".*\\.class");
        assertThat(found).isEmpty();
    }

    @Test
    void findResourceInFile_extractsMatchingClassNamesFromJar_skipsAbstract(@TempDir Path tmp) throws IOException {
        File jar = tmp.resolve("custom-laf.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(jar.toPath()), buildManifest())) {
            putEntry(jos, "com/example/MyLookAndFeel.class", new byte[]{1});
            // 'Abstract' im Pfad/Klassennamen wird vom Original ueberspringen.
            putEntry(jos, "com/example/AbstractLookAndFeel.class", new byte[]{2});
        }

        Map<String, InputStream> found = new ClassPathSearcher()
                .findResourceInFile(jar, ".*LookAndFeel.class");

        assertThat(found).hasSize(1);
        assertThat(found).containsKey("com.example.MyLookAndFeel");
        for (InputStream is : found.values()) is.close();
    }

    private static Manifest buildManifest() {
        Manifest m = new Manifest();
        m.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        return m;
    }

    private static void putEntry(JarOutputStream jos, String name, byte[] content) throws IOException {
        JarEntry e = new JarEntry(name);
        jos.putNextEntry(e);
        jos.write(content);
        jos.closeEntry();
    }
}
