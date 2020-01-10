package com.qy.ccm.aty.wallet;

import android.os.Build;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qy.ccm.R;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.config.Config;
import com.qy.ccm.retrofit.Constants;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import butterknife.BindView;

public class UserProtiAty extends BaseAty {

    @BindView(R.id.nll_wallet_market)
    NavigationLucencyLayout nll_wallet_market;

    @BindView(R.id.myv_advertising)
    WebView myv_advertising;

    @Override
    public int initView() {
        return R.layout.aty_common_webview;
    }

    @Override
    public void setListener() {
    }

    @Override
    public void fillData() {

        showStatusBar(true);
        StatusBarUtil.setStatusColor(this, true, true, R.color.colorPrimary);
        nll_wallet_market.setTitle("用户协议");


//        ----------------------------------DEMO----------------------
        myv_advertising.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (request.getUrl().toString().contains("sina.cn")) {
                        view.loadUrl("http://ccmht.cc/userPro.txt");
                        return true;
                    }
                }
                return false;
            }

        });

        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        };
        myv_advertising.setWebChromeClient(wvcc);

        myv_advertising.loadUrl("http://ccmht.cc/userPro.txt");

    }
}
