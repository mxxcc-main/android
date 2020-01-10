package com.qy.ccm.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.qy.ccm.R;
import com.qy.ccm.adapter.wallet.MainCoinListAdapter;
import com.qy.ccm.adapter.wallet.WebBannerAdapter;
import com.qy.ccm.app.MyApp;
import com.qy.ccm.aty.my.ExportPrikeyAty;
import com.qy.ccm.aty.wallet.WalletCodeAty;
import com.qy.ccm.aty.wallet.WalletMarketAty;
import com.qy.ccm.aty.wallet.WalletMnemonicAty;
import com.qy.ccm.aty.wallet.WalletTransactionAty;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.bean.other.database.WalletCoinBean;
import com.qy.ccm.bean.other.entity.WalletToken;
import com.qy.ccm.bean.other.entity.WalletTokenState;
import com.qy.ccm.bean.other.rxbus.ImportWalletEvent;
import com.qy.ccm.bean.other.rxbus.TokenWalletEvent;
import com.qy.ccm.config.Config;
import com.qy.ccm.config.EthConfig;
import com.qy.ccm.constants.UrlConstants;
import com.qy.ccm.fragment.base.BaseFrag;
import com.qy.ccm.fragment.main.CreateWalletInAty;
import com.qy.ccm.retrofit.HttpRequestCallback;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.blockchain.BlockChainUtils;
import com.qy.ccm.utils.blockchain.btc.BtcUtils;
import com.qy.ccm.utils.database.DatabaseWalletUtils;
import com.qy.ccm.utils.database.WalletCoinUtils;
import com.qy.ccm.utils.function.ScreenUtils;
import com.qy.ccm.utils.function.StringUtils;
import com.qy.ccm.utils.function.safety.AES256;
import com.qy.ccm.utils.rxbus.ErrorBean;
import com.qy.ccm.utils.rxbus.RxBusHelper;
import com.qy.ccm.view.group.NavigationLucencyLayout;
import com.qy.ccm.view.group.banner.BannerLayout;
import com.qy.ccm.websocket.SocketClient;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;

/**
 * Description:钱包页
 * Data：2019/5/8-4:54 PM
 */
public class MainFrag extends BaseFrag implements OnRefreshListener, HttpRequestCallback<Object> {
    @BindView(R.id.nll_main)
    NavigationLucencyLayout nllMain;

    @BindView(R.id.vp_wallet)
    LRecyclerView vpRecyclerView;

    @BindView(R.id.bl_include_main)
    BannerLayout blIncludeMain;

    @BindView(R.id.tv_testbtn)
    TextView tv_testbtn;

    private Dialog dialog;
    private Dialog dialogFenX;

    private EditText pow;
    private Button pow_button;
    private ImageView powd_diss;
    private Button powd_diss1;

    private MainCoinListAdapter mainCoinListAdapter;

    private List<WalletBean> walletBeans;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    List<MultiItemEntity> tokenEntityList;
    //    private BigDecimal rmbAmount;
//    private BigDecimal ptbAmount;
    private String tradePwd;

    private String address;
    double sum = 0;

    private Context mContext;

    @Override
    public int initView() {
        return R.layout.frag_main;
    }

