package com.qy.ccm.aty.wallet;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.qy.ccm.R;
import com.qy.ccm.aty.MainAty;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.config.Config;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.blockchain.BlockChainUtils;
import com.qy.ccm.utils.database.DatabaseWalletUtils;
import com.qy.ccm.view.group.NavigationLucencyLayout;
import com.qy.ccm.view.group.password.PayPwdEditText;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:设置交易密码
 * Data：2019/5/7-10:40 AM
 *
 * @author
 */
public class WalletPasswordAty extends BaseAty {
    @BindView(R.id.nll_wallet_password)
    NavigationLucencyLayout nllWalletPassword;
    @BindView(R.id.ppe_wallet_setpassword)
    PayPwdEditText ppeWalletSetpassword;
    @BindView(R.id.ppe_wallet_affirmpassword)
    PayPwdEditText ppeWalletAffirmpassword;

    private String mobile;
    private String transactionPassword;
    private String affirmPassword;
    private String mnemonicWord;
    private String password;

    @OnClick({R.id.tv_password_next})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_password_next:
                if (transactionPassword != null && affirmPassword != null && transactionPassword.equals(affirmPassword)) {
                    Config.setTransactionPassword(transactionPassword);
                    Config.setPassword(password);
//                    DatabaseWalletUtils.deleteLocalCoin();
                    createWallet();
                } else {
                    Utils.Toast(getString(R.string.inconformity_password));
                }

                break;
            default:
                break;
        }
    }


    @Override
    public int initView() {
        return R.layout.aty_wallet_password;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillData() {
        nllWalletPassword.setTitle(getString(R.string.set_transaction_password));
        mobile = getIntent().getStringExtra("mobile");
        mnemonicWord = getIntent().getStringExtra("mnemonicWord");
        password = getIntent().getStringExtra("password");
        Log.e("==========", "mnemonicWord2:" + mnemonicWord);
        inputPassword();

    }

    private void inputPassword() {

        /**
         * @param bgcolor 背景drawable
         * @param pwdlength 密码长度
         * @param slpilinewidth 分割线宽度
         * @param splilinecolor 分割线颜色
         * @param pwdcolor 密码字体颜色
         * @param pwdsize 密码字体大小
         */
        ppeWalletSetpassword.initStyle(R.drawable.edit_num_bg, 6, 0.33f, R.color.yellow, R.color.yellow, 30);
        ppeWalletAffirmpassword.initStyle(R.drawable.edit_num_bg, 6, 0.33f, R.color.yellow, R.color.yellow, 30);

        //密码输入完后的回调
        ppeWalletSetpassword.setOnTextFinishListener(str -> {
            transactionPassword = str;
            //手动收起软键盘
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ppeWalletSetpassword.getWindowToken(), 0);

        });

        ppeWalletAffirmpassword.setOnTextFinishListener(str -> {
            affirmPassword = str;
            //手动收起软键盘
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ppeWalletSetpassword.getWindowToken(), 0);

        });
    }

    private void createWallet() {
        new MyAsyncTask().execute(mnemonicWord);
    }

    class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //onPreExecute用于异步处理前的操作
            dialogUtils.showProgressDialog();

        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            //在doInBackground方法中进行异步任务的处理.
            //获取传进来的参数
            String mnemonicWord = strings[0];
            BlockChainUtils.importTheWallet(mnemonicWord, transactionPassword,Config.getMobleMapping(),"");
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            dialogUtils.dismissProgressDialog();
            myStartActivity(MainAty.class);
            finish();
        }
    }
}
