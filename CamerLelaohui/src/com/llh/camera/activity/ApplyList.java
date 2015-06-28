package com.llh.camera.activity;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ipcamer.demo.R;
import com.ipcamer.demo.SystemValue;
import com.llh.base.BaseNetActivity;
import com.llh.net.SysVar;
import com.llh.utils.Constant.RESPONSE_CODE;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

public class ApplyList extends BaseNetActivity {
	@Override
	protected void parserData(JSONObject response) {
		LogTool.i(response.toString());
		try {
			JSONObject obj = response.getJSONObject("result");
			String code = obj.getString("code");
			if (RESPONSE_CODE.SUCCESS_CODE.equals(code)) {
				String serial = obj.getString("serial");
				if (serial.equals("updateBindCameraStatus")) {
					ToastTool.showText(ApplyList.this, obj.getString("msg"));
					finish();
				}
				LogTool.i("===============================");
				JSONArray RSs = obj.getJSONArray("rs");
				for (int i = 0; i < RSs.length(); i++) {
					JSONObject rs = RSs.getJSONObject(i);
					Bundle param = new Bundle();
					String applyUserId = rs.getString("applyUserId");
					LogTool.i(applyUserId);
					String businessCode = rs.getString("businessCode");
					LogTool.i(businessCode);
					String applyUserName = rs.getString("applyUserName");
					LogTool.i(applyUserName);
					String applyTime = rs.getString("applyTime");
					LogTool.i(applyTime);
					String phone = rs.getString("phone");
					LogTool.i(phone);
					String cameraTime = rs.getString("cameraTime");
					LogTool.i(cameraTime);
					String bindCameraStatus = rs.getString("bindCameraStatus");
					String remark = rs.getString("remark");
					LogTool.i(bindCameraStatus);
					String id = rs.getString("id");
					param.putString("id", id);
					param.putString("businessCode", businessCode);
					param.putString("applyUserName", applyUserName);
					param.putString("applyTime", applyTime);
					param.putString("phone", phone);
					param.putString("cameraTime", cameraTime);
					param.putString("remark", remark);
					param.putString("bindCameraStatus", bindCameraStatus);
					param.putString("applyUserId", applyUserId);
					if (userId.equals(applyUserId)) {// 管理员自己
						this.adminData = param;
						continue;
					}
					if (!TextUtils.isEmpty(bindCameraStatus)
							&& "1".equals(bindCameraStatus)) {
						applyFinishList.add(param);
					} else {
						notapplyList.add(param);
					}

				}
				setData();
			} else {
				ToastTool.showText(ApplyList.this, obj.getString("msg"));
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

	private String userId = "";
	// private ArrayList<Bundle> array;
	private ArrayList<Bundle> applyFinishList;
	private ArrayList<Bundle> notapplyList;
	private Bundle adminData;
	@ViewInject(id = R.id.name_text, click = "onClick")
	private TextView adminNameTextView;

	// ListView apply_list;
	@Override
	public void initView() {
		applyFinishList = new ArrayList<Bundle>();
		notapplyList = new ArrayList<Bundle>();
		Map<String, Object> map = SysVar.getInstance(this).getUserInfo();
		userId = (String) map.get("userId");
		Bundle param = new Bundle();
		param.putString("businessCode", SystemValue.deviceId);
		reqData("/data/applyBindCameraList.json", param);
		// apply_list = (ListView)findViewById(R.id.apply_list);

		// apply_list.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// Intent intent = new Intent(ApplyList.this, BindUserActivity.class);
		// intent.putExtras(array.get(position));
		// startActivity(intent);
		// finish();
		//
		// }
		// });

	}

	private void setData() {
		apply_finish_list.removeAllViews();
		apply_notfinish_list.removeAllViews();
		if (null != adminData) {
			adminNameTextView.setText(adminData.getString("applyUserName"));
		}
		for (int i = -1; i < applyFinishList.size(); i++) {
			View convertView = LayoutInflater.from(this).inflate(
					R.layout.apply_item, null);
			TextView name = (TextView) convertView.findViewById(R.id.name_text);
			if (i == -1) {
				name.setText("审核通过用户");
			} else {
				final Bundle bundle = applyFinishList.get(i);
				if (null == bundle)
					continue;
				name.setText(bundle.getString("applyUserName"));
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(ApplyList.this,
								BindUserActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
						finish();

					}
				});
			}

			apply_finish_list.addView(convertView);
		}
		for (int i = -1; i < notapplyList.size(); i++) {
			View convertView = LayoutInflater.from(this).inflate(
					R.layout.apply_item, null);
			TextView name = (TextView) convertView.findViewById(R.id.name_text);
			if (i == -1) {
				name.setText("待审核用户");
			} else {
				final Bundle bundle = notapplyList.get(i);
				if (null == bundle)
					continue;
				name.setText(bundle.getString("applyUserName"));
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(ApplyList.this,
								BindUserActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
						finish();
					}
				});
			}

			apply_notfinish_list.addView(convertView);
		}
	}

	@ViewInject(id = R.id.left_btn, click = "onClick")
	private ImageButton left_btn;
	@ViewInject(id = R.id.apply_finish_list)
	private LinearLayout apply_finish_list;
	@ViewInject(id = R.id.apply_notfinish_list)
	private LinearLayout apply_notfinish_list;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_btn:
			finish();
			break;
		case R.id.name_text:
			if(null!=adminData)
			{
			 Intent intent = new Intent(ApplyList.this, BindUserActivity.class);
			 intent.putExtras(adminData);
			 startActivity(intent);
			 finish();
			}
			break;
		}
	}

	@Override
	public int setLayout() {
		return R.layout.applybindcameralist;
	}

	// class MyAdapter extends BaseAdapter {
	// private LayoutInflater mInflater;
	//
	// // 得到一个LayoutInfalter对象用来导入布局 /*构造函数*/
	// public MyAdapter(Context context) {
	// this.mInflater = LayoutInflater.from(context);
	// }
	//
	// @Override
	// public int getCount() {
	// // TODO Auto-generated method stub
	// return array.size();
	// }
	//
	// @Override
	// public Object getItem(int position) {
	// // TODO Auto-generated method stub
	// return array.get(position);
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// // TODO Auto-generated method stub
	// return position;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// ViewHolder holder;
	// if (convertView == null) {
	// convertView = mInflater.inflate(R.layout.apply_item, null);
	// holder = new ViewHolder();
	// holder.tv = (TextView) convertView.findViewById(R.id.name_text);
	// convertView.setTag(holder);
	// } else {
	// holder = (ViewHolder) convertView.getTag();
	// }
	// Bundle param = array.get(position);
	// String applyUserName = param.getString("applyUserName");
	// LogTool.i("============" + applyUserName);
	// holder.tv.setText(applyUserName);
	// return convertView;
	// }
	//
	// public class ViewHolder {
	// TextView tv;
	// }
	//
	// }

}
