package com.llh.camera.activity;

import vstc2.nativecaller.NativeCaller;

import com.ipcamer.demo.BridgeService;
import com.ipcamer.demo.ContentCommon;
import com.ipcamer.demo.R;
import com.ipcamer.demo.SystemValue;
import com.ipcamer.demo.BridgeService.IpcamClientInterface;
import com.tool.utils.LogTool;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * 使用后台服务与摄像头进行连接
 * 
 * @author Administrator
 * 
 */
public class LinkService extends Service implements IpcamClientInterface {

	private static final String TAG = "LinkService";
	private static final String STR_DID = "did";
	private static final String STR_MSG_PARAM = "msgparam";

	private Intent intent;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(TAG, "onCreate");
		BridgeService.setIpcamClientInterface(this);
		NativeCaller.Init();
		new Thread(new StartPPPPThread()).start();
	}
	class StartPPPPThread implements Runnable {
		@Override
		public void run() {
			try {
				Thread.sleep(100);
				StartCameraPPPP();
			} catch (Exception e) {

			}
		}
	}

	private void StartCameraPPPP() {
		try {
			Thread.sleep(100);
		} catch (Exception e) {
		}
		int result = NativeCaller.StartPPPP(SystemValue.deviceId,
				SystemValue.deviceName, SystemValue.devicePass);
		LogTool.d("ip =" + "result:" + result);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private int tag = 0;
	private Handler PPPPMsgHandler = new Handler() {
		public void handleMessage(Message msg) {

			Bundle bd = msg.getData();
			int msgParam = bd.getInt(STR_MSG_PARAM);
			int msgType = msg.what;
			String did = bd.getString(STR_DID);
			LogTool.d("msgParam = " + msgParam);
			LogTool.d("msgType = " + msgType);
			switch (msgType) {
			case ContentCommon.PPPP_MSG_TYPE_PPPP_STATUS:
				int resid;
				switch (msgParam) {
				case ContentCommon.PPPP_STATUS_CONNECTING:// 0
					resid = R.string.pppp_status_connecting;
					tag = 2;
					break;
				case ContentCommon.PPPP_STATUS_CONNECT_FAILED:// 3 连接失败
					resid = R.string.pppp_status_connect_failed;
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_DISCONNECT:// 4
					resid = R.string.pppp_status_disconnect;
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_INITIALING:// 1
					resid = R.string.pppp_status_initialing;
					tag = 2;
					break;
				case ContentCommon.PPPP_STATUS_INVALID_ID:// 5 ID号无效
					resid = R.string.pppp_status_invalid_id;
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_ON_LINE:// 2
					resid = R.string.pppp_status_online;
					tag = 1;
					break;
				case ContentCommon.PPPP_STATUS_DEVICE_NOT_ON_LINE:// 6 摄像机不在线
					resid = R.string.device_not_on_line;
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_CONNECT_TIMEOUT:// 7 连接超时
					resid = R.string.pppp_status_connect_timeout;
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_CONNECT_ERRER:// 8 密码错误
					resid = R.string.pppp_status_pwd_error;
					tag = 0;
					break;
				default:
					resid = R.string.pppp_status_unknown;
				}
				intent = new Intent();
				intent.putExtra("tag", tag);
				intent.putExtra("resid", resid);
				intent.putExtra("msgParam", msgParam);
				sendBroadcast(intent);
				if (msgParam == ContentCommon.PPPP_STATUS_ON_LINE) {
					NativeCaller.PPPPGetSystemParams(did,
							ContentCommon.MSG_TYPE_GET_PARAMS);
				}
				if (msgParam == ContentCommon.PPPP_STATUS_INVALID_ID
						|| msgParam == ContentCommon.PPPP_STATUS_CONNECT_FAILED
						|| msgParam == ContentCommon.PPPP_STATUS_DEVICE_NOT_ON_LINE
						|| msgParam == ContentCommon.PPPP_STATUS_CONNECT_TIMEOUT
						|| msgParam == ContentCommon.PPPP_STATUS_CONNECT_ERRER) {
					NativeCaller.StopPPPP(did);
				}
				if (msgParam == ContentCommon.PPPP_STATUS_CONNECT_TIMEOUT) {
					try {
						Thread.sleep(3000);
						NativeCaller.StartPPPP(SystemValue.deviceId,
								SystemValue.deviceName, SystemValue.devicePass);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case ContentCommon.PPPP_MSG_TYPE_PPPP_MODE:
				break;
			}
		}
	};

	@Override
	public void BSMsgNotifyData(String did, int type, int param) {
		LogTool.d(" param:" + param);
		Bundle bd = new Bundle();
		Message msg = PPPPMsgHandler.obtainMessage();
		msg.what = type;
		bd.putInt(STR_MSG_PARAM, param);
		bd.putString(STR_DID, did);
		msg.setData(bd);
		PPPPMsgHandler.sendMessage(msg);
	}

	@Override
	public void BSSnapshotNotify(String did, byte[] bImage, int len) {
		// TODO Auto-generated method stub

	}

	@Override
	public void callBackUserParams(String did, String user1, String pwd1,
			String user2, String pwd2, String user3, String pwd3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void CameraStatus(String did, int status) {
		// TODO Auto-generated method stub

	}

}
