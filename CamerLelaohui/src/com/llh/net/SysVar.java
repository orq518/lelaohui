package com.llh.net;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.ipcamer.app.MyApplication;
import com.tool.utils.LogTool;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class SysVar {
	private static SysVar sysVar = null;
	private SharedPreferences userInfo;
	private SharedPreferences userInfo2;
	private Context context;
	private Map<String, Object> userBundle = null;

	private SysVar(Context context) {
		this.context = context;
	}

	public static SysVar getInstance(Context context) {
		if (sysVar == null) {
			sysVar = new SysVar(context);
		}
		return sysVar;
	}

	public void saveUserInfo(JSONObject obj) {

		userInfo = this.context.getSharedPreferences("oldmanuserinfo",
				Activity.MODE_PRIVATE);
		userInfo2  = this.context.getSharedPreferences("oldmanuserinfo2",
				Activity.MODE_PRIVATE);
		Iterator<String> keyIter = obj.keys();
		String keyStr = null;
		Object object = null;
		SharedPreferences.Editor editor = userInfo.edit(); // �༭����
		if (userBundle == null) {
			userBundle = new HashMap<String, Object>();
		}
		while (keyIter.hasNext()) {
			keyStr = keyIter.next();
			try {
				object = obj.get(keyStr);
				userBundle.put(keyStr, object);
				if(!TextUtils.isEmpty(keyStr)&&keyStr.equals("cameraStatus"))
				{
					String status = String.valueOf(object);
					SharedPreferences.Editor editor2 = userInfo2.edit();
					editor2.clear();
					editor2.commit();
					String[] value = status.split(",");
					if(null!=value){
						if(value.length>=1&&!TextUtils.isEmpty(value[0])&&"1".equals(value[0]))
						{
							editor2.putBoolean("userIsNull", true);
						}
						if(value.length>=2&&!TextUtils.isEmpty(value[1])&&"1".equals(value[1]))
						{
							editor2.putBoolean("userIsBinded", true);
						}
						if(value.length>=3&&!TextUtils.isEmpty(value[2])&&"1".equals(value[2]))
						{
							editor2.putBoolean("userIsAdmin", true);
						}
						editor2.commit();
					}
					continue;
				}
				if (object.getClass() == String.class) {
					editor.putString(keyStr, String.valueOf(object));
				} else if (object.getClass() == Boolean.class
						|| object.getClass() == boolean.class) {
					editor.putBoolean(keyStr, (Boolean) object);
				} else if (object.getClass() == Integer.class
						|| object.getClass() == int.class) {
					editor.putInt(keyStr, (Integer) object);
				} else if (object.getClass() == Float.class
						|| object.getClass() == float.class) {
					editor.putFloat(keyStr, (Float) object);
				} else if (object.getClass() == Long.class
						|| object.getClass() == long.class) {
					editor.putLong(keyStr, (Long) object);
				} else if (object.getClass() == JSONObject.class) {
//					SharedPreferences.Editor editor2 = userInfo2.edit();
//					String key = null;
//					Iterator<String> keyIter1 = ((JSONObject)object).keys();
//					editor2.putString("CameraAdministrator", "");
//					editor2.commit();
//					while(keyIter1.hasNext()){
//						key = keyIter1.next();
//						String value =  String.valueOf(((JSONObject)object).get(key));
//						editor2.putString("CameraAdministrator", value);
//						editor2.commit();
//					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		editor.commit();
	}
	public void setUserCameraAdministrator(String status){
		if(null==userInfo2)
    	{
    		userInfo2 = this.context.getSharedPreferences("oldmanuserinfo2",
					Activity.MODE_PRIVATE);	
    	}
		SharedPreferences.Editor editor = userInfo2.edit();
		editor.putBoolean("userIsAdmin", true);
		editor.commit();
	}
	
	public void setUserIsNotCameraAdministrator(String status){
		if(null==userInfo2)
    	{
    		userInfo2 = this.context.getSharedPreferences("oldmanuserinfo2",
					Activity.MODE_PRIVATE);	
    	}
		SharedPreferences.Editor editor = userInfo2.edit();
		editor.putBoolean("userIsAdmin", false);
		editor.commit();
	}
    public boolean getUserIsCameraAdministrator(String cameraid){
//    	if(TextUtils.isEmpty(cameraid))
//    		return false;
    	//暂时就一个摄像头写死  key  CameraAdministrator
    	if(null==userInfo2)
    	{
    		userInfo2 = this.context.getSharedPreferences("oldmanuserinfo2",
					Activity.MODE_PRIVATE);	
    	}
    	return userInfo2.getBoolean("userIsAdmin", false);
    }
    public boolean getUserIsNull(){
    	if(null==userInfo2)
    	{
    		userInfo2 = this.context.getSharedPreferences("oldmanuserinfo2",
					Activity.MODE_PRIVATE);	
    	}
    	return userInfo2.getBoolean("userIsNull", false);
    }
    public void setUserIsNull(boolean isNull){
    	if(null==userInfo2)
    	{
    		userInfo2 = this.context.getSharedPreferences("oldmanuserinfo2",
					Activity.MODE_PRIVATE);	
    	}
    	SharedPreferences.Editor editor = userInfo2.edit();
		editor.putBoolean("userIsNull", isNull);
		editor.commit();
    }
    public boolean getUseIsBinded(){

    	if(null==userInfo2)
    	{
    		userInfo2 = this.context.getSharedPreferences("oldmanuserinfo2",
					Activity.MODE_PRIVATE);	
    	}
    	return userInfo2.getBoolean("userIsBinded", false);
    
    }
    public String getUserCameraAdministratorStatus(String cameraid){
//    	if(TextUtils.isEmpty(cameraid))
//    		return false;
    	//暂时就一个摄像头写死  key  CameraAdministrator
    	if(null==userInfo2)
    	{
    		userInfo2 = this.context.getSharedPreferences("oldmanuserinfo2",
					Activity.MODE_PRIVATE);	
    	}
    	return userInfo2.getString("CameraAdministrator", "");
    }
	public void clearUserInfo() {
		if (userInfo == null) {
			userInfo = this.context.getSharedPreferences("oldmanuserinfo",
					Activity.MODE_PRIVATE);
		}
		SharedPreferences.Editor editor = userInfo.edit(); // �༭����
		editor.clear();
		editor.commit();
	}

	public Map<String, Object> getUserInfo() {

		if (userInfo == null) {
			userInfo = this.context.getSharedPreferences("oldmanuserinfo",
					Activity.MODE_PRIVATE);
			userBundle = extracted();
		}
		return userBundle;
	}

	private Map<String, Object> extracted() {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> all = ((Map<String, Object>) userInfo.getAll());
			LogTool.i("userInfo = " + all);
			if (all.size() == 0) {
				return null;
			}
			return all;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}
}
