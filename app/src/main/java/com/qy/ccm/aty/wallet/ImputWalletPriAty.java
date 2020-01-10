package com.qy.ccm.aty.wallet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.qy.ccm.R;
import com.qy.ccm.adapter.wallet.ImportPrikeyAdapter;
import com.qy.ccm.aty.MainAty;
import com.qy.ccm.aty.account.LoginAndRegisterAty;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.bean.other.rxbus.ImportWalletEvent;
import com.qy.ccm.config.Config;
import com.qy.ccm.retrofit.HttpRequestCallback;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.blockchain.eth.CCMUtils;
import com.qy.ccm.utils.database.DatabaseWalletUtils;
import com.qy.ccm.utils.rxbus.ErrorBean;
import com.qy.ccm.utils.rxbus.RxBusHelper;
import com.qy.ccm.view.dialog.CreateSucceedDialog;
import com.qy.ccm.view.group.NavigationLucencyLayout;
import com.qy.ccm.view.group.password.PayPwdEditText;
import com.uuzuche.lib_zxing.DisplayUtil;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import io.github.novacrypto.bip39.MnemonicGenerator;
import io.github.novacrypto.bip39.Words;
import io.github.novacrypto.bip39.wordlists.English;

/**
 * Description:导入助记词
 * Data：2019/5/13-4:21 PM
 */
public class ImputWalletPriAty extends BaseAty implements OnRefreshListener, HttpRequestCallback<Object> {
    @BindView(R.id.add_wallet_new)
    NavigationLucencyLayout addWalletNew;
    CreateSucceedDialog createSucceedDialog;
    List<WalletBean> walletBeans;
    private Dialog dialog;
    private PayPwdEditText pow;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private String flag;
    private ImportPrikeyAdapter importPrikeyAdapter;

    @BindView(R.id.lr_id)
    LRecyclerView lRecyclerView;

    @BindView(R.id.import_create_btn)
    Button import_create_btn;

    @Override
    public int initView() {
        return R.layout.aty_imput_wallet_pr;
    }

    @Override
    public void setListener() {
        createSucceedDialog.setOkListener(() -> {
            finish();
            ImportWalletEvent walletEvent = new ImportWalletEvent();
            RxBusHelper.post(walletEvent);
        });
        //通知创建钱包
        RxBusHelper.doOnMainThread(ImportWalletEvent.class, new RxBusHelper.OnEventListener<ImportWalletEvent>() {
            @Override
            public void onEvent(ImportWalletEvent walletEvent) {
                Log.e("==========", "walletEvent:" + "弹出对话框");
                createSucceedDialog.show();
            }

            @Override
            public void onError(ErrorBean errorBean) {

            }
        });

        import_create_btn.setOnClickListener(v -> {

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

            pow.initStyle(R.drawable.empty, 6, 0.1f, R.color.color_999999, R.color.color_999999, 30);

            //密码输入完后的回调

            pow.setOnTextFinishListener(str -> {
                requestData(0);
            });

        });
    }

    @Override
    public void fillData() {
        addWalletNew.setTitle("添加钱包");
//        coin = getIntent().getStringExtra("coin");
        walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");

        flag = getIntent().getStringExtra("flag");
        createSucceedDialog = new CreateSucceedDialog(this);
//        Log.e("==========", "coin:" + coin);

        StatusBarUtil.setStatusColor(this, true, true, R.color.colorPrimary);

        if (walletBeans == null || walletBeans.size() < 1) {

            if ("0".equals(flag)) {
                walletBeans = new ArrayList<>();
                WalletBean walletBean = new WalletBean();
                walletBean.setCoin("CCM");
                walletBeans.add(walletBean);
            }
        } else {
            boolean existCCM = false;

            for (WalletBean walletBean : walletBeans) {
                if (walletBean.getCoin().equals("CCM")) {
                    existCCM = true;
                }
            }

            if (!existCCM) {
                WalletBean walletBean = new WalletBean();
                walletBean.setCoin("CCM");
                walletBeans.add(walletBean);
            }
        }


        importPrikeyAdapter = new ImportPrikeyAdapter(this, walletBeans, (view, pojo, position) -> {
//            Utils.Toast("点击了条目：" + position);
            if (pojo.getTokenAddress() != null && !"".equals(pojo.getTokenAddress())) {
                Utils.Toast("地址已经存在");
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("coin", pojo.getCoin());
                bundle.putString("state", "12");
                myStartActivity(ImputWalletPrivateAty.class, bundle);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lRecyclerView.setLayoutManager(manager);
        lRecyclerView.setOnRefreshListener(this);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(importPrikeyAdapter);
        lRecyclerView.setAdapter(lRecyclerViewAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 12) {
            createSucceedDialog.show();
        }
    }

    @Override
    public void onRefresh() {
        fillData();
        lRecyclerView.refreshComplete(3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillData();
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

                        StringBuilder sb = new StringBuilder();
                        byte[] entropy = new byte[Words.TWELVE.byteLength()];
                        new SecureRandom().nextBytes(entropy);
                        new MnemonicGenerator(English.INSTANCE)
                                .createMnemonic(entropy, sb::append);
                        String mnemonicWord = sb.toString();

                        walletBeans = walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");
//            判断钱包那些不存在，则会导入
                        if (walletBeans != null && walletBeans.size() > 0) {

                            boolean existCCM = false;

                            for (WalletBean walletBean : walletBeans) {
                                if (walletBean.getCoin().equals("CCM")) {
                                    existCCM = true;
                                }
                            }

                            if (!existCCM) {
                                new CCMUtils().createWallet(mnemonicWord, pow.getPwdText(), "CCM", Config.getMobleMapping(),"");
                            }

                        } else {
                            new CCMUtils().createWallet(mnemonicWord, pow.getPwdText(), "CCM", Config.getMobleMapping(),"");
                        }

//                        进入首页
                        myStartActivity(MainAty.class);
                        finish();
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
