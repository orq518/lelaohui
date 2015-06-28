package com.llh.activity;

import vstc2.nativecaller.NativeCaller;

import android.app.ProgressDialog;

import com.ipcamer.demo.BridgeService;
import com.ipcamer.demo.ContentCommon;
import com.ipcamer.demo.R;
import com.ipcamer.demo.SystemValue;
import com.ipcamer.demo.WifiBean;
import com.ipcamer.demo.BridgeService.WifiInterface;
import com.llh.base.BaseActivity;
import com.tool.utils.LogTool;

public class SearchWifiActivity extends BaseActivity implements WifiInterface{
	private ProgressDialog progressDialog;
	WifiBean wifiBean;
	@Override
	public void initView() {
		//-------------������----------------
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(getString(R.string.wifi_getparams));
		progressDialog.show();
		//---------------------------------
		wifiBean = new WifiBean();
		NativeCaller.PPPPGetSystemParams(SystemValue.deviceId, ContentCommon.MSG_TYPE_WIFI_SCAN);
		BridgeService.setWifiInterface(this);
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.search_wifi_layout;
	}
	//�ص�wifi����
	@Override
	public void callBackWifiParams(String did, int enable, String ssid,
			int channel, int mode, int authtype, int encryp, int keyformat,
			int defkey, String key1, String key2, String key3, String key4,
			int key1_bits, int key2_bits, int key3_bits, int key4_bits,
			String wpa_psk) {
		wifiBean.setDid(did);
		wifiBean.setEnable(1);// enable������ʱһ����1
		wifiBean.setSsid(ssid);
		wifiBean.setChannel(channel);
		wifiBean.setMode(0);// 0
		wifiBean.setAuthtype(authtype);// security ��--��������
		wifiBean.setEncryp(0);// 0
		wifiBean.setKeyformat(0);// 0
		wifiBean.setDefkey(0);// 0
		wifiBean.setKey1(key1);// ""wep
		wifiBean.setKey2("");// ""
		wifiBean.setKey3("");// ""
		wifiBean.setKey4("");// ""
		wifiBean.setKey1_bits(0);// 0
		wifiBean.setKey2_bits(0);// 0
		wifiBean.setKey3_bits(0);// 0
		wifiBean.setKey4_bits(0);// 0
		wifiBean.setWpa_psk(wpa_psk);// ����
		LogTool.i(" wifi����= "+wifiBean.toString());
		
	}
	//�ص�wifiɨ����
	@Override
	public void callBackWifiScanResult(String did, String ssid, String mac,
			int security, int dbm0, int dbm1, int mode, int channel, int bEnd) {
		// TODO Auto-generated method stub
		
	}
	//�ص�ϵͳ�������
	@Override
	public void callBackSetSystemParamsResult(String did, int paramType,
			int result) {
		// TODO Auto-generated method stub
		
	}
	//�ص���Ϣ��Ϣ����
	@Override
	public void callBackPPPPMsgNotifyData(String did, int type, int param) {
		// TODO Auto-generated method stub
		
	}

}
