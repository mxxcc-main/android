package com.qy.ccm.aty.my;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qy.ccm.R;
import com.qy.ccm.app.MyApp;
import com.qy.ccm.aty.account.LoginAndRegisterAty;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.aty.wallet.WalletMnemonicAty;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.config.Config;
import com.qy.ccm.retrofit.HttpRequestCallback;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.database.DatabaseWalletUtils;
import com.qy.ccm.utils.function.safety.AES256;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:交易页面
 * Data：2019/5/9-8:01 PM
 */
public class ChangePasswordAty extends BaseAty implements HttpRequestCallback<Object> {

    @BindView(R.id.iv_arrows_lift)
    ImageView iv_arrows_lift;

    @BindView(R.id.switch_id)
    Switch switch_id;

    @BindView(R.id.login_pass_change)
    LinearLayout login_pass_change;
    @BindView(R.id.trade_pass_id)
    LinearLayout trade_pass_id;

    @BindView(R.id.old_login_pass)
    EditText old_login_pass;

    @BindView(R.id.newe_login_pass)
    EditText newe_login_pass;

    @BindView(R.id.conf_login_pass)
    EditText conf_login_pass;


    @BindView(R.id.old_trade_pass)
    EditText old_trade_pass;

    @BindView(R.id.new_trade_pass)
    EditText new_trade_pass;

    @BindView(R.id.conf_trade_pass)
    EditText conf_trade_pass;


    @BindView(R.id.login_cshow_1)
    ImageView login_cshow_1;

    @BindView(R.id.login_show_2)
    ImageView login_show_2;

    @BindView(R.id.trade_i1)
    ImageView trade_i1;

    @BindView(R.id.trade_q2)
    ImageView trade_q2;


    private boolean isShowNewLoginPass = false;
    private boolean isShowConfNewLoginPass = false;
    private boolean isShowNewTradePass = false;
    private boolean isShowNewConfTradePass = false;

    @OnClick({R.id.iv_arrows_lift,
            R.id.login_edit,
            R.id.login_cshow_1,
            R.id.login_show_2,
            R.id.trade_i1,
            R.id.trade_q2,
            R.id.trade_edit})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_arrows_lift:
                finish();
                break;
            case R.id.login_edit:

                String oldLoginPass = old_login_pass.getText().toString();
                if (oldLoginPass == null || "".equals(oldLoginPass.trim())) {
                    Utils.Toast("请输入旧登录密码");
                    return;
                }

                String pow1 = newe_login_pass.getText().toString();
                String pow2 = conf_login_pass.getText().toString();
                if (pow1 == null || "".equals(pow1.trim()) || pow2 == null || "".equals(pow2.trim()) || !pow1.trim().equals(pow2.trim())) {
                    Utils.Toast("请输入正确的登录密码");
                    return;
                }


                requestData(0);
                break;
            case R.id.trade_edit:

                String oldTradePass = old_trade_pass.getText().toString();
                if (oldTradePass == null || "".equals(oldTradePass.trim())) {
                    Utils.Toast("请输入旧交易密码");
                    return;
                }


                String tra1 = new_trade_pass.getText().toString();
                String tra2 = conf_trade_pass.getText().toString();
                if (tra1 == null || "".equals(tra1.trim()) || tra2 == null || "".equals(tra2.trim()) || !tra1.trim().equals(tra2.trim())) {
                    Utils.Toast("请输入正确的交易密码");
                    return;
                }

                requestData(1);
                break;

            case R.id.login_cshow_1:
                if (!isShowNewLoginPass) {
                    newe_login_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    login_cshow_1.setImageResource(R.mipmap.icon_found_eyes);
                    isShowNewLoginPass = true;
                } else {
                    newe_login_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    login_cshow_1.setImageResource(R.mipmap.icon_close);
                    isShowNewLoginPass = false;
                }
                break;
            case R.id.login_show_2:

                if (!isShowConfNewLoginPass) {
                    conf_login_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    login_show_2.setImageResource(R.mipmap.icon_found_eyes);
                    isShowConfNewLoginPass = true;
                } else {
                    conf_login_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    login_show_2.setImageResource(R.mipmap.icon_close);
                    isShowConfNewLoginPass = false;
                }
                break;
            case R.id.trade_i1:
                if (!isShowNewTradePass) {
                    new_trade_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    trade_i1.setImageResource(R.mipmap.icon_found_eyes);
                    isShowNewTradePass = true;
                } else {
                    new_trade_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    trade_i1.setImageResource(R.mipmap.icon_close);
                    isShowNewTradePass = false;
                }
                break;
            case R.id.trade_q2:

