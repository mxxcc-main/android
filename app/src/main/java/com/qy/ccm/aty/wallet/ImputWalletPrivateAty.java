package com.qy.ccm.aty.wallet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.qy.ccm.R;
import com.qy.ccm.aty.account.LoginAndRegisterAty;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.config.Config;
import com.qy.ccm.retrofit.HttpRequestCallback;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.blockchain.BlockChainUtils;
import com.qy.ccm.utils.database.DatabaseWalletUtils;
import com.qy.ccm.utils.function.StringUtils;
import com.qy.ccm.view.group.NavigationLucencyLayout;
import com.qy.ccm.view.group.password.PayPwdEditText;
import com.uuzuche.lib_zxing.DisplayUtil;

import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:导入私钥/助记词
 * Data：2019/5/15-2:37 PM
 */
public class ImputWalletPrivateAty extends BaseAty implements HttpRequestCallback<Object> {
    @BindView(R.id.nll_private_wallet)
    NavigationLucencyLayout nllPrivateWallet;
    @BindView(R.id.et_private_wallet)
    EditText etPrivateWallet;
    @BindView(R.id.tv_private_wallet)
    TextView tvPrivateWallet;
    @BindView(R.id.tv_wallet_imput)
    TextView tvWalletImput;
    @BindView(R.id.tv_wallet_what)
    TextView tvWalletWallet;
    private String state;
    private String coin;
    private String imputContext;
    private String tradePwd;
    private Dialog dialog;
    private PayPwdEditText pow;

    @OnClick({R.id.tv_wallet_imput, R.id.tv_wallet_reset})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_wallet_imput:


                //弹框输入交易密码
                dialog = new Dialog(this, R.style.DialogThemeHalfAlpa);
                View view1 = LayoutInflater.from(this).inflate(R.layout.pow_dialog, null);
                dialog.setContentView(view1);
                dialog.setCanceledOnTouchOutside(true);
                Window dialogWindow = dialog.getWindow();

                WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//                                    p.height = DisplayUtil.dip2px(activity, 230f) // 高度设置为屏幕的0.6，根据实际情况调整
                p.width = DisplayUtil.dip2px(this, 300f); // 宽度设置为屏幕的0.65，根据实际情况调整
                dialogWindow.setAttributes(p);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);

                dialog.show();

                pow = view1.findViewById(R.id.ppe_wallet_affirmpassword);

                pow.initStyle(R.drawable.empty, 6, 0.5f, R.color.black, R.color.black, 30);

                //密码输入完后的回调

                pow.setOnTextFinishListener(str -> {
                    requestData(0);
                });


                break;
            case R.id.tv_wallet_reset:
                etPrivateWallet.setText("");
                break;
            default:
                break;
        }
    }

    private void imput(String pow) {
        if ("11".equals(state)) {
            //导入助记词
            new ImputWalletPrivateAty.MyAsyncTask().execute(imputContext, state, pow);
        } else if ("12".equals(state)) {
            //导入私钥
            new ImputWalletPrivateAty.MyAsyncTask().execute(imputContext, state, pow);
        }

    }

    @Override
    public int initView() {
        return R.layout.aty_imput_private_wallet;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillData() {

        StatusBarUtil.setStatusColor(this, false, true, R.color.colorPrimary);

        state = getIntent().getStringExtra("state");
        coin = getIntent().getStringExtra("coin");
        if ("11".equals(state)) {
            nllPrivateWallet.setTitle(getString(R.string.input_mn));
            tvPrivateWallet.setText(getString(R.string.imput_mn_context));
            tvWalletImput.setText(getString(R.string.input_mn));
            tvWalletWallet.setText(getString(R.string.imput_mn_what));
        } else if ("12".equals(state)) {
            nllPrivateWallet.setTitle(getString(R.string.input_pr));
            tvPrivateWallet.setText(getString(R.string.imput_private_context));
            tvWalletImput.setText(getString(R.string.input_pr));
            tvWalletWallet.setText(getString(R.string.imput_private_what));
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
            if ("11".equals(state)) {
                newAddress = BlockChainUtils.importTheWallet(mnemonicWord, "", coin,"");
                Log.e("============", "newAddress:" + newAddress);
                List<WalletBean> walletList2 = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");
                List<WalletBean> walletList = DatabaseWalletUtils.getLocalCreatedWalletList(newAddress, coin, Config.getMobleMapping());
                if (walletList == null || walletList.isEmpty()) {
                    BlockChainUtils.importTheWallet(mnemonicWord, pow, coin,"");
                } else {
                    newAddress = "已存在";
                }
            } else if ("12".equals(state)) {
                newAddress = BlockChainUtils.imputPricate(mnemonicWord, "", coin,"");
                if (newAddress == null) {
                    return null;
                }
                Log.e("============", "newAddress:" + newAddress);
                List<WalletBean> walletList = DatabaseWalletUtils.getLocalCreatedWalletList(newAddress, coin, Config.getMobleMapping());
                if (walletList == null || walletList.isEmpty()) {
                    BlockChainUtils.imputPricate(mnemonicWord, pow, coin,"");
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
                Utils.Toast("私钥地址不合法");
            } else {
                Intent intent = new Intent();
                setResult(12, intent);
                finish();
            }

        }
    }


    private void requestData(int type) {

        dialogUtils.showProgressDialog();
        TreeMap<String, Object> map;
        if (type == 0) {
            map = new TreeMap<>();
            map.put("tradePwd", Utils.getMD5String(pow.getPwdText()));
            HttpUtils.getHttpUtils().executeGet(this, map, "checkTradePwd", type, this);
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
                    Boolean isSuccess = jsonObject.getBoolean("success");
                    if (isSuccess) {
                        dialog.dismiss();
                        InputMethodManager imm = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(pow.getWindowToken(), 0);


                        imputContext = etPrivateWallet.getText().toString().trim();
                        if (StringUtils.isEmpty(imputContext)) {
                            if ("11".equals(state)) {
                                Utils.Toast(getString(R.string.imput_mn_context));
                            } else if ("12".equals(state)) {
                                Utils.Toast(getString(R.string.imput_private_context));
                            }
                        } else {
                            imput(pow.getPwdText());
                        }
                    } else {
                        Utils.Toast("交易密码错误！请重新输入");
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

}
