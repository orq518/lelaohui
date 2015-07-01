package com.llh.camera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ipcamer.demo.R;
import com.ipcamer.demo.SystemValue;
import com.llh.base.BaseNetActivity;
import com.llh.net.SysVar;
import com.llh.utils.CommonUtils;
import com.llh.utils.Constant.RESPONSE_CODE;
import com.llh.utils.MatchTool;
import com.llh.utils.SharedPreferenceUtil;
import com.llh.utils.SysValue;
import com.llh.utils.utils;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

import org.json.JSONException;
import org.json.JSONObject;

public class AddressAtivity extends BaseNetActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public void initView() {
	}

	@Override
	public int setLayout() {
		return R.layout.login_activity;
	}
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.btn_login:
			break;
		}
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);

	}
	private void enroll(){
		Intent intent = new Intent(AddressAtivity.this,EnrollPhoneActivity.class);
		startActivityForResult(intent, 100);
	}
	@Override
	protected void parserData(JSONObject response) {
		LogTool.i("登陆应答 ：");
		LogTool.i(response.toString());
		android.util.Log.d("xcq", "登录返回数据: "+response.toString());
//		String str = utils.readFile();
		try {
//			response = new JSONObject(str);
			JSONObject obj = response.getJSONObject("result");
			String code = obj.getString("code");
			if (RESPONSE_CODE.FAIL_CODE.equals(code)) {
				ToastTool.showText(AddressAtivity.this,obj.getString("msg"));
				return;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void dataError(VolleyError error) {
		LogTool.e(error.toString());
		
	}
}
