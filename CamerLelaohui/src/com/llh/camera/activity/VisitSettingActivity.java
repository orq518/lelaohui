package com.llh.camera.activity;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vstc2.nativecaller.NativeCaller;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ipcamer.demo.BridgeService;
import com.ipcamer.demo.R;
import com.ipcamer.demo.SystemValue;
import com.llh.activity.JFActivity;
import com.llh.base.BaseNetActivity;
import com.llh.camera.activity.ApplyBind.ListViewAdapter;
import com.llh.camera.activity.VisitMainActivity.StartPPPPThread;
import com.llh.net.SysVar;
import com.llh.utils.SharedPreferenceUtil;
import com.llh.utils.SysValue;
import com.llh.utils.Constant.RESPONSE_CODE;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

public class VisitSettingActivity extends BaseNetActivity {

	private final static String QUERYREMAINDERDAYS = "queryRemainderDays";

	@Override
	protected void parserData(JSONObject response) {
		try {
			JSONObject result = response.getJSONObject("result");
			String code = result.getString("code");
			String msg = result.getString("msg");
			if(code.equals("2")){
				JSONObject rs = result.getJSONObject("rs");
				SystemValue.deviceId= rs.getString("businessCode");
				int remainingDays = rs.getInt("remainingDays");
				showMsgDialog(remainingDays);
			}else {
				ToastTool.showText(this, msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void dataError(VolleyError error) {
		// TODO Auto-generated method stub

	}

	@ViewInject(id = R.id.titlebar_text)
	private TextView titlebar_text;
	@ViewInject(id = R.id.my_name)
	private TextView my_name;
	@ViewInject(id = R.id.left_btn, click = "onClick")
	private ImageButton left_btn;

	@Override
	public void initView() {
		titlebar_text.setText("探望设置");
		Map<String, Object> map = SysVar.getInstance(this).getUserInfo();
		String realName = String.valueOf(map.get("realName"));
		String userName = String.valueOf(map.get("userName"));
		LogTool.i("realName = " + realName);
		LogTool.i("userName = " + userName);
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
		String str_DID = SharedPreferenceUtil.getUserInfo(
				SysValue.KEY_CAMERA_ID, this);
		view_Binding.setVisibility(View.GONE);
		appyl_bind_dis.setVisibility(View.GONE);
		if (SysVar.getInstance(getApplicationContext())
				.getUserIsCameraAdministrator(str_DID)) {
			apply_bind.setVisibility(View.GONE);
			bind_user_dis.setVisibility(View.GONE);

			manager_users_dis.setVisibility(View.VISIBLE);
			bind_user.setVisibility(View.VISIBLE);

			service_fee.setVisibility(View.VISIBLE);
			service_fee_dis.setVisibility(View.VISIBLE);

			bind_user_query.setVisibility(View.GONE);
			bind_user_query_dis.setVisibility(View.GONE);

			remainder_days_query.setVisibility(View.VISIBLE);
			remainder_days_query_dis.setVisibility(View.VISIBLE);
		} else {
			apply_bind.setVisibility(View.VISIBLE);
			bind_user_dis.setVisibility(View.VISIBLE);
			bind_user.setVisibility(View.GONE);
			manager_users_dis.setVisibility(View.GONE);

			service_fee.setVisibility(View.GONE);
			service_fee_dis.setVisibility(View.GONE);

			bind_user_query.setVisibility(View.VISIBLE);
			bind_user_query_dis.setVisibility(View.VISIBLE);

			remainder_days_query.setVisibility(View.GONE);
			remainder_days_query_dis.setVisibility(View.GONE);
		}

	}

	@ViewInject(id = R.id.view_Binding, click = "onClock")
	private View view_Binding;// 摄像头绑定
	@ViewInject(id = R.id.Change_Password, click = "onClock")
	private View Change_Password;
	@ViewInject(id = R.id.service_fee, click = "onClock")
	private View service_fee;// 服务缴费
	@ViewInject(id = R.id.service_fee_dis)
	private View service_fee_dis;// 服务缴费分割线
	@ViewInject(id = R.id.bind_user, click = "onClock")
	private View bind_user;// 申请用户管理
	@ViewInject(id = R.id.manager_users_dis)
	private View manager_users_dis;// 申请用户管理分割线
	@ViewInject(id = R.id.appyl_bind, click = "onClock")
	private View apply_bind;// 探望申请
	@ViewInject(id = R.id.appyl_bind_dis)
	private View appyl_bind_dis;// 摄像头绑定分割线
	@ViewInject(id = R.id.bind_user_dis)
	private View bind_user_dis;// 探望申请分割线

	@ViewInject(id = R.id.bind_user_query, click = "onClock")
	private LinearLayout bind_user_query;// 绑定状态查询
	@ViewInject(id = R.id.bind_user_query_dis)
	private View bind_user_query_dis;// 绑定状态查询分隔线
	@ViewInject(id = R.id.remainder_days_query, click = "onClock")
	private LinearLayout remainder_days_query;
	@ViewInject(id = R.id.remainder_days_query_dis)
	private View remainder_days_query_dis;

	public void onClock(View v) {
		LogTool.i("view = " + v.getId());
		Intent intent;
		switch (v.getId()) {
		case R.id.view_Binding:// 绑定绑定摄像头
			intent = new Intent(VisitSettingActivity.this,
					SettingCameraParam.class);
			startActivity(intent);
			// finish();
			break;

		case R.id.Change_Password:
			LogTool.i("修改密码");
			intent = new Intent(VisitSettingActivity.this,
					ChangePasswordLoginActivity.class);
			startActivity(intent);
			// finish();
			break;
		case R.id.service_fee:
			intent = new Intent(VisitSettingActivity.this, JFActivity.class);
			startActivity(intent);
			// finish();
			break;
		case R.id.bind_user:
			// intent = new Intent(SettingActivity.this,
			// BindUserActivity.class);
			intent = new Intent(VisitSettingActivity.this, ApplyList.class);
			startActivity(intent);
			// finish();
			break;
		case R.id.appyl_bind:
			intent = new Intent(VisitSettingActivity.this, ApplyBind.class);
			startActivity(intent);
			// finish();
			break;
		case R.id.bind_user_query:
			intent = new Intent(VisitSettingActivity.this, QueryActivity.class);
			startActivity(intent);
			break;
		case R.id.remainder_days_query:
			queryRemainderDays();
			break;
		}
	}

	private void queryRemainderDays() {
		Bundle param = new Bundle();
		param.putString("serial", QUERYREMAINDERDAYS);
		reqData("/data/paymentReminders.json", param);
	}

	private void showMsgDialog(int remainingDays) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				VisitSettingActivity.this);
		final TextView tv = new TextView(VisitSettingActivity.this);
		tv.setText("     剩余"+String.valueOf(remainingDays)+"天");
		tv.setTextSize(16);
		builder.setTitle("剩余天数");
		builder.setView(tv);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		builder.create().show();
	}

	@Override
	public int setLayout() {
		return R.layout.visit_setting;
	}

}
