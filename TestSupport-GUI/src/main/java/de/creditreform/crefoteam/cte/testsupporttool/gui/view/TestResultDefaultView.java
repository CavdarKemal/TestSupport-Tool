package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.design.TestResultDefaultPanel;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public abstract class TestResultDefaultView extends TestResultDefaultPanel {

    private final Highlighter.HighlightPainter normalPainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 255, 150));
    private List<Integer> matchPositions = new ArrayList<>();
    private int currentMatchIndex = -1;
    private String lastSearchTerm = "";

    private enum SearchStatus {
        SEARCH_EMPTY,
        NO_MATCHES,
        MATCHES_FOUND
    }

    public TestResultDefaultView() {
        super();
        initControls();
        initListeners();

        // Ein angepasstes Caret erstellen, das die Selektion nicht versteckt
        DefaultCaret persistentCaret = new DefaultCaret() {
            @Override
            public void focusLost(FocusEvent e) {
                // Normalerweise würde super.focusLost die Selektion unsichtbar machen.
                // Wir rufen super nicht auf oder erzwingen die Sichtbarkeit:
                setVisible(true);
                setSelectionVisible(true);
            }
        };
        // Die Blink-Rate vom alten Caret übernehmen (damit es natürlich aussieht)
        persistentCaret.setBlinkRate(getTextAreaFileSrc().getCaret().getBlinkRate());
        // Das neue Caret der TextArea zuweisen
        getTextAreaFileSrc().setCaret(persistentCaret);
        // Wichtig: Die Selektionsfarbe setzen
        getTextAreaFileSrc().setSelectionColor(Color.ORANGE);
        getTextAreaFileSrc().setSelectedTextColor(Color.BLACK);
    }

    private void initControls() {
    }

    private void initListeners() {
        getTextFieldSearch().addActionListener(event -> doSearch());
        // Echtzeit-Suche bei jeder Änderung des Dokuments
        getTextFieldSearch().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                doSearch();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                doSearch();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                // Wird bei Attributänderungen aufgerufen, meist nicht relevant für Text
                doSearch();
            }
        });
        // Navigation Weiter
        getButtonNextMath().addActionListener(event -> {
            if (!matchPositions.isEmpty()) {
                currentMatchIndex++;
                if (currentMatchIndex >= matchPositions.size()) currentMatchIndex = 0;
                navigateToCurrentMatch(true); // WAHR: Hier darf der Fokus zur TextArea
            }
        });

        // Navigation Zurück
        getButtonPrevMatch().addActionListener(event -> {
            if (!matchPositions.isEmpty()) {
                currentMatchIndex--;
                if (currentMatchIndex < 0) currentMatchIndex = matchPositions.size() - 1;
                navigateToCurrentMatch();
            }
        });
    }

    private void navigateToCurrentMatch(boolean shouldRequestFocus) {
        if (currentMatchIndex < 0 || matchPositions.isEmpty()) return;

        int start = matchPositions.get(currentMatchIndex);
        int length = lastSearchTerm.length();

        // Nur Fokus anfordern, wenn explizit gewünscht (z.B. durch Button-Klick)
        if (shouldRequestFocus) {
            getTextAreaFileSrc().requestFocusInWindow();
        }

        // Selektion setzen
        getTextAreaFileSrc().setSelectionStart(start);
        getTextAreaFileSrc().setSelectionEnd(start + length);

        // Scrollen
        try {
            Rectangle viewRect = getTextAreaFileSrc().modelToView(start);
            if (viewRect != null) {
                getTextAreaFileSrc().scrollRectToVisible(viewRect);
            }
        } catch (BadLocationException ignored) {
        }
    }

    private SearchStatus doSearch() {
        String query = getTextFieldSearch().getText().toLowerCase();

        // Vorbereitung
        lastSearchTerm = query;
        matchPositions.clear();
        currentMatchIndex = -1;
        Highlighter highlighter = getTextAreaFileSrc().getHighlighter();
        highlighter.removeAllHighlights();

        // FALL 1: Suchfeld ist leer
        if (query.isEmpty()) {
            getTextFieldSearch().setBackground(Color.WHITE); // Oder System-Farbe
            return SearchStatus.SEARCH_EMPTY;
        }

        String text = getTextAreaFileSrc().getText().toLowerCase();
        int index = text.indexOf(query);

        // Suche durchführen
        while (index >= 0) {
            matchPositions.add(index);
            try {
                highlighter.addHighlight(index, index + query.length(), normalPainter);
            } catch (BadLocationException ex) {
                // Logger oder Exception Handling
            }
            index = text.indexOf(query, index + query.length());
        }

        // FALL 2: Keine Treffer
        if (matchPositions.isEmpty()) {
            getTextFieldSearch().setBackground(new Color(255, 200, 200)); // Hellrot
            return SearchStatus.NO_MATCHES;
        }
        // FALL 3: Treffer gefunden
        if (!matchPositions.isEmpty()) {
            currentMatchIndex = 0;
            navigateToCurrentMatch(false); // FALSCH: Fokus im Suchfeld lassen!
        }
        return SearchStatus.MATCHES_FOUND;
    }

    private void navigateToCurrentMatch() {
        if (currentMatchIndex < 0 || matchPositions.isEmpty()) {
            return;
        }
        int start = matchPositions.get(currentMatchIndex);
        int length = lastSearchTerm.length();
        // 1. Fokus auf die TextArea, damit die Selektion sichtbar wird
        getTextAreaFileSrc().requestFocusInWindow();
        // 2. Den Text selektieren.
        // Da wir oben setSelectionColor(Color.ORANGE) gesetzt haben, wird der gelbe Highlight-Bereich jetzt orange "überstrahlt".
        getTextAreaFileSrc().setSelectionStart(start);
        getTextAreaFileSrc().setSelectionEnd(start + length);
        // 3. Scrollen zum Treffer
        try {
            Rectangle viewRect = getTextAreaFileSrc().modelToView(start);
            if (viewRect != null) {
                getTextAreaFileSrc().scrollRectToVisible(viewRect);
            }
        } catch (BadLocationException ignored) {
        }
    }
}
