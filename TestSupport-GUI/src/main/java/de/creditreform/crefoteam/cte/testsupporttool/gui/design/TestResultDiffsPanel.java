/*
 * Created by JFormDesigner on Wed Jan 22 12:58:22 CET 2025
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.design;

import javax.swing.*;
import java.awt.*;

/**
 * @author CavdarK
 */
public class TestResultDiffsPanel extends JPanel {
    public TestResultDiffsPanel() {
        initComponents();
    }

    public JSplitPane getSplitPanelDifferences() {
        return splitPanelDifferences;
    }

    public JPanel getPanelScrDifFile() {
        return panelScrDifFile;
    }

    public JLabel getLabelScfFilePath() {
        return labelScfFilePath;
    }

    public JScrollPane getScrollPaneSrcFile() {
        return scrollPaneSrcFile;
    }

    public JEditorPane getTextAreaFileSrc() {
        return textAreaFileSrc;
    }

    public JPanel getPanelDstFile() {
        return panelDstFile;
    }

    public JLabel getLabelDstFilePath() {
        return labelDstFilePath;
    }

    public JScrollPane getScrollPaneDstFile() {
        return scrollPaneDstFile;
    }

    public JEditorPane getTextAreaFileDst() {
        return textAreaFileDst;
    }

    public JLabel getLabelDiffFilePath() {
        return labelDiffFilePath;
    }

    public JPanel getPanelControls() {
        return panelControls;
    }

    public JTextField getTextFieldDiffFilePath() {
        return textFieldDiffFilePath;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panelControls = new JPanel();
        labelDiffFilePath = new JLabel();
        textFieldDiffFilePath = new JTextField();
        splitPanelDifferences = new JSplitPane();
        panelScrDifFile = new JPanel();
        labelScfFilePath = new JLabel();
        scrollPaneSrcFile = new JScrollPane();
        textAreaFileSrc = new JEditorPane();
        panelDstFile = new JPanel();
        labelDstFilePath = new JLabel();
        scrollPaneDstFile = new JScrollPane();
        textAreaFileDst = new JEditorPane();

        //======== this ========
        setLayout(new BorderLayout());

        //======== panelControls ========
        {
           panelControls.setLayout(new GridBagLayout());
           ((GridBagLayout)panelControls.getLayout()).columnWidths = new int[] {0, 199, 32, 0, 0};
           ((GridBagLayout)panelControls.getLayout()).rowHeights = new int[] {0, 0};
           ((GridBagLayout)panelControls.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 0.0, 1.0E-4};
           ((GridBagLayout)panelControls.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

           //---- labelDiffFilePath ----
           labelDiffFilePath.setText("Diff-Datei: ");
           panelControls.add(labelDiffFilePath, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(2, 2, 2, 7), 0, 0));

           //---- textFieldDiffFilePath ----
           textFieldDiffFilePath.setEditable(false);
           panelControls.add(textFieldDiffFilePath, new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(2, 2, 2, 2), 0, 0));
        }
        add(panelControls, BorderLayout.NORTH);

        //======== splitPanelDifferences ========
        {
           splitPanelDifferences.setDividerLocation(190);

           //======== panelScrDifFile ========
           {
              panelScrDifFile.setLayout(new GridBagLayout());
              ((GridBagLayout)panelScrDifFile.getLayout()).columnWidths = new int[] {0, 0};
              ((GridBagLayout)panelScrDifFile.getLayout()).rowHeights = new int[] {0, 0, 0};
              ((GridBagLayout)panelScrDifFile.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
              ((GridBagLayout)panelScrDifFile.getLayout()).rowWeights = new double[] {0.0, 1.0, 1.0E-4};

              //---- labelScfFilePath ----
              labelScfFilePath.setText("text");
              panelScrDifFile.add(labelScfFilePath, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                 new Insets(0, 0, 5, 0), 0, 0));

              //======== scrollPaneSrcFile ========
              {

                 //---- textAreaFileSrc ----
                 textAreaFileSrc.setEditable(false);
                 textAreaFileSrc.setFont(new Font("Verdana", Font.PLAIN, 12));
                 scrollPaneSrcFile.setViewportView(textAreaFileSrc);
              }
              panelScrDifFile.add(scrollPaneSrcFile, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                 GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                 new Insets(0, 0, 0, 0), 0, 0));
           }
           splitPanelDifferences.setLeftComponent(panelScrDifFile);

           //======== panelDstFile ========
           {
              panelDstFile.setLayout(new GridBagLayout());
              ((GridBagLayout)panelDstFile.getLayout()).columnWidths = new int[] {0, 0};
              ((GridBagLayout)panelDstFile.getLayout()).rowHeights = new int[] {0, 0, 0};
              ((GridBagLayout)panelDstFile.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
              ((GridBagLayout)panelDstFile.getLayout()).rowWeights = new double[] {0.0, 1.0, 1.0E-4};

              //---- labelDstFilePath ----
              labelDstFilePath.setText("text");
              panelDstFile.add(labelDstFilePath, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                 new Insets(0, 0, 5, 0), 0, 0));

              //======== scrollPaneDstFile ========
              {

                 //---- textAreaFileDst ----
                 textAreaFileDst.setEditable(false);
                 textAreaFileDst.setFont(new Font("Verdana", Font.PLAIN, 12));
                 scrollPaneDstFile.setViewportView(textAreaFileDst);
              }
              panelDstFile.add(scrollPaneDstFile, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                 GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                 new Insets(0, 0, 0, 0), 0, 0));
           }
           splitPanelDifferences.setRightComponent(panelDstFile);
        }
        add(splitPanelDifferences, BorderLayout.CENTER);
      // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel panelControls;
    private JLabel labelDiffFilePath;
    private JTextField textFieldDiffFilePath;
    private JSplitPane splitPanelDifferences;
    private JPanel panelScrDifFile;
    private JLabel labelScfFilePath;
    private JScrollPane scrollPaneSrcFile;
    private JEditorPane textAreaFileSrc;
    private JPanel panelDstFile;
    private JLabel labelDstFilePath;
    private JScrollPane scrollPaneDstFile;
    private JEditorPane textAreaFileDst;
   // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
