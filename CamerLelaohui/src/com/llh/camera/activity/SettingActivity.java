package com.llh.camera.activity;

import java.net.URLEncoder;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ipcamer.demo.R;
import com.llh.base.BaseNetActivity;
import com.llh.net.SysVar;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

public class SettingActivity extends BaseNetActivity {

	@Override
	protected void parserData(JSONObject response) {
	}

	@Override
	protected void dataError(VolleyError error) {

	}

	@ViewInject(id = R.id.titlebar_text)
	private TextView titlebar_text;
	@ViewInject(id = R.id.my_name)
	private TextView my_name;
	@ViewInject(id = R.id.left_btn, click = "onClick")
	private ImageButton left_btn;

	@Override
	public void initView() {
		titlebar_text.setText("设置");
		Map<String, Object> map = SysVar.getInstance(this).getUserInfo();
		String realName = String.valueOf(map.get("realName"));
		String userName = String.valueOf(map.get("userName"));

		LogTool.i("userName = " + userName);
		LogTool.d("@@realName = " + realName);
		if (!realName.equals("null")) {
			my_name.setText(realName);
		} else {
			my_name.setText(userName);
		}
		left_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@ViewInject(id = R.id.Change_Password, click = "onClock")
	private View Change_Password;
	@ViewInject(id = R.id.visit_setting, click = "onClock")
	private View visit_setting;
	@ViewInject(id = R.id.order_setting, click = "onClock")
	private View order_setting;
	@ViewInject(id=R.id.forget_pwd,click="onClock")
	private LinearLayout forget_pwd;

	public void onClock(View v) {
		LogTool.i("view = " + v.getId());
		Intent intent = null;
		switch (v.getId()) {
		case R.id.visit_setting:// 探望设置

			if (SysVar.getInstance(getApplicationContext()).getUserIsNull()) {
				intent = new Intent(SettingActivity.this,
						SettingCameraParam.class);
			} else {
				if (SysVar.getInstance(getApplicationContext())
						.getUseIsBinded()) {
					intent = new Intent(SettingActivity.this,
							VisitSettingActivity.class);
				} else {
					intent = new Intent(SettingActivity.this,
							VisitSettingActivity.class);
				}

			}
			break;
		case R.id.order_setting:
			ToastTool.showText(SettingActivity.this, "功能开发中...");
			break;
		case R.id.Change_Password:
			intent = new Intent(SettingActivity.this,
					ChangePasswordLoginActivity.class);
			break;
		case R.id.forget_pwd:
//			ToastTool.showText(SettingActivity.this, "功能开发中...");
			intent = new Intent(SettingActivity.this, ForgetPassWordAitivity.class);
			break;
		}
		if (null != intent) {
			startActivity(intent);
		}

	}

	@Override
	public int setLayout() {
		return R.layout.new_setting_layout;
	}

}
