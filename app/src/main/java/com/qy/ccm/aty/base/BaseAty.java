package com.qy.ccm.aty.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.function.WidgetUtils;
import com.qy.ccm.utils.http.Http;
import com.qy.ccm.utils.http.OnSuccessAndFaultListener;
import com.qy.ccm.utils.http.OnSuccessAndFaultSub;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Map;

import butterknife.ButterKnife;

/**
 * Description:
 * Data：2019/5/5-1:34 PM
 */
public abstract class BaseAty extends AppCompatActivity {
    protected WidgetUtils dialogUtils;
    protected RxPermissions rxPermissions;

    @Override
    protected void onResume() {
        super.onResume();
        //注：回调 1
//        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        注：回调 2
//        Bugtags.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
//        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }

    /**
     * Allows for setting statusbar visibility (true by default)
     *
     * @param isVisible put true to show status bar, and false to hide it
     */
    public void showStatusBar(boolean isVisible) {
        if (!isVisible) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //屏幕固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //权限
        rxPermissions = new RxPermissions(this);
        setContentView(initView());
        ButterKnife.bind(this);
        dialogUtils = new WidgetUtils(this);
        fillData();
        setListener();

    }

    /**
     * 初始化页面
     */
    public abstract int initView();


    /**
     * 设置监听
     */
    public abstract void setListener();

    /**
     * 填充数据  在这个方法里 要设置pageName 和pageCode等数据 设置页面展示的用户行为日志数据
     */
    public abstract void fillData();


    /**
     * intent
     */
    public void myStartActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void myStartActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void myStartActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, requestCode);
    }


    public void myStartActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    public void postHttp(String url, Map map) {
        Http.postData(url, map, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result, String url) {
                if (dialogUtils != null) {
                    dialogUtils.dismissProgressDialog();
                }
                onHttpSuccess(result, url);


            }

            @Override
            public void onFault(String errorMsg, String url) {
                if (dialogUtils != null) {
                    dialogUtils.dismissProgressDialog();
                }
                onHttpFault(errorMsg, url);
            }

            @Override
            public void onException(String errorMsg, String url) {
                dialogUtils.dismissProgressDialog();
            }
        }, url));
    }

    public void getHttp(String url, Map map) {
        Http.getData(url, map, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result, String url) {
                dialogUtils.dismissProgressDialog();
                onHttpSuccess(result, url);

            }

            @Override
            public void onFault(String errorMsg, String url) {
                dialogUtils.dismissProgressDialog();
                onHttpFault(errorMsg, url);
            }

            @Override
            public void onException(String errorMsg, String url) {
                dialogUtils.dismissProgressDialog();
                onHttpFault(errorMsg, url);
            }
        }, url));
    }

    public void onHttpSuccess(String result, String url) {

    }

    public void onHttpFault(String result, String url) {
        Utils.Toast(result);
    }


}
