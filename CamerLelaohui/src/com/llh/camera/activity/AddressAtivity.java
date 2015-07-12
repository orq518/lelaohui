package com.llh.camera.activity;

import android.app.Activity;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ipcamer.demo.R;
import com.llh.base.BaseNetActivity;
import com.llh.entity.DeliveryAddressListModel;
import com.llh.entity.DeliveryAddressModel;
import com.llh.entity.DietByCateIdModel;
import com.llh.net.SysVar;
import com.llh.utils.Constant;
import com.llh.utils.Constant.RESPONSE_CODE;
import com.llh.utils.DataTools;
import com.llh.utils.SharedPreferenceUtil;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressAtivity extends BaseNetActivity implements View.OnClickListener {
    @ViewInject(id = R.id.left_btn, click = "onClick")
    private ImageButton left_btn;


    ExpandableListView addressList;
    MyExpandableListViewAdapter adapter;
    private List<DeliveryAddressModel> group_list = new ArrayList<DeliveryAddressModel>();

    TextView titlebar_text;
    Button right_btn;
    /**
     * 是否来自创建订单
     */
    boolean isFromCreatOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFromCreatOrder = getIntent().getBooleanExtra("isFromCreatOrder", false);
        Logout.d("isFromCreatOrder:" + isFromCreatOrder);
    }

    @Override
    public void initView() {
        titlebar_text = (TextView) findViewById(R.id.titlebar_text);
        titlebar_text.setText("地址管理");
        right_btn = (Button) findViewById(R.id.right_btn);
        right_btn.setText("新增收货地址");
        right_btn.setOnClickListener(this);
        right_btn.setVisibility(View.VISIBLE);
        addressList = (ExpandableListView) findViewById(R.id.address_list);
        adapter = new MyExpandableListViewAdapter(this);
        addressList.setAdapter(adapter);

    }

    public void getAddress() {
        //获取默认的地址
        Map<String, Object> map = SysVar.getInstance(this).getUserInfo();
        String userId = (String) map.get("userId");
        Bundle param = new Bundle();
        param.putString("userId", userId);

        reqData("/data/getDefaultDeliveryAdd.json", param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                parserAddressData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                dataError(error);
            }
        }, this, false);

