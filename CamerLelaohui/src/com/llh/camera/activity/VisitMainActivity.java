package com.llh.camera.activity;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.json.JSONObject;

import vstc2.nativecaller.NativeCaller;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ipcamer.demo.BridgeService;
import com.ipcamer.demo.BridgeService.IpcamClientInterface;
import com.ipcamer.demo.BridgeService.PlayInterface;
import com.ipcamer.demo.ContentCommon;
import com.ipcamer.demo.PlayActivity;
import com.ipcamer.demo.R;
import com.ipcamer.demo.SettingWifiActivity;
import com.ipcamer.demo.SystemValue;
import com.llh.activity.JFActivity;
import com.llh.adapter.VisitAdapter;
import com.llh.adapter.VisitHistoryAdapter;
import com.llh.base.BaseNetActivity;
import com.llh.net.SysVar;
import com.llh.utils.SharedPreferenceUtil;
import com.llh.utils.SysValue;
import com.llh.utils.utils;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

public class VisitMainActivity extends BaseNetActivity implements
		IpcamClientInterface, PlayInterface {
	@ViewInject(id = R.id.titlebar_text)
	private TextView titlebar_text;
	@ViewInject(id = R.id.right_btn, click = "onClick")
	Button btn;
	@ViewInject(id = R.id.left_btn, click = "onClick")
	ImageButton close;
	int num = 0;
    
	ArrayList<ImageView> array = null;
	VisitHistoryAdapter adapter;
	@ViewInject(id = R.id.point_layout)
	LinearLayout indicatorContainer;
	@Override
	public void initView() {
		titlebar_text.setText("探望");
		vistServiceTime();
		num = utils.getBtmapCount(3);
		viewpager.setVisibility(View.GONE);
		imageView.setVisibility(View.VISIBLE);
		String str_DID = SharedPreferenceUtil
				.getUserInfo(SysValue.KEY_CAMERA_ID, this);
		if(SysVar.getInstance(getApplicationContext()).getUserIsCameraAdministrator(str_DID))
		{
			btn.setVisibility(View.VISIBLE);
		}else{
			btn.setVisibility(View.INVISIBLE);
		}
		viewpagerInit();
//		num --;
	}
    private void viewpagerInit() {
		array = new ArrayList<ImageView>();
		adapter = new VisitHistoryAdapter(array);
		viewpager.setAdapter(adapter);
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				if(null!=num_text)
				num_text.setText("当前第" + (arg0+1) + "张"+" "+"共计"+array.size()+"张");
				if (array.size() > 1) {
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
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@ViewInject(id = R.id.image_progressBar1)
	private ProgressBar progressBar;
	@ViewInject(id = R.id.image_textView1)
	private TextView textView_top_show;
	private static final String STR_DID = "did";
	private static final String STR_MSG_PARAM = "msgparam";
	private int tag = 0;

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
		LogTool.e("ip =" + "result:" + result);
	}

	@Override
	public int setLayout() {
		return R.layout.image_activity;
	}

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
	}

	@Override
	public void callBackUserParams(String did, String user1, String pwd1,
			String user2, String pwd2, String user3, String pwd3) {
	}

	@Override
	public void CameraStatus(String did, int status) {
	}

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
					progressBar.setVisibility(View.VISIBLE);
					tag = 2;
					break;
				case ContentCommon.PPPP_STATUS_CONNECT_FAILED:// 3
					resid = R.string.pppp_status_connect_failed;
					progressBar.setVisibility(View.GONE);
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_DISCONNECT:// 4
					resid = R.string.pppp_status_disconnect;
					progressBar.setVisibility(View.GONE);
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_INITIALING:// 1
					resid = R.string.pppp_status_initialing;
					progressBar.setVisibility(View.VISIBLE);
					tag = 2;
					break;
				case ContentCommon.PPPP_STATUS_INVALID_ID:// 5
					resid = R.string.pppp_status_invalid_id;
					progressBar.setVisibility(View.GONE);
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_ON_LINE:// 2
					resid = R.string.pppp_status_online;
					progressBar.setVisibility(View.GONE);
					tag = 1;
					break;
				case ContentCommon.PPPP_STATUS_DEVICE_NOT_ON_LINE:// 6
					resid = R.string.device_not_on_line;
					progressBar.setVisibility(View.GONE);
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_CONNECT_TIMEOUT:// 7
					resid = R.string.pppp_status_connect_timeout;
					progressBar.setVisibility(View.GONE);
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_CONNECT_ERRER:// 8
					resid = R.string.pppp_status_pwd_error;
					progressBar.setVisibility(View.GONE);
					tag = 0;
					break;
				default:
					resid = R.string.pppp_status_unknown;
				}
				textView_top_show.setText(getResources().getString(resid));
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
				break;
			case ContentCommon.PPPP_MSG_TYPE_PPPP_MODE:
				break;
			}
			LogTool.e("msgParam = " + msgParam);
			if (msgParam == ContentCommon.PPPP_STATUS_ON_LINE) {
				tag = msgParam;
//				btn.setVisibility(View.VISIBLE);
				BridgeService.setPlayInterface(VisitMainActivity.this);
				NativeCaller.StartPPPPLivestream(SystemValue.deviceId, 10);
			}

		}
	};
	@ViewInject(id = R.id.btn_next1, click = "onClick")
	private Button btn_next;
	@ViewInject(id = R.id.btn_next2, click = "onClick")
	private Button btn_next2;
	@ViewInject(id = R.id.btn_next3, click = "onClick")
	private Button btn_next3;
	@ViewInject(id = R.id.show_image1)
	private ImageView imageView;
	@ViewInject(id = R.id.viewpager)
	private ViewPager viewpager;
	@ViewInject(id = R.id.num_text)
	private TextView num_text;
	private boolean isTakepic = true;
	private byte[] videodata = null;
	public int nVideoWidths = 0;
	public int nVideoHeights = 0;
	private Bitmap mBmp;
	public boolean isRun = false;

	int count = 0;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next3:
			LogTool.i("探望记录");
			if (num <= 0) {
				num = utils.getBtmapCount(2);
			}
			LogTool.i("??????????="+num);
			if(num<=0)
			{
				ToastTool.showText(this, "没有探望记录");
			}else{
				num_text.setVisibility(View.GONE);
				viewpager.setCurrentItem(0);
				viewpager.setVisibility(View.VISIBLE);
				indicatorContainer.setVisibility(View.VISIBLE);
				imageView.setVisibility(View.GONE);
				new Thread() {
					@Override
					public void run() {
						utils.readAllBitmap();
						array.clear();
						for(int i = 0;i<utils.arr.size();i++)
						{
							ImageView image = new ImageView(VisitMainActivity.this);
							image.setImageBitmap(utils.arr.get(i));
							array.add(image);
						}
						Message msg = new Message();
						msg.what = 2;
						imageHandler.sendMessage(msg);
					}
				}.start();
			}

			break;
		case R.id.btn_next1:
			indicatorContainer.setVisibility(View.GONE);
			viewpager.setVisibility(View.GONE);
			imageView.setVisibility(View.VISIBLE);
			LogTool.d("拍照");
			num_text.setVisibility(View.VISIBLE);
			num_text.setText("");
			isTakepic = true;
			LogTool.e(isTakepic);
			break;

		case R.id.btn_next2:
			android.util.Log.d("xcq", "nVideoHeights: "+nVideoHeights);
			if (tag == 2) {
				isTakepic = false;
				Intent exitIntent = new Intent(VisitMainActivity.this,
						PlayActivity.class);
				startActivity(exitIntent);
			} else {
				ToastTool.showText(this, "摄像头状态异常");
			}
			break;
		case R.id.right_btn:
			Intent exitIntent = new Intent(VisitMainActivity.this,
					SettingWifiActivity.class);
			startActivity(exitIntent);
			break;
		case R.id.left_btn:
			finish();
			break;
		}

	}
	int xxx=0;
	// ---------------------------------------------------
	@Override
	public void callBackCameraParamNotify(String did, int resolution,
			int brightness, int contrast, int hue, int saturation, int flip) {
		// TODO Auto-generated method stub

	}

	Handler imageHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {

				if (msg.what == 1) {
					byte[] rgb = new byte[nVideoWidths * nVideoHeights * 2];
					NativeCaller.YUV4202RGB565(videodata, rgb, nVideoWidths,
							nVideoHeights);
					ByteBuffer buffer = ByteBuffer.wrap(rgb);
					rgb = null;
					mBmp = Bitmap.createBitmap(nVideoWidths, nVideoHeights,
							Bitmap.Config.RGB_565);
					mBmp.copyPixelsFromBuffer(buffer);
					DisplayMetrics DM = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(DM);
					int width = DM.widthPixels;
					int height = DM.heightPixels;
					Bitmap bitmap = null;
					if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
						FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
								nVideoWidths, nVideoHeights * 3 / 4);
						lp.gravity = Gravity.CENTER;
						bitmap = Bitmap
								.createScaledBitmap(mBmp, 320, 240, true);
					} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
						FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
								width, height);
						lp.gravity = Gravity.CENTER;
						bitmap = Bitmap.createScaledBitmap(mBmp, width, height,
								true);
					}
					imageView.setImageBitmap(bitmap);
					imageView.setVisibility(View.VISIBLE);
					LogTool.i(num);
