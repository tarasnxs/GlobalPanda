package ua.com.pandasushi.controller.inputconvert;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class FloatFieldChangeListener implements ChangeListener<String> {
	private TextField source;

	public FloatFieldChangeListener(TextField source) {
		this.source = source;
	}

	@Override
	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		
		if (!newValue.matches("\\-?\\d+(\\.\\d{0,})?")) {
            source.setText(newValue.replaceAll("[^\\d\\.]", ""));
        }
	}

}
