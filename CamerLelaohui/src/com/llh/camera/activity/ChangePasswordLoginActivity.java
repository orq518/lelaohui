package com.llh.camera.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ipcamer.demo.R;
import com.llh.base.BaseNetActivity;
import com.llh.utils.CommonUtils;
import com.llh.utils.Constant.RESPONSE_CODE;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

/**
 * 登录密码
 * 
 * @author Administrator
 * 
 */
public class ChangePasswordLoginActivity extends BaseNetActivity {

	@Override
	protected void parserData(JSONObject response) {
		LogTool.d(response.toString());
		try {
			JSONObject obj = response.getJSONObject("result");
			String code = obj.getString("code");
			if (RESPONSE_CODE.SUCCESS_CODE.equals(code)) {
				ToastTool.showText(ChangePasswordLoginActivity.this,"修改成功");
				this.finish();
			}else {
				ToastTool.showText(ChangePasswordLoginActivity.this, obj.getString("msg"));
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

	@Override
	public void initView() {
		titlebar_text.setText("修改登录密码");

	}
	@ViewInject(id = R.id.titlebar_text)
	private TextView titlebar_text;
	@ViewInject(id = R.id.l_mima)
	private EditText l_mima;
	@ViewInject(id = R.id.x_mima)
	private EditText x_mima;
	@ViewInject(id = R.id.btn_xiugai, click = "onClick")
	private Button ok;
	@ViewInject(id = R.id.left_btn,click="onClick")
	private ImageButton left_btn;
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_xiugai:
			String l_pwd = CommonUtils.getContentFromEditText(l_mima);
			String x_pwd = CommonUtils.getContentFromEditText(x_mima);
			if (l_pwd.equals("")) {
				ToastTool.showText(this, "请输入原始密码");
			}
			if (x_pwd.equals("")) {
				ToastTool.showText(this, "请输入新密码");
			}
			Bundle param = new Bundle();
			param.putString("userPwdNew", x_pwd);
			param.putString("userPwd", l_pwd);
			reqData("/data/updatePassword.json", param);
			break;
		case R.id.left_btn:
			finish();
			break;
		}
	
		
	}

	@Override
	public int setLayout() {
		return R.layout.change_password_layout;
	}

}
