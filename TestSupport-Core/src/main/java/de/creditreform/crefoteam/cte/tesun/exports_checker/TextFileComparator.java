package de.creditreform.crefoteam.cte.tesun.exports_checker;

import de.creditreform.crefoteam.cte.tesun.util.ContentDifference;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.NodeDetail;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextFileComparator {

    public List<Difference> compareContent(String name, String expectedContent, String actualContent, List<String> ignorableXPaths) throws IOException, SAXException {
        List<Difference> allDifferences = new ArrayList<>();
        if (name.endsWith(".xml")) {
            if (!expectedContent.isEmpty() && !actualContent.isEmpty()) {
                DetailedDiff diff = new DetailedDiff(XMLUnit.compareXML(expectedContent, actualContent));
                if ((ignorableXPaths != null) && !ignorableXPaths.isEmpty()) {
                    diff.overrideDifferenceListener(new ExportContentsDifferenceListener(ignorableXPaths));
                }
                allDifferences = diff.getAllDifferences();
            }
        } else if (name.endsWith(".csv")) {
            allDifferences = compareCSV(expectedContent, actualContent);
        } else if (name.endsWith(".txt")) {
            allDifferences = compareTXT(expectedContent, actualContent);
        }
        return allDifferences;
    }

    protected List<Difference> compareCSV(String control, String test) {
        List<Difference> differenceList = new ArrayList<>();
        final String[] controlLines = control.split("\n");
        final String[] testLines = test.split("\n");
        if (controlLines.length != testLines.length) {
            final String description = "Zeilenanzahl bei Control(=" + controlLines.length + ") und Test(=" + testLines.length + ") sind unterschiedlich!";
            differenceList.add(createContentDifference(differenceList.size() + 1, controlLines.length, testLines.length, description));
        }
        int numLines = Math.min(controlLines.length, testLines.length);
        for (int lineNr = 0; lineNr < numLines; lineNr++) {
            final String[] controlCols = controlLines[lineNr].trim().split(";");
            final String[] testCols = testLines[lineNr].trim().split(";");
            if (controlCols.length != testCols.length) {
                final String description = "Spaltenanzahl bei Control(=" + controlCols.length + ") und Test(=" + testCols.length + ") der Zeile " + (lineNr + 1) + " sind unterschiedlich!";
                differenceList.add(createContentDifference(differenceList.size() + 1, controlLines.length, testLines.length, description));
            }
            int numCols = Math.min(controlCols.length, testCols.length);
            for (int colNr = 0; colNr < numCols; colNr++) {
                final String controlCol = controlCols[colNr].trim();
                final String testCol = testCols[colNr].trim();
                if (!controlCol.equals(testCol)) {
                    final String description = "Spalte " + (colNr + 1) + " des Control(=" + controlCol + ") und Test(=" + testCol + ") der Zeile " + (lineNr + 1) + " sind unterschiedlich!";
                    differenceList.add(createContentDifference(differenceList.size() + 1, controlLines.length, testLines.length, description));
                }
            }
        }
        return differenceList;
    }

    protected List<Difference> compareTXT(String control, String test) {
        List<Difference> differenceList = new ArrayList<>();
        if (!control.equals(test)) {
            final String description = "Control und Test haben unterschiedlichen Inhalt!";
            differenceList.add(createContentDifference(differenceList.size() + 1, control, test, description));
        }
        return differenceList;
    }

    protected ContentDifference createContentDifference(int diffCount, Object controlValue, Object testValue, String description) {
        ContentDifference protoType = new ContentDifference(diffCount, description);
        NodeDetail controlDetail = new NodeDetail(String.valueOf(controlValue), null, "");
        NodeDetail testDetail = new NodeDetail(String.valueOf(testValue), null, "");
        return new ContentDifference(protoType, controlDetail, testDetail);
    }
}
