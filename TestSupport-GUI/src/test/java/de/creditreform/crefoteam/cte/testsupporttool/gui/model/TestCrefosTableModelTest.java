package de.creditreform.crefoteam.cte.testsupporttool.gui.model;

import de.creditreform.crefoteam.cte.tesun.util.TestCrefo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TestCrefosTableModelTest {

    @Test
    void columnSetup_matchesConstant() {
        TestCrefosTableModel model = new TestCrefosTableModel(List.of());
        assertThat(model.getColumnCount()).isEqualTo(7);
        assertThat(model.getColumnName(0)).isEqualTo("#");
        assertThat(model.getColumnName(1)).isEqualTo("Testfall");
        assertThat(model.getColumnName(6)).isEqualTo("Exprted");
    }

    @Test
    void getValueAt_returnsAllSevenColumns() {
        // TestCrefo(String, Long) setzt pseudoCrefoNr — itsqTestCrefoNr per Setter.
        TestCrefo crefo = new TestCrefo("p001", 2222222222L);
        crefo.setItsqTestCrefoNr(1111111111L);
        crefo.setTestFallInfo("info-txt");
        crefo.setShouldBeExported(true);
        crefo.setExported(false);
        crefo.setActivated(true);

        TestCrefosTableModel model = new TestCrefosTableModel(List.of(crefo));
        assertThat(model.getValueAt(0, 0)).isEqualTo(true);
        assertThat(model.getValueAt(0, 1)).isEqualTo("p001");
        assertThat(model.getValueAt(0, 2)).isEqualTo(1111111111L);
        assertThat(model.getValueAt(0, 3)).isEqualTo(2222222222L);
        assertThat(model.getValueAt(0, 4)).isEqualTo("info-txt");
        assertThat(model.getValueAt(0, 5)).isEqualTo(true);
        assertThat(model.getValueAt(0, 6)).isEqualTo(false);
    }

    @Test
    void setValueAt_crefoNumbers_parsedAsLong() {
        TestCrefo crefo = new TestCrefo("p001", 100L);
        TestCrefosTableModel model = new TestCrefosTableModel(List.of(crefo));
        model.setValueAt("9876543210", 0, 2);
        model.setValueAt("1234567890", 0, 3);
        assertThat(crefo.getItsqTestCrefoNr()).isEqualTo(9876543210L);
        assertThat(crefo.getPseudoCrefoNr()).isEqualTo(1234567890L);
    }

    @Test
    void setValueAt_flagColumns_parsedAsBoolean() {
        TestCrefo crefo = new TestCrefo("p001", 100L);
        TestCrefosTableModel model = new TestCrefosTableModel(List.of(crefo));
        model.setValueAt("true", 0, 5);
        model.setValueAt("true", 0, 6);
        assertThat(crefo.isShouldBeExported()).isTrue();
        assertThat(crefo.isExported()).isTrue();
    }

    @Test
    void isCellEditable_onlyFirstColumn() {
        TestCrefosTableModel model = new TestCrefosTableModel(List.of());
        assertThat(model.isCellEditable(0, 0)).isTrue();
        assertThat(model.isCellEditable(0, 3)).isFalse();
    }
}
