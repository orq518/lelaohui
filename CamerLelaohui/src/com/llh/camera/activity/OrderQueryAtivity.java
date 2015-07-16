package com.llh.camera.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ipcamer.demo.R;
import com.llh.base.BaseNetActivity;
import com.llh.entity.DeliveryAddressListModel;
import com.llh.entity.DeliveryAddressModel;
import com.llh.entity.FoodModel;
import com.llh.entity.OrderListModel;
import com.llh.entity.OrderModel;
import com.llh.entity.WapDietInfoModel;
import com.llh.net.NetManager;
import com.llh.utils.Constant.RESPONSE_CODE;
import com.llh.utils.ImageManager;
import com.llh.utils.utils;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单查询
 */
public class OrderQueryAtivity extends BaseNetActivity implements View.OnClickListener {
    @ViewInject(id = R.id.left_btn, click = "onClick")
    private ImageButton left_btn;

    ExpandableListView addressList;
    MyExpandableListViewAdapter adapter;
    private List<OrderListModel> group_list = new ArrayList<OrderListModel>();

    TextView titlebar_text;
    Button right_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setLayout() {
        return R.layout.order_querry_layout;
    }

    String serial;

    @Override
    public void initView() {
        titlebar_text = (TextView) findViewById(R.id.titlebar_text);
        titlebar_text.setText("我的订单");
        addressList = (ExpandableListView) findViewById(R.id.address_list);
        adapter = new MyExpandableListViewAdapter(this);
        addressList.setAdapter(adapter);
    }

    public void querryorder() {

        Bundle param = new Bundle();
        param.putString("isScope", "0");
        reqData("/data/orderedDiet.json", param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                parserorderListData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                dataError(error);
            }
        }, this, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        querryorder();
    }


    public void onClick(View v) {
        switch (v.getId()) {
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
    protected void parserorderListData(JSONObject response) {
        Log.d("ouou", "response:" + response);
        try {
            JSONObject obj = response.getJSONObject("result");
            String code = obj.getString("code");
            serial = obj.getString("serial");
            if (!RESPONSE_CODE.SUCCESS_CODE.equals(code)) {
                ToastTool.showText(OrderQueryAtivity.this, obj.getString("msg"));
                return;
            }
            Gson gson = new Gson();
            OrderModel orderModer = gson.fromJson(obj.toString(),
                    OrderModel.class);

            Log.d("ouou", "DietByCateIdModel:" + orderModer.rs.size());
            group_list.clear();
            group_list.addAll(orderModer.rs);
            adapter.notifyDataSetChanged();
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
            return group_list.get(groupPosition).wapDietInfoList.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return group_list.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return group_list.get(groupPosition).wapDietInfoList;
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
                        R.layout.order_father_item_layout, null);
                groupHolder = new GroupHolder();
                groupHolder.order_num = (TextView) convertView.findViewById(R.id.order_num);
                groupHolder.goods = (TextView) convertView.findViewById(R.id.goods);
                groupHolder.amount = (TextView) convertView.findViewById(R.id.amount);
                groupHolder.date = (TextView) convertView.findViewById(R.id.date);
                groupHolder.food = (TextView) convertView.findViewById(R.id.food);
                groupHolder.order_status = (TextView) convertView.findViewById(R.id.order_status);
                groupHolder.btn_cancel = (Button) convertView.findViewById(R.id.btn_cancel);

                convertView.setTag(groupHolder);
            } else {
                groupHolder = (GroupHolder) convertView.getTag();
            }
            final OrderListModel orderListModel = group_list.get(groupPosition);

            if (orderListModel.payStatus != null && orderListModel.payStatus.equals("0")) {
                groupHolder.order_status.setText("支付状态：未支付");
            } else if (orderListModel.payStatus != null && orderListModel.payStatus.equals("1")) {
                groupHolder.order_status.setText("支付状态：已支付");
            } else {
                groupHolder.order_status.setText("");
            }
            if (orderListModel.cancel != null && orderListModel.cancel.equals("0")) {
                groupHolder.btn_cancel.setVisibility(View.GONE);
                groupHolder.btn_cancel.setText("支付状态：未支付");
                groupHolder.btn_cancel.setOnClickListener(null);
            } else if (orderListModel.cancel != null && orderListModel.cancel.equals("1")) {
                groupHolder.btn_cancel.setVisibility(View.VISIBLE);
                groupHolder.btn_cancel.setClickable(true);
                groupHolder.btn_cancel.setText("取消");
                groupHolder.btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrder(orderListModel);

                    }
                });
            } else if (orderListModel.cancel != null && orderListModel.cancel.equals("2")) {
                groupHolder.btn_cancel.setVisibility(View.VISIBLE);
                groupHolder.btn_cancel.setClickable(false);
                groupHolder.btn_cancel.setText("已取消");
                groupHolder.btn_cancel.setOnClickListener(null);
            } else {
                groupHolder.btn_cancel.setVisibility(View.GONE);
                groupHolder.btn_cancel.setText("");
                groupHolder.btn_cancel.setOnClickListener(null);
            }


            groupHolder.order_num.setText(orderListModel.orderCode);
