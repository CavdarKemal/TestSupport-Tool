package de.creditreform.crefoteam.cte.testsupporttool.util;

import de.creditreform.crefoteam.cte.tesun.util.TesunDateUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    /**
     * Fügt eine einzelne Datei einem ZIP-Archiv hinzu. Port aus
     * {@code ActivitiTestSupport#addFileToArchive}.
     */
    public static void addFileToArchive(ZipOutputStream zipOutputStream, File parentFile, File srcFile) throws IOException {
        byte[] buffer = new byte[1024];
        String absolutePath = srcFile.getAbsolutePath();
        String entryName = absolutePath.substring(parentFile.getAbsolutePath().length() + 1);
        zipOutputStream.putNextEntry(new ZipEntry(entryName));
        try (FileInputStream fileInputStream = new FileInputStream(srcFile)) {
            int length;
            while ((length = fileInputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, length);
            }
        }
        zipOutputStream.closeEntry();
    }

    /**
     * Fügt rekursiv ein Verzeichnis einem ZIP-Archiv hinzu. Port aus
     * {@code ActivitiTestSupport#addDirToArchive}.
     *
     * @param warnLogger wird mit Warn-Text aufgerufen, wenn ein leeres
     *                   Unterverzeichnis gefunden wird (kann {@code null} sein)
     */
    public static void addDirToArchive(ZipOutputStream zipOutputStream, File parentFile, File srcFile,
                                       Consumer<String> warnLogger) throws IOException {
        File[] files = srcFile.listFiles();
        if (files == null || files.length < 1) {
            if (warnLogger != null) {
                warnLogger.accept(String.format("\t! Das Verzeichnis %s ist leer!", srcFile.getAbsolutePath()));
            }
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                addDirToArchive(zipOutputStream, parentFile, file, warnLogger);
                continue;
            }
            addFileToArchive(zipOutputStream, parentFile, file);
        }
    }

    /**
     * Erzeugt eine ZIP-Datei aus {@code outputsRoot} und {@code itsqRefExportsRoot}.
     * Port aus {@code ActivitiTestSupport#zipOutputDirectory}. Der ZIP-Dateiname
     * folgt dem Original-Schema {@code Automated-Test-<env>-<yyyy_MM_dd_HH_mm_ss>}
     * und landet im Elternverzeichnis von {@code outputsRoot}.
     *
     * @return absoluter Pfad der erzeugten ZIP-Datei oder {@code null} bei Fehler
     */
    public static String zipOutputDirectory(File outputsRoot, File itsqRefExportsRoot, String envName,
                                            Consumer<String> infoLogger, Consumer<String> warnLogger,
                                            Consumer<String> errorLogger) {
        try {
            String strDateTime = TesunDateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS
                    .format(Calendar.getInstance().getTime()).replaceAll(":", "_");
            File zipFile = new File(outputsRoot.getParent(), "Automated-Test-" + envName + "-" + strDateTime + ".zip");
            if (infoLogger != null) {
                infoLogger.accept("Das Verzeichnis '" + outputsRoot.getAbsolutePath() + "' wird gezippt...");
            }
            try (FileOutputStream fos = new FileOutputStream(zipFile);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {
                addDirToArchive(zos, outputsRoot.getParentFile(), outputsRoot, warnLogger);
                addDirToArchive(zos, itsqRefExportsRoot.getParentFile(), itsqRefExportsRoot, warnLogger);
            }
            return zipFile.getAbsolutePath();
        } catch (Exception ex) {
            if (errorLogger != null) {
                String exMsg = ex.getMessage();
                errorLogger.accept(exMsg != null ? exMsg : ex.getClass().getSimpleName());
            }
            return null;
        }
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
