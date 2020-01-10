package com.qy.ccm.adapter.other;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qy.ccm.R;
import com.qy.ccm.utils.blockchain.eth.CCMUtils;
import com.qy.ccm.utils.function.TimeUtils;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * Created by Zhaoqi
 * <p>
 */
public class TransListAdapter extends RecyclerView.Adapter<TransListAdapter.MviewHolder> {
    private JSONArray jsonNArray;
    private Context context;
    private CallBack mCallBack;
    private String address;
    private BannerAdapter.ItemClickListener itemClickListener;

    public TransListAdapter(Context context, JSONArray jsonNArray, String address, CallBack callBack) {
        this.jsonNArray = jsonNArray;
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

    public void setqOrgs(JSONArray jsonNArray) {
        this.jsonNArray = jsonNArray;
    }

    public interface CallBack {
        void click(View view, JSONObject pojo, int position);
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

        if (jsonNArray == null || jsonNArray.size() < 1) {
            return;
        }
        JSONObject pojo = jsonNArray.getJSONObject(position);

        if (pojo == null || pojo.size() < 1) {
            return;
        }
//        blue  color_00c9a7
//        red  #FB4848
//        coin_name, coin_rmb, in_out, trans_time;
        holder.coin_name.setText(pojo.getString("tokenName"));
        if (pojo.getString("tokenName") == null || "".equals(pojo.getString("tokenName"))) {
            holder.coin_name.setText("CCM");
        }

        if (pojo.getString("from").toLowerCase().equals(address.toLowerCase())) {
            holder.in_out.setBackgroundColor(Color.parseColor("#FB4848"));
            holder.in_out.setText("OUT");
        }
//
        else {
            holder.in_out.setBackgroundColor(Color.parseColor("#41c977"));
            holder.in_out.setText("IN");

        }

//        判断是成功还是失败
        if ("0x1".equals(pojo.getString("status"))) {
            holder.trans_status.setText("success");
            holder.trans_status.setTextColor(Color.parseColor("#00c9a7"));

        }


        if (new BigDecimal(pojo.getString("value")).compareTo(new BigDecimal("0")) > 0) {
            holder.coin_rmb.setText(CCMUtils.formatEth(new BigInteger(pojo.getString("value"))).toPlainString());
        } else {
            holder.coin_rmb.setText(pojo.getString("realValue").toString());
        }

        holder.trans_time.setText(TimeUtils.long2String(pojo.getLongValue("timestamp") * 1000, TimeUtils.FORMAT_TYPE_3).toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.click(v, pojo, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (jsonNArray == null)
            return 0;
        else
            return jsonNArray.size();
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
