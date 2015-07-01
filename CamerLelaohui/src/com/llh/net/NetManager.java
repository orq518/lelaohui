package com.llh.net;

import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ipcamer.app.MyApplication;
import com.llh.utils.SysValue;
import com.llh.utils.utils;
import com.tool.utils.LogTool;

public class NetManager {
	private RequestQueue mQueue = null;

//	 public static final String Ip = "http://192.168.0.227:8080";
//	 public static final String Ip = "http://192.168.5.19:8080";
	 public static final String Ip = "http://111.204.236.15:8081";
//	 public static final String Ip = "http://111.204.236.5:8081";
	private Context context = null;
	/**
	 * ���Ե�ַ
	 */
	// public static final String Ip = "http://192.168.1.110:8080";
	private SysVar var = null;

	private NetManager(Context context) {
		mQueue = Volley.newRequestQueue(context);

		var = SysVar.getInstance(context);
		this.context = context;
	}

	private static NetManager netManager = null;

	public static NetManager getInstance(Context context) {
		if (netManager == null) {
			netManager = new NetManager(context);
		}
		return netManager;
	}

	public void cancelData(Object tag) {
		mQueue.cancelAll(tag);
	}

	public void reqData(String action, Bundle param,
			Response.Listener<JSONObject> dataListener,
			Response.ErrorListener err, Object tag, boolean isServcice) {
		if (action == null)
			return;
		StringBuffer url = getUrl(action, param);

		String urlStr = url.toString();
		android.util.Log.d("xcq", "urlStr: " + urlStr);
		if (action.equals("/data/paymentReminders.json")) {
			utils.writeLogToFile(MyApplication.getMyApplication(), urlStr);

		}
		RetryPolicy rp = new DefaultRetryPolicy() {

			@Override
			public void retry(VolleyError arg0) throws VolleyError {
				// TODO Auto-generated method stub
				LogTool.e(arg0.getMessage());
				// Toast.makeText(context, arg0.getMessage(),
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public int getCurrentTimeout() {
				// TODO Auto-generated method stub
				return 30000;
			}

			@Override
			public int getCurrentRetryCount() {
				return 5;
			}
		};
		if (checkNetWorkStatus(this.context)) {
			JsonObjectRequest request = new JsonObjectRequest(Method.POST,
					url.toString(), null, dataListener, err);
			request.setShouldCache(false);
			if (!isServcice) {
				request.setRetryPolicy(rp);
			}
			request.setTag(tag);
			LogTool.i(urlStr);
			mQueue.add(request);
		} else {
			if (!isServcice)
				Toast.makeText(context, "当前网络不可用，请您设置网络", Toast.LENGTH_SHORT)
						.show();
		}

	}

	protected StringBuffer getUrl(String action, Bundle param) {
		StringBuffer url = new StringBuffer(Ip);
		url.append(action);
		if (param != null) {
			url.append("?");
			Iterator<String> key = param.keySet().iterator();
			String keyStr;
			while (key.hasNext()) {
				keyStr = key.next();
				url.append("&").append(keyStr).append("=")
						.append(param.getString(keyStr));
			}
		}
		// if (SysValue.is_login) {
		Map<String, Object> userInfo = var.getUserInfo();
		if (null != userInfo&&getIsAddUserInfo(action)) {
			String userId = (String) userInfo.get("userId");
			if (action.equals("/data/paymentReminders.json")) {
				utils.writeLogToFile(MyApplication.getMyApplication(), "userid"
						+ userInfo.get("userId"));

			}
			if (!TextUtils.isEmpty(userId)) {
				url.append("&customerId=").append(userId);
				url.append("&userId=").append(userId);
				Iterator<String> key = userInfo.keySet().iterator();
				String keyStr = null;
				while (key.hasNext()) {
					keyStr = key.next();
					if (!TextUtils.isEmpty(keyStr)
							&& keyStr.indexOf("userId") != -1) {
						continue;
					}
					if (!TextUtils.isEmpty(keyStr)
							&& keyStr.indexOf("cameraManagers") != -1) {
						continue;
					}
					if (keyStr != null) {
						url.append("&").append(keyStr).append("=")
								.append(userInfo.get(keyStr));
					}
				}
			}
		}

		// }
		return url;
	}

	private boolean getIsAddUserInfo(String action) {
		return !TextUtils.isEmpty(action)
				&& action.indexOf("registerByPhone") == -1
				&& action.indexOf("getPhoneCompareRandom") == -1
				&&action.indexOf("getSendRandom")==-1&&action.indexOf("getRandomComPhoneForPwd")==-1&&action.indexOf("getRandomForPwd")==-1&&action.indexOf("chklogin")==-1&&action.indexOf("updateUserPwd")==-1;
	}
	protected boolean checkNetWorkStatus(Context context) {
		boolean result;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if (netinfo != null && netinfo.isConnected()) {
			result = true;
			Log.i("NetStatus", "The net was connected");
		} else {
			result = false;
			Log.i("NetStatus", "The net was bad!");
		}
		return result;
	}
}
