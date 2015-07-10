package com.llh.utils;

import android.content.Context;

import com.ipcamer.app.MyApplication;
import com.llh.camera.activity.AddressAtivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * The author ou on 2015/7/10.
 */
public class DataTools {

    public static final String FILENAME = "llhdate";

    /**
     * 写文件
     * @param
     * @throws IOException
     */
    public static void writeData(Context context,HashMap<String, Object> dataMap)
            throws IOException {
        // Log.e("ouou", "开始写数据:" + name);
        String userName = SharedPreferenceUtil.getUserInfo("user_name",context);
       String fileName=userName+FILENAME;
        context.deleteFile(fileName);
        FileOutputStream fos = context.openFileOutput(
                fileName, Context.MODE_PRIVATE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); // 构造一个字节输出流
        ObjectOutputStream oos = new ObjectOutputStream(baos); // 构造一个类输出流

        try {
            oos.writeObject(dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray(); // 从这个地层字节流中把传输的数组给一个新的数组
        oos.flush();
        fos.write(buf);
        fos.close();
        oos.close();
        baos.close();
    }

    /**
     * 读文件
     *
     * @param
     */
    @SuppressWarnings("unchecked")
    public static HashMap<String, Object> readData(Context context) {
        String userName = SharedPreferenceUtil.getUserInfo("user_name",context);
        String fileName=userName+FILENAME;
        HashMap<String, Object> datamap = new HashMap<String, Object>();
        FileInputStream fos = null;
        try {
            fos =context.openFileInput(fileName);
        } catch (Exception e1) {
            return datamap;
        }

        try {
            if (fos.available() < 0) {
                fos.close();
                return datamap;
            }

            byte[] buf = new byte[fos.available()];
            fos.read(buf);

            // 构建一个类输入流，地层用字节输入流实现
            ByteArrayInputStream bais = new ByteArrayInputStream(buf);
            ObjectInputStream ois = new ObjectInputStream(bais);
            datamap = (HashMap<String, Object>) ois.readObject();
            fos.close();
            bais.close();
            ois.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return datamap;
    }

    /**
     * 删除文件
     */
    public static void clearAllData(Context context) {
        String userName = SharedPreferenceUtil.getUserInfo("user_name",context);
        String fileName=userName+FILENAME;
        context.deleteFile(fileName);
    }
}
