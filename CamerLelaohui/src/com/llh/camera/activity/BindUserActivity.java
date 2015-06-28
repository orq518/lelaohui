package com.llh.camera.activity;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.ipcamer.demo.R;
import com.ipcamer.demo.SystemValue;
import com.llh.base.BaseNetActivity;
import com.llh.net.SysVar;
import com.llh.utils.Constant.RESPONSE_CODE;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

public class BindUserActivity extends BaseNetActivity {
	@ViewInject(id = R.id.left_btn, click = "onClick")
	private ImageButton left_btn;
	@ViewInject(id = R.id.shebei_id)
	private EditText shebei_id;
	@ViewInject(id = R.id.apply_Name)
	private EditText apply_Name;
	@ViewInject(id = R.id.apply_time)
	private EditText apply_time;
	@ViewInject(id = R.id.apply_phone)
	private EditText apply_phone;
	@ViewInject(id = R.id.apply_pass, click = "onClick")
	private Button apply_pass;
	@ViewInject(id = R.id.apply_failure, click = "onClick")
	private Button apply_failure;
	private String id;
	@ViewInject(id = R.id.select_start_time)
	private Spinner startSpinner;
	@ViewInject(id = R.id.select_end_time)
	private Spinner endSpinner;
	@ViewInject(id = R.id.apply_remark)
	private EditText remarkMsg;
	@ViewInject(id = R.id.remak_layout)
	private LinearLayout remak_layout;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_btn:
			Intent intent = new Intent(BindUserActivity.this, ApplyList.class);
			startActivity(intent);
			overridePendingTransition(R.anim.out_to_right, R.anim.in_from_left);
			finish();
			break;

		case R.id.apply_pass:
			if (!isTimeOK()) {
				ToastTool.showText(BindUserActivity.this, "探望结束时间必须大于开始时间");
				return;
			}
			String time = startSpinner.getSelectedItem().toString() + "-"
					+ endSpinner.getSelectedItem().toString();
			Bundle param = new Bundle();
			param.putString("id", id);
			param.putString("cameraBindStatus", "1");
			param.putString("businessCode", shebei_id.getText().toString());
			param.putString("serial", "updateBindCameraStatus");
			param.putString("cameraTime", time);
			reqData("/data/updateBindCameraStatus.json", param);
			break;

