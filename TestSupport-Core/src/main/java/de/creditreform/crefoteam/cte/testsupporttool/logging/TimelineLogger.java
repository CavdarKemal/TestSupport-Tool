package de.creditreform.crefoteam.cte.testsupporttool.logging;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Zentrales Logging-Utility — kombiniert Standard-Logging und
 * Timeline-/Performance-Tracking.
 *
 * <p>Adaption der gleichnamigen Klasse aus
 * {@code testsupport_client.tesun_util}. API ist kompatibel; intern auf den
 * Spike-Bedarf reduziert (kein {@code log4j.properties}-Lookup, fester
 * Application-Package, kein dynamisches Reload).
 *
 * <h3>Konfiguration:</h3>
 * <pre>{@code
 * TimelineLogger.configure(new File("logs/ENE"), "app.log", "actions.log");
 * // ... Arbeit ...
 * TimelineLogger.close();
 * }</pre>
 *
 * <h3>Standard-Logging:</h3>
 * <pre>{@code
 * TimelineLogger.info(MyClass.class, "Verarbeite {} Einträge", count);
 * TimelineLogger.error(MyClass.class, "Fehler", exception);
 * }</pre>
 *
 * <h3>Timeline-Tracking:</h3>
 * <pre>{@code
 * try (TimelineLogger.Action a = TimelineLogger.action("processFile")) {
 *     // ... Arbeit ...
 *     a.result("5 records");
 * }
 * }</pre>
 */
public final class TimelineLogger {

    private static final String APP_PACKAGE = "de.creditreform.crefoteam.cte.testsupporttool";

    private static final String TIMELINE_LOGGER_NAME = "TIMELINE";
    private static final String TIMELINE_APPENDER_NAME = "TimelineAppender";
    private static final String APP_APPENDER_NAME = "AppFileAppender";
    private static final String TIMELINE_PATTERN = "%d{dd.MM.yyyy HH:mm:ss.SSS} | %m%n";
    private static final String APP_PATTERN = "%d{dd.MM.yyyy HH:mm:ss.SSS} [%-5p] %c - %m%n";

    private static final Logger TIMELINE = LoggerFactory.getLogger(TIMELINE_LOGGER_NAME);
    private static final org.apache.log4j.Logger LOG4J_TIMELINE = org.apache.log4j.Logger.getLogger(TIMELINE_LOGGER_NAME);

    private static final Map<Class<?>, Logger> loggerCache = new ConcurrentHashMap<>();
    private static final Map<String, ActionInfo> activeActions = new ConcurrentHashMap<>();

    private static RollingFileAppender timelineAppender;
    private static RollingFileAppender appAppender;
    private static long actionCounter = 0;

    static {
        LOG4J_TIMELINE.setAdditivity(false);
    }

    private TimelineLogger() { }

    // ====================== Standard-Logging ======================

    public static Logger getLogger(Class<?> clazz) {
        return loggerCache.computeIfAbsent(clazz, LoggerFactory::getLogger);
    }

    public static void trace(Class<?> clazz, String message, Object... args) { getLogger(clazz).trace(message, args); }
    public static void debug(Class<?> clazz, String message, Object... args) { getLogger(clazz).debug(message, args); }
    public static void info(Class<?>  clazz, String message, Object... args) { getLogger(clazz).info(message, args); }
    public static void warn(Class<?>  clazz, String message, Object... args) { getLogger(clazz).warn(message, args); }
    public static void warn(Class<?>  clazz, String message, Throwable t)    { getLogger(clazz).warn(message, t); }
    public static void error(Class<?> clazz, String message, Object... args) { getLogger(clazz).error(message, args); }
    public static void error(Class<?> clazz, String message, Throwable t)    { getLogger(clazz).error(message, t); }

    public static boolean isDebugEnabled(Class<?> clazz) { return getLogger(clazz).isDebugEnabled(); }
    public static boolean isTraceEnabled(Class<?> clazz) { return getLogger(clazz).isTraceEnabled(); }

    // ====================== Logger-Konfiguration ======================

    /**
     * Konfiguriert App- und Timeline-Logger auf das gegebene Verzeichnis.
     * Existierende Log-Dateien werden rotiert (.001, .002, …).
     *
     * @return {@code true} bei Erfolg
     */
    public static synchronized boolean configure(File logOutputDir, String appLogFileName, String actionLogFileName) {
        if (!logOutputDir.exists() && !logOutputDir.mkdirs()) {
            System.err.println("[TimelineLogger] Konnte Log-Verzeichnis nicht anlegen: " + logOutputDir.getAbsolutePath());
            return false;
        }
        File appLogFile = new File(logOutputDir, appLogFileName);
        File actionLogFile = new File(logOutputDir, actionLogFileName);
        rotateExistingLogFile(appLogFile);
        rotateExistingLogFile(actionLogFile);

        appAppender = configureAppender(appAppender, APP_APPENDER_NAME, APP_PATTERN, appLogFile, true);
        if (appAppender == null) return false;
        org.apache.log4j.Logger appPackageLogger = org.apache.log4j.Logger.getLogger(APP_PACKAGE);
        appPackageLogger.setAdditivity(false);
        if (!appPackageLogger.isAttached(appAppender)) {
            appPackageLogger.addAppender(appAppender);
        }

        timelineAppender = configureAppender(timelineAppender, TIMELINE_APPENDER_NAME, TIMELINE_PATTERN, actionLogFile, false);
        if (timelineAppender == null) return false;
        if (!LOG4J_TIMELINE.isAttached(timelineAppender)) {
            LOG4J_TIMELINE.addAppender(timelineAppender);
        }
        return true;
    }

