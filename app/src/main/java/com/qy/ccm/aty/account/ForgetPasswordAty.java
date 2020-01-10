//package com.qy.ccm.aty.account;
//
//
//import android.animation.ObjectAnimator;
//import android.content.Context;
//import android.graphics.Rect;
//import android.support.v7.widget.AppCompatSpinner;
//import android.text.TextUtils;
//import android.text.method.HideReturnsTransformationMethod;
//import android.text.method.PasswordTransformationMethod;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.qy.ccm.R;
//import com.qy.ccm.aty.base.BaseAty;
//import com.qy.ccm.retrofit.HttpRequestCallback;
//import com.qy.ccm.retrofit.HttpUtils;
//import com.qy.ccm.utils.FontUtil;
//import com.qy.ccm.utils.Utils;
//import com.qy.ccm.utils.database.DatabaseWalletUtils;
//import com.qy.ccm.view.group.NavigationLucencyLayout;
//
//import org.json.JSONObject;
//
//import java.math.BigInteger;
//import java.security.MessageDigest;
//import java.util.TreeMap;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
///**
// * 登录页
// */
//public class ForgetPasswordAty extends BaseAty implements HttpRequestCallback<Object> {
//
//    @BindView(R.id.login_mobile_number)
//    EditText login_mobile_number;
//    @BindView(R.id.login_pass_id)
//    EditText login_pass_id;
//    @BindView(R.id.login_trade_pass_id)
//    EditText login_trade_pass_id;
//    @BindView(R.id.answer_text)
//    EditText answer_text;
//    @BindView((R.id.is_show_trade_password))
//    ImageView is_show_trade_password;
//    @BindView((R.id.is_show_login_password))
//    ImageView is_show_login_password;
//    @BindView(R.id.login_sub_btn)
//    Button loginSubBtn;
//
//    @BindView(R.id.question_text)
//    AppCompatSpinner question_text;
//
//    @BindView(R.id.root_rl_id)
//    LinearLayout root_rl_id;
//
//    Boolean isShowLoginPass = false;
//    Boolean isShowTradePass = false;
//
//    InputMethodManager imm;
//    //    Step2
//    @BindView(R.id.nll_main)
//    NavigationLucencyLayout nllMain;
//
//    @OnClick({
//            R.id.is_show_trade_password,
//            R.id.login_sub_btn,
//            R.id.is_show_login_password})
//    public void onClick(View v) {
//        switch (v.getId()) {
//
//            case R.id.is_show_trade_password:
//
////                登录密码是否显示
//                if (!isShowLoginPass) {
//                    login_pass_id.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    is_show_login_password.setImageResource(R.mipmap.icon_found_eyes);
//                    isShowLoginPass = true;
//                } else {
//                    login_pass_id.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    is_show_login_password.setImageResource(R.mipmap.icon_close);
//                    isShowLoginPass = false;
//                }
//                break;
//            case R.id.is_show_login_password:
//
//                if (!isShowTradePass) {
//                    login_trade_pass_id.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    is_show_trade_password.setImageResource(R.mipmap.icon_found_eyes);
//                    isShowTradePass = true;
//                } else {
//                    login_trade_pass_id.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    is_show_trade_password.setImageResource(R.mipmap.icon_close);
//                    isShowTradePass = false;
//                }
//                break;
//
//            case R.id.login_sub_btn:
//                //手动收起软键盘
//                imm = (InputMethodManager)
//                        getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(loginSubBtn.getWindowToken(), 0);
////                登录时的相关操作
////                Utils.Toast("点击了登录按钮");
//                if (login_mobile_number.getText().toString() != null && !login_mobile_number.getText().toString().equals("") && login_mobile_number.getText().toString().length() == 11) {
//
//                    if (login_pass_id.getText() == null || "".equals(login_pass_id.getText().toString().trim())) {
//                        Utils.Toast("请输入正确的登录密码");
//                        return;
//                    }
//
//                    if (login_trade_pass_id.getText() == null || "".equals(login_trade_pass_id.getText().toString().trim()) || login_trade_pass_id.getText().toString().trim().length() != 6) {
//                        Utils.Toast("请输入正确的交易密码");
//                        return;
//                    }
//                    requestData(0);
//                    break;
//
//                } else {
//                    Utils.Toast("请输入正确的手机号");
//                    break;
//
//                }
//
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public int initView() {
//        return R.layout.forget_pass;
//    }
//
//    @Override
//    public void setListener() {
//        //因为系统没有直接监听软键盘API，所以就用以下方法
//        login_trade_pass_id.getViewTreeObserver().addOnGlobalLayoutListener(
//                () -> { //当界面大小变化时，系统就会调用该方法
//                    Rect r = new Rect();
//                    //获取当前界面可视部分
//                    getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
//                    //获取屏幕的高度
//                    int screenHeight = getWindow().getDecorView().getRootView().getHeight();
//                    //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
//                    int heightDifference = screenHeight - r.bottom;
//                    if (heightDifference > 500) {
//                        // 软键盘弹出
//                        ObjectAnimator.ofFloat(root_rl_id, "translationY", -FontUtil.dp2px(ForgetPasswordAty.this, 50)).setDuration(100).start();
//                    } else {
//                        // 软键盘关闭
//                        ObjectAnimator.ofFloat(root_rl_id, "translationY", 0).setDuration(100).start();
//                    }
//                });
////        登录注册切换r
//
//    }
//
//    @Override
//    public void fillData() {
//        //        设置状态栏为透明
////        StatusBarUtil.setStatusColor(this, true, false, Color.parseColor("#00000000"));
//        nllMain.setTitle("忘记密码");
//    }
//
//    private void requestData(int type) {
//
//        dialogUtils.showProgressDialog();
//        TreeMap<String, Object> map;
//        if (type == 0) {
//            map = new TreeMap<>();
////            localhost:8080/Web/changePwdByQues
////            mobile 手机号
////            newPwd   登录密码
////            newTradePwd  交易密码
////            quesTion  问题
////            answer  答案
//
//            map.put("mobile", login_mobile_number.getText().toString());
//            map.put("newPwd", getMD5String(login_pass_id.getText().toString()));
//            map.put("newTradePwd", getMD5String(login_trade_pass_id.getText().toString()));
//            map.put("quesTion", question_text.getSelectedItem().toString());
//            map.put("answer", answer_text.getText().toString());
//            HttpUtils.getHttpUtils().executeGet(this, map, "changePwdByQues", type, this);
//        }
//    }
//
//    /**
//     * 接口请求成功
//     */
//    @Override
//    public void onRequestSuccess(String result, int type) {
//
//        dialogUtils.dismissProgressDialog();
//        try {
//            if (!TextUtils.isEmpty(result)) {
//                if (type == 0) {
//                    JSONObject jsonObject = new JSONObject(result);
//                    if (jsonObject != null) {
//                        if (jsonObject.getBoolean("success")) {
//
////                    不删除本地账号
//                            DatabaseWalletUtils.deleteLocalCoin(login_mobile_number.getText().toString());
//                            Utils.Toast("密码重置成功，请登录");
//                            finish();
//
//                        } else {
//                            Utils.Toast(String.valueOf(jsonObject.getBoolean("info")));
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 接口请求失败
//     */
//    @Override
//    public void onRequestFail(String value, String failCode, int type) {
//
//        dialogUtils.dismissProgressDialog();
//        if (!"null".equals(value)) {
//            Utils.Toast(value);
//        } else {
//            Utils.Toast(failCode);
//        }
//    }
//
//    public static String getMD5String(String str) {
//        try {
//            // 生成一个MD5加密计算摘要
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            // 计算md5函数
//            md.update(str.getBytes());
//            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
//            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
//            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
//            return new BigInteger(1, md.digest()).toString(16);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        dialogUtils.dismissProgressDialog();
//    }
//
//
//}
