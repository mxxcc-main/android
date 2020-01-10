package com.qy.ccm.utils.function;

import android.app.ProgressDialog;
import android.content.Context;

import com.qy.ccm.R;
import com.qy.ccm.app.MyApp;

/**
 * Description:
 * Data：2019/5/5-5:14 PM
 */
public class WidgetUtils {
    private ProgressDialog progressDialog;
    private boolean showProgress = true;
    private Context context;

    public WidgetUtils(Context context) {
        this.context = context;
    }


    public void showProgressDialog() {
        progressDialog = new ProgressDialog(context);
        //3.设置显示内容
        progressDialog.setMessage(MyApp.getSingleInstance().getResources().getString(R.string.loadingplswait));
        if (showProgress && null != progressDialog) {
//            触摸外部不能取消
            progressDialog.setCanceledOnTouchOutside(false);
//            返回键可以取消 dialog
//            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }


    public void dismissProgressDialog() {
        if (showProgress && null != progressDialog) {
            progressDialog.dismiss();
        }
    }
}
