package com.qy.ccm.adapter.wallet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.qy.ccm.R;
import com.qy.ccm.config.Config;
import com.qy.ccm.retrofit.Constants;
import com.qy.ccm.utils.function.GlideUtils;
import com.qy.ccm.view.group.banner.BannerLayout;

import java.util.List;

public class ManagerBannerAdapter extends RecyclerView.Adapter<ManagerBannerAdapter.MzViewHolder> {

    private Context context;
    private JSONArray jsonArray;
    private BannerLayout.OnBannerItemClickListener onBannerItemClickListener;

    public ManagerBannerAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    public void setOnBannerItemClickListener(BannerLayout.OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    @Override
    public ManagerBannerAdapter.MzViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MzViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manager_image, parent, false));
    }

    public void setDate(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    ;

    @Override
    public void onBindViewHolder(ManagerBannerAdapter.MzViewHolder holder, final int position) {
        if (jsonArray == null || jsonArray.isEmpty()) {
            return;
        }
        final int P = position % jsonArray.size();
        String url = jsonArray.getString(P);

//        if (!url.startsWith("http")) {
            url = Constants.BASE_URL_ONE + url;
//        }

        ImageView img = holder.imageView;
//        Glide.with(context).load(url).into(img);
        GlideUtils.showImageView(context, url, img);
        img.setOnClickListener(v -> {
            if (onBannerItemClickListener != null) {
                onBannerItemClickListener.onItemClick(P);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (jsonArray != null) {
            return jsonArray.size();
        }
        return 0;
    }

    class MzViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        MzViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }

}
