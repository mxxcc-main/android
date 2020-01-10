package com.qy.ccm.retrofit;

/**
 * Created by zhoufan on 2018/1/3.
 * Retrofit的基本设置
 * 主要作用是获取okhttp和retrofit的辅助类
 * 使用单例模式生成唯一实例
 */
class BaseHttpRetrofit {

    private static OkHttpUtil okHttpUtil;
    private static RetrofitUtil retrofitUtil;

    static OkHttpUtil getOkHttpUtil() {
        if (okHttpUtil == null) {
            synchronized (OkHttpUtil.class) {
                if (okHttpUtil == null) {
                    okHttpUtil = new OkHttpUtil();
                }
            }
        }
        return okHttpUtil;
    }

    static RetrofitUtil getRetrofitUtil() {
        if (retrofitUtil == null) {
            synchronized (RetrofitUtil.class) {
                if (retrofitUtil == null) {
                    retrofitUtil = new RetrofitUtil();
                }
            }
        }
        return retrofitUtil;
    }
}
