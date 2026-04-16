package de.creditreform.crefoteam.cte.testsupporttool.resume;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Properties;

/**
 * Persistenz-Snapshot fuer einen abgebrochenen Prozess. Enthaelt den Index-
 * Pfad des zuletzt gestarteten Steps (z.B. {@code [3, 9]} = Phase-2,
 * innerer Step 9) plus Metadaten fuer den Fortsetzen-Dialog.
 *
 * <p>Persistiert als {@link Properties}-Datei unter
 * {@code <testOutputsRoot>/resume.properties}, umgebungsspezifisch.
 *
 * <p>Was <b>nicht</b> persistiert wird: die gesamte Task-Variables-Map. Die
 * wird beim Resume-Restart frisch aus dem aktuellen GUI-Zustand +
 * {@link de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig} gebaut;
 * nur der Resume-Index-Pfad wird als Skip-Marker reingereicht.
 */
public final class ResumeState {

    public static final String FILE_NAME = "resume.properties";

    private static final String KEY_INDEX_PATH = "indexPath";
    private static final String KEY_SAVED_AT = "savedAt";
    private static final String KEY_LAST_STEP_NAME = "lastStepName";
    private static final String KEY_TEST_PHASE = "testPhase";

    private final int[] indexPath;
    private final String savedAt;
    private final String lastStepName;
    private final String testPhase;

    public ResumeState(int[] indexPath, String lastStepName, String testPhase) {
        this.indexPath = indexPath;
        this.savedAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.lastStepName = lastStepName;
        this.testPhase = testPhase;
    }

    private ResumeState(int[] indexPath, String savedAt, String lastStepName, String testPhase) {
        this.indexPath = indexPath;
        this.savedAt = savedAt;
        this.lastStepName = lastStepName;
        this.testPhase = testPhase;
    }

    public int[] indexPath() { return indexPath; }
    public String savedAt() { return savedAt; }
    public String lastStepName() { return lastStepName; }
    public String testPhase() { return testPhase; }

    public void save(File file) throws IOException {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        Properties props = new Properties();
        props.setProperty(KEY_INDEX_PATH, indexPathToString(indexPath));
        props.setProperty(KEY_SAVED_AT, savedAt);
        if (lastStepName != null) props.setProperty(KEY_LAST_STEP_NAME, lastStepName);
        if (testPhase != null) props.setProperty(KEY_TEST_PHASE, testPhase);
        try (FileOutputStream out = new FileOutputStream(file)) {
            props.store(out, "Resume-State fuer TestSupport-Prozess");
        }
    }

    public static ResumeState load(File file) throws IOException {
        if (!file.exists()) return null;
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            props.load(in);
        }
        String raw = props.getProperty(KEY_INDEX_PATH);
        if (raw == null || raw.isBlank()) return null;
        int[] idx = indexPathFromString(raw);
        return new ResumeState(
                idx,
                props.getProperty(KEY_SAVED_AT, ""),
                props.getProperty(KEY_LAST_STEP_NAME),
                props.getProperty(KEY_TEST_PHASE));
    }

    public static void delete(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    public static String indexPathToString(int[] path) {
        if (path == null || path.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(path[i]);
        }
        return sb.toString();
    }

    public static int[] indexPathFromString(String s) {
        if (s == null || s.isBlank()) return new int[0];
        String[] parts = s.split(",");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i].trim());
        }
        return result;
    }

    /** Lexikographischer Vergleich. */
    public static int compareIndexPaths(int[] a, int[] b) {
        int n = Math.min(a.length, b.length);
        for (int i = 0; i < n; i++) {
            if (a[i] != b[i]) return Integer.compare(a[i], b[i]);
        }
        return Integer.compare(a.length, b.length);
    }

    @Override
    public String toString() {
        return "ResumeState{path=" + Arrays.toString(indexPath)
                + ", phase=" + testPhase
                + ", step=" + lastStepName
                + ", at=" + savedAt + "}";
    }
}
