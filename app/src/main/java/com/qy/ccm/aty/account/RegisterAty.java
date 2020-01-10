package com.qy.ccm.aty.account;


import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.qy.ccm.R;
import com.qy.ccm.aty.MainAty;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.aty.wallet.WalletMnemonicAty;
import com.qy.ccm.aty.wallet.WalletPasswordAty;
import com.qy.ccm.config.Config;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建账户
 */
public class RegisterAty extends BaseAty {
    @BindView(R.id.nll_register)
    NavigationLucencyLayout nllRegister;
    @BindView(R.id.et_register_pow1)
    EditText etRegisterPow1;
    @BindView(R.id.et_register_pow2)
    EditText etRegisterPow2;
    @BindView(R.id.iv_register_show1)
    ImageView ivRegisterShow1;
    @BindView(R.id.iv_register_show2)
    ImageView ivRegisterShow2;
    private boolean showState = false;
    private String mnemonic;

    @OnClick({R.id.iv_register_show1, R.id.iv_register_show2, R.id.tv_register})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_register_show1:
                if (showState) {
                    etRegisterPow1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivRegisterShow1.setImageResource(R.mipmap.icon_login_close);
                } else {
                    etRegisterPow1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivRegisterShow1.setImageResource(R.mipmap.icon_login_open);
                }
                //设置光标位置在文本框末尾
                etRegisterPow1.setSelection(etRegisterPow1.getText().toString().length());
                showState = !showState;
                etRegisterPow1.postInvalidate();
                break;
            case R.id.iv_register_show2:
                if (showState) {
                    etRegisterPow2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivRegisterShow2.setImageResource(R.mipmap.icon_login_close);
                } else {
                    etRegisterPow2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivRegisterShow2.setImageResource(R.mipmap.icon_login_open);
                }
                //设置光标位置在文本框末尾
                etRegisterPow2.setSelection(etRegisterPow2.getText().toString().length());
                showState = !showState;
                etRegisterPow2.postInvalidate();
                break;
            case R.id.tv_register:
                String pow1 = etRegisterPow1.getText().toString();
                String pow2 = etRegisterPow2.getText().toString();
                if (pow1 == null || pow2 == null || !pow1.equals(pow2)) {
                    Utils.Toast("请输入正确密码");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("mnemonicWord", mnemonic);
                bundle.putString("password", pow1);
                myStartActivity(WalletPasswordAty.class, bundle);
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    public int initView() {
        return R.layout.aty_register;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillData() {
        mnemonic = getIntent().getStringExtra("mnemonicWord");
    }

}
