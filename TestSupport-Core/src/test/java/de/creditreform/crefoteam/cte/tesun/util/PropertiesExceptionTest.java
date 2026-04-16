package de.creditreform.crefoteam.cte.tesun.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PropertiesExceptionTest {

    @Test
    void messageOnlyConstructor_setsMessageAndNullCause() {
        PropertiesException ex = new PropertiesException("boom");

        assertThat(ex.getMessage()).isEqualTo("boom");
        assertThat(ex.getCause()).isNull();
    }

    @Test
    void messageAndCauseConstructor_propagatesBoth() {
        Throwable cause = new IllegalStateException("root");
        PropertiesException ex = new PropertiesException("wrapped", cause);

        assertThat(ex.getMessage()).isEqualTo("wrapped");
        assertThat(ex.getCause()).isSameAs(cause);
    }

    @Test
    void isCheckedException() {
        assertThat(Exception.class.isAssignableFrom(PropertiesException.class)).isTrue();
        assertThat(RuntimeException.class.isAssignableFrom(PropertiesException.class)).isFalse();
    }
}
