package com.qy.ccm.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
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


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qy.ccm.R;
import com.qy.ccm.app.MyApp;
import com.qy.ccm.aty.MainAty;
import com.qy.ccm.aty.my.AboutUsAty;
import com.qy.ccm.aty.wallet.ChangePassAty;
import com.qy.ccm.aty.wallet.CurrVersionAty;
import com.qy.ccm.aty.wallet.NoticeCenterViewPagerAty;
import com.qy.ccm.aty.wallet.UserFallbackAty;
import com.qy.ccm.aty.wallet.UserProtiAty;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.config.Config;
import com.qy.ccm.fragment.base.BaseFrag;
import com.qy.ccm.retrofit.HttpRequestCallback;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.database.DatabaseWalletUtils;
import com.qy.ccm.utils.function.ScreenUtils;
import com.qy.ccm.utils.function.StringUtils;
import com.qy.ccm.utils.function.safety.AES256;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:交易页面
 * Data：2019/5/9-8:01 PM
 */
public class MyFrag extends BaseFrag implements HttpRequestCallback<Object> {

    @BindView(R.id.wallet_name_id)
    TextView wallet_name_id;

    @BindView(R.id.notice_center_id)
    LinearLayout notice_center_id;

    @BindView(R.id.user_fallback_id)
    LinearLayout user_fallback_id;

    @BindView(R.id.change_pass_id)
    LinearLayout change_pass_id;

    @BindView(R.id.user_protel_id)
    LinearLayout user_protel_id;

    @BindView(R.id.about_us_id)
    LinearLayout about_us_id;

    @BindView(R.id.curr_version_id)
    LinearLayout curr_version_id;

    String address,btcAddr,ethAddr = "";
    @BindView(R.id.notice_num_id)
    TextView notice_num_id;
    private static final int TIMER = 999;
    private static boolean flag = true;

    private Dialog dialog;
    private EditText pow;

    private Button pow_button;
    private ImageView powd_diss;
    private Button powd_diss1;

    List<WalletBean> walletBeans;
    WalletBean walletToken;

    @OnClick({R.id.notice_center_id, R.id.user_fallback_id, R.id.change_pass_id, R.id.user_protel_id, R.id.about_us_id, R.id.curr_version_id, R.id.layout_out})
    public void onClick(View v) {
        Bundle bundle;
        switch (v.getId()) {
            case R.id.notice_center_id:

                bundle = new Bundle();
                Utils.startActivity(getContext(), NoticeCenterViewPagerAty.class, bundle);

                break;
            case R.id.layout_out:

//                退出账号
                Config.setInviteCode("");

                //弹框输入交易密码
                dialog = new Dialog(this.getContext(), R.style.DialogThemeHalfAlpa1);
                View view1 = LayoutInflater.from(this.getContext()).inflate(R.layout.pow_dialog, null);
                dialog.setContentView(view1);
                dialog.setCanceledOnTouchOutside(true);
                Window dialogWindow = dialog.getWindow();

                WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//                                    p.height = DisplayUtil.dip2px(activity, 230f) // 高度设置为屏幕的0.6，根据实际情况调整

//                p.width = DisplayUtil.dip2px(this, Float.valueOf(hi)); // 宽度设置为屏幕的0.65，根据实际情况调整
                p.width = ScreenUtils.getScreenWidth(this.getContext());
                ; // 宽度设置为屏幕的0.65，根据实际情况调整
                dialogWindow.setAttributes(p);

                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);

                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

                            if ("".equals(Config.getInviteCode()) || Config.getInviteCode() == null) {
                                MyApp.getSingleInstance().exitAllActivity();
                            }
                            return true;
                        } else {
                            return false;
                        }
                    }
                });


                dialog.show();

                pow = view1.findViewById(R.id.ppe_wallet_affirmpassword);
                pow_button = view1.findViewById(R.id.pow_button);
                powd_diss = view1.findViewById(R.id.powd_diss);
                powd_diss1 = view1.findViewById(R.id.powd_diss1);
                pow_button.setText("进入");
