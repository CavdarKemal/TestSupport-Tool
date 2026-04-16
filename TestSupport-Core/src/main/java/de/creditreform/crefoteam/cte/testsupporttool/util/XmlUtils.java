package de.creditreform.crefoteam.cte.testsupporttool.util;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * XML-Hilfsmethoden. Gezogen aus {@code TesunUtilites} des Original-
 * Projekts — nur die XML-Pretty-Print-Funktionen.
 */
public final class XmlUtils {

    private XmlUtils() { }

    public static String toPrettyString(String xml, int indent) {
        try {
            InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes("UTF-8")));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource);
            document.normalize();
            return toPrettyString(document, indent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String toPrettyString(Document document, int indent) throws Exception {
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']", document, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            node.getParentNode().removeChild(node);
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", indent);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        StringWriter stringWriter = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
        return stringWriter.toString();
    }

    public static void formatXMLFilesInDir(File srcDir, File dstDir) throws IOException {
        try (var stream = Files.walk(srcDir.toPath())) {
            stream.filter(path -> !Files.isDirectory(path)).forEach(path -> {
                File xmlFile = path.toFile();
                if (!xmlFile.getName().endsWith(".xml")) {
                    return;
                }
                try {
                    String xmlContent = FileUtils.readFileToString(xmlFile, StandardCharsets.UTF_8);
                    String newName = xmlFile.getAbsolutePath().replace(srcDir.getPath(), "");
                    File outputFile = new File(dstDir, newName);
                    if (!outputFile.getParentFile().exists()) {
                        outputFile.getParentFile().mkdirs();
                    }
                    FileUtils.writeStringToFile(outputFile, toPrettyString(xmlContent, 2), StandardCharsets.UTF_8);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }
}
