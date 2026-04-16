package de.creditreform.crefoteam.cte.tesun.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Literal-Port aus {@code testsupport_client.tesun_util}. */
public class TesunDateUtils {
    public static SimpleDateFormat DATE_FORMAT_DD_MM_YYYY_HH_MINUS_MM = new SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.getDefault());
    public static SimpleDateFormat DATE_FORMAT_DD_MM_YYYY_HH_MINUS_MM_MINUS_SS = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());
    public static SimpleDateFormat DATE_FORMAT_DD_MM_YYYY_HH_MM_SS = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
    public static SimpleDateFormat DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static SimpleDateFormat DATE_FORMAT_YYYY_MM_DD_HH_MM = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    public static SimpleDateFormat DATE_FORMAT_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private static final String[] SUPPORTED_PATTERNS = {
            "yyyy-MM-dd_HH-mm-ss",
            "yyyy_MM_dd_HH_mm_ss",
            "yyyy.MM.dd_HH.mm.ss",
            "dd-MM-yyyy_HH-mm-ss",
            "dd.MM.yyyy HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd_HH-mm",
            "yyyy_MM_dd_HH_mm",
            "yyyy.MM.dd_HH.mm",
            "dd-MM-yyyy_HH-mm",
            "dd.MM.yyyy HH:mm",
            "dd.MM.yyyy",
            "dd-MM-yyyy",
            "yyyy-MM-dd",
            "yyyy_MM_dd",
            "yyyy.MM.dd",
            "HH:mm:ss",
            "HH:mm"
    };
    private static final List<DateTimeFormatter> FORMATTERS = new ArrayList<>();
    private static final List<Pattern> EXTRACTION_PATTERNS = new ArrayList<>();

    static {
        for (String pattern : SUPPORTED_PATTERNS) {
            FORMATTERS.add(DateTimeFormatter.ofPattern(pattern));
            EXTRACTION_PATTERNS.add(Pattern.compile(".*?" + convertDateFormatToRegex(pattern) + ".*"));
        }
    }

    public static LocalDateTime parseToLocalDateTime(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        if (input.matches("^\\d+$")) {
            try {
                long millis = Long.parseLong(input);
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
            } catch (NumberFormatException ignored) { }
        }
        for (int i = 0; i < SUPPORTED_PATTERNS.length; i++) {
            Pattern regex = EXTRACTION_PATTERNS.get(i);
            Matcher matcher = regex.matcher(input);
            if (matcher.matches()) {
                String dateString = matcher.group(1);
                DateTimeFormatter formatter = FORMATTERS.get(i);
                try {
                    TemporalAccessor accessor = formatter.parseBest(dateString,
                            LocalDateTime::from,
                            LocalDate::from,
                            LocalTime::from);
                    if (accessor instanceof LocalDateTime) {
                        return (LocalDateTime) accessor;
                    } else if (accessor instanceof LocalDate) {
                        return ((LocalDate) accessor).atStartOfDay();
                    } else if (accessor instanceof LocalTime) {
                        return ((LocalTime) accessor).atDate(LocalDate.now());
                    }
                } catch (DateTimeParseException ignored) { }
            }
        }
        return null;
    }

    public static Calendar toCalendar(String input) {
        LocalDateTime ldt = parseToLocalDateTime(input);
        return ldt != null ? toCalendar(ldt) : null;
    }

    public static Date toDate(String input) {
        LocalDateTime ldt = parseToLocalDateTime(input);
        return ldt != null ? toDate(ldt) : null;
    }

    public static Calendar extractDateFromString(String fileName) {
        return toCalendar(fileName);
    }

    public static boolean isAfter(String fileName, Calendar calendar) {
        LocalDateTime fileDate = parseToLocalDateTime(fileName);
        LocalDateTime targetDate = toLocalDateTime(calendar);
        return fileDate != null && targetDate != null && fileDate.isAfter(targetDate);
    }

    public static boolean isBefore(String fileName, Calendar calendar) {
        LocalDateTime fileDate = parseToLocalDateTime(fileName);
        LocalDateTime targetDate = toLocalDateTime(calendar);
        return fileDate != null && targetDate != null && fileDate.isBefore(targetDate);
    }

    public static boolean isSame(String fileName, Calendar calendar) {
        LocalDateTime fileDate = parseToLocalDateTime(fileName);
        LocalDateTime targetDate = toLocalDateTime(calendar);
        return fileDate != null && targetDate != null && fileDate.isEqual(targetDate);
    }

    public static boolean isSameOrAfter(Calendar theCal, Calendar otherCal) {
        LocalDateTime fromDate = toLocalDateTime(theCal);
        LocalDateTime targetDate = toLocalDateTime(otherCal);
        return !fromDate.isBefore(targetDate);
    }

    public static boolean isSameOrAfter(String fileName, Calendar calendar) {
        LocalDateTime fileDate = parseToLocalDateTime(fileName);
        LocalDateTime targetDate = toLocalDateTime(calendar);
        return fileDate != null && targetDate != null && !fileDate.isBefore(targetDate);
    }

    public static boolean isSameOrBefore(String fileName, Calendar calendar) {
        LocalDateTime fileDate = parseToLocalDateTime(fileName);
        LocalDateTime targetDate = toLocalDateTime(calendar);
        return fileDate != null && targetDate != null && !fileDate.isAfter(targetDate);
    }

    public static boolean isBetween(String fileName, Calendar cal1, Calendar cal2) {
        LocalDateTime fileDate = parseToLocalDateTime(fileName);
        if (fileDate == null || cal1 == null || cal2 == null) return false;
        LocalDateTime start = toLocalDateTime(cal1);
        LocalDateTime end = toLocalDateTime(cal2);
        if (start.isAfter(end)) {
            LocalDateTime temp = start;
            start = end;
            end = temp;
        }
        return !fileDate.isBefore(start) && !fileDate.isAfter(end);
    }

    public static boolean isBeforeDate(String pathName, Date fromDate) {
        LocalDateTime fileDate = parseToLocalDateTime(pathName);
        LocalDateTime targetDate = toLocalDateTime(fromDate);
        return fileDate != null && targetDate != null && targetDate.isBefore(fileDate);
    }

    public static boolean isAfterDate(String pathName, Date fromDate) {
        return isBeforeDate(pathName, fromDate);
    }

    public static String formatCalendar(Calendar cal) {
        if (cal == null) return "Datum ungültig oder null!";
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(toLocalDateTime(cal));
    }

    public static String formatCalendar(Calendar cal, String pattern) {
        if (cal == null) return "";
        return DateTimeFormatter.ofPattern(pattern).format(toLocalDateTime(cal));
    }

    public static String formatElapsedTime(String strAction, long startMillis, long endMillis) {
        long millis = endMillis - startMillis;
        return strAction + " hat " + millis + " ms gedauert.";
    }

    public static String ermittleDateTimeFormat(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isBlank()) return null;
        for (int i = 0; i < SUPPORTED_PATTERNS.length; i++) {
            String pattern = SUPPORTED_PATTERNS[i];
            String regex = "^" + convertDateFormatToRegex(pattern) + "$";
            if (dateTimeString.matches(regex)) {
                return pattern;
            }
        }
        return "Unbekanntes Format";
    }

    private static String convertDateFormatToRegex(String dateFormat) {
        String regex = dateFormat
                .replace("yyyy", "\\d{4}")
                .replace("MM", "\\d{2}")
                .replace("dd", "\\d{2}")
                .replace("HH", "\\d{2}")
                .replace("mm", "\\d{2}")
                .replace("ss", "\\d{2}")
                .replace(".", "\\.")
                .replace("-", "[-_]")
                .replace("_", "[-_]")
                .replace(" ", "\\s");
        return "(" + regex + ")";
    }

    private static Calendar toCalendar(LocalDateTime ldt) {
        if (ldt == null) return null;
        Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    private static Date toDate(LocalDateTime ldt) {
        if (ldt == null) return null;
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static LocalDateTime toLocalDateTime(Calendar cal) {
        if (cal == null) return null;
        return LocalDateTime.ofInstant(cal.toInstant(), ZoneId.systemDefault());
    }

    private static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) return null;
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
