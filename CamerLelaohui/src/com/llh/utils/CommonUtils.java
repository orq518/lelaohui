package com.llh.utils;

import android.widget.EditText;

public class CommonUtils {
	
	
	
	public static String getContentFromEditText(EditText et) {

		if (et.getText() != null) {
			return String.valueOf(et.getText());
		} else {
			return "";
		}
	}
}
