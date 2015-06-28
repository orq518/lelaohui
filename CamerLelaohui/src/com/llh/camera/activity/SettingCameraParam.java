package com.llh.camera.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vstc2.nativecaller.NativeCaller;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ipcamer.demo.BridgeService;
import com.ipcamer.demo.BridgeService.AddCameraInterface;
import com.ipcamer.demo.ContentCommon;
import com.ipcamer.demo.R;
import com.ipcamer.demo.SearchListAdapter;
import com.llh.base.BaseNetActivity;
import com.llh.net.SysVar;
import com.llh.utils.CommonUtils;
import com.llh.utils.SharedPreferenceUtil;
import com.llh.utils.Constant.RESPONSE_CODE;
import com.llh.utils.SysValue;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

/**
 * 设置摄像头参数
 * 
 * @author Administrator
 * 
 */
public class SettingCameraParam extends BaseNetActivity {
	@ViewInject(id = R.id.search_did, click = "onClick")
	private Button btn_search;
	@ViewInject(id = R.id.btn_sure, click = "onClick")
	private Button btn_sure;
	@ViewInject(id = R.id.camera_id)
	private EditText didEdit;
	private boolean isSearched;
	private SearchListAdapter listAdapter = null;
	private ProgressDialog progressdlg = null;
	private static final int SEARCH_TIME = 3000;
	@ViewInject(id = R.id.visit_time)
	private Spinner visit_time;
	@ViewInject(id = R.id.visit_starttime)
	private LinearLayout startTimeLayout;
	@ViewInject(id = R.id.visit_endtime)
	private LinearLayout endTimeLayout;

	@Override
	public int setLayout() {
		return R.layout.setting_camera_param_layout;
	}

	String str_DID;
	String str_visit_time;

