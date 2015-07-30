package com.llh.camera.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.llh.entity.DeliveryAddressListModel;
import com.llh.entity.DeliveryAddressModel;
import com.llh.entity.DietByCateIdModel;
import com.llh.entity.FoodModel;
import com.llh.entity.WapDietInfoModel;
import com.llh.net.NetManager;
import com.llh.utils.Constant;
import com.llh.utils.Constant.CACHE_KEY;
import com.llh.utils.ImageManager;
import com.llh.utils.OrderFoodInterface;
import com.llh.utils.utils;
import com.llh.view.ActionItem;
import com.llh.view.MyPopupWindow;
import com.llh.view.TitlePopup;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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


    private final static String BREAK_FOOD = "1";
    private final static String LUNCH_FOOD = "2";
    private final static String DINNER_FOOD = "3";
    private final static String NIGHT_FOOD = "4";

    private final static String TODAY_BREAK_FOOD = "today_break_food";
    // private final static String TODAY_BREAK_FOOD = "today_break_food";

    OrderFoodTypeAdapter leftListAdapter;
    FoodAdapter foodAdapter;
    private int mealTime = 1;
    private String cacheKey = CACHE_KEY.FOOT_TODAY_KEY;

    private final static String TODAY_FOOD = "0";
    private final static String TOMORROW_FOOD = "1";
    private final static String POSTNATAL_FOOD = "2";
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

    TextView food_num, address;
    TextView totle_price, delete_all;
    Button commit_button, change_address;
    TextView rightlist_tips;
    /**
     * 是否配送  0：否  1：配送
     */
    int peisongfangshi = 0;
    //    /**
//     * 订餐的地址ID
//     */
//    String addId;
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

    String curDate;
    RadioGroup rg_distribution;

    @Override
    public void initView() {
        getDate();
//        reqData(TODAY_FOOD);
        rightlist_tips = (TextView) findViewById(R.id.rightlist_tips);
        food_num = (TextView) findViewById(R.id.food_num);
        buy_list_Layout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.shopping_list_layout, null);// (RelativeLayout) findViewById(R.id.buy_list_Layout);
        commit_button = (Button) buy_list_Layout.findViewById(R.id.commit_button);
        change_address = (Button) buy_list_Layout.findViewById(R.id.change_address);
        delete_all = (TextView) buy_list_Layout.findViewById(R.id.delete_all);
        commit_button.setOnClickListener(this);
        change_address.setOnClickListener(this);
        delete_all.setOnClickListener(this);
        address = (TextView) buy_list_Layout.findViewById(R.id.address);
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

        //将可选内容与ArrayAdapter连接
        ArrayAdapter typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dateStringArray);

        //设置下拉列表的风格
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter添加到m_Spinner中
        spnner.setAdapter(typeAdapter);


        spnner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0:
                        if (isScope != TODAY_FOOD && shopping_cart_List.size() > 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(OrderFooderActivity.this);
                            builder.setMessage("您有当日已选购的餐饮，是否要放弃？");
                            builder.setTitle("提示");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    shopping_cart_List.clear();
                                    refreshShoppingList(null);
                                    cacheKey = CACHE_KEY.FOOT_TODAY_KEY;
                                    isScope = TODAY_FOOD;
                                    reqData(isScope);
                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.create().show();
                        } else {
                            cacheKey = CACHE_KEY.FOOT_TODAY_KEY;
                            isScope = TODAY_FOOD;
                            reqData(isScope);
                        }

                        break;
                    case 1:
                        if (isScope != TOMORROW_FOOD && shopping_cart_List.size() > 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(OrderFooderActivity.this);
                            builder.setMessage("您有当日已选购的餐饮，是否要放弃？");
                            builder.setTitle("提示");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    shopping_cart_List.clear();
                                    refreshShoppingList(null);
                                    cacheKey = CACHE_KEY.FOOT_TOMORROW_KEY;
                                    isScope = TOMORROW_FOOD;
                                    reqData(isScope);
                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.create().show();
                        } else {
                            cacheKey = CACHE_KEY.FOOT_TOMORROW_KEY;
                            isScope = TOMORROW_FOOD;
                            reqData(isScope);
                        }

                        break;
                    case 2:
                        if (isScope != POSTNATAL_FOOD && shopping_cart_List.size() > 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(OrderFooderActivity.this);
                            builder.setMessage("您有当日已选购的餐饮，是否要放弃？");
                            builder.setTitle("提示");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    shopping_cart_List.clear();
                                    refreshShoppingList(null);
                                    cacheKey = CACHE_KEY.FOOT_AFTERTOMORROW_KEY;
                                    isScope = POSTNATAL_FOOD;
                                    reqData(isScope);
                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.create().show();
                        } else {
                            cacheKey = CACHE_KEY.FOOT_AFTERTOMORROW_KEY;
                            isScope = POSTNATAL_FOOD;
                            reqData(isScope);
                        }

                        break;
                }
