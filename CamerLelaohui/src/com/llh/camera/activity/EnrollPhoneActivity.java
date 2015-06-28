package com.llh.camera.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ipcamer.demo.R;
import com.llh.base.BaseNetActivity;
import com.llh.net.SysVar;
import com.llh.utils.SharedPreferenceUtil;
import com.llh.utils.SysValue;
import com.llh.utils.Constant.RESPONSE_CODE;
import com.llh.utils.utils;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

public class EnrollPhoneActivity extends BaseNetActivity {
	String randomNum;
	private final static String GET_CODE = "getCode";
	private final static String REGISTER = "register";
	private final static String CHECK_CODE = "check_code";

	@Override
	protected void parserData(JSONObject response) {
		// TODO Auto-generated method stub
		LogTool.i(response.toString());
		try {
			JSONObject obj = response.getJSONObject("result");
			String code = obj.getString("code");
			String serial = obj.getString("serial");
			String msg = obj.getString("rs");
			if (!TextUtils.isEmpty(serial) && GET_CODE.equals(serial))// 获取验证码
			{
				Toast.makeText(EnrollPhoneActivity.this, msg, Toast.LENGTH_SHORT)
				.show();
				if ("1".equals(code)) {// 成功
				} else if ("3".equals(code)) {
					in_phonenum.setText("");
					isRun = false;
					isClickable = true;
					yanzheng_code.setEnabled(true);
					yanzheng_code.setText("获取验证码");
				} else {
					isClickable = true;
					isRun = false;
					yanzheng_code.setEnabled(true);
					yanzheng_code.setText("获取验证码");
				}
			} else if (!TextUtils.isEmpty(serial) && REGISTER.equals(serial)) {// 注册
				Toast.makeText(EnrollPhoneActivity.this, msg, Toast.LENGTH_SHORT)
				.show();
				if ("1".equals(code)) {// 成功
					SysVar.getInstance(getApplicationContext()).clearUserInfo();
					Intent i = new Intent();
					i.putExtra("name", enroll_phone.getText().toString());
					i.putExtra("pwd", in_pwd.getText().toString());
					setResult(RESULT_OK, i);
					finish();
				} else if ("2".equals(code)) {
					in_phonenum.setText("");
					in_code.setText("");
				} else {
					isClickable = true;
					yanzheng_code.setEnabled(true);
					yanzheng_code.setText("获取验证码");

				}
			} else if (!TextUtils.isEmpty(serial) && CHECK_CODE.equals(serial)) {// 校验验证码
				if ("1".equals(code)) {// 成功
					sure();
				} else {
					isClickable = true;
					yanzheng_code.setEnabled(true);
					yanzheng_code.setText("获取验证码");
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void dataError(VolleyError error) {
		// TODO Auto-generated method stub
		LogTool.e(error);

	}

	@ViewInject(id = R.id.titlebar_text)
	TextView titlebar_text;
	@ViewInject(id = R.id.enroll_phone)
	EditText enroll_phone;
	@ViewInject(id = R.id.in_phonenum)
	EditText in_phonenum;
	@ViewInject(id = R.id.in_code)
	EditText in_code;
	@ViewInject(id = R.id.yanzheng_code, click = "onClick")
	TextView yanzheng_code;
	@ViewInject(id = R.id.xiayibu, click = "onClick")
	Button xiayibu;
	@ViewInject(id = R.id.in_pwd)
	EditText in_pwd;
	@ViewInject(id = R.id.in_pwd_1)
	EditText in_pwd_1;
	@ViewInject(id = R.id.check_btn, click = "onClick")
	TextView check_btn;

	@Override
	public void initView() {
		titlebar_text.setText("注册");
		yanzheng_code.setText("获取验证码");
		isClickable = true;
		// Bundle param = new Bundle();
		// param.putString("android", "android");
		// param.putString("serial", "enroll_phone");
		// reqData("/data/getRandom.json", param);

	}

	@ViewInject(id = R.id.left_btn, click = "onClick")
	private ImageButton left_btn;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_btn:
			finish();
			break;
		case R.id.xiayibu:
				if (TextUtils.isEmpty(in_phonenum.getText())) {
					ToastTool.showText(this, "手机号码不能为空！");
					return;
				}
				if (!utils.isMobile(in_phonenum.getText().toString())) {
					ToastTool.showText(this, "手机号码格式不正确！");
					return;
				}
				if (TextUtils.isEmpty(in_code.getText())) {
					ToastTool.showText(this, "验证码不能为空！");
					return;
				}
				Bundle b = new Bundle();
				b.putString("phone", in_phonenum.getText().toString());
				b.putString("randomNum", in_code.getText().toString());
				b.putString("serial", CHECK_CODE);
				reqData("/data/getPhoneCompareRandom.json", b);
			break;
		case R.id.yanzheng_code:
			if (TextUtils.isEmpty(in_phonenum.getText())) {
				ToastTool.showText(this, "手机号码不能为空！");
				return;
			}
			if (!utils.isMobile(in_phonenum.getText().toString())) {
				ToastTool.showText(this, "手机号码格式不正确！");
				return;
			}
			isRun = true;
			new Thread(timer).start();
			v.setEnabled(false);
			isClickable = false;
			Bundle param = new Bundle();
			param.putString("phone", in_phonenum.getText().toString());
			param.putString("serial", GET_CODE);
			reqData("/data/getSendRandom.json", param);
			break;
		case R.id.check_btn:
			if (TextUtils.isEmpty(in_phonenum.getText())) {
				ToastTool.showText(this, "手机号码不能为空！");
				return;
			}
			if (!utils.isMobile(in_phonenum.getText().toString())) {
				ToastTool.showText(this, "手机号码格式不正确！");
				return;
			}
			if (TextUtils.isEmpty(in_code.getText())) {
				ToastTool.showText(this, "验证码不能为空！");
				return;
			}
			Bundle bundle = new Bundle();
			bundle.putString("phone", in_phonenum.getText().toString());
			bundle.putString("randomNum", in_code.getText().toString());
			reqData("/data/getPhoneCompareRandom.json", bundle);
			break;
		default:
			break;
		}
	}
	boolean isRun = true;
	Runnable timer = new Runnable() {

		@Override
		public void run() {
			int count = 60;
			while (isRun) {
				try {
					Message message = new Message();
					message.what = count;
					handler.sendMessage(message);
					count--;
					if (count <= 0) {
						isRun = false;
					}
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 要做的事情
			if (msg.what > 1 && !isClickable) {
				yanzheng_code.setText(msg.what + "s");
			} else {
				yanzheng_code.setText("获取验证码");
				yanzheng_code.setEnabled(true);
			}
			super.handleMessage(msg);
		}
	};
	boolean isClickable = true;

	private void sure() {

		String str = in_pwd.getText().toString();
		String str1 = in_pwd_1.getText().toString();
		String ph = enroll_phone.getText().toString();
		if (TextUtils.isEmpty(ph)) {
			ToastTool.showText(this, "请输入用户名");
			return;
		}
		if (TextUtils.isEmpty(in_phonenum.getText())) {
			ToastTool.showText(this, "手机号码不能为空！");
			return;
		}
		if (!utils.isMobile(in_phonenum.getText().toString())) {
			ToastTool.showText(this, "手机号码格式不正确！");
			return;
		}
		if (TextUtils.isEmpty(str)) {
			ToastTool.showText(this, "密码不能为空");
			return;
		}
		if (TextUtils.isEmpty(str1)) {
			ToastTool.showText(this, "请再次确认密码");
			return;
		}
		if (!str.equals(str1)) {
			ToastTool.showText(this, "输入的密码不一致");
			return;
		}
		if (!TextUtils.isEmpty(str1) && str.length() < 6) {
			ToastTool.showText(this, "密码长度不能小于6位");
			return;
		}
		Bundle param = new Bundle();
		param.putString("userName", ph);
		param.putString("userPwd", str);
		param.putString("serial", REGISTER);
		param.putString("phone", in_phonenum.getText().toString());
		reqData("/data/registerByPhone.json", param);
		LogTool.i("==============");
	}

	@Override
	public int setLayout() {
		return R.layout.enroll_phone;
	}

}
