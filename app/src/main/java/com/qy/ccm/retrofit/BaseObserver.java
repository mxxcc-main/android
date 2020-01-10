package com.qy.ccm.retrofit;

import android.content.Context;
import android.util.Log;

import com.qy.ccm.R;
import com.qy.ccm.config.Config;
import com.qy.ccm.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by zhoufan on 2018/1/8.
 * 对返回的数据进行处理
 */

public class BaseObserver extends Subscriber<String> {

    private HttpRequestCallback mCallBack;
    private int mType;
    private Context mContext;

    BaseObserver(Context context, HttpRequestCallback mCallBack, int type) {
        this.mCallBack = mCallBack;
        this.mContext = context;
        this.mType = type;
    }

    // 请求开始前，判断网络连接情况
    @Override
    public void onStart() {
        super.onStart();
        // 在这里还要注意一种情况，就是如果使用了缓存的情况下，我们不能仅仅只通过网络情况就判断是否去请求数据
        // 所以在这里还需要判断没有开启离线缓存
        if (!HttpTool.hasNetwork(mContext)) {
//            TODO
//            Utils.Toast("连接错误");
            onCompleted();
        }
        // 显示进度条
        // showLoadingProgress();
    }

    @Override
    public void onCompleted() {
        // 关闭等待进度条
        // closeLoadingProgress();
    }

    // 请求错误的话直接弹出
    @Override
    public void onError(Throwable e) {
        Log.e("iozhaq", "请求错误");

        if (HttpTool.hasNetwork(mContext)) {
            if (e instanceof SocketTimeoutException) {
//                Utils.Toast("请求超时");

//TODO  暂时线这样做
//                mCallBack.onRequestFail("请求超时", null, mType);

                return;
            } else if (e instanceof UnknownHostException) {

//TODO  暂时线这样做
                mCallBack.onRequestFail("地址错误", null, mType);
//                Utils.Toast("地址错误");
                return;
            } else {
                mCallBack.onRequestFail(e.getMessage(), null, mType);
            }
        } else {
//            Utils.Toast("连接错误");
//            mCallBack.onRequestFail("连接错误", null, mType);
            return;
        }
    }

    // 请求成功
    @Override
    public void onNext(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            Log.e("iozhaq", s);
            if (jsonObject.has("success")) {
                Boolean success = jsonObject.getBoolean("success");
                String info = jsonObject.getString("info");
                String data = jsonObject.getString("data");
                if (success) {
                    mCallBack.onRequestSuccess(s, mType);
                } else {
                    //                    判断Token 是不是已经失效
                    if (info.contains("不存在或已失")) {
                        Config.setCustomerId("");
                        Config.setToken("");
//                        mCallBack.onJumpLogin();
                    } else {
                        mCallBack.onRequestFail(data, info, mType);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
