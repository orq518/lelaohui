package com.llh.utils;

import java.util.Objects;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressLint({ "NewApi", "CommitPrefEdits" }) 
public class SharedPreferenceUtil {
	static SharedPreferences sp;

	/**
	 * 
	 * @param key
	 * @param value
	 * @param context
	 */
	public static void saveUserInfo(String key, Object value, Context context) {
		sp = getSpInstance(context);
		Editor editor = sp.edit();
		if(value==null)
			return;
		if (value instanceof String) {
			editor.putString(key, String.valueOf(value));
		} else if (value instanceof Boolean) {
			editor.putBoolean(key, (Boolean) value);
		}else if(value.getClass()==Long.class){
			editor.putLong(key, (Long)(value));
		}else if(value.getClass()==Integer.class){
			editor.putInt(key, (Integer)(value));
		}
		editor.commit();
	}
	public static void saveCustomer( Context context,String key,Set<String>set){
		sp = getSpInstance(context);
		Editor editor = sp.edit();
		editor.putStringSet(key, set);
		
	} 
	public static String getUserInfo(String key, Context context) {
		sp = getSpInstance(context);
		return sp.getString(key, "");
	}

	public static boolean getUserLoginInfo(String key, Context context) {
		sp = getSpInstance(context);
		return sp.getBoolean(key, false);
	}

	public static SharedPreferences getSpInstance(Context context) {
		if (sp == null) {
			sp = context.getSharedPreferences("user_info_shared_preference",
					Context.MODE_PRIVATE);
		} else {
			return sp;
		}
		return sp;
	}
	/**
	 * 设置参数
	 */
	public static String getString(Context context,String name,String defaultString){
		sp = getSpInstance(context);
		return sp.getString(name,defaultString);

	}
	/*
	* 设置参数
	*/
	public static boolean getBoolean(Context context,String name,boolean defaultBoolean){
		sp = getSpInstance(context);
		return sp.getBoolean(name,defaultBoolean);

	}
	/**
	 * 设置参数
	 */
	public static void saveString(Context context,String name,String objects){
		sp = getSpInstance(context);
		Editor editor = sp.edit();
		editor.putString(name,objects);
		editor.commit();

	}
	/*
	* 设置参数
	*/
	public static void saveBoolean(Context context,String name,boolean objects){
		sp = getSpInstance(context);
		Editor editor = sp.edit();
		editor.putBoolean(name, objects);
		editor.commit();

	}
}
