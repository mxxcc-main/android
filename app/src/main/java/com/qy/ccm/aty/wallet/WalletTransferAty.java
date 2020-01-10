package com.qy.ccm.aty.wallet;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qy.ccm.R;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.bean.other.entity.WalletToken;
import com.qy.ccm.bean.wallet.BtcTransactionBean;
import com.qy.ccm.config.BtcConfig;
import com.qy.ccm.config.Config;
import com.qy.ccm.constants.UrlConstants;
import com.qy.ccm.model.FeeModel;
import com.qy.ccm.model.TransferModel;
import com.qy.ccm.model.WalletModel;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.blockchain.btc.BtcUtils;
import com.qy.ccm.utils.blockchain.eth.CCMUtils;
import com.qy.ccm.utils.function.ScreenUtils;
import com.qy.ccm.utils.function.StringUtils;
import com.qy.ccm.utils.function.TimeUtils;
import com.qy.ccm.utils.function.WidgetUtils;
import com.qy.ccm.utils.function.safety.AES256;
import com.qy.ccm.utils.system.PermissionUtils;
import com.qy.ccm.view.group.NavigationLucencyLayout;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.OnClick;

import static ccm.web3j.tx.Transfer.GAS_LIMIT;

/**
 * Description:交易页
 * Data：2019/5/10-2:51 PM
 */
public class WalletTransferAty extends BaseAty {
    private final BigDecimal MAX_NUM = new BigDecimal(100);

    @BindView(R.id.nll_wallet_transafer)
    NavigationLucencyLayout nllWalletTransfer;
    @BindView(R.id.et_transfer_state)
    EditText etTransferState;
    @BindView(R.id.et_transfer_amount)
    EditText etTransferAmunt;
    @BindView(R.id.sb_wallet_transfer)
    SeekBar sbWalletTransfer;
    @BindView(R.id.tv_transfer_fee)
    TextView tvTransferFee;
    @BindView(R.id.tv_trafser_fee1)
    TextView tvTransferFee1;
    @BindView(R.id.tv_trafser_fee2)
    TextView tvTransferFee2;
    @BindView(R.id.tv_transfer_amount)
    TextView tvTransferAmount;


    WalletToken walletToken;
    private String tokenName;

    private FeeHandler mhandler = new FeeHandler();

