package com.llh.activity;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.android.volley.VolleyError;
import com.ipcamer.demo.R;
import com.llh.base.BaseNetActivity;
import com.llh.utils.CommonUtils;
import com.llh.utils.MatchTool;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

public class EnrollActivity extends BaseNetActivity{

	@ViewInject(id=R.id.userName)
	private EditText userName;
	@ViewInject(id=R.id.userPwd)
	private EditText userPwd;
	@ViewInject(id=R.id.phone)
	private EditText phone;
//	@ViewInject(id=R.id.cameraCode)
//	private EditText cameraCode;
	@ViewInject(id=R.id.real_Name)
	private EditText real_Name;
	@ViewInject(id=R.id.gender)
	private RadioGroup gender;
	@ViewInject(id=R.id.nursingType)
	private RadioGroup nursingType;
	@ViewInject(id=R.id.btn_enroll_login,click="onClick")
	private Button btn_login;
	private String userNameStr,userPwdStr,real_NameStr,phoneStr,cameraCodeStr;
	private int nursingType_id = 0;
	private int gender_id = 2;
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.btn_enroll_login:
			LogTool.i("注册");
			userNameStr = CommonUtils.getContentFromEditText(userName);
			userPwdStr = CommonUtils.getContentFromEditText(userPwd);
			real_NameStr = CommonUtils.getContentFromEditText(real_Name);
			phoneStr = CommonUtils.getContentFromEditText(phone);
//			cameraCodeStr = CommonUtils.getContentFromEditText(cameraCode);
			if(gender.getCheckedRadioButtonId()==R.id.male){
				gender_id = 0;
			}else {
				gender_id = 1;
			}
			if(nursingType.getCheckedRadioButtonId()==R.id.user_org){
				nursingType_id = 1;
			}else {
				nursingType_id = 2;
			}
			if (userNameStr.equals("")) {
				ToastTool.showText(EnrollActivity.this,  "请输入用户名");
				return;
			}
			if (userPwdStr.equals("")) {
				ToastTool.showText(EnrollActivity.this,  "请设置密码");
				return;
			}
			if(phoneStr.equals("")){
				ToastTool.showText(EnrollActivity.this, "请输入手机号");
				return;
			}
//			if(cameraCodeStr.equals("")){
//				ToastTool.showText(EnrollActivity.this,"请输入摄像头编码");
//				return;
//			}

			if (!MatchTool.match(MatchTool.PWD, userPwdStr)) {
				ToastTool.showText(EnrollActivity.this,"请输入6-16位只包含汉字、字母、数字和特殊字符的密码");
				return;
			}
			Enroll();
			break;
		}
	}
	private void Enroll() {
		Bundle param = new Bundle();
		param.putString("userName", userNameStr);
		param.putString("userPwd", userPwdStr);
		param.putString("realName", real_NameStr);
		param.putString("phone", phoneStr);
		param.putInt("nursingType", nursingType_id);
		param.putInt("gender",gender_id);
		reqData("/data/register.json", param);
		LogTool.i("gender_id = "+gender_id);
		LogTool.i("nursingType_id = "+nursingType_id);
	}
	@Override
	public void initView() {
		
	}

	@Override
	public int setLayout() {
		return R.layout.enroll_activity;
	}

	@Override
	protected void parserData(JSONObject response) {
		LogTool.i(response.toString());
		ToastTool.showText(this, "注册成功");
	}

	@Override
	protected void dataError(VolleyError error) {
		LogTool.e(error.toString());
	}

}
