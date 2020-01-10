package com.qy.ccm.aty;

import android.Manifest;
import android.app.Dialog;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.qy.ccm.R;
import com.qy.ccm.app.MyApp;
import com.qy.ccm.aty.account.LoginAndRegisterAty;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.aty.my.MyAboutAty;
import com.qy.ccm.aty.my.MyOpinionAty;
import com.qy.ccm.aty.wallet.WalletAlterPasswordAty;
import com.qy.ccm.config.Config;
import com.qy.ccm.config.EthConfig;
import com.qy.ccm.fragment.FindFrag;
import com.qy.ccm.fragment.MainFrag;
import com.qy.ccm.fragment.MarketFrag;
import com.qy.ccm.fragment.MyFrag;
import com.qy.ccm.retrofit.Constants;
import com.qy.ccm.retrofit.HttpRequestCallback;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.utils.UpdateVersionController;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.database.DatabaseWalletUtils;
import com.qy.ccm.utils.database.WalletCoinUtils;
import com.qy.ccm.utils.system.PermissionUtils;
import com.uuzuche.lib_zxing.DisplayUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.TreeMap;

import butterknife.BindView;

/**
 * Description:
 * Data：2019/5/7-2:23 PM
 *
 * @author
 */
public class MainAty extends BaseAty implements NavigationView.OnNavigationItemSelectedListener, HttpRequestCallback<Object> {

    @BindView(R.id.bnv_main)
    BottomNavigationView bnvMain;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    //    @BindView(R.id.nav_view)
//    NavigationView navView;
    @BindView(R.id.main_frame)
    FrameLayout mainFrame;
    public static TextView notice_num_id;
    private ArrayList<MenuItem> menuItems;
    private ArrayList<Integer> menuIcon;

    private MainFrag mainFrag;
    private FindFrag findFrag;
    //    private ApplicationFrag applicationFrag;
    private MarketFrag marketFrag;
    private MyFrag myFrag;

    /**
     * 事务
     */
    private FragmentTransaction ftr;

    @Override
    public int initView() {
        return R.layout.aty_main;
    }

    @Override
    public void setListener() {

//        CrashReport.testJavaCrash();

//        navView.setNavigationItemSelectedListener(this);
        setSelected(0);
        bnvMain.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_main:
                    menuItem.setIcon(R.mipmap.icon_wallet_blue);
                    setSelected(0);

//                    检测更新
                    new Handler().postDelayed(() -> {
                        requestData(0);
                    }, 2000);


                    resetToDefaultIcon(0);
                    return true;
                case R.id.menu_management:
                    menuItem.setIcon(R.mipmap.icon_market_blue);
                    setSelected(1);
                    resetToDefaultIcon(1);
                    return true;
                case R.id.menu_find:
                    menuItem.setIcon(R.mipmap.icon_found_blue);
                    setSelected(2);
                    resetToDefaultIcon(2);
                    return true;
                case R.id.menu_my:
                    menuItem.setIcon(R.mipmap.icon_user_blue);
                    setSelected(3);
                    resetToDefaultIcon(3);
                    return true;
                default:
                    return false;
            }
        });


