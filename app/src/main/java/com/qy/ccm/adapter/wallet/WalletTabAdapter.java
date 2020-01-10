package com.qy.ccm.adapter.wallet;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qy.ccm.R;
import com.qy.ccm.aty.wallet.ImputWalletAty;
import com.qy.ccm.bean.wallet.WalletTabBean;
import com.qy.ccm.utils.Utils;

import java.util.List;

/**
 * Description:
 * Data：2019/5/8-7:47 PM
 */
public class WalletTabAdapter extends BaseQuickAdapter<WalletTabBean, BaseViewHolder> {
    private Context context;

    public WalletTabAdapter(Context context, int layoutResId, @Nullable List<WalletTabBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletTabBean tabBean) {
        TextView tvWalletTab = helper.getView(R.id.tv_wallet_tab);
        LinearLayout llWalletTab = helper.getView(R.id.ll_wallet_tab);
        LinearLayout llWalletTabmore = helper.getView(R.id.ll_wallet_tabmore);
        tvWalletTab.setText(tabBean.getTabContext());

        if (tabBean.isTabSelect()) {
            llWalletTab.setBackgroundResource(R.drawable.shape_bn_oval_dp14);
            Drawable drawable = ContextCompat.getDrawable(context, R.mipmap.icon_home_drop_down);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvWalletTab.setCompoundDrawables(null, null, drawable, null);
            tvWalletTab.setTextColor(ContextCompat.getColor(context, R.color.color_ffffff));
        } else {
            llWalletTab.setBackgroundResource(R.color.transparent);
            tvWalletTab.setTextColor(ContextCompat.getColor(context, R.color.color_33ffffff));
            tvWalletTab.setCompoundDrawables(null, null, null, null);
        }

        if (tabBean.isTabSelect2()) {
            if (tabBean.isTabSelect()) {
                llWalletTabmore.setVisibility(View.VISIBLE);
            }
        } else {
            llWalletTabmore.setVisibility(View.GONE);
        }

        llWalletTabmore.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("coin", tabBean.getTabContext());
            Utils.startActivity(context, ImputWalletAty.class, bundle);
        });

    }


}
