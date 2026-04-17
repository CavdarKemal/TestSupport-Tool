package de.creditreform.crefoteam.cte.testsupporttool.logsearch;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

abstract class TextFileParser<T> {
    public List<T> getMatchedTokens(String fileName) throws IOException {
        List<T> matchedTokens = new ArrayList<>();
        List<String> readLines = FileUtils.readLines(new File(fileName));
        for (String strLine : readLines) {
            T matchedToken = extractData(strLine);
            if (matchedToken != null) {
                matchedTokens.add(matchedToken);
            }
        }
        return matchedTokens;
    }

    public abstract T extractData(String strLine);
}