    /**
     * Schließt beide Appender und leert den Action-Tracker. Wichtig vor
     * dem Beenden des Prozesses oder einem Wechsel der Umgebung.
     */
    public static synchronized void close() {
        if (timelineAppender != null) {
            timelineAppender.close();
            LOG4J_TIMELINE.removeAppender(timelineAppender);
            timelineAppender = null;
        }
        if (appAppender != null) {
            appAppender.close();
            org.apache.log4j.Logger.getLogger(APP_PACKAGE).removeAppender(appAppender);
            appAppender = null;
        }
        activeActions.clear();
    }

    private static RollingFileAppender configureAppender(RollingFileAppender existing,
                                                         String appenderName,
                                                         String pattern,
                                                         File logFile,
                                                         boolean shortenPackageNames) {
        try {
            if (existing != null) {
                rotateExistingLogFile(logFile);
                existing.setFile(logFile.getAbsolutePath());
                existing.activateOptions();
                return existing;
            }
            RollingFileAppender appender = new RollingFileAppender();
            appender.setName(appenderName);
            appender.setFile(logFile.getAbsolutePath());
            appender.setMaxFileSize("10MB");
            appender.setMaxBackupIndex(5);
            appender.setLayout(shortenPackageNames ? new ShortenedPackageLayout(pattern) : new PatternLayout(pattern));
            appender.setAppend(true);
            appender.activateOptions();
            return appender;
        } catch (Exception e) {
            System.err.println("[TimelineLogger] Fehler beim Konfigurieren von " + appenderName + ": " + e.getMessage());
            return null;
        }
    }

    private static void rotateExistingLogFile(File logFile) {
        if (!logFile.exists()) return;
        int number = 1;
        File rotatedFile;
        do {
            rotatedFile = new File(logFile.getAbsolutePath() + String.format(".%03d", number));
            number++;
        } while (rotatedFile.exists() && number < 1000);
        if (number >= 1000) {
            System.err.println("[TimelineLogger] Zu viele rotierte Logs für: " + logFile.getName());
            return;
        }
        if (!logFile.renameTo(rotatedFile)) {
            System.err.println("[TimelineLogger] Konnte Log nicht rotieren: " + logFile.getName());
        }
    }

    /** Verkürzt Paketnamen im Log: {@code de.creditreform.crefoteam → d.c.c}. */
    private static final class ShortenedPackageLayout extends PatternLayout {
        ShortenedPackageLayout(String pattern) { super(pattern); }
        @Override public String format(LoggingEvent event) {
            return super.format(event).replace("de.creditreform.crefoteam", "d.c.c");
        }
    }

    // ====================== Timeline-Tracking ======================

    public static String start(String actionName) { return start(actionName, null); }

    public static String start(String actionName, String description) {
        String actionId = generateActionId(actionName);
        activeActions.put(actionId, new ActionInfo(actionName, description, Instant.now()));
        StringBuilder sb = new StringBuilder("START | action=").append(actionName);
        if (description != null && !description.isEmpty()) sb.append(" | desc=").append(description);
        sb.append(" | id=").append(actionId);
        TIMELINE.info(sb.toString());
        return actionId;
    }

    public static void end(String actionId) { end(actionId, null); }

    public static void end(String actionId, String result) {
        ActionInfo info = activeActions.remove(actionId);
        if (info == null) {
            TIMELINE.warn("END   | action=UNKNOWN | id={} | error=No matching start", actionId);
            return;
        }
        Duration duration = Duration.between(info.startTime, Instant.now());
        StringBuilder sb = new StringBuilder("END   | action=").append(info.actionName)
                .append(" | duration=").append(formatDuration(duration));
        if (result != null && !result.isEmpty()) sb.append(" | result=").append(result);
        sb.append(" | id=").append(actionId);
        TIMELINE.info(sb.toString());
    }

    public static void event(String eventName) { event(eventName, null); }

    public static void event(String eventName, String details) {
        StringBuilder sb = new StringBuilder("EVENT | name=").append(eventName);
        if (details != null && !details.isEmpty()) sb.append(" | details=").append(details);
        TIMELINE.info(sb.toString());
    }

    public static Action action(String actionName) { return new Action(actionName, null); }

    public static Action action(String actionName, String description) { return new Action(actionName, description); }

    private static synchronized String generateActionId(String actionName) {
        return actionName + "-" + (++actionCounter);
    }

    private static String formatDuration(Duration duration) {
        long millis = duration.toMillis();
        if (millis < 1000) return millis + "ms";
        if (millis < 60_000) return String.format("%.2fs", millis / 1000.0);
        return String.format("%dm %ds", duration.toMinutes(), duration.toSecondsPart());
    }

    private static final class ActionInfo {
        final String actionName;
        final String description;
        final Instant startTime;
        ActionInfo(String actionName, String description, Instant startTime) {
            this.actionName = actionName;
            this.description = description;
            this.startTime = startTime;
        }
    }

    /** Try-with-resources-Variante. */
    public static final class Action implements AutoCloseable {
        private final String actionId;
        private String result;
        Action(String actionName, String description) {
            this.actionId = TimelineLogger.start(actionName, description);
        }
        public Action result(String result) { this.result = result; return this; }
        @Override public void close() { TimelineLogger.end(actionId, result); }
    }
}
