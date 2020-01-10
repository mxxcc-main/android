package com.qy.ccm.utils.http;


import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;
import com.qy.ccm.R;
import com.qy.ccm.app.MyApp;
import com.qy.ccm.bean.base.BaseBean;

import org.json.JSONObject;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * @author xq
 */

public class OnSuccessAndFaultSub extends DisposableObserver<ResponseBody> {
    private OnSuccessAndFaultListener mOnSuccessAndFaultListener;
    private String url;


    /**
     * @param mOnSuccessAndFaultListener 成功回调监听
     */
    public OnSuccessAndFaultSub(OnSuccessAndFaultListener mOnSuccessAndFaultListener, String url) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
        this.url = url;
    }


    /**
     * @param mOnSuccessAndFaultListener 成功回调监听
     * @param context                    上下文
     */
    public OnSuccessAndFaultSub(OnSuccessAndFaultListener mOnSuccessAndFaultListener, Context context) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
    }


    /**
     * @param mOnSuccessAndFaultListener 成功回调监听
     * @param context                    上下文
     * @param showProgress               是否需要显示默认Loading
     */
    public OnSuccessAndFaultSub(OnSuccessAndFaultListener mOnSuccessAndFaultListener, Context context, boolean showProgress) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
    }


    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     */
    @Override
    public void onError(Throwable e) {
        Log.e("===========2", "onError:" + e);
        try {
            Resources res = MyApp.getSingleInstance().getResources();
            //请求超时
            if (e instanceof SocketTimeoutException) {
                mOnSuccessAndFaultListener.onFault(res.getString(R.string.requesttimeout), url);
            } else if (e instanceof ConnectException) {
                //网络连接超时
                mOnSuccessAndFaultListener.onFault(res.getString(R.string.connecttimeout), url);
            } else if (e instanceof SSLHandshakeException) {
                //安全证书异常
                mOnSuccessAndFaultListener.onFault(res.getString(R.string.SSLhandshakeexception), url);
            } else if (e instanceof HttpException) {
                //请求的地址不存在
                int code = ((HttpException) e).code();
                if (code == 504) {
                    mOnSuccessAndFaultListener.onFault(res.getString(R.string.httpexception), url);
                } else if (code == 404) {
                    mOnSuccessAndFaultListener.onFault(res.getString(R.string.http404), url);
                } else if (code == 500) {
                    mOnSuccessAndFaultListener.onFault(res.getString(R.string.http500), url);
                } else {
                    mOnSuccessAndFaultListener.onFault(res.getString(R.string.requestfailure), url);
                }
            } else if (e instanceof UnknownHostException) {
                //域名解析失败
                mOnSuccessAndFaultListener.onFault(res.getString(R.string.unknownhostexception), url);
            } else if (e instanceof NullPointerException) {
                mOnSuccessAndFaultListener.onFault(res.getString(R.string.requestfailure), url);
                Log.e("===========2", "NullPointerException:" + e.toString());
            } else {
                mOnSuccessAndFaultListener.onFault(res.getString(R.string.error) + e.getMessage(), url);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            mOnSuccessAndFaultListener.onException(e2.toString(), url);
        } finally {
            Log.e("OnSuccessAndFaultSub", "error:" + e.getMessage());
        }

    }

    @Override
    public void onComplete() {

    }


    /**
     * 当result等于1回调给调用者，否则自动显示错误信息，若错误信息为401跳转登录页面。
     * ResponseBody  body = response.body();//获取响应体
     * InputStream inputStream = body.byteStream();//获取输入流
     * byte[] bytes = body.bytes();//获取字节数组
     * String str = body.string();//获取字符串数据
     */
    @Override
    public void onNext(ResponseBody body) {
        try {
            String result;
            result = body.string();
            Log.e("===========%%%1", result);
            JSONObject jsonObject = new JSONObject(result);
            BaseBean bean = new Gson().fromJson(result, BaseBean.class);
            String resultCode = bean.wtoken_code;
            if (resultCode == null) {
                //外部接口
                mOnSuccessAndFaultListener.onSuccess(result, url);
            } else {

                if (resultCode.equals("200")) {
                    mOnSuccessAndFaultListener.onSuccess(result, url);
                } else {
                    String errorMsg = jsonObject.getString("msg");
                    mOnSuccessAndFaultListener.onFault(errorMsg, url);
                }
            }
        } catch (EOFException e) {
            // this exception is a well know bug, ignore it.
            mOnSuccessAndFaultListener.onException(e.toString(), url);
            Log.e("===========2", "EOFException:" + e.toString());
        } catch (Exception e) {
            mOnSuccessAndFaultListener.onException(e.toString(), url);
            Log.e("===========2", "Exception:" + e.toString());

            e.printStackTrace();
        }
    }
}