//					if (num == 10) {
//						num = 0;
//					}
					utils.saveBitmap(bitmap,0);
					num =utils.getBtmapCount(1);
					if(num>10){
						utils.deleteImage();
					}
					progressBar.setVisibility(View.GONE);
					num_text.setText("当前"+utils.getTime2());
				}
				if (msg.what == 2) {
					adapter.notifyDataSetChanged();
					indicatorContainer.removeAllViews();
					for(int i=0;i<array.size();i++)
					{
						ImageView indicator = new ImageView(getApplicationContext());
						indicator.setPadding(0, 0, 10, 0);
						if (i == 0) {
							indicator.setImageResource(R.drawable.indicator_dot_selected);
						} else {
							indicator.setImageResource(R.drawable.indicator_dot_normal);
						}
						indicatorContainer.addView(indicator, i);
					}
					
//					LogTool.i("上一张图片");
//					LogTool.i("缓存当前下标 = " + num);
////					xxx= utils.getBtmapCount();
////					xxx--;
////					if (num == 0) {
////						num = utils.getBtmapCount();
////					}
////					int x = num--;
////					LogTool.i(x);
////					if(x==10){
////						x=x-1;
////					}
//					LogTool.i("wwwww==="+num);
//					
//					Bitmap bitmap = utils.readBitmap(num--);
//					if(bitmap == null){
//						LogTool.i("null");
//						return;
//					}
//					LogTool.i("wwwwwdddd=="+num);
//					imageView.setImageBitmap(bitmap);
//					imageView.setVisibility(View.VISIBLE);
//					num_text.setText("第" + num + "张");
					
					
				}
			} catch (Exception e) {
				NativeCaller.StopPPPP(SystemValue.deviceId);
			}
		}

	};

	@Override
	public void callBaceVideoData(byte[] videobuf, int h264Data, int len,
			int width, int height) {
		videodata = videobuf;
		nVideoWidths = width;
		nVideoHeights = height;
		Message msg = new Message();
		if (h264Data == 1) {
			if (isTakepic) {
				isTakepic = false;
				byte[] rgb = new byte[width * height * 2];
				NativeCaller.YUV4202RGB565(videobuf, rgb, width, height);
				ByteBuffer buffer = ByteBuffer.wrap(rgb);
				mBmp = Bitmap
						.createBitmap(width, height, Bitmap.Config.RGB_565);
				mBmp.copyPixelsFromBuffer(buffer);
				msg.what = h264Data;
				imageHandler.sendMessage(msg);
			}
		}
	}

	@Override
	public void callBackMessageNotify(String did, int msgType, int param) {
	}

	@Override
	public void callBackAudioData(byte[] pcm, int len) {
	}

	@Override
	public void callBackH264Data(byte[] h264, int type, int size) {
	}

	@Override
	protected void onDestroy() {
		LogTool.i("退出当前页");
//		LocalBitmapManage.saveBitmap();
//		utils.saveBitmap();
		super.onDestroy();
		NativeCaller.StopPPPP(SystemValue.deviceId);
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
			String msg = result.getString("msg");
			if(code.equals("2")){
				JSONObject rs = result.getJSONObject("rs");
				SystemValue.deviceId= rs.getString("businessCode");
				LogTool.i("摄像头编码 = "+SystemValue.deviceName);
//				SystemValue.deviceName = "admin";
//				SystemValue.devicePass = "888888";
				BridgeService.setIpcamClientInterface(this);
				NativeCaller.Init();
				new Thread(new StartPPPPThread()).start();
				int remainingDays = rs.getInt("remainingDays");
				if(remainingDays<30)
				{
					ToastTool.showText(this, "可使用时间"+remainingDays+"天");
				}
			}else {
				ToastTool.showText(this, msg);
				finish();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void AlertDialog(String info) {
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
				this);
		builder.setIcon(R.drawable.app);
		builder.setTitle(getResources().getString(R.string.exit) + "̽提示信息");
		builder.setMessage(info);
		builder.setPositiveButton(R.string.str_ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Intent intent = new Intent(VisitMainActivity.this,
								JFActivity.class);
						startActivity(intent);
						VisitMainActivity.this.finish();
					}
				});
		builder.setNegativeButton(R.string.str_cancel, null);
		builder.show();
	}

	@Override
	protected void dataError(VolleyError error) {
		LogTool.e(error.toString());
	}

	private class MyBroadCast extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			VisitMainActivity.this.finish();
			Log.d("ip", "AddCameraActivity.this.finish()");
		}

	}

	@Override
	protected void onRestart() {
		BridgeService.setPlayInterface(VisitMainActivity.this);
		super.onRestart();
	}

}
