package ua.com.pandasushi.controller.inputconvert;

import javafx.util.StringConverter;

public class ToFloatConverter extends StringConverter<Float>{

	@Override
	public Float fromString(String string) {
		if (!string.matches("\\-?\\d+(\\.\\d{0,})?")) {
            string = string.replaceAll("[^\\d\\.]", "");
        }
		if(string != null && !string.isEmpty())
			return Float.parseFloat(string);
		else
			return 0.0f;
	}

	@Override
	public String toString(Float object) {
		return object.toString();
	}
	
}
