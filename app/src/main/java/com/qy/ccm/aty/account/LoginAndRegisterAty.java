package com.qy.ccm.aty.account;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qy.ccm.R;
import com.qy.ccm.aty.MainAty;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.aty.wallet.ImputWalletPriAty;
import com.qy.ccm.bean.other.database.UserMobileBean;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.config.Config;
import com.qy.ccm.retrofit.Constants;
import com.qy.ccm.retrofit.HttpRequestCallback;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.FastJsonUtils;
import com.qy.ccm.utils.FontUtil;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.blockchain.BlockChainUtils;
import com.qy.ccm.utils.blockchain.eth.CCMUtils;
import com.qy.ccm.utils.database.DatabaseUserMobileUtils;
import com.qy.ccm.utils.database.DatabaseWalletUtils;
import com.qy.ccm.utils.function.WidgetUtils;

import org.json.JSONObject;
import ccm.web3j.crypto.CipherException;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.novacrypto.bip39.MnemonicGenerator;
import io.github.novacrypto.bip39.Words;
import io.github.novacrypto.bip39.wordlists.English;

/**
 * 登录页
 */
public class LoginAndRegisterAty extends BaseAty implements HttpRequestCallback<Object> {

    @BindView(R.id.login_module)
    TextView loginModule;
    @BindView(R.id.module_register_module)
    TextView moduleRegisterModule;

    //    登录
    @BindView(R.id.login_img_s)
    ImageView loginImgS;
    @BindView(R.id.register_imgs)
    ImageView registerImgS;
    @BindView(R.id.login_mobile_number)
    EditText loginMobileNumber;
    @BindView(R.id.login_pass_id)
    EditText loginPassId;
    @BindView((R.id.is_show_password))
    ImageView isShowPassword;
    @BindView(R.id.login_sub_btn)
    Button loginSubBtn;

    @BindView(R.id.form_sub_id)
    RelativeLayout form_sub_id;

    @BindView(R.id.rl_top_lr_id)
    RelativeLayout rl_top_lr_id;

    @BindView(R.id.root_rl_id)
    RelativeLayout root_rl_id;

    @BindView(R.id.login_rl_id)
    RelativeLayout loginLrId;
    Boolean isShowLoginPass = false;
    Boolean isShowRegPass = false;
    Boolean isShowRegConfPass = false;
    Boolean isShowTradePassPass = false;
    Boolean isShowTradeConfPassPass = false;

    //    注册
    @BindView(R.id.register_lr_id)
    RelativeLayout registerLrId;
    @BindView(R.id.register_mobile_id)
    EditText registerMobileId;
    @BindView(R.id.register_pass_text_id)
    EditText register_pass_text_id;
    @BindView(R.id.pass_show_id_icon)
    ImageView pass_show_id_icon;
    @BindView(R.id.reg_conf_psss_id)
    EditText reg_conf_psss_id;
    @BindView(R.id.confirm_pass_show_id)
    ImageView confirm_pass_show_id;
    @BindView(R.id.register_btn_id)
    Button registerBtnId;
    InputMethodManager imm;
//    Step2

    @BindView(R.id.register_lr_step2_id)
    RelativeLayout register_lr_step2_id;
    @BindView(R.id.register_lr_step3_id)
    RelativeLayout register_lr_step3_id;
    @BindView(R.id.trade_pass_id)
    EditText trade_pass_id;
    @BindView(R.id.show_trade_pass_icon)
    ImageView show_trade_pass_icon;
    @BindView(R.id.register_trade_pass_text_id)
    EditText register_trade_pass_text_id;
    @BindView(R.id.pass_trade_show_id_icon)
    ImageView pass_trade_show_id_icon;
    @BindView(R.id.invite_code)
    EditText invite_code;
    @BindView(R.id.answer_text)
    EditText answer_text;
    @BindView(R.id.tv_q8)
    AppCompatSpinner tv_q8;
    @BindView(R.id.register_btn_step2_id)
    Button register_btn_step2_id;
    @BindView(R.id.register_btn_step2_next_id)
    Button register_btn_step2_next_id;
    //    List<UserMobileBean> userMobileBeans;
    //    助剂词  设置为 空 字符串
    private String mnemonicWord = "";
    String tra1 = "";
    String tra2 = "";
    private List<WalletBean> walletBeans;
    String quesTion = "";
    String answer = "";

//    private WidgetUtils switchDialog0;
//    private WidgetUtils switchDialog1;

