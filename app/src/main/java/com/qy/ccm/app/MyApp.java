package com.qy.ccm.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;

import com.qy.ccm.retrofit.HttpRetrofitRequest;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.Utils;
import com.tencent.bugly.crashreport.CrashReport;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.litepal.LitePal;

import java.util.ArrayList;

public class MyApp extends Application {

    public static MyApp singleInstance;
    /**
     * 屏幕密度
     */
    public float mDensity;
    /**
     * 屏幕的宽高
     */
    public int widthPixels, heightPixels;
    public static ArrayList<Activity> allActivities = new ArrayList<Activity>();
    public static Context appContext;
    public static MyApp app;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    @Override
    public void onCreate() {
        super.onCreate();

        HttpUtils.initHttpRequest(HttpRetrofitRequest.getInstances());
        // 初始化LitePal数据库
        LitePal.initialize(this);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        widthPixels = displayMetrics.widthPixels;
        heightPixels = displayMetrics.heightPixels;
        mDensity = displayMetrics.density;
        singleInstance = this;
        appContext = getApplicationContext();
        app = this;
//        TODO
//        Multidex.install(this);
        initBugly();
//        CrashReport.initCrashReport(getApplicationContext(), "1991c5bd49", false);
        //在这里初始化
//        BugtagsOptions options = new BugtagsOptions.Builder().trackingCrashlog(true).build();
//        BugtagsOptions options = new BugtagsOptions.Builder().trackingCrashLog(true).build();
//        Bugtags.start("b383b452db23242899b2d5df371e7481", this, Bugtags.BTGInvocationEventBubble, options);
//        Bugtags.start("b383b452db23242899b2d5df371e7481", this, Bugtags.BTGInvocationEventBubble);
    }


    public static Context getConText() {
        return appContext;
    }

    public static MyApp getApp() {
        return app;
    }

    /**
     * 返回一个单例的application
     */
    @NonNull
    public static MyApp getSingleInstance() {
        return singleInstance;
    }

    public static void addActivity(Activity activity) {
        allActivities.add(activity);
    }

    public static void delActivity(Activity activity) {
        allActivities.remove(activity);
    }


    public static void exitAllActivity() {
        for (Activity activity : allActivities) {
            activity.finish();
        }
    }

    /**
     * 初始化腾讯bug管理平台
     */
    private void initBugly() {
        /* Bugly SDK初始化
         * 参数1：上下文对象
         * 参数2：APPID，平台注册时得到,注意替换成你的appId
         * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
         * 注意：如果您之前使用过Bugly SDK，请将以下这句注释掉。
         */
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());

        strategy.setAppVersion(Utils.getVersionName(this));
        strategy.setAppPackageName("com.qy.ccm");
        strategy.setAppReportDelay(200);                          //Bugly会在启动20s后联网同步数据

        /*  第三个参数为SDK调试模式开关，调试模式的行为特性如下：
            输出详细的Bugly SDK的Log；
            每一条Crash都会被立即上报；
            自定义日志将会在Logcat中输出。
            建议在测试阶段建议设置成true，发布时设置为false。*/

        CrashReport.initCrashReport(getApplicationContext(), "1991c5bd49", false ,strategy);

        //Bugly.init(getApplicationContext(), "1374455732", false);
    }

}
