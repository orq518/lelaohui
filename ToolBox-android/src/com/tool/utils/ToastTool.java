package com.tool.utils;


import com.tool.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

public class ToastTool {
	private static LayoutInflater inflater;
	@SuppressLint("InflateParams") 
	public static void showText(Context context,String msg){
		inflater = LayoutInflater.from(context);
		TextView tv =(TextView)inflater.inflate(R.layout.toast_layout, null);
		tv.setText(msg);
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(tv);
		toast.show();
	}
}
