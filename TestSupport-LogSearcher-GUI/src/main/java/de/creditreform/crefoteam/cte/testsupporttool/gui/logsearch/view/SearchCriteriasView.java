package de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.logsearch.design.SearchCriteriasPanel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.testsupporttool.logsearch.LogEntry;
import de.creditreform.crefoteam.cte.testsupporttool.logsearch.SearchCriteria;
import de.creditreform.crefoteam.cte.tesun.util.TesunDateUtils;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchCriteriasView extends SearchCriteriasPanel {
    private final List<SearchCriteriasListener> searchCriteriasListenerList = new ArrayList<>();

    public SearchCriteriasView() {
        super();
        initListeners();
        initModel();
    }

    public void addSearchCriteriasListener(SearchCriteriasListener searchCriteriasListener) {
        searchCriteriasListenerList.add(searchCriteriasListener);
    }

    private void initListeners() {
        getComboBoxSearchCritsType().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyListeners();
            }
        });
        FocusListener textFieldFocusListener = new FocusListener() {
            @Override
            public void focusLost(FocusEvent focusEvent) {
                if (!focusEvent.isTemporary()) {
                    notifyListeners();
                }
            }

            @Override
            public void focusGained(FocusEvent focusEvent) {
            }
        };
        getTextFieldSearchCritsFrom().addFocusListener(textFieldFocusListener);
        getTextFieldSearchCritsTo().addFocusListener(textFieldFocusListener);
        getTextFieldSearchCritsPackage().addFocusListener(textFieldFocusListener);
        getTextFieldSearchCritsInfo().addFocusListener(textFieldFocusListener);

        ActionListener textFieldActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println(actionEvent.getActionCommand());
            }
        };
        getTextFieldSearchCritsFrom().addActionListener(textFieldActionListener);
        getTextFieldSearchCritsTo().addActionListener(textFieldActionListener);
        getTextFieldSearchCritsPackage().addActionListener(textFieldActionListener);
        getTextFieldSearchCritsInfo().addActionListener(textFieldActionListener);

        CaretListener textFieldCaretListener = new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent caretEvent) {
                System.out.println(caretEvent.getSource());
            }
        };
        getTextFieldSearchCritsFrom().addCaretListener(textFieldCaretListener);
        getTextFieldSearchCritsTo().addCaretListener(textFieldCaretListener);
        getTextFieldSearchCritsPackage().addCaretListener(textFieldCaretListener);
        getTextFieldSearchCritsInfo().addCaretListener(textFieldCaretListener);
    }

    protected void notifyListeners() {
        for (SearchCriteriasListener searchCriteriasListener : searchCriteriasListenerList) {
            searchCriteriasListener.updateSearchCriteria(getModel());
        }
    }

    private void initModel() {
        DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel(LogEntry.ENTRY_TYPE.values());
        getComboBoxSearchCritsType().setModel(defaultComboBoxModel);
    }

    private void initControlsState() {
    }

    /***********************           TableModelListener                ***********************/

    public SearchCriteria getModel() {
        LogEntry.ENTRY_TYPE selectedItem = (LogEntry.ENTRY_TYPE) getComboBoxSearchCritsType().getSelectedItem();
        SearchCriteria searchCriteria = new SearchCriteria(selectedItem);
        searchCriteria.setLogDateFrom(makeDateFromStringField(getTextFieldSearchCritsFrom()));
        searchCriteria.setLogDateTo(makeDateFromStringField(getTextFieldSearchCritsTo()));
        searchCriteria.setLogPackage(checkStarFromStringField(getTextFieldSearchCritsPackage()));
        searchCriteria.setLogInfo(checkStarFromStringField(getTextFieldSearchCritsInfo()));
        return searchCriteria;
    }

    private String checkStarFromStringField(JTextField textField) {
        String text = textField.getText();
        if (text.isEmpty() || text.equals("*")) {
            return null;
        }
        return text;
    }

    private Date makeDateFromStringField(JTextField textField) {
        String text = textField.getText();
        if (text.isEmpty() || text.equals("*")) {
            textField.setForeground(Color.BLACK);
            return null;
        }
        try {
            Date theDate = TesunDateUtils.DATE_FORMAT_DD_MM_YYYY_HH_MM_SS.parse(text);
            textField.setForeground(Color.BLACK);
            return theDate;
        } catch (ParseException ex) {
            try {
                Date theDate = TesunDateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS.parse(text);
                textField.setForeground(Color.BLACK);
                return theDate;
            } catch (ParseException ex1) {
                textField.setForeground(Color.RED);
                String errMsg = "Fehler beim Parsen als Datum" + text + "\n";
                GUIStaticUtils.showExceptionMessage(this, errMsg, ex1);
            }
        }
        return null;
    }

}
