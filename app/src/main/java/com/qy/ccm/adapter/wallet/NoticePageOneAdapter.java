package com.qy.ccm.adapter.wallet;

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
import com.qy.ccm.adapter.other.BannerAdapter;
import com.qy.ccm.utils.function.TimeUtils;

/**
 * Created by Zhaoqi
 * <p>
 */
public class NoticePageOneAdapter extends RecyclerView.Adapter<NoticePageOneAdapter.MviewHolder> implements View.OnClickListener {
    private JSONArray jsonArray;
    private Context context;
    private CallBack mCallBack;
    private String address;
    private String btcaddress;
    private String ethaddress;
    private BannerAdapter.ItemClickListener itemClickListener;

    public NoticePageOneAdapter(Context context, JSONArray jsonArray, String address, String btcaddress, String ethaddress, CallBack callBack) {
        this.jsonArray = jsonArray;
        this.context = context;
        this.address = address;
        this.btcaddress = btcaddress;
        this.ethaddress = ethaddress;
        this.mCallBack = callBack;
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
        if (this.jsonArray.size()==0){
            return;
        }
        System.out.println("-----this.jsonArray------" + this.jsonArray.toJSONString());
        JSONObject pojo = this.jsonArray.getJSONObject(position);
//        JSONObject pojo = jsonArray.getJSONObject(position);
        String str = "";
        String tokenName = pojo.getString("tokenName");
        String btcName = pojo.getString("type");

        if (btcaddress == null  || ethaddress == null){
            if (pojo.getString("from").toLowerCase().equals(address.toLowerCase())) {
//                if (tokenName == null || tokenName.equals("CCM")) {
                if (btcName == null) {
                    if (pojo.getIntValue("fromReadStatus") == 1 && pojo.getIntValue("fromReadStatus") == 1) {
                        holder.title_id.setTextColor(Color.parseColor("#999999"));
                        holder.content_id.setTextColor(Color.parseColor("#999999"));

//            holder.img_icon.setImageResource(R.mipmap.icon_gray);
                    }
                    str = "转账失败通知";
                    if ("0x1".equals(pojo.getString("status"))) {
                        str = "转账成功通知";
                    }
                    holder.title_id.setText(str); //交易对名称。

                }
            } else {
                if (pojo.getIntValue("toReadStatus") == 1 && pojo.getIntValue("toReadStatus") == 1) {
                    holder.title_id.setTextColor(Color.parseColor("#999999"));
                    holder.content_id.setTextColor(Color.parseColor("#999999"));

//            holder.img_icon.setImageResource(R.mipmap.icon_gray);
                }
                str = "收款失败通知";
                if ("0x1".equals(pojo.getString("status"))) {
                    str = "收款成功通知";
                }
                holder.title_id.setText(str); //交易对名称。
            }
        }else {
            if (pojo.getString("from").toLowerCase().equals(address.toLowerCase())) {
//                if (tokenName == null || tokenName.equals("CCM")) {
                if (btcName == null) {
                    if (pojo.getIntValue("fromReadStatus") == 1 && pojo.getIntValue("fromReadStatus") == 1) {
                        holder.title_id.setTextColor(Color.parseColor("#999999"));
                        holder.content_id.setTextColor(Color.parseColor("#999999"));

//            holder.img_icon.setImageResource(R.mipmap.icon_gray);
                    }
                    str = "转账失败通知";
                    if ("0x1".equals(pojo.getString("status"))) {
                        str = "转账成功通知";
                    }
                    holder.title_id.setText(str); //交易对名称。

                }
            } else {
                if (pojo.getIntValue("toReadStatus") == 1 && pojo.getIntValue("toReadStatus") == 1) {
                    holder.title_id.setTextColor(Color.parseColor("#999999"));
                    holder.content_id.setTextColor(Color.parseColor("#999999"));

//            holder.img_icon.setImageResource(R.mipmap.icon_gray);
                }
                str = "收款失败通知";
                if ("0x1".equals(pojo.getString("status"))) {
                    str = "收款成功通知";
                }
                holder.title_id.setText(str); //交易对名称。
            }
            if (pojo.getString("from").toLowerCase().equals(btcaddress.toLowerCase())) {
                if (btcName != null && btcName.equals("btc")) {
                    if (pojo.getIntValue("fromreadstatus") == 1 && pojo.getIntValue("fromreadstatus") == 1) {
                        holder.title_id.setTextColor(Color.parseColor("#999999"));
                        holder.content_id.setTextColor(Color.parseColor("#999999"));

//            holder.img_icon.setImageResource(R.mipmap.icon_gray);
                    }
                    str = "转账失败通知";
                    if ("0x1".equals(pojo.getString("status"))) {
                        str = "转账成功通知";
                    }
                    holder.title_id.setText(str); //交易对名称。

                }

            } else {
                if (pojo.getIntValue("toreadstatus") == 1 && pojo.getIntValue("toreadstatus") == 1) {
                    holder.title_id.setTextColor(Color.parseColor("#999999"));
                    holder.content_id.setTextColor(Color.parseColor("#999999"));

//            holder.img_icon.setImageResource(R.mipmap.icon_gray);
                }
                str = "收款失败通知";
                if ("1".equals(pojo.getString("status"))) {
                    str = "收款成功通知";
                }
                holder.title_id.setText(str); //交易对名称。
            }



            if (pojo.getString("from").toLowerCase().equals(ethaddress.toLowerCase())) {

                if (btcName != null && btcName.equals("eth")) {
                    if (pojo.getIntValue("fromreadstatus") == 1 && pojo.getIntValue("fromreadstatus") == 1) {
                        holder.title_id.setTextColor(Color.parseColor("#999999"));
                        holder.content_id.setTextColor(Color.parseColor("#999999"));

//            holder.img_icon.setImageResource(R.mipmap.icon_gray);
                    }
                    str = "转账失败通知";
                    if ("1".equals(pojo.getString("status"))) {
                        str = "转账成功通知";
                    }
                    holder.title_id.setText(str); //交易对名称。

                }
            } else {
                if (pojo.getIntValue("toreadstatus") == 1 && pojo.getIntValue("toreadstatus") == 1) {
                    holder.title_id.setTextColor(Color.parseColor("#999999"));
                    holder.content_id.setTextColor(Color.parseColor("#999999"));

//            holder.img_icon.setImageResource(R.mipmap.icon_gray);
                }
                str = "收款失败通知";
                if ("1".equals(pojo.getString("status"))) {
                    str = "收款成功通知";
                }
                holder.title_id.setText(str); //交易对名称。

            }
        }



 /*       if (pojo.getString("from").toLowerCase().equals(address.toLowerCase())) {
            if (pojo.getIntValue("fromReadStatus") == 1 && pojo.getIntValue("toreadstatus") == 1) {
                holder.title_id.setTextColor(Color.parseColor("#999999"));
                holder.content_id.setTextColor(Color.parseColor("#999999"));

//            holder.img_icon.setImageResource(R.mipmap.icon_gray);
            }
            str = "转账失败通知";
            if ("0x1".equals(pojo.getString("status"))) {
                str = "转账成功通知";
            }


        } else {
            if (pojo.getIntValue("toReadStatus") == 1 && pojo.getIntValue("toreadstatus") == 1) {
                holder.title_id.setTextColor(Color.parseColor("#999999"));
                holder.content_id.setTextColor(Color.parseColor("#999999"));

//            holder.img_icon.setImageResource(R.mipmap.icon_gray);
            }
            str = "收款失败通知";
            if ("0x1".equals(pojo.getString("status"))) {
                str = "收款成功通知";
            }


        }*/


        holder.date_id.setText(TimeUtils.long2String(Long.valueOf(pojo.getLong("timestamp") * 1000L), TimeUtils.FORMAT_TYPE_3));           //以主币为单位，目前已成交的部分的平均价格。
        holder.content_id.setText("交易hash:" + pojo.getString("hash").substring(0, 10) + "...,点击查看详情");

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


        public MviewHolder(final View itemView) {
            super(itemView);

            this.title_id = itemView.findViewById(R.id.title_id);
            this.date_id = itemView.findViewById(R.id.date_id);
            this.content_id = itemView.findViewById(R.id.content_id);
        }
    }
}