    @Override
    public void setListener() {
//        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        nllMain.setOnBuyListener((NavigationLucencyLayout.OnBuyListener) () ->

                {
//                    drawer.openDrawer(GravityCompat.START)
                    //弹框输入交易密码
                    dialog = new Dialog(this.getActivity(), R.style.DialogThemeHalfAlpa);
                    View view1 = LayoutInflater.from(this.getActivity()).inflate(R.layout.pow_dialog, null);
                    dialog.setContentView(view1);
                    dialog.setCanceledOnTouchOutside(true);
                    Window dialogWindow = dialog.getWindow();

                    WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//                                    p.height = DisplayUtil.dip2px(activity, 230f) // 高度设置为屏幕的0.6，根据实际情况调整
//                    p.width = DisplayUtil.dip2px(this.getActivity(), 300f); // 宽度设置为屏幕的0.65，根据实际情况调整
//                    dialogWindow.setAttributes(p);


                    int hi = ScreenUtils.getScreenWidth(this.getActivity());
//                p.width = DisplayUtil.dip2px(this, Float.valueOf(hi)); // 宽度设置为屏幕的0.65，根据实际情况调整
                    p.width = hi; // 宽度设置为屏幕的0.65，根据实际情况调整
                    dialogWindow.setAttributes(p);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);

                    dialog.show();

                    pow = view1.findViewById(R.id.ppe_wallet_affirmpassword);
                    pow_button = view1.findViewById(R.id.pow_button);
                    powd_diss = view1.findViewById(R.id.powd_diss);

//                    pow.initStyle(R.drawable.edit_num_bg2, 6, 0.33f, R.color.black, R.color.black, 30);
                    powd_diss.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    //密码输入完后的回调
                    pow_button.setOnClickListener(view -> {
                        tradePwd = pow.getText().toString();
//                        requestData(3);
                        walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");
                        String testPass;
                        if (walletBeans.size() > 0) {
                            testPass = AES256.SHA256Decrypt(walletBeans.get(0).getPrivateKey(), pow.getText().toString());
                        } else {
                            Utils.Toast("当前设备不存在地址");
                            return;
                        }
                        if (!StringUtils.isEmpty(testPass)) {

                            //手动收起软键盘
                            InputMethodManager imm = (InputMethodManager)
                                    this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(pow.getWindowToken(), 0);

                            dialog.dismiss();
//                        这里做分叉
                            dialogFenX = new Dialog(this.getActivity(), R.style.DialogThemeHalfAlpa);
                            View view2 = LayoutInflater.from(this.getActivity()).inflate(R.layout.private_key_and_nonecmic_word_dialog, null);
                            dialogFenX.setContentView(view2);
                            dialogFenX.setCanceledOnTouchOutside(true);
                            Window dialogWindow1 = dialogFenX.getWindow();

                            WindowManager.LayoutParams p1 = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//                                    p.height = DisplayUtil.dip2px(activity, 230f) // 高度设置为屏幕的0.6，根据实际情况调整
                            int hi1 = ScreenUtils.getScreenWidth(this.getActivity());
//                p.width = DisplayUtil.dip2px(this, Float.valueOf(hi)); // 宽度设置为屏幕的0.65，根据实际情况调整
                            p1.width = hi1; // 宽度设置为屏幕的0.65，根据实际情况调整
                            dialogWindow1.setAttributes(p1);
                            dialogFenX.setCanceledOnTouchOutside(false);
                            dialogFenX.setCancelable(false);

                            dialogFenX.show();

                            LinearLayout jump_private_key = view2.findViewById(R.id.jump_private_key);
                            LinearLayout nonemic_word_id = view2.findViewById(R.id.nonemic_word_id);
                            ImageView dismiss_id = view2.findViewById(R.id.dismiss_id);
                            TextView ppe_wallet_affirmpassword = view2.findViewById(R.id.ppe_wallet_affirmpassword);

                            ppe_wallet_affirmpassword.setText("请选择导出钱包的方式");

                            jump_private_key.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogFenX.dismiss();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("tradePwd", tradePwd);
                                    walletBeans = DatabaseWalletUtils.getLocalCoinList(10);

                                    bundle.putSerializable("walletBeans", (Serializable) walletBeans);
                                    Utils.startActivity(mContext, ExportPrikeyAty.class, bundle);

                                }
                            });
                            nonemic_word_id.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Config.setCustomerId("");

                                    walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM(tokennamezhcn);

                                    if (walletBeans == null || walletBeans.isEmpty()) {
                                        Utils.Toast("请先创建账户!");
                                        return;
                                    }
                                    if (StringUtils.isEmpty(AES256.SHA256Decrypt(walletBeans.get(0).getTheMnemonicWord(), pow.getText().toString()))) {
                                        Utils.Toast("当前地址不存在可以导出的助记词。请确认导入方式是否为助记词或者是否为新建钱包");
                                        return;
                                    }

                                    dialogFenX.dismiss();

                                    Bundle bundle = new Bundle();
                                    bundle.putString("tradePwd", tradePwd);
                                    walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM(tokennamezhcn);

                                    bundle.putSerializable("walletBeans", (Serializable) walletBeans);
                                    Utils.startActivity(mContext, WalletMnemonicAty.class, bundle);

                                }
                            });
                            dismiss_id.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogFenX.dismiss();
                                }
                            });

                        } else {
                            Utils.Toast("交易密码错误！请重新输入");
                        }
                    });
                }
        );

        nllMain.setOnBuyListener((NavigationLucencyLayout.OnBuyOneListener) () -> {
//            Utils.Toast("点击了行情。。。");

            Bundle bundle = new Bundle();
//                bundle.putSerializable("walletToken", walletToken);
            Utils.startActivity(getContext(), WalletMarketAty.class, bundle);

        });

//        add wallet
        nllMain.setOnBuyListener((NavigationLucencyLayout.OnBuyTwoListener) () -> {
//            Utils.Toast("点击了行情。。。");

            Bundle bundle = new Bundle();
//                bundle.putSerializable("walletToken", walletToken);
            Utils.startActivity(getContext(), CreateWalletInAty.class, bundle);

        });

        //导入钱包更新数据
        RxBusHelper.doOnMainThread(ImportWalletEvent.class, new RxBusHelper.OnEventListener<ImportWalletEvent>() {
            @Override
            public void onEvent(ImportWalletEvent walletEvent) {
                Log.e("==========", "walletEvent:" + "通知创建钱包");
                getTokenBalance();
            }

            @Override
            public void onError(ErrorBean errorBean) {

            }
        });


//    这里更新首次数据不能自动刷新的问题

        new Handler().postDelayed(() -> {

//            判断存在不存在Token
            getTokenBalance();
            walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");
            mainCoinListAdapter.setqOrgs((walletBeans));
            mainCoinListAdapter.notifyDataSetChanged();
            getCoinPrice(SocketClient.result);
        }, 2000);

