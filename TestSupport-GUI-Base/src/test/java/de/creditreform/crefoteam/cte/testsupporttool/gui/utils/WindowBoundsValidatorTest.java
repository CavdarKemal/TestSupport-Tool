package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import org.junit.jupiter.api.Test;

import java.awt.Rectangle;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit-Tests fuer die reine Validate-Funktion. Die Convenience-Variante
 * {@code validate(Rectangle)} (die das GraphicsEnvironment selbst abfragt)
 * wird hier bewusst nicht getestet — die ist Headless-anfaellig.
 */
class WindowBoundsValidatorTest {

    private static final Rectangle SCREEN = new Rectangle(0, 0, 1920, 1080);
    private static final Rectangle DEFAULT = new Rectangle(100, 100, 800, 600);

    @Test
    void requested_fullyVisible_returnedAsIs() {
        Rectangle requested = new Rectangle(50, 50, 800, 600);
        assertThat(WindowBoundsValidator.validate(requested, List.of(SCREEN), DEFAULT))
                .isSameAs(requested);
    }

    @Test
    void requested_halfOnScreen_stillReturnedAsIs() {
        // Genau 50% sichtbar (Schwellwert MIN_VISIBLE_FRACTION) — soll akzeptiert werden.
        Rectangle requested = new Rectangle(1920 - 400, 0, 800, 600);
        assertThat(WindowBoundsValidator.validate(requested, List.of(SCREEN), DEFAULT))
                .isSameAs(requested);
    }

    @Test
    void requested_mostlyOffScreen_fallsBackToDefault() {
        // Nur 100x600 von 800x600 sichtbar = 12.5% < 50%.
        Rectangle requested = new Rectangle(1920 - 100, 0, 800, 600);
        assertThat(WindowBoundsValidator.validate(requested, List.of(SCREEN), DEFAULT))
                .isSameAs(DEFAULT);
    }

    @Test
    void requested_completelyOffScreen_fallsBackToDefault() {
        Rectangle requested = new Rectangle(5000, 5000, 800, 600);
        assertThat(WindowBoundsValidator.validate(requested, List.of(SCREEN), DEFAULT))
                .isSameAs(DEFAULT);
    }

    @Test
    void requested_null_fallsBackToDefault() {
        assertThat(WindowBoundsValidator.validate(null, List.of(SCREEN), DEFAULT))
                .isSameAs(DEFAULT);
    }

    @Test
    void requested_zeroOrNegativeSize_fallsBackToDefault() {
        assertThat(WindowBoundsValidator.validate(new Rectangle(0, 0, 0, 600), List.of(SCREEN), DEFAULT))
                .isSameAs(DEFAULT);
        assertThat(WindowBoundsValidator.validate(new Rectangle(0, 0, 800, 0), List.of(SCREEN), DEFAULT))
                .isSameAs(DEFAULT);
        assertThat(WindowBoundsValidator.validate(new Rectangle(0, 0, -10, 600), List.of(SCREEN), DEFAULT))
                .isSameAs(DEFAULT);
    }

    @Test
    void noScreens_fallsBackToDefault() {
        Rectangle requested = new Rectangle(50, 50, 800, 600);
        assertThat(WindowBoundsValidator.validate(requested, List.of(), DEFAULT))
                .isSameAs(DEFAULT);
        assertThat(WindowBoundsValidator.validate(requested, null, DEFAULT))
                .isSameAs(DEFAULT);
    }

    @Test
    void multiScreen_visibilityAccumulatesAcrossScreens() {
        Rectangle screenLeft = new Rectangle(0, 0, 1000, 1080);
        Rectangle screenRight = new Rectangle(1000, 0, 1000, 1080);
        // Fenster spannt ueber beide Screens — voll sichtbar via Akkumulation.
        Rectangle requested = new Rectangle(800, 0, 400, 600);
        assertThat(WindowBoundsValidator.validate(requested, List.of(screenLeft, screenRight), DEFAULT))
                .isSameAs(requested);
    }
}
