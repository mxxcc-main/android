package com.qy.ccm.aty.wallet;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.qy.ccm.R;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.config.Config;
import com.qy.ccm.utils.database.DatabaseWalletUtils;
import com.qy.ccm.utils.function.safety.AES256;
import com.qy.ccm.view.group.NavigationLucencyLayout;
import com.qy.ccm.view.group.password.PayPwdEditText;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:
 * Data：2019/5/13-5:46 PM
 *
 * @author
 */
public class WalletAlterPasswordAty extends BaseAty {
    @BindView(R.id.nll_wallet_password)
    NavigationLucencyLayout nllWalletPassword;
    @BindView(R.id.ppe_wallet_after_setpassword)
    PayPwdEditText ppeWalletAfterSetpassword;
    @BindView(R.id.ppe_wallet_after_affirmpassword)
    PayPwdEditText ppeWalletFterAffirmpassword;
    private String transactionOld;
    private String transactionNew;


    @OnClick({R.id.tv_password_after_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_password_after_next:
                if (transactionOld != null && transactionNew != null && transactionOld.equals(transactionNew)) {
                    alterPassword();
                }
                break;
            default:
                break;
        }
    }


    @Override
    public int initView() {
        return R.layout.aty_wallet_alter_password;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillData() {
        nllWalletPassword.setTitle(getString(R.string.retrieve_password));

        ppeWalletAfterSetpassword.initStyle(R.drawable.edit_num_bg, 6, 0.33f, R.color.yellow, R.color.yellow, 30);
        ppeWalletFterAffirmpassword.initStyle(R.drawable.edit_num_bg, 6, 0.33f, R.color.yellow, R.color.yellow, 30);

        //密码输入完后的回调
        ppeWalletAfterSetpassword.setOnTextFinishListener(str -> {
            transactionOld = str;
            //手动收起软键盘
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ppeWalletAfterSetpassword.getWindowToken(), 0);

        });

        ppeWalletFterAffirmpassword.setOnTextFinishListener(str -> {
            transactionNew = str;
            //手动收起软键盘
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ppeWalletFterAffirmpassword.getWindowToken(), 0);

        });
    }


    private void alterPassword() {

        List<WalletBean> walletList = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");
        if (walletList == null || walletList.isEmpty()) {
            return;
        }
        for (WalletBean walletBean : walletList) {
            String tokenName = walletBean.getTokenName();
            String privateKey = walletBean.getPrivateKey();
            String mnemonicWord = walletBean.getTheMnemonicWord();
            String decryptPrivateKey = AES256.SHA256Decrypt(privateKey, transactionOld);
            String decryptMnemonicWord = AES256.SHA256Decrypt(mnemonicWord, transactionOld);
            //重新加密
            String encodePrivateKey = AES256.SHA256Encode(decryptPrivateKey, transactionNew);
            String encodeMnemonicKey = AES256.SHA256Encode(decryptMnemonicWord, transactionNew);
            walletBean.setPrivateKey(encodePrivateKey);
            walletBean.setTheMnemonicWord(encodeMnemonicKey);
            DatabaseWalletUtils.updateLocalData(walletBean, tokenName,Config.getMobleMapping(),Config.getMobleMapping(),3D);
        }

    }
}
