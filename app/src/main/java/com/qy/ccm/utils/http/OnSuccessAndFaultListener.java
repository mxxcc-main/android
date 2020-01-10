package com.qy.ccm.utils.http;

/**
 * Created by 眼神 on 2018/3/27.
 */
public interface OnSuccessAndFaultListener {
    void onSuccess(String result, String url);

    void onFault(String errorMsg, String url);

    void onException(String errorMsg, String url);

}