//                reqData(isScope);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        right_listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetailPOP(curFoodList.get(position));
//                getFoodDetaile(curFoodList.get(position));
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
                showShopListPoP();

            }
        });
        //获取默认地址
        getDefaultDeliveryAdd();
//        //测试的
//        addTestData();
        rg_distribution = (RadioGroup) buy_list_Layout.findViewById(R.id.rg_distribution);
        rg_distribution.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.dining_room) {//食堂
                    address.setVisibility(View.GONE);
                    peisongfangshi = 0;
                    change_address.setVisibility(View.GONE);
                } else if (checkedId == R.id.kuaidi) {//快递
                    address.setVisibility(View.VISIBLE);
                    change_address.setVisibility(View.VISIBLE);
                    peisongfangshi = 1;
                }

            }
        });
        RadioButton shitang = (RadioButton) buy_list_Layout.findViewById(R.id.dining_room);
        shitang.setChecked(true);
    }

    public void refreshShopButton() {
        if (addressModel != null) {
            address.setText("默认地址：" + addressModel.deliveryAddress + "\n电话：" + addressModel.mobile);
            commit_button.setText("提交订单");
        } else {
            address.setText("无地址");
            commit_button.setText("选择地址");
        }
    }

    MyPopupWindow foodDetailPOP;

    public void showDetailPOP(FoodModel foodModel) {

        LinearLayout foodDetailLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.food_detail_layout, null);
        TextView mealTime = (TextView) foodDetailLayout.findViewById(R.id.mealTime);
        TextView proName = (TextView) foodDetailLayout.findViewById(R.id.proName);
        TextView remark = (TextView) foodDetailLayout.findViewById(R.id.remark);
        TextView proPrice = (TextView) foodDetailLayout.findViewById(R.id.proPrice);
        ImageView image = (ImageView) foodDetailLayout.findViewById(R.id.image);

        proName.setText("餐名：" + foodModel.proName);
        remark.setText("描述：" + foodModel.remark);
        proPrice.setText("价格：" + foodModel.proPrice + "元");


        //1(早餐),2(午餐),3(晚餐),4(夜加餐)
        if (foodModel.mealTime.equals("1")) {
            mealTime.setText("早餐");
        } else if (foodModel.mealTime.equals("2")) {
            mealTime.setText("午餐");
        } else if (foodModel.mealTime.equals("3")) {
            mealTime.setText("晚餐");
        } else if (foodModel.mealTime.equals("4")) {
            mealTime.setText("夜加餐");
        }
        if (!utils.isEmpty(foodModel.proPic)) {
            ImageManager.getInstance(this).getBitmap(NetManager.Ip + foodModel.proPic, new ImageManager.ImageCallBack() {
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

        foodDetailPOP = new MyPopupWindow(OrderFooderActivity.this, foodDetailLayout, 1);
        //显示窗口
//        foodDetailPOP.showAtLocation(OrderFooderActivity.this.findViewById(R.id.main_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
        foodDetailPOP.showAtLocation(OrderFooderActivity.this.findViewById(R.id.main_layout), Gravity.CENTER, 0, 0);


    }

    public void showShopListPoP() {


        refreshShopButton();

        if (shopping_cart_List.size() > 0) {
            shoppingPopupWindow = new MyPopupWindow(OrderFooderActivity.this, buy_list_Layout);
            //显示窗口
            shoppingPopupWindow.showAtLocation(OrderFooderActivity.this.findViewById(R.id.main_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
            shoppingAdapter.setData(shopping_cart_List);
        }
    }

    /**
     * 购物车的popwindow
     */
    MyPopupWindow shoppingPopupWindow;

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
        foodAdapter.setData(data, isScope);

    }

    private void initRightListData(String mealTime) {
        curMealTime = "" + this.mealTime;
        ArrayList<FoodModel> data = sortFood(curFoodType, mealTime);
//        for (int i = 0; i < shopping_cart_List.size(); i++) {
//            FoodModel foodModel = shopping_cart_List.get(i);
//            if (!foodModel.mealTime.equals(curMealTime)) {
//                shopping_cart_List.remove(i);
//                i--;
//            }
//
//        }
        foodAdapter.setData(data, isScope);
    }

    String[] dateStringArray = new String[3];

    public void getDate() {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 0);//
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        dateStringArray[0] = "今日:" + dateString;
        date = new Date();//取时间
        calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        dateString = formatter.format(date);
        dateStringArray[1] = "明日:" + dateString;

        date = new Date();//取时间
        calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 2);//把日期往后增加2天.整数往后推,负数往前移动
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        dateString = formatter.format(date);
        dateStringArray[2] = "后日:" + dateString;


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_address:
                Intent intent = new Intent(OrderFooderActivity.this, AddressAtivity.class);
                intent.putExtra("isFromCreatOrder", true);
                startActivityForResult(intent, GETADDRESS);
                break;

            case R.id.delete_all:

                for (int i = 0; i < shopping_cart_List.size(); i++) {
                    FoodModel foodModel = shopping_cart_List.get(i);
                    foodModel.buyNum = 0;
                }
                foodAdapter.notifyDataSetChanged();
                shopping_cart_List.clear();
                shoppingAdapter.notifyDataSetChanged();
                shoppingPopupWindow.dismiss();
                refreshShoppingList(null);
                break;
            case R.id.commit_button:
                createOrderSort();
                break;
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
                int index = Integer.parseInt(isScope);
                rightlist_tips.setText(dateStringArray[index] + " 早餐");
//                dateStringArray
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
                index = Integer.parseInt(isScope);
                rightlist_tips.setText(dateStringArray[index] + " 午餐");
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
                index = Integer.parseInt(isScope);
                rightlist_tips.setText(dateStringArray[index] + " 晚餐");
                break;
            case R.id.shop_car_commit://选好了
                showShopListPoP();
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

    private void getFoodDetaile(FoodModel foodModel) {
        Bundle b = new Bundle();
//        b.putString("packId", foodModel.proId);
//        reqData("/data/getPackInfo.json", b, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                dialog.dismiss();
//                Logout.d("###response:"+response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
//                dataError(error);
//            }
//        }, this, false);
    }


    public void createOrderSort() {
        ArrayList<ArrayList<FoodModel>> foodTimeList = new ArrayList<ArrayList<FoodModel>>();
        for (int i = 0; i < shopping_cart_List.size(); i++) {
            FoodModel foodModel = shopping_cart_List.get(i);
            if (foodTimeList.size() == 0) {
                ArrayList<FoodModel> foodTypeList = new ArrayList<FoodModel>();
                foodTypeList.add(foodModel);
                foodTimeList.add(foodTypeList);
            } else {
                boolean isHave = false;
                for (int j = 0; j < foodTimeList.size(); j++) {
                    ArrayList<FoodModel> typeList = foodTimeList.get(j);
                    if (foodModel.mealTime.equals(typeList.get(0).mealTime)) {
                        typeList.add(foodModel);
                        isHave = true;
                        break;
                    }
                }
                if (!isHave) {
                    ArrayList<FoodModel> foodTypeList = new ArrayList<FoodModel>();
                    foodTypeList.add(foodModel);
                    foodTimeList.add(foodTypeList);
                }
            }

        }
        for (int i = 0; i < foodTimeList.size(); i++) {
            ArrayList<FoodModel> shopping_List = foodTimeList.get(i);
            if (shopping_List.size() > 0) {
                if (i == shopping_List.size() - 1) {
                    createOrder(shopping_List, true);
                } else {
                    createOrder(shopping_List, false);
                }

            }


        }
        if (foodTimeList.size() == 0) {
            Toast.makeText(this, "您还未选择餐品", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * 创建件订单
     */
    public void createOrder(ArrayList<FoodModel> shopping_cart_List, boolean isLast) {
        Logout.d("创建订单");
        try {
            if (shopping_cart_List.size() == 0) {
                Toast.makeText(this, "您还未选择餐品", Toast.LENGTH_SHORT).show();
                return;
            }

            if (addressModel == null) {
                Intent intent = new Intent(OrderFooderActivity.this, AddressAtivity.class);
                intent.putExtra("isFromCreatOrder", true);
                startActivityForResult(intent, GETADDRESS);
                return;
            }

            Bundle param = new Bundle();
            param.putString("isDistr", "" + peisongfangshi);//是否配送 1:是  0：否
            if (peisongfangshi != 0) {
                param.putString("addId", addressModel.id);
            }
            param.putString("channel", "1");//渠道： 0：pad订餐  1：手机订餐
            param.putString("isScope", isScope);//今天 明天 后天
            param.putString("mealTime", shopping_cart_List.get(0).mealTime);//餐饮时间段


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
            if (isLast) {
                reqData("/data/placeDiet.json", param, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        parserCreatOrder(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        dataError(error);
                    }
                }, this, false);

            } else {
                reqData("/data/placeDiet.json", param, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        parserCreatOrder(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        dataError(error);
                    }
                }, this, false, false);

            }

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
                for (int i = 0; i < shopping_cart_List.size(); i++) {
                    FoodModel foodModel = shopping_cart_List.get(i);
                    foodModel.buyNum = 0;
                }
                foodAdapter.notifyDataSetChanged();
                shopping_cart_List.clear();
                if (shoppingPopupWindow != null) {
                    shoppingPopupWindow.dismiss();
                }
                shoppingAdapter.notifyDataSetChanged();
                refreshShoppingList(null);
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
            int index = Integer.parseInt(isScope);

            switch (this.mealTime) {
                case 1:
                    rightlist_tips.setText(dateStringArray[index] + " 早餐");
                    break;
                case 2:
                    rightlist_tips.setText(dateStringArray[index] + " 午餐");
                    break;
                case 3:
                    rightlist_tips.setText(dateStringArray[index] + " 晚餐");
                    break;
            }
//
//
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
//        Log.d("ouou", "##cateName:" + cateName + "  " + mealTime);
//        Log.d("ouou", "##totleFoodList.size():" + totleFoodList.size());
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
    public void refreshFoodList() {
        foodAdapter.notifyDataSetChanged();
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
                    i--;
                }
                tempFoodModel.buyNum = foodModel.buyNum;
                break;
            }
        }
        if (!isAdded && foodModel != null) {
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
            double d = totlepeice;
            String result = String.format("%.2f", d);
            shop_car_total_msg.setText("总金额：" + result + "元");
            totle_price.setText("总金额：" + result + "元");
        } else {
            shop_car_total_msg.setVisibility(View.VISIBLE);
            shop_car_total_msg.setText("您的购物车是空的");
            totle_price.setText("总金额：0元");
        }

        shoppingAdapter.setData(shopping_cart_List);
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

    public void getDefaultDeliveryAdd() {
        //获取默认的地址
        Bundle param = new Bundle();

        reqData("/data/getDefaultDeliveryAdd.json", param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                dialog.dismiss();
                parserDefaultDeliveryAdd(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
                dataError(error);
            }
        }, this, false, false);

    }

    /**
     * 获取地址列表
     *
     * @param response
     */
    protected void parserDefaultDeliveryAdd(JSONObject response) {
        Log.d("ouou", "response:" + response);
        try {
            JSONObject obj = response.getJSONObject("result");
            String code = obj.getString("code");
            if (!Constant.RESPONSE_CODE.SUCCESS_CODE.equals(code)) {
                ToastTool.showText(OrderFooderActivity.this, obj.getString("msg"));
                return;
            }
            Gson gson = new Gson();
            DeliveryAddressListModel addressListModel = gson.fromJson(obj.toString(),
                    DeliveryAddressListModel.class);

            Log.d("ouou", "DietByCateIdModel:" + addressListModel.rs.size());

            if (addressListModel.rs != null && addressListModel.rs.size() > 0) {
                addressModel = addressListModel.rs.get(0);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    DeliveryAddressModel addressModel;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //resultCode就是在B页面中返回时传的parama，可以根据需求做相应的处理
        if (requestCode == GETADDRESS && resultCode == RESULT_OK && data != null) {
            Serializable ob = data.getSerializableExtra("addressModel");
            if (ob != null) {
                addressModel = (DeliveryAddressModel) ob;
                refreshShopButton();
            }

        }
    }
}
