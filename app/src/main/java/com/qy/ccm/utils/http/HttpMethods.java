package com.qy.ccm.utils.http;

import android.util.Log;

import com.qy.ccm.app.MyApp;
import com.qy.ccm.config.Config;
import com.qy.ccm.constants.UrlConstants;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.function.StringUtils;
import com.qy.ccm.utils.sp.SpKeyUtils;
import com.qy.ccm.utils.sp.SpUtils;


import android.util.Base64;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author xq
 */

public class HttpMethods {
    private static final String CACHE_NAME = "wtoken";
    private static String BASE_URL = UrlConstants.BASE_URL;
    private static final int DEFAULT_CONNECT_TIMEOUT = 30;
    private static final int DEFAULT_WRITE_TIMEOUT = 30;
    private static final int DEFAULT_READ_TIMEOUT = 30;

    private Retrofit retrofit;
    private HttpApi httpApi;
    private String TOKEN = "";

    /**
     * 请求失败重连次数
     */
    private int RETRY_COUNT = 0;
    private OkHttpClient.Builder okHttpBuilder;

    /**
     * 构造方法私有
     */
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        okHttpBuilder = new OkHttpClient.Builder();
        /**
         * 设置缓存
         */
        File cacheFile = new File(MyApp.appContext.getExternalCacheDir(), CACHE_NAME);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = chain -> {
            Request request = chain.request();
            if (!NetUtil.isNetworkConnected()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            if (!NetUtil.isNetworkConnected()) {
                int maxAge = 0;
                // 有网络时 设置缓存超时时间0个小时
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .removeHeader(CACHE_NAME)
                        .build();
            } else {
                //无网络时，设置超时为4周
                int maxStale = 60 * 60 * 24 * 28;
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader(CACHE_NAME)
                        .build();
            }
            List<String> setCookie = response.headers("Set-Cookie");
            if (!setCookie.isEmpty()) {
                Config.setCookie(setCookie);

            }

            return response;
        };
        okHttpBuilder.cache(cache).addInterceptor(cacheInterceptor);


        /**
         * 设置头信息
         */
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();


                Request.Builder requestBuilder = originalRequest.newBuilder();
                requestBuilder.addHeader("Accept", "application/json, text/plain, */*");
                requestBuilder.addHeader("Content-Type", "application/json; charset=utf-8");
//                requestBuilder.addHeader("Content-Type","application/x-www-form-urlencoded");
                requestBuilder.method(originalRequest.method(), originalRequest.body());
                //添加请求头信息，服务器进行token有效性验证
                List<String> list = Config.getCookie();
                for (String cookie : list) {
                    requestBuilder.addHeader("cookie", cookie);
                }
                String accessToken = SpUtils.getString(null, SpKeyUtils.ACCESS_TOKEN, "");
                if (StringUtils.isNotEmpty(accessToken)) {
                    requestBuilder.addHeader("accessToken", accessToken);
                }

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        okHttpBuilder.addInterceptor(headerInterceptor);


        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("=========", message);
//                if("No free outputs to spend".equals(message.trim())) {
//                    Utils.Toast("余额不足");
//                    return;
//                }

            }


        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //设置 Debug Log 模式
        okHttpBuilder.addInterceptor(loggingInterceptor);


        //设置超时和重新连接
        okHttpBuilder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);
        //错误重连
        okHttpBuilder.retryOnConnectionFailure(true);


        retrofit = new Retrofit.Builder()
                .client(okHttpBuilder.build())
                //json转换成JavaBean
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        httpApi = retrofit.create(HttpApi.class);
    }

    /**
     * 在访问HttpMethods时创建单例
     */
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();

    }

    /**
     * 获取单例
     */
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取retrofit
     */
    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void changeBaseUrl(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .client(okHttpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        httpApi = retrofit.create(HttpApi.class);
    }

    /**
     * 获取httpService
     */
    public HttpApi getHttpApi() {
        return httpApi;
    }

    /**
     * 设置订阅 和 所在的线程环境
     */
    public <T> void toSubscribe(Observable<T> o, DisposableObserver<T> s) {

        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //请求失败重连次数
                .retry(RETRY_COUNT)
                .subscribe(s);

    }
}