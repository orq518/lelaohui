package com.tool.utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilsTiem {

	/**
	 * 获取当前时间 
	 * 如： yyyy:MM:dd:HH:mm:ss
	 * @param pattern
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
	public static String CurrentTime(String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}
}
