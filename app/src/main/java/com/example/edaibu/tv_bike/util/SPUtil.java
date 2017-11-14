package com.example.edaibu.tv_bike.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ${gyj} on 2017/9/20.
 */

public class SPUtil {
    //骑行距离
    public final static String SP_DISTANCE = "distance";
    //骑行路径
    public final static String SP_ROUTE = "route";
    public final static String USERMESSAGE = "usermessage";
    private static SPUtil sharUtil;
    private final SharedPreferences shar;
    private final SharedPreferences.Editor editor;
    /**
     * 保存在手机里面的名字
     */
    public static final String FILE_NAME = "share";
    @SuppressLint("WrongConstant")
    public SPUtil(Context context) {
        shar = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = shar.edit();
    }

    public static SPUtil getInstance(Context context) {
        if (null == sharUtil) {
            sharUtil = new SPUtil(context);
        }
        return sharUtil;
    }


    //添加String信息
    public void addString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    //添加int信息
    public void addInt(String key, Integer value) {
        editor.putInt(key, value);
        editor.commit();
    }

    //添加boolean信息
    public void addBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    //添加float信息
    public void addFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public void removeMessage(String delKey) {
        editor.remove(delKey);
        editor.commit();
    }

    public String getString(String key) {
        return shar.getString(key, "");
    }

    public Integer getInteger(String key) {
        return shar.getInt(key, 0);
    }

    public boolean getBoolean(String key) {
        return shar.getBoolean(key, false);
    }


}
