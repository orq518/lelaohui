package com.llh.camera.activity;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ipcamer.demo.R;
import com.llh.base.BaseNetActivity;
import com.llh.net.SysVar;
import com.llh.utils.CommonUtils;
import com.llh.utils.SharedPreferenceUtil;
import com.llh.utils.SysValue;
import com.llh.utils.Constant.RESPONSE_CODE;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

public class ApplyBind extends BaseNetActivity {

	@Override
	protected void parserData(JSONObject response) {
		LogTool.i(response.toString());
		try {
			JSONObject obj = response.getJSONObject("result");
			String code = obj.getString("code");
			
			if (!RESPONSE_CODE.SUCCESS_CODE.equals(code)) {
				
				ToastTool.showText(ApplyBind.this, obj.getString("msg"));
				return;
			}
			
			String serial = obj.getString("serial");
			if(serial.equals("bindCamera")){
				ToastTool.showText(ApplyBind.this, obj.getString("msg"));
				finish();
			}
			if (serial.equals("ByPhone")) {
				JSONArray objArray = obj.getJSONArray("rs");
				int len = objArray.length();
				ArrayList<String> arr = new ArrayList<String>();
				for (int i = 0; i < len; i++) {
					JSONObject o = objArray.getJSONObject(i);
					String str = o.getString("cameraCode");
					LogTool.i(str);
					arr.add(str);
				}
				ListViewAdapter adapter = new ListViewAdapter(this, arr);
				did_list.setVisibility(View.VISIBLE);
				did_list.setAdapter(adapter);
//				ToastTool.showText(this, "申请成功");
//				finish();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void dataError(VolleyError error) {
		// TODO Auto-generated method stub

	}

	@ViewInject(id = R.id.phone_code)
	EditText phone_code;
	// @ViewInject(id=R.id.manage_name)
	// EditText manage_name;
	@ViewInject(id = R.id.btn_jf_1, click = "onClick")
	Button btn_jf_1;
	@ViewInject(id = R.id.left_btn, click = "onClick")
	ImageButton left_btn;
	@ViewInject(id=R.id.did_list)
	ListView did_list;
	@ViewInject(id=R.id.visit_start_time)
	private LinearLayout startTimeLayout;
	@ViewInject(id=R.id.visit_end_time)
	private LinearLayout endTimeLayout;
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_jf_1:
			if (phone_code.equals("")) {
				ToastTool.showText(this, "请输入管理者手机号");
				return;
			}
			String phone = CommonUtils.getContentFromEditText(phone_code);
			Bundle param = new Bundle();
			param.putString("adminPhone", phone);
			param.putString("serial", "ByPhone");
			reqData("/data/getCameraListByPhone.json", param);
			break;

		case R.id.left_btn:
			finish();
			break;
		}

	}
    private void showMsgDialog(final ArrayList<String> arr,final int position){
    	AlertDialog.Builder builder = new AlertDialog.Builder(ApplyBind.this);   
    	 LayoutInflater factory = LayoutInflater.from(this);  
//    	 final View textEntryView = factory.inflate(R.layout.test, null); 
    	 final EditText et = new EditText(ApplyBind.this);
    	 et.setText("我是");
    	     builder.setTitle("绑定申请");  
    	     builder.setView(et);  
    	     builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
    	         public void onClick(DialogInterface dialog, int whichButton) {  
 					Bundle param = new Bundle();
 					param.putString("businessCode", arr.get(position));
 					param.putString("userType", "2");
 					param.putString("cameraTime", "7:00-19:00");
 					param.putString("serial", "bindCamera");
 					param.putString("remark",URLEncoder.encode(et.getText().toString()));
 					reqData("/data/bindCamera.json", param);
    	         }  
    	     });  
    	     builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {  
    	         public void onClick(DialogInterface dialog, int whichButton) {  
    	 
    	         }  
    	     });  
    	   builder.create().show(); 
    }
	@Override
	public void initView() {
		did_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LogTool.i("========");
				
			}
		});
		String str_DID = SharedPreferenceUtil
				.getUserInfo(SysValue.KEY_CAMERA_ID, this);
		if(SysVar.getInstance(getApplicationContext()).getUserIsCameraAdministrator(str_DID))
		{
			startTimeLayout.setVisibility(View.VISIBLE);
			endTimeLayout.setVisibility(View.VISIBLE);
		}else{
			startTimeLayout.setVisibility(View.GONE);
			endTimeLayout.setVisibility(View.GONE);
		}
	}
	@Override
	public int setLayout() {
		return R.layout.apply_camera_layout;
	}

	class ListViewAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<String> arr;
		public ListViewAdapter(Context context,ArrayList<String> arr) {
			this.mInflater = LayoutInflater.from(context);
			this.arr = arr;
		}

		@Override
		public int getCount() {
			return arr.size();
		}

		@Override
		public Object getItem(int position) {
			return arr.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			UIitem iitem;
			if (convertView == null) {
				iitem = new UIitem();
				convertView = mInflater.inflate(R.layout.list_item, null);
				iitem.tv =(TextView)convertView.findViewById(R.id.my_item);
				iitem.btn = (Button)convertView.findViewById(R.id.my_ok);
				
				convertView.setTag(iitem);
			}else {
				iitem = (UIitem)convertView.getTag();
			}
			
			iitem.tv.setText(arr.get(position));
			iitem.btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					System.out.println(arr.get(position));
					showMsgDialog(arr,position);
				}
			});
			return convertView;
		}

	}
	class UIitem{
		TextView tv;
		Button btn;
	}
}
