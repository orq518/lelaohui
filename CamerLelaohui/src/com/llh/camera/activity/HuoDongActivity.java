package com.llh.camera.activity;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.ipcamer.demo.R;
import com.llh.activity.ShowInfoAtivity;
import com.llh.adapter.InformationListAdapter;
import com.llh.base.BaseNetActivity;
import com.llh.utils.Constant.REQ_ACTION;
import com.llh.utils.ParserTools;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;

public class HuoDongActivity extends BaseNetActivity{
	@ViewInject(id = R.id.left_btn,click="onClick")
	private ImageButton left_btn;
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.left_btn:
			finish();
			break;

		default:
			break;
		}
	}
	@ViewInject(id=R.id.lsit_layout,itemClick="onItemClick")
	private ListView list;
	private ArrayList<Map<String, Object>> data;
	private InformationListAdapter adapter;
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Map<String, Object>map =  data.get(position);
		Bundle param = new Bundle();
		param.putString("contentId", map.get("contentId").toString());
		Intent intent = new Intent(HuoDongActivity.this,ShowInfoAtivity.class);
		intent.putExtras(param);
		startActivity(intent);
		//infoContent.reqData(REQ_ACTION.INFO_CONTENT, param);
	}
	@Override
	protected void parserData(JSONObject response) {
		LogTool.i(response.toString());
		try {
			JSONObject result = response.getJSONObject("result");
			String code = result.getString("code");
			if ("2".equals(code)) {
				JSONArray rs = result.getJSONArray("rs");
				String key = result.getString("serial");
				data = ParserTools.parserCommonTools(rs, key);
				for(Map<String, Object> m : data){
					
					LogTool.i(String.valueOf(m.get("updTime")));
					LogTool.i(String.valueOf(m.get("title")));
					LogTool.i(String.valueOf(m.get("title2")));
				}
				if(data !=null){
					adapter = new InformationListAdapter(this,data);
					adapter.notifyDataSetChanged();
					list.setAdapter(adapter);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void dataError(VolleyError error) {
		LogTool.e(error.toString());
	}

	@Override
	public void initView() {
		Bundle param = new Bundle();
		param.putString("cateId", "61");
		param.putString("page", "1");
		param.putString("pageSize","20");
		this.reqData(REQ_ACTION.INFORMATION_LIST, param);
	}

	@Override
	public int setLayout() {
		return R.layout.info_list;
	}

}
