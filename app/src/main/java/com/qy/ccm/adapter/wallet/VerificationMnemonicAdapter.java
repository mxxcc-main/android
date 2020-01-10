package com.qy.ccm.adapter.wallet;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qy.ccm.R;
import com.qy.ccm.aty.wallet.WalletMnemonicAty;
import com.qy.ccm.bean.my.MnemonicBean;

import java.util.List;

/**
 * Description:
 * Data：2019/5/6-3:42 PM
 */
public class VerificationMnemonicAdapter extends BaseQuickAdapter<MnemonicBean, BaseViewHolder> {

    private Context context;

    public VerificationMnemonicAdapter(int layoutResId, Context context, @Nullable List<MnemonicBean> mnemonicList) {
        super(layoutResId, mnemonicList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MnemonicBean bean) {
        TextView tvMarket = helper.getView(R.id.tv_market);
        tvMarket.setText(bean.getContext());
        if (bean.isSelect()) {
            tvMarket.setBackgroundResource(R.color.color_f1f1f1);
        } else {
            tvMarket.setBackgroundResource(R.color.color_eaecf5);

        }

    }


}
