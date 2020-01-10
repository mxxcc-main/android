package com.qy.ccm.adapter.other;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.qy.ccm.R;
import com.qy.ccm.app.MyApp;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Zhaoqi
 * <p>
 */
public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.MviewHolder> implements View.OnClickListener {
    private JSONArray jsonNArray;
    private Context context;
    private CallBack mCallBack;
    private Double saptRate;
    private BannerAdapter.ItemClickListener itemClickListener;

    public MarketAdapter(Context context, Double saptRate, JSONArray jsonNArray, CallBack callBack) {
        this.jsonNArray = jsonNArray;
        this.context = context;
        mCallBack = callBack;

        this.saptRate = saptRate;
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

    @Override
    public void onClick(View v) {
//        mCallBack.click(v,);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_item, parent, false);
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
//        blue  #41c977
//        red  #FB4848

        if (pojo.getString("change_percent").contains("%")) {


            holder.up_down.setText(pojo.getString("change_percent"));
            if (pojo.getString("change_percent").contains("+")) {
                holder.up_down.setBackgroundColor(Color.parseColor("#FB4848"));
            } else {
                holder.up_down.setBackgroundColor(Color.parseColor("#41c977"));

            }
            holder.up_down.setTextColor(Color.parseColor("#FFFFFF"));

            holder.coin_usd.setText("$" + pojo.getString("current_price_usd"));
            holder.coin_rmb.setText("￥" + pojo.getString("current_price_cny"));
            holder.coin_name.setText(pojo.getString("name"));

        } else {

            holder.up_down.setText(pojo.getString("change_percent") + "%");
            if (pojo.getDoubleValue("change_percent") > 0) {
                holder.up_down.setBackgroundColor(Color.parseColor("#FB4848"));
            } else {
                holder.up_down.setBackgroundColor(Color.parseColor("#41c977"));

            }
            holder.up_down.setTextColor(Color.parseColor("#FFFFFF"));

            holder.coin_usd.setText("$" + pojo.getString("current_price_usd"));
            holder.coin_rmb.setText("￥" + new BigDecimal(pojo.getDoubleValue("current_price_usd") * saptRate).setScale(2, RoundingMode.DOWN));
            holder.coin_name.setText(pojo.getString("name"));

        }
        RequestOptions requestOptions = new RequestOptions()
                //正在请求图片的时候展示的图片
                .placeholder(R.mipmap.ccm)
                //如果请求失败的时候展示的图片
                .error(R.mipmap.ccm)
                //如果请求的url/model为 null 的时候展示的图片
                .fallback(R.mipmap.ccm)
                // 缓存修改过的图片
//                .skipMemoryCache(flase)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        // 加载图片
        Glide.with(MyApp.getSingleInstance()).load(pojo.getString("logo"))
                // 设置错误图片
                .apply(requestOptions)
                .into(holder.coin_img);


//        holder.itemView.setOnClickListener(v -> mCallBack.click(v, pojo, position));
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

        public TextView up_down, coin_usd, coin_rmb, coin_name;
        public ImageView coin_img;

        public MviewHolder(final View itemView) {
            super(itemView);

            this.up_down = itemView.findViewById(R.id.up_down);
            this.coin_usd = itemView.findViewById(R.id.coin_usd);
            this.coin_rmb = itemView.findViewById(R.id.coin_rmb);
            this.coin_name = itemView.findViewById(R.id.coin_name);
            this.coin_img = itemView.findViewById(R.id.coin_img);
        }
    }
}
