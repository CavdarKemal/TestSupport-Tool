package de.creditreform.crefoteam.cte.tesun.util;

import org.custommonkey.xmlunit.Difference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

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

    public static class DiffenrenceInfo {
        private final Map<String, List<Difference>> diffsMap = new HashMap<>();
        private final File xmlFileSrc;
        private final File xmlFileDst;
        private final String testFallName;
        private final File diffFile;

        public DiffenrenceInfo(String testFallName, File xmlFileSrc, File xmlFileDst, File diffFile) {
            this.testFallName = testFallName;
            this.xmlFileSrc = xmlFileSrc;
            this.xmlFileDst = xmlFileDst;
            this.diffFile = diffFile;
        }

        public DiffenrenceInfo(String testFallName, File xmlFileSrc, File xmlFileDst, File diffFile, List<Difference> differenceList) {
            this(testFallName, xmlFileSrc, xmlFileDst, diffFile);
            diffsMap.put(diffFile.getName(), differenceList);
        }

        public String getTestFallName() { return testFallName; }
        public File getXmlFileSrc() { return xmlFileSrc; }
        public File getXmlFileDst() { return xmlFileDst; }
        public File getDiffFile() { return diffFile; }
        public Map<String, List<Difference>> getDiffsMap() { return diffsMap; }
    }

    public static class ResultInfo {

        private final List<DiffenrenceInfo> diffenrenceInfosList = new ArrayList<>();
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

        public List<DiffenrenceInfo> getDiffenrenceInfosList() {
            return diffenrenceInfosList;
        }

        public void addDifferences(DiffenrenceInfo diffenrenceInfo) {
            diffenrenceInfosList.add(diffenrenceInfo);
        }
    }
}
