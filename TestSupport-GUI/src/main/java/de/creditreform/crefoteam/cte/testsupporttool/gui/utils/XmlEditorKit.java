package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;

/**
 * 1:1-Port aus {@code testsupport_client.tesun.gui.utils.XmlEditorKit}.
 *
 * <p>{@link StyledEditorKit} fuer XML-Inhalte mit
 * {@code text/xml}-Content-Type und {@link XmlViewFactory} als View-Factory.
 */
public class XmlEditorKit extends StyledEditorKit {
    private final ViewFactory xmlViewFactory;

    public XmlEditorKit() {
        xmlViewFactory = new XmlViewFactory();
    }

    @Override
    public ViewFactory getViewFactory() {
        return xmlViewFactory;
    }

    @Override
    public String getContentType() {
        return "text/xml";
    }

}
