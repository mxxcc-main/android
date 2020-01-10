package com.qy.ccm.utils.http;


import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

/**
 * http请求
 *
 * @author xq
 */
public class Http {
    /**
     * get请求
     *
     * @param url        请求地址
     * @param map        map数据
     * @param subscriber 监听
     */
    public static void getData(String url, Map<String, Object> map, DisposableObserver<ResponseBody> subscriber) {
        if (map == null) {
            map = new HashMap<>();
        }

        Observable<ResponseBody> observable = HttpMethods.getInstance().getHttpApi().getDataForMap(url, map);
        HttpMethods.getInstance().toSubscribe(observable, subscriber);
    }


    /**
     * post请求
     *
     * @param url        地址
     * @param map        数据
     * @param subscriber 监听
     */
    public static void postData(String url, Map<String, Object> map, DisposableObserver<ResponseBody> subscriber) {
        if (map == null) {
            map = new HashMap<>();
        }
        Observable<ResponseBody> observable = HttpMethods.getInstance().getHttpApi().postDataForMap(url, map);
        HttpMethods.getInstance().toSubscribe(observable, subscriber);
    }


    /**
     * post请求
     *
     * @param url        地址
     * @param addr       数据
     * @param subscriber 监听
     */
    public static void postData2(String url, String addr, DisposableObserver<ResponseBody> subscriber) {
//        putLanguage(map);
        Observable<ResponseBody> observable = HttpMethods.getInstance().getHttpApi().postDataForQuery(url, addr);
        HttpMethods.getInstance().toSubscribe(observable, subscriber);
    }


    /**
     * 上传单个文件
     *
     * @param url        地址
     * @param parts      数据
     * @param subscriber 监听
     */
    public static void upload(String url, Map<String, String> map, MultipartBody.Part parts, DisposableObserver<ResponseBody> subscriber) {
        if (map == null) {
            map = new HashMap<>();
        }
        Observable<ResponseBody> observable = HttpMethods.getInstance().getHttpApi().uploadSingle(url, map, parts);
        HttpMethods.getInstance().toSubscribe(observable, subscriber);
    }


}
