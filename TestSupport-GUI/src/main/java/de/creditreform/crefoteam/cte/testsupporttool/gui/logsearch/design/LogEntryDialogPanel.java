/*
 * Created by JFormDesigner on Sun Jun 28 19:20:20 CEST 2015
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.design;

import javax.swing.*;
import java.awt.*;

/**
 * @author Uzayli Temel
 */
public class LogEntryDialogPanel extends JDialog {
    public LogEntryDialogPanel(Frame owner) {
        super(owner);
        initComponents();
    }

    public LogEntryDialogPanel(Dialog owner) {
        super(owner);
        initComponents();
    }

    public JLabel getLabelLogType() {
        return labelLogType;
    }

    public JTextField getTextFieldLogType() {
        return textFieldLogType;
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

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
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
        setTitle("LOG - Eintrag");
        setName("this");
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout) contentPane.getLayout()).columnWidths = new int[]{0, 82, 0, 139, 0, 0};
        ((GridBagLayout) contentPane.getLayout()).rowHeights = new int[]{0, 0, 0, 130, 0, 0};
        ((GridBagLayout) contentPane.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 1.0E-4};
        ((GridBagLayout) contentPane.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 1.0E-4};

        //---- labelLogType ----
        labelLogType.setText("Typ:");
        labelLogType.setName("labelLogType");
        contentPane.add(labelLogType, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 2, 7, 7), 0, 0));

        //---- textFieldLogType ----
        textFieldLogType.setEditable(false);
        textFieldLogType.setName("textFieldLogType");
        contentPane.add(textFieldLogType, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 7), 0, 0));

        //---- labelLogDate ----
        labelLogDate.setText("Datum:");
        labelLogDate.setName("labelLogDate");
        contentPane.add(labelLogDate, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                new Insets(2, 2, 7, 7), 0, 0));

        //---- textFieldLogDate ----
        textFieldLogDate.setEditable(false);
        textFieldLogDate.setName("textFieldLogDate");
        contentPane.add(textFieldLogDate, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 7), 0, 0));

        //---- labelLogPackage ----
        labelLogPackage.setText("Package:");
        labelLogPackage.setName("labelLogPackage");
        contentPane.add(labelLogPackage, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 2, 7, 7), 0, 0));

        //---- textFieldLogPackage ----
        textFieldLogPackage.setEditable(false);
        textFieldLogPackage.setName("textFieldLogPackage");
        contentPane.add(textFieldLogPackage, new GridBagConstraints(1, 2, 4, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 2), 0, 0));

        //---- labelLogInfos ----
        labelLogInfos.setText("Info:");
        labelLogInfos.setName("labelLogInfos");
        contentPane.add(labelLogInfos, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
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
        contentPane.add(scrollPaneLogInfos, new GridBagConstraints(1, 3, 4, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 7, 2), 0, 0));

        //---- buttonClose ----
        buttonClose.setText("OK");
        buttonClose.setIcon(new ImageIcon(getClass().getResource("/icons/folder_view.png")));
        buttonClose.setName("buttonClose");
        contentPane.add(buttonClose, new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
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
