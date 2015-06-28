package com.llh.camera.activity;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
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
import com.llh.utils.utils;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

public class ForgetPassWordAitivity extends BaseNetActivity {
	String randomNum;
	private final static String GET_CODE = "getCode";
	private final static String COMMINT = "commint";
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
				Toast.makeText(ForgetPassWordAitivity.this, msg,
						Toast.LENGTH_SHORT).show();
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
			} else if (!TextUtils.isEmpty(serial) && COMMINT.equals(serial)) {// 注册
				Toast.makeText(ForgetPassWordAitivity.this,
						obj.getString("msg"), Toast.LENGTH_SHORT).show();
				if ("1".equals(code)) {// 成功
					SysVar.getInstance(getApplicationContext()).clearUserInfo();
					// JSONObject rsObj=obj.getJSONObject("rs");
					// SysVar.getInstance(this).saveUserInfo(rsObj);
					Intent i = new Intent();
					i.putExtra("name", in_code.getText().toString());
					i.putExtra("pwd", in_pwd.getText().toString());
					setResult(RESULT_OK, i);
					finish();
				} else {// caowu

				}
				/**
				 * else if ("2".equals(code)) { in_phonenum.setText("");
				 * in_code.setText(""); } else { isClickable = true;
				 * yanzheng_code.setEnabled(true);
				 * yanzheng_code.setText("获取验证码");
				 * 
				 * }
				 **/
			} else if (!TextUtils.isEmpty(serial) && CHECK_CODE.equals(serial)) {// 校验验证码
				if ("1".equals(code)) {// 成功
					JSONObject rsObj = obj.getJSONObject("rs");
					SysVar.getInstance(this).saveUserInfo(rsObj);
					// Map<String, Object> userInfo = SysVar.getInstance(this)
					// .getUserInfo();
					// String userName = (String) userInfo.get("userName");
					String userName = rsObj.getString("userName");
					sure(userName);
				} else {
					Toast.makeText(ForgetPassWordAitivity.this,
							obj.getString("msg"), Toast.LENGTH_SHORT).show();
					isRun = false;
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

	@Override
	public void initView() {
		titlebar_text.setText("忘记密码");
		yanzheng_code.setText("获取验证码");
		isClickable = true;
		xiayibu.setText("确定");
		in_pwd.setVisibility(View.GONE);
		in_pwd_1.setVisibility(View.GONE);
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
			if (in_pwd.getVisibility() == View.GONE)// 校验验证码
			{
				Bundle b = new Bundle();
				b.putString("phone", in_phonenum.getText().toString());
				b.putString("randomNum", in_code.getText().toString());
				b.putString("serial", CHECK_CODE);
				reqData("/data/getRandomComPhoneForPwd.json", b);
			} else {// 提交密码
				commit();
			}

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
			Bundle b = new Bundle();
			b.putString("phone", in_phonenum.getText().toString());
			b.putString("serial", GET_CODE);
			reqData("/data/getRandomForPwd.json", b);
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

	private void sure(String name) {
		in_phonenum.setVisibility(View.GONE);
		yanzheng_code.setVisibility(View.GONE);
		in_pwd.setVisibility(View.VISIBLE);
		in_pwd_1.setVisibility(View.VISIBLE);
		in_code.setHint("请输入用户名");
		in_code.setText(name);
		setEditable(in_code, false);
	}

	private void commit() {
		String str = in_pwd.getText().toString();
		String str1 = in_pwd_1.getText().toString();
		if (TextUtils.isEmpty(in_code.getText())) {
			ToastTool.showText(this, "用户名不能为空！");
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
		Map<String, Object> userInfo = SysVar.getInstance(this).getUserInfo();
		if (null != userInfo) {
			String userId = (String) userInfo.get("userId");
			String userName = (String) userInfo.get("userName");
			Bundle param = new Bundle();
			param.putString("userPwd", str);
			param.putString("serial", COMMINT);
			param.putString("phone", in_phonenum.getText().toString());
			param.putString("userId", userId);
			param.putString("userName", userName);
			reqData("/data/updateUserPwd.json", param);
		}

	}

	@Override
	public int setLayout() {
		return R.layout.forget_password;
	}

	private void setEditable(EditText et, boolean value) {
		if (value) {
			et.setFilters(new InputFilter[] { new InputFilter() {
				@Override
				public CharSequence filter(CharSequence source, int start,
						int end, Spanned dest, int dstart, int dend) {
					return null;
				}
			} });
			et.setCursorVisible(true);
			et.setTextColor(Color.BLACK);

		} else {
			et.setFilters(new InputFilter[] { new InputFilter() {
				@Override
				public CharSequence filter(CharSequence source, int start,
						int end, Spanned dest, int dstart, int dend) {
					return source.length() < 1 ? dest.subSequence(dstart, dend)
							: "";
				}

			} });
			et.setCursorVisible(false);
			et.setTextColor(Color.GRAY);

		}
	}
}
