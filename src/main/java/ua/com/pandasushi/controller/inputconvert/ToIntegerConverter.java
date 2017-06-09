package ua.com.pandasushi.controller.inputconvert;

import javafx.util.StringConverter;

public class ToIntegerConverter extends StringConverter<Integer>{

	@Override
	public String toString(Integer object) {
		if(object != null)
			return object.toString();
		else 
			return "";
	}

	@Override
	public Integer fromString(String string) {
		if(!string.matches("^[0-9]*$")) {
			string = string.replaceAll("\\D", "");
		}
		if(string!=null && !string.isEmpty())
			return Integer.parseInt(string);
		else
			return 0;
	}

}
