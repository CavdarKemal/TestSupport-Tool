package de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.view;

import de.creditreform.crefoteam.cte.tesun.util.TesunDateUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Date;

public class DateCellRenderer extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if ((value != null) && (value instanceof Date)) {
            // Use SimpleDateFormat class to get a formatted String from Date object.
            String strDate = TesunDateUtils.DATE_FORMAT_DD_MM_YYYY_HH_MM_SS.format((Date) value);
            // Sorting algorithm will work with model value. So you dont need to worry about the renderer's display value.
            this.setText(strDate);
        } else
            this.setText("");
        return this;
    }
}
