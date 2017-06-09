package ua.com.pandasushi.controller.inputconvert;

import javafx.util.StringConverter;

public class ToStringConverter extends StringConverter<String>{

	@Override
	public String toString(String object) {
		if(object != null)
			return object.toString();
		else
			return "";
	}

	@Override
	public String fromString(String string) {
		return string;
	}
	
}