//        reqData("/data/getDeliveryAddList.json", param, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                dialog.dismiss();
//                parserAddressListData(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
//                dataError(error);
//            }
//        }, this, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddress();
    }

    @Override
    public int setLayout() {
        return R.layout.address_manage_layout;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_btn:
                startActivity(new Intent(this, AddAddressAtivity.class));
                break;
            case R.id.left_btn:
                Intent intent = new Intent();
                if (defaultAddress != null) {
                    intent.putExtra("address", defaultAddress);
                }
                setResult(RESULT_OK);
                finish();
                break;
        }
    }



    private void enroll() {

    }

    /**
     * 获取地址列表
     *
     * @param response
     */
    protected void parserAddressData(JSONObject response) {
        Log.d("ouou", "response:" + response);
        try {
            JSONObject obj = response.getJSONObject("result");
            String code = obj.getString("code");
            if (!RESPONSE_CODE.SUCCESS_CODE.equals(code)) {
                ToastTool.showText(AddressAtivity.this, obj.getString("msg"));
                return;
            }
            Gson gson = new Gson();
            DeliveryAddressListModel addressModel = gson.fromJson(obj.toString(),
                    DeliveryAddressListModel.class);

            Log.d("ouou", "DietByCateIdModel:" + addressModel.rs.size());
            group_list.clear();
            group_list.addAll(addressModel.rs);
            adapter.notifyDataSetChanged();
            for (int i = 0; i < group_list.size(); i++) {
                DeliveryAddressModel adModel = group_list.get(i);
                if (adModel.isCurrAdd.equals("1")) {
                    defaultAddress = adModel;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                ToastTool.showText(AddressAtivity.this, obj.getString("msg"));
                return;
            }
            Gson gson = new Gson();
            DeliveryAddressListModel addressModel = gson.fromJson(obj.toString(),
                    DeliveryAddressListModel.class);

            Log.d("ouou", "DietByCateIdModel:" + addressModel.rs.size());
            group_list.clear();
            group_list.addAll(addressModel.rs);
            adapter.notifyDataSetChanged();
            for (int i = 0; i < group_list.size(); i++) {
                DeliveryAddressModel adModel = group_list.get(i);
                if (adModel.isCurrAdd.equals("1")) {
                    defaultAddress = adModel;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 默认收件地址
     */
    DeliveryAddressModel defaultAddress;

    @Override
    protected void parserData(JSONObject response) {

    }

    @Override
    protected void dataError(VolleyError error) {
        LogTool.e(error.toString());

    }

    //用过ListView的人一定很熟悉，只不过这里是BaseExpandableListAdapter
    class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

        private Context context;

        public MyExpandableListViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getGroupCount() {
            return group_list.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return group_list.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return group_list.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            GroupHolder groupHolder = null;
            if (convertView == null) {
                convertView = (View) getLayoutInflater().from(context).inflate(
                        R.layout.address_father_item_layout, null);
                groupHolder = new GroupHolder();
                groupHolder.name = (TextView) convertView.findViewById(R.id.name);
                groupHolder.address = (TextView) convertView.findViewById(R.id.address);
                groupHolder.operate = (Button) convertView.findViewById(R.id.operate);
                convertView.setTag(groupHolder);
            } else {
                groupHolder = (GroupHolder) convertView.getTag();
            }
            final DeliveryAddressModel addressModel = group_list.get(groupPosition);
            String name = addressModel.realName;
            if (addressModel.isCurrAdd != null && addressModel.isCurrAdd.equals("1")) {
                name = name + "(默认)";
            }
            groupHolder.name.setText(name);
            groupHolder.address.setText(addressModel.deliveryAddress);
            Logout.d("##isFromCreatOrder:" + isFromCreatOrder);
            if (isFromCreatOrder) {
                Logout.d("##1111:");
                groupHolder.operate.setVisibility(View.VISIBLE);
                groupHolder.operate.setText("选择");
                groupHolder.operate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("addressModel", addressModel);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
            } else {
                Logout.d("##222:");

//                if (addressModel.isCurrAdd != null && addressModel.isCurrAdd.equals("1")) {
//                    groupHolder.operate.setVisibility(View.VISIBLE);
//                    groupHolder.operate.setText("编辑");
//                    groupHolder.operate.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            editDefaultAddress(addressModel);
//                        }
//                    });
//                } else {
                    groupHolder.operate.setVisibility(View.INVISIBLE);
//                    groupHolder.operate.setText("设为默认");
//                    groupHolder.operate.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            setDefaultAddress(addressModel);
//                        }
//                    });
//                }

            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            convertView = (View) getLayoutInflater().from(context).inflate(
                    R.layout.address_expand_item_layout, null);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView phonenum = (TextView) convertView.findViewById(R.id.phonenum);
            TextView address = (TextView) convertView.findViewById(R.id.address);
            TextView postnum = (TextView) convertView.findViewById(R.id.postnum);
            DeliveryAddressModel addressModel = group_list.get(groupPosition);
            name.setText("姓名：" + addressModel.realName);
            phonenum.setText("电话：" + addressModel.mobile);
            address.setText("地址：" + addressModel.deliveryAddress);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }


    }

    class GroupHolder {
        public TextView name;
        public TextView address;
        public Button operate;
    }

    /**
     * 设置默认地址
     */
    public void setDefaultAddress(final DeliveryAddressModel addressModel) {

        try {
            Bundle param = new Bundle();
            param.putString("realName", URLEncoder.encode(addressModel.realName, "UTF-8"));
            param.putString("mobile", URLEncoder.encode(addressModel.mobile, "UTF-8"));
            param.putString("phoneNew", URLEncoder.encode(addressModel.phone, "UTF-8"));
            param.putString("id", URLEncoder.encode(addressModel.id, "UTF-8"));
            if (addressModel.code != null) {
                param.putString("code", URLEncoder.encode(addressModel.code, "UTF-8"));
            }
            param.putString("deliveryAddress", URLEncoder.encode(addressModel.deliveryAddress, "UTF-8"));
            param.putString("operator", "1");//更新

            reqData("/data/createDeliveryAdd.json", param, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    JSONObject obj = null;
                    try {
                        obj = response.getJSONObject("result");
                        String code = obj.getString("code");
                        if (!RESPONSE_CODE.SUCCESS_CODE.equals(code)) {
                            ToastTool.showText(AddressAtivity.this, obj.getString("msg"));

//                                HashMap<String, Object> dataMap=DataTools.readData(AddressAtivity.this);
//                                dataMap.put(Constant.DEFAUIT_ADDRESS,addressModel);
//                                DataTools.writeData(AddressAtivity.this,dataMap);


                            return;
                        } else {
                            ToastTool.showText(AddressAtivity.this, obj.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    dataError(error);
                }
            }, this, false);
        } catch (Exception e) {
            Logout.d("e:" + e);
        }
    }

    /**
     * 编辑地址
     */
    public void editDefaultAddress(final DeliveryAddressModel addressModel) {

        try {
            Intent intent = new Intent(this,AddAddressAtivity.class);
            intent.putExtra("addressModel", addressModel);
            startActivityForResult(intent, 1100);
        } catch (Exception e) {
            Logout.d("e:" + e);
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        //resultCode就是在B页面中返回时传的parama，可以根据需求做相应的处理
//        if (arg0 == 1100 && arg1 == RESULT_OK && arg2 != null) {
//            getAddress();
//        }
    }
}
