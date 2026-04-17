/*
 * Created by JFormDesigner on Thu Aug 16 15:07:49 CEST 2018
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.jvm.design;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.view.TableWithButtonsView;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * @author Kemal Cavdar
 */
public class JvmInstallationPanel extends JPanel {
    public JvmInstallationPanel() {
        super();
        initComponents();
    }

    public JPanel getPanelNorth() {
        return panelNorth;
    }

    public JLabel getLabelJvmName() {
        return labelJvmName;
    }

    public JTextField getTextFieldJvmName() {
        return textFieldJvmName;
    }

    public JLabel getLabelJvmUrl() {
        return labelJvmUrl;
    }

    public JTextField getTextFieldJvmURL() {
        return textFieldJvmURL;
    }

    public JLabel getLabelInfo() {
        return labelInfo;
    }

    public TableWithButtonsView getPanelJvmJobs() {
        return panelJvmJobs;
    }

    public JButton getButtonStartJobs() {
        return buttonStartJobs;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public JSplitPane getSplitPaneJobs() {
        return splitPaneJobs;
    }

    public JScrollPane getScrollPaneInfoArea() {
        return scrollPaneInfoArea;
    }

    public JTextArea getTextAreaInfo() {
        return textAreaInfo;
    }

    public JButton getButtonRefreshJvmJobs() {
        return buttonRefreshJvmJobs;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panelNorth = new JPanel();
        labelJvmName = new JLabel();
        textFieldJvmName = new JTextField();
        labelJvmUrl = new JLabel();
        textFieldJvmURL = new JTextField();
        labelInfo = new JLabel();
        buttonRefreshJvmJobs = new JButton();
        buttonStartJobs = new JButton();
        splitPaneJobs = new JSplitPane();
        panelJvmJobs = new TableWithButtonsView();
        scrollPaneInfoArea = new JScrollPane();
        textAreaInfo = new JTextArea();
        progressBar = new JProgressBar();

        //======== this ========
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setName("this");
        setLayout(new BorderLayout());

        //======== panelNorth ========
        {
           panelNorth.setName("panelNorth");
           panelNorth.setLayout(new GridBagLayout());
           ((GridBagLayout)panelNorth.getLayout()).columnWidths = new int[] {70, 78, 26, 0, 0, 0};
           ((GridBagLayout)panelNorth.getLayout()).rowHeights = new int[] {0, 0, 0};
           ((GridBagLayout)panelNorth.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0, 0.0, 1.0E-4};
           ((GridBagLayout)panelNorth.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

           //---- labelJvmName ----
           labelJvmName.setText("JVM-Name:");
           labelJvmName.setName("labelJvmName");
           panelNorth.add(labelJvmName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(2, 4, 7, 7), 0, 0));

           //---- textFieldJvmName ----
           textFieldJvmName.setEditable(false);
           textFieldJvmName.setName("textFieldJvmName");
           panelNorth.add(textFieldJvmName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(2, 2, 7, 7), 0, 0));

           //---- labelJvmUrl ----
           labelJvmUrl.setText("URL:");
           labelJvmUrl.setName("labelJvmUrl");
           panelNorth.add(labelJvmUrl, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(2, 2, 7, 7), 0, 0));

           //---- textFieldJvmURL ----
           textFieldJvmURL.setEditable(false);
           textFieldJvmURL.setName("textFieldJvmURL");
           panelNorth.add(textFieldJvmURL, new GridBagConstraints(3, 0, 2, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(2, 2, 7, 2), 0, 0));

           //---- labelInfo ----
           labelInfo.setText("Verf\u00fcgbare Jobs");
           labelInfo.setName("labelInfo");
           panelNorth.add(labelInfo, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(2, 4, 2, 7), 0, 0));

           //---- buttonRefreshJvmJobs ----
           buttonRefreshJvmJobs.setIcon(new ImageIcon(getClass().getResource("/icons/refresh.png")));
           buttonRefreshJvmJobs.setActionCommand("OK");
           buttonRefreshJvmJobs.setName("buttonRefreshJvmJobs");
           panelNorth.add(buttonRefreshJvmJobs, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(2, 2, 2, 7), 0, 0));

           //---- buttonStartJobs ----
           buttonStartJobs.setIcon(new ImageIcon(getClass().getResource("/icons/gear_run.png")));
           buttonStartJobs.setActionCommand("OK");
           buttonStartJobs.setText("Start selektierte Jobs...");
           buttonStartJobs.setName("buttonStartJobs");
           panelNorth.add(buttonStartJobs, new GridBagConstraints(3, 1, 2, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(2, 2, 2, 2), 0, 0));
        }
        add(panelNorth, BorderLayout.PAGE_START);

        //======== splitPaneJobs ========
        {
           splitPaneJobs.setOrientation(JSplitPane.VERTICAL_SPLIT);
           splitPaneJobs.setDividerLocation(180);
           splitPaneJobs.setName("splitPaneJobs");

           //---- panelJvmJobs ----
           panelJvmJobs.setName("panelJvmJobs");
           splitPaneJobs.setTopComponent(panelJvmJobs);

           //======== scrollPaneInfoArea ========
           {
              scrollPaneInfoArea.setName("scrollPaneInfoArea");

              //---- textAreaInfo ----
              textAreaInfo.setEditable(false);
              textAreaInfo.setName("textAreaInfo");
              scrollPaneInfoArea.setViewportView(textAreaInfo);
           }
           splitPaneJobs.setBottomComponent(scrollPaneInfoArea);
        }
        add(splitPaneJobs, BorderLayout.CENTER);

        //---- progressBar ----
        progressBar.setName("progressBar");
        add(progressBar, BorderLayout.PAGE_END);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panelNorth;
    private JLabel labelJvmName;
    private JTextField textFieldJvmName;
    private JLabel labelJvmUrl;
    private JTextField textFieldJvmURL;
    private JLabel labelInfo;
    private JButton buttonRefreshJvmJobs;
    private JButton buttonStartJobs;
    private JSplitPane splitPaneJobs;
    private TableWithButtonsView panelJvmJobs;
    private JScrollPane scrollPaneInfoArea;
    private JTextArea textAreaInfo;
    private JProgressBar progressBar;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
