package com.qy.ccm.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.qy.ccm.R;
import com.qy.ccm.view.base.BaseDialog;
import com.qy.ccm.view.group.password.PayPwdEditText;


/**
 * 带有文字信息统一确认取消对话框
 * crobot
 *
 * @author xq
 */
public class PowDialog extends BaseDialog {

    private PayPwdEditText ppeWalletAffirmpassword;
    private ClickOkListener listener;
    private String password;
    private Context context;

    public PowDialog(Context context) {
        super(context);
        this.context = context;
    }

    public PowDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected PowDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }


    public interface ClickOkListener {
        void onTextPow(String pow);

        void clickcancle();

        void clickclose();

    }

    public void setOkListener(ClickOkListener listener) {
        this.listener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pow_dialog);
        ppeWalletAffirmpassword = findViewById(R.id.ppe_wallet_affirmpassword);

        /**
         * @param bgcolor 背景drawable
         * @param pwdlength 密码长度
         * @param slpilinewidth 分割线宽度
         * @param splilinecolor 分割线颜色
         * @param pwdcolor 密码字体颜色
         * @param pwdsize 密码字体大小
         */
        ppeWalletAffirmpassword.initStyle(R.drawable.edit_num_bg2, 6, 0.33f, R.color.black, R.color.black, 30);
        ppeWalletAffirmpassword.showKey();


        //密码输入完后的回调
        ppeWalletAffirmpassword.setOnTextFinishListener(str -> {
            password = str;
            //手动收起软键盘
            InputMethodManager imm = (InputMethodManager) context.
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ppeWalletAffirmpassword.getWindowToken(), 0);
            dismiss();
            listener.onTextPow(password);
        });

    }

    @Override
    public void show() {
        super.show(590, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (ppeWalletAffirmpassword != null) {
            ppeWalletAffirmpassword.clearText();
        }
    }
}
