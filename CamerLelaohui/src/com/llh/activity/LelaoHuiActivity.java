package com.llh.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.VolleyError;
import com.ipcamer.demo.R;
import com.llh.base.BaseNetActivity;
import com.llh.utils.CommonUtils;
import com.llh.utils.Constant.RESPONSE_CODE;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

public class LelaoHuiActivity extends BaseNetActivity {

	@Override
	protected void parserData(JSONObject response) {
		LogTool.i("成功返回数据:" + response.toString());
		try {
			JSONObject obj = response.getJSONObject("result");
			String code = obj.getString("code");
			if (code.equals("1")) {
				ToastTool.showText(LelaoHuiActivity.this,obj.getString("msg"));
				finish();
			}else {
				ToastTool.showText(LelaoHuiActivity.this,obj.getString("msg"));
				finish();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void dataError(VolleyError error) {
		LogTool.e(error.toString());

	}

	@ViewInject(id = R.id.llh_yue)
	EditText llh_yue;
	@ViewInject(id = R.id.ka_id)
	EditText ka_id;
	@ViewInject(id = R.id.ka_pwd)
	EditText ka_pwd;
	@ViewInject(id = R.id.jin_e)
	EditText jin_e;
	@ViewInject(id = R.id.btn_zf, click = "onClick")
	Button btn_zf;
	Bundle paran;

	public void onClick(View V) {
		String str_ka_id = CommonUtils.getContentFromEditText(ka_id);
		String str_ka_pwd = CommonUtils.getContentFromEditText(ka_pwd);
		String str_jin_e = CommonUtils.getContentFromEditText(jin_e);
		if (str_ka_id.equals("")) {
			ToastTool.showText(this, "请输入卡号");
		}
		if (str_ka_pwd.equals("")) {
			ToastTool.showText(this, "请输入密码");
		}
		if (str_jin_e.equals("")) {
			ToastTool.showText(this, "请输入金额");
		}
		LogTool.d("cardId" + str_ka_id);
//		paran.putLong("cardId",Long.valueOf(str_ka_id));
		paran.putString("cardId",str_ka_id);
		paran.putString("userPwd",str_ka_pwd);
		reqData("/data/renewalFee.json", paran);
	}

	@Override
	public void initView() {
		paran = getIntent().getExtras();
		llh_yue.setText(paran.getString("value"));
		ka_id.setText(paran.getString("cardId"));
		jin_e.setText(paran.getString("my_value"));
	}

	@Override
	public int setLayout() {
		return R.layout.lelaoka_layout;
	}

}