	@Override
	public void initView() {
		// Map<String, Object> map= SysVar.getInstance(this).getUserInfo();
		Bundle param = new Bundle();
		// param.putString("userId",(String)map.get("userId"));
		param.putString("serial", "getCameraListByUserId");
		reqData("/data/getCameraListByUserId.json", param);
		str_DID = SharedPreferenceUtil
				.getUserInfo(SysValue.KEY_CAMERA_ID, this);
		didEdit.setText(str_DID);
		visit_time.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				str_visit_time = getResources().getStringArray(R.array.start)[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		progressdlg = new ProgressDialog(this);
		progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdlg.setMessage(getString(R.string.searching_tip));
		listAdapter = new SearchListAdapter(this);
		BridgeService.setAddCameraInterface(new AddCameraInterface() {

			@Override
			public void callBackSearchResultData(int cameraType, String strMac,
					String strName, String strDeviceID, String strIpAddr,
					int port) {
				if (!listAdapter.AddCamera(strMac, strName, strDeviceID)) {
					return;
				}
			}
		});
		// String str_DID = SharedPreferenceUtil
		// .getUserInfo(SysValue.KEY_CAMERA_ID, this);
		// if(SysVar.getInstance(getApplicationContext()).getUserIsCameraAdministrator(str_DID))
		// {
		// startTimeLayout.setVisibility(View.VISIBLE);
		// endTimeLayout.setVisibility(View.VISIBLE);
		// }else{
		// startTimeLayout.setVisibility(View.GONE);
		// endTimeLayout.setVisibility(View.GONE);
		// }
		startTimeLayout.setVisibility(View.GONE);
		endTimeLayout.setVisibility(View.GONE);
	}

	@ViewInject(id = R.id.left_btn, click = "onClick")
	private ImageButton left_btn;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_did:// 搜索摄像头编码
			searchCamera();
			break;

		case R.id.btn_sure:// 保存设置
			// saveCameraParam();
			justCameraIsNullCamera();
			break;
		case R.id.left_btn:
			finish();
			break;
		}
	}

	private void justCameraIsNullCamera() {
		// http://111.204.236.15:8081/data/bindCameraByBusCode.json?businessCode=VSTC535408WXUKY
		String cameraId = CommonUtils.getContentFromEditText(didEdit);
		Bundle param = new Bundle();
		param.putString("businessCode", cameraId);
		param.putString("serial", "justCameraIsNullCamera");
		reqData("/data/bindCameraByBusCode.json", param);

	}

	private void saveCameraParam(String remark) {
		str_DID = CommonUtils.getContentFromEditText(didEdit);
		if (TextUtils.isEmpty(str_DID)/* ||TextUtils.isEmpty(str_visit_time) */) {
			ToastTool.showText(this, "请检查设置参数");
			return;
		}
		SharedPreferenceUtil.saveUserInfo(SysValue.KEY_CAMERA_ID, str_DID,
				getApplicationContext());
		SharedPreferenceUtil.saveUserInfo(SysValue.KEY_CAMERA_USER, "admin",
				getApplicationContext());
		SharedPreferenceUtil.saveUserInfo(SysValue.KEY_CAMERA_PWD, "888888",
				getApplicationContext());
		Bundle param = new Bundle();
		param.putString("businessCode", str_DID);
		param.putString("cameraTime", str_visit_time);
		if (!TextUtils.isEmpty(remark)) {
			param.putString("remark", URLEncoder.encode(remark));
		}
		reqData("/data/bindCamera.json", param);
		// ToastTool.showText(this, "设置成功");
		// finish();
	}

	private void startSearch() {
		listAdapter.ClearAll();
		progressdlg.setMessage(getString(R.string.searching_tip));
		progressdlg.show();
		new Thread(new SearchThread()).start();
		updateListHandler.postDelayed(updateThread, SEARCH_TIME);
	}

	Runnable updateThread = new Runnable() {
		public void run() {
			NativeCaller.StopSearch();
			progressdlg.dismiss();
			Message msg = updateListHandler.obtainMessage();
			msg.what = 1;
			updateListHandler.sendMessage(msg);
		}
	};
	// 15576341699
	Handler updateListHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				listAdapter.notifyDataSetChanged();
				if (listAdapter.getCount() > 0) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(
							SettingCameraParam.this);
					dialog.setTitle(getResources().getString(
							R.string.add_search_result));
					dialog.setPositiveButton(
							getResources().getString(R.string.refresh),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									startSearch();
								}
							});
					dialog.setNegativeButton(
							getResources().getString(R.string.str_cancel), null);
					dialog.setAdapter(listAdapter,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int arg2) {
									Map<String, Object> mapItem = (Map<String, Object>) listAdapter
											.getItemContent(arg2);
									if (mapItem == null) {
										return;
									}
									String strDID = (String) mapItem
											.get(ContentCommon.STR_CAMERA_ID);
									String strUser = ContentCommon.DEFAULT_USER_NAME;
									String strPwd = ContentCommon.DEFAULT_USER_PWD;
									// userEdit.setText(strUser);
									// pwdEdit.setText(strPwd);
									didEdit.setText(strDID);

								}
							});

					dialog.show();
				} else {
					Toast.makeText(SettingCameraParam.this,
							getResources().getString(R.string.add_search_no),
							Toast.LENGTH_LONG).show();
					isSearched = false;//
				}
			}
		}
	};

	private class SearchThread implements Runnable {
		@Override
		public void run() {
			Log.d("tag", "startSearch");
			NativeCaller.StartSearch();
		}
	}

	private void searchCamera() {
		if (!isSearched) {
			isSearched = true;
			startSearch();
		} else {
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					SettingCameraParam.this);
			dialog.setTitle(getResources()
					.getString(R.string.add_search_result));
			dialog.setPositiveButton(
					getResources().getString(R.string.refresh),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							startSearch();

						}
					});
			dialog.setNegativeButton(
					getResources().getString(R.string.str_cancel), null);
			dialog.setAdapter(listAdapter,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int arg2) {
							Map<String, Object> mapItem = (Map<String, Object>) listAdapter
									.getItemContent(arg2);
							if (mapItem == null) {
								return;
							}
							String strDID = (String) mapItem
									.get(ContentCommon.STR_CAMERA_ID);
							String strUser = ContentCommon.DEFAULT_USER_NAME;
							String strPwd = ContentCommon.DEFAULT_USER_PWD;
							// userEdit.setText(strUser);
							// pwdEdit.setText(strPwd);
							didEdit.setText(strDID);

						}
					});
			dialog.show();
		}
	}

	@Override
	protected void parserData(JSONObject response) {
		LogTool.i("绑定摄像头应答：" + response.toString());
		try {
			JSONObject obj = response.getJSONObject("result");
			String code = obj.getString("code");
			String status = obj.getString("rs");
			String serial = obj.getString("serial");
			android.util.Log.d("xcq", "status: " + status);
			if (!TextUtils.isEmpty(serial)
					&& serial.equals("justCameraIsNullCamera")) {
				if ("3".equals(code)) {// 是空摄像头
					saveCameraParam("");
				} else if ("2".equals(code)) {// 不是空摄像头
					showMsgDialog();
				} else {
					ToastTool.showText(SettingCameraParam.this,
							obj.getString("msg"));
				}
			} else {
				if (RESPONSE_CODE.SUCCESS_CODE.equals(code)) {
					if (serial.equals("getCameraListByUserId")) {
						JSONArray objArray = obj.getJSONArray("rs");
						int len = objArray.length();
						if (len != 0) {
							String[] str = new String[len];
							for (int i = 0; i < len; i++) {
								JSONObject o = objArray.getJSONObject(i);
								str[i] = o.getString("cameraCode");
								LogTool.i("cameraCode = " + str[i]);
							}
							didEdit.setText(str[0]);
						}

					} else {
						ToastTool.showText(this, "参数设置成功");
						if ("0".equals(status) || "1".equals(status)) {
							SysVar.getInstance(getApplicationContext())
									.setUserCameraAdministrator(status);
							SysVar.getInstance(getApplicationContext())
									.setUserIsNull(false);
							Intent intent = new Intent(SettingCameraParam.this,
									VisitSettingActivity.class);
							startActivity(intent);
							finish();
						}
					}
				} else {
					// ToastTool.showText(SettingCameraParam.this,
					// obj.getString("msg"));
					Toast.makeText(getApplicationContext(),
							obj.getString("msg"), Toast.LENGTH_SHORT).show();
					if ("4".equals(code) || "3".equals(code))// 绑定满了
					{
						if ("1".equals(status)) {
							SysVar.getInstance(getApplicationContext())
									.setUserCameraAdministrator(status);
							Intent intent = new Intent(SettingCameraParam.this,
									VisitSettingActivity.class);
							startActivity(intent);
							finish();
						} else if ("0".equals(status)) {
							SysVar.getInstance(getApplicationContext())
									.setUserIsNotCameraAdministrator(status);
							Intent intent = new Intent(SettingCameraParam.this,
									VisitSettingActivity.class);
							startActivity(intent);
							finish();
						}
						SysVar.getInstance(getApplicationContext())
								.setUserIsNull(false);
					} else {

					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void showMsgDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SettingCameraParam.this);
		LayoutInflater factory = LayoutInflater.from(this);
		// final View textEntryView = factory.inflate(R.layout.test, null);
		final EditText et = new EditText(SettingCameraParam.this);
		et.setText("我是");
		builder.setTitle("绑定申请");
		builder.setView(et);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				saveCameraParam(et.getText().toString());
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});
		builder.create().show();
	}

	@Override
	protected void dataError(VolleyError error) {
		LogTool.e(error);
	}
}
