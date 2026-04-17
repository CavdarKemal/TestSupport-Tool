package de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.view;

import de.creditreform.crefoteam.cte.tesun.util.TesunDateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import javax.swing.JTable;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
class DateCellRendererTest {

    private final DateCellRenderer renderer = new DateCellRenderer();
    private final JTable table = new JTable();

    @Test
    void dateValue_isFormattedWithDateUtils() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.MARCH, 15, 14, 30, 45);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        renderer.getTableCellRendererComponent(table, date, false, false, 0, 0);
        assertThat(renderer.getText()).isEqualTo(TesunDateUtils.DATE_FORMAT_DD_MM_YYYY_HH_MM_SS.format(date));
    }

    @Test
    void nonDateValue_rendersAsEmptyString() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        renderer.getTableCellRendererComponent(table, "not-a-date", false, false, 0, 0);
        assertThat(renderer.getText()).isEmpty();
    }

    @Test
    void nullValue_rendersAsEmptyString() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        renderer.getTableCellRendererComponent(table, null, false, false, 0, 0);
        assertThat(renderer.getText()).isEmpty();
    }

    @Test
    void rendererReturnsSelfFromRenderCall() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless-JVM");
        Component c = renderer.getTableCellRendererComponent(table, null, false, false, 0, 0);
        assertThat(c).isSameAs(renderer);
    }
}
