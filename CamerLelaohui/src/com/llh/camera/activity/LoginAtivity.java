package com.llh.camera.activity;

import org.json.JSONException;
import org.json.JSONObject;

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

public class LoginAtivity extends BaseNetActivity{
	@ViewInject(id = R.id.et_user_name)
	private EditText user_name;
	@ViewInject(id = R.id.et_user_password)
	private EditText user_pw;
	@ViewInject(id = R.id.btn_login, click = "onClick")
	private Button btn_login;
	@ViewInject(id = R.id.enroll_login, click = "onClick")
	private Button btn_enroll;
	private String userName, userPassword;
	@ViewInject(id=R.id.forget_pwd_tv,click="onClick")
	private TextView forget_pwd_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 //设置全屏显示
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
	}
	@Override
	public void initView() {
		userName = SharedPreferenceUtil.getUserInfo("user_name", this);
		userPassword = SharedPreferenceUtil.getUserInfo("user_password", this);
		user_name.setText(userName);
		user_pw.setText(userPassword);
		SystemValue.deviceId = SharedPreferenceUtil.getUserInfo(SysValue.KEY_CAMERA_ID, this);
		SystemValue.devicePass= SharedPreferenceUtil.getUserInfo(SysValue.KEY_CAMERA_PWD, this);
		SystemValue.deviceName = SharedPreferenceUtil.getUserInfo(SysValue.KEY_CAMERA_USER, this);
	}

	@Override
	public int setLayout() {
		return R.layout.login_activity;
	}
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.btn_login:
			LogTool.d("登录");
//			Intent intent = new Intent(LoginAtivity.this,MainActivity.class);
//			startActivity(intent);
//			SysValue.is_login = true;
			login();
			break;

		case R.id.enroll_login:
			LogTool.d("注册");
			enroll();
			break;
		case R.id.forget_pwd_tv:
			Intent i = new Intent();
			i.setClass(LoginAtivity.this, ForgetPassWordAitivity.class);
			this.startActivityForResult(i,100);
			break;
		}
	}
	private void login(){
		userName = CommonUtils.getContentFromEditText(user_name);
		userPassword = CommonUtils.getContentFromEditText(user_pw);
		if (userName.equals("")) {
			ToastTool.showText(LoginAtivity.this, "请输入用户名");

			return;
		}
		if (userPassword.equals("")) {
			ToastTool.showText(LoginAtivity.this, "请输入密码");
			return;
		}
		if (!MatchTool.match(MatchTool.PWD, userPassword)) {
			ToastTool.showText(LoginAtivity.this, 
					"请输入6-16位只包含汉字、字母、数字和特殊字符的密码");
			return;
		}
		LogTool.i("登陆请求：");
		Bundle param = new Bundle();
		param.putString("user", userName);
		param.putString("pwd", userPassword);
		reqData("/data/chklogin.json", param);
		String name = SharedPreferenceUtil.getUserInfo("user_name", this);
		//清楚本地图片
		if(!name.equals(userName)){
			LogTool.i("新用户登录");
			utils.deleteBtmap();
		}
		
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (null != arg2) {
			String name = arg2.getStringExtra("name");
			user_name.setText(name);
			String pwd = arg2.getStringExtra("pwd");
			user_pw.setText(pwd);
			SharedPreferenceUtil.saveUserInfo("user_name", name, getApplicationContext());
			SharedPreferenceUtil.saveUserInfo("user_password", pwd, getApplicationContext());
		}

	}
	private void  enroll(){
		Intent intent = new Intent(LoginAtivity.this,EnrollPhoneActivity.class);
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
				ToastTool.showText(LoginAtivity.this,obj.getString("msg"));
				return;
			}
			SharedPreferenceUtil.saveUserInfo("user_name", userName, getApplicationContext());
			SharedPreferenceUtil.saveUserInfo("user_password", userPassword, getApplicationContext());
			JSONObject rsObj=obj.getJSONObject("rs");
			SysVar.getInstance(this).saveUserInfo(rsObj);
			Intent intent = new Intent(LoginAtivity.this,MainActivity.class);
			startActivity(intent);
			SysValue.is_login = true;
			this.finish();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void dataError(VolleyError error) {
		LogTool.e(error.toString());
		
	}
}
