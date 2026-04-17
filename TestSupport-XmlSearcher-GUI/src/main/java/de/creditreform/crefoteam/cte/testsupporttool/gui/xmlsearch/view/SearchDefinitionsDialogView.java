package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design.SearchDefinitionsDialogPanel;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten.SEARCH_RESULT_TYPE;
import de.creditreform.crefoteam.cte.tesun.util.TestSupportClientKonstanten.XML_STREAM_PROCESSOR;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.SearchSpecification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SearchDefinitionsDialogView extends SearchDefinitionsDialogPanel {
    private boolean isCancelled = false;
    private SearchSpecification searchDefinition;

    public SearchDefinitionsDialogView(Frame owner, String strTitle) {
        super(owner);
        setTitle(strTitle);
        setModal(true);
        //setSize( 800, 270 );
        initControls();
        initListeners();
    }

    private void initListeners() {
        getComboBoxSearchType().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        getButtonSourcePath().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                doSelectSourceDir();
            }
        });
        getButtonOK().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doClose();
            }
        });
        getButtonCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                isCancelled = true;
                doClose();
            }
        });
    }

    private void initControls() {
    }

    private void doClose() {
        this.dispose();
        this.setVisible(false);
    }

    protected void doSelectSourceDir() {
        String defDirName = getTextFieldSourcePath().getText();
        if (defDirName.isEmpty()) {
            defDirName = System.getProperty("user.dir");
        }
        String choosenFileName = GUIStaticUtils.chooseDirectory(this, defDirName, "Quelle wählen");
        if (GUIStaticUtils.isEmpty(choosenFileName)) {
            return;
        }
        getTextFieldSourcePath().setText(choosenFileName);
    }

    private DefaultComboBoxModel buildXmlProcessorsComboModel() {
        DefaultComboBoxModel xmlProcessorsModel = new DefaultComboBoxModel();
        xmlProcessorsModel.addElement(XML_STREAM_PROCESSOR.LINEAR);
        xmlProcessorsModel.addElement(XML_STREAM_PROCESSOR.RECURSIVE);
        return xmlProcessorsModel;
    }

    private DefaultComboBoxModel buildResultTypesComboModel() {
        DefaultComboBoxModel resultTypesModel = new DefaultComboBoxModel();
        resultTypesModel.addElement(SEARCH_RESULT_TYPE.CREFOS_COUNT);
        resultTypesModel.addElement(SEARCH_RESULT_TYPE.CREFOS_LIST);
        resultTypesModel.addElement(SEARCH_RESULT_TYPE.CREFOS_XML);
        return resultTypesModel;
    }

    public void setModel(SearchSpecification searchDefinition) {
        this.searchDefinition = searchDefinition;
        // "#", "Name", "Ident", "Typ", "Invert", "Quellverzeichnis"
        getTextFieldName().setText(searchDefinition.getName());
        getTextFieldIdentifier().setText(searchDefinition.getCrefoNrTagName());

        DefaultComboBoxModel xmlProcessorsModel = buildXmlProcessorsComboModel();
        getComboBoxXmlProcessor().setModel(xmlProcessorsModel);
        getComboBoxXmlProcessor().setSelectedItem(searchDefinition.getUsedXmlStreamProcessor());

        DefaultComboBoxModel resultTypesModel = buildResultTypesComboModel();
        getComboBoxSearchType().setModel(resultTypesModel);
        getComboBoxSearchType().setSelectedItem(searchDefinition.getSearchResultsType());

        getRadioButtonLogicOr().setSelected(searchDefinition.isLogicalConnectionOr());
        getRadioButtonLogicAnd().setSelected(searchDefinition.isLogicalConnectionAnd());
        getCheckBoxInvert().setSelected(searchDefinition.isInvertedResults());
        File sourceFile = searchDefinition.getSourceFile();
        if (sourceFile == null) {
            sourceFile = new File(System.getProperty("user.dir"));
        }
        getTextFieldSourcePath().setText(sourceFile.getAbsolutePath());
    }

    public SearchSpecification getSearchDefinition() {
        if (isCancelled) {
            return null;
        }
        // "#", "Name", "Ident", "Typ", "Invert", "Quellverzeichnis"
        searchDefinition.setActivated(true);
        searchDefinition.setName(getTextFieldName().getText());
        searchDefinition.setCrefoNrTagName(getTextFieldIdentifier().getText());
        XML_STREAM_PROCESSOR xmlStreamProcessor = (XML_STREAM_PROCESSOR) getComboBoxXmlProcessor().getSelectedItem();
        searchDefinition.setUsedXmlStreamProcessor(xmlStreamProcessor);
        SEARCH_RESULT_TYPE searchResultType = (SEARCH_RESULT_TYPE) getComboBoxSearchType().getSelectedItem();
        searchDefinition.setSearchResultsType(searchResultType);
        searchDefinition.setInvertedResults(getCheckBoxInvert().isSelected());
        searchDefinition.setLogicalConnection(getRadioButtonLogicOr().isSelected() ?
                TestSupportClientKonstanten.LOGICAL_CONNECTION.LOGIC_OR : TestSupportClientKonstanten.LOGICAL_CONNECTION.LOGIC_AND);
        searchDefinition.setSourceFile(new File(getTextFieldSourcePath().getText()));
        return searchDefinition;
    }

}
