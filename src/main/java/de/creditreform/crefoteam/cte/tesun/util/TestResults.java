package de.creditreform.crefoteam.cte.tesun.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Schalen-Port aus {@code testsupport_client.tesun_util}. Die
 * XML-Diff-bezogene innere Klasse {@code DiffenrenceInfo} wurde
 * bewusst nicht portiert — sie zieht die xmlunit-Library nach sich.
 * Kann später ergänzt werden, wenn Check-Tasks portiert werden.
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
            stringBuilder.append(prefix).append("\t");
            stringBuilder.append(resultInfo.getErrorStr());
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

        public ResultInfo(String errorStr) {
            this.crefoNummer = extractCrefonummer(errorStr);
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
