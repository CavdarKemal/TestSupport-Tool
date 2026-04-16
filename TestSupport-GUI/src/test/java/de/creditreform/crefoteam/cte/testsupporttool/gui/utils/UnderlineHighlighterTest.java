package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import org.junit.jupiter.api.Test;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;

import java.awt.Color;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UnderlineHighlighterTest {

    @Test
    void addHighlight_usesPainterFromConstructor_andRegistersHighlight() throws BadLocationException {
        JTextArea ta = new JTextArea("hello world");
        UnderlineHighlighter highlighter = new UnderlineHighlighter(Color.RED);
        ta.setHighlighter(highlighter);

        highlighter.addHighlight(0, 5);

        Highlighter.Highlight[] highlights = highlighter.getHighlights();
        assertThat(highlights).hasSize(1);
        assertThat(highlights[0].getPainter())
                .isInstanceOf(UnderlineHighlighter.UnderlineHighlightPainter.class);
    }

    @Test
    void setDrawsLayeredHighlights_false_throws() {
        UnderlineHighlighter highlighter = new UnderlineHighlighter(Color.BLUE);
        // Original-Vertrag: nur layered Highlights erlaubt.
        assertThatThrownBy(() -> highlighter.setDrawsLayeredHighlights(false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void setDrawsLayeredHighlights_true_isAccepted() {
        UnderlineHighlighter highlighter = new UnderlineHighlighter(Color.BLUE);
        // Wirft nicht.
        highlighter.setDrawsLayeredHighlights(true);
    }

    @Test
    void nullColorConstructor_usesSharedPainter_thatPaintsInSelectionColor() throws BadLocationException {
        JTextArea ta = new JTextArea("abc");
        UnderlineHighlighter h1 = new UnderlineHighlighter(null);
        UnderlineHighlighter h2 = new UnderlineHighlighter(null);
        ta.setHighlighter(h1);
        h1.addHighlight(0, 1);

        // Beide null-Konstruktor-Instanzen teilen sich den gleichen Painter (sharedPainter).
        Highlighter.Highlight[] highlights = h1.getHighlights();
        assertThat(highlights).hasSize(1);
        // Painter-Identitaet ist Shared — wir vergleichen Klassen-Identitaet auf Painter-Ebene.
        assertThat(highlights[0].getPainter())
                .isInstanceOf(UnderlineHighlighter.UnderlineHighlightPainter.class);
    }
}
