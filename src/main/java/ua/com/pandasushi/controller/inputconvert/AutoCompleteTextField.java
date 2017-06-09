package ua.com.pandasushi.controller.inputconvert;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.*;

/**
 * This class is a TextField which implements an "autocomplete" functionality, based on a supplied list of entries.
 * @author Caleb Brinkman
 */
public class AutoCompleteTextField extends TextField
{
    /** The existing autocomplete entries. */
    private final SortedSet<String> entries;
    /** The popup used to select an entry. */
    private ContextMenu entriesPopup;

    /** Construct a new AutoCompleteTextField. */
    public AutoCompleteTextField() {
        super();
        entries = new TreeSet<>();
        entriesPopup = new ContextMenu();
        textProperty().addListener((observableValue, s, s2) -> {
            if (getText() == null || getText().length() == 0) {
                entriesPopup.hide();
            } else {
                String entered = getText().toLowerCase();
                String trans = toAnotherKeyboard(entered);
                LinkedList<String> searchResult = new LinkedList<>();
                Iterator<String> iterator = entries.iterator();
                while (iterator.hasNext()) {
                    String c = iterator.next();
                    if(c.toLowerCase().contains(entered) || c.toLowerCase().contains(trans))
                        searchResult.add(c);
                }
                if (entries.size() > 0) {
                    populatePopup(searchResult);
                    if (!entriesPopup.isShowing()) {
                        entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
                    }
                } else {
                    entriesPopup.hide();
                }
            }
        });

        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                if(!isFocused())
                    entriesPopup.hide();
            }
        });

    }

    /**
     * Get the existing set of autocomplete entries.
     * @return The existing autocomplete entries.
     */
    public SortedSet<String> getEntries() { return entries; }

    public String toAnotherKeyboard (String s) {
        String result = "";
        char[][] key = {
                {'q', 'й'},
                {'w', 'ц'},
                {'e', 'у'},
                {'r', 'к'},
                {'t', 'е'},
                {'y', 'н'},
                {'u', 'г'},
                {'i', 'ш'},
                {'o', 'щ'},
                {'p', 'з'},
                {'[', 'х'},
                {']', 'ї'},
                {'a', 'ф'},
                {'s', 'і'},
                {'d', 'в'},
                {'f', 'а'},
                {'g', 'п'},
                {'h', 'р'},
                {'j', 'о'},
                {'k', 'л'},
                {'l', 'д'},
                {';', 'ж'},
                {'\'', 'є'},
                {'z', 'я'},
                {'x', 'ч'},
                {'c', 'с'},
                {'v', 'м'},
                {'b', 'и'},
                {'n', 'т'},
                {'m', 'ь'},
                {',', 'б'},
                {'.', 'ю'}
        };
        for(int i = 0; i < s.length(); i++) {
            char t = s.charAt(i);
            for(int j = 0; j < key.length; j++) {
                if( t == key[j][0] || t == key[j][1] ) {
                    result += t == key[j][0] ? key[j][1] : key[j][0];
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Populate the entry set with the given search results.  Display is limited to 10 entries, for performance.
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(List<String> searchResult) {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        // If you'd like more entries, modify this line.
        int maxEntries = 10;
        int count = Math.min(searchResult.size(), maxEntries);
        for (int i = 0; i < count; i++)
        {
            final String result = searchResult.get(i);
            Label entryLabel = new Label(result);
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            item.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent actionEvent) {
                    setText(result);
                    entriesPopup.hide();
                }
            });
            menuItems.add(item);
        }
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);

    }
}