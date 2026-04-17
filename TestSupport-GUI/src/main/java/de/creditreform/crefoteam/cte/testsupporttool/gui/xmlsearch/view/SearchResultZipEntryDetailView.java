package de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.WordSearcher;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.XmlEditorKit;
import de.creditreform.crefoteam.cte.testsupporttool.gui.xmlsearch.design.SearchResultZipEntryDetailPanel;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ZipSearcResult.ZipEntryInfo;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchResultZipEntryDetailView extends SearchResultZipEntryDetailPanel {
    private final ZipEntryInfo zipEntryInfo;
    private static final List<String> searchList = new ArrayList<>();

    public SearchResultZipEntryDetailView(ZipEntryInfo zipEntryInfo) {
        super();
        this.zipEntryInfo = zipEntryInfo;
        initControls();
        initListeners();
    }

    private void initListeners() {
        getButtonSearchFor().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doSearchfor();
            }
        });
        getComboBoxSearchFor().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Object item = getComboBoxSearchFor().getEditor().getItem();
                if (item != null) {
                    DefaultComboBoxModel theModel = (DefaultComboBoxModel) getComboBoxSearchFor().getModel();
                    int indexOf = theModel.getIndexOf(item);
                    if (indexOf < 0) {
                        theModel.addElement(item);
                        searchList.add((String) item);
                    }
                }
                doSearchfor();
            }
        });
    }

    private void initControls() {
        String resultFileName = zipEntryInfo.getResultFileName();
        if (resultFileName != null) {
            boolean isTxt = resultFileName.endsWith(".txt");
            getLabelEntyName().setText(isTxt ? "Results-File:" : "Entry-Name:");
            getTextFieldEntryName().setText(isTxt ? resultFileName : zipEntryInfo.getZipEntryName());
            getTextFieldEntryName().setEditable(false);
        }
        getTextPaneEntryContent().setEditorKitForContentType("text/xml", new XmlEditorKit());
        getTextPaneEntryContent().setContentType("text/xml");
        getTextPaneEntryContent().setText(readZipEntry());
        getTextPaneEntryContent().setEditable(false);

        DefaultComboBoxModel aModel = new DefaultComboBoxModel();
        if (!searchList.isEmpty()) {
            aModel = new DefaultComboBoxModel(searchList.toArray(new String[]{}));
        }
        getComboBoxSearchFor().setModel(aModel);
    }

    protected void doSearchfor() {
        String searchFor = "";
        int selectedIndex = getComboBoxSearchFor().getSelectedIndex();
        if (selectedIndex > -1) {
            searchFor = (String) getComboBoxSearchFor().getSelectedItem();
        }
        if (!searchFor.isEmpty()) {
            final WordSearcher searcher = new WordSearcher(getTextPaneEntryContent());
            int offset = searcher.search(searchFor);
            if (offset != -1) {
                try {
                    getTextPaneEntryContent().scrollRectToVisible(getTextPaneEntryContent().modelToView(offset));
                } catch (BadLocationException e) {
                }
            }
        }
    }

    private String readZipEntry() {
        String entryContent = "";
        String resultFileName = zipEntryInfo.getResultFileName();
        if (!GUIStaticUtils.isEmpty(resultFileName)) {
            File entryFile = new File(resultFileName);
            try {
                entryContent = FileUtils.readFileToString(entryFile);
            } catch (IOException ex) {
                entryContent = ex.getMessage();
            }
        } else {
            entryContent = "Keine XML Datei produziert!";
        }
        return entryContent;
    }

}
