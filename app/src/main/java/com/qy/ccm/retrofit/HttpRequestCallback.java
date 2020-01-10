package com.qy.ccm.retrofit;


/**
 * Created by zhoufan on 2018/1/3.
 */

public interface HttpRequestCallback<T> {

    void onRequestSuccess(String result, int type) throws Exception;

    void onRequestFail(String value, String failCode, int type);

}
