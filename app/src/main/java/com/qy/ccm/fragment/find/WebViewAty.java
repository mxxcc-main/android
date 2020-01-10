package com.qy.ccm.fragment.find;

import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.qy.ccm.R;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.utils.StatusBarUtil;

import butterknife.BindView;


public class WebViewAty extends BaseAty {

    @BindView(R.id.webview_contract)
    MyWebView myv_advertising;

    @BindView(R.id.iv_arrows_lift)
    ImageView iv_arrows_lift;

    @BindView(R.id.iv_arrows_home)
    ImageView iv_arrows_home;

    @BindView(R.id.url_title)
    TextView url_title;

    @BindView(R.id.iv_bar_two)
    ImageView iv_bar_two;

    String detailUrl;

    String baiduSeUrl = "https://m.baidu.com/s?wd=";

    @Override
    public int initView() {
        return R.layout.aty_webview;
    }

    @Override
    public void setListener() {

        iv_arrows_lift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_arrows_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myv_advertising.loadUrl(detailUrl);
            }
        });
        iv_bar_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                myv_advertising.onResume();
//                //恢复pauseTimers状态
//                myv_advertising.resumeTimers();
                myv_advertising.reload();
            }
        });
        myv_advertising.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    //按返回键操作并且能回退网页
                    if (keyCode == KeyEvent.KEYCODE_BACK && myv_advertising.canGoBack()) {
                        //后退
                        myv_advertising.goBack();
                        return true;
                    }
                }
                return true;
            }
        });
        myv_advertising.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return super.shouldOverrideUrlLoading(view, url);
                if(request.getUrl().toString().startsWith("http")||request.getUrl().toString().startsWith("https")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Log.e("iozhaq:", request.getUrl().toString());
                        view.loadUrl(request.getUrl().toString());
                        return true;
                    }
                }
                return true;
            }

        });


        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                url_title.setText(title);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                // TODO Auto-generated method stub
                return super.onJsAlert(view, url, message, result);
            }

            // 将公共的js文件等资源放在本地，然后加载webView的时候直接从本地进行加载
            @Nullable
//            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                Log.e("AAAA", url);
                return null;
            }

            // wvIndexWeb.setWebViewClient(new WebViewClient(){

// });


        };
        myv_advertising.setWebChromeClient(wvcc);
//        detailUrl = "https://m.baidu.com";
        myv_advertising.loadUrl(detailUrl);

    }

    @Override
    public void fillData() {
        detailUrl = getIntent().getStringExtra("detailUrl");
        StatusBarUtil.setStatusColor(this, true, true, R.color.colorPrimary);
        StatusBarUtil.setStatusBarColor(this, R.color.colorPrimary);
        myv_advertising.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        myv_advertising.getSettings().setSupportZoom(true);
        //扩大比例的缩放
        myv_advertising.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        myv_advertising.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        myv_advertising.setInitialScale(70);
        myv_advertising.getSettings().setLoadWithOverviewMode(true);
        myv_advertising.getSettings().setBlockNetworkImage(false);//解决图片不显示
        myv_advertising.requestFocus();
        myv_advertising.getSettings().setSupportMultipleWindows(true);
        myv_advertising.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        打开LocalStorage
        myv_advertising.getSettings().setDomStorageEnabled(true);

//
//        //下载监听
        myv_advertising.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
//                String blob = JavaScriptInterface.H5toAndoid_test(url);
//                myv_advertising.loadUrl(blob);
            }
        });

        myv_advertising.getSettings().setAppCachePath(this.getApplicationContext().getCacheDir().getAbsolutePath());
//        myv_advertising.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        myv_advertising.getSettings().setDatabaseEnabled(true);
        myv_advertising.getSettings().setDomStorageEnabled(true);
        myv_advertising.getSettings().setUseWideViewPort(true);
        myv_advertising.getSettings().setLoadWithOverviewMode(true);
        myv_advertising.getSettings().setPluginState(WebSettings.PluginState.ON);
        myv_advertising.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

    }

}
