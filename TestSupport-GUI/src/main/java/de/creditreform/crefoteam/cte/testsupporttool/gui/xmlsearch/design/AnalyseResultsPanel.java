/*
 * Created by JFormDesigner on Wed Jan 31 13:57:39 CET 2024
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * @author CavdarK
 */
public class AnalyseResultsPanel extends JPanel {
    public AnalyseResultsPanel() {
        initComponents();
    }

    public JScrollPane getScrollPaneAnalyseResults() {
        return scrollPaneAnalyseResults;
    }

    public JTable getTableAnalyseResults() {
        return tableAnalyseResults;
    }

    public JPanel getPanelControls() {
        return panelControls;
    }

    public JLabel getLabelSearchName() {
        return labelSearchName;
    }

    public JLabel getLabelAnalyse() {
        return labelAnalyse;
    }

    public JComboBox getComboBoxAnalyse() {
        return comboBoxAnalyse;
    }

    public JComboBox getComboBoxSearchName() {
        return comboBoxSearchName;
    }

    public JButton getButtonStartAnalyse() {
        return buttonStartAnalyse;
    }

    public JLabel getLabelAnalyseResults() {
        return labelAnalyseResults;
    }

    public JPanel getPaneAnalyse() {
        return paneAnalyse;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
      paneAnalyse = new JPanel();
      panelControls = new JPanel();
      labelAnalyse = new JLabel();
      comboBoxAnalyse = new JComboBox();
      labelSearchName = new JLabel();
      comboBoxSearchName = new JComboBox();
      buttonStartAnalyse = new JButton();
      labelAnalyseResults = new JLabel();
      scrollPaneAnalyseResults = new JScrollPane();
      tableAnalyseResults = new JTable();

      //======== this ========
      setName("this");
      setLayout(new BorderLayout());

      //======== paneAnalyse ========
      {
         paneAnalyse.setName("paneAnalyse");
         paneAnalyse.setLayout(new GridBagLayout());
         ((GridBagLayout)paneAnalyse.getLayout()).columnWidths = new int[] {0, 0};
         ((GridBagLayout)paneAnalyse.getLayout()).rowHeights = new int[] {0, 0};
         ((GridBagLayout)paneAnalyse.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
         ((GridBagLayout)paneAnalyse.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

         //======== panelControls ========
         {
            panelControls.setName("panelControls");
            panelControls.setLayout(new GridBagLayout());
            ((GridBagLayout)panelControls.getLayout()).columnWidths = new int[] {0, 0, 0};
            ((GridBagLayout)panelControls.getLayout()).rowHeights = new int[] {0, 30, 20, 0, 0, 0};
            ((GridBagLayout)panelControls.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
            ((GridBagLayout)panelControls.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0, 1.0E-4};

            //---- labelAnalyse ----
            labelAnalyse.setText("Analyse f\u00fcr");
            labelAnalyse.setName("labelAnalyse");
            panelControls.add(labelAnalyse, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
               GridBagConstraints.CENTER, GridBagConstraints.BOTH,
               new Insets(2, 2, 7, 7), 0, 0));

            //---- comboBoxAnalyse ----
            comboBoxAnalyse.setName("comboBoxAnalyse");
            panelControls.add(comboBoxAnalyse, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
               GridBagConstraints.CENTER, GridBagConstraints.BOTH,
               new Insets(2, 2, 7, 2), 0, 0));

            //---- labelSearchName ----
            labelSearchName.setText("Suchname:");
            labelSearchName.setName("labelSearchName");
            panelControls.add(labelSearchName, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
               GridBagConstraints.CENTER, GridBagConstraints.BOTH,
               new Insets(2, 2, 7, 7), 0, 0));

            //---- comboBoxSearchName ----
            comboBoxSearchName.setName("comboBoxSearchName");
            panelControls.add(comboBoxSearchName, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
               GridBagConstraints.CENTER, GridBagConstraints.BOTH,
               new Insets(2, 2, 7, 2), 0, 0));

            //---- buttonStartAnalyse ----
            buttonStartAnalyse.setText("Analyse starten");
            buttonStartAnalyse.setIcon(new ImageIcon(getClass().getResource("/icons/gear_run.png")));
            buttonStartAnalyse.setName("buttonStartAnalyse");
            panelControls.add(buttonStartAnalyse, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
               GridBagConstraints.CENTER, GridBagConstraints.BOTH,
               new Insets(2, 2, 7, 2), 0, 0));

            //---- labelAnalyseResults ----
            labelAnalyseResults.setText("Analyse Ergebnisse");
            labelAnalyseResults.setName("labelAnalyseResults");
            panelControls.add(labelAnalyseResults, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
               GridBagConstraints.CENTER, GridBagConstraints.BOTH,
               new Insets(2, 2, 7, 2), 0, 0));

            //======== scrollPaneAnalyseResults ========
            {
               scrollPaneAnalyseResults.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
               scrollPaneAnalyseResults.setName("scrollPaneAnalyseResults");

               //---- tableAnalyseResults ----
               tableAnalyseResults.setPreferredScrollableViewportSize(new Dimension(450, 180));
               tableAnalyseResults.setAutoCreateRowSorter(true);
               tableAnalyseResults.setBorder(new EtchedBorder());
               tableAnalyseResults.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
               tableAnalyseResults.setName("tableAnalyseResults");
               scrollPaneAnalyseResults.setViewportView(tableAnalyseResults);
            }
            panelControls.add(scrollPaneAnalyseResults, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0,
               GridBagConstraints.CENTER, GridBagConstraints.BOTH,
               new Insets(2, 2, 2, 2), 0, 0));
         }
         paneAnalyse.add(panelControls, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
      }
      add(paneAnalyse, BorderLayout.CENTER);
      // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
   private JPanel paneAnalyse;
   private JPanel panelControls;
   private JLabel labelAnalyse;
   private JComboBox comboBoxAnalyse;
   private JLabel labelSearchName;
   private JComboBox comboBoxSearchName;
   private JButton buttonStartAnalyse;
   private JLabel labelAnalyseResults;
   private JScrollPane scrollPaneAnalyseResults;
   private JTable tableAnalyseResults;
   // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