		case R.id.apply_failure:
			if (!isTimeOK()) {
				ToastTool.showText(BindUserActivity.this, "探望结束时间必须大于开始时间");
				return;
			}
			String time1 = startSpinner.getSelectedItem().toString() + "-"
					+ endSpinner.getSelectedItem().toString();
			Bundle params = new Bundle();
			params.putString("id", id);
			params.putString("cameraBindStatus", "2");
			params.putString("businessCode", shebei_id.getText().toString());
			params.putString("serial", "updateBindCameraStatus");
			params.putString("cameraTime", time1);
			reqData("/data/updateBindCameraStatus.json", params);
			break;
		}
	}
    public boolean isTimeOK(){
    	return (endSelection > startSelection)||((endSelection <= startSelection)&&endSelection==0);
    }
	@Override
	protected void parserData(JSONObject response) {
		// // TODO Auto-generated method stub
		LogTool.i(response.toString());
		try {
			JSONObject obj = response.getJSONObject("result");
			String code = obj.getString("code");
			if (RESPONSE_CODE.SUCCESS_CODE.equals(code) || "1".equals(code)) {
				String serial = obj.getString("serial");
				if (serial.equals("updateBindCameraStatus")) {
					ToastTool.showText(BindUserActivity.this,
							obj.getString("msg"));
					Intent intent = new Intent(BindUserActivity.this,
							ApplyList.class);
					startActivity(intent);
					finish();
				}
			} else {
				ToastTool.showText(BindUserActivity.this, obj.getString("msg"));
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void dataError(VolleyError error) {
		LogTool.e(error);

	}
	String startLocal[],endLocal[];
	@Override
	public void initView() {
		Bundle pasem = getIntent().getExtras();
		LogTool.i(pasem.toString());

		String businessCode = pasem.getString("businessCode");
		LogTool.i("businessCode = " + businessCode);
		String applyUserName = pasem.getString("applyUserName");
		LogTool.i("applyUserName = " + applyUserName);
		String cameraTime = pasem.getString("cameraTime");
		if (!TextUtils.isEmpty(cameraTime) && cameraTime.indexOf("-") != -1) {
			String start = cameraTime.split("-")[0];
			String end = cameraTime.split("-")[1];
			 startLocal = this.getResources().getStringArray(
					R.array.start);
			 endLocal = this.getResources().getStringArray(
					R.array.start);
			int startSelection = 0;
			int endSelection = 0;
			for (int i = 0; i < startLocal.length; i++) {
				if (start.equals(startLocal[i])) {
					startSelection = i;
					break;
				}
			}
			this.startSelection = startSelection;
			startSpinner.setSelection(startSelection);
			for (int i = 0; i < endLocal.length; i++) {
				if (end.equals(endLocal[i])) {
					endSelection = i;
					break;
				}
			}
			this.endSelection = endSelection;
			endSpinner.setSelection(endSelection);
		}
	
		String remark = pasem.getString("remark");
		if (TextUtils.isEmpty(remark)) {
			remak_layout.setVisibility(View.GONE);
		} else {
			remak_layout.setVisibility(View.VISIBLE);
		}
		remarkMsg.setText(remark);
		shebei_id.setText(pasem.getString("businessCode"));
		apply_Name.setText(pasem.getString("applyUserName"));
		apply_time.setText(pasem.getString("applyTime"));
		apply_phone.setText(pasem.getString("phone"));
		String StbindCameraStatus = pasem.getString("bindCameraStatus");
		id = pasem.getString("id");
		String applyUserId = pasem.getString("applyUserId");
		Map<String, Object> map = SysVar.getInstance(this).getUserInfo();
		String userId = (String) map.get("userId");
		LogTool.i("?????****=" + applyUserId);
		LogTool.i("?????=" + userId);
		if (userId.equals(applyUserId)) {
			apply_pass.setVisibility(View.GONE);
			apply_failure.setVisibility(View.GONE);
			return;
		}
		if (StbindCameraStatus.equals("1")) {
			apply_pass.setVisibility(View.GONE);
			// shebei_id.setKeyListener(null);
			// apply_Name.setKeyListener(null);
			setEditable(shebei_id, false);
			setEditable(apply_Name, false);
			setEditable(apply_time, false);
			setEditable(apply_phone, false);
			setEditable(remarkMsg, false);
			startSpinner.setEnabled(false);
			endSpinner.setEnabled(false);
		} else {
			apply_failure.setVisibility(View.GONE);
			apply_pass.setVisibility(View.VISIBLE);
			setEditable(shebei_id, false);
			setEditable(apply_Name, false);
			setEditable(apply_time, false);
			setEditable(apply_phone, false);
			setEditable(remarkMsg, false);
			startSpinner.setEnabled(true);
			endSpinner.setEnabled(true);
		}
		//
		sipinnerListener();
	}

	private int startSelection, endSelection;

	private void sipinnerListener() {
		startSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				android.util.Log.d("xcq", "startSelection2: " + startSelection);
				android.util.Log.d("xcq", "endSelection2: " + endSelection);
				startSelection = arg2;
				if (!isTimeOK()) {
					ToastTool.showText(BindUserActivity.this, "探望结束时间必须大于开始时间");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		endSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				android.util.Log.d("xcq", "startSelection: " + startSelection);
				android.util.Log.d("xcq", "endSelection: " + endSelection);
				endSelection = arg2;
				if (!isTimeOK()) {
					ToastTool.showText(BindUserActivity.this, "探望结束时间必须大于开始时间");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(BindUserActivity.this, ApplyList.class);
			startActivity(intent);
			overridePendingTransition(R.anim.out_to_right, R.anim.in_from_left);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.tanwang_manage;
	}

}
