package com.llh.activity;

import java.nio.ByteBuffer;

import vstc2.nativecaller.NativeCaller;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ipcamer.demo.BridgeService;
import com.ipcamer.demo.BridgeService.PlayInterface;
import com.ipcamer.demo.R;
import com.ipcamer.demo.SystemValue;
import com.llh.base.BaseActivity;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;

public class ImageActivity extends BaseActivity implements PlayInterface{
	@ViewInject(id =R.id.load_image_progress1)
	private View progressView;
	@ViewInject(id = R.id.show_image1)
	private ImageView imageView;
	@ViewInject(id=R.id.btn_next1,click="onClick")
	private Button btn_next;
	/**是否拍照*/
	private boolean isTakepic = true;
	/**视频数据 */
	private byte[] videodata = null;
	public int nVideoWidths = 0;
	public int nVideoHeights = 0;
	private Bitmap mBmp;
	public void onClick(View v){
		LogTool.d("下一张");
		progressView.setVisibility(View.VISIBLE);
		isTakepic = true;
	}
	@Override
	public void initView() {
		BridgeService.setPlayInterface(this);
		//启动流
		NativeCaller.StartPPPPLivestream(SystemValue.deviceId, 10);
	}

	@Override
	public int setLayout() {
		return R.layout.image_activity;
	}
	Handler imageHandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what == 1){
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
					bitmap = Bitmap.createScaledBitmap(mBmp, 320,
							240, true);
					
					LogTool.d("竖屏");
					
				} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
					FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
							width, height);
					lp.gravity = Gravity.CENTER;
					bitmap = Bitmap
							.createScaledBitmap(mBmp, width, height, true);
					LogTool.d("横屏");
				}				
				imageView.setImageBitmap(bitmap);
				imageView.setVisibility(View.VISIBLE);
				progressView.setVisibility(View.GONE);
			}
		}
	};
	@Override
	public void callBackCameraParamNotify(String did, int resolution,
			int brightness, int contrast, int hue, int saturation, int flip) {
		
	}

	@Override
	public void callBaceVideoData(byte[] videobuf, int h264Data, int len,
			int width, int height) {
		videodata = videobuf;
		nVideoWidths = width;
		nVideoHeights = height;
		Message msg = new Message();
		if (h264Data == 1){
			if(isTakepic){
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void callBackAudioData(byte[] pcm, int len) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void callBackH264Data(byte[] h264, int type, int size) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onDestroy() {
		NativeCaller.StopPPPPLivestream(SystemValue.deviceId);
		super.onDestroy();
	}

}
