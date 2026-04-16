/*
 * Created by JFormDesigner on Wed Jan 22 14:09:35 CET 2025
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.design;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

/**
 * @author CavdarK
 */
public class TestResultDefaultPanel extends JPanel {
    public TestResultDefaultPanel() {
        initComponents();
    }

    public JScrollPane getScrollPaneSrcFile() {
        return scrollPaneSrcFile;
    }

    public JTextArea getTextAreaFileSrc() {
        return textAreaFileSrc;
    }

    public JPanel getPanelControls() {
       return panelControls;
    }

    public JLabel getLabelSearch() {
       return labelSearch;
    }

    public JTextField getTextFieldSearch() {
       return textFieldSearch;
    }

    public JButton getButtonNextMath() {
       return buttonNextMath;
    }

    public JButton getButtonPrevMatch() {
       return buttonPrevMatch;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        scrollPaneSrcFile = new JScrollPane();
        textAreaFileSrc = new JTextArea();
        panelControls = new JPanel();
        labelSearch = new JLabel();
        textFieldSearch = new JTextField();
        buttonNextMath = new JButton();
        buttonPrevMatch = new JButton();

        //======== this ========
        setLayout(new BorderLayout());

        //======== scrollPaneSrcFile ========
        {

           //---- textAreaFileSrc ----
           textAreaFileSrc.setEditable(false);
           textAreaFileSrc.setFont(new Font("Verdana", Font.PLAIN, 12));
           scrollPaneSrcFile.setViewportView(textAreaFileSrc);
        }
        add(scrollPaneSrcFile, BorderLayout.CENTER);

        //======== panelControls ========
        {
           panelControls.setBorder(new BevelBorder(BevelBorder.LOWERED));
           panelControls.setLayout(new GridBagLayout());
           ((GridBagLayout)panelControls.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
           ((GridBagLayout)panelControls.getLayout()).rowHeights = new int[] {0, 0, 0};
           ((GridBagLayout)panelControls.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 0.0, 1.0E-4};
           ((GridBagLayout)panelControls.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

           //---- labelSearch ----
           labelSearch.setText("Suche:");
           panelControls.add(labelSearch, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(2, 2, 7, 7), 0, 0));
           panelControls.add(textFieldSearch, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(2, 2, 7, 7), 0, 0));

           //---- buttonNextMath ----
           buttonNextMath.setIcon(new ImageIcon(getClass().getResource("/icons/resultset_next.png")));
           panelControls.add(buttonNextMath, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(2, 1, 7, 6), 0, 0));

           //---- buttonPrevMatch ----
           buttonPrevMatch.setIcon(new ImageIcon(getClass().getResource("/icons/resultset_previous.png")));
           panelControls.add(buttonPrevMatch, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(2, 1, 7, 1), 0, 0));
        }
        add(panelControls, BorderLayout.NORTH);
      // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JScrollPane scrollPaneSrcFile;
    private JTextArea textAreaFileSrc;
    private JPanel panelControls;
    private JLabel labelSearch;
    private JTextField textFieldSearch;
    private JButton buttonNextMath;
    private JButton buttonPrevMatch;
   // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
