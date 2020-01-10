package com.qy.ccm.aty.wallet;

import android.widget.ImageView;

import com.qy.ccm.R;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import butterknife.BindView;

public class ChangeLanguageAty extends BaseAty {

    @BindView(R.id.nll_wallet_market)
    NavigationLucencyLayout nll_wallet_market;

    @Override
    public int initView() {
        return R.layout.aty_change_language;
    }

    @Override
    public void setListener() {
    }

    @Override
    public void fillData() {

        showStatusBar(true);
        nll_wallet_market.setTitle("多语言");
        StatusBarUtil.setStatusColor(this, true, true, R.color.colorPrimary);
    }
}
