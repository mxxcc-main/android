package com.qy.ccm.fragment;

import android.os.Build;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qy.ccm.R;
import com.qy.ccm.config.Config;
import com.qy.ccm.fragment.base.BaseFrag;
import com.qy.ccm.retrofit.Constants;
import com.qy.ccm.view.MyWebView;

import butterknife.BindView;

/**
 * Description:
 * Data：2019/5/8-4:54 PM
 *
 * @author
 */
public class FindFragkl extends BaseFrag {
    @Override
    public int initView() {
        return R.layout.frag_find;
    }

    @BindView(R.id.myv_advertising)
    MyWebView myv_advertising;

    @Override
    public void setListener() {

    }

    @Override
    public void fillData() {

//        StatusBarUtil.setStatusColor(this.getActivity(), true, false, R.color.color_2E303B);


//        ----------------------------------DEMO----------------------
        String url = Constants.BASE_URL + "resources/starmoney.html?customerId=" + Config.getCustomerId() + "&token=" + Config.getToken();
        myv_advertising.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (request.getUrl().toString().contains("sina.cn")) {
                        view.loadUrl(url);
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

        myv_advertising.loadUrl(url);
//        ---------------------------------------------------END------------------------------------
    }

}
