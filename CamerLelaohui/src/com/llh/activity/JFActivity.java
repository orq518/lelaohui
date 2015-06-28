package com.llh.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.ipcamer.demo.R;
import com.ipcamer.demo.SystemValue;
import com.llh.base.BaseNetActivity;
import com.llh.utils.Constant.REQ_ACTION;
import com.llh.utils.Constant.RESPONSE_CODE;
import com.llh.utils.SharedPreferenceUtil;
import com.llh.utils.SysValue;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

public class JFActivity extends BaseNetActivity {

	private final String BALANCE = "balance";
	private final String SERVICE_LIST = "servicelist";
	ArrayAdapter<String> adapter;
	List<String> list = new ArrayList<String>();
	List<Double> value = new ArrayList<Double>();
	List<Long> id = new ArrayList<Long>();
	@ViewInject(id = R.id.my_spinner)
	private Spinner my_spinner;
	@ViewInject(id = R.id.my_value)
	private EditText my_value;
	@ViewInject(id = R.id.m_yue)
	private EditText m_yue;
	@ViewInject(id = R.id.gender)
	private RadioGroup gender;
	@ViewInject(id = R.id.btn_jf, click = "onClick")
	private Button btn_jf;
	private int tag = -1;
	JSONObject obj_balance;
	@ViewInject(id = R.id.left_btn,click="onClick")
	private ImageButton left_btn;
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_jf:
//			Bundle paran = new Bundle();
//			try {
//				paran.putString("value", m_yue.getText().toString());
//				paran.putString("propackageId", String.valueOf(id.get(tag)));
//				paran.putString("businessCode",SystemValue.deviceId);
//
//				paran.putString("cardId",obj_balance.get("cardId").toString());
//				paran.putString("my_value", my_value.getText().toString());
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			Intent intent = new Intent(JFActivity.this, LelaoHuiActivity.class);
//			intent.putExtras(paran);
//			startActivity(intent);
//			finish();
			break;

		case R.id.left_btn:
			finish();
			break;
		}
		
	}

	@Override
	protected void parserData(JSONObject response) {
		try {
			JSONObject obj = response.getJSONObject("result");
			String code = obj.getString("code");
			if (RESPONSE_CODE.FAIL_CODE.equals(code)) {
				ToastTool.showText(JFActivity.this,
						obj.getString(obj.getString("msg")));
				return;
			}
			String serial = obj.getString("serial");
			LogTool.d("serial = " + serial);
			if (serial.equals(SERVICE_LIST)) {
				JSONArray rsRrray = obj.getJSONArray("rs");

				for (int i = 0; i < rsRrray.length(); i++) {
					JSONObject rsObj = rsRrray.getJSONObject(i);
					String packageName = String.valueOf(rsObj
							.get("packageName"));
					double packagePrice = Double.valueOf(rsObj.get(
							"packagePrice").toString());
					Long propackageId = Long.valueOf(rsObj.get("propackageId")
							.toString());
					LogTool.d("packageName = " + packageName);
					LogTool.d("packagePrice = " + packagePrice);
					list.add(packageName);
					value.add(packagePrice);
					id.add(propackageId);
				}
				Message message = new Message();
				message.what = 1;

				updateUI.sendMessage(message);
			}
			if (serial.equals(BALANCE)) {
				JSONObject rsO = obj.getJSONObject("rs");
				LogTool.d("cardId"+rsO.get("cardId"));
				obj_balance = rsO;
				Message message = new Message();
				message.what = 2;
				message.obj = rsO.get("cardValue");
				updateUI.sendMessage(message);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	Handler updateUI = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 2) {
				String str = String.valueOf(msg.obj);
				m_yue.setText(str);
			}
			if (msg.what == 1) {
				adapter = new ArrayAdapter<String>(JFActivity.this,
						android.R.layout.simple_spinner_item, list);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				my_spinner.setAdapter(adapter);
				my_spinner
						.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								tag = arg2;
								my_value.setText(String.valueOf(value.get(arg2)));
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
							}
						});
			}

		}
	};

	@Override
	protected void dataError(VolleyError error) {
		// TODO Auto-generated method stub

	}

	@ViewInject(id = R.id.titlebar_text)
	private TextView titlebar_text;

	@Override
	public void initView() {
		titlebar_text.setText("服务缴费");
		Balance();// ���
		ServiceList();
		gender.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == 1) {
					int radioButtonId = group.getCheckedRadioButtonId();
					RadioButton rb = (RadioButton)findViewById(radioButtonId);
					ToastTool.showText(JFActivity.this, rb.getText().toString());
				}
				// //��ȡ������ѡ�����ID
				// int radioButtonId = group.getCheckedRadioButtonId();
				// //����ID��ȡRadioButton��ʵ��
				// RadioButton rb =
				// (RadioButton)JFActivity.this.findViewById(radioButtonId);
				// LogTool.d("checkedId = "+checkedId);
				// LogTool.d("radioButtonId = "+radioButtonId);

			}
		});
	}

	private void ServiceList() {
		Bundle param = new Bundle();
		param.putString("serial", SERVICE_LIST);
		reqData(REQ_ACTION.SERVICE_LIST, param);
	}

	private void Balance() {
		Bundle param = new Bundle();
		param.putString("serial", BALANCE);
		reqData(REQ_ACTION.USER_BALANCE, param);
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.jf_layout;
	}

}
