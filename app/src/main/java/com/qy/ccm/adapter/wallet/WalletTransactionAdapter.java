package com.qy.ccm.adapter.wallet;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qy.ccm.R;
import com.qy.ccm.bean.wallet.RecordBean;
import com.qy.ccm.bean.wallet.WalletTabBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Data：2019/5/10-2:04 PM
 *
 * @author
 */
public class WalletTransactionAdapter extends BaseQuickAdapter<RecordBean, BaseViewHolder> {
    private String tokenName;
    List<RecordBean> listStr = new ArrayList<>();


    public WalletTransactionAdapter(int layoutResId, @Nullable List<RecordBean> data, String tokenName) {
        super(layoutResId, data);
        this.tokenName = tokenName;
    }

    @Override
    protected void convert(BaseViewHolder helper, RecordBean item) {
        TextView tvTransactionName = helper.getView(R.id.tv_transaction_name);
        tvTransactionName.setText(tokenName);
        TextView tvTransactionTime = helper.getView(R.id.tv_transaction_time);
        tvTransactionTime.setText(item.getTime());
        TextView tvTransactionAmount = helper.getView(R.id.tv_transaction_amount);
        tvTransactionAmount.setText(item.getAmunt());

    }


}