    class FeeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (btcUtils.utxos == null || btcUtils.fastestFee == null) {
                        mhandler.sendEmptyMessageDelayed(1, 100);
                        Log.e("==========", "btcUtils:" + btcUtils.utxos + " btcUtils.fastestFee:" + btcUtils.fastestFee);
                    } else {
                        Log.e("==========", "成功:");
                        dialogUtils.dismissProgressDialog();
                        minFee = new BigDecimal(btcUtils.getFee(amountLong, btcUtils.hourFee, btcUtils.utxos));
                        targetFee = new BigDecimal(btcUtils.getFee(amountLong, btcUtils.halfHourFee, btcUtils.utxos));
                        maxFee = new BigDecimal(btcUtils.getFee(amountLong, btcUtils.fastestFee, btcUtils.utxos));
                        middle = maxFee.subtract(minFee).divide(MAX_NUM);
                        btcFee();
                        btcUtils.utxos = null;
                        btcUtils.fastestFee = null;
                    }
                    break;
                case 2:
                    if (!btcUtils.isUtxos) {
                        mhandler.sendEmptyMessageDelayed(2, 100);
                    } else {
                        dialogUtils.dismissProgressDialog();
                        btcHash();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void btcFee() {
//        BigDecimal fee = minFee.multiply(feePrice);
//        model.feeDisplay(tvTransferFee, tokenName, fee, feePrice);
        int schedule = (targetFee.subtract(minFee)).divide(middle, 0, BigDecimal.ROUND_HALF_UP).intValue();
        sbWalletTransfer.setProgress(schedule);
    }


    private void btcHash() {
        Map<String, String> map = new HashMap<>();
        String hash = null;
        Long targetFeeLong = targetFee.longValue();
        String amount = getAmount();
        BigInteger amountBig = BtcUtils.btcFormatSat(new BigDecimal(amount));
        amountLong = amountBig.longValue();
        dialogUtils.showProgressDialog();
        switch (tokenName) {
            case "USDT":
                try {
                    hash = btcUtils.omniSign(sendAddress, getReceiveAddress(), privateKey, amountLong, targetFeeLong, BtcConfig.USDT_TAG, btcUtils.utxos);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "BTC":
                try {
                    hash = btcUtils.sign(sendAddress, getReceiveAddress(), privateKey, amountLong, targetFeeLong, btcUtils.utxos);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        Log.e("================", "hash:" + hash);
        Log.e("================", "hash:" + hash);
        map.put("signedTransaction", hash);
        postHttp(UrlConstants.USDT_SEND, map);
//        String url = BTC_BASE_API + "pushtx";
//        map = new HashMap();
//        map.put("tx", hash);
//
//        postHttp(url, map);
//        btcUtils.sendTransaction(hash);
    }

    private Dialog dialog;
    private EditText pow;

    private Button pow_button;
    private ImageView powd_diss;

    /**
     * BTC
     */
    private BtcUtils btcUtils;
    private long amountLong;
    private BigDecimal minFee;
    private BigDecimal maxFee;
    private BigDecimal targetFee;
    private BigDecimal middle;
    private BigDecimal feePrice;
    private FeeModel model;
    private String txhash;
    private String sendAddress;
    private String privateKey;


    @OnClick({R.id.tv_wallet_transfer, R.id.iv_transfer_code})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_wallet_transfer:
                String state = etTransferState.getText().toString().trim();
                String amount = etTransferAmunt.getText().toString().trim();
                if (StringUtils.isEmpty(state)) {
                    Utils.Toast("请输入对方地址");
                } else if (StringUtils.isEmpty(amount)) {
                    Utils.Toast("请输入金额");
                } else {

//                    这里进行预先判断余额够不够
                    switch (tokenName) {
                        case "CCM":
//                            String currAccountBalance = new EthUtils().getBalance(walletToken.getTokenAddress()).toPlainString();
//                            if (new BigDecimal(getAmount()).add(EthUtils.formatEth(targetFee)).compareTo(new BigDecimal(currAccountBalance)) == 1) {
//                                Utils.Toast("CCM 余额不足。");
//                                return;
//                            }

                            break;
                        default:
                            break;
                    }


                    //弹框输入交易密码
                    dialog = new Dialog(this, R.style.DialogThemeHalfAlpa);
                    View view1 = LayoutInflater.from(this).inflate(R.layout.pow_dialog, null);
                    dialog.setContentView(view1);
                    dialog.setCanceledOnTouchOutside(true);
                    Window dialogWindow = dialog.getWindow();

                    WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//                                    p.height = DisplayUtil.dip2px(activity, 230f) // 高度设置为屏幕的0.6，根据实际情况调整

//                p.width = DisplayUtil.dip2px(this, Float.valueOf(hi)); // 宽度设置为屏幕的0.65，根据实际情况调整
                    p.width = ScreenUtils.getScreenWidth(this);
                    ; // 宽度设置为屏幕的0.65，根据实际情况调整
                    dialogWindow.setAttributes(p);

                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);

                    dialog.show();

                    pow = view1.findViewById(R.id.ppe_wallet_affirmpassword);
                    pow_button = view1.findViewById(R.id.pow_button);
                    powd_diss = view1.findViewById(R.id.powd_diss);

//                    pow.initStyle(R.drawable.empty, 6, 0.5f, R.color.black, R.color.black, 30);

                    //密码输入完后的回调
                    powd_diss.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    pow_button.setOnClickListener(view -> {

                        privateKey = walletToken.getPrivateKey();
                        privateKey = AES256.SHA256Decrypt(privateKey, pow.getText().toString());
//                        requestData(0);

                        if (!StringUtils.isEmpty(privateKey)) {
                            dialog.dismiss();
                            //手动收起软键盘
                            InputMethodManager imm = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(pow.getWindowToken(), 0);

                            walletTransaction();

                        } else {
                            Utils.Toast("交易密码错误！请重新输入");
                        }

                    });
                }
                break;
            case R.id.iv_transfer_code:
                scanCode();
                break;
            default:
                break;
        }
    }

    @Override
    public int initView() {
        return R.layout.aty_wallet_transfer;
    }

    @Override
    public void setListener() {
        sbWalletTransfer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (minFee == null) {
                    return;
                }
                if (progress == 0) {
                    model.feeDisplay(tvTransferFee, tokenName, minFee, feePrice);
                } else {
                    BigDecimal progressBig = new BigDecimal(progress);
                    BigDecimal fee = middle.multiply(progressBig).add(minFee);
                    targetFee = fee;

                    model.feeDisplay(tvTransferFee, tokenName, fee, feePrice);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        etTransferAmunt.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus == false) {
                Log.e("==========", "离开了:");
//                    getTokenFee();
            }
        });
    }

    @Override
    public void fillData() {

        nllWalletTransfer.setTitle("转账");
        StatusBarUtil.setStatusColor(this, true, true, R.color.colorPrimary);
        initData();
        String tokenHint = "请输入金额(" + tokenName + ")";
        Utils.edtSpannable(etTransferAmunt, tokenHint);
        btcUtils = new BtcUtils();

        getTokenFee();
    }

    private void initData() {

        walletToken = (WalletToken) getIntent().getSerializableExtra("walletToken");
        tokenName = walletToken.getTokenName();
        privateKey = walletToken.getPrivateKey();
        privateKey = AES256.SHA256Decrypt(privateKey, Config.getTransactionPassword());
        sendAddress = walletToken.getTokenAddress();
        tvTransferAmount.setText("余额:" + walletToken.getAmount() + " " + tokenName);
    }

    private void walletTransaction() {

        switch (tokenName) {

            case "CCM":
            case "ETH":
                new MyAsyncTask(WalletTransferAty.this, dialogUtils, targetFee, tokenName, privateKey, getReceiveAddress(), getAmount(), txhash).execute(tokenName, privateKey, getReceiveAddress(), getAmount());
                break;

            case "USDT":
            case "BTC":
                btcUtils.utxos = null;
                dialogUtils.showProgressDialog();
                mhandler.sendEmptyMessageDelayed(2, 100);
                btcUtils.getUsdtList(sendAddress);
                break;

            default:
                Boolean ethTokenState = new WalletModel().token(tokenName);
                if (ethTokenState == true) {
                    new MyAsyncTask(WalletTransferAty.this, dialogUtils, targetFee, tokenName, privateKey, getReceiveAddress(), getAmount(), txhash).execute(tokenName, privateKey, getReceiveAddress(), getAmount());
                }
                break;
        }
    }

    private void ethGas() {

        minFee = new BigDecimal("0.00008").multiply(new BigDecimal("1000000000000000000"));
        maxFee = new BigDecimal("0.00042").multiply(new BigDecimal("1000000000000000000"));
        targetFee = new BigDecimal("0.00021").multiply(new BigDecimal("1000000000000000000"));

        middle = maxFee.subtract(minFee).divide(MAX_NUM);
        BigDecimal difference = targetFee.subtract(minFee);
        int schedule = difference.divide(middle, 0, BigDecimal.ROUND_HALF_UP).intValue();
        Log.e("==========", "schedule:" + schedule);
        sbWalletTransfer.setProgress(schedule);
        BigDecimal targetFeeBig = CCMUtils.formatEth(targetFee.toBigIntegerExact());
        BigDecimal minFeeBig = CCMUtils.formatEth(minFee.toBigIntegerExact());
        BigDecimal maxFeeBig = CCMUtils.formatEth(maxFee.toBigIntegerExact());

        if (!tokenName.equals("ETH")) {
            tvTransferFee.setText(StringUtils.bigDecimal8(targetFeeBig) + " " + "CCM");
            tvTransferFee1.setText(StringUtils.bigDecimal8(minFeeBig) + " " + "CCM");
            tvTransferFee2.setText(StringUtils.bigDecimal8(maxFeeBig) + " " + "CCM");
        }

       else{
            tvTransferFee.setText(StringUtils.bigDecimal8(targetFeeBig) + " " + tokenName);
            tvTransferFee1.setText(StringUtils.bigDecimal8(minFeeBig) + " " + tokenName);
            tvTransferFee2.setText(StringUtils.bigDecimal8(maxFeeBig) + " " + tokenName);
        }

    }

    @Override
    public void onHttpFault(String result, String url) {

        super.onHttpFault(result, url);

//        Utils.Toast(result);

    }


    private void initBtcFee() {

        tvTransferFee1.setText("0.000033 " + tokenName);
        tvTransferFee2.setText("0.00011 " + tokenName);
        tvTransferFee.setText("0.000045 " + tokenName);
        minFee = new BigDecimal(3300);
        targetFee = new BigDecimal(4500);
        maxFee = new BigDecimal(11000);
        middle = maxFee.subtract(minFee).divide(MAX_NUM);
        btcFee();

    }

    /**
     * 获取钱包手续费
     */
    private void getTokenFee() {
        model = new FeeModel();
        feePrice = new BigDecimal(1);
        switch (tokenName) {
            case "BTC":
            case "USDT":
                initBtcFee();
                break;
            case "ETH":
            case "CCM":
                ethGas();
                break;
            default:
                Boolean ethTokenState = new WalletModel().token(tokenName);
                if (ethTokenState == true) {
                    ethGas();
                }

                break;
        }
    }

    /**
     * 获取CCM手续费
     */
