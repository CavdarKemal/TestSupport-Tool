/*
 * Created by JFormDesigner on Thu Aug 16 14:27:10 CEST 2018
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.jvm.design;

import de.creditreform.crefoteam.cte.testsupporttool.gui.base.view.TableWithButtonsView;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * @author Kemal Cavdar
 */
public class JvmInstallationsPanel extends JPanel {
    public JvmInstallationsPanel() {
        super();
        initComponents();
    }

    public JPanel getPanelNorth() {
        return panelNorth;
    }

    public JLabel getLabelInfo() {
        return labelInfo;
    }

    public TableWithButtonsView getPanelJvmInstallations() {
        return panelJvmInstallations;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panelNorth = new JPanel();
        labelInfo = new JLabel();
        panelJvmInstallations = new TableWithButtonsView();

        //======== this ========
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setName("this");
        setLayout(new BorderLayout());

        //======== panelNorth ========
        {
           panelNorth.setName("panelNorth");
           panelNorth.setLayout(new GridBagLayout());
           ((GridBagLayout)panelNorth.getLayout()).columnWidths = new int[] {70, 0, 0};
           ((GridBagLayout)panelNorth.getLayout()).rowHeights = new int[] {0, 0};
           ((GridBagLayout)panelNorth.getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0E-4};
           ((GridBagLayout)panelNorth.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

           //---- labelInfo ----
           labelInfo.setText("JVM -  Installationen");
           labelInfo.setName("labelInfo");
           panelNorth.add(labelInfo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(2, 2, 2, 7), 0, 0));
        }
        add(panelNorth, BorderLayout.NORTH);

        //---- panelJvmInstallations ----
        panelJvmInstallations.setName("panelJvmInstallations");
        add(panelJvmInstallations, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panelNorth;
    private JLabel labelInfo;
    private TableWithButtonsView panelJvmInstallations;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
