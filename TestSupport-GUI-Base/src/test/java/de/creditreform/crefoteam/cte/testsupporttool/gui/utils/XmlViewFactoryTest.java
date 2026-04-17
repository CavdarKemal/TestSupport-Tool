package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import org.junit.jupiter.api.Test;

import javax.swing.JEditorPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Element;
import javax.swing.text.View;

import static org.assertj.core.api.Assertions.assertThat;

class XmlViewFactoryTest {

    @Test
    void create_returnsXmlViewBoundToGivenElement() throws Exception {
        // Realistischer Element-Lieferant: JEditorPane mit XmlEditorKit installiert,
        // dann das Root-Element des AbstractDocument abgreifen.
        JEditorPane pane = new JEditorPane();
        pane.setEditorKit(new XmlEditorKit());
        pane.setText("<root attr=\"v\"/>");
        Element rootElement = ((AbstractDocument) pane.getDocument()).getDefaultRootElement();

        View view = new XmlViewFactory().create(rootElement);

        assertThat(view).isInstanceOf(XmlView.class);
        assertThat(view.getElement()).isSameAs(rootElement);
    }
}
