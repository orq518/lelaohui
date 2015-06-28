package com.llh.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import com.ipcamer.app.MyApplication;
import com.llh.net.SysVar;
import com.tool.utils.LogTool;

public class utils {

	// private static final String folderPath = ALBUM_PATH;
	private final static String ALBUM_PATH = getBitmapStoragePath(MyApplication
			.getMyApplication()) + "/namecard/";
	private final static String LOG_PATH = getBitmapStoragePath(MyApplication
			.getMyApplication());
	public static ArrayList<Bitmap> arr = new ArrayList<Bitmap>(10);

	public static void deleteImage() {
		String name = readName();
		String img = ALBUM_PATH + name;
		LogTool.i(img);
		try {
			File f = new File(img);
			if (f.delete()) {
				LogTool.i("图片删除成功");
			}
		} catch (Exception e) {
			LogTool.i(e.toString());
			e.printStackTrace();
		}
	}

	public static String readName() {
		try {
			File f = new File(ALBUM_PATH);
			if (f.isDirectory()) {
				File[] childFiles = bubbleSort(f.listFiles());
				if (childFiles == null || childFiles.length == 0) {
					return "";
				}
				return childFiles[childFiles.length-1].getName();
			}
		} catch (Exception e) {
			LogTool.i(e.toString());
			e.printStackTrace();
		}
		return "";

	}

	public static ArrayList<Bitmap> readAllBitmap() {
		try {
			File f = new File(ALBUM_PATH);
			arr.clear();
			if (f.isDirectory()) {
				File[] childFiles = f.listFiles();
				if (childFiles == null || childFiles.length == 0) {
					return null;
				}
				childFiles = bubbleSort(childFiles);

				for (int i = 0; i < childFiles.length; i++) {
					LogTool.i(childFiles[i].getName());
					if (i < 10) {
						Bitmap b = readBitmap(childFiles[i].getName());
						arr.add(b);
					} else {
//						break;
						childFiles[i].delete();
					}
				
					
				}
			}

			return arr;
		} catch (Exception e) {
			LogTool.i(e.toString());
			e.printStackTrace();
		}
		return null;
	}

	public static void saveIamge(Bitmap bm) {
		LogTool.i(arr.size());
		if (arr.size() > 0 && arr.size() < 10) {
			arr.remove(0);
		}
		arr.add(bm);
	}

	public static Bitmap lastBitmap(int count) {
		int num = arr.size();
		if (num >= count)
			return arr.get(count);
		return null;
	}