    @OnClick({R.id.confirm_pass_show_id, R.id.register_btn_step2_id, R.id.register_btn_id, R.id.pass_show_id_icon, R.id.rl_top_lr_id, R.id.module_register_module, R.id.is_show_password, R.id.login_sub_btn, R.id.show_trade_pass_icon, R.id.pass_trade_show_id_icon, R.id.forget_pass, R.id.register_btn_step2_next_id})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_top_lr_id:
                loginImgS.setVisibility(View.VISIBLE);
                registerImgS.setVisibility(View.INVISIBLE);
                loginLrId.setVisibility(View.VISIBLE);
                registerLrId.setVisibility(View.GONE);
                register_lr_step2_id.setVisibility(View.GONE);
                register_lr_step3_id.setVisibility(View.GONE);
//                truncate   reg module data
                registerMobileId.setText("");
                register_pass_text_id.setText("");
                reg_conf_psss_id.setText("");
                trade_pass_id.setText("");
                register_trade_pass_text_id.setText("");
                invite_code.setText("");
                break;
            case R.id.module_register_module:
                loginImgS.setVisibility(View.INVISIBLE);
                registerImgS.setVisibility(View.VISIBLE);
                loginLrId.setVisibility(View.GONE);
                registerLrId.setVisibility(View.VISIBLE);
                register_lr_step2_id.setVisibility(View.GONE);
                register_lr_step3_id.setVisibility(View.GONE);

//                truncate login data
                loginMobileNumber.setText("");
                loginPassId.setText("");

                break;
            case R.id.register_btn_step2_next_id:
                if (register_trade_pass_text_id.getText().toString() != null && trade_pass_id.getText().toString() != null && trade_pass_id.getText().toString().equals(register_trade_pass_text_id.getText().toString())) {
                } else {
                    Utils.Toast("请输入正确的交易密码");
                    return;
                }

                tra1 = register_pass_text_id.getText().toString();
                tra2 = reg_conf_psss_id.getText().toString();
                if (tra1 == null || "".equals(tra1.trim()) || tra2 == null || "".equals(tra2.trim()) || !tra1.trim().equals(tra2.trim())) {
                    Utils.Toast("请输入正确的交易密码");
                    return;
                }

                loginImgS.setVisibility(View.INVISIBLE);
                registerImgS.setVisibility(View.VISIBLE);
                loginLrId.setVisibility(View.GONE);
                registerLrId.setVisibility(View.VISIBLE);
                register_lr_step2_id.setVisibility(View.GONE);
                register_lr_step3_id.setVisibility(View.VISIBLE);


                break;
            case R.id.is_show_password:

//                登录密码是否显示
                if (!isShowLoginPass) {
                    loginPassId.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowPassword.setImageResource(R.mipmap.icon_found_eyes);
                    isShowLoginPass = true;
                } else {
                    loginPassId.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isShowPassword.setImageResource(R.mipmap.icon_close);
                    isShowLoginPass = false;
                }
                break;
            case R.id.login_sub_btn:
                //手动收起软键盘
                imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(loginSubBtn.getWindowToken(), 0);
//                登录时的相关操作
//                Utils.Toast("点击了登录按钮");
                if (loginMobileNumber.getText().toString() != null && !loginMobileNumber.getText().toString().equals("") && loginMobileNumber.getText().toString().length() == 11) {
//                    userMobileBeans = DatabaseUserMobileUtils.getMobileList(loginMobileNumber.getText().toString());

//                    if (userMobileBeans == null || userMobileBeans.size() < 1) {
//                        Utils.Toast("手机号不存在，请直接注册");
//                        break;
//                    } else {


//         登录请求
//                    String password = userMobileBeans.get(0).getLoginPass();

                    if (loginPassId.getText() == null || "".equals(loginPassId.getText().toString().trim())) {
                        Utils.Toast("请输入正确的登录密码");
                        return;
                    }

                    if (Constants.isOnLine) {
                        requestData(1);

                    } else {
                        Config.setMobleMapping(loginMobileNumber.getText().toString());
                        Config.setTransactionPassword("123456");

                        StringBuilder sb = new StringBuilder();
                        byte[] entropy = new byte[Words.TWELVE.byteLength()];
                        new SecureRandom().nextBytes(entropy);
                        new MnemonicGenerator(English.INSTANCE)
                                .createMnemonic(entropy, sb::append);
                        String mnemonicWord = sb.toString();
                        try {
                            new CCMUtils().createWallet(mnemonicWord, Config.getTransactionPassword(), "CCM", Config.getMobleMapping(),"");
                        } catch (CipherException e) {
                            e.printStackTrace();
                        }
                        myStartActivity(MainAty.class);
                        finish();

                    }

                    break;

                } else {
                    Utils.Toast("请输入正确的手机号");
                    break;

                }

