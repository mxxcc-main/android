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
import com.qy.ccm.R;
import com.qy.ccm.utils.function.TimeUtils;

/**
 * Created by Zhaoqi
 * <p>
 */
public class TangguoRecordAdapter extends RecyclerView.Adapter<TangguoRecordAdapter.MviewHolder> implements View.OnClickListener {
    private JSONArray jsonNArray;
    private Context context;
    private CallBack mCallBack;
    private BannerAdapter.ItemClickListener itemClickListener;

    public TangguoRecordAdapter(Context context, JSONArray jsonNArray, CallBack callBack) {
        this.jsonNArray = jsonNArray;
        this.context = context;
        mCallBack = callBack;
    }

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tangguo_item, parent, false);
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

        holder.desc_id.setText(pojo.getString("awardDesc"));
        holder.tangguo_value.setText(pojo.getString("awardValue") + " WINT");
        holder.tangguo_date.setText(TimeUtils.long2String(Long.valueOf(pojo.getString("createTime")), TimeUtils.FORMAT_TYPE_3));


//        RequestOptions requestOptions = new RequestOptions()
//                //正在请求图片的时候展示的图片
//                .placeholder(R.mipmap.ic_launcher)
//                //如果请求失败的时候展示的图片
//                .error(R.mipmap.ic_launcher)
//                //如果请求的url/model为 null 的时候展示的图片
//                .fallback(R.mipmap.ic_launcher)
//                // 缓存修改过的图片
//                .skipMemoryCache(flase)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//        // 加载图片
//        Glide.with(MyApp.getSingleInstance()).load(pojo.getIconUrl())
//                // 设置错误图片
//                .apply(requestOptions)
//                .into(holder.coin_img);


        holder.itemView.setOnClickListener(v -> mCallBack.click(v, pojo, position));
    }

    @Override
    public int getItemCount() {
        if (jsonNArray == null)
            return 0;
        else
            return jsonNArray.size();
    }

    public class MviewHolder extends RecyclerView.ViewHolder {

        public TextView desc_id, tangguo_date, tangguo_value;

        public MviewHolder(final View itemView) {
            super(itemView);

            this.desc_id = itemView.findViewById(R.id.desc_id);
            this.tangguo_date = itemView.findViewById(R.id.tangguo_date);
            this.tangguo_value = itemView.findViewById(R.id.tangguo_value);
        }
    }
}
