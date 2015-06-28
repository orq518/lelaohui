package com.llh.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.tool.utils.LogTool;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LocalBitmapManage {
	private static final String picName = "image";
	private static ArrayList<Bitmap> arr = new ArrayList<Bitmap>();
	public static void CacheBitmap(Bitmap b){
		if(arr.size() == 10){
			arr.remove(0);
		}
		arr.add(b);
	}
	public static void readAllBitmap() {
		try {
			File f = new File("/sdcard/namecard/");
			if (f.isDirectory()) {
				File[] childFiles = f.listFiles();
				if (childFiles == null || childFiles.length == 0) {
					return;
				}
				for (int i = 0; i < childFiles.length; i++) {
					
					LogTool.i(childFiles[i].getName());
					Bitmap b = readBitmap(childFiles[i].getName());
					arr.add(b);
				}
			}
		} catch (Exception e) {
			LogTool.i(e.toString());
			e.printStackTrace();
		}
	}
	public static Bitmap readBitmap(String name) {
		String img = "/sdcard/namecard" + name;
		LogTool.i("读取图片名称 = " + img);
		try {
			FileInputStream fis = new FileInputStream(img);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static int getCacheCount(){
		return arr.size();
	}
	public static Bitmap ReadCache(int i){
		return arr.get(i);
	}
	/** 保存方法 */
	public static void saveBitmap(){
		new Thread(){
			public void run() {
				LogTool.i("保存图片");
				for(int i = 0;i<=arr.size();i++){
					saveBitmap(arr.get(i), i);
				}
				LogTool.i("保存图片结束");
			};
		}.start();
		
	}
	/** 保存方法 */
	private static void saveBitmap(Bitmap bm, int num) {
//		LogTool.i("保存图片");
		String img = picName + num + ".png";
		LogTool.i("图片名称 = " + img);
		File f = new File("/sdcard/namecard/",img);

		try {
			f.mkdirs();
			if (f.exists()) {
				LogTool.i("删除");
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			LogTool.i("已经保存");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