            case R.id.confirm_pass_show_id:

                if (!isShowLoginPass) {
                    reg_conf_psss_id.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirm_pass_show_id.setImageResource(R.mipmap.icon_found_eyes);
                    isShowLoginPass = true;
                } else {
                    reg_conf_psss_id.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirm_pass_show_id.setImageResource(R.mipmap.icon_close);
                    isShowLoginPass = false;
                }
                break;
            case R.id.pass_show_id_icon:

                if (!isShowRegPass) {
                    register_pass_text_id.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pass_show_id_icon.setImageResource(R.mipmap.icon_found_eyes);
                    isShowRegPass = true;
                } else {
                    register_pass_text_id.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pass_show_id_icon.setImageResource(R.mipmap.icon_close);
                    isShowRegPass = false;
                }
                break;
            case R.id.show_trade_pass_icon:

                if (!isShowTradePassPass) {
                    trade_pass_id.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    show_trade_pass_icon.setImageResource(R.mipmap.icon_found_eyes);
                    isShowTradePassPass = true;
                } else {
                    trade_pass_id.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    show_trade_pass_icon.setImageResource(R.mipmap.icon_close);
                    isShowTradePassPass = false;
                }
                break;
            case R.id.pass_trade_show_id_icon:

                if (!isShowTradeConfPassPass) {
                    register_trade_pass_text_id.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pass_trade_show_id_icon.setImageResource(R.mipmap.icon_found_eyes);
                    isShowTradeConfPassPass = true;
                } else {
                    register_trade_pass_text_id.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pass_trade_show_id_icon.setImageResource(R.mipmap.icon_close);
                    isShowTradeConfPassPass = false;
                }
                break;

            case R.id.register_btn_id:


//                下一步
                if (registerMobileId.getText().toString() != null && !registerMobileId.getText().toString().equals("") && registerMobileId.getText().toString().length() == 11) {
//                    List<UserMobileBean> userMobileBeans = DatabaseUserMobileUtils.getMobileList(registerMobileId.getText().toString());

//                    if (userMobileBeans != null && userMobileBeans.size() >= 1) {
//                        Utils.Toast("手机号已经存在，请直接登录");
//                        break;
//                    }
                } else {
                    Utils.Toast("请输入正确的手机号");
                    break;

                }

                String pow1 = register_pass_text_id.getText().toString();
                String pow2 = reg_conf_psss_id.getText().toString();
                if (pow1 == null || "".equals(pow1.trim()) || pow2 == null || "".equals(pow2.trim()) || !pow1.trim().equals(pow2.trim())) {
                    Utils.Toast("请输入正确的登录密码");
                    return;
                }
                Bundle bundle = new Bundle();
//                助剂词设置为字符串mnemonic
//                bundle.putString("mnemonicWord", "mnemonic");
//                bundle.putString("password", pow1);
//                myStartActivity(WalletPasswordAty.class, bundle);

                register_trade_pass_text_id.setTransformationMethod(PasswordTransformationMethod.getInstance());
                trade_pass_id.setTransformationMethod(PasswordTransformationMethod.getInstance());

