package com.qy.ccm.utils.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.qy.ccm.app.MyApp;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * SharedPreferences
 */
public class SpUtils {
    private static SharedPreferences preferences;

    public static void getPreference(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences("wtokensp", Context.MODE_PRIVATE);
        }
    }

    /**
     * @param context 上下文 最好用Application的context
     * @param key     属性名
     * @param value   属性值
     */
    public static void putBoolean(Context context, String key, boolean value) {
        getPreference(context);
        preferences.edit().putBoolean(key, value).commit();
    }

    /**
     * @param context      上下文 最好用Application的context
     * @param key          属性名
     * @param defaultValue 属性值 默认
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        getPreference(context);
        return preferences.getBoolean(key, defaultValue);
    }

    /**
     * @param context 上下文 最好用Application的context
     * @param key     属性名
     * @param value   属性值
     */
    public static void putFloat(Context context, String key, float value) {
        getPreference(context);
        preferences.edit().putFloat(key, value).commit();
    }

    /**
     * @param context      上下文 最好用Application的context
     * @param key          属性名
     * @param defaultValue 属性值 默认
     */
    public static float getFloat(Context context, String key, float defaultValue) {
        getPreference(context);
        return preferences.getFloat(key, defaultValue);
    }

    /**
     * @param context 上下文 最好用Application的context
     * @param key     属性名
     * @param value   属性值
     */
    public static void putString(Context context, String key, String value) {
        getPreference(context);
        preferences.edit().putString(key, value).commit();
    }

    /**
     * @param context      上下文 最好用Application的context
     * @param key          属性名
     * @param defaultValue 属性值 默认
     */
    public static String getString(Context context, String key, String defaultValue) {
        getPreference(MyApp.getSingleInstance());
        return preferences.getString(key, defaultValue);
    }

    /**
     * @param context 上下文 最好用Application的context
     * @param key     属性名
     * @param value   属性值
     */
    public static void putLong(Context context, String key, long value) {
        getPreference(context);
        preferences.edit().putLong(key, value).commit();
    }

    /**
     * @param context      上下文 最好用Application的context
     * @param key          属性名
     * @param defaultValue 属性值 默认
     */
    public static long getLong(Context context, String key, long defaultValue) {
        getPreference(context);
        return preferences.getLong(key, defaultValue);
    }

    /**
     * @param key   属性名
     * @param value 属性值
     */
    public static void putInt(String key, int value) {
        getPreference(MyApp.getSingleInstance());
        preferences.edit().putInt(key, value).commit();
    }

    /**
     * @param key          属性名
     * @param defaultValue 属性值 默认
     */
    public static int getInt(String key, int defaultValue) {
        getPreference(MyApp.getSingleInstance());
        return preferences.getInt(key, defaultValue);
    }


    /**
     * 保存list数据
     *
     * @param key  key
     * @param list 属性值
     */
    public static void putStringList(String key, List<String> list) {
        SharedPreferences prefs = MyApp.getSingleInstance().getSharedPreferences(key, Context.MODE_PRIVATE);
        JSONArray jsonArray = new JSONArray();
        if (list != null && !list.isEmpty()) {
            for (String str : list) {
                jsonArray.put(str);
            }
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, jsonArray.toString());
        editor.commit();
    }

    /**
     * 获取list数据
     *
     * @param key key
     */
    public static List<String> getStringList(String key) {
        SharedPreferences prefs = MyApp.getSingleInstance().getSharedPreferences(key, Context.MODE_PRIVATE);
        List<String> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(prefs.getString(key, "[]"));
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}