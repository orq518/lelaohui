package com.llh.activity;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ipcamer.demo.PlayActivity;
import com.ipcamer.demo.R;
import com.llh.base.BaseNetActivity;
import com.llh.utils.Constant.REQ_ACTION;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;

public class ShowInfoAtivity extends BaseNetActivity{
	@ViewInject(id=R.id.tv_info_detail_time)
	private TextView tv_time;
	@ViewInject(id=R.id.tv_info_detail_content)
	private TextView tv_content;
	@Override
	protected void parserData(JSONObject response) {
		try {
			JSONObject result = response.getJSONObject("result");
			String code = result.getString("code");
			if ("2".equals(code)) {
				JSONObject rs = result.getJSONObject("rs");
				String content = rs.getString("content");
				String updTime =  rs.getString("updTime");
				tv_content.setText(Html.fromHtml(content));
				tv_time.setText(updTime);
//				pb.setVisibility(View.GONE);
//				pvessel.setVisibility(View.GONE);
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
		Intent intent = this.getIntent();        //��ȡ���е�intent����   
		Bundle param = intent.getExtras();    //��ȡintent�����bundle���� 
		reqData(REQ_ACTION.INFO_CONTENT, param);
	}

	@Override
	public int setLayout() {
		return R.layout.info_content;
	}

}
