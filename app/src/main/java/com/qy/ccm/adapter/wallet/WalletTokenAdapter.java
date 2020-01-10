package com.qy.ccm.adapter.wallet;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.qy.ccm.R;
import com.qy.ccm.bean.other.entity.WalletToken;
import com.qy.ccm.bean.other.entity.WalletTokenState;
import com.qy.ccm.aty.wallet.WalletTransactionAty;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.function.StringUtils;

import java.util.List;

/**
 * Description:
 * Data：2019/5/9-11:59 AM
 *
 * @author
 */
public class WalletTokenAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public static final int SITE = 1;
    public static final int TOKEN = 2;
    private Context mContext;

    public WalletTokenAdapter(Context context, List<MultiItemEntity> tokenEntityList) {
        super(tokenEntityList);
        this.mContext = context;
        addItemType(SITE, R.layout.item_wallet_site);
        addItemType(TOKEN, R.layout.item_wallet_token);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case SITE:
                TextView tvWalletSite = helper.getView(R.id.tv_wallet_site);
                TextView tvWalletCopy = helper.getView(R.id.tv_wallet_copy);
                LinearLayout llWalletTab = helper.getView(R.id.ll_wallet_tab);

                final WalletTokenState lv1 = (WalletTokenState) item;
                tvWalletSite.setText(lv1.getState());
                helper.itemView.setOnClickListener(v -> {
                    int pos = helper.getAdapterPosition();
                    if (lv1.isExpanded()) {
                        llWalletTab.setBackgroundResource(R.mipmap.home_wallet_background_open);
                        notifyDataSetChanged();
                        collapse(pos);
                        Log.e("==========", "切换成圆角的");
                    } else {
                        llWalletTab.setBackgroundResource(R.mipmap.home_wallet_background_close);
                        notifyDataSetChanged();
                        expand(pos);
                        Log.e("==========", "切换成半圆角的");

                    }
                });
                //复制
                tvWalletCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringUtils.isCopy(lv1.getState());
                    }
                });
                break;
            case TOKEN:
                TextView tvWalletName = helper.getView(R.id.tv_wallet_name);
                TextView tvWalletAmount = helper.getView(R.id.tv_wallet_amount);
                TextView tvWalletPrice = helper.getView(R.id.tv_wallet_price);
                WalletToken walletToken = (WalletToken) item;
                tvWalletName.setText(walletToken.getTokenName());
                tvWalletAmount.setText(walletToken.getAmount());
                tvWalletPrice.setText(walletToken.getPrice());
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WalletToken walletToken = (WalletToken) item;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("walletToken", walletToken);
                        Utils.startActivity(mContext, WalletTransactionAty.class, bundle);
                    }
                });


                break;
            default:
                break;

        }
    }
}
