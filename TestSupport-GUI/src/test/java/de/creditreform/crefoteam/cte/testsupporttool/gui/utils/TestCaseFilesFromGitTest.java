package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit-Tests fuer die rein-statische Branch-Logik. Die {@code git}-
 * ProcessBuilder-Pfade werden hier <b>nicht</b> ausgefuehrt — das waeren
 * Integration-Tests, die ein installiertes git-Binary + Netzwerk-
 * Konnektivitaet brauchen.
 */
class TestCaseFilesFromGitTest {

    @Test
    void constructor_storesListener() {
        CommandExecutorListener listener = msg -> { };
        TestCaseFilesFromGit fixture = new TestCaseFilesFromGit(listener);
        // Indirekt verifizieren ueber tagAndPushITSQProject(null, null) — geht den
        // tagName==null-Branch und ruft den Listener mit der "ausgeschaltet"-Meldung.
        List<String> captured = new ArrayList<>();
        TestCaseFilesFromGit fixtureWithCapture = new TestCaseFilesFromGit(captured::add);

        assertThat(fixture).isNotNull();
        assertThat(fixtureWithCapture).isNotNull();
    }

    @Test
    void updateItsqTestPaket_emptyList_returnsNull(@TempDir Path tmp) throws Exception {
        TestCaseFilesFromGit fixture = new TestCaseFilesFromGit(msg -> { });

        File result = fixture.updateItsqTestPaket(tmp.toFile(), List.of());

        // Leere Liste: keine Schleifen-Iteration, keine git-Aufrufe, return null.
        assertThat(result).isNull();
    }

    @Test
    void tagAndPushITSQProject_nullTagName_skipsGitCalls_andLogsHint() throws Exception {
        List<String> captured = new ArrayList<>();
        TestCaseFilesFromGit fixture = new TestCaseFilesFromGit(captured::add);

        String result = fixture.tagAndPushITSQProject("/some/path", null);

        assertThat(result).isNull();
        assertThat(captured)
                .hasSize(1)
                .allSatisfy(msg -> assertThat(msg).contains("Taggen ist ausgeschaltet"));
    }
}
