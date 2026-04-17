package de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.XmlSearchCursor;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;

import javax.xml.stream.XMLStreamException;

/**
 * Wrapper für {@link XmlMatcher} zur Invertierung der Bedingung
 * Created by ralf on 29.10.14.
 */
public class XmlMatcherLogicNot
implements XmlMatcher {
    private final XmlMatcher wrappedMatcher;

    public XmlMatcherLogicNot(XmlMatcher wrappedMatcher) {
        this.wrappedMatcher = wrappedMatcher;
    }

    @Override
    public XmlMatcherLogicNot matchCursor(XmlSearchCursor childCursor, IPerEntryListener perEntryListener)
    throws XMLStreamException {
        wrappedMatcher.matchCursor(childCursor, perEntryListener);
        return this;
    }

    @Override
    public void notifyExitElement(XmlSearchCursor childCursor, IPerEntryListener perEntryListener) throws XMLStreamException {
        wrappedMatcher.notifyExitElement(childCursor, perEntryListener);
    }

    @Override
    public boolean isSatisfied() {
        return !wrappedMatcher.isSatisfied();
    }

    @Override
    public XmlMatcherLogicNot reset() {
        wrappedMatcher.reset();
        return this;
    }

    @Override
    public void notifyZipEntryCompleted(boolean success, IPerEntryListener perEntryListener) {
        wrappedMatcher.notifyZipEntryCompleted(success, perEntryListener);
    }

}