//    private void getWalletEthFee() {
//        dialogUtils.showProgressDialog();
//        String url = UrlConstants.ETHGAS_API;
//        getHttp(url, null);
//    }
    private String getAmount() {
        return etTransferAmunt.getText().toString();
    }

    private String getReceiveAddress() {
        return etTransferState.getText().toString();
    }


    /**
     * Local wallet asynchrony
     */
    private static class MyAsyncTask extends AsyncTask<String, Void, String> {
        private WeakReference<WalletTransferAty> mWeakReference;
        WidgetUtils dialogUtils;
        BigDecimal targetFee;
        BigDecimal fee;
        String tokenName;
        String privateKey;
        String receiveAddress;
        String amount;
        String txhash;

        public MyAsyncTask(Context context, WidgetUtils dialogUtils, BigDecimal targetFee, String tokenName, String privateKey, String receiveAddress, String amount, String txhash) {
            mWeakReference = new WeakReference<>((WalletTransferAty) context);
            this.dialogUtils = dialogUtils;
            this.targetFee = targetFee;
            this.tokenName = tokenName;
            this.privateKey = privateKey;
            this.receiveAddress = receiveAddress;
            this.amount = amount;
            this.txhash = txhash;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //onPreExecute用于异步处理前的操作
            dialogUtils.showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            //在doInBackground方法中进行异步任务的处理.
            //获取传进来的参数
            TransferModel transferModel = new TransferModel();
            String msg;
            try {
                BigInteger GasPrice = targetFee.toBigIntegerExact().divide(GAS_LIMIT);
                fee = CCMUtils.formatEth(targetFee);
                msg = transferModel.localTransafer(tokenName, privateKey, receiveAddress, amount, GasPrice);
            } catch (ExecutionException e) {
                e.printStackTrace();
                msg = e.toString();
            } catch (InterruptedException e) {
                e.printStackTrace();
                msg = e.toString();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            super.onPostExecute(msg);
            //onPostExecute用于UI的更新.此方法的参数为doInBackground方法返回的值
            dialogUtils.dismissProgressDialog();
            txhash = msg;
            WalletTransferAty walletTransferInfoAty = mWeakReference.get();
            walletTransferInfoAty.jumpSucceed(txhash, StringUtils.bigDecimal8(fee));
        }
    }

    /**
     * Jump a new page
     */
    public void jumpSucceed(String txhash, String fee) {

        if (!txhash.startsWith("0x")) {
            Utils.Toast("余额不足");
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("tokenName", tokenName);
        bundle.putString("sendAddress", sendAddress);
        bundle.putString("receiveAddress", getReceiveAddress());
        bundle.putString("amount", getAmount());
        bundle.putString("tokenAmount", walletToken.getAmount());
        bundle.putString("txhash", txhash);
        bundle.putString("fee", fee);
        String time = TimeUtils.getCurrentDateStr(TimeUtils.FORMAT_TYPE_5);
        bundle.putString("time", time);
        Utils.startActivity(this, WalletTransferSuccessAty.class, bundle);
        finish();

    }

    public void scanCode() {
        if (PermissionUtils.permissionJudge(this, PermissionUtils.CAMERA)) {
            codePager();
        } else {
            rxPermissions
                    .request(Manifest.permission.CAMERA)
                    .subscribe(granted -> {
                        // Always true pre-M
                        if (granted) {
                            codePager();
                        } else {
                            Utils.Toast("没有相机的使用权限");
                        }
                    });
        }
    }

    /**
     * 二维码
     */
    private void codePager() {
        Intent intent = new Intent(this, MyCodeAty.class);
        startActivityForResult(intent, 11);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    etTransferState.setText(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Utils.Toast("解析二维码失败");
                }
            }
        }
    }

    @Override
    public void onHttpSuccess(String result, String url) {
        super.onHttpSuccess(result, url);
        dialogUtils.dismissProgressDialog();
//        if (url.equals("pushtx")) {
//            btcTransfer(result);
//        }
        if (url.equals(UrlConstants.USDT_SEND)) {
            btcTransfer(result);
        }


    }

    /**
     * 提交交易事务
     */
    private void btcTransfer(String result) {
        BtcTransactionBean createBean = new Gson().fromJson(result, BtcTransactionBean.class);
        String state = createBean.getStatus();
        if (state != null && "OK".equals(state)) {
            txhash = createBean.getTx();
            jumpSucceed(txhash, StringUtils.bigDecimal8(BtcUtils.btcFormat(targetFee)));
        } else {
            Utils.Toast(state);
        }
    }

}
