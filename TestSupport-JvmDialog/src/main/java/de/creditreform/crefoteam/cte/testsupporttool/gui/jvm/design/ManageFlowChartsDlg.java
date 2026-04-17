/*
 * Created by JFormDesigner on Fri Apr 17 20:15:27 CEST 2026
 */

package de.creditreform.crefoteam.cte.testsupporttool.gui.jvm.design;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import de.creditreform.crefoteam.cte.testsupporttool.gui.base.view.*;

/**
 * @author CavdarK
 */
public class ManageFlowChartsDlg extends JDialog {
   public ManageFlowChartsDlg(Window owner) {
      super(owner);
      initComponents();
   }

   public JPanel getPanelSouth() {
      return panelSouth;
   }

   public JRadioButton getRadioButtonUseFlux() {
      return radioButtonUseFlux;
   }

   public JRadioButton getRadioButtonUseJVM() {
      return radioButtonUseJVM;
   }

   public JTextField getTextFieldFluxURL() {
      return textFieldFluxURL;
   }

   public JCheckBox getCheckAutoBoxRefresh() {
      return checkAutoBoxRefresh;
   }

   public JSpinner getSpinnerAutoRefreshRate() {
      return spinnerAutoRefreshRate;
   }

   public JButton getButtonRefreshFlowcharts() {
      return buttonRefreshFlowcharts;
   }

   public JSplitPane getSplitPaneTreeView() {
      return splitPaneTreeView;
   }

   public JPanel getPanelTree() {
      return panelTree;
   }

   public JScrollPane getScrollPaneTree() {
      return scrollPaneTree;
   }

   public JTree getTreeFlows() {
      return treeFlows;
   }

   public TableWithButtonsView getPanelView() {
      return panelView;
   }

   public JPanel getPanelControls() {
      return panelControls;
   }

   public JPanel getPanelRepository() {
      return panelRepository;
   }

   public JCheckBox getCheckBoxOverrideInRepop() {
      return checkBoxOverrideInRepop;
   }

   public JButton getButtonUploadToRepo() {
      return buttonUploadToRepo;
   }

   public JButton getButtonDeleteFromRepo() {
      return buttonDeleteFromRepo;
   }

   public JPanel getPanelEngine() {
      return panelEngine;
   }

   public JButton getButtonExportToEngine() {
      return buttonExportToEngine;
   }

   public JButton getButtonDeleteFromEngine() {
      return buttonDeleteFromEngine;
   }

   public JButton getButtonStartJob() {
      return buttonStartJob;
   }

   public JButton getButtonClose() {
      return buttonClose;
   }

   private void initComponents() {
      // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
      panelSouth = new JPanel();
      radioButtonUseFlux = new JRadioButton();
      radioButtonUseJVM = new JRadioButton();
      textFieldFluxURL = new JTextField();
      checkAutoBoxRefresh = new JCheckBox();
      spinnerAutoRefreshRate = new JSpinner();
      buttonRefreshFlowcharts = new JButton();
      splitPaneTreeView = new JSplitPane();
      panelTree = new JPanel();
      scrollPaneTree = new JScrollPane();
      treeFlows = new JTree();
      panelView = new TableWithButtonsView();
      panelControls = new JPanel();
      panelRepository = new JPanel();
      checkBoxOverrideInRepop = new JCheckBox();
      buttonUploadToRepo = new JButton();
      buttonDeleteFromRepo = new JButton();
      panelEngine = new JPanel();
      buttonExportToEngine = new JButton();
      buttonDeleteFromEngine = new JButton();
      buttonStartJob = new JButton();
      buttonClose = new JButton();

      //======== this ========
      setName("this");
      var contentPane = getContentPane();
      contentPane.setLayout(new BorderLayout());

      //======== panelSouth ========
      {
         panelSouth.setName("panelSouth");
         panelSouth.setLayout(new GridBagLayout());
         ((GridBagLayout)panelSouth.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 90, 51, 0, 0};
         ((GridBagLayout)panelSouth.getLayout()).rowHeights = new int[] {0, 0};
         ((GridBagLayout)panelSouth.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0E-4};
         ((GridBagLayout)panelSouth.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

         //---- radioButtonUseFlux ----
         radioButtonUseFlux.setText("Flux");
         radioButtonUseFlux.setName("radioButtonUseFlux");
         panelSouth.add(radioButtonUseFlux, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(2, 2, 2, 7), 0, 0));

         //---- radioButtonUseJVM ----
         radioButtonUseJVM.setText("JVM");
         radioButtonUseJVM.setName("radioButtonUseJVM");
         panelSouth.add(radioButtonUseJVM, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(2, 2, 2, 7), 0, 0));

         //---- textFieldFluxURL ----
         textFieldFluxURL.setEditable(false);
         textFieldFluxURL.setName("textFieldFluxURL");
         panelSouth.add(textFieldFluxURL, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(2, 2, 2, 7), 0, 0));

