package com.qy.ccm.aty.wallet;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qy.ccm.R;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.utils.function.StringUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:二维码
 * Data：2019/5/14-8:00 PM
 *
 * @author
 */
public class WalletCodeAty extends BaseAty {
    @BindView(R.id.tv_wallet_code)
    TextView tvWalletCode;
    @BindView(R.id.iv_code_copy)
    ImageView ivCodeCopy;
    @BindView(R.id.nl_wallet_code)
    com.qy.ccm.view.group.NavigationLucencyLayout nl_wallet_code;

    Bitmap mBitmap = null;
    String tokenAddress;

    @OnClick({R.id.tv_code_copy})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_code_copy:
                StringUtils.isCopy(tokenAddress);
                break;
            default:
                break;
        }
    }

    @Override
    public int initView() {
        return R.layout.aty_wallet_code;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillData() {
        StatusBarUtil.setStatusColor(this, true, false, R.color.colorPrimary);
        nl_wallet_code.setTitle("收款");
        tokenAddress = getIntent().getStringExtra("tokenAddress");
        code();
    }

    /**
     * 生成二维码
     */
    private void code() {
        tvWalletCode.setText(tokenAddress);
        mBitmap = CodeUtils.createImage(tokenAddress, 400, 400, null);
        ivCodeCopy.setImageBitmap(mBitmap);
    }
}
