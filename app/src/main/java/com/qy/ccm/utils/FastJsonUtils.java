package com.qy.ccm.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * JSON解析工具
 */
public class FastJsonUtils {

    /**
     * 功能描述：把JSON数据转换成普通字符串列表
     */
    public static List<String> getStringList(String jsonData) throws Exception {
        return JSON.parseArray(jsonData, String.class);
    }

    /**
     * 功能描述：把JSON数据转换成普通字符串列表
     */
    public static List<String> getStringList(String jsonData, String value) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        JSONArray jsonValue = jsonObject.getJSONArray(value);
        return JSON.parseArray(jsonValue.toString(), String.class);
    }

    /**
     * 功能描述：把JSON数据转换成指定的java对象
     */
    public static <T> T getSingleBean(String jsonData, Class<T> clazz) {
        return JSON.parseObject(jsonData, clazz);
    }

    /**
     * 功能描述：把JSON数据转换成指定的java对象列表
     *
     */
    public static <T> List<T> getBeanList(String jsonData, Class<T> clazz) {
        return JSON.parseArray(jsonData, clazz);
    }

    /**
     * 功能描述：把JSON数据转换成较为复杂的java对象列表
     */
    public static List<Map<String, Object>> getBeanMapList(String jsonData) {
        return JSON.parseObject(jsonData, new TypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * 功能描述：根据key把JSON数据转换成集合形式的列表
     * 对应的格式：list:[{},{},{}]
     */
    public static List<Map<String, Object>> getList(String jsonData, String value) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        JSONArray jsonValue = jsonObject.getJSONArray(value);
        return JSON.parseObject(jsonValue.toString(),
                new TypeReference<List<Map<String, Object>>>() {
                });
    }

    /**
     * 将json 数组转换为Map 对象.
     *
     */

    public static Map<String, Object> getMap(String jsonString) {
        org.json.JSONObject jsonObject;
        try {
            jsonObject = new org.json.JSONObject(jsonString);
            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, Object> valueMap = new HashMap<>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (JSONException e) {
            Log.i("TAG", "can not parse json:" + jsonString);
            e.printStackTrace();
        }
        return null;
    }
}
