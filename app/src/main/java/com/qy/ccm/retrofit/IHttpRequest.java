package com.qy.ccm.retrofit;

import android.content.Context;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2018/1/23.
 */

public interface IHttpRequest {

    // Get请求
    void mHttpGet(Context context, String api, int type, HttpRequestCallback mCallBack);

    // Get请求
    void mHttpGet(Context context, String api, TreeMap<String, Object> map, int type, HttpRequestCallback mCallBack);

    // Get请求
    void mHttpGet(Context context, TreeMap<String, Object> map, int type, HttpRequestCallback mCallBack);

    // Post请求
    void mHttpPost(Context context, String api, int type, HttpRequestCallback mCallBack);

    // Post请求
    void mHttpPost(Context context, String api, TreeMap<String, Object> map, int type, HttpRequestCallback mCallBack);

    // Post请求
    void mHttpPost(Context context, TreeMap<String, Object> map, int type, HttpRequestCallback mCallBack);

    // Post请求(包含数组)
    void mHttpPost(Context context, TreeMap<String, Object> treeMap, String[] data, int type, HttpRequestCallback mCallBack);

    //    ===================================================================
    // Get请求
    void mHttpLoginGet(Context context, String api, int type, HttpRequestCallback mCallBack);

    // Get请求
    void mHttpLoginGet(Context context, String api, TreeMap<String, Object> map, int type, HttpRequestCallback mCallBack);

    // Get请求
    void mHttpLoginGet(Context context, TreeMap<String, Object> map, int type, HttpRequestCallback mCallBack);

    // Post请求
    void mHttpLoginPost(Context context, String api, int type, HttpRequestCallback mCallBack);

    // Post请求
    void mHttpLoginPost(Context context, String api, TreeMap<String, Object> map, int type, HttpRequestCallback mCallBack);

    // Post请求
    void mHttpLoginPost(Context context, TreeMap<String, Object> map, int type, HttpRequestCallback mCallBack);

    // Post请求(包含数组)
    void mHttpLoginPost(Context context, TreeMap<String, Object> treeMap, String[] data, int type, HttpRequestCallback mCallBack);

    //    Common
    void commonGet(Context context, String api, TreeMap<String, Object> treeMap, int type, HttpRequestCallback mCallBack);

    void commonPost(Context context, String api, TreeMap<String, Object> treeMap, int type, HttpRequestCallback mCallBack);
}