//                    pow.initStyle(R.drawable.empty, 6, 0.5f, R.color.black, R.color.black, 30);
                powd_diss.setVisibility(View.GONE);
                powd_diss1.setVisibility(View.VISIBLE);
                //密码输入完后的回调
                powd_diss1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyApp.getSingleInstance().exitAllActivity();
                    }
                });
                pow_button.setOnClickListener(view -> {

                    String privateKey = walletToken.getPrivateKey();
                    privateKey = AES256.SHA256Decrypt(privateKey, pow.getText().toString());
//                        requestData(0);

                    if (!StringUtils.isEmpty(privateKey)) {
                        dialog.dismiss();
                        //手动收起软键盘
                        InputMethodManager imm = (InputMethodManager)
                                this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(pow.getWindowToken(), 0);
                        Config.setInviteCode("islogin");

                    } else {
                        Utils.Toast("密码错误！请重新输入");
                    }

                });

                break;
            case R.id.user_fallback_id:
                bundle = new Bundle();
                Utils.startActivity(getContext(), UserFallbackAty.class, bundle);

                break;
            case R.id.change_pass_id:
                bundle = new Bundle();
                Utils.startActivity(getContext(), ChangePassAty.class, bundle);

                break;
            case R.id.user_protel_id:
                bundle = new Bundle();
                Utils.startActivity(getContext(), UserProtiAty.class, bundle);

                break;
            case R.id.about_us_id:
                bundle = new Bundle();
                Utils.startActivity(getContext(), AboutUsAty.class, bundle);

                break;
            case R.id.curr_version_id:
                bundle = new Bundle();
                Utils.startActivity(getContext(), CurrVersionAty.class, bundle);
                break;
            default:
                break;

        }

    }

    @Override
    public int initView() {
        return R.layout.frag_my;
    }

    @Override
    public void setListener() {

        List<WalletBean> walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");

        if (walletBeans == null || walletBeans.isEmpty()) {
            wallet_name_id.setText(walletBeans.get(0).getWalletName());
        }
        walletToken = walletBeans.get(0);
        setTimer();

    }

    @Override
    public void fillData() {
        walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");

        StatusBarUtil.setStatusColor(this.getActivity(), true, false, R.color.color_0692c2);
        StatusBarUtil.setStatusBarColor(this.getActivity(), R.color.color_0692c2);
    }

    private void requestData(int type) {
        List<WalletBean> walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");
        List<WalletBean> btcBeans = DatabaseWalletUtils.getLocalCoinList(1);
        List<WalletBean> ethBeans = DatabaseWalletUtils.getLocalCoinList(2);

        if (walletBeans != null && !walletBeans.isEmpty()) {
            address = walletBeans.get(0).getTokenAddress();
        }
        if (btcBeans != null && !btcBeans.isEmpty()) {
            btcAddr = btcBeans.get(0).getTokenAddress();
        }
        if (ethBeans != null && !ethBeans.isEmpty()) {
            ethAddr = ethBeans.get(0).getTokenAddress();
        } else {
            return;
        }
//        dialogUtils.showProgressDialog();
        TreeMap<String, Object> map;
        if (type == 0) {
            map = new TreeMap<String, Object>();
            map.put("pageNo", 1);
            map.put("pageSize", "20");
            map.put("address", address);
            HttpUtils.getHttpUtils().executeGet(this.getActivity(), map, "getTokenNoticeTransRecord", type, this);
        }
        if (type == 1) {
            map = new TreeMap<String, Object>();
            map.put("pageNo", 1);
            map.put("pageSize", "20");
            map.put("address", btcAddr);
            HttpUtils.getHttpUtils().executeGet(this.getActivity(), map, "getOthersTransRecord", type, this);
        }
        if (type == 2) {
            map = new TreeMap<String, Object>();
            map.put("pageNo", 1);
            map.put("pageSize", "20");
            map.put("address", ethAddr);
            HttpUtils.getHttpUtils().executeGet(this.getActivity(), map, "getOthersTransRecord", type, this);
        }
    }

    /**
     * 接口请求成功
     */
    @Override
    public void onRequestSuccess(String result, int type) {
        int a = 0;

        dialogUtils.dismissProgressDialog();
        try {
            if (!TextUtils.isEmpty(result)) {
                if (type == 0) {
//                    rmbAmount = new BigDecimal(0);
                    JSONObject jsonO = null;
                    JSONArray jsonArray = JSONObject.parseObject(result).getJSONObject("info").getJSONArray("obj");

                    Iterator iterator = jsonArray.iterator();
                    while (iterator.hasNext()) {
                        JSONObject o = (JSONObject) iterator.next();
                        if (o.getString("from").toLowerCase().equals(address.toLowerCase())) {
                            if (o.getIntValue("fromReadStatus") == 0) {
                                a = a + 1;
                                iterator.remove();
                            }

                        } else if (!o.getString("from").toLowerCase().equals(address.toLowerCase())) {
                            if (o.getIntValue("toReadStatus") == 0) {
                                a = a + 1;
                                iterator.remove();
                            }
                        }

                    }

                    if(getActivity() instanceof MainAty) {
                        TextView main_notice_num_id = getActivity().findViewById(R.id.notice_num_id);

                        if (a > 0) {
                            MainAty.notice_num_id.setVisibility(View.VISIBLE);
                            MainAty.notice_num_id.setText(String.valueOf(a));

                        }
                        else {
                            MainAty.notice_num_id.setVisibility(View.GONE);
                        }
                    }

                }else if (type == 1){
                    JSONObject jsonO = null;
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    if (jsonObject.getJSONArray("data") != null) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Iterator iterator = jsonArray.iterator();
                        while (iterator.hasNext()) {
                            JSONObject o = (JSONObject) iterator.next();
                            if (o.getString("from").toLowerCase().equals(btcAddr.toLowerCase())) {
                                if (o.getIntValue("fromreadstatus") == 0) {
                                    a = a + 1;
                                    iterator.remove();
                                }

                            } else if (!o.getString("from").toLowerCase().equals(btcAddr.toLowerCase())) {
                                if (o.getIntValue("toreadstatus") == 0) {
                                    a = a + 1;
                                    iterator.remove();
                                }
                            }

                        }

                        if(getActivity() instanceof MainAty) {
                            TextView main_notice_num_id = getActivity().findViewById(R.id.notice_num_id);

                            if (a > 0) {
                                MainAty.notice_num_id.setVisibility(View.VISIBLE);
                                MainAty.notice_num_id.setText(String.valueOf(a));

                            }
                            else {
                                MainAty.notice_num_id.setVisibility(View.GONE);
                            }
                        }

                    }


                }else if (type == 2){
                    JSONObject jsonO = null;
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    if (jsonObject.getJSONArray("data") != null) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Iterator iterator = jsonArray.iterator();
                        while (iterator.hasNext()) {
                            JSONObject o = (JSONObject) iterator.next();
                            if (o.getString("from").toLowerCase().equals(ethAddr.toLowerCase())) {
                                if (o.getIntValue("fromreadstatus") == 0) {
                                    a = a + 1;
                                    iterator.remove();
                                }

                            } else if (!o.getString("from").toLowerCase().equals(ethAddr.toLowerCase())) {
                                if (o.getIntValue("toreadstatus") == 0) {
                                    a = a + 1;
                                    iterator.remove();
                                }
                            }

                        }

                        if(getActivity() instanceof MainAty) {
                            TextView main_notice_num_id = getActivity().findViewById(R.id.notice_num_id);

                            if (a > 0) {
                                MainAty.notice_num_id.setVisibility(View.VISIBLE);
                                MainAty.notice_num_id.setText(String.valueOf(a));

                            } else {
                                MainAty.notice_num_id.setVisibility(View.GONE);
                            }

                        }

                    }

                }
                if (a > 0) {
                    MainAty.notice_num_id.setVisibility(View.VISIBLE);
                    MainAty.notice_num_id.setText(String.valueOf(a));

                } else {
                    MainAty.notice_num_id.setVisibility(View.GONE);
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

    //更新本金的定时
    private void setTimer() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (flag) {
                    try {
                        Thread.sleep(5000); //休眠一秒

                        mHanler.sendEmptyMessage(TIMER);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private Handler mHanler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIMER:

                    //去执行定时操作逻辑
                    requestData(0);
                    requestData(1);
                    requestData(2);

                    break;
                default:
                    break;
            }
        }
    };

}
