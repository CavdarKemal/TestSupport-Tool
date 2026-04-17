package de.creditreform.crefoteam.cte.tesun.util;

import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.NodeDetail;

public class ContentDifference extends Difference {
    public ContentDifference(int id, String description) {
        super(id, description);
    }

    public ContentDifference(Difference prototype, NodeDetail controlNodeDetail, NodeDetail testNodeDetail) {
        super(prototype, controlNodeDetail, testNodeDetail);
    }
}
