package com.llh.camera.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ipcamer.demo.R;
import com.llh.adapter.FoodAdapter;
import com.llh.adapter.OrderFoodTypeAdapter;
import com.llh.adapter.ShoppingAdapter;
import com.llh.base.BaseNetActivity;
import com.llh.entity.DeliveryAddressModel;
import com.llh.entity.DietByCateIdModel;
import com.llh.entity.FoodModel;
import com.llh.net.SysVar;
import com.llh.utils.Constant;
import com.llh.utils.Constant.CACHE_KEY;
import com.llh.utils.OrderFoodInterface;
import com.llh.view.ActionItem;
import com.llh.view.ShoppingPopupWindow;
import com.llh.view.TitlePopup;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.json.Json;

public class OrderFooderActivity extends BaseNetActivity implements OrderFoodInterface, OnClickListener {

    private boolean isScroll = true;

    @ViewInject(id = R.id.left_listview)
    private ListView left_listView;
    @ViewInject(id = R.id.right_listview)
    private ListView right_listview;
    @ViewInject(id = R.id.left_btn, click = "onClick")
    private ImageButton left_btn;
    @ViewInject(id = R.id.break_btn, click = "onClick")
    private Button break_btn;
    @ViewInject(id = R.id.lunch_btn, click = "onClick")
    private Button lunch_btn;
    @ViewInject(id = R.id.dinner_btn, click = "onClick")
    private Button dinner_btn;
    @ViewInject(id = R.id.titlebar_text)
    TextView titlebar_text;
    @ViewInject(id = R.id.spnner)
    private Spinner spnner;
    @ViewInject(id = R.id.shop_car_total_msg)
    TextView shop_car_total_msg;
    @ViewInject(id = R.id.shop_car_commit, click = "onClick")
    private Button commit;
    @ViewInject(id = R.id.msg_toast)
    private ImageView msg_toast;

    private final static String TODAY_FOOD = "0";
    private final static String TOMORROW_FOOD = "1";
    private final static String POSTNATAL_FOOD = "2";

    private final static String BREAK_FOOD = "1";
    private final static String LUNCH_FOOD = "2";
    private final static String DINNER_FOOD = "3";
    private final static String NIGHT_FOOD = "4";

    private final static String TODAY_BREAK_FOOD = "today_break_food";
    // private final static String TODAY_BREAK_FOOD = "today_break_food";

    OrderFoodTypeAdapter leftListAdapter;
    FoodAdapter foodAdapter;
    private int mealTime = 0;
    private String cacheKey = CACHE_KEY.FOOT_TODAY_KEY;
    private String isScope = TODAY_FOOD;

    /**
     * 餐饮的实体类
     */
    DietByCateIdModel foodModel;
    /**
     * 当前食物类型
     */
    public String curFoodType;
    /**
     * 当前吃饭时间
     */
    public String curMealTime = BREAK_FOOD;
    /**
     * 食物类型数组
     */
    ArrayList<String> foodType = new ArrayList<String>();
    /**
     * 总的的食物列表
     */
    ArrayList<FoodModel> totleFoodList = new ArrayList<FoodModel>();
    /**
     * 当前的食物列表
     */
    ArrayList<FoodModel> curFoodList = new ArrayList<FoodModel>();
    /**
     * 购物车食物列表
     */
    ArrayList<FoodModel> shopping_cart_List = new ArrayList<FoodModel>();
    /**
     * 购物车的清单
     */
    private ListView buy_listView;
    /**
     * 购物车的清单布局
     */
    RelativeLayout buy_list_Layout;

    ShoppingAdapter shoppingAdapter;

