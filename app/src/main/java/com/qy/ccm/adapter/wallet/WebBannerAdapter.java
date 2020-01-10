package com.qy.ccm.adapter.wallet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.qy.ccm.R;
import com.qy.ccm.aty.wallet.WalletCodeAty;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.function.GlideUtils;
import com.qy.ccm.view.group.banner.BannerLayout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class WebBannerAdapter extends RecyclerView.Adapter<WebBannerAdapter.MzViewHolder> {

    private Context context;
    private JSONArray jsonO;
    private BannerLayout.OnBannerItemClickListener onBannerItemClickListener;
    private BannerLayout.AddBannerScrollListener addBannerScrollListener;
    private Resources resources;
    public WebBannerAdapter(Context context, JSONArray jsonO) {
        this.context = context;
        this.jsonO = jsonO;
        resources = context.getResources();
    }

    public void setData(JSONArray jsonO) {
        this.jsonO = jsonO;
    }

    public void setOnBannerItemClickListener(BannerLayout.OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }
    public void addOnScrollListener(BannerLayout.AddBannerScrollListener addBannerScrollListener) {
        this.addBannerScrollListener = addBannerScrollListener;
    }

    @Override
    public WebBannerAdapter.MzViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
        int mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        RecyclerView.LayoutParams relParams =(RecyclerView.LayoutParams) view1.getLayoutParams();
        relParams.width = (int)(mScreenWidth * 0.9); //设置宽度
        view1.setLayoutParams(relParams);
        return new MzViewHolder(view1);
    }


    @Override
    public void onBindViewHolder(WebBannerAdapter.MzViewHolder holder, final int position) {
        if (jsonO == null || jsonO.isEmpty()) {
            return;
        }
        JSONObject pojo = jsonO.getJSONObject(position);
        if (pojo == null || pojo.isEmpty()) {
            return;
        }
        holder.ccm_coin_name.setText(pojo.getString("walletName"));
        holder.zongzichan_cny_title.setText(pojo.getString("tokenAddress"));
        holder.ccm_balance_id.setText(pojo.getString("price"));

//        holder.ccm_coin_name_address_qcore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putString("tokenAddress", pojo.getString("tokenAddressAll"));
//                Utils.startActivity(context, WalletCodeAty.class, bundle);
//            }
//        });

        Drawable btnDrawable;
        if(position == 0){
            btnDrawable = resources.getDrawable(R.drawable.dw_home_background00);
            holder.root_rl_id.setBackground(btnDrawable);
        }else if(position == 1){
            btnDrawable = resources.getDrawable(R.drawable.dw_home_background01);
            holder.root_rl_id.setBackground(btnDrawable);
        }else if(position == 2){
            btnDrawable = resources.getDrawable(R.drawable.dw_home_background02);
            holder.root_rl_id.setBackground(btnDrawable);
        }
//        holder.itemView.setOnI
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onBannerItemClickListener != null) {
                    onBannerItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (jsonO != null) {
            return jsonO.size();
        }
        return 0;
    }


    class MzViewHolder extends RecyclerView.ViewHolder {

        TextView ccm_coin_name, zongzichan_cny_title,ccm_balance_id;
        ImageView ccm_coin_name_address_qcore;
        RelativeLayout root_rl_id;
        MzViewHolder(View itemView) {
            super(itemView);
            root_rl_id = itemView.findViewById(R.id.root_rl_id);

            ccm_coin_name = itemView.findViewById(R.id.ccm_coin_name_item);
            zongzichan_cny_title = itemView.findViewById(R.id.zongzichan_cny_title_item);
            ccm_balance_id = itemView.findViewById(R.id.ccm_balance_id_item);
            ccm_coin_name_address_qcore = itemView.findViewById(R.id.ccm_coin_name_address_qcore_item);

        }
    }

}