//        检测更新


    }

    private void requestData(int type) {
        TreeMap<String, Object> map;
        if (type == 0) {
            map = new TreeMap<>();
            map.put("versionName", Utils.getVersionName(this));
            map.put("type", 1);
            HttpUtils.getHttpUtils().executeGet(this, map, "getVersionInfo", type, this);
        }
    }

    private Dialog dialogLicai;

    /**
     * 接口请求成功
     */
    @Override
    public void onRequestSuccess(String result, int type) {

        try {
            if (!TextUtils.isEmpty(result)) {
                if (type == 0) {
                    JSONObject jsonObject = JSONObject.parseObject(result);
//                    jsonObject = jsonObject.getJSONObject("data");

                    Config.setToken(jsonObject.getString("data"));

                    dialogLicai = new Dialog(this);
                    View view1 = LayoutInflater.from(this).inflate(R.layout.uploaddialog, null);
                    dialogLicai.setContentView(view1);
                    dialogLicai.setCanceledOnTouchOutside(false);
                    dialogLicai.setCancelable(false);
                    Window dialogWindow = dialogLicai.getWindow();

                    WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                    p.width = DisplayUtil.dip2px(this, 300f); // 宽度设置为屏幕的0.65，根据实际情况调整
                    p.height = DisplayUtil.dip2px(this, 200f); // 宽度设置为屏幕的0.65，根据实际情况调整
                    dialogWindow.setAttributes(p);

                    dialogLicai.show();
                    TextView sure = view1.findViewById(R.id.sure);
                    sure.setOnClickListener(v -> {
                        dialogLicai.dismiss();

                        if (PermissionUtils.permissionJudge(this, PermissionUtils.WRITE_EXTERNAL_STORAGE)) {
                            new UpdateVersionController(this).downLoadApk(Constants.BASE_URL_ONE + jsonObject.getString("info"));

                        } else {
                            rxPermissions
                                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    .subscribe(granted -> {
                                        if (granted) {
                                            new UpdateVersionController(this).downLoadApk(Constants.BASE_URL_ONE + jsonObject.getString("info"));
                                        } else {
                                            Utils.Toast("没有写内存的使用权限");
                                        }
                                    });
                        }
                    });
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

    }

    @Override
    public void fillData() {

        MyApp.getSingleInstance().addActivity(this);
        //自带侧滑菜单
//        initToolBar();
        StatusBarUtil.setStatusColor(this, true, true, R.color.colorPrimary);

        notice_num_id = this.findViewById(R.id.notice_num_id);
        notice_num_id.bringToFront();
        notice_num_id.setVisibility(View.GONE);
        initMenuData();


    }


    private void initMenuData() {
        bnvMain.setItemIconTintList(null);
        menuItems = new ArrayList<>();
        MenuItem navOverallWallet = bnvMain.getMenu().findItem(R.id.menu_main);
        navOverallWallet.setIcon(R.mipmap.icon_wallet_blue);
        menuItems.add(navOverallWallet);
        MenuItem navManager = bnvMain.getMenu().findItem(R.id.menu_management);
        menuItems.add(navManager);
        MenuItem navDiscovery = bnvMain.getMenu().findItem(R.id.menu_find);
        menuItems.add(navDiscovery);
        MenuItem navMy = bnvMain.getMenu().findItem(R.id.menu_my);
        menuItems.add(navMy);

        menuIcon = new ArrayList<>();
        menuIcon.add(R.mipmap.icon_wallet_gray);
        menuIcon.add(R.mipmap.icon_market_gray);
        menuIcon.add(R.mipmap.icon_found_gray);
        menuIcon.add(R.mipmap.icon_user_gray);
    }

    private void resetToDefaultIcon(int position) {
        for (int i = 0; i < menuItems.size(); i++) {
            if (i != position) {
                menuItems.get(i).setIcon(menuIcon.get(i));
            }
        }
    }

    /**
     * FrameLayout的选择状态
     *
     * @param position
     */
    private void setSelected(int position) {
        FragmentManager fm = getSupportFragmentManager();
        //开启一个事务
        ftr = fm.beginTransaction();
        //自定义一个方法，来隐藏所有的fragment
        hideTransaction(ftr);
        switch (position) {
            case 0:
                if (mainFrag == null) {
                    mainFrag = new MainFrag();
                    ftr.add(R.id.main_frame, mainFrag);
                }

                StatusBarUtil.setStatusColor(this, true, true, R.color.colorPrimary);
                ftr.show(mainFrag);

//                花去首页余额
                if (findFrag != null) {
                    mainFrag.onRefresh();
                }
                break;

            case 1:
                if (marketFrag == null) {
                    marketFrag = new MarketFrag();
                    ftr.add(R.id.main_frame, marketFrag);
                }

                StatusBarUtil.setStatusColor(this, true, true, R.color.colorPrimary);
                ftr.show(marketFrag);
                break;

            case 2:
//                if (findFrag == null) {
//                    findFrag = new FindFrag();
//                    ftr.add(R.id.main_frame, findFrag);
//                }
//
//                StatusBarUtil.setStatusColor(this, true, true, R.color.colorPrimary);
//                StatusBarUtil.setStatusBarColor(this, R.color.color_828EA1);
//
//                ftr.show(findFrag);
//                break;

                if (findFrag == null) {
                    findFrag = new FindFrag();
                    ftr.add(R.id.main_frame, findFrag);
                }
                StatusBarUtil.setStatusColor(this, true, true, R.color.colorPrimary);
                StatusBarUtil.setStatusBarColor(this, R.color.color_828EA1);
                ftr.show(findFrag);
                break;
            case 3:

                if (myFrag == null) {
                    myFrag = new MyFrag();
                    ftr.add(R.id.main_frame, myFrag);
                }
                StatusBarUtil.setStatusColor(this, true, false, R.color.color_0692c2);
                StatusBarUtil.setStatusBarColor(this, R.color.color_0692c2);
                ftr.show(myFrag);
                break;

            default:
                break;
        }
        ftr.commit();
    }


    /**
     * 隐藏fragment
     *
     * @param ftr
     */
    private void hideTransaction(FragmentTransaction ftr) {


        if (mainFrag != null) {
            //隐藏该fragment
            ftr.hide(mainFrag);
        }

        if (marketFrag != null) {
            ftr.hide(marketFrag);
        }

        if (findFrag != null) {
            ftr.hide(findFrag);
        }

        if (myFrag != null) {
            ftr.hide(myFrag);
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_transaction) {
            // Handle the camera action
            Utils.startActivity(this, WalletAlterPasswordAty.class);
        } else if (id == R.id.nav_about) {
            Utils.startActivity(this, MyAboutAty.class);
        } else if (id == R.id.nav_version) {

        } else if (id == R.id.nav_opinion) {
            Utils.startActivity(this, MyOpinionAty.class);
        } else if (id == R.id.nav_clear) {

        } else if (id == R.id.nav_share) {
            finish();
            Utils.startActivity(this, LoginAndRegisterAty.class);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
