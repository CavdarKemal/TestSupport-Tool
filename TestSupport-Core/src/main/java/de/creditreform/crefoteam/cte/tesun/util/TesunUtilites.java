package de.creditreform.crefoteam.cte.tesun.util;

import java.io.File;

/**
 * Teilport von {@code testsupport_client.tesun_util.TesunUtilites}.
 *
 * <p>Portiert ist derzeit nur {@link #shortPath(String, int)} /
 * {@link #shortPath(File, int)} — gebraucht von der GUI (Test-Result-Diffs).
 * Weitere Methoden (Email-Versand, FutureTask-Waiter, Directory-Scanner …)
 * werden nachgezogen, wenn die entsprechenden Consumer portiert sind;
 * Platzhalter dafür in {@code CLAUDE_MODE}-Markierung unten.
 */
public final class TesunUtilites {

    private TesunUtilites() { }

    public static String shortPath(String thePath, int maxLen) {
        return shortPath(new File(thePath), maxLen);
    }

    public static String shortPath(File theFile, int maxLen) {
        String absolutePath = theFile.getAbsolutePath();
        if (absolutePath.length() < maxLen) {
            return theFile.getAbsolutePath();
        }
        File rootFile = new File(absolutePath);
        while (rootFile.getParentFile().getParentFile() != null) {
            rootFile = rootFile.getParentFile();
        }
        int rootLen = rootFile.getAbsolutePath().length();
        while (absolutePath.length() >= (rootLen + maxLen)) {
            int fistSlashPos = absolutePath.indexOf(File.separator, 3);
            if (fistSlashPos < 0) {
                break;
            }
            absolutePath = absolutePath.substring(fistSlashPos);
        }
        if (!absolutePath.startsWith(rootFile.getPath())) {
            absolutePath = rootFile + File.separator + "..." + absolutePath;
        }
        return absolutePath;
    }

    /* CLAUDE_MODE
     * Nicht portiert — folgen bei Bedarf:
     *   public static void sendEmail(...)                 // SMTP-Versand
     *   public static Long extractCrefonummerFromString(...)
     *   public static List<File> getFilesFromDir(...)
     *   public static void waitForFutureTasks(...)
     *   ... (siehe Original testsupport_client.tesun_util.TesunUtilites)
     */
}
