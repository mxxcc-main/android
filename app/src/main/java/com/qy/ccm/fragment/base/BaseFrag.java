package com.qy.ccm.fragment.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.function.WidgetUtils;
import com.qy.ccm.utils.http.Http;
import com.qy.ccm.utils.http.OnSuccessAndFaultListener;
import com.qy.ccm.utils.http.OnSuccessAndFaultSub;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.net.URISyntaxException;
import java.util.Map;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Description:
 * Data：2019/5/8-4:55 PM
 *
 * @author
 */
public abstract class BaseFrag extends Fragment {
    protected RxPermissions rxPermissions;
    protected WidgetUtils dialogUtils;
    protected CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //权限管理
        rxPermissions = new RxPermissions(this);
        // Inflate the layout for this fragment
        View view = inflater.inflate(initView(), container, false);
        ButterKnife.bind(this, view);
        dialogUtils = new WidgetUtils(getActivity());
        fillData();
        setListener();
        return view;
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
                onHttpException(errorMsg, url);

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
            }
        }, url));
    }

    public void onHttpSuccess(String result, String url) {

    }

    public void onHttpFault(String errorMsg, String url) {
        Utils.Toast(errorMsg);

    }

    public void onHttpException(String result, String url) {

    }

}
