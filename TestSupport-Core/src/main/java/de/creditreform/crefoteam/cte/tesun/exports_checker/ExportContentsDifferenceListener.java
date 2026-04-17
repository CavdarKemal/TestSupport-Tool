package de.creditreform.crefoteam.cte.tesun.exports_checker;

import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class ExportContentsDifferenceListener implements DifferenceListener {
    private final Logger logger = LoggerFactory.getLogger(ExportContentsComparator.class);

    private List<String> ignorableXPaths;

    public ExportContentsDifferenceListener(final List<String> ignorableXPaths) {
        this.ignorableXPaths = ignorableXPaths;
        if (this.ignorableXPaths == null) {
            this.ignorableXPaths = new ArrayList<>();
        }
    }

    public int differenceFound(Difference difference) {
        String xpathLocation = difference.getTestNodeDetail().getXpathLocation();
        String description = difference.getDescription();
        if (description.equals("number of child nodes")) {
            return DifferenceListener.RETURN_ACCEPT_DIFFERENCE;
        }
        if (description.equals("sequence of child nodes")) {
            return DifferenceListener.RETURN_ACCEPT_DIFFERENCE;
        }
        if (description.equals("presence of child node")) {
            return DifferenceListener.RETURN_ACCEPT_DIFFERENCE;
        }
        if (description.equals("text value")) {
            xpathLocation = xpathLocation.replace("/text()[1]", "");
        }
        for (String ignorableXPath : ignorableXPaths) {
            if (xpathLocation.endsWith(ignorableXPath)) {
                logger.info("\t\t\tUnterschied wird ignoriert!");
                return DifferenceListener.RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
            }
        }
        return DifferenceListener.RETURN_ACCEPT_DIFFERENCE;
    }

    public void skippedComparison(Node node, Node node1) {
        logger.warn("skippedComparison(): " + node.getLocalName() + ", " + node1.getLocalName());
    }
}
