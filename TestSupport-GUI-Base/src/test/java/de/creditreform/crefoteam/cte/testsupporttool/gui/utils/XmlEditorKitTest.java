package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import org.junit.jupiter.api.Test;

import javax.swing.text.ViewFactory;

import static org.assertj.core.api.Assertions.assertThat;

class XmlEditorKitTest {

    @Test
    void contentType_isTextXml() {
        assertThat(new XmlEditorKit().getContentType()).isEqualTo("text/xml");
    }

    @Test
    void viewFactory_isXmlViewFactoryInstance() {
        ViewFactory factory = new XmlEditorKit().getViewFactory();
        assertThat(factory).isInstanceOf(XmlViewFactory.class);
    }

    @Test
    void viewFactory_isStableSingleInstancePerKit() {
        XmlEditorKit kit = new XmlEditorKit();
        assertThat(kit.getViewFactory()).isSameAs(kit.getViewFactory());
    }
}
