package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import org.junit.jupiter.api.Test;

import javax.swing.JTextArea;
import javax.swing.text.Highlighter;

import static org.assertj.core.api.Assertions.assertThat;

class WordSearcherTest {

    @Test
    void search_findsFirstOccurrenceOffset_caseInsensitive() {
        JTextArea ta = new JTextArea("Hello World hello");
        WordSearcher searcher = new WordSearcher(ta);

        int firstOffset = searcher.search("hello");

        // Case-insensitive: erstes "Hello" an Offset 0.
        assertThat(firstOffset).isZero();
    }

    @Test
    void search_highlightsAllOccurrencesWithUnderlinePainter() {
        JTextArea ta = new JTextArea("abc abc abc");
        WordSearcher searcher = new WordSearcher(ta);

        searcher.search("abc");

        Highlighter.Highlight[] highlights = ta.getHighlighter().getHighlights();
        assertThat(highlights).hasSize(3);
        assertThat(highlights)
                .allSatisfy(h -> assertThat(h.getPainter())
                        .isInstanceOf(UnderlineHighlighter.UnderlineHighlightPainter.class));
    }

    @Test
    void search_returnsMinusOne_whenWordNotFound() {
        JTextArea ta = new JTextArea("nothing here");
        WordSearcher searcher = new WordSearcher(ta);

        assertThat(searcher.search("xyz")).isEqualTo(-1);
        assertThat(ta.getHighlighter().getHighlights()).isEmpty();
    }

    @Test
    void search_returnsMinusOne_andClearsHighlights_whenWordIsNullOrEmpty() {
        JTextArea ta = new JTextArea("abc abc");
        WordSearcher searcher = new WordSearcher(ta);
        searcher.search("abc");
        assertThat(ta.getHighlighter().getHighlights()).hasSize(2);

        // null und "" entfernen vorhandene Underline-Highlights und liefern -1.
        assertThat(searcher.search(null)).isEqualTo(-1);
        assertThat(ta.getHighlighter().getHighlights()).isEmpty();

        searcher.search("abc");
        assertThat(searcher.search("")).isEqualTo(-1);
        assertThat(ta.getHighlighter().getHighlights()).isEmpty();
    }

    @Test
    void search_replacesPreviousHighlightsOnNewSearch() {
        JTextArea ta = new JTextArea("foo bar foo bar");
        WordSearcher searcher = new WordSearcher(ta);

        searcher.search("foo");
        assertThat(ta.getHighlighter().getHighlights()).hasSize(2);

        searcher.search("bar");
        // Vorherige foo-Highlights sind entfernt, neue bar-Highlights gesetzt.
        Highlighter.Highlight[] highlights = ta.getHighlighter().getHighlights();
        assertThat(highlights).hasSize(2);
    }
}
