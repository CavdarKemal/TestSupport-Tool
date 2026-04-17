package de.creditreform.crefoteam.cte.testsupporttool.gui.base.model;

import org.junit.jupiter.api.Test;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import static org.assertj.core.api.Assertions.assertThat;

class ColumnsInfoTest {

    @Test
    void setColumnsInfos_appliesMinPrefMaxWidth() {
        TableColumn col = new TableColumn();
        new ColumnsInfo(50, 120, 300).setColumnsInfos(col);

        assertThat(col.getMinWidth()).isEqualTo(50);
        assertThat(col.getPreferredWidth()).isEqualTo(120);
        assertThat(col.getMaxWidth()).isEqualTo(300);
    }

    @Test
    void setColumnsInfos_zeroMaxWidth_doesNotChangeColumnMaxWidth() {
        TableColumn col = new TableColumn();
        int beforeMax = col.getMaxWidth();
        new ColumnsInfo(50, 120, 0).setColumnsInfos(col);

        // maxWidth==0 → setMaxWidth wird NICHT aufgerufen.
        assertThat(col.getMaxWidth()).isEqualTo(beforeMax);
        assertThat(col.getMinWidth()).isEqualTo(50);
        assertThat(col.getPreferredWidth()).isEqualTo(120);
    }

    @Test
    void staticHelper_appliesArrayToColumnModel() {
        DefaultTableColumnModel model = new DefaultTableColumnModel();
        TableColumn c0 = new TableColumn(0);
        TableColumn c1 = new TableColumn(1);
        model.addColumn(c0);
        model.addColumn(c1);

        ColumnsInfo[] infos = {
                new ColumnsInfo(10, 50, 100),
                new ColumnsInfo(20, 80, 0)
        };

        ColumnsInfo.setColumnsInfos(model, infos);

        assertThat(c0.getMinWidth()).isEqualTo(10);
        assertThat(c0.getPreferredWidth()).isEqualTo(50);
        assertThat(c0.getMaxWidth()).isEqualTo(100);
        assertThat(c1.getMinWidth()).isEqualTo(20);
        assertThat(c1.getPreferredWidth()).isEqualTo(80);
    }
}
