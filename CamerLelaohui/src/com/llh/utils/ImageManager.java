/**
 * 
 */
package com.llh.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.yh.materialdesign.utils.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: ImageManager
 * @Description: TODO
 * @author ou
 * @date 2015年1月13日 上午10:26:50
 * 
 */
public class ImageManager {
	static ImageManager iManager;
	private ImageMemoryCache imageMemoryCache; // 内存缓存
	private ImageFileCache imageFileCache; // 文件缓存
	ExecutorService threadPool;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};

	public static ImageManager getInstance(Context context) {
		if (iManager == null) {
			iManager = new ImageManager(context);
		}
		return iManager;
	}

	public ImageManager(Context context) {
		imageMemoryCache = new ImageMemoryCache(context);
		imageFileCache = new ImageFileCache();
		threadPool = Executors.newFixedThreadPool(2);
	}

	// =============================获取图片========================================
	/*** 获得一张图片,从三个地方获取,首先是内存缓存,然后是文件缓存,最后从网络获取 ***/
	public void getBitmap(final String url, final ImageCallBack callback,final ImageView imageview) {
		if (utils.isEmpty(url)) {
			callback.loadImage(imageview,null);
			return;
		}
		// 从内存缓存中获取图片
		Bitmap result = imageMemoryCache.getBitmapFromCache(url);
		if (result == null) {
			// 文件缓存中获取
			result = imageFileCache.getImage(url);
			if (result == null) {
				threadPool.submit(new Runnable() {
					@Override
					public void run() {
						// 从网络获取
						final Bitmap bitmap = ImageGetFromHttp
								.downloadBitmap(url);
						
						if (bitmap != null) {
							mHandler.post(new Runnable() {
								public void run() {
									callback.loadImage(imageview,bitmap);
								}
							});
							imageFileCache.saveBitmap(url, bitmap);
							imageMemoryCache.addBitmapToCache(url, bitmap);
						}
					}
				});

			} else {
				// 添加到内存缓存
				imageMemoryCache.addBitmapToCache(url, result);
			}
		}
		callback.loadImage(imageview,result);
	}

	public interface ImageCallBack {
		void loadImage(ImageView imageView, Bitmap bitmap);
	}
}

// class BitmapUtil {
//
// /**
// * 计算SampleSize
// *
// * @Title computeImageSampleSize
// * @Description TODO
// * @param width
// * @param height
// * @param limitWidth
// * @param limitHeight
// * @param viewScaleType
// * @param powerOf2Scale
// * @return int
// */
// public static int computeImageSampleSize(int width, int height,
// int limitWidth, int limitHeight, int viewScaleType,
// boolean powerOf2Scale) {
//
// int scale = 1;
//
// int widthScale = width / limitWidth;
// int heightScale = height / limitHeight;
//
// switch (viewScaleType) {
// case 1:// FIT_INSIDE
// if (powerOf2Scale) {
// while (width / 2 >= limitWidth || height / 2 >= limitHeight) { // ||
// width /= 2;
// height /= 2;
// scale *= 2;
// }
// } else {
// scale = Math.max(widthScale, heightScale); // max
// }
// break;
// case 2:// CROP
// if (powerOf2Scale) {
// while (width / 2 >= limitWidth && height / 2 >= limitHeight) { // &&
// width /= 2;
// height /= 2;
// scale *= 2;
// }
// } else {
// scale = Math.min(widthScale, heightScale); // min
// }
// break;
// }
//
// if (scale < 1) {
// scale = 1;
// }
//
// return scale;
// }
// }
