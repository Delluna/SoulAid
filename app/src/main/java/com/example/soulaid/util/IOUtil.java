package com.example.soulaid.util;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

public class IOUtil {

    public static String getUserType(Context context) {
        String path = "userType.txt";

        //如果存在文件“userType.txt”则返回字符串，否则返回"".
        if (IOUtil.read(context, path) != null) {
            return IOUtil.read(context, path);
        }
        return "";

    }

    public static boolean setUserType(Context context, String type) {
        String path = "userType.txt";
        boolean state = save(context,type,path);
        return state;
    }

    public static String[] getUserInfo(Context context) {
        String path = "userInfo.txt";
        String result = IOUtil.read(context, path);
        String[] r = new String[2];
        r[0] = result.split(";")[0];
        r[1] = result.split(";")[1];
        return r;
    }

    public static boolean save(Context context, String userMessage, String path) {
        boolean state = false;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(path, MODE_PRIVATE);
            fileOutputStream.write(userMessage.getBytes());
            state = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return state;
    }

    public static String read(Context context, String path) {

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = context.openFileInput(path);
            byte[] bytes = new byte[1024];
            StringBuilder userInfo = new StringBuilder("");
            int len = 0;
            while ((len = fileInputStream.read(bytes)) > 0) {
                userInfo.append(new String(bytes, 0, len));
            }
            return userInfo.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
