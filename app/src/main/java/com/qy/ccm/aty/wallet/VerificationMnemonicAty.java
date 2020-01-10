package com.qy.ccm.aty.wallet;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.qy.ccm.R;
import com.qy.ccm.adapter.wallet.VerificationMnemonicAdapter;
import com.qy.ccm.aty.account.RegisterAty;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.bean.my.MnemonicBean;
import com.qy.ccm.bean.other.rxbus.ImportWalletEvent;
import com.qy.ccm.config.Config;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.blockchain.BlockChainUtils;
import com.qy.ccm.utils.rxbus.RxBusHelper;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:助记词验证
 */
public class VerificationMnemonicAty extends BaseAty {
    @BindView(R.id.nll_verification_mnemonic)
    NavigationLucencyLayout nllVerificationMnemonic;
    @BindView(R.id.rv_verification_mnemonic)
    RecyclerView rvVerificationMnemonic;
    @BindView(R.id.tv_verification_mnemonic)
    TextView tvVerificationMnemonic;
    private String mnemonic;
    private String coin;

    @OnClick({R.id.tv_mnemonic_next})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_mnemonic_next:
                String tvMnemonic = tvVerificationMnemonic.getText().toString().trim();


//                这里做测试，所以暂时注释掉
                if (mnemonic.equals(tvMnemonic)) {
//                if (1 == 1) {

                    Utils.Toast("助记词验证成功");
                    if (coin == null) {
                        Bundle bundle = new Bundle();
                        bundle.putString("mnemonicWord", mnemonic);
                        myStartActivity(RegisterAty.class, bundle);
                        finish();
                    } else {
                        new MyAsyncTask().execute(mnemonic, coin);

                    }

                } else {
                    Utils.Toast("助记词验证失败");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public int initView() {
        return R.layout.aty_verification_mnemonic;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillData() {
        coin = getIntent().getStringExtra("coin");
        nllVerificationMnemonic.setTitle(getString(R.string.verification_mnemonic));
        mnemonic = getIntent().getStringExtra("mnemonic");
        List<String> mnemonicList = Arrays.asList(mnemonic.split(" "));
        List<MnemonicBean> mnemonicBeanList = new ArrayList<>();
        for (String context : mnemonicList) {
            MnemonicBean mnemonicBean = new MnemonicBean();
            mnemonicBean.setContext(context);
            mnemonicBean.setSelect(false);
            mnemonicBeanList.add(mnemonicBean);
        }
        Collections.shuffle(mnemonicBeanList);
        initAdapter(mnemonicBeanList);
    }

    private void initAdapter(List<MnemonicBean> mnemonicList) {
        VerificationMnemonicAdapter verificationMnemonicAdapter = new VerificationMnemonicAdapter(R.layout.item_verification_mnemonic, this, mnemonicList);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        rvVerificationMnemonic.setLayoutManager(manager);
        rvVerificationMnemonic.setAdapter(verificationMnemonicAdapter);

        verificationMnemonicAdapter.setOnItemClickListener((adapter, view, position) -> {
            MnemonicBean mnemonicBean = mnemonicList.get(position);
            if (mnemonicBean.isSelect()) {
                mnemonicBean.setSelect(false);
            } else {
                mnemonicBean.setSelect(true);
            }
            setMnemonic(mnemonicBean.getContext());
            verificationMnemonicAdapter.notifyDataSetChanged();
        });

    }


    /**
     * 输入助记词
     */
    private void setMnemonic(String mnWord) {
        String s = tvVerificationMnemonic.getText().toString().trim();

        if (mnWord == null) {
            mnWord = "";
        } else if (!s.contains(mnWord)) {
            tvVerificationMnemonic.setText(s + " " + mnWord);
        } else {
            String sLeft, sRight;

            int pos = s.indexOf(mnWord);
            if (pos == 0) {
                sLeft = "";
            } else {
                sLeft = s.substring(0, pos - 1).trim();
            }

            if (pos + mnWord.length() >= s.length()) {
                sRight = "";
            } else {
                sRight = s.substring(pos + mnWord.length()).trim();
            }

            s = sLeft + " " + sRight;
            tvVerificationMnemonic.setText(s);
        }


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
            String coin = strings[1];
            String transactionPassword = Config.getTransactionPassword();
            BlockChainUtils.importTheWallet(mnemonicWord, transactionPassword, coin,"");
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            dialogUtils.dismissProgressDialog();
            ImportWalletEvent walletEvent = new ImportWalletEvent();
            RxBusHelper.post(walletEvent);
            finish();
        }
    }
}
