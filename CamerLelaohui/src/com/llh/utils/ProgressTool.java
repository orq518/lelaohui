package com.llh.utils;

import android.app.ProgressDialog;
import android.content.Context;


public class ProgressTool {

	/**对话框进度条*/
	public static ProgressDialog getDialog(Context context,CharSequence message){
		ProgressDialog progressdlg = new ProgressDialog(context);
		progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdlg.setMessage(message);
		return progressdlg;
	}
}
