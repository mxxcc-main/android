package com.qy.ccm.utils.function;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.qy.ccm.app.MyApp;


public class ScreenUtils {


    /**
     * 750设计方案，标准宽
     */
    private static final int STANDARD_WIDTH = 750;

    /**
     * 750设计方案，默认对话框宽度
     */
    public static final int DIALOG_DEFAULT_WIDTH = 590;

    /**
     * 对话框默认背景透明度50%
     */
    public static final float ALPHA_DEFAULT_VALUE = 0.5f;

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取相对750宽度的宽
     * * AW/AH=RW/RH
     * <p>
     * <p>
     * <p>
     * RW=AW*RH/AH
     *
     * @param absoluteWidth
     * @return
     */
    public static int getRelWidth(int absoluteWidth) {


        return MyApp.singleInstance.widthPixels * absoluteWidth / STANDARD_WIDTH;
    }

    /**
     * 获取相对750高度的高
     * <p>
     * AW/AH=RW/RH
     * <p>
     * RH=AH*RW/AW
     *
     * @param absoluteHeight
     * @return
     */
    public static int getRelHeight(int absoluteHeight) {
        return MyApp.singleInstance.widthPixels * absoluteHeight / STANDARD_WIDTH;


    }
}