                loginLrId.setVisibility(View.GONE);
                registerLrId.setVisibility(View.GONE);
                register_lr_step2_id.setVisibility(View.VISIBLE);
                break;
            case R.id.register_btn_step2_id:

                quesTion = tv_q8.getSelectedItem().toString();

                answer = answer_text.getText().toString();

                if (answer == null || "".equals(answer.trim())) {
                    Utils.Toast("请输入所选问题的答案");
                    return;
                }


//                TODO 请求创建账号
                //手动收起软键盘
                imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(register_btn_step2_id.getWindowToken(), 0);
//                Utils.Toast("注册");
                if (register_trade_pass_text_id.getText().toString() != null && trade_pass_id.getText().toString() != null && trade_pass_id.getText().toString().equals(register_trade_pass_text_id.getText().toString())) {

                    if (Constants.isOnLine) {
//                请求服务器  将用户数据发送给服务器
                        requestData(0);
                    } else {
                        Config.setMobleMapping(registerMobileId.getText().toString());
                        Config.setTransactionPassword("123456");
                        StringBuilder sb = new StringBuilder();
                        byte[] entropy = new byte[Words.TWELVE.byteLength()];
                        new SecureRandom().nextBytes(entropy);
                        new MnemonicGenerator(English.INSTANCE)
                                .createMnemonic(entropy, sb::append);
                        mnemonicWord = sb.toString();
//                    mnemonicWord = userMobileBean.getMobileMapping();
                        Utils.Toast("注册成功");
                        createWallet(registerMobileId.getText().toString());

                    }

                } else {
                    Utils.Toast("请输入正确的交易密码");
                }
                break;

