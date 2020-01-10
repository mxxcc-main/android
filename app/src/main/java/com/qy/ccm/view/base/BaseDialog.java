package com.qy.ccm.view.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.qy.ccm.R;
import com.qy.ccm.utils.function.ScreenUtils;

/**
 * base dialog
 */
public class BaseDialog extends Dialog {


    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.pow_dialog);
        //设置无背景边框
        getWindow().setBackgroundDrawable(new ColorDrawable());
        //设置透明度为50%
        getWindow().setDimAmount(0.5f);
        this.setCancelable(true);
    }

    public void showDefault() {
        this.show(ScreenUtils.DIALOG_DEFAULT_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void show(int width, int height) {
        super.show();
        height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 获取对话框当前的参数值
        WindowManager.LayoutParams params = getWindow().getAttributes();
        if (width > 0 && height > 0) {
            params.height = ScreenUtils.getRelWidth(height);
            params.width = ScreenUtils.getRelWidth(width);
        }

        if (width > 0 && height < 0) {
            params.height = height;
            params.width = ScreenUtils.getRelWidth(width);
        }
        if (width < 0 && height > 0) {
            params.height = ScreenUtils.getRelWidth(height);
            params.width = width;
        }
        if (width < 0 && height < 0) {
            params.height = height;
            params.width = width;
        }
        getWindow().setAttributes(params);
    }
}
