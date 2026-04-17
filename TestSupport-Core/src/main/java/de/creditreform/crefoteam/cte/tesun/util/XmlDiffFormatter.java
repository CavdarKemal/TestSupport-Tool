package de.creditreform.crefoteam.cte.tesun.util;

import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.NodeDetail;

import java.util.List;

public class XmlDiffFormatter {

    public StringBuilder appendDifferences(StringBuilder sBuilder, String prefix, List<Difference> differenceList) throws Exception {
        for (Difference theDifference : differenceList) {
            sBuilder = appendDifference(sBuilder, prefix, theDifference);
        }
        return sBuilder;
    }

    public StringBuilder appendDifference(StringBuilder sBuilder, String prefix, Difference theDifference) {
        final String description = theDifference.getDescription();
        final NodeDetail controlNodeDetail = theDifference.getControlNodeDetail();
        String xpathLocation = (controlNodeDetail != null) ? controlNodeDetail.getXpathLocation() : "";
        String controlXpathLocation = (xpathLocation != null) ? xpathLocation.replace("/text()[1]", "") : null;
        final NodeDetail testNodeDetail = theDifference.getTestNodeDetail();
        final String testXpathLocation = (testNodeDetail != null) ? testNodeDetail.getXpathLocation() : null;
        sBuilder.append(prefix).append("  Element '");
        if (description.equals("number of child nodes")) {
            sBuilder.append(controlXpathLocation);
            sBuilder.append("' hat unterschiedliche Anzahl an Child-Elementen ::");
            sBuilder.append(" Test: ");
            sBuilder.append((controlNodeDetail != null) ? controlNodeDetail.getValue() : "?");
            sBuilder.append("  Ctrl: ");
            sBuilder.append((testNodeDetail != null) ? testNodeDetail.getValue() : "????");
        } else if (description.equals("sequence of child nodes")) {
            sBuilder.append(controlXpathLocation);
            sBuilder.append("' ist an unterschiedlicher Stelle!");
        } else if (description.equals("presence of child node")) {
            if (controlXpathLocation != null) {
                sBuilder.append(controlXpathLocation);
                sBuilder.append("' existiert nur im Test-XML und fehlt im Control-XML!");
            } else if (testXpathLocation != null) {
                sBuilder.append(testXpathLocation);
                sBuilder.append("' existiert nur im Control-XML und fehlt im Test-XML!");
            }
        } else {
            if (description.equals("attribute value")) {
                sBuilder.append("-Attribut '");
            }
            sBuilder.append(controlXpathLocation);
            sBuilder.append("' hat unterschiedlichen Wert :: ");
            sBuilder.append(prefix).append("  Test: ");
            sBuilder.append((controlNodeDetail != null) ? controlNodeDetail.getValue() : "?");
            sBuilder.append(prefix).append("  Ctrl: ");
            sBuilder.append((testNodeDetail != null) ? testNodeDetail.getValue() : "`????");
        }
        return sBuilder;
    }
}