//            0 单品 1 套餐 2 混合
            if (orderListModel.mealType.equals("0")) {
                groupHolder.goods.setText("单品");
            } else if (orderListModel.mealType.equals("1")) {
                groupHolder.goods.setText("套餐");
            } else if (orderListModel.mealType.equals("2")) {
                groupHolder.goods.setText("混合");
            }

            groupHolder.amount.setText(orderListModel.proPrice);
            groupHolder.date.setText(orderListModel.addTime);
            //1(早餐),2(午餐),3(晚餐),4(夜加餐)
            if (orderListModel.mealTime.equals("1")) {
                groupHolder.food.setText("早餐");
            } else if (orderListModel.mealTime.equals("2")) {
                groupHolder.food.setText("午餐");
            } else if (orderListModel.mealTime.equals("3")) {
                groupHolder.food.setText("晚餐");
            } else if (orderListModel.mealTime.equals("4")) {
                groupHolder.food.setText("夜加餐");
            }


            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            Log.d("", "");
            convertView = (View) getLayoutInflater().from(context).inflate(
                    R.layout.order_expand_item_layout, null);
            TextView mealTime = (TextView) convertView.findViewById(R.id.mealTime);
            TextView proName = (TextView) convertView.findViewById(R.id.proName);
            TextView remark = (TextView) convertView.findViewById(R.id.remark);
            TextView proPrice = (TextView) convertView.findViewById(R.id.proPrice);
            TextView proNum = (TextView) convertView.findViewById(R.id.proNum);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            if (childPosition == 0) {
                title.setVisibility(View.VISIBLE);
            } else {
                title.setVisibility(View.GONE);
            }

            ImageView image = (ImageView) convertView.findViewById(R.id.image);
            WapDietInfoModel orderListModel = group_list.get(groupPosition).wapDietInfoList.get(childPosition);

            proName.setText("餐名：" + orderListModel.proName);
            remark.setText("描述：" + orderListModel.remark);
            proPrice.setText("价格：" + orderListModel.proPrice + "元");
            proNum.setText("数量：" + orderListModel.proNum);


            //1(早餐),2(午餐),3(晚餐),4(夜加餐)
            if (orderListModel.mealTime.equals("1")) {
                mealTime.setText("早餐");
            } else if (orderListModel.mealTime.equals("2")) {
                mealTime.setText("午餐");
            } else if (orderListModel.mealTime.equals("3")) {
                mealTime.setText("晚餐");
            } else if (orderListModel.mealTime.equals("4")) {
                mealTime.setText("夜加餐");
            }
            if (!utils.isEmpty(orderListModel.imgUrl)) {
                ImageManager.getInstance(OrderQueryAtivity.this).getBitmap(NetManager.Ip + orderListModel.imgUrl, new ImageManager.ImageCallBack() {
                    @Override
                    public void loadImage(ImageView imageView, Bitmap bitmap) {
                        if (bitmap != null && imageView != null) {
                            imageView.setImageBitmap(bitmap);
                            imageView
                                    .setScaleType(ImageView.ScaleType.FIT_XY);
                        } else {
                            imageView.setImageResource(R.drawable.waimai);
                        }
                    }
                }, image);
            } else {
                image.setImageResource(R.drawable.waimai);
            }


            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

    class GroupHolder {
        public TextView order_num;
        public TextView goods;
        public TextView amount;
        public TextView date;
        public TextView food;
        public TextView title;
        public Button btn_cancel;
        public TextView order_status;


    }

    /**
     * 取消订单
     */
    public void cancelOrder(final OrderListModel orderListModel) {

        Bundle param = new Bundle();

//        JSONArray jsonArray = new JSONArray();
//        try {
//            JSONObject jb = new JSONObject();
////            jb.put("orderId", orderListModel.orderCode);
//            jb.put("orderCode", "35532694342058990");
//            jsonArray.put(jb);
//            param.putString("list", URLEncoder.encode(jsonArray.toString(), "UTF-8"));
//            Log.d("ouou", "jsonArray.toString():" + jsonArray.toString());
//
//
//        } catch (Exception e) {
//        }
        param.putString("orderId", orderListModel.orderId);
        param.putString("serial", serial);
        reqData("/data/cancelDietOrder.json", param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.d("ouou", "response:" + response);
                try {
                    JSONObject obj = response.getJSONObject("result");
                    String code = obj.getString("code");
                    ToastTool.showText(OrderQueryAtivity.this, obj.getString("msg"));

                    if (RESPONSE_CODE.SUCCESS_CODE.equals(code)) {
//                        group_list.remove(orderListModel);
//                        adapter.notifyDataSetChanged();
                        querryorder();
                        return;
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
    }
}
