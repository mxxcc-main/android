package com.qy.ccm.aty.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.qy.ccm.R;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.bean.other.rxbus.ImportWalletEvent;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.rxbus.ErrorBean;
import com.qy.ccm.utils.rxbus.RxBusHelper;
import com.qy.ccm.view.dialog.CreateSucceedDialog;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:导入助记词
 * Data：2019/5/13-4:21 PM
 */
public class ImputWalletAty extends BaseAty {
    @BindView(R.id.add_wallet_new)
    NavigationLucencyLayout addWalletNew;
    private String coin;
    CreateSucceedDialog createSucceedDialog;

    @OnClick({R.id.tv_input_wallet_new, R.id.tv_input_wallet_input, R.id.tv_input_wallet_input2})
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.tv_input_wallet_new:
                bundle.putString("coin", coin);
                myStartActivity(WalletMnemonicAty.class, bundle);
                break;
            case R.id.tv_input_wallet_input:
                bundle.putString("coin", coin);
                bundle.putString("state", "11");
                myStartActivityForResult(ImputWalletPrivateAty.class, bundle, 11);
                break;
            case R.id.tv_input_wallet_input2:
                bundle.putString("coin", coin);
                bundle.putString("state", "12");
                myStartActivity(ImputWalletPrivateAty.class, bundle);
                break;
            default:
                break;
        }
    }

    @Override
    public int initView() {
        return R.layout.aty_imput_wallet;
    }

    @Override
    public void setListener() {
        createSucceedDialog.setOkListener(new CreateSucceedDialog.ClickOkListener() {
            @Override
            public void onTextPow() {
                finish();
                ImportWalletEvent walletEvent = new ImportWalletEvent();
                RxBusHelper.post(walletEvent);
            }

        });
        //通知创建钱包
        RxBusHelper.doOnMainThread(ImportWalletEvent.class, new RxBusHelper.OnEventListener<ImportWalletEvent>() {
            @Override
            public void onEvent(ImportWalletEvent walletEvent) {
                Log.e("==========", "walletEvent:" + "弹出对话框");
                createSucceedDialog.show();
            }

            @Override
            public void onError(ErrorBean errorBean) {

            }
        });
    }

    @Override
    public void fillData() {
        addWalletNew.setTitle("添加钱包");
        coin = getIntent().getStringExtra("coin");
        createSucceedDialog = new CreateSucceedDialog(this);
        Log.e("==========", "coin:" + coin);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 12) {
            createSucceedDialog.show();
        }
    }
}
