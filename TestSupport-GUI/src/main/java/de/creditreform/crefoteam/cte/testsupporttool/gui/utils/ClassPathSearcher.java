package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 1:1-Port aus {@code testsupport_client.tesun.gui.utils.ClassPathSearcher}.
 *
 * <p>Durchsucht den aktuellen Java-Classpath (Verzeichnisse + JAR-Dateien)
 * nach Resources, die einem Regex-Pattern entsprechen. Wird vor allem von
 * {@link #buildLookAndFeelInfosList(boolean)} genutzt, um installierte
 * Look&Feel-Implementierungen zur Laufzeit zu entdecken.
 */
public class ClassPathSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathSearcher.class);

    public Map<String, InputStream> findFilesInClassPath(String fileNamePattern) {
        LOGGER.debug("findFilesInClassPath():: Durchsuche Classpath für Resources '{}'...\n", fileNamePattern);
        Map<String, InputStream> result = new TreeMap<>();
        String classPath = System.getProperty("java.class.path");
        String[] pathElements = classPath.split(System.getProperty("path.separator"));
        Map<String, InputStream> resourcesInDirectory;
        for (String element : pathElements) {
            try {
                File newFile = new File(element);
                if (newFile.isDirectory()) {
                    resourcesInDirectory = findResourceInDirectory(newFile, fileNamePattern);
                } else {
                    resourcesInDirectory = findResourceInFile(newFile, fileNamePattern);
                }
                result.putAll(resourcesInDirectory);
            } catch (IOException ex) {
                new RuntimeException(ex);
            }
        }
        LOGGER.debug("\tAnzahl gefundene Resourcen: {}\n", result.size());
        return result;
    }

    public Map<String, InputStream> findResourceInDirectory(File directory, String fileNamePattern) throws IOException {
        LOGGER.debug("findResourceInDirectory():: Durchsuche in '{}' nach '{}'...\n", directory.getAbsolutePath(), fileNamePattern);
        Map<String, InputStream> result = new TreeMap<>();
        File[] files = directory.listFiles();
        for (File currentFile : files) {
            if (currentFile.getAbsolutePath().matches(fileNamePattern)) {
                result.put(currentFile.getAbsolutePath(), new FileInputStream(currentFile));
            } else if (currentFile.isDirectory()) {
                result.putAll(findResourceInDirectory(currentFile, fileNamePattern));
            } else {
                result.putAll(findResourceInFile(currentFile, fileNamePattern));
            }
        }
        return result;
    }

    public Map<String, InputStream> findResourceInFile(File resourceFile, String fileNamePattern) throws IOException {
        LOGGER.debug("findResourceInFile():: Suche in '{}' nach '{}'...\n", resourceFile.getAbsolutePath(), fileNamePattern);
        Map<String, InputStream> result = new TreeMap<>();
        if (resourceFile.canRead() && resourceFile.getAbsolutePath().endsWith(".jar")) {
            JarFile jarFile = new JarFile(resourceFile);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry singleEntry = entries.nextElement();
                if (singleEntry.getName().matches(fileNamePattern)) {
                    String className = singleEntry.getName().replace("/", ".").replace(".class", "");
                    if (!result.containsKey(className) && !className.contains("Abstract")) {
                        System.out.println(className);
                        InputStream inputStream = jarFile.getInputStream(singleEntry);
                        result.put(className, inputStream);
                    }
                }
            }
            jarFile.close();
        }
        LOGGER.debug("\tAnzahl gefundene Dateien: {}\n", result.size());
        return result;
    }

    public List<UIManager.LookAndFeelInfo> buildLookAndFeelInfosList(boolean inlcuedDefaults) {
        List<UIManager.LookAndFeelInfo> lookAndFeelInfosList = new ArrayList<>();
        ClassPathSearcher searcher = new ClassPathSearcher();
        Map<String, InputStream> lookAndFeelFiles = searcher.findFilesInClassPath(".*LookAndFeel.class");
        Iterator<String> iterator = lookAndFeelFiles.keySet().iterator();
        while (iterator.hasNext()) {
            String className = iterator.next();
            try {
                Class calzz = Class.forName(className);
                String simpleName = calzz.getSimpleName();
                UIManager.LookAndFeelInfo tmpLookAndFeelInfo = new UIManager.LookAndFeelInfo(simpleName, className);
                lookAndFeelInfosList.add(tmpLookAndFeelInfo);
            } catch (ClassNotFoundException ex) {
                new RuntimeException(ex);
            }
        }
        if (inlcuedDefaults && (lookAndFeelInfosList.size() < 1)) {
            UIManager.LookAndFeelInfo[] lookAndFeelInfos = UIManager.getInstalledLookAndFeels();
            Collections.addAll(lookAndFeelInfosList, lookAndFeelInfos);
        }
        return lookAndFeelInfosList;
    }

}
