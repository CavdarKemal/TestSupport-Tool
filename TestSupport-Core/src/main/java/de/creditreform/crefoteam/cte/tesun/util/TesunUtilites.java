package de.creditreform.crefoteam.cte.tesun.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xml.sax.InputSource;

/**
 * Teilport von {@code testsupport_client.tesun_util.TesunUtilites}.
 *
 * <p>Portiert ist derzeit nur {@link #shortPath(String, int)} /
 * {@link #shortPath(File, int)} — gebraucht von der GUI (Test-Result-Diffs).
 * Weitere Methoden (Email-Versand, FutureTask-Waiter, Directory-Scanner …)
 * werden nachgezogen, wenn die entsprechenden Consumer portiert sind;
 * Platzhalter dafür in {@code CLAUDE_MODE}-Markierung unten.
 */
public final class TesunUtilites {

    private TesunUtilites() { }

    public static String shortPath(String thePath, int maxLen) {
        return shortPath(new File(thePath), maxLen);
    }

    public static String shortPath(File theFile, int maxLen) {
        String absolutePath = theFile.getAbsolutePath();
        if (absolutePath.length() < maxLen) {
            return theFile.getAbsolutePath();
        }
        File rootFile = new File(absolutePath);
        while (rootFile.getParentFile().getParentFile() != null) {
            rootFile = rootFile.getParentFile();
        }
        int rootLen = rootFile.getAbsolutePath().length();
        while (absolutePath.length() >= (rootLen + maxLen)) {
            int fistSlashPos = absolutePath.indexOf(File.separator, 3);
            if (fistSlashPos < 0) {
                break;
            }
            absolutePath = absolutePath.substring(fistSlashPos);
        }
        if (!absolutePath.startsWith(rootFile.getPath())) {
            absolutePath = rootFile + File.separator + "..." + absolutePath;
        }
        return absolutePath;
    }

    public static List<File> getFilesFromDir(File theRoot, final String regExp) throws IOException {
        File[] files = null;
        if (theRoot != null && theRoot.exists()) {
            files = theRoot.listFiles((dir, fileName) -> fileName.endsWith(regExp) || fileName.matches(regExp));
        }
        List<File> filesFromDir = new ArrayList<>();
        if (files != null) {
            Collections.addAll(filesFromDir, files);
        }
        filesFromDir.sort((o1, o2) -> o1.getPath().compareTo(o2.getPath()));
        return filesFromDir;
    }

    public static String toPrettyString(String xml, int indent) {
        try {
            final InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes("UTF-8")));
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

    /* CLAUDE_MODE
     * Nicht portiert:
     *   public static void sendEmail(...)
     *   public static Long extractCrefonummerFromString(...)
     *   public static void waitForFutureTasks(...)
     */
}