    TextView food_num;
    TextView totle_price;
    /**
     * 订餐的地址ID
     */
    String addId;
    // 定义标题栏弹窗按钮
    private TitlePopup titlePopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 初始化弹出框数据
     */
    private void initPopData() {
        // 给标题栏弹窗添加子类

//        titlePopup.addAction(new ActionItem(this, "我的账余", 0));
//        titlePopup.addAction(new ActionItem(this, "我的退款", 0));
        titlePopup.addAction(new ActionItem(this, "我的地址", R.drawable.ic_edit_camera));
        titlePopup.addAction(new ActionItem(this, "我的订单", R.drawable.ic_edit_camera));
        titlePopup
                .setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
                    @Override
                    public void onItemClick(ActionItem item, int position) {
                        switch (position) {
                            case 0://我的地址
                                Intent intent = new Intent(OrderFooderActivity.this, AddressAtivity.class);
                                startActivityForResult(intent, GETADDRESS);
                                break;
                            case 1:// 我的订单
//                                ToastTool.showText(OrderFooderActivity.this,"暂无订单");
                                intent = new Intent(OrderFooderActivity.this, OrderQueryAtivity.class);
                                startActivity(intent);
                                break;

                            default:
                                break;
                        }
                    }
                });
    }

    @Override
    public int setLayout() {
        return R.layout.order_food_layout;
    }

    @Override
    public void initView() {

        reqData(TODAY_FOOD);

        food_num = (TextView) findViewById(R.id.food_num);
        buy_list_Layout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.shopping_list_layout, null);// (RelativeLayout) findViewById(R.id.buy_list_Layout);
        buy_listView = (ListView) buy_list_Layout.findViewById(R.id.food_listview);
        totle_price = (TextView) buy_list_Layout.findViewById(R.id.totle_price);
        shoppingAdapter = new ShoppingAdapter();
        buy_listView.setAdapter(shoppingAdapter);
        shoppingAdapter.registerCallBack(this);
        titlebar_text.setText("订餐");
        Button right_btn = (Button) findViewById(R.id.right_btn);
        right_btn.setText("我的");
        right_btn.setOnClickListener(this);
        right_btn.setVisibility(View.VISIBLE);
        leftListAdapter = new OrderFoodTypeAdapter();
        left_listView.setAdapter(leftListAdapter);

        // setLeftListData(cacheKey, 1);
        left_listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long arg3) {
                isScroll = false;
                setLeftSelected(position);


            }

        });
        foodAdapter = new FoodAdapter(OrderFooderActivity.this);
        right_listview.setAdapter(foodAdapter);
        foodAdapter.registerCallBack(this);

        spnner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0:
                        cacheKey = CACHE_KEY.FOOT_TODAY_KEY;
                        isScope = TODAY_FOOD;
                        break;
                    case 1:
                        cacheKey = CACHE_KEY.FOOT_TOMORROW_KEY;
                        isScope = TOMORROW_FOOD;
                        break;
                    case 2:
                        cacheKey = CACHE_KEY.FOOT_AFTERTOMORROW_KEY;
                        isScope = POSTNATAL_FOOD;
                        break;
                }
                // Log.d("ouou", "setOnItemSelectedListener");
                // initLeftListData("" + mealTime);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        break_btn.setTextColor(getResources().getColor(R.color.color_white));
        lunch_btn.setTextColor(getResources().getColor(R.color.color_black));
        dinner_btn.setTextColor(getResources().getColor(R.color.color_black));
        break_btn.setSelected(true);
        break_btn.setClickable(false);
        lunch_btn.setSelected(false);
        lunch_btn.setClickable(true);
        dinner_btn.setSelected(false);
        dinner_btn.setClickable(true);
        RelativeLayout shop_car_layout = (RelativeLayout) findViewById(R.id.shop_car_layout);
        /**
         * 购物栏数量
         */
        shop_car_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shopping_cart_List.size() > 0) {

//                    float totlepeice = 0;
//                    for (int i = 0; i < shopping_cart_List.size(); i++) {
//                        FoodModel tempFoodModel = shopping_cart_List.get(i);
//                        totlepeice = totlepeice + Float.parseFloat(tempFoodModel.proPrice);
//                    }
//                    totle_price.setText("总金额：" + totlepeice + "元");

                    menuWindow = new ShoppingPopupWindow(OrderFooderActivity.this, buy_list_Layout);
                    //显示窗口
                    menuWindow.showAtLocation(OrderFooderActivity.this.findViewById(R.id.main_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                    shoppingAdapter.setData(shopping_cart_List);
//                    if (buy_list_Layout.isShown()){
//                        buy_list_Layout.setVisibility(View.GONE);
//                    }else{
//                        buy_list_Layout.setVisibility(View.VISIBLE);
//                        shoppingAdapter.setData(shopping_cart_List);
//                    }
                } else {
//                    buy_list_Layout.setVisibility(View.GONE);
                }

            }
        });


