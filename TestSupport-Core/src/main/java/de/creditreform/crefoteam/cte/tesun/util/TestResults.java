package de.creditreform.crefoteam.cte.tesun.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Port aus {@code testsupport_client.tesun_util} ohne {@code DiffenrenceInfo}
 * — diese innere Klasse zieht {@code org.custommonkey.xmlunit.Difference} nach
 * sich. Sie kann später ergänzt werden, wenn Check-Tasks portiert werden, die
 * XML-Vergleiche durchführen.
 */
public class TestResults {

    private final List<ResultInfo> resultInfosList = new ArrayList<>();
    private final String command;

    public TestResults(String command) {
        this.command = command;
    }

    public String getCommand() { return command; }

    public void addResultInfo(ResultInfo resultInfo) {
        resultInfosList.add(resultInfo);
    }

    public List<ResultInfo> getResultInfosList() {
        return resultInfosList;
    }

    public StringBuilder dumpResults(StringBuilder stringBuilder, String prefix) {
        for (ResultInfo resultInfo : resultInfosList) {
            stringBuilder.append(prefix).append("\t").append(resultInfo.getErrorStr());
        }
        return stringBuilder;
    }

    public static class ResultInfo {

        private String errorStr;
        private Long crefoNummer;

        public ResultInfo(Long crefoNummer, String errorStr) {
            this.crefoNummer = crefoNummer;
            this.errorStr = errorStr;
        }

        public ResultInfo(String xmlName, String errorStr) {
            this.crefoNummer = extractCrefonummer(xmlName);
            this.errorStr = errorStr;
        }

        public ResultInfo(String errorStr) {
            try {
                this.crefoNummer = extractCrefonummer(errorStr);
            } catch (Exception ex) {
                this.crefoNummer = -1L;
            }
            this.errorStr = errorStr;
        }

        private static Long extractCrefonummer(String str) {
            if (str == null) return -1L;
            Matcher matcher = TestSupportClientKonstanten.CREFONUMMER_PATTERN.matcher(str);
            if (matcher.find()) {
                try {
                    return Long.valueOf(matcher.group(0));
                } catch (NumberFormatException ignored) { }
            }
            return -1L;
        }

        public Long getCrefoNummer() { return crefoNummer; }
        public String getErrorStr() { return errorStr; }

        public String appendToErrorStr(String strAppend) {
            errorStr += strAppend;
            return errorStr;
        }
    }
}