         //---- checkAutoBoxRefresh ----
         checkAutoBoxRefresh.setText("Auto Refresh [m]");
         checkAutoBoxRefresh.setName("checkAutoBoxRefresh");
         panelSouth.add(checkAutoBoxRefresh, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(2, 2, 2, 7), 0, 0));

         //---- spinnerAutoRefreshRate ----
         spinnerAutoRefreshRate.setName("spinnerAutoRefreshRate");
         panelSouth.add(spinnerAutoRefreshRate, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(2, 2, 2, 7), 0, 0));

         //---- buttonRefreshFlowcharts ----
         buttonRefreshFlowcharts.setIcon(new ImageIcon(getClass().getResource("/icons/refresh.png")));
         buttonRefreshFlowcharts.setActionCommand("OK");
         buttonRefreshFlowcharts.setName("buttonRefreshFlowcharts");
         panelSouth.add(buttonRefreshFlowcharts, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(2, 2, 2, 2), 0, 0));
      }
      contentPane.add(panelSouth, BorderLayout.PAGE_START);

      //======== splitPaneTreeView ========
      {
         splitPaneTreeView.setName("splitPaneTreeView");

         //======== panelTree ========
         {
            panelTree.setName("panelTree");
            panelTree.setLayout(new BorderLayout());

            //======== scrollPaneTree ========
            {
               scrollPaneTree.setName("scrollPaneTree");

               //---- treeFlows ----
               treeFlows.setName("treeFlows");
               scrollPaneTree.setViewportView(treeFlows);
            }
            panelTree.add(scrollPaneTree, BorderLayout.CENTER);
         }
         splitPaneTreeView.setLeftComponent(panelTree);

         //---- panelView ----
         panelView.setBorder(new BevelBorder(BevelBorder.LOWERED));
         panelView.setName("panelView");
         splitPaneTreeView.setRightComponent(panelView);
      }
      contentPane.add(splitPaneTreeView, BorderLayout.CENTER);

      //======== panelControls ========
      {
         panelControls.setName("panelControls");
         panelControls.setLayout(new GridBagLayout());
         ((GridBagLayout)panelControls.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
         ((GridBagLayout)panelControls.getLayout()).rowHeights = new int[] {0, 0, 0};
         ((GridBagLayout)panelControls.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0, 1.0E-4};
         ((GridBagLayout)panelControls.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

         //======== panelRepository ========
         {
            panelRepository.setBorder(new TitledBorder("Repository"));
            panelRepository.setName("panelRepository");
            panelRepository.setLayout(new GridBagLayout());
            ((GridBagLayout)panelRepository.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
            ((GridBagLayout)panelRepository.getLayout()).rowHeights = new int[] {0, 0};
            ((GridBagLayout)panelRepository.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
            ((GridBagLayout)panelRepository.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

            //---- checkBoxOverrideInRepop ----
            checkBoxOverrideInRepop.setText("\u00dcberschreiben");
            checkBoxOverrideInRepop.setName("checkBoxOverrideInRepop");
            panelRepository.add(checkBoxOverrideInRepop, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
               GridBagConstraints.CENTER, GridBagConstraints.BOTH,
               new Insets(2, 2, 2, 7), 0, 0));

            //---- buttonUploadToRepo ----
            buttonUploadToRepo.setText("Hochladen");
            buttonUploadToRepo.setIcon(new ImageIcon(getClass().getResource("/icons/folder_out.png")));
            buttonUploadToRepo.setActionCommand("OK");
            buttonUploadToRepo.setName("buttonUploadToRepo");
            panelRepository.add(buttonUploadToRepo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
               GridBagConstraints.CENTER, GridBagConstraints.BOTH,
               new Insets(2, 2, 2, 7), 0, 0));

            //---- buttonDeleteFromRepo ----
            buttonDeleteFromRepo.setText("L\u00f6schen");
            buttonDeleteFromRepo.setIcon(new ImageIcon(getClass().getResource("/icons/folder_delete.png")));
            buttonDeleteFromRepo.setActionCommand("OK");
            buttonDeleteFromRepo.setName("buttonDeleteFromRepo");
            panelRepository.add(buttonDeleteFromRepo, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
               GridBagConstraints.CENTER, GridBagConstraints.BOTH,
               new Insets(2, 2, 2, 2), 0, 0));
         }
         panelControls.add(panelRepository, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 5), 0, 0));

         //======== panelEngine ========
         {
            panelEngine.setBorder(new TitledBorder("Engine"));
            panelEngine.setName("panelEngine");
            panelEngine.setLayout(new GridBagLayout());
            ((GridBagLayout)panelEngine.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
            ((GridBagLayout)panelEngine.getLayout()).rowHeights = new int[] {0, 0};
            ((GridBagLayout)panelEngine.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
            ((GridBagLayout)panelEngine.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

            //---- buttonExportToEngine ----
            buttonExportToEngine.setText("Export");
            buttonExportToEngine.setIcon(new ImageIcon(getClass().getResource("/icons/folder_gear.png")));
            buttonExportToEngine.setActionCommand("OK");
            buttonExportToEngine.setName("buttonExportToEngine");
            panelEngine.add(buttonExportToEngine, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
               GridBagConstraints.CENTER, GridBagConstraints.BOTH,
               new Insets(2, 2, 2, 7), 0, 0));

            //---- buttonDeleteFromEngine ----
            buttonDeleteFromEngine.setText("L\u00f6schen");
            buttonDeleteFromEngine.setIcon(new ImageIcon(getClass().getResource("/icons/folder_delete.png")));
            buttonDeleteFromEngine.setActionCommand("OK");
            buttonDeleteFromEngine.setName("buttonDeleteFromEngine");
            panelEngine.add(buttonDeleteFromEngine, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
               GridBagConstraints.CENTER, GridBagConstraints.BOTH,
               new Insets(2, 2, 2, 7), 0, 0));

            //---- buttonStartJob ----
            buttonStartJob.setText("Start");
            buttonStartJob.setIcon(new ImageIcon(getClass().getResource("/icons/gear_run.png")));
            buttonStartJob.setActionCommand("OK");
            buttonStartJob.setName("buttonStartJob");
            panelEngine.add(buttonStartJob, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
               GridBagConstraints.CENTER, GridBagConstraints.BOTH,
               new Insets(2, 2, 2, 2), 0, 0));
         }
         panelControls.add(panelEngine, new GridBagConstraints(1, 0, 1, 2, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 5), 0, 0));

         //---- buttonClose ----
         buttonClose.setText("Schliessen");
         buttonClose.setIcon(new ImageIcon(getClass().getResource("/icons/exit.png")));
         buttonClose.setActionCommand("OK");
         buttonClose.setName("buttonClose");
         panelControls.add(buttonClose, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
            new Insets(2, 2, 6, 2), 0, 0));
      }
      contentPane.add(panelControls, BorderLayout.PAGE_END);
      pack();
      setLocationRelativeTo(getOwner());

      //---- buttonGroupCallMethod ----
      var buttonGroupCallMethod = new ButtonGroup();
      buttonGroupCallMethod.add(radioButtonUseFlux);
      buttonGroupCallMethod.add(radioButtonUseJVM);
      // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
   }

   // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
   private JPanel panelSouth;
   private JRadioButton radioButtonUseFlux;
   private JRadioButton radioButtonUseJVM;
   private JTextField textFieldFluxURL;
   private JCheckBox checkAutoBoxRefresh;
   private JSpinner spinnerAutoRefreshRate;
   private JButton buttonRefreshFlowcharts;
   private JSplitPane splitPaneTreeView;
   private JPanel panelTree;
   private JScrollPane scrollPaneTree;
   private JTree treeFlows;
   private TableWithButtonsView panelView;
   private JPanel panelControls;
   private JPanel panelRepository;
   private JCheckBox checkBoxOverrideInRepop;
   private JButton buttonUploadToRepo;
   private JButton buttonDeleteFromRepo;
   private JPanel panelEngine;
   private JButton buttonExportToEngine;
   private JButton buttonDeleteFromEngine;
   private JButton buttonStartJob;
   private JButton buttonClose;
   // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
