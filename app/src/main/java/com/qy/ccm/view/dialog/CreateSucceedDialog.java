package com.qy.ccm.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.qy.ccm.R;
import com.qy.ccm.view.base.BaseDialog;
import com.qy.ccm.view.group.password.PayPwdEditText;


/**
 * 带有文字信息统一确认取消对话框
 * crobot
 *
 * @author xq
 */
public class CreateSucceedDialog extends BaseDialog {

    private TextView tvCreateSucceed;
    private ClickOkListener listener;
    private Context context;

    public CreateSucceedDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CreateSucceedDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected CreateSucceedDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }


    public interface ClickOkListener {
        void onTextPow();


    }

    public void setOkListener(ClickOkListener listener) {
        this.listener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_create_succeed);
        tvCreateSucceed = findViewById(R.id.tv_create_succeed);
        tvCreateSucceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("==========", "成功点击了");
                listener.onTextPow();
            }
        });
    }

    @Override
    public void show() {
        super.show(590, ViewGroup.LayoutParams.WRAP_CONTENT);

    }
}
