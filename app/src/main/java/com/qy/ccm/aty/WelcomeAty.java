package com.qy.ccm.aty;


import android.view.View;

import com.qy.ccm.R;
import com.qy.ccm.aty.account.LoginAndRegisterAty;
import com.qy.ccm.aty.account.LoginAty;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.aty.wallet.WalletMnemonicAty;

import butterknife.OnClick;

public class WelcomeAty extends BaseAty {

    @OnClick({R.id.tv_welcome_login, R.id.tv_welcome_create})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_welcome_login:
                myStartActivity(LoginAndRegisterAty.class);
                break;
            case R.id.tv_welcome_create:
                myStartActivity(WalletMnemonicAty.class);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public int initView() {
        return R.layout.aty_welcome;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillData() {

    }
}