//        //测试的
//        addTestData();
    }

    ShoppingPopupWindow menuWindow;

    public void setLeftSelected(int position) {

        for (int i = 0; i < left_listView.getChildCount(); i++) {
            if (i == position) {
                left_listView.getChildAt(i).setBackgroundColor(
                        Color.rgb(255, 255, 255));
                left_listView.getChildAt(i).findViewById(R.id.distance)
                        .setVisibility(View.VISIBLE);
            } else {
                left_listView.getChildAt(i).setBackgroundColor(
                        Color.TRANSPARENT);
                left_listView.getChildAt(i).findViewById(R.id.distance)
                        .setVisibility(View.GONE);
            }
        }
        if (foodType.size() == 0) {
            getFoodType();
        }
        curFoodType = foodType.get(position);
        initRightListData(curMealTime);
    }

    private void initLeftListData(String mealTime) {
        getFoodType();
        Log.d("ouou", "foodType.size():" + foodType.size());
        // 设置左边菜单
        leftListAdapter.setData(foodType);
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setLeftSelected(0);
            }
        }, 500);


        ArrayList<FoodModel> data = sortFood(foodType.get(0), mealTime);
        foodAdapter.setData(data);

    }

    private void initRightListData(String mealTime) {
        curMealTime = "" + this.mealTime;
        ArrayList<FoodModel> data = sortFood(curFoodType, mealTime);
        for (int i = 0; i < shopping_cart_List.size(); i++) {
            FoodModel foodModel = shopping_cart_List.get(i);
            if (!foodModel.mealTime.equals(curMealTime)) {
                shopping_cart_List.remove(i);
                i--;
            }

        }
        foodAdapter.setData(data);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                finish();
                break;
            case R.id.break_btn:
                break_btn.setTextColor(getResources().getColor(R.color.color_white));
                lunch_btn.setTextColor(getResources().getColor(R.color.color_black));
                dinner_btn.setTextColor(getResources().getColor(R.color.color_black));
                break_btn.setSelected(true);
                break_btn.setClickable(false);
                lunch_btn.setSelected(false);
                lunch_btn.setClickable(true);
                dinner_btn.setSelected(false);
                dinner_btn.setClickable(true);
                this.mealTime = 1;
                initRightListData("" + mealTime);
                break;
            case R.id.lunch_btn:
                lunch_btn.setTextColor(getResources().getColor(R.color.color_white));
                break_btn.setTextColor(getResources().getColor(R.color.color_black));
                dinner_btn.setTextColor(getResources().getColor(R.color.color_black));
                break_btn.setSelected(false);
                break_btn.setClickable(true);
                lunch_btn.setSelected(true);
                lunch_btn.setClickable(false);
                dinner_btn.setSelected(false);
                dinner_btn.setClickable(true);
                this.mealTime = 2;
                initRightListData("" + mealTime);
                break;
            case R.id.dinner_btn:
                dinner_btn.setTextColor(getResources().getColor(R.color.color_white));
                lunch_btn.setTextColor(getResources().getColor(R.color.color_black));
                break_btn.setTextColor(getResources().getColor(R.color.color_black));
                break_btn.setSelected(false);
                break_btn.setClickable(true);
                lunch_btn.setSelected(false);
                lunch_btn.setClickable(true);
                dinner_btn.setSelected(true);
                dinner_btn.setClickable(false);
                this.mealTime = 3;
                initRightListData("" + mealTime);
                break;
            case R.id.shop_car_commit://选好了
//                Intent intent = new Intent(this, AddressAtivity.class);
//                startActivityForResult(intent, GETADDRESS);
////                startActivity(new );
                createOrder();
                break;
            case R.id.right_btn:

                if (titlePopup == null) {
                    titlePopup = new TitlePopup(this,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    initPopData();
                }
                titlePopup.show(v);
                break;

            default:
                break;
        }
    }

    final int GETADDRESS = 1011;

    private void reqData(String isScope) {
        Bundle b = new Bundle();
        b.putString("isScope", isScope);
        // b.putString("mealTime", BREAK_FOOD);
        // b.putString("merchantId", "cb0fa542-8d0f-43c9-acda-cda7890bae75");
        b.putString("serial", isScope);
        reqData("/data/getDietByCateId.json", b, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                parserGetDietData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                dataError(error);
            }
        }, this, false);
    }

    /**
     * 创建件订单
     */
    public void createOrder() {

        try {
            if (shopping_cart_List.size() == 0) {
                Toast.makeText(this, "您还未选择餐品", Toast.LENGTH_SHORT).show();
                return;
            }
//        userId
//        merchantId
//        isDistr(20150601新增)
//        addId(20150601新增)
//        channel(2015060新增)
//        isScope(20150602新增)
//        mealtime(0602新增)
            Bundle param = new Bundle();
            param.putString("isDistr", "1");
            param.putString("addId", "179");
            param.putString("channel", "1");
            param.putString("isScope", isScope);
            param.putString("mealTime", curMealTime);


            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < shopping_cart_List.size(); i++) {
                JSONObject jb = new JSONObject();
                FoodModel foodModel = shopping_cart_List.get(i);
                jb.put("proId", foodModel.proId);
                jb.put("proNum", "" + foodModel.buyNum);
                jb.put("supplierId", foodModel.supplierId);
                jb.put("mealType", foodModel.mealType);
                jsonArray.put(jb);
            }

            Log.d("ouou", "jsonArray.toString():" + jsonArray.toString());
            param.putString("list", URLEncoder.encode(jsonArray.toString(), "UTF-8"));
            reqData("/data/placeDiet.json", param, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    parserCreatOrder(response);
//                parserGetDietData(response);
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

    protected void dataError(VolleyError error) {
        LogTool.e(error.toString());

    }

    /**
     * 订餐返回解析
     *
     * @param response
     */
    protected void parserCreatOrder(JSONObject response) {
        Log.d("ouou", "response:" + response);
        JSONObject resultObj;
        try {
            resultObj = response.getJSONObject("result");
            if (resultObj == null) {
                ToastTool.showText(this, "数据错误");
                return;
            }
            String code = resultObj.getString("code");
            Log.d("ouou", "code:" + code);
            if (!Constant.RESPONSE_CODE.SUCCESS_CODE.equals(code)) {
                ToastTool.showText(this, resultObj.getString("msg"));
                return;
            } else {
                ToastTool.showText(this, resultObj.getString("msg"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取餐饮的列表
     *
     * @param response
     */
    protected void parserGetDietData(JSONObject response) {
        Log.d("ouou", "response:" + response);
        JSONObject resultObj;
        try {
            resultObj = response.getJSONObject("result");
            if (resultObj == null) {
                ToastTool.showText(this, "数据错误");
                return;
            }
            String code = resultObj.getString("code");
            if (!Constant.RESPONSE_CODE.SUCCESS_CODE.equals(code)) {
                ToastTool.showText(this, resultObj.getString("msg"));
                return;
            }
            Gson gson = new Gson();
            foodModel = gson.fromJson(resultObj.toString(),
                    DietByCateIdModel.class);

            Log.d("ouou", "DietByCateIdModel:" + foodModel.rs.size());
            totleFoodList.clear();
            totleFoodList.addAll(foodModel.rs);

            initLeftListData(curFoodType);

            // String code = resultObj.getString("code");
            // String serial = resultObj.getString("serial");
            // String msg = resultObj.getString("msg");
            // JSONArray rs = resultObj.getJSONArray("rs");
            // ArrayList<Map<String, Object>> data = ParserTools
            // .parserCommonTools(rs, serial);
            // cacheDataMap.put(serial, data);
            // setLeftListData(cacheKey, mealTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取食物的类型
     */
    public ArrayList<String> getFoodType() {
        foodType.clear();
        for (int i = 0; i < totleFoodList.size(); i++) {
            FoodModel food = totleFoodList.get(i);
            if (!foodType.contains(food.cateName)) {
                foodType.add(food.cateName);
            }
        }
        if (foodType.size() > 0) {
            curFoodType = foodType.get(0);
        }

        return foodType;
    }

    /**
     * 根据食品类型和吃饭时间划分
     *
     * @param cateName
     * @param mealTime
     */
    public ArrayList<FoodModel> sortFood(String cateName, String mealTime) {
        curFoodList.clear();
        Log.d("ouou", "##cateName:" + cateName + "  " + mealTime);
        Log.d("ouou", "##totleFoodList.size():" + totleFoodList.size());
        for (int i = 0; i < totleFoodList.size(); i++) {
            FoodModel food = totleFoodList.get(i);

//            Log.d("ouou", "%%cateName:" + food.cateName + "  " + food.mealTime);

            if (food.cateName.equals(cateName)
                    && food.mealTime.equals(mealTime)) {
                curFoodList.add(food);
            }
        }
        Log.d("ouou", "##curFoodList.size():" + curFoodList.size());
        return curFoodList;
    }

    @Override
    public void callBack() {
    }

    @Override
    public void refreshShoppingList(FoodModel foodModel) {
        boolean isAdded = false;
        for (int i = 0; i < shopping_cart_List.size(); i++) {
            FoodModel tempFoodModel = shopping_cart_List.get(i);
            if (foodModel.proId.equals(tempFoodModel.proId)) {
                isAdded = true;
                if (foodModel.buyNum <= 0) {
                    shopping_cart_List.remove(i);
                }
                tempFoodModel.buyNum = foodModel.buyNum;
                break;
            }
        }
        if (!isAdded) {
            shopping_cart_List.add(foodModel);
        }

        int num = 0;
        for (int i = 0; i < shopping_cart_List.size(); i++) {
            FoodModel tempFoodModel = shopping_cart_List.get(i);
            Log.d("ouou", "##tempFoodModel.buyNum:" + tempFoodModel.buyNum);
            num = num + tempFoodModel.buyNum;
        }
        food_num.setText("" + num);

        if (shopping_cart_List.size() > 0) {
            float totlepeice = 0;
            for (int i = 0; i < shopping_cart_List.size(); i++) {
                FoodModel tempFoodModel = shopping_cart_List.get(i);
                totlepeice = totlepeice + Float.parseFloat(tempFoodModel.proPrice) * tempFoodModel.buyNum;
            }
            shop_car_total_msg.setText("总金额：" + totlepeice + "元");
        } else {
            shop_car_total_msg.setVisibility(View.VISIBLE);
            shop_car_total_msg.setText("您的购物车是空的");
        }


    }


//    public void addTestData() {
//        for (int i = 0; i < 14; i++) {
//            FoodModel model = new FoodModel();
//            model.cateName = "类别 " + i % 3;
//            model.proId = "" + i;
//            model.proName = "土豆 " + i;
//            model.proPrice = i + "";
//            model.mealTime = "" + (i % 4 + 1);
//            model.remark = "营养丰富";
//            totleFoodList.add(model);
//        }
//        initLeftListData(curMealTime);
//    }

    @Override
    protected void parserData(JSONObject response) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //resultCode就是在B页面中返回时传的parama，可以根据需求做相应的处理
        if (requestCode == GETADDRESS && resultCode == RESULT_OK && data != null) {
            Serializable ob = data.getSerializableExtra("address");
            if (ob != null) {
                DeliveryAddressModel address = (DeliveryAddressModel) ob;
                addId = address.id;
            }

        }
    }
}
