package com.qy.ccm.adapter.other;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qy.ccm.R;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.utils.function.TimeUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Zhaoqi
 * <p>
 */
public class AdvertisingListAdapter extends RecyclerView.Adapter<AdvertisingListAdapter.MviewHolder> implements View.OnClickListener {
    private JSONArray jsonArray;
    private Context context;
    private CallBack mCallBack;
    private BannerAdapter.ItemClickListener itemClickListener;

    public AdvertisingListAdapter(Context context, JSONArray jsonArray, CallBack callBack) {
        this.jsonArray = jsonArray;
        this.context = context;
        mCallBack = callBack;
    }

    public void setData(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advertising_item, parent, false);
        MviewHolder mviewHolder = new MviewHolder(view);
        return mviewHolder;
    }

    @Override
    public void onBindViewHolder(MviewHolder holder, int position) {
        JSONObject pojo = jsonArray.getJSONObject(position);

        if(pojo.getIntValue("isRead") == 1) {
            holder.advertising_title_id.setTextColor(Color.parseColor("#c3c3c3"));
            holder.advertising_time.setTextColor(Color.parseColor("#c3c3c3"));
            holder.adversing_content_id.setTextColor(Color.parseColor("#c3c3c3"));

            holder.img_icon.setImageResource(R.mipmap.icon_gray);
        }
        holder.advertising_title_id.setText(pojo.getString("noticeTitle")); //公告名称。
        holder.advertising_time.setText(TimeUtils.long2String(Long.valueOf(pojo.getString("createTime")), TimeUtils.FORMAT_TYPE_3));                 //公告时间
        holder.adversing_content_id.setText(pojo.getString("noticeDesc"));           // 公告内容

        holder.itemView.setOnClickListener(v -> mCallBack.click(v, pojo, position));
    }

    @Override
    public int getItemCount() {
        if (jsonArray == null)
            return 0;
        else
            return jsonArray.size();
    }

    public class MviewHolder extends RecyclerView.ViewHolder {

        public TextView advertising_title_id, advertising_time, adversing_content_id;
        public ImageView img_icon;
        public MviewHolder(final View itemView) {
            super(itemView);

            this.advertising_title_id = itemView.findViewById(R.id.advertising_title_id);
            this.advertising_time = itemView.findViewById(R.id.advertising_time);
            this.adversing_content_id = itemView.findViewById(R.id.adversing_content_id);
            this.img_icon = itemView.findViewById(R.id.img_icon);
        }
    }
}
