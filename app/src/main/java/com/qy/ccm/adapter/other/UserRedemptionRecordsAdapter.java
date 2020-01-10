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

import java.math.BigDecimal;

/**
 * Created by Zhaoqi
 * <p>
 */
public class UserRedemptionRecordsAdapter extends RecyclerView.Adapter<UserRedemptionRecordsAdapter.MviewHolder> implements View.OnClickListener {
    private JSONArray jsonNArray;
    private Context context;
    private CallBack mCallBack;
    private BannerAdapter.ItemClickListener itemClickListener;

    public UserRedemptionRecordsAdapter(Context context, JSONArray jsonNArray, CallBack callBack) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.finacial_rede_item, parent, false);
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

        holder.send_coin.setText(pojo.getString("coinTypeDesc"));
        holder.send_amount.setText(pojo.getString("ransomValue") + " " + pojo.getString("coinTypeDesc"));
        holder.time_date.setText(TimeUtils.long2String(Long.valueOf(pojo.getString("createTime")), TimeUtils.FORMAT_TYPE_3));
        holder.rate_id.setText("手续费：" + new BigDecimal(pojo.getString("ransomPoundage").trim()).toPlainString() + " " + pojo.getString("coinTypeDesc"));
        if (pojo.getString("ransomStatus") == null) {
            return;
        }
        if (pojo.getString("coinTypeDesc") == null) {
            return;
        }

        switch (pojo.getIntValue("ransomStatus")) {
            case 1:
            case 6:
            case 2:
                holder.send_status.setText("提现中");
                holder.send_status.setTextColor(Color.parseColor("#DEBC13"));
                break;
            case 3:
                holder.send_status.setText("提现失败");
                holder.send_status.setTextColor(Color.parseColor("#FF4900"));
                break;
            case 4:
                holder.send_status.setText("提现成功");
                holder.send_status.setTextColor(Color.parseColor("#00BE3E"));
                break;
            default:
                holder.send_status.setText("审核中");
                holder.send_status.setTextColor(Color.parseColor("#000000"));
                break;

        }

        if (pojo.getString("coinTypeDesc").equals("BTC")) {
            holder.coin_img.setImageResource(R.mipmap.btc);
        } else if (pojo.getString("coinTypeDesc").equals("USDT")) {
            holder.coin_img.setImageResource(R.mipmap.usdt);
        } else {
            holder.coin_img.setImageResource(R.mipmap.eth);
        }


//        RequestOptions requestOptions = new RequestOptions()
//                //正在请求图片的时候展示的图片
//                .placeholder(R.mipmap.ic_launcher)
//                //如果请求失败的时候展示的图片
//                .error(R.mipmap.ic_launcher)
//                //如果请求的url/model为 null 的时候展示的图片
//                .fallback(R.mipmap.ic_launcher)
//                // 缓存修改过的图片
//                .skipMemoryCache(false)
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

        public TextView send_coin, send_amount, time_date, send_status, rate_id;
        public ImageView coin_img;

        public MviewHolder(final View itemView) {
            super(itemView);

            this.send_coin = itemView.findViewById(R.id.send_coin);
            this.send_amount = itemView.findViewById(R.id.send_amount);
            this.time_date = itemView.findViewById(R.id.time_date);
            this.send_status = itemView.findViewById(R.id.send_status);
            this.coin_img = itemView.findViewById(R.id.coin_img);
            this.rate_id = itemView.findViewById(R.id.rate_id);
        }
    }
}
