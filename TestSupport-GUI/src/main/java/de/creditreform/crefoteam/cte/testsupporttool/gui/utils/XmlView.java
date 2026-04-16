package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import javax.swing.text.*;
import java.awt.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1:1-Port aus {@code testsupport_client.tesun.gui.utils.XmlView}.
 *
 * <p>Syntax-Highlighting-View fuer XML-Inhalte in Swing-{@code JTextPane}s.
 * Faerbt Tags, Attribute, CDATA, Kommentare und Werte ueber Regex-Patterns.
 */
public class XmlView extends PlainView {
    private static final HashMap<Pattern, Color> patternColors;
    private static final String GENERIC_XML_NAME = "[A-Za-z]+[A-Za-z0-9\\-_]*(:[A-Za-z]+[A-Za-z0-9\\-_]+)?";
    private static final String TAG_PATTERN = "(</?" + GENERIC_XML_NAME + ")";
    private static final String TAG_END_PATTERN = "(>|/>)";
    private static final String TAG_ATTRIBUTE_PATTERN = "(" + GENERIC_XML_NAME + ")\\w*\\=";
    private static final String TAG_ATTRIBUTE_VALUE = "\\w*\\=\\w*(\"[^\"]*\")";
    private static final String TAG_COMMENT = "(<\\!--[\\w ]*-->)";
    private static final String TAG_CDATA = "(<\\!\\[CDATA\\[.*\\]\\]>)";

    static {
        // NOTE: the order is important!
        patternColors = new LinkedHashMap<Pattern, Color>();
        patternColors.put(Pattern.compile(TAG_PATTERN), new Color(63, 127, 127));
        patternColors.put(Pattern.compile(TAG_CDATA), Color.GRAY);
        patternColors.put(Pattern.compile(TAG_ATTRIBUTE_PATTERN), new Color(127, 0, 127));
        patternColors.put(Pattern.compile(TAG_END_PATTERN), new Color(63, 127, 127));
        patternColors.put(Pattern.compile(TAG_ATTRIBUTE_VALUE), new Color(42, 0, 255));
        patternColors.put(Pattern.compile(TAG_COMMENT), Color.BLUE);
    }

    public XmlView(Element element) {
        super(element);
        getDocument().putProperty(PlainDocument.tabSizeAttribute, 4);
    }

    @Override
    protected int drawUnselectedText(Graphics graphics, int x, int y, int p0, int p1) throws BadLocationException {
        Document doc = getDocument();
        String text = doc.getText(p0, p1 - p0);
        Segment segment = getLineBuffer();
        SortedMap<Integer, Integer> startMap = new TreeMap<Integer, Integer>();
        SortedMap<Integer, Color> colorMap = new TreeMap<Integer, Color>();
        // Match all regexes on this snippet, store positions
        for (Map.Entry<Pattern, Color> entry : patternColors.entrySet()) {
            Matcher matcher = entry.getKey().matcher(text);
            while (matcher.find()) {
                startMap.put(matcher.start(1), matcher.end());
                colorMap.put(matcher.start(1), entry.getValue());
            }
        }
        int i = 0;
        // Colour the parts
        for (Map.Entry<Integer, Integer> entry : startMap.entrySet()) {
            int start = entry.getKey();
            int end = entry.getValue();
            if (i < start) {
                graphics.setColor(Color.black);
                doc.getText(p0 + i, start - i, segment);
                x = Utilities.drawTabbedText(segment, x, y, graphics, this, i);
            }
            graphics.setColor(colorMap.get(start));
            i = end;
            doc.getText(p0 + start, i - start, segment);
            x = Utilities.drawTabbedText(segment, x, y, graphics, this, start);
        }
        // Paint possible remaining text black
        if (i < text.length()) {
            graphics.setColor(Color.black);
            doc.getText(p0 + i, text.length() - i, segment);
            x = Utilities.drawTabbedText(segment, x, y, graphics, this, i);
        }
        return x;
    }

}
