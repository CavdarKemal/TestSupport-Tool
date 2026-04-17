/*
 * Created by JFormDesigner on Thu Aug 16 16:27:28 CEST 2018
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.jvm.design;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * @author Kemal Cavdar
 */
public class JvmJobExecutionsPanel extends JPanel {
    public JvmJobExecutionsPanel() {
        super();
        initComponents();
    }

    public JLabel getLabelJobName() {
        return labelJobName;
    }

    public JTextField getTextFieldJobName() {
        return textFieldJobName;
    }

    public JLabel getLabelId() {
        return labelId;
    }

    public JTextField getTextFieldId() {
        return textFieldId;
    }

    public JLabel getLabelJobId() {
        return labelJobId;
    }

    public JTextField getTextFieldJobId() {
        return textFieldJobId;
    }

    public JLabel getLabelStatus() {
        return labelStatus;
    }

    public JTextField getTextFieldStatus() {
        return textFieldStatus;
    }

    public JLabel getLabelRunning() {
        return labelRunning;
    }

    public JTextField getTextFieldRunning() {
        return textFieldRunning;
    }

    public JLabel getLabelStartDate() {
        return labelStartDate;
    }

    public JTextField getTextFieldStartDate() {
        return textFieldStartDate;
    }

    public JLabel getLabelEndDate() {
        return labelEndDate;
    }

    public JTextField getTextFieldEndDate() {
        return textFieldEndDate;
    }

    public JLabel getLabelExitCode() {
        return labelExitCode;
    }

    public JTextField getTextFieldExitCode() {
        return textFieldExitCode;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        labelJobName = new JLabel();
        textFieldJobName = new JTextField();
        labelRunning = new JLabel();
        textFieldRunning = new JTextField();
        labelStatus = new JLabel();
        textFieldStatus = new JTextField();
        labelId = new JLabel();
        textFieldId = new JTextField();
        labelJobId = new JLabel();
        textFieldJobId = new JTextField();
        labelStartDate = new JLabel();
        textFieldStartDate = new JTextField();
        labelEndDate = new JLabel();
        textFieldEndDate = new JTextField();
        labelExitCode = new JLabel();
        textFieldExitCode = new JTextField();

        //======== this ========
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setName("this");
        setLayout(new GridBagLayout());
        ((GridBagLayout) getLayout()).columnWidths = new int[]{0, 61, 0, 79, 75, 61, 0};
        ((GridBagLayout) getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0};
        ((GridBagLayout) getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0E-4};
        ((GridBagLayout) getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0E-4};

        //---- labelJobName ----
        labelJobName.setText("Job-Name:");
        labelJobName.setName("labelJobName");
        add(labelJobName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(4, 2, 2, 2), 0, 0));

        //---- textFieldJobName ----
        textFieldJobName.setEditable(false);
        textFieldJobName.setName("textFieldJobName");
        add(textFieldJobName, new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0));

        //---- labelRunning ----
        labelRunning.setText("Running:");
        labelRunning.setName("labelRunning");
        add(labelRunning, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(4, 2, 2, 2), 0, 0));

        //---- textFieldRunning ----
        textFieldRunning.setEditable(false);
        textFieldRunning.setName("textFieldRunning");
        add(textFieldRunning, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0));

        //---- labelStatus ----
        labelStatus.setText("Status");
        labelStatus.setName("labelStatus");
        add(labelStatus, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(4, 2, 2, 2), 0, 0));

        //---- textFieldStatus ----
        textFieldStatus.setEditable(false);
        textFieldStatus.setName("textFieldStatus");
        add(textFieldStatus, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0));

        //---- labelId ----
        labelId.setText("ID:");
        labelId.setName("labelId");
        add(labelId, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(4, 2, 2, 2), 0, 0));

        //---- textFieldId ----
        textFieldId.setEditable(false);
        textFieldId.setName("textFieldId");
        add(textFieldId, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0));

        //---- labelJobId ----
        labelJobId.setText("Job-ID:");
        labelJobId.setName("labelJobId");
        add(labelJobId, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(4, 2, 2, 2), 0, 0));

        //---- textFieldJobId ----
        textFieldJobId.setEditable(false);
        textFieldJobId.setName("textFieldJobId");
        add(textFieldJobId, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0));

        //---- labelStartDate ----
        labelStartDate.setText("StartDate:");
        labelStartDate.setName("labelStartDate");
        add(labelStartDate, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(4, 2, 2, 2), 0, 0));

        //---- textFieldStartDate ----
        textFieldStartDate.setEditable(false);
        textFieldStartDate.setName("textFieldStartDate");
        add(textFieldStartDate, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0));

        //---- labelEndDate ----
        labelEndDate.setText("EndDate:");
        labelEndDate.setName("labelEndDate");
        add(labelEndDate, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(4, 2, 2, 2), 0, 0));

        //---- textFieldEndDate ----
        textFieldEndDate.setEditable(false);
        textFieldEndDate.setName("textFieldEndDate");
        add(textFieldEndDate, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0));

        //---- labelExitCode ----
        labelExitCode.setText("ExitCode:");
        labelExitCode.setName("labelExitCode");
        add(labelExitCode, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(4, 2, 2, 2), 0, 0));

        //---- textFieldExitCode ----
        textFieldExitCode.setEditable(false);
        textFieldExitCode.setName("textFieldExitCode");
        add(textFieldExitCode, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel labelJobName;
    private JTextField textFieldJobName;
    private JLabel labelRunning;
    private JTextField textFieldRunning;
    private JLabel labelStatus;
    private JTextField textFieldStatus;
    private JLabel labelId;
    private JTextField textFieldId;
    private JLabel labelJobId;
    private JTextField textFieldJobId;
    private JLabel labelStartDate;
    private JTextField textFieldStartDate;
    private JLabel labelEndDate;
    private JTextField textFieldEndDate;
    private JLabel labelExitCode;
    private JTextField textFieldExitCode;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
