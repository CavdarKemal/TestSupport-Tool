package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;

import java.util.List;

public class XmlStreamListenerGroup {

    private final ProgressListenerIF progressListener;
    private final ProgressListenerGUI progressListenerGui;
    private int anzMatchingCriteria;

    public XmlStreamListenerGroup(ProgressListenerGUI progressListenerGui) {
        this(progressListenerGui, new ProgressListenerBridge(progressListenerGui));
    }

    public XmlStreamListenerGroup(ProgressListenerGUI progressListenerGui, ProgressListenerIF progressListener) {
        this.progressListener = progressListener;
        this.progressListenerGui = (progressListenerGui != null) ? progressListenerGui : new ProgressListenerGUINop();
    }

    /**
     * Erzeuge eine neuee Instanz vom {@link IPerEntryListener} und speichere diese. Die Methode ist
     * final, weil die Pflege der internen Liste nicht unterlaufen werden darf.
     */
    public final IPerEntryListener createPerEntryListener() {
        IPerEntryListener newListener = newPerEntryListener();
        return newListener;
    }

    public void notifyMatchingCriteria(List<String> matchingCriteriaList) {
        if (matchingCriteriaList != null) {
            anzMatchingCriteria += matchingCriteriaList.size();
        }
    }

    public int getAnzMatchingCriteria() {
        return anzMatchingCriteria;
    }

    public ProgressListenerIF getProgressListener() {
        return progressListener;
    }

    public ProgressListenerGUI getProgressListenerGui() {
        return progressListenerGui;
    }

    /**
     * Hook für abgeleitete Klassen, hier kann eine andere Implementierung für {@link IPerEntryListener}
     * integriert werden
     */
    protected IPerEntryListener newPerEntryListener() {
        return new PerEntryListenerDefaultImpl();
    }

    public void updateData(Object updateData) {
        getProgressListener().updateData(updateData);
    }

    public void updateProgress(List<Object> chunks) {
        getProgressListenerGui().updateProgress(chunks);
    }

}
