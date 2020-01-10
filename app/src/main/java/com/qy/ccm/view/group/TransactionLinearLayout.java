package com.qy.ccm.view.group;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.qy.ccm.R;
import com.qy.ccm.view.base.BaseLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description:
 * Data：2019/5/10-5:43 PM
 *
 * @author
 */
public class TransactionLinearLayout extends BaseLinearLayout {

    @BindView(R.id.tv_transaction_title)
    TextView tvTransactionTitle;
    @BindView(R.id.tv_transaction_context)
    TextView tvTransactionContext;


    public TransactionLinearLayout(Context context) {
        super(context);
    }

    public TransactionLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TransactionLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TransactionLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setAttrs(AttributeSet attrs) {

    }

    @Override
    public void initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.layout_transaction, this);
        ButterKnife.bind(view);
    }

    public void setTitle(String context) {
        tvTransactionTitle.setText(context);
    }

    public void setContext(String context) {
        tvTransactionContext.setText(context);
    }

}
