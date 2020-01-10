package com.qy.ccm.aty.my;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.qy.ccm.R;
import com.qy.ccm.aty.account.LoginAndRegisterAty;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.retrofit.HttpRequestCallback;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import java.util.TreeMap;

import butterknife.BindView;

/**
 * Description:交易页面
 * Data：2019/5/9-8:01 PM
 */
public class AboutUsAty extends BaseAty {
    @BindView(R.id.nll_wallet_market)
    NavigationLucencyLayout nllWalletMarket;

    @BindView(R.id.version_info_id)
    TextView version_info_id;

    @Override
    public int initView() {
        return R.layout.aty_about_us;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillData() {

        StatusBarUtil.setStatusColor(this, true, true, R.color.color_fbfbfd);
        nllWalletMarket.setTitle("关于我们");
        version_info_id.setText("v" + Utils.getVersionName(this));

    }


}