                if (!isShowNewConfTradePass) {
                    conf_trade_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    trade_q2.setImageResource(R.mipmap.icon_found_eyes);
                    isShowNewConfTradePass = true;
                } else {
                    conf_trade_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    trade_q2.setImageResource(R.mipmap.icon_close);
                    isShowNewConfTradePass = false;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public int initView() {
        return R.layout.change_password;
    }

    @Override
    public void setListener() {

        switch_id.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                login_pass_change.setVisibility(View.GONE);
                trade_pass_id.setVisibility(View.VISIBLE);
            } else {
                login_pass_change.setVisibility(View.VISIBLE);
                trade_pass_id.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void fillData() {

//        StatusBarUtil.setStatusColor(this, true, false, R.color.color_2E303B);
//        nllWalletMarket.setTitle("行情");
        login_pass_change.setVisibility(View.VISIBLE);
        trade_pass_id.setVisibility(View.GONE);
        switch_id.setChecked(false);
    }

    private void requestData(int type) {
        dialogUtils.showProgressDialog();
        TreeMap<String, Object> map;
//        Login pass
        if (type == 0) {

            map = new TreeMap<>();
            map.put("oldPwd", Utils.getMD5String(old_login_pass.getText().toString()));
            map.put("newPwd", Utils.getMD5String(newe_login_pass.getText().toString()));
            map.put("type", 1);
            HttpUtils.getHttpUtils().executeGet(this, map, "changePwd", type, this);
        }
//        搬砖界面文字介绍
        if (type == 1) {
//            return;
//            TODO
            map = new TreeMap<>();
            map.put("oldPwd", Utils.getMD5String(old_trade_pass.getText().toString()));
            map.put("newPwd", Utils.getMD5String(new_trade_pass.getText().toString()));
            map.put("type", 2);
            HttpUtils.getHttpUtils().executeGet(this, map, "changePwd", type, this);
        }

    }

    /**
     * 接口请求成功
     */
    @Override
    public void onRequestSuccess(String result, int type) {

        dialogUtils.dismissProgressDialog();
        try {
            if (!TextUtils.isEmpty(result)) {
                if (type == 0) {

                    JSONObject jsonObject = JSONObject.parseObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    Config.setToken("");
                    Config.setCustomerId("");
                    Utils.Toast("修改登录密码成功");
                    cleanInputText();
                    MyApp.getSingleInstance().exitAllActivity();
                    startActivity(new Intent(this, LoginAndRegisterAty.class));

                }
                if (type == 1) {
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    if (jsonObject.getBoolean("success")) {
                        alterPassword();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接口请求失败
     */
    @Override
    public void onRequestFail(String value, String failCode, int type) {

        dialogUtils.dismissProgressDialog();
        if (!"null".equals(value)) {
            Utils.Toast(value);
        } else {
            Utils.Toast(failCode);
        }
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


            String old = old_trade_pass.getText().toString();
            String newpass = new_trade_pass.getText().toString();

            String decryptPrivateKey = AES256.SHA256Decrypt(privateKey, old);
            if (mnemonicWord == null || mnemonicWord.length() == 0) {
            } else {
                String decryptMnemonicWord = AES256.SHA256Decrypt(mnemonicWord, old);
                String encodeMnemonicKey = AES256.SHA256Encode(decryptMnemonicWord, newpass);
                walletBean.setTheMnemonicWord(encodeMnemonicKey);
            }
            //重新加密
            String encodePrivateKey = AES256.SHA256Encode(decryptPrivateKey, newpass);
            walletBean.setPrivateKey(encodePrivateKey);
            DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), tokenName, Config.getMobleMapping(), 3D);
        }
        Utils.Toast("修改交易密码成功");
        cleanInputText();
        InputMethodManager imm = (InputMethodManager)
                this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(new_trade_pass.getWindowToken(), 0);

    }

    private void cleanInputText() {
        old_login_pass.setText("");
        newe_login_pass.setText("");
        old_trade_pass.setText("");
        old_login_pass.setText("");
        new_trade_pass.setText("");
        conf_trade_pass.setText("");


    }

}
