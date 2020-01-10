package com.qy.ccm.retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.qy.ccm.config.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OkHttp+Retrofit的网络请求框架
 */
public class HttpRetrofitRequest extends HttpRetrofit implements IHttpRequest {

    private volatile static HttpRetrofitRequest INSTANCES;


    public static HttpRetrofitRequest getInstances() {
        if (INSTANCES == null) {
            synchronized (HttpRetrofitRequest.class) {
                if (INSTANCES == null) {
                    INSTANCES = new HttpRetrofitRequest();
                }
            }
        }
        return INSTANCES;
    }

    private HttpRetrofitRequest() {
        build();
    }

    // GET请求(无参，无api)
    @Override
    public void mHttpGet(Context context, String api, int type, HttpRequestCallback mCallBack) {
        TreeMap map = new TreeMap<>();
        map.put("token", Config.getToken());
        map.put("customerId", Config.getCustomerId());
        Observable<String> observable = apiManager.getData(api, map);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver(context, mCallBack, type));
    }

    // GET请求(有参，无api)
    @Override
    public void mHttpGet(Context context, String api, TreeMap<String, Object> map, int type, HttpRequestCallback mCallBack) {
        map.put("token", Config.getToken());
        map.put("customerId", Config.getCustomerId());
        Observable<String> observable = apiManager.getData(api, map);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver(context, mCallBack, type));
    }

    // GET请求(有参，有api)
    @Override
    public void mHttpGet(Context context, TreeMap<String, Object> map, int type, HttpRequestCallback mCallBack) {
        map = HttpTool.getTreeCrc(map);
        map.put("token", Config.getToken());
        map.put("customerId", Config.getCustomerId());

        Observable<String> observable = apiManager.getData(map);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver(context, mCallBack, type));
    }

    // Post请求(无参，无api)
    @Override
    public void mHttpPost(Context context, String api, int type, HttpRequestCallback mCallBack) {
        TreeMap map = new TreeMap<>();
        map.put("token", Config.getToken());
        map.put("customerId", Config.getCustomerId());
        Observable<String> observable = apiManager.postData(map, api);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver(context, mCallBack, type));
    }

    @Override
    public void mHttpPost(Context context, String api, TreeMap<String, Object> map, int type, HttpRequestCallback mCallBack) {
        map.put("token", Config.getToken());
        map.put("customerId", Config.getCustomerId());


        Observable<String> observable = apiManager.postData(api, RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map)));
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver(context, mCallBack, type));
    }

    // 执行Post请求
    @Override
    public void mHttpPost(Context context, TreeMap<String, Object> map, int type, HttpRequestCallback mCallBack) {
        map.put("token", Config.getToken());
        map.put("customerId", Config.getCustomerId());

        map = HttpTool.getTreeCrc(map);
        Observable<String> observable = apiManager.postData(map);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver(context, mCallBack, type));
    }


    // 执行Post请求(包含数组)
    @Override
    public void mHttpPost(Context context, TreeMap<String, Object> map, String[] data, int type, HttpRequestCallback mCallBack) {
        map.put("token", Config.getToken());
        map.put("customerId", Config.getCustomerId());

        map = HttpTool.getTreeCrc(map);
        Observable<String> observable = apiManager.postData(map, data);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver(context, mCallBack, type));
    }

    // GET请求(无参，无api)
    @Override
    public void mHttpLoginGet(Context context, String api, int type, HttpRequestCallback mCallBack) {
        TreeMap map = new TreeMap<>();
        map.put("token", Config.getToken());
        map.put("customerId", Config.getCustomerId());
        Observable<String> observable = apiManager.getLoginData(api, map);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver(context, mCallBack, type));
    }

    // GET请求(有参，无api)
    @Override
    public void mHttpLoginGet(Context context, String api, TreeMap<String, Object> map, int type, HttpRequestCallback mCallBack) {
        map.put("token", Config.getToken());
        map.put("customerId", Config.getCustomerId());

        Observable<String> observable = apiManager.getLoginData(api, map);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver(context, mCallBack, type));
    }

    // GET请求(有参，有api)
    @Override
    public void mHttpLoginGet(Context context, TreeMap<String, Object> map, int type, HttpRequestCallback mCallBack) {
        map.put("token", Config.getToken());
        map.put("customerId", Config.getCustomerId());

        map = HttpTool.getTreeCrc(map);
        Observable<String> observable = apiManager.getLoginData(map);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver(context, mCallBack, type));
    }

    // Post请求(无参，无api)
    @Override
    public void mHttpLoginPost(Context context, String api, int type, HttpRequestCallback mCallBack) {
        TreeMap map = new TreeMap<>();
        map.put("token", Config.getToken());
        map.put("customerId", Config.getCustomerId());
        Observable<String> observable = apiManager.postLoginData(map, api);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver(context, mCallBack, type));
    }

    @Override
    public void mHttpLoginPost(Context context, String api, TreeMap<String, Object> map, int type, HttpRequestCallback mCallBack) {
        map.put("token", Config.getToken());
        map.put("customerId", Config.getCustomerId());

        Observable<String> observable = apiManager.postLoginData(api, (map));
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver(context, mCallBack, type));
    }

    // 执行Post请求
    @Override
    public void mHttpLoginPost(Context context, TreeMap<String, Object> map, int type, HttpRequestCallback mCallBack) {
        map.put("token", Config.getToken());
        map.put("customerId", Config.getCustomerId());

        map = HttpTool.getTreeCrc(map);
        Observable<String> observable = apiManager.postLoginData(map);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver(context, mCallBack, type));
    }


    // 执行Post请求(包含数组)
    @Override
    public void mHttpLoginPost(Context context, TreeMap<String, Object> map, String[] data, int type, HttpRequestCallback mCallBack) {
        map.put("token", Config.getToken());
        map.put("customerId", Config.getCustomerId());

        map = HttpTool.getTreeCrc(map);
        Observable<String> observable = apiManager.postLoginData(map, data);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver(context, mCallBack, type));
    }

    @Override
    public void commonGet(Context context, String api, TreeMap<String, Object> map, int type, HttpRequestCallback mCallBack) {
        Observable<String> observable = apiManager.commonGetData(api, map);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver(context, mCallBack, type));

    }

    @Override
    public void commonPost(Context context, String api, TreeMap<String, Object> map, int type, HttpRequestCallback mCallBack) {

        Gson gson = new Gson();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), gson.toJson(map));
        Observable<String> observable = apiManager.commonPostData(api, body);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver(context, mCallBack, type));
    }
}
