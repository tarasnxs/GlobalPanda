package ua.com.pandasushi.controller.inputconvert;

import javafx.collections.ObservableList;
import javafx.util.converter.DefaultStringConverter;
import org.controlsfx.control.spreadsheet.*;

/**
 * Created by Тарас on 17.01.2017.
 */
public class AutocompleteSpreadsheetCellType extends SpreadsheetCellType<String> {
    protected ObservableList<String> items;

    public AutocompleteSpreadsheetCellType (final ObservableList<String> items) {
        super(new DefaultStringConverter() {
            public String fromString(String str) {
                return str != null && items.contains(str)?str:null;
            }
        });
        this.items = items;
    }

    @Override
    public SpreadsheetCellEditor createEditor(SpreadsheetView spreadsheetView) {
        return new AutoCompleteSpreadsheetCellEditor(spreadsheetView, this.items);
    }

    public ObservableList<String> getItems() {
        return items;
    }

    public void setItems(ObservableList<String> items) {
        this.items = items;
    }

    public SpreadsheetCell createCell(int row, int column, int rowSpan, int columnSpan, String value) {
        SpreadsheetCellBase cell = new SpreadsheetCellBase(row, column, rowSpan, columnSpan, this);
        if(this.items != null && this.items.size() > 0) {
            if(value != null && this.items.contains(value)) {
                cell.setItem(value);
            } else {
                cell.setItem("");
            }
        }
        return cell;
    }

    @Override
    public String toString(String s) {
        return this.converter.toString(s);
    }

    @Override
    public String toString() {
        return "autocomplete";
    }

    @Override
    public boolean match(Object o) {
        if (o instanceof String) {
            if (this.items.contains(o.toString()) || o.toString().isEmpty() )
                return true;
            else {
                return false;
            }
        }

        return o instanceof String && this.items.contains(o.toString())?true:this.items.contains(o == null?null:o.toString());
    }

    @Override
    public String convertValue(Object o) {
        return (String)this.converter.fromString(o == null?null:o.toString());
    }
}
