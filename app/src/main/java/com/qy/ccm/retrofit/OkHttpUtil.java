package com.qy.ccm.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by zhoufan on 2018/1/12 0012.
 * 设置OkHttp的一些操作
 */

public class OkHttpUtil {

    private boolean isCache = false;
    private boolean isIntercept = true;
    private long readTimeOut = 300000;
    private long connectTimeOut =300000;
    private long writeTimeOut =300000;


    // 设置缓存
    public OkHttpUtil setCache(boolean cacheValue) {
        this.isCache = cacheValue;
        return this;
    }

    // 设置数据读取超时时间
    public OkHttpUtil setTimeOutTime(long timeout) {
        readTimeOut = timeout;
        return this;
    }

    // 设置网络连接超时时间
    public OkHttpUtil setConnectTime(long timeout) {
        connectTimeOut = timeout;
        return this;
    }

    // 设置写入服务器的超时时间
    public OkHttpUtil setWriteTime(long timeout) {
        writeTimeOut = timeout;
        return this;
    }

    // 设置拦截器
    public OkHttpUtil setIntercept(boolean isIntercept) {
        this.isIntercept = isIntercept;
        return this;
    }

    // 设置Build方法
    public OkHttpClient build() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.readTimeout(readTimeOut, TimeUnit.MILLISECONDS);
        okHttpClient.connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS);
        okHttpClient.writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS);
        okHttpClient.addInterceptor(new HttpLoggingInterceptor());
        return okHttpClient.build();
    }
}
