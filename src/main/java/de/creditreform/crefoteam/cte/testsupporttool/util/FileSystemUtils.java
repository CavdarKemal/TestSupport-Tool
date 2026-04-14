package de.creditreform.crefoteam.cte.testsupporttool.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dateisystem-Hilfsmethoden. Gezogen aus {@code TesunUtilites} des
 * Original-Projekts (nur die Datei-bezogenen Methoden — XML-, Exception-
 * und Customer-Helfer liegen in eigenen Klassen).
 */
public final class FileSystemUtils {

    private FileSystemUtils() { }

    public static String shortPath(String thePath, int maxLen) {
        return shortPath(new File(thePath), maxLen);
    }

    public static String shortPath(File theFile, int maxLen) {
        String absolutePath = theFile.getAbsolutePath();
        if (absolutePath.length() < maxLen) {
            return theFile.getAbsolutePath();
        }
        File rootFile = new File(absolutePath);
        while (rootFile.getParentFile() != null && rootFile.getParentFile().getParentFile() != null) {
            rootFile = rootFile.getParentFile();
        }
        int rootLen = rootFile.getAbsolutePath().length();
        while (absolutePath.length() >= (rootLen + maxLen)) {
            int firstSlashPos = absolutePath.indexOf(File.separator, 3);
            if (firstSlashPos < 0) {
                break;
            }
            absolutePath = absolutePath.substring(firstSlashPos);
        }
        if (!absolutePath.startsWith(rootFile.getPath())) {
            absolutePath = rootFile + File.separator + "..." + absolutePath;
        }
        return absolutePath;
    }

    public static List<File> getFilesFromDir(File theRoot, String regExp) {
        File[] files = null;
        if (theRoot.exists()) {
            files = theRoot.listFiles((dir, fileName) -> {
                boolean matches = fileName.endsWith(regExp);
                if (!matches) {
                    matches = fileName.matches(regExp);
                }
                return matches;
            });
        }
        List<File> filesFromDir = new ArrayList<>();
        if (files != null) {
            Collections.addAll(filesFromDir, files);
        }
        filesFromDir.sort((a, b) -> a.getPath().compareTo(b.getPath()));
        return filesFromDir;
    }

    public static String checkAndCreateDirectoryX(File theFile) {
        if (!theFile.exists() && !theFile.mkdirs()) {
            throw new RuntimeException(String.format(
                    "Das Verzeichnis %s konnte nicht angelegt werden!", theFile.getAbsolutePath()));
        }
        return theFile.getAbsolutePath();
    }

    public static String checkAndCreateDirectory(File phaseFile, boolean create) throws IOException {
        if (create) {
            if (phaseFile.exists()) {
                File destDir = new File(phaseFile + "-" + System.nanoTime());
                FileUtils.moveDirectory(phaseFile, destDir);
            }
            if (!phaseFile.mkdirs()) {
                throw new RuntimeException(String.format(
                        "Das Verzeichnis %s konnte nicht angelegt werden!", phaseFile.getAbsolutePath()));
            }
        } else if (!phaseFile.exists()) {
            throw new RuntimeException(String.format(
                    "Das Verzeichnis %s existiert nicht!", phaseFile.getAbsolutePath()));
        }
        return phaseFile.getAbsolutePath();
    }
}