//        getTokenBalance();

        initBlIncludeMain();


//        requestData(4);
        marketHttp();

    }

    @Override
    public void fillData() {
        nllMain.setIvLift(R.mipmap.icon_export);
        nllMain.setIvTwo(R.mipmap.icon_add);

        testLogin();

        try {
            SocketClient.webMarket();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mContext = this.getActivity();


//        tv_testbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle bundle = new Bundle();
////                bundle.putSerializable("walletToken", walletToken);
//                Utils.startActivity(getContext(), CreBtcAty.class, bundle);
//            }
//        });

        StatusBarUtil.setStatusColor(this.getActivity(), true, true, R.color.colorPrimary);
//        nllMain.setIvOne(R.mipmap.icon_home_market);
        nllMain.setTitle("钱包");

//      这里是对新版本做的数据库初始化
        initDb();

        initWalletData();
        getCoinPrice(SocketClient.result);

    }

    private void testLogin() {
        if ("".equals(Config.getInviteCode()) || Config.getInviteCode() == null) {
//弹框输入交易密码
            dialog = new Dialog(this.getActivity(), R.style.DialogThemeHalfAlpa1);
            View view1 = LayoutInflater.from(this.getActivity()).inflate(R.layout.pow_dialog, null);
            dialog.setContentView(view1);
            dialog.setCanceledOnTouchOutside(true);
            Window dialogWindow = dialog.getWindow();

            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//                                    p.height = DisplayUtil.dip2px(activity, 230f) // 高度设置为屏幕的0.6，根据实际情况调整

//                p.width = DisplayUtil.dip2px(this, Float.valueOf(hi)); // 宽度设置为屏幕的0.65，根据实际情况调整
            p.width = ScreenUtils.getScreenWidth(this.getActivity());
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

                String privateKey = walletBeans.get(0).getPrivateKey();
                privateKey = AES256.SHA256Decrypt(privateKey, pow.getText().toString());
//                        getCoinPrice(SocketClient.result);

                if (!StringUtils.isEmpty(privateKey)) {
                    dialog.dismiss();
                    //手动收起软键盘
                    InputMethodManager imm = (InputMethodManager)
                            this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(pow.getWindowToken(), 0);
                    Config.setInviteCode("islogin");

                } else {
                    Utils.Toast("密码错误！请重新输入");
                }

            });
        }


    }

    private JSONArray jsonArray;
    private WebBannerAdapter webBannerAdapter;
    private JSONObject jsonObjectUser;


    private void initDb() {
        walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");
        for (WalletBean walletBean : walletBeans) {
            switch (walletBean.getTokenName()) {

                case "SAR":
                    walletBean.setContractAddress(EthConfig.SAR_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);

                    break;
                case "SAR-ASI":
                    walletBean.setContractAddress(EthConfig.SAR_ASI_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;
                case "CCM-ISR":
                    walletBean.setContractAddress(EthConfig.CCM_ISR_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;
                case "SAR-SAN":
                    walletBean.setContractAddress(EthConfig.SAR_SAN_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;
                case "HKDD":
                    walletBean.setContractAddress(EthConfig.HKDD_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;
                case "SAR-S":
                    walletBean.setContractAddress(EthConfig.SAR_S_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;
                case "ISR-S":
                    walletBean.setContractAddress(EthConfig.ISR_S_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;
                case "CCM-S":
                    walletBean.setContractAddress(EthConfig.CCM_S_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;
                case "SAR-ES":
                    walletBean.setContractAddress(EthConfig.SAR_ES_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;
                case "HKDD-S":
                    walletBean.setContractAddress(EthConfig.HKDD_S_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;

//                    new add
                case "SAN-0301":
                    walletBean.setContractAddress(EthConfig.SAN_0301_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;
                case "SAN-0306":
                    walletBean.setContractAddress(EthConfig.SAN_0306_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;
                case "SAN-0502":
                    walletBean.setContractAddress(EthConfig.SAN_0502_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;
                case "SAN-3105":
                    walletBean.setContractAddress(EthConfig.SAN_3105_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;
                case "SAN-3301":
                    walletBean.setContractAddress(EthConfig.SAN_3301_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;
                case "SAN-2101":
                    walletBean.setContractAddress(EthConfig.SAN_2101_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;
                case "SAN-2105":
                    walletBean.setContractAddress(EthConfig.SAN_2105_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;
                case "SAN-2406":
                    walletBean.setContractAddress(EthConfig.SAN_2406_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;
                case "SAN-410101":
                    walletBean.setContractAddress(EthConfig.SAN_410101_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;

                case "SAN-2906":
                    walletBean.setContractAddress(EthConfig.SAN_2906_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;

                case "SAN-3201":
                    walletBean.setContractAddress(EthConfig.SAN_3201_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;

                case "SAN-1606":
                    walletBean.setContractAddress(EthConfig.SAN_1606_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;

                case "SAN-2901":
                    walletBean.setContractAddress(EthConfig.SAN_2901_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;

                case "SAN-410101S":
                    walletBean.setContractAddress(EthConfig.SAN_410101S_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;

                case "SAN-1106":
                    walletBean.setContractAddress(EthConfig.SAN_1106_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;

                case "SAN-1104":
                    walletBean.setContractAddress(EthConfig.SAN_1104_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;

                case "SAN-1802":
                    walletBean.setContractAddress(EthConfig.SAN_1802_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;

                case "SAN-410102":
                    walletBean.setContractAddress(EthConfig.SAN_410102_CONTRACT);
                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), walletBean.getMobileMapping(), 1D);
                    break;

                default:
                    break;
            }
        }
    }

    private View recyclerView_main;
    private int currentPosition = 0;

    private void initBlIncludeMain() {

        blIncludeMain.setAutoPlayDuration(3000);
//        jsonArray = new JSONArray();

        jsonArray = new JSONArray();
        jsonObjectUser = new JSONObject();
        jsonObjectUser.put("tokenAddress", "...");
        jsonObjectUser.put("tokenAddressAll", "--");
        jsonObjectUser.put("walletName", "--");
        jsonObjectUser.put("price", "--");
        jsonArray.add(jsonObjectUser);

        jsonObjectUser = new JSONObject();
        jsonObjectUser.put("tokenAddress", "...");
        jsonObjectUser.put("tokenAddressAll", "--");
        jsonObjectUser.put("walletName", "--");
        jsonObjectUser.put("price", "--");
        jsonArray.add(jsonObjectUser);

        jsonObjectUser = new JSONObject();
        jsonObjectUser.put("tokenAddress", "...");
        jsonObjectUser.put("tokenAddressAll", "--");
        jsonObjectUser.put("walletName", "--");
        jsonObjectUser.put("price", "--");
        jsonArray.add(jsonObjectUser);

        walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");
        if (walletBeans == null || walletBeans.isEmpty()) {
            return;
        }

        for (WalletBean walletBean : walletBeans) {
            if (walletBean.getCoin().equals("CCM")) {

                blIncludeMain.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                        Log.e(Tag,"onScrolled-----------");
//                        int first = blIncludeMain.getmLayoutManager().getCurrentPosition();
                        ccm_coin_name_item = blIncludeMain.getRecyclerView().getChildAt(1).findViewById(R.id.ccm_coin_name_item);
                        zongzichan_cny_title_item = blIncludeMain.getRecyclerView().getChildAt(1).findViewById(R.id.zongzichan_cny_title_item);
                        ccm_balance_id_item = blIncludeMain.getRecyclerView().getChildAt(1).findViewById(R.id.ccm_balance_id_item);
                        ccm_coin_name_address_qcore_item = blIncludeMain.getRecyclerView().getChildAt(1).findViewById(R.id.ccm_coin_name_address_qcore_item);

                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        int first = blIncludeMain.getmLayoutManager().getCurrentPosition();
                        if (first != currentPosition) {
//                            Utils.Toast("%%%%%%%%%%" + first);

                            if (first == 0) {  //CCM
                                walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");
                                tokennamezhcn = "CCM";

                            } else {
                                if (first == 1) {
                                    tokennamezhcn = "BTC";

                                } else {
                                    tokennamezhcn = "ETH";

                                }


                                walletBeans = DatabaseWalletUtils.getLocalCoinList(first);


                            }


                            mainCoinListAdapter.setqOrgs((walletBeans));
                            mainCoinListAdapter.notifyDataSetChanged();
//                            requestData(4);
                            marketHttp();

                            vpRecyclerView.refreshComplete(20);
                            currentPosition = first;
                            if (walletBeans == null) {
                                return;
                            }

                            for (WalletBean walletBean : walletBeans) {
                                if (tokennamezhcn.equals(walletBean.getCoin())) {
                                    ccm_coin_name_item.setText(walletBean.getWalletName());
                                    zongzichan_cny_title_item.setText(walletBean.getTokenAddress().substring(0, 10) + "..." + walletBean.getTokenAddress().substring(walletBean.getTokenAddress().length() - 10));
                                    ccm_balance_id_item.setText(walletBean.getPrice());
                                    ccm_coin_name_address_qcore_item.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("tokenAddress", walletBean.getTokenAddress());
                                            Utils.startActivity(view.getContext(), WalletCodeAty.class, bundle);

                                        }
                                    });
                                }
                                break;
                            }


                        }
                        if (walletBeans != null && walletBeans.size() > 0) {
                            sum = 0;
                            for (int i = 0; i < walletBeans.size(); i++) {
                                sum = sum + new BigDecimal(walletBeans.get(i).getPrice()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                            }
                            ccm_balance_id_item.setText(String.valueOf(sum));


                        }

                    }
                });
                if (walletBeans != null && walletBeans.size() > 0) {
                    sum = 0;
                    for (int i = 0; i < walletBeans.size(); i++) {
                        sum = sum + new BigDecimal(walletBeans.get(i).getPrice()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                    }
                    if (currentPosition == 0) {
                        address = walletBeans.get(0).getTokenAddress();
                        jsonObjectUser = (JSONObject) jsonArray.get(0);
                        jsonObjectUser.put("tokenAddress", walletBeans.get(0).getTokenAddress().substring(0, 10) + "..." + walletBeans.get(0).getTokenAddress().substring(walletBeans.get(0).getTokenAddress().length() - 10, walletBeans.get(0).getTokenAddress().length()));
                        jsonObjectUser.put("tokenAddressAll", walletBeans.get(0).getTokenAddress());
                        jsonObjectUser.put("walletName", walletBeans.get(0).getWalletName());
                        jsonObjectUser.put("price", String.valueOf(sum));

                    }


                }
                webBannerAdapter = new WebBannerAdapter(getActivity(), jsonArray);
                blIncludeMain.setAdapter(webBannerAdapter);
                webBannerAdapter.notifyDataSetChanged();
                if (currentPosition != 0) {
                    blIncludeMain.getRecyclerView().smoothScrollToPosition(currentPosition);
                }
                break;
            }


        }

    }

    private void getBannerInfo() {
        walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");

        if (walletBeans != null && walletBeans.size() > 0) {
            sum = 0;
            for (int i = 0; i < walletBeans.size(); i++) {
                sum = sum + new BigDecimal(walletBeans.get(i).getPrice()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
            }
            jsonArray = new JSONArray();
            jsonObjectUser = new JSONObject();
            jsonObjectUser.put("tokenAddress", walletBeans.get(0).getTokenAddress().substring(0, 10) + "..." + walletBeans.get(0).getTokenAddress().substring(walletBeans.get(0).getTokenAddress().length() - 10, walletBeans.get(0).getTokenAddress().length()));
            jsonObjectUser.put("tokenAddressAll", walletBeans.get(0).getTokenAddress());
            jsonObjectUser.put("walletName", walletBeans.get(0).getWalletName());
            jsonObjectUser.put("price", String.valueOf(sum));
            jsonArray.add(jsonObjectUser);

            jsonObjectUser = new JSONObject();
            jsonObjectUser.put("tokenAddress", "...");
            jsonObjectUser.put("tokenAddressAll", "--");
            jsonObjectUser.put("walletName", "--");
            jsonObjectUser.put("price", "--");
            jsonArray.add(jsonObjectUser);

            jsonObjectUser = new JSONObject();
            jsonObjectUser.put("tokenAddress", "...");
            jsonObjectUser.put("tokenAddressAll", "--");
            jsonObjectUser.put("walletName", "--");
            jsonObjectUser.put("price", "--");
            jsonArray.add(jsonObjectUser);


        }
        webBannerAdapter = new WebBannerAdapter(getActivity(), jsonArray);
        blIncludeMain.setAdapter(webBannerAdapter);
        webBannerAdapter.notifyDataSetChanged();
    }

    private void getTokenBalance() {
        geCCMBalance();
        geETHBalance();
        getBTCBalance();
    }

    /**
     * 获取btc、usdt的余额
     */
    private void getBTCBalance() {
        if (walletBeans == null) {
            return;
        }
        for (WalletBean coinBean : walletBeans) {
            if ("BTC".equals(coinBean.getTokenName())) {
                String btcAddress = coinBean.getTokenAddress();
                //usdt余额
                Map<String, String> map = new HashMap<>();
                map.put("active", btcAddress);
                String btcUrl = UrlConstants.BTC_BALANCE_API;
                postHttp(btcUrl, map);
                break;
            }
        }


        //usdt余额
//        自己得节点
//        if (coinUsdtUtil == null) {
//            coinUsdtUtil = new CoinUsdtUtil();
//        }
//
//        coinUsdtUtil.getUsdtBalance();

//       第三方暂时注释掉
//        Map<String, String> map = new HashMap<>();
//        for (WalletBean coinBean : usdtWalletList) {
//            map.put("addr", coinBean.getTokenAddress());
//        }
//        String usdtUrl = UrlConstants.USDT_BALANCE;
//        postHttp(usdtUrl, map);


    }

    private String Tag = "***********";

    @Override
    public void onHttpFault(String errorMsg, String url) {
        super.onHttpFault(errorMsg, url);
    }

    private void geETHBalance() {
        BlockChainUtils.getETHBlockchain();
        /**
         * 通知创建钱包
         */
        RxBusHelper.doOnMainThread(TokenWalletEvent.class, new RxBusHelper.OnEventListener<TokenWalletEvent>() {
            @Override
            public void onEvent(TokenWalletEvent walletEvent) {
                Log.e("==========", "walletEvent:");
//                updateData("ETH");
                updateView();
            }

            @Override
            public void onError(ErrorBean errorBean) {
                Log.e("===========1", "isLocalBalance2:" + errorBean.toString());
            }
        });
    }

    private void geCCMBalance() {
        BlockChainUtils.getBlockchain();
        /**
         * 通知创建钱包
         */
        RxBusHelper.doOnMainThread(TokenWalletEvent.class, new RxBusHelper.OnEventListener<TokenWalletEvent>() {
            @Override
            public void onEvent(TokenWalletEvent walletEvent) {
                Log.e("==========", "walletEvent:");
                updateView();
            }

            @Override
            public void onError(ErrorBean errorBean) {
                Log.e("===========1", "isLocalBalance2:" + errorBean.toString());
            }
        });
    }

    @Override
    public void onHttpSuccess(String result, String url) {
        super.onHttpSuccess(result, url);
//        if (url.equals(UrlConstants.USDT_BALANCE)) {
//            localSuccessUsdt(result);
//        } else
        if (url.equals(UrlConstants.BTC_BALANCE_API)) {
            localSuccessBtc(result);
        } else if (url.contains("getOtcTransactionList")) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            sarPrice = jsonObject.getJSONObject("data").getString("price");
            getCoinPrice(SocketClient.result);

        }
    }

    String tokennamezhcn = "CCM";

    private void localSuccessBtc(String result) {
        List<WalletCoinBean> walletList = WalletCoinUtils.getCoinList("BTC", Config.getMobleMapping(), 2);
        for (WalletCoinBean walletCoinBean : walletList) {
            try {
                String address = walletCoinBean.getCoinAddress();
                JSONObject jsonObject = JSONObject.parseObject(result);
                JSONObject btcObject = jsonObject.getJSONObject(walletCoinBean.getCoinAddress());
                String balanceStr = btcObject.getString("final_balance");
                String balance = StringUtils.bigDecimal8(BtcUtils.btcFormat(balanceStr));
                WalletBean tokenBean = new WalletBean();
                tokenBean.setAmount(balance);
                DatabaseWalletUtils.updateLocalData(tokenBean, address, "BTC", Config.getMobleMapping(), 3D);

                updateView();
//                updateData("BTC");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    public void initWalletData() {

        walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");
        mainCoinListAdapter = new MainCoinListAdapter(getActivity(), (walletBeans), (view, pojo, position) -> {
//                Utils.Toast("点击了条目：" + position);

            tokenEntityList = new ArrayList<>();
            List<WalletCoinBean> walletList = WalletCoinUtils.getCoinList(pojo.getCoin(), Config.getMobleMapping(), 2);
            if (walletList == null || walletList.isEmpty()) {
                return;
            }
//            String coinAddress = walletList.get(0).getCoinAddress();
            String coinAddress = pojo.getContractAddress();

            WalletTokenState walletTokenEntity = new WalletTokenState("钱包1", coinAddress);
            WalletBean walletBean = pojo;
            WalletToken walletToken = new WalletToken();
            walletToken.setTokenName(walletBean.getTokenName());
            walletToken.setAmount(walletBean.getAmount());
            walletToken.setPrice(walletBean.getPrice());
            walletToken.setTokenAddress(walletBean.getTokenAddress());
            walletToken.setPrivateKey(walletBean.getPrivateKey());
            walletToken.setContractAddress(walletBean.getContractAddress());
            walletToken.setTheMnemonicWord(walletBean.getTheMnemonicWord());
            walletToken.setAmount(walletBean.getAmount());
            walletToken.setTheMnemonicWord(walletBean.getTheMnemonicWord());
            walletTokenEntity.addSubItem(walletToken);

            Bundle bundle = new Bundle();
            bundle.putSerializable("walletToken", walletToken);
            Utils.startActivity(getContext(), WalletTransactionAty.class, bundle);
        });
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        vpRecyclerView.setLayoutManager(manager);
        vpRecyclerView.setOnRefreshListener(this);

        lRecyclerViewAdapter = new LRecyclerViewAdapter(mainCoinListAdapter);
        vpRecyclerView.setAdapter(lRecyclerViewAdapter);

    }

    TextView ccm_coin_name_item;
    TextView zongzichan_cny_title_item;
    TextView ccm_balance_id_item;
    ImageView ccm_coin_name_address_qcore_item;

    @Override
    public void onRefresh() {
//        if(currentPosition!=0){
//
//        }
//        Log.e(Tag,"----------------currentPosition："+currentPosition);
//        getTokenBalance();
        //        test_aaaa.setText("10100000");

        if (currentPosition == 0) {
            geCCMBalance();

        } else if (currentPosition == 1) {
            getBTCBalance();
        } else if (currentPosition == 2) {
            geETHBalance();
        }

//        recyclerView_main = blIncludeMain.getmLayoutManager().getFocusedChild();

//        Log.e(Tag,"----------------"+blIncludeMain.getmLayoutManager().getChildCount());

    }

    public void updateView() {
        if (currentPosition == 0) {
            tokennamezhcn = "CCM";
        } else if (currentPosition == 1) {
            tokennamezhcn = "BTC";
        } else if (currentPosition == 2) {
            tokennamezhcn = "ETH";
        } else {
            return;
        }
        marketHttp();
        walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM(tokennamezhcn);
        if (walletBeans != null) {
            for (WalletBean walletBean : walletBeans) {
                if (tokennamezhcn.equals(walletBean.getCoin())) {
                    ccm_coin_name_item.setText(walletBean.getWalletName());
                    zongzichan_cny_title_item.setText(walletBean.getTokenAddress().substring(0, 10) + "..." + walletBean.getTokenAddress().substring(walletBean.getTokenAddress().length() - 10));
                    ccm_balance_id_item.setText(String.valueOf(sum));
                }
                break;
            }
        }
        mainCoinListAdapter.setqOrgs((walletBeans));
        mainCoinListAdapter.notifyDataSetChanged();
        getCoinPrice(SocketClient.result);
//        requestData(4);


        vpRecyclerView.refreshComplete(20);
    }

    private void requestData(int type) {

//        dialogUtils.showProgressDialog();
        TreeMap<String, Object> map;
//        获取各种币呃汇率
        if (type == 0) {
            HttpUtils.getHttpUtils().executeGet(this.getActivity(), "getCoinSystem", type, this);
        }
//        公告
        if (type == 1) {
            map = new TreeMap<String, Object>();
            map.put("pageNo", "1");
            map.put("pageSize", "20");
            HttpUtils.getHttpUtils().executeGet(this.getActivity(), map, "getNoticeListByPage", type, this);
        }
        //4.	获取总资产和平台币：
        if (type == 2) {
            HttpUtils.getHttpUtils().executeGet(this.getContext(), "getProperty", type, this);
        }

        if (type == 3) {
            dialogUtils.showProgressDialog();
            //手动收起软键盘
            InputMethodManager imm = (InputMethodManager)
                    this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(pow.getWindowToken(), 0);

            map = new TreeMap<>();
            map.put("tradePwd", Utils.getMD5String(tradePwd));
            HttpUtils.getHttpUtils().executeGet(this.getActivity(), map, "checkTradePwd", type, this);

        }
        if (type == 4) {
            map = new TreeMap<>();
            HttpUtils.getHttpUtils().executeCommonGet(this.getActivity(), "https://www.ccm.one/api/Otc/getOtcTransactionList?currency_id=13&legal_currency=1&trade_type=buy&member_id=&page=1&row=10", map, type, this);

        }
    }

    String sarPrice = "334.21";

    /**
     * 接口请求成功
     */
    @Override
    public void onRequestSuccess(String result, int type) {

        dialogUtils.dismissProgressDialog();
        try {
            if (!TextUtils.isEmpty(result)) {
                if (type == 0) {
//                    rmbAmount = new BigDecimal(0);
                    JSONObject jsonO = null;
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
//                    TODO 首页的余额显示和各个币种汇率
                    if (walletBeans != null) {
                        for (WalletBean walletBean : walletBeans) {
                            for (Object obj : jsonArray) {
                                jsonO = (JSONObject) obj;
                                if (jsonO.getString("coinName").equals(walletBean.getCoin())) {
                                    Log.e("======", walletBean.getAmount());

                                    Log.e("======", jsonO.getString("rmgExchageRate"));
                                    walletBean.setPrice(
                                            String.valueOf(
                                                    new BigDecimal(walletBean.getAmount()).multiply(new BigDecimal(jsonO.getString("rmgExchageRate"))).setScale(2, RoundingMode.HALF_DOWN)
                                            )
                                    );

                                    if ("CCM".equals(walletBean.getCoin())) {
//                                        ccm_balance_id.setText(walletBean.getPrice());
                                    }

                                    DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), Config.getMobleMapping(), 1D);

                                    break;
                                }
                            }
                        }
                    }
                }
                if (type == 1) {
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
//                  TODO 公告信息的处理
//                    for (Object object : jsonArray) {
//                        JSONObject jsonObject1 = (JSONObject) object;
//                        View item1 = View.inflate(getActivity(), R.layout.item_vf, null);
//                        TextView textView = item1.findViewById(R.id.tv_item_vf);
//                        textView.setText("公告：" + jsonObject1.getString("noticeTitle"));
//                        vfMainRun.addView(item1);
//                    }
                }
                if (type == 2) {
//                    jsonObjectUser = JSONObject.parseObject(result).getJSONObject("data");

                }
                if (type == 3) {
                    dialogUtils.dismissProgressDialog();
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    Boolean isSuccess = jsonObject.getBoolean("success");
                    if (isSuccess) {

                        dialog.dismiss();
                        //手动收起软键盘
                        InputMethodManager imm = (InputMethodManager)
                                this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(pow.getWindowToken(), 0);

//                        这里做分叉
                        dialogFenX = new Dialog(this.getActivity(), R.style.DialogThemeHalfAlpa);
                        View view1 = LayoutInflater.from(this.getActivity()).inflate(R.layout.private_key_and_nonecmic_word_dialog, null);
                        dialogFenX.setContentView(view1);
                        dialogFenX.setCanceledOnTouchOutside(true);
                        Window dialogWindow = dialogFenX.getWindow();

                        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//                                    p.height = DisplayUtil.dip2px(activity, 230f) // 高度设置为屏幕的0.6，根据实际情况调整
                        int hi = ScreenUtils.getScreenWidth(this.getActivity());
//                p.width = DisplayUtil.dip2px(this, Float.valueOf(hi)); // 宽度设置为屏幕的0.65，根据实际情况调整
                        p.width = hi; // 宽度设置为屏幕的0.65，根据实际情况调整
                        dialogWindow.setAttributes(p);
                        dialogFenX.setCanceledOnTouchOutside(false);
                        dialogFenX.setCancelable(false);

                        dialogFenX.show();

                        LinearLayout jump_private_key = view1.findViewById(R.id.jump_private_key);
                        LinearLayout nonemic_word_id = view1.findViewById(R.id.nonemic_word_id);
                        ImageView dismiss_id = view1.findViewById(R.id.dismiss_id);

                        jump_private_key.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Bundle bundle = new Bundle();
                                bundle.putString("tradePwd", tradePwd);
                                walletBeans = DatabaseWalletUtils.getLocalCoinList(10);
                                bundle.putSerializable("walletBeans", (Serializable) walletBeans);
                                Utils.startActivity(mContext, ExportPrikeyAty.class, bundle);

                            }
                        });
                        nonemic_word_id.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle bundle = new Bundle();
                                bundle.putString("tradePwd", tradePwd);
                                walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM("");

                                bundle.putSerializable("walletBeans", (Serializable) walletBeans);
                                Utils.startActivity(mContext, WalletMnemonicAty.class, bundle);

                            }
                        });
                        dismiss_id.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogFenX.dismiss();
                            }
                        });

                    } else {
                        Utils.Toast("交易密码错误！请重新输入");
                    }
                }
                if (type == 4) {
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    sarPrice = jsonObject.getJSONObject("data").getString("price");
                    getCoinPrice(SocketClient.result);

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

    public void getCoinPrice(String result) {
        while ("".equals(SocketClient.result)) {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        JSONObject resultJsonObject = JSONObject.parseObject(SocketClient.result);

        JSONArray jsonArray1 = new JSONArray();
        for (Object object : resultJsonObject.getJSONArray("type_list")) {
            JSONObject jsonObject = (JSONObject) object;
            for (Object object1 : jsonObject.getJSONArray("data")) {
                JSONObject jsonObject1 = (JSONObject) object1;
                String change_percent = jsonObject1.getString("up_down");
                String name = jsonObject1.getString("lname");
                String current_price_usd = jsonObject1.getString("latest_price");
                String logo = "https://raw.githubusercontent.com/iozhaq/image/master/" + jsonObject1.getString("lname") + ".png";
                String current_price_cny = jsonObject1.getString("latest_price_xs");
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("change_percent", change_percent);
                jsonObject2.put("name", name);
                jsonObject2.put("current_price_usd", current_price_usd);
                jsonObject2.put("logo", logo);
                jsonObject2.put("current_price_cny", current_price_cny);

                jsonArray1.add(jsonObject2);

            }

            if (walletBeans != null) {
                for (WalletBean walletBean : walletBeans) {

                    for (Object obj : jsonArray1) {
                        JSONObject jsonO = (JSONObject) obj;

                        if ("SAR".equals(walletBean.getCoin().toUpperCase())) {
                            walletBean.setPrice(
                                    String.valueOf(
                                            new BigDecimal(walletBean.getAmount()).multiply(new BigDecimal(sarPrice)).setScale(2, RoundingMode.HALF_DOWN)
                                    )
                            );
                            DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), Config.getMobleMapping(), 1D);

                            continue;
                        }
                        if (jsonO.getString("name").toUpperCase().equals(walletBean.getCoin().toUpperCase())) {
                            Log.e("======", walletBean.getAmount());

                            Log.e("======", jsonO.getString("current_price_cny"));
                            walletBean.setPrice(
                                    String.valueOf(
                                            new BigDecimal(walletBean.getAmount()).multiply(new BigDecimal(jsonO.getString("current_price_cny"))).setScale(2, RoundingMode.HALF_DOWN)
                                    )
                            );
                            DatabaseWalletUtils.updateLocalData(walletBean, walletBean.getTokenAddress(), walletBean.getTokenName(), Config.getMobleMapping(), 1D);

                            break;
                        }
                    }


                }
                if (ccm_balance_id_item == null) {
                    getBannerInfo();

                }


            }


        }
    }

    ;


    private void marketHttp() {
//        eth_usd
        String url = "https://www.ccm.one/api/Otc/getOtcTransactionList?currency_id=13&legal_currency=1&trade_type=buy&member_id=&page=1&row=10";
        getHttp(url, null);
    }

}



