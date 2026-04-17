package de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.XmlSearchCursor;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;

import javax.xml.stream.XMLStreamException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class XmlMatcherCrefo
extends XmlMatcherGroupBySingle {
    public static final List<String> DEFAULT_TAGNAMES_CREFO = Collections.unmodifiableList(Arrays.asList("crefonummer", "crefo-number", "CrefoNummer"));

    public XmlMatcherCrefo(MatcherParameterTag matcherParameterTag) {
        super(true, matcherParameterTag,
              (matcherParameterTag == null || matcherParameterTag.getXmlTagName()==null) ? DEFAULT_TAGNAMES_CREFO : null);
    }

    @Override
    protected boolean onMatchFound(XmlSearchCursor elementCursor, IPerEntryListener perEntryListener)
       throws XMLStreamException {
        if(matcherResult == null) {
            return super.onMatchFound(elementCursor, perEntryListener);
        }
        return true;
    }

}
