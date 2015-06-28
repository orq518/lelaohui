package com.llh.camera.activity;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.ipcamer.demo.R;
import com.llh.base.BaseActivity;
import com.llh.utils.SysValue;
import com.tool.Inject.ViewInject;





public class CameraWifiSettingActivity extends BaseActivity {
	@ViewInject(id=R.id.camera_state_info)
	TextView camera_state_info;
	@ViewInject(id=R.id.camera_state_progressBar)
	ProgressBar camera_state_progressBar;
	@Override
	public void initView() {
		if (SysValue.IS_LINK!=2) {
			
		}
	}

	@Override
	public int setLayout() {
		return R.layout.search_wifi_layout;
	}
}