            case R.id.forget_pass:
//                bundle = new Bundle();
//                myStartActivity(ForgetPasswordAty.class, bundle);
//                break;
            default:
                break;
        }
    }

    @Override
    public int initView() {
        return R.layout.aty_login_and_register;
    }

    @Override
    public void setListener() {
        //因为系统没有直接监听软键盘API，所以就用以下方法
        loginMobileNumber.getViewTreeObserver().addOnGlobalLayoutListener(
                () -> { //当界面大小变化时，系统就会调用该方法
                    Rect r = new Rect();
                    //获取当前界面可视部分
                    getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                    //获取屏幕的高度
                    int screenHeight = getWindow().getDecorView().getRootView().getHeight();
                    //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                    int heightDifference = screenHeight - r.bottom;
                    if (heightDifference > 500) {
                        // 软键盘弹出
                        ObjectAnimator.ofFloat(root_rl_id, "translationY", -FontUtil.dp2px(LoginAndRegisterAty.this, 50)).setDuration(100).start();
                    } else {
                        // 软键盘关闭
                        ObjectAnimator.ofFloat(root_rl_id, "translationY", 0).setDuration(100).start();
                    }
                });
//        登录注册切换r


    }

    @Override
    public void fillData() {
//        设置状态栏为透明

        StatusBarUtil.setStatusColor(this, true, false, Color.parseColor("#00000000"));


    }

    private void createWallet(String mobileMapping) {
        new MyAsyncTask().execute(mnemonicWord, mobileMapping);
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
            String mobileMapping = strings[1];
//            存在多个重载方法， 使用  1  来 进行标识
            BlockChainUtils.importTheWallet(mnemonicWord, trade_pass_id.getText().toString(), mobileMapping, 1);
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            dialogUtils.dismissProgressDialog();
            register_lr_step3_id.setVisibility(View.GONE);

//            myStartActivity(LoginAndRegisterAty.class);
//            finish();
            Utils.Toast("注册成功，请登录");
            registerMobileId.setText("");
            register_pass_text_id.setText("");
            reg_conf_psss_id.setText("");
            trade_pass_id.setText("");
            register_trade_pass_text_id.setText("");
            invite_code.setText("");


        }
    }


    private void requestData(int type) {

        dialogUtils.showProgressDialog();
        TreeMap<String, Object> map;
        if (type == 0) {
            map = new TreeMap<>();
            map.put("mobile", registerMobileId.getText().toString());
            map.put("password", getMD5String(register_pass_text_id.getText().toString()));
            map.put("tradePwd", getMD5String(trade_pass_id.getText().toString()));
            map.put("inviteCode", invite_code.getText().toString());
            map.put("quesTion", quesTion);
            map.put("answer", answer);
            HttpUtils.getHttpUtils().executePost(this, map, "register", type, this);
        }
        if (type == 1) {
            map = new TreeMap<>();
            map.put("mobile", loginMobileNumber.getText().toString());
            map.put("password", getMD5String(loginPassId.getText().toString()));
            HttpUtils.getHttpUtils().executePost(this, map, "loginForMobile", type, this);
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
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject != null) {
                        if (jsonObject.getBoolean("success")) {

//                            Config.setTransactionPassword(getMD5String(register_trade_pass_text_id.getText().toString()));
//                            Config.setPassword(getMD5String(register_pass_text_id.getText().toString()));
//                            UserMobileBean userMobileBean = new UserMobileBean();
//                            userMobileBean.setInviteCode(invite_code.getText().toString());
//                            userMobileBean.setLoginPass(register_pass_text_id.getText().toString());
//                            userMobileBean.setMobileMapping(registerMobileId.getText().toString());
//                            userMobileBean.setMobile(registerMobileId.getText().toString());
//                            DatabaseUserMobileUtils.setLocalUserJson(userMobileBean);
//                    不删除本地账号
//                    DatabaseWalletUtils.deleteLocalCoin();
                            Config.setMobleMapping(registerMobileId.getText().toString());
                            StringBuilder sb = new StringBuilder();
                            byte[] entropy = new byte[Words.TWELVE.byteLength()];
                            new SecureRandom().nextBytes(entropy);
                            new MnemonicGenerator(English.INSTANCE)
                                    .createMnemonic(entropy, sb::append);
                            mnemonicWord = sb.toString();
//                    mnemonicWord = userMobileBean.getMobileMapping();

                            createWallet(registerMobileId.getText().toString());

                            loginImgS.setVisibility(View.VISIBLE);
                            registerImgS.setVisibility(View.INVISIBLE);
                            loginLrId.setVisibility(View.VISIBLE);
                            registerLrId.setVisibility(View.GONE);
                            register_lr_step2_id.setVisibility(View.GONE);
                        } else {
                            Utils.Toast(String.valueOf(jsonObject.getBoolean("info")));

                        }
                    }
                }

                if (type == 1) {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("success")) {
//                        if (userMobileBeans == null || userMobileBeans.size() < 1) {
//
//                        }
                        Config.setMobleMapping(loginMobileNumber.getText().toString());
                        Config.setToken(jsonObject.getJSONObject("data").getString("token"));
                        Config.setCustomerId(jsonObject.getJSONObject("data").getString("customerId"));
                        Config.setInviteCode(jsonObject.getJSONObject("data").getString("myCode"));

                        //下面将对当前用户手机号和本机钱包地址做验证， 如若 本地地址都存在，则会跳入首页， 负责将会跳入到导入私钥的界面

                        walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");

                        boolean existCCM = false;

                        if (walletBeans == null) {
                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("walletBeans", (Serializable) walletBeans);
                            bundle.putString("flag", "0");
                            myStartActivity(ImputWalletPriAty.class, bundle);
                        } else {
                            for (WalletBean walletBean : walletBeans) {
                                if (walletBean.getCoin().equals("CCM")) {
                                    existCCM = true;
                                    continue;
                                }
//                                if (walletBean.getCoin().equals("USDT")) {
//                                    existUSDT = true;
//                                    continue;
//                                }
//                                if (walletBean.getCoin().equals("BTC")) {
//                                    existBTC = true;
//                                    continue;
//                                }
                            }
                        }

                        if (existCCM) {
                            myStartActivity(MainAty.class);
                            finish();
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("walletBeans", (Serializable) walletBeans);
                            myStartActivity(ImputWalletPriAty.class, bundle);
                        }
                        finish();
                    } else {
                        Utils.Toast("手机号或者密码错误！");
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

    public static String getMD5String(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialogUtils.dismissProgressDialog();
    }


}
