package de.creditreform.crefoteam.cte.testsupporttool.logsearch;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SearchableLogFileFactoryTest {

    @Test
    void emptyFileName_throwsUnsupported() {
        assertThatThrownBy(() -> SearchableLogFileFactory.createInstanceFor(""))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("Typ der Log-Datei '' unbekannt!");
    }

    @Test
    void unknownExtension_throwsUnsupported() {
        assertThatThrownBy(() -> SearchableLogFileFactory.createInstanceFor("blabla.xyz"))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("Typ der Log-Datei 'blabla.xyz' unbekannt!");
    }

    @Test
    void dotLog_returnsNormalLogFile() throws URISyntaxException, FileNotFoundException {
        String logFileName = SearchableLogFileFactoryTest.class.getResource("/all_flux_out.log").toURI().getPath();
        SearchableLogFile logFile = SearchableLogFileFactory.createInstanceFor(logFileName);
        assertThat(logFile).isInstanceOf(NormalLogFile.class);
    }

    @Test
    void dotGz_returnsZippedLogFile() throws URISyntaxException, FileNotFoundException {
        String logFileName = SearchableLogFileFactoryTest.class.getResource("/all_flux_out.log.1.gz").toURI().getPath();
        SearchableLogFile logFile = SearchableLogFileFactory.createInstanceFor(logFileName);
        assertThat(logFile).isInstanceOf(ZippedLogFile.class);
    }
}
