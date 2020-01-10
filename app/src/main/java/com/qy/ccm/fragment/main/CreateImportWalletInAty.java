package com.qy.ccm.fragment.main;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qy.ccm.R;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.blockchain.BlockChainUtils;
import com.qy.ccm.utils.database.DatabaseWalletUtils;
import com.qy.ccm.utils.function.ScreenUtils;
import com.qy.ccm.utils.function.StringUtils;
import com.qy.ccm.utils.function.safety.AES256;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import java.security.SecureRandom;
import java.util.List;

import butterknife.BindView;
import io.github.novacrypto.bip39.MnemonicGenerator;
import io.github.novacrypto.bip39.Words;
import io.github.novacrypto.bip39.wordlists.English;

public class CreateImportWalletInAty extends BaseAty {

    @BindView(R.id.nll_wallet_market)
    NavigationLucencyLayout nll_wallet_market;

    private String coin;

    private Dialog dialog;

    @BindView(R.id.create_wallet_buttom_id)
    Button create_wallet_buttom_id;

    @BindView(R.id.wallet_name_id)
    EditText wallet_name_id;

    @Override
    public int initView() {
        return R.layout.aty_create_wallet_in;
    }

    @Override
    public void setListener() {
        create_wallet_buttom_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ("".equals(wallet_name_id.getText().toString().trim())) {
                    Utils.Toast("钱包名称不能为空");
                    return;
                }

                dialog = new Dialog(view.getContext(), R.style.DialogThemeHalfAlpa);
                View view1 = LayoutInflater.from(view.getContext()).inflate(R.layout.pow_dialog, null);
                dialog.setContentView(view1);
                dialog.setCanceledOnTouchOutside(true);
                Window dialogWindow = dialog.getWindow();

                WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//                                    p.height = DisplayUtil.dip2px(activity, 230f) // 高度设置为屏幕的0.6，根据实际情况调整
//                    p.width = DisplayUtil.dip2px(this.getActivity(), 300f); // 宽度设置为屏幕的0.65，根据实际情况调整
//                    dialogWindow.setAttributes(p);


                int hi = ScreenUtils.getScreenWidth(view.getContext());
//                p.width = DisplayUtil.dip2px(this, Float.valueOf(hi)); // 宽度设置为屏幕的0.65，根据实际情况调整
                p.width = hi; // 宽度设置为屏幕的0.65，根据实际情况调整
                dialogWindow.setAttributes(p);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);

                dialog.show();

                EditText pow = view1.findViewById(R.id.ppe_wallet_affirmpassword);
                Button pow_button = view1.findViewById(R.id.pow_button);
                ImageView powd_diss = view1.findViewById(R.id.powd_diss);

//                    pow.initStyle(R.drawable.edit_num_bg2, 6, 0.33f, R.color.black, R.color.black, 30);
                powd_diss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                //密码输入完后的回调
                pow_button.setOnClickListener(view3 -> {
                    String tradePwd = pow.getText().toString();
                    List<WalletBean> walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");
                    String testPass = "";
                    if (walletBeans.size() > 0) {
                        testPass = AES256.SHA256Decrypt(walletBeans.get(0).getPrivateKey(), pow.getText().toString());
                    }
                    if (!StringUtils.isEmpty(testPass)) {

                        //手动收起软键盘
                        //手动收起软键盘
                        InputMethodManager imm = (InputMethodManager)
                                view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(pow.getWindowToken(), 0);

                        dialog.dismiss();

                        createWallet(wallet_name_id.getText().toString().trim(), coin, tradePwd);

                    } else {
                        Utils.Toast("交易密码错误！请重新输入");
                        return;
                    }
                });


            }
        });
    }

    @Override
    public void fillData() {
        coin = getIntent().getStringExtra("coinName");
        nll_wallet_market.setTitle("");
        StatusBarUtil.setStatusColor(this, true, false, R.color.color_0692c2);
        StatusBarUtil.setStatusBarColor(this, R.color.color_0692c2);

    }

    private void createWallet(String name, String coin, String tradePwd) {
        StringBuilder sb = new StringBuilder();
        byte[] entropy = new byte[Words.TWELVE.byteLength()];
        new SecureRandom().nextBytes(entropy);
        new MnemonicGenerator(English.INSTANCE).createMnemonic(entropy, sb::append);
        String mnemonicWord = sb.toString();
        new CreateImportWalletInAty.MyAsyncTask().execute(name, mnemonicWord, coin, tradePwd);
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
            String name = strings[0];
            String mnemonicWord = strings[1];
            String coin = strings[2];
            String tradePwd = strings[3];
//            存在多个重载方法， 使用  1  来 进行标识
            Log.e("111111111111111", "-----------------");
            BlockChainUtils.importTheWallet_btc_eth(mnemonicWord, tradePwd, coin, "", name, 1);
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            dialogUtils.dismissProgressDialog();
            Utils.Toast("创建成功");
            finish();
        }
    }

}
