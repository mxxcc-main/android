package com.qy.ccm.adapter.wallet;

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
import com.qy.ccm.adapter.other.BannerAdapter;
import com.qy.ccm.app.MyApp;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.function.TimeUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by Zhaoqi
 * <p>
 */
public class NoticePageTwoAdapter extends RecyclerView.Adapter<NoticePageTwoAdapter.MviewHolder> implements View.OnClickListener {
    private JSONArray jsonArray;
    private Context context;
    private CallBack mCallBack;
    private BannerAdapter.ItemClickListener itemClickListener;

    public NoticePageTwoAdapter(Context context, JSONArray jsonArray, CallBack callBack) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_page_one_item, parent, false);
        MviewHolder mviewHolder = new MviewHolder(view);
        return mviewHolder;
    }

    @Override
    public void onBindViewHolder(MviewHolder holder, int position) {
        JSONObject pojo = jsonArray.getJSONObject(position);
//        holder.title_id.setText(""); //交易对名称。
//        holder.date_id.setText("");           //以主币为单位，目前已成交的部分的平均价格。
//        holder.content_id.setText("");
//        if(pojo.getIntValue("isRead") == 1) {
//            holder.title_id.setTextColor(Color.parseColor("#999999"));
//            holder.content_id.setTextColor(Color.parseColor("#999999"));
//
////            holder.img_icon.setImageResource(R.mipmap.icon_gray);
//        }
        holder.title_id.setText(pojo.getString("noticeTitle")); //公告名称。
        holder.date_id.setText(TimeUtils.long2String(Long.valueOf(pojo.getString("createTime")), TimeUtils.FORMAT_TYPE_3));                 //公告时间
        holder.content_id.setText(pojo.getString("noticeDesc"));           // 公告内容
        holder.ic_back.setVisibility(View.GONE);
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

        public TextView title_id, date_id, content_id;
        public ImageView ic_back;


        public MviewHolder(final View itemView) {
            super(itemView);

            this.title_id = itemView.findViewById(R.id.title_id);
            this.date_id = itemView.findViewById(R.id.date_id);
            this.content_id = itemView.findViewById(R.id.content_id);
            this.ic_back = itemView.findViewById(R.id.ic_back);
        }
    }
}
