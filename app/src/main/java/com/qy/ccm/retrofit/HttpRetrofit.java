package com.qy.ccm.retrofit;

/**
 * 具体的请求操作
 */

public class HttpRetrofit extends BaseHttpRetrofit {


    // 使用单例模式生成唯一的HttpRetrofit类
    static ApiManager apiManager;

    // 设置缓存
    public HttpRetrofit setCache(boolean cacheValue) {
        //getOkHttpUtil().setCache(cacheValue);
        return this;
    }

    // 设置数据读取超时时间
    public HttpRetrofit setTimeOutTime(long timeout) {
        getOkHttpUtil().setTimeOutTime(timeout);
        return this;
    }

    // 设置网络连接超时时间
    public HttpRetrofit setConnectTime(long timeout) {
        getOkHttpUtil().setConnectTime(timeout);
        return this;
    }

    // 设置写入服务器的超时时间
    public HttpRetrofit setWriteTime(long timeout) {
        getOkHttpUtil().setWriteTime(timeout);
        return this;
    }

    // 设置拦截器
    public HttpRetrofit setIntercept(boolean isIntercept) {
        getOkHttpUtil().setIntercept(isIntercept);
        return this;
    }

    // 设置ApiManager
    public void build() {
        apiManager = getRetrofitUtil().setOkHttpClient(getOkHttpUtil().build()).build().create(ApiManager.class);
    }
}
