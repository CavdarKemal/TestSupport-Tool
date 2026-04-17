package de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.design.LogEntryDialogPanel;
import de.creditreform.crefoteam.cte.testsupporttool.logsearch.LogEntry;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LogEntryDialogView extends LogEntryDialogPanel {

    public LogEntryDialogView(Frame owner) {
        super(owner);
        getButtonClose().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
    }

    public void setModel(LogEntry logEntry) {
        getTextFieldLogType().setText(logEntry.getType().name());
        getTextFieldLogDate().setText(logEntry.getLogDateAsString());
        getTextFieldLogPackage().setText(logEntry.getPackg());
        StringBuilder textBuf = new StringBuilder();
        List<String> infoList = logEntry.getInfoList();
        for (String info : infoList) {
            textBuf.append(info);
            textBuf.append("\n");
        }
        getTextAreaLogInfos().setText(textBuf.toString());
    }

}
