package com.llh.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ipcamer.app.MyApplication;
import com.llh.net.NetManager;
import com.llh.utils.Constant.CACHE_KEY;
import com.llh.utils.Constant.REQ_ACTION;

public class FoodCacheUtils {
	private static FoodCacheUtils intance = null;

	private FoodCacheUtils() {
	}

	public static FoodCacheUtils getIntance() {
		if (null == intance) {
			intance = new FoodCacheUtils();
		}
		return intance;
	}
    public void reqFoodData(){//请求需要缓存的数据

//		ACache product_cache = MyApplication.getMyApplication().getProductCache();
//		if (product_cache.getAsObject(CACHE_KEY.PRODUCT_TYPE_KEY) == null) {
//			Bundle param = new Bundle();
//			param.putString("serial", CACHE_KEY.PRODUCT_TYPE_KEY);
//			param.putString("cateType", "1");
//			reqData(REQ_ACTION.CATEGOR_ACTION, param);
//		}
		ACache foot_cache =  MyApplication.getMyApplication().getFoodListCache();
		if (foot_cache.getAsObject(CACHE_KEY.FOOT_TODAY_KEY) == null) {
			Bundle param = new Bundle();
			param.putString("serial", CACHE_KEY.FOOT_TODAY_KEY);
			param.putString("cateType", "1");
			param.putString("isScope", "0");
			reqData(REQ_ACTION.FOOT_ACTION, param);
		}
		if (foot_cache.getAsObject(CACHE_KEY.FOOT_TOMORROW_KEY) == null) {
			Bundle param = new Bundle();
			param.putString("serial", CACHE_KEY.FOOT_TOMORROW_KEY);
			param.putString("cateType", "1");
			param.putString("isScope", "1");
			reqData(REQ_ACTION.FOOT_ACTION, param);
		}
		if (foot_cache.getAsObject(CACHE_KEY.FOOT_AFTERTOMORROW_KEY) == null) {
			Bundle param = new Bundle();
			param.putString("serial", CACHE_KEY.FOOT_AFTERTOMORROW_KEY);
			param.putString("cateType", "1");
			param.putString("isScope", "2");
			reqData(REQ_ACTION.FOOT_ACTION, param);
		}
	
    	
    }
	public void reqData(String action, Bundle param) {
		NetManager.getInstance(MyApplication.getMyApplication()).reqData(
				action, param, responseListener, errorListener, this, false);
	}

	private Response.ErrorListener errorListener = new Response.ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			// dataError(error);
		}
	};
	private Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// jie xi
			try {
				JSONObject obj = response.getJSONObject("result");
				String code = obj.getString("code");
				String serial = obj.getString("serial");
				if(!TextUtils.isEmpty(code)&&"2".equals(code)){
					JSONArray jsonArray = obj.getJSONArray("rs");
					if (CACHE_KEY.FOOT_TODAY_KEY
							.equals(serial)
							|| CACHE_KEY.FOOT_TOMORROW_KEY
							.equals(serial)
							|| CACHE_KEY.FOOT_AFTERTOMORROW_KEY
							.equals(serial)) {
						HashMap<Integer, ArrayList<HashMap<String, Object>>> data = (HashMap<Integer, ArrayList<HashMap<String, Object>>>) ParserTools
								.parserFootProduct(serial,
										jsonArray);
						MyApplication.getMyApplication().getFoodListCache().put(serial, data);
						
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};
}
