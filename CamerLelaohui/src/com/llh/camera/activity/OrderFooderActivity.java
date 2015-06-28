package com.llh.camera.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ipcamer.demo.R;
import com.llh.adapter.FoodAdapter;
import com.llh.adapter.OrderFoodTypeAdapter;
import com.llh.base.BaseNetActivity;
import com.llh.entity.DietByCateIdModel;
import com.llh.entity.FoodModel;
import com.llh.utils.Constant.CACHE_KEY;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

public class OrderFooderActivity extends BaseNetActivity {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// // 设置全屏显示
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

	}

	@Override
	public void initView() {
		reqData(TODAY_FOOD);
		titlebar_text.setText("订餐");
		leftListAdapter = new OrderFoodTypeAdapter();
		left_listView.setAdapter(leftListAdapter);

		// setLeftListData(cacheKey, 1);
		left_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				isScroll = false;
				setLeftSelected(position);
				// int rightSection = 0;
				// if (null != sectionedAdapter) {
				// for (int i = 0; i < position; i++) {
				// rightSection += sectionedAdapter.getCountForSection(i) + 1;
				// }
				// right_listview.setSelection(rightSection);
				// }

				
			}

		});
		foodAdapter = new FoodAdapter();
		right_listview.setAdapter(foodAdapter);
		// right_listview.setOnScrollListener(new OnScrollListener() {
		//
		// @Override
		// public void onScrollStateChanged(AbsListView arg0, int arg1) {
		//
		// }
		//
		// @Override
		// public void onScroll(AbsListView view, int firstVisibleItem,
		// int visibleItemCount, int totalItemCount) {
		// if (isScroll) {
		// for (int i = 0; i < left_listView.getChildCount(); i++) {
		//
		// if (i == sectionedAdapter
		// .getSectionForPosition(firstVisibleItem)) {
		// left_listView.getChildAt(i).setBackgroundColor(
		// Color.rgb(255, 255, 255));
		// left_listView.getChildAt(i)
		// .findViewById(R.id.distance)
		// .setVisibility(View.VISIBLE);
		// } else {
		// left_listView.getChildAt(i).setBackgroundColor(
		// Color.TRANSPARENT);
		// left_listView.getChildAt(i)
		// .findViewById(R.id.distance)
		// .setVisibility(View.GONE);
		//
		// }
		// }
		//
		// } else {
		// isScroll = true;
		// }
		// }
		// });
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
		break_btn.setSelected(true);
		break_btn.setClickable(false);
		lunch_btn.setSelected(false);
		lunch_btn.setClickable(true);
		dinner_btn.setSelected(false);
		dinner_btn.setClickable(true);

	}

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
		curFoodType = foodType.get(position);
		initRightListData(curMealTime);
	}

	private void initLeftListData(String mealTime) {
		getFoodType();
		Log.d("ouou", "foodType.size():" + foodType.size());
		// 设置左边菜单
		leftListAdapter.setData(foodType);
		Handler mHandler=new Handler();
		mHandler.postDelayed(new Runnable(){@Override
		public void run() {
			setLeftSelected(0);
		}}, 500);
		

		ArrayList<FoodModel> data = sortFood(foodType.get(0), mealTime);
		foodAdapter.setData(data);

	}

	private void initRightListData(String mealTime) {
		ArrayList<FoodModel> data = sortFood(curFoodType, mealTime);
		foodAdapter.setData(data);
	}

	private void reqData(String isScope) {
		Bundle b = new Bundle();
		b.putString("isScope", isScope);
		// b.putString("mealTime", BREAK_FOOD);
		// b.putString("merchantId", "cb0fa542-8d0f-43c9-acda-cda7890bae75");
		b.putString("serial", isScope);
		reqData("/data/getDietByCateId.json", b);
	}

	@Override
	public int setLayout() {
		return R.layout.order_food_layout;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_btn:
			finish();
			break;
		case R.id.break_btn:
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
			break_btn.setSelected(false);
			break_btn.setClickable(true);
			lunch_btn.setSelected(false);
			lunch_btn.setClickable(true);
			dinner_btn.setSelected(true);
			dinner_btn.setClickable(false);
			this.mealTime = 3;
			initRightListData("" + mealTime);
			break;
		case R.id.shop_car_commit:
			break;
		default:
			break;
		}
	}

	@Override
	protected void dataError(VolleyError error) {
		LogTool.e(error.toString());

	}

	@Override
	protected void parserData(JSONObject response) {
		Log.d("ouou", "response:" + response);
		JSONObject resultObj;
		try {
			resultObj = response.getJSONObject("result");
			if (resultObj == null) {
				ToastTool.showText(this, "数据错误");
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
		for (int i = 0; i < totleFoodList.size(); i++) {
			FoodModel food = totleFoodList.get(i);
			if (food.cateName.equals(cateName)
					&& food.mealTime.equals(mealTime)) {
				curFoodList.add(food);
			}
		}
		return curFoodList;
	}
}
