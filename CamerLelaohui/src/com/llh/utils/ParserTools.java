package com.llh.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.llh.utils.Constant.CACHE_KEY;

public class ParserTools {
	public static ArrayList<Map<String, Object>> parserProduct(JSONArray data) {
		ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		int length = data.length();
		Map<String, Object> bundle = null;
		JSONObject jsonObject = null;
		for (int i = 0; i < length; i++) {
			try {
				jsonObject = data.getJSONObject(i);
				bundle = new HashMap<String, Object>();
				bundle.put("cateId", jsonObject.getString("cateId"));
				bundle.put("cateName", jsonObject.getString("cateName"));
				bundle.put("childProCateList", jsonObject.getString("childProCateList"));
				arrayList.add(bundle);
			} catch (JSONException e) {
				continue;
			}
		}
		return arrayList;
	}

	public static HashMap<Integer, ArrayList<HashMap<String, Object>>> parserFootProduct(String serial,
			JSONArray data) {
		HashMap<Integer, ArrayList<HashMap<String, Object>>> dataMap = new HashMap<Integer, ArrayList<HashMap<String, Object>>>();
		JSONObject jsonObject = null;
		int mealTime = -1;
		HashMap<String, Object> itemData = null;
		Iterator<String> keyIter = null;
		String keyStr=null;
		for (int i = 0; i < data.length(); i++) {
			try {
				jsonObject = data.getJSONObject(i);
				mealTime = jsonObject.getInt("mealTime");
				itemData = new HashMap<String, Object>();
				keyIter=jsonObject.keys();
				while(keyIter.hasNext()){
					keyStr=keyIter.next();
					
					itemData.put(keyStr, jsonObject.get(keyStr)==null||"null".equalsIgnoreCase(jsonObject.get(keyStr).toString())?"":jsonObject.get(keyStr));
				}
				itemData.put("serial", serial);
				if(serial.equals(Constant.CACHE_KEY.FOOT_TODAY_KEY)){
					
					itemData.put("isScope", 0);
				}else if(serial.equals(Constant.CACHE_KEY.FOOT_TOMORROW_KEY)){
					
					itemData.put("isScope", 1);
				}else if(serial.equals(Constant.CACHE_KEY.FOOT_AFTERTOMORROW_KEY)){
					
					itemData.put("isScope", 2);
				}
				if (dataMap.get(mealTime) == null) {
					ArrayList<HashMap<String, Object>> itemDataList=new ArrayList<HashMap<String, Object>>();
					itemDataList.add(itemData);
					dataMap.put(mealTime, itemDataList);
					
				}else{
					dataMap.get(mealTime).add(itemData);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
		}
		return dataMap;
	}

	public static ArrayList<Map<String, Object>> parserCommonTools(
			JSONArray data ,String key) {
		ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		int length = data.length();
		Map<String, Object> bundle = null;
		JSONObject jsonObject = null;
		Iterator<String> keyIter = null;
		String keyStr = null;
		for (int i = 0; i < length; i++) {
			try {
				jsonObject = data.getJSONObject(i);
				bundle = new HashMap<String, Object>();
				keyIter = jsonObject.keys();
				while (keyIter.hasNext()) {
					keyStr = keyIter.next();
					bundle.put(keyStr, jsonObject.getString(keyStr));
				}
//				if(key!=null){
//					bundle.put("server", key);
//				}
				arrayList.add(bundle);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
		}
		return arrayList;
	}

	public static void setProduct_cache(ACache product_cache,
			JSONObject response) throws JSONException {
		JSONObject result = response.getJSONObject("result");
		String code = result.getString("code");
		if ("2".equals(code)) {
			JSONArray jsonArray = result.getJSONArray("rs");
			ArrayList<Map<String, Object>> data = ParserTools
					.parserProduct(jsonArray);
			product_cache.put(CACHE_KEY.PRODUCT_TYPE_KEY, data,
					ACache.TIME_HOUR / 60);

		}
	}
}
