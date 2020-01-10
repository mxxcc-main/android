package com.qy.ccm.aty.account;


import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.qy.ccm.R;
import com.qy.ccm.aty.MainAty;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.aty.wallet.WalletMnemonicAty;
import com.qy.ccm.config.Config;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页
 */
public class LoginAty extends BaseAty {
    @BindView(R.id.nll_login)
    NavigationLucencyLayout nllLogin;
    @BindView(R.id.tv_login_show)
    ImageView tvLoginShow;
    @BindView(R.id.et_login)
    EditText etLogin;
    private boolean showState = false;

    @OnClick({R.id.tv_login_show, R.id.tv_login_create, R.id.tv_login_into})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_show:
                if (showState) {
                    etLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    tvLoginShow.setImageResource(R.mipmap.icon_login_close);
                } else {
                    etLogin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    tvLoginShow.setImageResource(R.mipmap.icon_login_open);

                }
                //设置光标位置在文本框末尾
                etLogin.setSelection(etLogin.getText().toString().length());
                showState = !showState;
                etLogin.postInvalidate();
                break;
            case R.id.tv_login_create:
                myStartActivity(WalletMnemonicAty.class);
                finish();
                break;
            case R.id.tv_login_into:
                String password = Config.getPassword();
                String inputPow = etLogin.getText().toString();
                if (password.equals(inputPow)) {
                    myStartActivity(MainAty.class);
                    finish();
                } else {
                    Utils.Toast("请输入正确密码");
                }

                break;
            default:
                break;
        }
    }

    @Override
    public int initView() {
        return R.layout.aty_login;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillData() {

    }
}
