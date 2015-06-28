package com.llh.camera.activity;


import java.util.Date;
import vstc2.nativecaller.NativeCaller;
import com.ipcamer.demo.BridgeService;
import com.ipcamer.demo.R;
import com.llh.utils.SysValue;
import com.llh.utils.utils;
import com.tool.utils.LogTool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

@SuppressLint("HandlerLeak") 
public class StartActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    SysValue.is_login = false;
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.welcome_activity);
		
		Intent intent = new Intent();
		intent.setClass(StartActivity.this, BridgeService.class);
		startService(intent);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					NativeCaller.PPPPInitial("ABC");
					long lStartTime = new Date().getTime();
					int state = NativeCaller.PPPPNetworkDetect();//
					LogTool.d("网络状态  = " + state);
					long lEndTime = new Date().getTime();
					if (lEndTime - lStartTime <= 1000) {
						Thread.sleep(3000);
					}
					Message msg = new Message();
					mHandler.sendMessage(msg);
				} catch (Exception e) {

				}
			}
		}).start();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
    		Intent in = new Intent(StartActivity.this, LoginAtivity.class);
//			Intent in = new Intent(StartActivity.this, AddCameraActivity.class);
			startActivity(in);
			finish();
		}
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
			return true;
		return super.onKeyDown(keyCode, event);
	}
}
