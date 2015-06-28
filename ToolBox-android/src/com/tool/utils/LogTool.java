package com.tool.utils;

import android.util.Log;

public class LogTool {

	public static void i(Object msg){
		Log.i("-i", String.valueOf(msg));
	}
	public static void e(Object msg){
		Log.e("-e", String.valueOf(msg));
	}
	public static void d(Object msg){
		Log.d("-d", String.valueOf(msg));
	}
}