	public static Bitmap readBitmap(String name) {
		String img = ALBUM_PATH + File.separator + name;
		LogTool.i("读取图片名称 = " + img);
		try {
			FileInputStream fis = new FileInputStream(img);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap readBitmaps(String name) {
		String img = ALBUM_PATH + name;
		LogTool.i("读取图片名称 = " + img);
		try {
			FileInputStream fis = new FileInputStream(img);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap readBitmap(int num) {
		ArrayList<String> arrs = new ArrayList<String>();
		try {
			File f = new File(ALBUM_PATH);
			if (f.isDirectory()) {
				File[] childFiles = f.listFiles();
				if (childFiles == null || childFiles.length == 0) {
					return null;
				}
				for (int i = 0; i < childFiles.length; i++) {
					arrs.add(childFiles[i].getName());
				}
			}
		} catch (Exception e) {
			LogTool.i(e.toString());
			e.printStackTrace();
		}
		String name = arrs.get(num);
		String img = ALBUM_PATH + name;
		LogTool.i("读取图片名称 = " + img);
		try {
			FileInputStream fis = new FileInputStream(img);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void saveBitmap() {
		new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < arr.size(); i++) {
					saveBitmap(arr.get(i), i);
				}
			}
		}.start();

	}

	public static int getBtmapCount(int i) {
		android.util.Log.d("xcq", "i: " + i);
		try {
			File f = new File(ALBUM_PATH);
			if (f.isDirectory()) {
				File[] childFiles = f.listFiles();
				if (childFiles == null || childFiles.length == 0) {
					return 0;
				} else {
					return childFiles.length;
				}
			}
		} catch (Exception e) {
			LogTool.i(e.toString());
			e.printStackTrace();
			return 0;
		}
		return 0;
	}

	public static void deleteBtmap() {
		try {
			File f = new File(ALBUM_PATH);
			if (f.isDirectory()) {
				File[] childFiles = f.listFiles();
				if (childFiles == null || childFiles.length == 0) {
					return;
				}
				for (int i = 0; i < childFiles.length; i++) {
					childFiles[i].delete();
				}
				LogTool.i("删除成功");
			}
		} catch (Exception e) {
			LogTool.i(e.toString());
			e.printStackTrace();
		}
	}

	public static String getTime2() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	public static String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}
    public static String getTimeFromCurrentTimeMills(long time){
    	Date d = new Date(time);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
    	return sdf.format(d);
    }
	/** 保存方法 */
	public static void saveBitmap(Bitmap bm, int num) {
		LogTool.i("保存图片");
		String img = System.currentTimeMillis() + ".png";
		LogTool.i("图片名称 = " + img);
		String path = ALBUM_PATH;
		File f = new File(path);

		try {
			if (!f.exists()) {
				f.mkdirs();
			}
			File imageFile = new File(f, img);
			imageFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(imageFile);
			bm.compress(CompressFormat.PNG, 90, fos);
			fos.flush();
			fos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static File[] bubbleSort(File[] childFiles) {
		if(null==childFiles||childFiles.length==0)
			return childFiles;
		File temp = null;
		for (int i = 0; i < childFiles.length - 1; i++) {
			for (int j = 0; j < childFiles.length - 1 - i; j++) {
				if (Long.parseLong(childFiles[j].getName().substring(0, childFiles[j].getName().indexOf("."))) < Long
						.parseLong(childFiles[j+1].getName().substring(0, childFiles[j+1].getName().indexOf(".")))) {
					temp = childFiles[j];
					childFiles[j] = childFiles[j + 1];
					childFiles[j + 1] = temp;
				}
			}
		}
		return childFiles;
	}

	public static Object getUserParam(Context context, String key) {
		Map<String, Object> map = SysVar.getInstance(context).getUserInfo();
		return map.get(key);
	}

	public static String getBitmapStoragePath(Context context) {

		int currentapiVersion = Build.VERSION.SDK_INT;
		String PATH_FLAG = "map_base_path";
		if (currentapiVersion > 18)
			PATH_FLAG = "map_base_path_v44";

		SharedPreferences sp = context.getSharedPreferences("base_path",
				Context.MODE_PRIVATE);
		String map_base_path = sp.getString(PATH_FLAG, "");
		if (map_base_path != null && map_base_path.length() > 2) {
			File file = new File(map_base_path);
			if (file.isDirectory()) {
				if (file.canWrite()) {
					createNoMediaFileIfNotExist(map_base_path);
					return map_base_path;
				} else {
					map_base_path = getCacheDir().toString();
					if (map_base_path != null && map_base_path.length() > 2) {
						file = new File(map_base_path);
						if (file.isDirectory()) {
							// createNoMediaFileIfNotExist(map_base_path);
							return map_base_path;
						}
					}
				}
			}
		}

		map_base_path = getExternalStroragePath(context);
		if (map_base_path != null && map_base_path.length() > 2) {
			File file = new File(map_base_path);
			if (file.isDirectory()) {
				Editor editor = sp.edit();
				editor.putString(PATH_FLAG, map_base_path);
				editor.commit();

				createNoMediaFileIfNotExist(map_base_path);
				return map_base_path;
			}
		}

		map_base_path = getCacheDir().toString();
		if (map_base_path != null && map_base_path.length() > 2) {
			File file = new File(map_base_path);
			if (file.isDirectory()) {

				// createNoMediaFileIfNotExist(map_base_path);
				// Editor editor = sp.edit();
				// editor.putString("map_base_path", map_base_path);
				// editor.commit();
				return map_base_path;
			}
		}
		return map_base_path;
	}

	/**
	 * 在sd卡应用目录下创建.nomedia文件，使得应用中的图片不被系统图库扫描
	 * 
	 * @param filePath
	 */
	public static void createNoMediaFileIfNotExist(String filePath) {
		// 此处try catch 不能抛出
		try {

			String appFilePath = filePath;
			File file = new File(appFilePath + "/autonavi/.nomedia");
			if (!file.exists()) {
				file.createNewFile(); // 创建.nomedia文件，使得应用中的图片不被系统图库扫描
			}

			// 解决android系统的bug(如果目录下的图片在创建.nomedia之前已经被图库收录了，创建之后也不会从图库中消失)
			long mtime = file.lastModified();
			// Calendar cal = Calendar.getInstance();
			// cal.set(1970, 0, 1, 8, 0, 0); // 将时间设置成1970-1-1 8:00:00
			// cal.getTimeInMillis();
			long time = 0;
			if (mtime > time) {
				file.setLastModified(time); // 将文件时间修改成1970-1-1 8:00:00
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static File getCacheDir() {
		// Application app =getApplication();
		// File result = app.getCacheDir();
		// if (result == null) {
		// result = app.getDir("cache", 0);
		// }
		// if (result == null) {
		// result = new File("/data/data/"+ app.getPackageName() +"/app_cache");
		// }
		// if (!result.exists()) {
		// result.mkdirs();
		// }
		// return result;
		return new File("");
	}

	/**
	 * 获取外置SD路径
	 * 
	 * @param context
	 * @return
	 */
	@SuppressLint("NewApi")
	public static String getExternalStroragePath(Context context) {

		int currentapiVersion = Build.VERSION.SDK_INT;
		if (currentapiVersion >= 12) {// 12 is HONEYCOMB_MR1
			try {
				StorageManager manager = (StorageManager) context
						.getSystemService(Context.STORAGE_SERVICE);
				/************** StorageManager的方法 ***********************/
				Method getVolumeList = StorageManager.class.getMethod(
						"getVolumeList", null);
				Method getVolumeState = StorageManager.class.getMethod(
						"getVolumeState", String.class);

				Object[] Volumes = (Object[]) getVolumeList.invoke(manager,
						null);
				String state = null;
				String path = null;
				Boolean isRemove = false;
				String sdPath = "";
				String innerPath = "";
				String sdState = "";
				String innerState = "";
				String storageDir = null;
				for (Object volume : Volumes) {
					/************** StorageVolume的方法 ***********************/
					Method getPath = volume.getClass().getMethod("getPath",
							null);
					Method isRemovable = volume.getClass().getMethod(
							"isRemovable", null);
					path = (String) getPath.invoke(volume, null);

					state = (String) getVolumeState.invoke(manager,
							getPath.invoke(volume, null));
					isRemove = (Boolean) isRemovable.invoke(volume, null);

					// 三星S5存储卡分区问题
					if (path.toLowerCase(Locale.US).contains("private")) {
						continue;
					}
					if (isRemove) {
						sdPath = path;
						sdState = state;
						// 如果sd卡路径存在
						if (null != sdPath && null != sdState
								&& sdState.equals(Environment.MEDIA_MOUNTED)) {

							if (currentapiVersion <= 18) {
								storageDir = sdPath;
							} else {
								try {
									File files = context.getExternalFilesDir(null);
									if (files != null) {
										storageDir = files
												.getAbsolutePath();
//										if (files.length > 1
//												&& null != files[1])
//											storageDir = files[1]
//													.getAbsolutePath();
//										else
//											storageDir = path;
									}
								} catch (Throwable ex) {
									// 此处保护java.lang.NoSuchMethodError:
									// android.content.Context.getExternalFilesDirs
									storageDir = sdPath;
								}
							}
							break;
						}
					} else {
						innerPath = path;
						innerState = state;
					}
				}

				// 如果sd卡路径为null,检测内部存储空间
				if (currentapiVersion <= 18) {// 18 is JELLY_BEAN_MR2
					if (null == storageDir && null != innerPath) {
						if (null != innerState
								&& innerState.equals(Environment.MEDIA_MOUNTED)) {
							storageDir = innerPath;
						}
					}
					return storageDir;
				} else {// 4.4以上系统有限内部存储卡
					if (null != innerPath) {
						if (null != innerState
								&& innerState.equals(Environment.MEDIA_MOUNTED)) {
							storageDir = innerPath;
						}
					}
					return storageDir;
				}
			} catch (Exception e) {
			}
		}

		{
			// 得到存储卡路径
			File sdDir = null;
			boolean sdCardExist = Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED); // 判断sd卡
			// 或可存储空间是否存在
			if (sdCardExist) {
				sdDir = Environment.getExternalStorageDirectory();// 获取sd卡或可存储空间的跟目录
				return sdDir.toString();
			}

			return null;
		}
	}

	public static void writeLogToFile(Context context, String content) {
		String aFileName = "lelaohuiLog.txt";
		if (!TextUtils.isEmpty(aFileName) && !TextUtils.isEmpty(content)) {
			File logFile = new File(LOG_PATH + File.separator+aFileName);
			Date currentTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String dateString = formatter.format(currentTime);
			content = dateString + "|" + "  " + content;
			final File file = logFile;
			final String logContent = content;
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						FileWriter writer = new FileWriter(file, true);
						writer.write(logContent);
						writer.write("\r\n-------------------\r\n");
						writer.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
	
	public static String readFile() {
		File file = new File(ALBUM_PATH+"json.txt");
		InputStreamReader inputReader = null;
		BufferedReader bufferReader = null;
		try {
			InputStream inputStream = new FileInputStream(file);
			inputReader = new InputStreamReader(inputStream);
			bufferReader = new BufferedReader(inputReader);

			// 读取一行
			String line = null;
			StringBuffer strBuffer = new StringBuffer();

			while ((line = bufferReader.readLine()) != null) {
				strBuffer.append(line);
			}
			return strBuffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferReader.close();
				inputReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		return "";
	}
	/**
	 * 手机号验证
	 * 
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) { 
		Pattern p = null;
		Matcher m = null;
		boolean b = false; 
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches(); 
		return b;
	}
	/**
	 * 电话号码验证
	 * 
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) { 
		Pattern p1 = null,p2 = null;
		Matcher m = null;
		boolean b = false;  
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
		if(str.length() >9)
		{	m = p1.matcher(str);
 		    b = m.matches();  
		}else{
			m = p2.matcher(str);
 			b = m.matches(); 
		}  
		return b;
	}
}
