package de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch;

import de.creditreform.crefoteam.cte.testsupporttool.gui.BaseGUITest;
import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.model.LogFilesTableModel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.model.SearchResultsTableModel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.view.SearchLogsView;
import de.creditreform.crefoteam.cte.testsupporttool.logsearch.LogEntry;
import de.creditreform.crefoteam.cte.testsupporttool.logsearch.SearchableLogFileFactory;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Jemmy-basierter End-to-End-Test fuer {@link SearchLOGsGUI} (Phase J).
 *
 * <p>Laedt eine mitgelieferte LOG-Datei programmatisch ins
 * {@link LogFilesTableModel}, stoesst via Button-Klick die Suche an, und
 * verifiziert, dass die {@link SearchResultsTableModel} anschliessend
 * Treffer enthaelt. Das Befuellen via TableModel umgeht den FileChooser
 * — der liefe in Jemmy plattformspezifisch und waere flaky.
 */
@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class SearchLOGsGUITest extends BaseGUITest {

    @BeforeAll
    static void assumeNotHeadless() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM — Jemmy braucht echtes Display");
    }

    @BeforeEach
    void openGui() throws Exception {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        SearchLOGsGUI gui = createOnEdt(() -> {
            SearchLOGsGUI g = new SearchLOGsGUI(env);
            g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            g.setVisible(true);
            return g;
        });
        setGuiFrame(gui);
    }

    @Test
    @Timeout(60)
    void logfileAdden_sucheStarten_liefertTreffer() throws Exception {
        SearchLOGsGUI gui = (SearchLOGsGUI) guiFrame;
        SearchLogsView view = gui.getSearchLogsView();

        File logFile = resolveTestLogFile();
        assertThat(logFile).exists();

        // LOG-Datei direkt ins Model schieben — vermeidet Plattform-spezifischen FileChooser.
        SwingUtilities.invokeAndWait(() -> {
            try {
                JTable tableLogFiles = view.getLogFilesView().getTableLogFiles();
                LogFilesTableModel model = (LogFilesTableModel) tableLogFiles.getModel();
                model.addRow(SearchableLogFileFactory.createInstanceFor(logFile.getAbsolutePath()));
                // Auswahl triggert Button-Zustand (initControlsState).
                tableLogFiles.getSelectionModel().setSelectionInterval(0, 0);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Damit "Suche starten" aktiv wird, muss das SearchResultsView den aktuellen
        // LogFilesTableModel-Stand kennen. Das passiert im Original via
        // TableModelListener (tableChanged) — wir triggern es hier explizit.
        SwingUtilities.invokeAndWait(() -> {
            JTable tableLogFiles = view.getLogFilesView().getTableLogFiles();
            LogFilesTableModel model = (LogFilesTableModel) tableLogFiles.getModel();
            view.getSearchResultsView().setLogFilesTableModel(model);
        });

        waitFor(() -> view.getSearchResultsView().getButtonSearch().isEnabled(), 5_000);

        // "Suche starten" ausloesen.
        SwingUtilities.invokeAndWait(() -> view.getSearchResultsView().getButtonSearch().doClick());

        // SwingWorker laeuft asynchron — warten, bis mind. 1 Treffer im Model ist
        // ODER der Button wieder auf "Suche starten" steht (= Worker ist fertig).
        waitFor(() -> {
            SearchResultsTableModel resultsModel =
                    (SearchResultsTableModel) view.getSearchResultsView().getTableSearchResults().getModel();
            return resultsModel.getRowCount() > 0
                    && view.getSearchResultsView().getButtonSearch().getText().contains("starten");
        }, 30_000);

        SearchResultsTableModel resultsModel =
                (SearchResultsTableModel) view.getSearchResultsView().getTableSearchResults().getModel();
        assertThat(resultsModel.getRowCount())
                .as("LOG-Suche muss Treffer liefern")
                .isGreaterThan(0);

        // Stichprobe: erster Treffer ist ein LogEntry mit befuelltem Datum + Typ.
        LogEntry firstHit = (LogEntry) resultsModel.getRow(0);
        assertThat(firstHit.getLogDate()).isNotNull();
        assertThat(firstHit.getType()).isNotNull();
    }

    private static File resolveTestLogFile() {
        URL url = SearchLOGsGUITest.class.getClassLoader().getResource("all_flux_out.log");
        if (url == null) {
            throw new IllegalStateException(
                    "Test-Ressource 'all_flux_out.log' nicht auf dem Classpath gefunden.");
        }
        return new File(url.getFile());
    }

    private static void waitFor(java.util.function.BooleanSupplier cond, long timeoutMs) throws Exception {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            boolean[] ok = {false};
            SwingUtilities.invokeAndWait(() -> ok[0] = cond.getAsBoolean());
            if (ok[0]) return;
            Thread.sleep(100);
        }
        throw new AssertionError("Bedingung nicht innerhalb " + timeoutMs + " ms erfuellt");
    }
}
