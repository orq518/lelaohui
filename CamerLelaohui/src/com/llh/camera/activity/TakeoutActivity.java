//package com.llh.camera.activity;
//
//import java.util.HashMap;
//import java.util.Map;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.VolleyError;
//import com.ipcamer.demo.R;
//import com.llh.base.BaseNetActivity;
//import com.llh.entity.DietCategory;
//import com.llh.utils.Constant.RESPONSE_CODE;
//import com.llh.utils.TiemUtils;
//import com.tool.Inject.ViewInject;
//import com.tool.utils.LogTool;
//import com.viewpagerindicator.TabPageIndicator;
//
//public class TakeoutActivity extends BaseNetActivity {
//
//	TabPageIndicator indicator;
//	// 标题数组
//	Map<Integer, DietCategory> title = new HashMap<Integer, DietCategory>();
//	int time_tag = 0;
//
//	@Override
//	protected void parserData(JSONObject response) {
//		LogTool.i(response.toString());
//		try {
//			JSONObject obj = response.getJSONObject("result");
//			String code = obj.getString("code");
//			if (RESPONSE_CODE.SUCCESS_CODE.equals(code)) {
//				String serial = obj.getString("serial");
//				if (serial.equals("classify")) {
//					JSONArray jarr = obj.getJSONArray("rs");
//					for (int i = 0; i < jarr.length(); i++) {
//						JSONObject ify = jarr.getJSONObject(i);
//						String cateName = ify.getString("cateType");
//						LogTool.i(cateName);
//						DietCategory head = new DietCategory();
//						head.cateId = ify.getString("cateId");
//						head.cateName = ify.getString("cateName");
//						head.cateType = ify.getString("cateType");
//						head.mealTime = ify.getString("mealTime");
//						title.put(i, head);
//					}
//				}
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	private void reqBody() {
//		// for(int i=0;i<title.size();i++){
//		// DietCategory head = title.get(i);
//		Bundle param = new Bundle();
//		param.putString("isScope", "0");
//		param.putString("mealTime", "1");
//		 param.putString("cateId", "241");
//		param.putString("serial", "1");
//		reqData("/data/getPackInfo.json", param);
//		
//		//reqData("/data/getDietByCateId.json", param);
//		// }
//	}
//
//	@Override
//	protected void dataError(VolleyError error) {
//		// TODO Auto-generated method stub
//
//	}
//
//	/**
//	 * Tab标题
//	 */
//	private static final String[] TITLE = new String[] { "今天", "明天", "后天" };
//
//	@Override
//	public void initView() {
//		// ViewPager的adapter
//		FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(
//				getSupportFragmentManager());
//		ViewPager pager = (ViewPager) findViewById(R.id.pager);
//		pager.setAdapter(adapter);
//
//		// 实例化TabPageIndicator然后设置ViewPager与之关联
//		indicator = (TabPageIndicator) findViewById(R.id.indicator);
//		indicator.setViewPager(pager);
//		indicator.setCurrentItem(0);
//		indicator.notifyDataSetChanged();
//
//		// 如果我们要对ViewPager设置监听，用indicator设置就行了
//		indicator.setOnPageChangeListener(new OnPageChangeListener() {
//
//			@Override
//			public void onPageSelected(int arg0) {
//				
//				LogTool.i("标题下标  =" + arg0);
//				int meal_time = TiemUtils.EqualsCurrentTime();
//				switch (arg0) {
//				case 0:// 今天
//					MealTime(meal_time);
//					break;
//				case 1:// 明天
//					MealTime(meal_time);
//					break;
//				case 2:// 后天
//					MealTime(meal_time);
//					break;
//				default:
//					break;
//				}
//				 Toast.makeText(getApplicationContext(), TITLE[arg0]+meal_time,
//				 Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//				
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//			}
//		});
//
//		Bundle param = new Bundle();
//		param.putString("isScope", "0");
//		param.putString("mealTime", "1");
//		param.putString("serial", "classify");
//		reqData("/data/getDietByCateId.json", param);
//		int meal_time = TiemUtils.EqualsCurrentTime();
//		MealTime(meal_time);
//	}
//
//	@ViewInject(id = R.id.breakfast, click = "onClick")
//	TextView breakfast;
//	@ViewInject(id = R.id.lunch, click = "onClick")
//	TextView lunch;
//	@ViewInject(id = R.id.dinner, click = "onClick")
//	TextView dinner;
//	@ViewInject(id = R.id.snacks, click = "onClick")
//	TextView snacks;
//
//	public void onClick(View v) {
//		textinit();
//		switch (v.getId()) {
//		case R.id.breakfast:
//			breakfast.setTextColor(getResources().getColor(R.color.hong_se));
//			break;
//		case R.id.lunch:
//			lunch.setTextColor(getResources().getColor(R.color.hong_se));
//			break;
//		case R.id.dinner:
//			dinner.setTextColor(getResources().getColor(R.color.hong_se));
//			break;
//		case R.id.snacks:
//			snacks.setTextColor(getResources().getColor(R.color.hong_se));
//			break;
//		}
//	}
//
//	private void textinit() {
//		breakfast.setTextColor(getResources().getColor(R.color.hei_se));
//		lunch.setTextColor(getResources().getColor(R.color.hei_se));
//		dinner.setTextColor(getResources().getColor(R.color.hei_se));
//		snacks.setTextColor(getResources().getColor(R.color.hei_se));
//	}
//
//	@Override
//	public int setLayout() {
//		return R.layout.takeout;
//	}
//
//	/**
//	 * ViewPager适配器
//	 * 
//	 * @author len
//	 * 
//	 */
//	class TabPageIndicatorAdapter extends FragmentPagerAdapter {
//		public TabPageIndicatorAdapter(FragmentManager fm) {
//			super(fm);
//		}
//
//		@Override
//		public Fragment getItem(int position) {
//			// 新建一个Fragment来展示ViewPager item的内容，并传递参数
//			Fragment fragment = new ItemFragment();
//			Bundle args = new Bundle();
//			args.putString("arg", TITLE[position]);
//			fragment.setArguments(args);
//
//			return fragment;
//		}
//
//		@Override
//		public CharSequence getPageTitle(int position) {
//			return TITLE[position % TITLE.length];
//		}
//
//		@Override
//		public int getCount() {
//			return TITLE.length;
//		}
//	}
//	public void MealTime(int tiem) {
//		textinit();
//		switch (tiem) {
//		case 1:
//			breakfast.setTextColor(getResources().getColor(R.color.hong_se));
//			break;
//		case 2:
//			lunch.setTextColor(getResources().getColor(R.color.hong_se));
//			break;
//		case 3:
//			dinner.setTextColor(getResources().getColor(R.color.hong_se));
//			break;
//		case 4:
//			snacks.setTextColor(getResources().getColor(R.color.hong_se));
//			break;
//		}
//	}
//	
//	public void getTitles(int date,int mealTime){
//		Bundle param = new Bundle();
//		param.putString("isScope", String.valueOf(date));
//		param.putString("mealTime", String.valueOf(mealTime));
//		param.putString("serial", "classify");
//		reqData("/data/getDietCategory.json", param);
//	}
//}
