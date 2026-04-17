/*
 * Created by JFormDesigner on Sun Jun 28 19:16:57 CEST 2015
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.design;

import javax.swing.*;
import java.awt.*;

/**
 * @author Uzayli Temel
 */
public class LogEntryPanel extends JPanel {
    public LogEntryPanel() {
        initComponents();
    }

    public JLabel getLabelLogEntry() {
        return labelLogEntry;
    }

    public JLabel getLabelLogType() {
        return labelLogType;
    }

    public JLabel getLabelLogDate() {
        return labelLogDate;
    }

    public JTextField getTextFieldLogDate() {
        return textFieldLogDate;
    }

    public JLabel getLabelLogPackage() {
        return labelLogPackage;
    }

    public JTextField getTextFieldLogPackage() {
        return textFieldLogPackage;
    }

    public JLabel getLabelLogInfos() {
        return labelLogInfos;
    }

    public JScrollPane getScrollPaneLogInfos() {
        return scrollPaneLogInfos;
    }

    public JTextArea getTextAreaLogInfos() {
        return textAreaLogInfos;
    }

    public JButton getButtonClose() {
        return buttonClose;
    }

    public JTextField getTextFieldLogType() {
        return textFieldLogType;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        labelLogEntry = new JLabel();
        labelLogType = new JLabel();
        textFieldLogType = new JTextField();
        labelLogDate = new JLabel();
        textFieldLogDate = new JTextField();
        labelLogPackage = new JLabel();
        textFieldLogPackage = new JTextField();
        labelLogInfos = new JLabel();
        scrollPaneLogInfos = new JScrollPane();
        textAreaLogInfos = new JTextArea();
        buttonClose = new JButton();

        //======== this ========
        setName("this");
        setLayout(new GridBagLayout());
        ((GridBagLayout) getLayout()).columnWidths = new int[]{92, 90, 70, 101, 95, 0};
        ((GridBagLayout) getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0, 0};
        ((GridBagLayout) getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 1.0E-4};
        ((GridBagLayout) getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 1.0E-4};

        //---- labelLogEntry ----
        labelLogEntry.setText("LOG-Eintrag");
        labelLogEntry.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
        labelLogEntry.setName("labelLogEntry");
        add(labelLogEntry, new GridBagConstraints(0, 0, 5, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 5, 7, 2), 0, 0));

        //---- labelLogType ----
        labelLogType.setText("Typ:");
        labelLogType.setName("labelLogType");
        add(labelLogType, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 2, 7, 7), 0, 0));

        //---- textFieldLogType ----
        textFieldLogType.setEditable(false);
        textFieldLogType.setName("textFieldLogType");
        add(textFieldLogType, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 7), 0, 0));

        //---- labelLogDate ----
        labelLogDate.setText("Datum:");
        labelLogDate.setName("labelLogDate");
        add(labelLogDate, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                new Insets(2, 2, 7, 7), 0, 0));

        //---- textFieldLogDate ----
        textFieldLogDate.setEditable(false);
        textFieldLogDate.setName("textFieldLogDate");
        add(textFieldLogDate, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 7), 0, 0));

        //---- labelLogPackage ----
        labelLogPackage.setText("Package:");
        labelLogPackage.setName("labelLogPackage");
        add(labelLogPackage, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 2, 7, 7), 0, 0));

        //---- textFieldLogPackage ----
        textFieldLogPackage.setEditable(false);
        textFieldLogPackage.setName("textFieldLogPackage");
        add(textFieldLogPackage, new GridBagConstraints(1, 2, 4, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 2), 0, 0));

        //---- labelLogInfos ----
        labelLogInfos.setText("Info:");
        labelLogInfos.setName("labelLogInfos");
        add(labelLogInfos, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 2, 7, 7), 0, 0));

        //======== scrollPaneLogInfos ========
        {
            scrollPaneLogInfos.setName("scrollPaneLogInfos");

            //---- textAreaLogInfos ----
            textAreaLogInfos.setEditable(false);
            textAreaLogInfos.setName("textAreaLogInfos");
            scrollPaneLogInfos.setViewportView(textAreaLogInfos);
        }
        add(scrollPaneLogInfos, new GridBagConstraints(1, 3, 4, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 2), 0, 0));

        //---- buttonClose ----
        buttonClose.setText("OK");
        buttonClose.setIcon(new ImageIcon(getClass().getResource("/icons/folder_view.png")));
        buttonClose.setName("buttonClose");
        add(buttonClose, new GridBagConstraints(1, 4, 4, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel labelLogEntry;
    private JLabel labelLogType;
    private JTextField textFieldLogType;
    private JLabel labelLogDate;
    private JTextField textFieldLogDate;
    private JLabel labelLogPackage;
    private JTextField textFieldLogPackage;
    private JLabel labelLogInfos;
    private JScrollPane scrollPaneLogInfos;
    private JTextArea textAreaLogInfos;
    private JButton buttonClose;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
