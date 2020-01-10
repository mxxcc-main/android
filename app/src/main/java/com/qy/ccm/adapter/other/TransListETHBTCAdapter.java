package com.qy.ccm.adapter.other;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qy.ccm.R;
import com.qy.ccm.bean.wallet.RecordBean;

import java.util.List;

/**
 * Created by Zhaoqi
 * <p>
 */
public class TransListETHBTCAdapter extends RecyclerView.Adapter<TransListETHBTCAdapter.MviewHolder> implements View.OnClickListener {
    private List<RecordBean> liststr;
    private Context context;
    private CallBack mCallBack;
    private String address;
    private BannerAdapter.ItemClickListener itemClickListener;

    public TransListETHBTCAdapter(Context context, List<RecordBean> liststr, String address, CallBack callBack) {
        this.liststr = liststr;
        this.context = context;
        mCallBack = callBack;
        this.address = address;
    }

//    public MarketAdapter(WalletMarketAty walletMarketAty, Double saptRate, JSONArray jsonArray, Object o,CallBack mCallBack) {
//        this.jsonNArray = jsonNArray;
//        this.context = context;
//        this.mCallBack = mCallBack;
//        this.saptRate = saptRate;
//    }

    public void setqOrgs(List<RecordBean> liststr) {
        this.liststr = liststr;
    }

    @Override
    public void onClick(View v) {
//        mCallBack.click(v,);
    }

    public interface CallBack {
        void click(View view, RecordBean pojo, int position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public MviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trans_list_item, parent, false);
        MviewHolder mviewHolder = new MviewHolder(view);
        return mviewHolder;
    }

    @Override
    public void onBindViewHolder(MviewHolder holder, int position) {

        if (liststr == null || liststr.size() < 1) {
            return;
        }
        RecordBean pojo = liststr.get(position);

        if (pojo == null) {
            return;
        }
//        blue  color_00c9a7
//        red  #FB4848
//        coin_name, coin_rmb, in_out, trans_time;
        holder.coin_name.setText(pojo.getCoinName());

        if (pojo.getAddress().toLowerCase().equals(address.toLowerCase())) {
            holder.in_out.setBackgroundColor(Color.parseColor("#FB4848"));
            holder.in_out.setText("OUT");
        }
//
// else {
//            holder.in_out.setBackgroundColor(Color.parseColor("#41c977"));
//        }
//        holder.in_out.setText("OUT");

//        判断是成功还是失败
        holder.trans_status.setText("success");
        holder.trans_status.setTextColor(Color.parseColor("#00c9a7"));


        holder.coin_rmb.setText(pojo.getAmunt().toString());

        holder.trans_time.setText(pojo.getTime());
    }

    @Override
    public int getItemCount() {
        if (liststr == null)
            return 0;
        else
            return liststr.size();
    }

    public class MviewHolder extends RecyclerView.ViewHolder {
//
//        字段{
//            days ：2019-09-18
//            income:0.1(收益)
//                    recommend:0.1 （激励）
//        }

        public TextView coin_name, coin_rmb, in_out, trans_time, trans_status;

        public MviewHolder(final View itemView) {
            super(itemView);

            this.coin_name = itemView.findViewById(R.id.coin_name);
            this.coin_rmb = itemView.findViewById(R.id.coin_rmb);
            this.in_out = itemView.findViewById(R.id.in_out);
            this.trans_time = itemView.findViewById(R.id.trans_time);
            this.trans_status = itemView.findViewById(R.id.trans_status);
        }
    }
}
