package com.qy.ccm.aty.wallet;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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
import com.qy.ccm.utils.blockchain.BlockChainUtils;
import com.qy.ccm.utils.database.DatabaseWalletUtils;
import com.qy.ccm.utils.function.StringUtils;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

public class NonnemicWordImportAty extends BaseAty {

    @BindView(R.id.nll_wallet_market)
    NavigationLucencyLayout nll_wallet_market;

    @BindView(R.id.nenemic_word_id)
    EditText nenemic_word_id;
    @BindView(R.id.wallet_name_id)
    EditText wallet_name_id;
    @BindView(R.id.pass_id)
    EditText pass_id;
    @BindView(R.id.pass_conf_id)
    EditText pass_conf_id;
    @BindView(R.id.is_show_pass_id)
    ImageView is_show_pass_id;
    @BindView(R.id.import_wallet_button_id)
    Button import_wallet_button_id;

    private Boolean isShowPass = false;
    private String state;
    private String coin;

    @OnClick({R.id.import_wallet_button_id, R.id.is_show_pass_id})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.import_wallet_button_id:

                if (StringUtils.isEmpty(nenemic_word_id.getText().toString())) {
                    Utils.Toast("助记词不能为空");
                    return;
                }
                if (wallet_name_id.getText().toString().isEmpty()) {
                    Utils.Toast("钱包名称不能为空");
                    return;
                }

                if (pass_id.getText().toString().isEmpty()) {
                    Utils.Toast("密码不能为空");
                    return;
                }

                if (!Pattern.matches(Utils.passRegex, pass_id.getText().toString())) {
                    Utils.Toast("密码必须为8-32位字母、数字组合");
                    return;
                }

                if (!pass_id.getText().toString().equals(pass_conf_id.getText().toString())) {
                    Utils.Toast("两次输入密码不一致");
                    return;
                }

//                创建钱包
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(pass_id.getWindowToken(), 0);


                dialogUtils.showProgressDialog();

//                import   private key

                new NonnemicWordImportAty.MyAsyncTask().execute(nenemic_word_id.getText().toString(), "11", pass_id.getText().toString(), wallet_name_id.getText().toString());

                break;
            case R.id.is_show_pass_id:
                if (!isShowPass) {
                    pass_id.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    is_show_pass_id.setImageResource(R.mipmap.icon_found_eyes);
                    isShowPass = true;
                } else {
                    pass_id.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    is_show_pass_id.setImageResource(R.mipmap.icon_close);
                    isShowPass = false;
                }

                break;
            default:
                break;
        }


    }

    class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private String newAddress;

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
            String state = strings[1];
            String pow = strings[2];
            String walletName = strings[3];
            if ("11".equals(state)) {
                newAddress = BlockChainUtils.importTheWallet(mnemonicWord, "", coin, walletName);
                Log.e("============", "newAddress:" + newAddress);
                List<WalletBean> walletList2 = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");
                List<WalletBean> walletList = DatabaseWalletUtils.getLocalCreatedWalletList(newAddress, coin, Config.getMobleMapping());
                if (walletList == null || walletList.isEmpty()) {
                    BlockChainUtils.importTheWallet(mnemonicWord, pow, coin, walletName);
                } else {
                    newAddress = "已存在";
                }
            } else if ("12".equals(state)) {
                newAddress = BlockChainUtils.imputPricate(mnemonicWord, "", coin, walletName);
                if (newAddress == null) {
                    return null;
                }
                Log.e("============", "newAddress:" + newAddress);
                List<WalletBean> walletList = DatabaseWalletUtils.getLocalCreatedWalletList(newAddress, coin, Config.getMobleMapping());
                if (walletList == null || walletList.isEmpty()) {
                    BlockChainUtils.imputPricate(mnemonicWord, pow, coin, walletName);
                } else {
                    newAddress = "已存在";
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            dialogUtils.dismissProgressDialog();
            Log.e("============", "newAddress:" + newAddress);
            if ("已存在".equals(newAddress)) {
                Utils.Toast("导入的地址已经存在");
            } else if (newAddress == null) {
                Utils.Toast("助记词不合法");
                dialogUtils.dismissProgressDialog();
            } else {
//                Intent intent = new Intent();
                myStartActivity(MainAty.class);
                finish();
//                setResult(12, intent);
//                finish();
            }

        }
    }

    @Override
    public int initView() {
        return R.layout.aty_mnemonic_word_import;
    }

    @Override
    public void setListener() {
    }

    @Override
    public void fillData() {

        showStatusBar(true);
        StatusBarUtil.setStatusColor(this, true, true, R.color.color_0692c2);
        nll_wallet_market.setTitle("助记词导入");
        state = "11";
        coin = "CCM";
    }
}
