package com.qy.ccm.retrofit;

import android.content.Context;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by zhofuan on 2018/1/23.
 * 遵循开闭原则，在这里实例化使用哪套网络请求框架
 */

public class HttpUtils {

    private static IHttpRequest mInitHttpRequest;
    private IHttpRequest mHttpRequest;

    public static void initHttpRequest(IHttpRequest httpRequest) {
        mInitHttpRequest = httpRequest;
    }

    // 如果有两种的情况下 比如 volley 下载文件并不是很屌 ，那么可以换成 OKHttp
    public HttpUtils httpRequest(IHttpRequest httpRequest) {
        mHttpRequest = httpRequest;
        return this;
    }

    private static volatile HttpUtils httpUtils;

    public static HttpUtils getHttpUtils() {
        if (httpUtils == null) {
            synchronized (HttpUtils.class) {
                if (httpUtils == null) {
                    httpUtils = new HttpUtils();
                }
            }
        }
        return httpUtils;
    }

    // GET请求(无参，无api)
    public <T> void executeGet(Context context, String api, int type, HttpRequestCallback<T> callback) {
        // 如果没有指定，那么就用 application 中初始化的
        if (mHttpRequest == null) {
            mHttpRequest = mInitHttpRequest;
        }
        mHttpRequest.mHttpGet(context, api, type, callback);
    }

    // GET请求(有参，无api)
    public <T> void executeGet(Context context, TreeMap<String, Object> maps, String api, int type, HttpRequestCallback<T> callback) {
        // 如果没有指定，那么就用 application 中初始化的
        if (mHttpRequest == null) {
            mHttpRequest = mInitHttpRequest;
        }
        mHttpRequest.mHttpGet(context, api, maps, type, callback);
    }

    // GET请求(有参，有api)
    public <T> void executeGet(Context context, TreeMap<String, Object> maps, int type, HttpRequestCallback<T> callback) {
        // 如果没有指定，那么就用 application 中初始化的
        if (mHttpRequest == null) {
            mHttpRequest = mInitHttpRequest;
        }
        mHttpRequest.mHttpGet(context, maps, type, callback);
    }

    // Post请求(无参，无api)
    public <T> void executePost(Context context, String api, int type, HttpRequestCallback<T> callback) {
        // 如果没有指定，那么就用 application 中初始化的
        if (mHttpRequest == null) {
            mHttpRequest = mInitHttpRequest;
        }
        mHttpRequest.mHttpPost(context, api, type, callback);
    }

    // Post请求(有参，无api)
    public <T> void executePost(Context context, TreeMap<String, Object> maps, String api, int type, HttpRequestCallback<T> callback) {
        // 如果没有指定，那么就用 application 中初始化的
        if (mHttpRequest == null) {
            mHttpRequest = mInitHttpRequest;
        }
        mHttpRequest.mHttpPost(context, api, maps, type, callback);
    }

    // Post请求(有参，有api)
    public <T> void executePost(Context context, TreeMap<String, Object> maps, int type, HttpRequestCallback<T> callback) {
        // 如果没有指定，那么就用 application 中初始化的
        if (mHttpRequest == null) {
            mHttpRequest = mInitHttpRequest;
        }
        mHttpRequest.mHttpPost(context, maps, type, callback);
    }

    // Post请求
    public <T> void executePost(Context context, TreeMap<String, Object> maps, String[] data, int type, HttpRequestCallback<T> callback) {
        // 如果没有指定，那么就用 application 中初始化的
        if (mHttpRequest == null) {
            mHttpRequest = mInitHttpRequest;
        }
        mHttpRequest.mHttpPost(context, maps, data, type, callback);
    }

    //=============================================================================
    // GET请求(无参，无api)
    public <T> void executeLoginGet(Context context, String api, int type, HttpRequestCallback<T> callback) {
        // 如果没有指定，那么就用 application 中初始化的
        if (mHttpRequest == null) {
            mHttpRequest = mInitHttpRequest;
        }
        mHttpRequest.mHttpLoginGet(context, api, type, callback);
    }

    // GET请求(有参，无api)
    public <T> void executeLoginGet(Context context, TreeMap<String, Object> maps, String api, int type, HttpRequestCallback<T> callback) {
        // 如果没有指定，那么就用 application 中初始化的
        if (mHttpRequest == null) {
            mHttpRequest = mInitHttpRequest;
        }
        mHttpRequest.mHttpLoginGet(context, api, maps, type, callback);
    }

    // GET请求(有参，有api)
    public <T> void executeLoginGet(Context context, TreeMap<String, Object> maps, int type, HttpRequestCallback<T> callback) {
        // 如果没有指定，那么就用 application 中初始化的
        if (mHttpRequest == null) {
            mHttpRequest = mInitHttpRequest;
        }
        mHttpRequest.mHttpLoginGet(context, maps, type, callback);
    }

    // Post请求(无参，无api)
    public <T> void executeLoginPost(Context context, String api, int type, HttpRequestCallback<T> callback) {
        // 如果没有指定，那么就用 application 中初始化的
        if (mHttpRequest == null) {
            mHttpRequest = mInitHttpRequest;
        }
        mHttpRequest.mHttpLoginPost(context, api, type, callback);
    }

    // Post请求(有参，无api)
    public <T> void executeLoginPost(Context context, TreeMap<String, Object> maps, String api, int type, HttpRequestCallback<T> callback) {
        // 如果没有指定，那么就用 application 中初始化的
        if (mHttpRequest == null) {
            mHttpRequest = mInitHttpRequest;
        }
        mHttpRequest.mHttpLoginPost(context, api, maps, type, callback);
    }

    // Post请求(有参，有api)
    public <T> void executeLoginPost(Context context, TreeMap<String, Object> maps, int type, HttpRequestCallback<T> callback) {
        // 如果没有指定，那么就用 application 中初始化的
        if (mHttpRequest == null) {
            mHttpRequest = mInitHttpRequest;
        }
        mHttpRequest.mHttpLoginPost(context, maps, type, callback);
    }

    // Post请求
    public <T> void executeLoginPost(Context context, TreeMap<String, Object> maps, String[] data, int type, HttpRequestCallback<T> callback) {
        // 如果没有指定，那么就用 application 中初始化的
        if (mHttpRequest == null) {
            mHttpRequest = mInitHttpRequest;
        }
        mHttpRequest.mHttpLoginPost(context, maps, data, type, callback);
    }

    //common get请求
    public <T> void executeCommonGet(Context context, String api, TreeMap<String, Object> maps, int type, HttpRequestCallback<T> callback) {
        // 如果没有指定，那么就用 application 中初始化的
        if (mHttpRequest == null) {
            mHttpRequest = mInitHttpRequest;
        }
        mHttpRequest.commonGet(context, api, maps, type, callback);
    }

    // common Post请求
    public <T> void executeCommonPost(Context context, String api, TreeMap<String, Object> maps, int type, HttpRequestCallback<T> callback) {
        // 如果没有指定，那么就用 application 中初始化的
        if (mHttpRequest == null) {
            mHttpRequest = mInitHttpRequest;
        }
        mHttpRequest.commonPost(context, api, maps, type, callback);
    }

}
