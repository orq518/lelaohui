package com.llh.camera.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ipcamer.demo.R;
import com.llh.base.BaseNetActivity;
import com.llh.entity.BaseModel;
import com.llh.entity.DeliveryAddressListModel;
import com.llh.entity.DeliveryAddressModel;
import com.llh.net.SysVar;
import com.llh.utils.Constant.RESPONSE_CODE;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;
import com.yh.materialdesign.edittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 新增收货地址
 */
public class AddAddressAtivity extends BaseNetActivity implements View.OnClickListener {
    @ViewInject(id = R.id.left_btn, click = "onClick")
    private ImageButton left_btn;

    TextView titlebar_text;
    MaterialEditText name, phone,phoneNew, address, post;
    Button commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public int setLayout() {
        return R.layout.add_address_layout;
    }
    @Override
    public void initView() {
        titlebar_text = (TextView) findViewById(R.id.titlebar_text);
        titlebar_text.setText("新增地址");
        name = (MaterialEditText) findViewById(R.id.name);
        phone = (MaterialEditText) findViewById(R.id.phone);
        phoneNew=(MaterialEditText) findViewById(R.id.phone);
        address = (MaterialEditText) findViewById(R.id.address);
        post = (MaterialEditText) findViewById(R.id.post);
        commit = (Button) findViewById(R.id.commit);
        commit.setOnClickListener(this);


    }

    public void commitAddress() {
        try {
            Map<String, Object> map = SysVar.getInstance(this).getUserInfo();
            String userId = (String) map.get("userId");
            String merchantId = (String) map.get("merchantId");
            Bundle param = new Bundle();
            param.putString("userId", userId);
            param.putString("merchantId", merchantId);
            param.putString("realName", URLEncoder.encode(name.getText().toString(), "UTF-8"));
            param.putString("mobile", URLEncoder.encode(phone.getText().toString(), "UTF-8"));
            param.putString("phoneNew", URLEncoder.encode(phoneNew.getText().toString(), "UTF-8"));
//            param.putString("code", URLEncoder.encode(post.getText().toString(), "UTF-8"));
            param.putString("deliveryAddress", URLEncoder.encode(address.getText().toString(), "UTF-8"));
            param.putString("operator", "0");

            reqData("/data/createDeliveryAdd.json", param, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    parserAddressListData(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    dataError(error);
                }
            }, this, false);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public boolean checkAddress() {
        if (name.getText() == null || name.getText().equals("")) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.getText() == null || phone.getText().equals("")) {
            Toast.makeText(this, "请输入电话", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (address.getText() == null || address.getText().equals("")) {
            Toast.makeText(this, "请输入地址", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                if (checkAddress()) {
                    commitAddress();
                }
                break;
            case R.id.left_btn:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);

    }

    private void enroll() {

    }

    /**
     * 获取地址列表
     *
     * @param response
     */
    protected void parserAddressListData(JSONObject response) {
        Log.d("ouou", "response:" + response);
        try {
            JSONObject obj = response.getJSONObject("result");
            String code = obj.getString("code");
            if (!RESPONSE_CODE.SUCCESS_CODE.equals(code)) {
                ToastTool.showText(AddAddressAtivity.this, obj.getString("msg"));
                return;
            }else{
                ToastTool.showText(AddAddressAtivity.this, obj.getString("msg"));
                finish();
            }
//
//            DeliveryAddressListModel addressModel = gson.fromJson(obj.toString(),
//                    DeliveryAddressListModel.class);
//
//            Log.d("ouou", "DietByCateIdModel:" + addressModel.rs.size());
//            group_list.clear();
//            group_list.addAll(addressModel.rs);
//            adapter.notifyDataSetChanged();
//            for (int i = 0; i < group_list.size(); i++) {
//                DeliveryAddressModel adModel = group_list.get(i);
//                if (adModel.isCurrAdd.equals("1")) {
//                    defaultAddress = adModel;
//                }
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void parserData(JSONObject response) {

    }

    @Override
    protected void dataError(VolleyError error) {
        LogTool.e(error.toString());

    }


}
