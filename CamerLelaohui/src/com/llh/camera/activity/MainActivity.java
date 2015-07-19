package com.llh.camera.activity;

import org.json.JSONObject;

import vstc2.nativecaller.NativeCaller;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.ipcamer.demo.BridgeService;
import com.ipcamer.demo.R;
import com.ipcamer.demo.SystemValue;
import com.llh.adapter.HomeAdatpter;
import com.llh.adapter.HomeAdatpterTwo;
import com.llh.adapter.VisitAdapter;
import com.llh.base.BaseNetActivity;
import com.llh.camera.activity.VisitMainActivity.StartPPPPThread;
import com.llh.net.SysVar;
import com.llh.utils.FoodCacheUtils;
import com.llh.utils.SharedPreferenceUtil;
import com.llh.utils.SysValue;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;
/**
 * 乐老会主界面
 * @author Administrator
 *
 */
public class MainActivity extends BaseNetActivity{
	
	@ViewInject(id = R.id.viewpager1)
	private ViewPager vp = null;
	@ViewInject(id = R.id.indicator_container)
	private LinearLayout indicatorContainer;
	private GridView gv = null, gv1 = null;
	private SparseArray<View> data = new SparseArray<View>();

	VisitAdapter<View> adapter = null;

	private long exitTime = 0;
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				ToastTool.showText(this, "再按一次退出程序");
				exitTime = System.currentTimeMillis();
			} else {
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void initView() {
		vistServiceTime();
		LayoutInflater li = LayoutInflater.from(getApplicationContext());
		gv = (GridView) li.inflate(R.layout.homepageitem, null);
		gv1 = (GridView)li.inflate(R.layout.homepageitem, null);
		gv.setAdapter(new HomeAdatpter(getApplicationContext()));
		gv1.setAdapter(new HomeAdatpterTwo(getApplicationContext()));
		data.put(0, gv);
		data.put(1, gv1);
		adapter = new VisitAdapter<View>(data);
		vp.setAdapter(adapter);
		indicatorContainer = (LinearLayout) this
				.findViewById(R.id.indicator_container);
		ImageView indicator = null;
		for (int i = 0; i < 2; i++) {
			indicator = new ImageView(getApplicationContext());
			indicator.setPadding(0, 0, 10, 0);
			if (i == 0) {
				indicator.setImageResource(R.drawable.indicator_dot_selected);
			} else {
				indicator.setImageResource(R.drawable.indicator_dot_normal);
			}
			indicatorContainer.addView(indicator, i);
		}
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				if (data.size() > 1) {
					for (int i = 0; i < indicatorContainer.getChildCount(); i++) {
						((ImageView) indicatorContainer.getChildAt(i))
								.setImageResource(R.drawable.indicator_dot_normal);
					}
					((ImageView) indicatorContainer.getChildAt(arg0))
							.setImageResource(R.drawable.indicator_dot_selected);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LogTool.d("功能项  = " + arg2);
				Intent exitIntent = null;
				switch (arg2) {
				case 0:// 活动
					exitIntent = new Intent(MainActivity.this,HuoDongActivity.class);
					break;
				case 1:// 资讯
					exitIntent = new Intent(MainActivity.this,NewsAtivity.class);
					break;
				case 2:// 探望
					exitIntent = new Intent(MainActivity.this,VisitMainActivity.class);
					break;
				case 3:// 积分
					break;
				case 4:// 订餐
					exitIntent = new Intent(MainActivity.this,OrderFooderActivity.class);
					break;
				case 5:// 设置
					exitIntent = new Intent(MainActivity.this,SettingActivity.class);
					break;
				}
				if (exitIntent != null) {
					startActivity(exitIntent);
				} else {
					ToastTool.showText(MainActivity.this, "功能开发中。。。。");
				}

			}
		});
		gv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LogTool.d("功能项2  = " + arg2);
				Intent exitIntent = null;
				switch (arg2) {
				case 0:// 我的账户
					break;
				case 1:// 注销
//					exitIntent = new Intent(MainActivity.this,LoginAtivity.class);
					System.exit(0);
					break;
				case 2:// 积分查询
					break;
				case 3:// 消费查询

					break;
				case 4:// 健康查询

					break;
				case 5:// 更多

					break;
				}
				if (exitIntent != null) {
					startActivity(exitIntent);
					if (arg2 == 1) {
						finish();
					}
				} else {
					ToastTool.showText(MainActivity.this, "功能开发中。。。。");
				}
			}
		});
		FoodCacheUtils.getIntance().reqFoodData();
	}
	@Override
	public int setLayout() {
		return R.layout.main_activity;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		NativeCaller.Free();
		Intent intent = new Intent();
		intent.setClass(this, BridgeService.class);
		stopService(intent);
		
	}

	private void vistServiceTime(){
		Bundle param = new Bundle();
		reqData("/data/paymentReminders.json", param);
	}
	@Override
	protected void parserData(JSONObject response) {
		LogTool.i(response.toString());
		try {
			JSONObject result = response.getJSONObject("result");
			String code = result.getString("code");
			if(code.equals("2")){
				JSONObject rs = result.getJSONObject("rs");
				SystemValue.deviceId= rs.getString("businessCode");
				Logout.d("摄像头deviceName = " + SystemValue.deviceName);
				Logout.d("摄像头devicePass = " + SystemValue.devicePass);
				SystemValue.deviceName = "admin";
				SystemValue.devicePass = "888888";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void dataError(VolleyError error) {
		// TODO Auto-generated method stub
		
	}
}
