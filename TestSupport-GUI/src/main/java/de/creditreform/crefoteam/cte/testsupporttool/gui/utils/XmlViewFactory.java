package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * 1:1-Port aus {@code testsupport_client.tesun.gui.utils.XmlViewFactory}.
 *
 * <p>{@link ViewFactory} fuer XML-Editor-Komponenten — liefert pro Element
 * eine {@link XmlView} mit Syntax-Highlighting.
 */
public class XmlViewFactory implements ViewFactory {

    public View create(Element element) {
        return new XmlView(element);
    }

}
