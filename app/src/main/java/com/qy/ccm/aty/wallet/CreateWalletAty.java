package com.qy.ccm.aty.wallet;

import android.content.Context;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.qy.ccm.R;
import com.qy.ccm.aty.MainAty;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.config.Config;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.blockchain.eth.CCMUtils;
import com.qy.ccm.utils.database.DatabaseWalletUtils;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import ccm.web3j.crypto.CipherException;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.novacrypto.bip39.MnemonicGenerator;
import io.github.novacrypto.bip39.Words;
import io.github.novacrypto.bip39.wordlists.English;

public class CreateWalletAty extends BaseAty {

    @BindView(R.id.nll_wallet_market)
    NavigationLucencyLayout nll_wallet_market;

    @BindView(R.id.wallet_name_id)
    EditText wallet_name_id;

    @BindView(R.id.wallet_password_id)
    EditText wallet_password_id;

    @BindView(R.id.wallet_conf_password_id)
    EditText wallet_conf_password_id;

    @BindView(R.id.is_show_pass_id)
    ImageView is_show_pass_id;

    @BindView(R.id.create_wallet_buttom_id)
    Button create_wallet_buttom_id;

    private Boolean isShowPass = false;

    @Override
    public int initView() {
        return R.layout.aty_create_wallet;
    }

    @Override
    public void setListener() {
    }

    @Override
    public void fillData() {

        showStatusBar(true);
        nll_wallet_market.ivArrowsLift.setImageResource(R.mipmap.icon_return_white);
        StatusBarUtil.setStatusColor(this, true, false, R.color.color_0692c2);
        StatusBarUtil.setStatusBarColor(this, R.color.color_0692c2);

    }

    @OnClick({R.id.is_show_pass_id, R.id.create_wallet_buttom_id})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.is_show_pass_id:
                if (!isShowPass) {
                    wallet_password_id.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    is_show_pass_id.setImageResource(R.mipmap.icon_found_eyes);
                    isShowPass = true;
                } else {
                    wallet_password_id.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    is_show_pass_id.setImageResource(R.mipmap.icon_close);
                    isShowPass = false;
                }

                break;
            case R.id.create_wallet_buttom_id:
                if (wallet_name_id.getText().toString().isEmpty()) {
                    Utils.Toast("钱包名称不能为空");
                    return;
                }

                if (wallet_password_id.getText().toString().isEmpty()) {
                    Utils.Toast("密码不能为空");
                    return;
                }

                if (!Pattern.matches(Utils.passRegex, wallet_password_id.getText().toString())) {
                    Utils.Toast("密码必须为8-32位字母、数字组合");
                    return;
                }

                if (!wallet_password_id.getText().toString().equals(wallet_conf_password_id.getText().toString())) {
                    Utils.Toast("两次输入密码不一致");
                    return;
                }

                create_wallet_buttom_id.setClickable(false);
//                创建钱包
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(wallet_password_id.getWindowToken(), 0);


                dialogUtils.showProgressDialog();

                StringBuilder sb = new StringBuilder();
                byte[] entropy = new byte[Words.TWELVE.byteLength()];
                new SecureRandom().nextBytes(entropy);
                new MnemonicGenerator(English.INSTANCE)
                        .createMnemonic(entropy, sb::append);
                String mnemonicWord = sb.toString();

                //            判断钱包那些不存在，则会导入
                try {
                    new CCMUtils().createWallet(mnemonicWord, wallet_password_id.getText().toString(), "CCM", Config.getMobleMapping(), wallet_name_id.getText().toString());
                } catch (CipherException e) {
                    e.printStackTrace();
                }

//                dialogUtils.dismissProgressDialog();
                //            进入首页

//                这里进行助记词备份
                Utils.Toast("钱包创建成功，请备份助记词");
                Config.setCustomerId("1");
                List<WalletBean> walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");

                Bundle bundle = new Bundle();
                bundle.putString("tradePwd", wallet_password_id.getText().toString());
                bundle.putSerializable("walletBeans", (Serializable) walletBeans);


                myStartActivity(WalletMnemonicAty.class, bundle);

//                myStartActivity(MainAty.class);
                finish();

                break;

            default:
                break;
        }
    }
}
