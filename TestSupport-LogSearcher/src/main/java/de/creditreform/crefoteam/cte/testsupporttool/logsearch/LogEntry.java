package de.creditreform.crefoteam.cte.testsupporttool.logsearch;

import de.creditreform.crefoteam.cte.tesun.util.TesunDateUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    private static final Pattern LOG_ENTRY_ALL_PATTERN   = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}),\\d{3} (.{3,10}) (.*) -(.*)");
    private static final Pattern LOG_ENTRY_INFO_PATTERN  = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}),\\d{3} (INFO ) (.*) -(.*)");
    private static final Pattern LOG_ENTRY_WARN_PATTERN  = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}),\\d{3} (WARN ) (.*) -(.*)");
    private static final Pattern LOG_ENTRY_ERROR_PATTERN = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}),\\d{3} (ERROR) (.*) -(.*)");
    private static final Pattern LOG_ENTRY_FATAL_PATTERN = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}),\\d{3} (FATAL) (.*) -(.*)");

    public enum ENTRY_TYPE {
        ALL(LOG_ENTRY_ALL_PATTERN),
        INFO(LOG_ENTRY_INFO_PATTERN),
        WARN(LOG_ENTRY_WARN_PATTERN),
        ERROR(LOG_ENTRY_ERROR_PATTERN),
        FATAL(LOG_ENTRY_FATAL_PATTERN);

        private final Pattern pattern;

        ENTRY_TYPE(Pattern pattern) {
            this.pattern = pattern;
        }

        public Pattern getPattern() {
            return pattern;
        }
    }

    private final File logFile;
    private final Date logDate;
    private final ENTRY_TYPE type;
    private final String packg;
    private final List<String> infoList = new ArrayList<>();

    public LogEntry(File logFile, Date logDate, String type, String packg, String info) {
        this.logFile = logFile;
        this.logDate = logDate;
        this.type = ENTRY_TYPE.valueOf(type.trim());
        this.packg = packg;
        this.infoList.add(info);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\tDatum:   ").append(TesunDateUtils.DATE_FORMAT_DD_MM_YYYY_HH_MM_SS.format(logDate));
        sb.append("\n\tTyp:     ").append(type);
        sb.append("\n\tPackage: ").append(packg);
        sb.append("\n\tInfos:  ").append(infoList.get(0));
        for (int i = 1; i < infoList.size(); i++) {
            sb.append("\n\t         ").append(infoList.get(i));
        }
        return sb.toString();
    }

    public void addAdditionalInfo(String strLine) {
        infoList.add(strLine);
    }

    public File getLogFile() {
        return logFile;
    }

    public Date getLogDate() {
        return logDate;
    }

    public ENTRY_TYPE getType() {
        return type;
    }

    public String getPackg() {
        return packg;
    }

    public List<String> getInfoList() {
        return infoList;
    }

    public Matcher searchInInfosList(String regEx) {
        Pattern pattern = Pattern.compile(regEx);
        for (String info : infoList) {
            Matcher matcher = pattern.matcher(info);
            if (matcher.find()) {
                return matcher;
            }
        }
        return null;
    }

    public String getLogDateAsString() {
        return TesunDateUtils.DATE_FORMAT_DD_MM_YYYY_HH_MM_SS.format(logDate);
    }
}
