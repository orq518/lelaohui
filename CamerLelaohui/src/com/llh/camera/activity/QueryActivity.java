package com.llh.camera.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ipcamer.demo.R;
import com.llh.base.BaseNetActivity;
import com.llh.utils.Constant.RESPONSE_CODE;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

public class QueryActivity extends BaseNetActivity {

	@ViewInject(id = R.id.container)
	LinearLayout container;
	private ArrayList<Bundle> queryList;
	@Override
	protected void parserData(JSONObject response) {
		// TODO Auto-generated method stub
		JSONObject obj;
		try {
			obj = response.getJSONObject("result");
			String code = obj.getString("code");
			// String serial = obj.getString("serial");
			String msg = obj.getString("rs");
			if (RESPONSE_CODE.SUCCESS_CODE.equals(code)) {
				JSONArray RSs = obj.getJSONArray("rs");
				for (int i = 0; i < RSs.length(); i++) {
					JSONObject rs = RSs.getJSONObject(i);
					Bundle param = new Bundle();
					String businessCode = rs.getString("businessCode");
					LogTool.i(businessCode);
					String userId = rs.getString("userId");
					LogTool.i(userId);
					String userName = rs.getString("userName");
					LogTool.i(userName);
					String managerId = rs.getString("managerId");
					LogTool.i(managerId);
					String managerName = rs.getString("managerName");
					LogTool.i(managerName);
					String phone = rs.getString("phone");
					LogTool.i(phone);
					String cameraBindStatus = rs.getString("cameraBindStatus");
					LogTool.i(cameraBindStatus);
					String updTime = rs.getString("updTime");
					param.putString("businessCode", businessCode);
					param.putString("userId", userId);
					param.putString("userName", userName);
					param.putString("managerId", managerId);
					param.putString("managerName", managerName);
					param.putString("phone", phone);
					param.putString("cameraBindStatus", cameraBindStatus);
					param.putString("updTime", updTime);
					queryList.add(param);
				}
				setData();
			} else {
				ToastTool.showText(QueryActivity.this, obj.getString("msg"));
				finish();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setData() {
		for (int i = 0; i < queryList.size(); i++) {
			View convertView = LayoutInflater.from(this).inflate(
					R.layout.query_item_layout, null);
			TextView tile = (TextView) convertView.findViewById(R.id.item_tile);
			final ImageView arrow = (ImageView) convertView
					.findViewById(R.id.item_arrow);
			final LinearLayout expandView = (LinearLayout) convertView
					.findViewById(R.id.expandView);
			TextView camreName = (TextView) convertView
					.findViewById(R.id.camreName);
			TextView admin = (TextView) convertView.findViewById(R.id.admin);
			TextView phone = (TextView) convertView.findViewById(R.id.phone);
			TextView status = (TextView) convertView.findViewById(R.id.status);
			TextView time = (TextView) convertView.findViewById(R.id.time);

			Bundle param = queryList.get(i);
			tile.setText("摄像头: "+param.getString("businessCode"));
			camreName.setText(param.getString("businessCode"));
			admin.setText(param.getString("managerName"));
			phone.setText(param.getString("phone"));
			if("0".equals(param.getString("cameraBindStatus")))
			{
				status.setText("申请绑定");
			}else if("1".equals(param.getString("cameraBindStatus"))){
				status.setText("同意绑定");
			}else if("2".equals(param.getString("cameraBindStatus"))){
				status.setText("解除绑定");
			}
			time.setText(param.getString("updTime"));

			expandView.setVisibility(View.GONE);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (expandView.getVisibility() == View.GONE) {
						expandView.setVisibility(View.VISIBLE);
						arrow.setImageResource(R.drawable.arrowdown);
					}else{
						expandView.setVisibility(View.GONE);
						arrow.setImageResource(R.drawable.arrow);
					}

				}
			});
			container.addView(convertView);
		}
	}

	@Override
	protected void dataError(VolleyError error) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		queryList = new ArrayList<Bundle>();
		reqData("/data/getCamerasUserByUserId.json",new Bundle());
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.query_layout;
	}
	@ViewInject(id = R.id.left_btn, click = "onClick")
	private ImageButton left_btn;
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_btn:
			finish();
			break;
		}
	}
}
