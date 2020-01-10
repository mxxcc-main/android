package com.qy.ccm.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2018/1/12 0012.
 * Retrofit辅助类
 */

public class RetrofitUtil {


    private String baseUrl;
    private Converter.Factory factory;
    private CallAdapter.Factory callFactory;
    private OkHttpClient okHttpClient;

    RetrofitUtil() {
        baseUrl = Constants.BASE_URL;
        factory = ScalarsConverterFactory.create();
        callFactory = RxJavaCallAdapterFactory.create();
    }

    // 设置BaseUrl
    public RetrofitUtil setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    // 设置数据解析
    public RetrofitUtil addConverterFactory(Converter.Factory factory) {
        this.factory = factory;
        return this;
    }

    // 设置数据解析
    public RetrofitUtil addCallAdapterFactory(CallAdapter.Factory factory) {
        this.callFactory = factory;
        return this;
    }

    // 设置写入服务器的超时时间
    public RetrofitUtil setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        return this;
    }

    // 设置Build方法
    public Retrofit build() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(baseUrl);
        builder.addConverterFactory(factory);
        builder.addCallAdapterFactory(callFactory);
        builder.client(okHttpClient);
        return builder.build();
    }
}
