package com.llh.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TiemUtils {
	/** 早餐 */
	private static final String breakfast = "8:30";
	/** 午餐 */
	private static final String lunch = "12:30";
	/** 晚餐 */
	private static final String dinner = "18:30";
	/** 早餐时间 */
	private static java.util.Calendar breakfast_tiem = java.util.Calendar
			.getInstance();
	/** 午餐时间 */
	private static java.util.Calendar lunch_tiem = java.util.Calendar
			.getInstance();
	/** 晚餐时间 */
	private static java.util.Calendar dinner_tiem = java.util.Calendar
			.getInstance();
	/** 当前时间 */
	private static java.util.Calendar current_tiem = java.util.Calendar
			.getInstance();

	public static int EqualsCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		// 获取当前时间
		String curenttime = CurrentTime();
		try {
			current_tiem.setTime(formatter.parse(curenttime));
			breakfast_tiem.setTime(formatter.parse(breakfast));
			lunch_tiem.setTime(formatter.parse(lunch));
			dinner_tiem.setTime(formatter.parse(dinner));
		} catch (Exception e) {
			e.printStackTrace();
		}
		int breakfast_result = current_tiem.compareTo(breakfast_tiem);
		if (breakfast_result <= 0) {// 早餐1
			return 1;
		}
		int lunch_result = current_tiem.compareTo(lunch_tiem);
		if (lunch_result <= 0) {// 午餐2
			return 2;
		}
		int dinner_result = current_tiem.compareTo(dinner_tiem);
		if (dinner_result <= 0) {// 晚餐3
			return 3;
		}else {//加餐
			return 4;
		}
	}

	// public static void MealTime(){
	// SimpleDateFormat CurrentTime = new SimpleDateFormat(
	// "HH:mm");
	// dinner.equals(other)
	// }

	/**
	 * 获取当前时间
	 * 
	 * @return String
	 */
	public static String CurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}
}
