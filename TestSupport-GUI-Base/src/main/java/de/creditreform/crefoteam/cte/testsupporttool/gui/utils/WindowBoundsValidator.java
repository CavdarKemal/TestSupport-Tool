package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * Validiert persistierte Fenster-Bounds (z.B. aus -config.properties) gegen
 * die aktuell verfügbaren GraphicsDevices. Wird gebraucht, damit eine Fenster-
 * Position, die auf einem anderen Monitor-Setup gespeichert wurde, nicht dazu
 * führt, dass das Fenster unsichtbar off-screen landet — Jemmy/AssertJ-Swing
 * Tests schlagen in dem Fall sporadisch fehl, und echte User sehen die App
 * einfach nicht.
 *
 * <p>Die Kern-Logik in {@link #validate(Rectangle, List, Rectangle)} ist bewusst
 * eine reine Funktion, die die verfügbaren Screen-Bounds als Parameter bekommt —
 * so ist sie ohne Mock-Framework testbar.
 */
public final class WindowBoundsValidator {

    /**
     * Minimaler sichtbarer Überlapp, damit Bounds als "sichtbar genug" gelten.
     * Wenn weniger als dieser Anteil des Fensters sichtbar ist, wird auf den
     * Default-Bereich zurückgefallen.
     */
    private static final double MIN_VISIBLE_FRACTION = 0.5;

    private WindowBoundsValidator() {
        // Utility class
    }

    /**
     * Convenience-Variante, die die Screen-Bounds selbst vom
     * {@link GraphicsEnvironment} abfragt.
     */
    public static Rectangle validate(Rectangle requested) {
        return validate(requested, collectScreenBounds(), defaultPrimaryScreenBounds());
    }

    /**
     * Reine Funktion — prüft ob das requested-Rechteck zu mindestens
     * {@value #MIN_VISIBLE_FRACTION} sichtbar auf einem der screens liegt.
     * Wenn nicht, wird {@code defaultBounds} zurückgegeben (üblicherweise
     * ein zentriertes Fenster auf dem Primary-Screen).
     *
     * @param requested      das gewünschte Rechteck (aus der Config)
     * @param screens        die aktuell verfügbaren Screen-Bounds (nicht leer)
     * @param defaultBounds  Fallback, wenn requested nicht hinreichend sichtbar ist
     * @return ein Rechteck, das garantiert sichtbar auf einem der screens liegt
     */
    public static Rectangle validate(Rectangle requested, List<Rectangle> screens, Rectangle defaultBounds) {
        if (requested == null || requested.width <= 0 || requested.height <= 0) {
            return defaultBounds;
        }
        if (screens == null || screens.isEmpty()) {
            return defaultBounds;
        }

        // Berechne wie viel des requested-Rechtecks mit irgendeinem Screen überlappt
        long requestedArea = (long) requested.width * requested.height;
        long visibleArea = 0;
        for (Rectangle screen : screens) {
            Rectangle intersection = requested.intersection(screen);
            if (!intersection.isEmpty()) {
                visibleArea += (long) intersection.width * intersection.height;
            }
        }

        double visibleFraction = requestedArea > 0 ? (double) visibleArea / requestedArea : 0.0;
        if (visibleFraction >= MIN_VISIBLE_FRACTION) {
            return requested;
        }
        return defaultBounds;
    }

    /**
     * Liest die Bounds aller aktuell angeschlossenen Monitore. Auf Headless-Systemen
     * (z.B. CI) liefert das eine leere Liste.
     */
    private static List<Rectangle> collectScreenBounds() {
        List<Rectangle> result = new ArrayList<>();
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            if (ge.isHeadlessInstance()) {
                return result;
            }
            for (GraphicsDevice gd : ge.getScreenDevices()) {
                for (GraphicsConfiguration gc : gd.getConfigurations()) {
                    result.add(gc.getBounds());
                }
            }
        } catch (Throwable ignored) {
            // Defensive: wenn GraphicsEnvironment Probleme macht, nicht crashen
        }
        return result;
    }

    /**
     * Liefert ein sinnvolles Default-Rechteck zentriert auf dem Primary-Screen.
     * Fallback 200,100,800,400 wenn kein Screen verfügbar ist.
     */
    private static Rectangle defaultPrimaryScreenBounds() {
        int w = 1200;
        int h = 800;
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            if (!ge.isHeadlessInstance()) {
                Rectangle primary = ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
                int x = primary.x + Math.max(0, (primary.width - w) / 2);
                int y = primary.y + Math.max(0, (primary.height - h) / 2);
                return new Rectangle(x, y, w, h);
            }
        } catch (Throwable ignored) {
            // Fallthrough zum Hard-Default
        }
        return new Rectangle(200, 100, w, h);
    }
}
