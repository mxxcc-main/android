package com.qy.ccm.aty.wallet;

import com.qy.ccm.R;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import butterknife.BindView;

public class AboutUsAty extends BaseAty {

    @BindView(R.id.nll_wallet_market)
    NavigationLucencyLayout nll_wallet_market;

    @Override
    public int initView() {
        return R.layout.about_us;
    }

    @Override
    public void setListener() {
    }

    @Override
    public void fillData() {

        showStatusBar(true);
        StatusBarUtil.setStatusColor(this, true, true, R.color.colorPrimary);
        nll_wallet_market.setTitle("通知中心");
    }
}
