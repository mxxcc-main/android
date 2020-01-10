package com.qy.ccm.adapter.wallet;


import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qy.ccm.R;
import com.qy.ccm.aty.wallet.WalletMnemonicAty;

import java.util.List;

/**
 * Description:
 * Data：2019/5/6-3:42 PM
 */
public class MnemonicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public MnemonicAdapter(int layoutResId, WalletMnemonicAty walletMnemonicAty, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView tvMarket = helper.getView(R.id.tv_market);
        tvMarket.setText(item);
    }

}
