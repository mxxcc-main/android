package com.qy.ccm.aty.my;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.qy.ccm.R;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.config.Config;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.blockchain.BlockChainUtils;
import com.qy.ccm.utils.blockchain.btc.BtcUtils;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import java.security.SecureRandom;
import java.util.List;

import butterknife.BindView;
import io.github.novacrypto.bip39.MnemonicGenerator;
import io.github.novacrypto.bip39.Words;
import io.github.novacrypto.bip39.wordlists.English;

public class CreBtcAty extends BaseAty {
    @BindView(R.id.tv_crebtc_name)
    TextView tv_crebtc_name;

    @BindView(R.id.btn_crebtc_start)
    Button btn_crebtc_start;

    @Override
    public int initView() {
        return R.layout.aty_cre_btc;
    }

    @Override
    public void setListener() {

    }
    private String mnemonicWord = "";
    private String stringaddress;
    @Override
    public void fillData() {
        StatusBarUtil.setStatusColor(this, true, true, R.color.color_fbfbfd);

        btn_crebtc_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testCreateBtcWallet();

                testCreateEthWallet();

            }
        });
    }

    private void testCreateBtcWallet(){
        List<WalletBean> getWalletList = BtcUtils.getLocalBTC("BTC");
        if (getWalletList != null && getWalletList.size() > 0) {
            Toast.makeText(this,"BTC钱包已存在",Toast.LENGTH_SHORT).show();
        }else{
            Config.setMobleMapping("");
            StringBuilder sb = new StringBuilder();
            byte[] entropy = new byte[Words.TWELVE.byteLength()];
            new SecureRandom().nextBytes(entropy);
            new MnemonicGenerator(English.INSTANCE).createMnemonic(entropy, sb::append);
            mnemonicWord = sb.toString();
            createWallet_BTC(tv_crebtc_name.getText().toString(),"2018liuyan");
        }
    }


    private void testCreateEthWallet(){
        List<WalletBean> getWalletList = BtcUtils.getLocalBTC("ETH");
        if (getWalletList != null && getWalletList.size() > 0) {
            Toast.makeText(this, "ETH钱包已存在", Toast.LENGTH_SHORT).show();
        }else{
            Config.setMobleMapping("");
            StringBuilder sb = new StringBuilder();
            byte[] entropy = new byte[Words.TWELVE.byteLength()];
            new SecureRandom().nextBytes(entropy);
            new MnemonicGenerator(English.INSTANCE).createMnemonic(entropy, sb::append);
            mnemonicWord = sb.toString();
            createWallet_ETH(tv_crebtc_name.getText().toString(), "2019liuyan");
        }
    }

    private void createWallet_ETH(String name,String pwd) {
        new MyAsyncTask_ETH().execute(mnemonicWord, name,pwd);
    }


    private void createWallet_BTC(String name,String pwd) {
        new MyAsyncTask_CCM().execute(mnemonicWord, name,pwd);
    }

    class MyAsyncTask_CCM extends AsyncTask<String, Void, Bitmap> {

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
            String name = strings[1];
            String pwd = strings[2];
//            存在多个重载方法， 使用  1  来 进行标识
            BlockChainUtils.importTheWallet_btc_eth(mnemonicWord, "2018liuyan", "BTC","", name,1);
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            dialogUtils.dismissProgressDialog();
            Utils.Toast("注册成功，请登录");
            finish();
        }
    }


    class MyAsyncTask_ETH extends AsyncTask<String, Void, Bitmap> {

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
            String name = strings[1];
            String pwd = strings[2];
//            存在多个重载方法， 使用  1  来 进行标识
            Log.e("111111111111111","-----------------");
            BlockChainUtils.importTheWallet_btc_eth(mnemonicWord, "2018liuyan", "ETH", "",name,1);
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            dialogUtils.dismissProgressDialog();
            Utils.Toast("注册成功，请登录");
            finish();
        }
    }
}