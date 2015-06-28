package com.llh.utils;

public class MatchTool {

	/**
	 * 匹配6~16位包含数字、字母和特殊符号的字符串
	 */
	public static final String PWD = "^[a-zA-Z0-9~!@#$%^*_]{6,16}$";
	/**
	 * 匹配1~20位包含数字、字母和中文的字符串
	 */
	public static final String USER_NAME = "^[a-zA-Z0-9\u4e00-\u9fa5]{1,20}$";

	/**
	 * @param type
	 *            匹配的类型(名称、密码)
	 * @param str
	 *            需要匹配的内容
	 * @return
	 */
	public static boolean match(String type, String str) {
		return str.matches(type);
	}
}
