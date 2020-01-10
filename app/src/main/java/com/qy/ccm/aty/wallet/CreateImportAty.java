package com.qy.ccm.aty.wallet;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qy.ccm.R;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.function.ScreenUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateImportAty extends BaseAty {

    @BindView(R.id.change_language_id)
    ImageView change_language_id;

    @BindView(R.id.create_wallet_id)
    TextView create_wallet_id;

    @BindView(R.id.import_wallet_id)
    TextView import_wallet_id;

    @BindView(R.id.ttt)
    TextView ttt;

    @BindView(R.id.checkBox)
    CheckBox checkBox;

    private Dialog dialog;

    @Override
    public int initView() {
        return R.layout.aty_create_import;
    }

    @OnClick({R.id.change_language_id, R.id.create_wallet_id, R.id.import_wallet_id, R.id.ttt})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.change_language_id:

                if (!checkBox.isChecked()) {
                    Utils.Toast("请阅读并勾选服务协议");
                    return;
                }

                myStartActivity(ChangeLanguageAty.class);
                break;
            case R.id.create_wallet_id:

                if (!checkBox.isChecked()) {
                    Utils.Toast("请阅读并勾选服务协议");
                    return;
                }

                myStartActivity(CreateWalletAty.class);
                finish();
                break;
            case R.id.ttt:
                myStartActivity(UserProtiAty.class);
                break;
            case R.id.import_wallet_id:

                if (!checkBox.isChecked()) {
                    Utils.Toast("请阅读并勾选服务协议");
                    return;
                }

//                dialog
                //弹框输入交易密码
                dialog = new Dialog(this, R.style.DialogThemeHalfAlpa);
                View view1 = LayoutInflater.from(this).inflate(R.layout.private_key_and_nonecmic_word_dialog, null);
                dialog.setContentView(view1);
                dialog.setCanceledOnTouchOutside(true);
                Window dialogWindow = dialog.getWindow();

                WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//                                    p.height = DisplayUtil.dip2px(activity, 230f) // 高度设置为屏幕的0.6，根据实际情况调整
                int hi = ScreenUtils.getScreenWidth(this);
//                p.width = DisplayUtil.dip2px(this, Float.valueOf(hi)); // 宽度设置为屏幕的0.65，根据实际情况调整
                p.width = hi; // 宽度设置为屏幕的0.65，根据实际情况调整
                dialogWindow.setAttributes(p);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);

                dialog.show();

                LinearLayout jump_private_key = view1.findViewById(R.id.jump_private_key);
                LinearLayout nonemic_word_id = view1.findViewById(R.id.nonemic_word_id);
                ImageView dismiss_id = view1.findViewById(R.id.dismiss_id);

                jump_private_key.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myStartActivity(PrivateKeyImportAty.class);
                        finish();

                    }
                });
                nonemic_word_id.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myStartActivity(NonnemicWordImportAty.class);
                        finish();

                    }
                });
                dismiss_id.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
//                myStartActivity(PrivateKeyImportAty.class);
                break;
            default:
                break;
        }
    }


    @Override
    public void setListener() {


    }

    @Override
    public void fillData() {

        showStatusBar(false);
    }
}
