package ua.com.pandasushi.controller.inputconvert;

import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import org.controlsfx.control.spreadsheet.SpreadsheetCellEditor;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import java.util.List;

/**
 * Created by Тарас on 17.01.2017.
 */
public class AutoCompleteSpreadsheetCellEditor extends SpreadsheetCellEditor {

    private final List<String> itemList;
    private final AutoCompleteTextField autoTF;
    private String originalValue;

    public AutoCompleteSpreadsheetCellEditor (SpreadsheetView view, List<String> itemList) {
        super(view);
        this.itemList = itemList;
        this.autoTF = new AutoCompleteTextField();
        this.autoTF.getEntries().addAll(itemList);
    }

    @Override
    public void startEdit(Object o) {
        this.attachEnterEscapeEventHandler();
        this.autoTF.requestFocus();
        if(o instanceof String || o == null) {
            this.autoTF.setText((String)o);
        }
    }

    @Override
    public String getControlValue() {
        return this.autoTF.getText();
    }

    @Override
    public Control getEditor() {
        return this.autoTF;
    }

    @Override
    public void end() {
        this.autoTF.setOnKeyPressed((EventHandler)null);
    }



    private void attachEnterEscapeEventHandler() {
        this.autoTF.setOnKeyPressed(t -> {
            if(t.getCode() == KeyCode.ESCAPE) {
                AutoCompleteSpreadsheetCellEditor.this.autoTF.setText(AutoCompleteSpreadsheetCellEditor.this.originalValue);
                AutoCompleteSpreadsheetCellEditor.this.endEdit(false);
            } else if(t.getCode() == KeyCode.ENTER) {
                AutoCompleteSpreadsheetCellEditor.this.endEdit(true);
            }
        });
    }
}
