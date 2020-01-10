package com.qy.ccm.adapter.wallet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.qy.ccm.R;
import com.qy.ccm.adapter.other.BannerAdapter;
import com.qy.ccm.app.MyApp;
import com.qy.ccm.retrofit.Constants;
import com.qy.ccm.utils.CornerTransform;

import static com.uuzuche.lib_zxing.DisplayUtil.dip2px;

/**
 * Created by Zhaoqi
 * <p>
 */
public class ApplicationWightListAdapter extends RecyclerView.Adapter<ApplicationWightListAdapter.MviewHolder> implements View.OnClickListener {
    private JSONArray jsonArray;
    private Context context;
    private CallBack mCallBack;
    private BannerAdapter.ItemClickListener itemClickListener;

    public ApplicationWightListAdapter(Context context, JSONArray jsonArray, CallBack callBack) {
        this.jsonArray = jsonArray;
        this.context = context;
        mCallBack = callBack;
    }

    public void setqOrgs(JSONArray jsonArray) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item_weight, parent, false);
        MviewHolder mviewHolder = new MviewHolder(view);
        return mviewHolder;
    }

    @Override
    public void onBindViewHolder(MviewHolder holder, int position) {
        JSONObject pojo = jsonArray.getJSONObject(position);
        holder.app_name_id.setText(pojo.getString("applicationName")); //交易对名称。
        holder.app_size.setText(pojo.getString("androidSize") + "M");                 //以主币为单位，已成交的总量。
        holder.app_name_desc_id.setText(pojo.getString("applicationDesc"));                 //以主币为单位，已成交的总量。

        RequestOptions requestOptions = new RequestOptions()
                //正在请求图片的时候展示的图片
                .placeholder(R.mipmap.ic_launcher)
                //如果请求失败的时候展示的图片
                .error(R.mipmap.ic_launcher)
                //如果请求的url/model为 null 的时候展示的图片
                .fallback(R.mipmap.ic_launcher)
                // 缓存修改过的图片
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        CornerTransform transformation = new CornerTransform(context, dip2px(context, 10));
//只是绘制左上角和右上角圆角
        transformation.setExceptCorner(false, false, false, false);


        // 加载图片
        Glide.with(MyApp.getSingleInstance()).load(Constants.BASE_URL_ONE + pojo.getString("iconUrl"))
                // 设置错误图片
                .apply(requestOptions)
                //                设置圆角

                .into(holder.app_icon);

        holder.app_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.click(v, pojo, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (jsonArray == null)
            return 0;
        else
            return jsonArray.size();
    }

    public class MviewHolder extends RecyclerView.ViewHolder {

        public TextView app_name_id, app_size, app_name_desc_id;
        public ImageView app_icon;
        public Button app_download;
        public LinearLayout app_name_level_id;

        public MviewHolder(final View itemView) {
            super(itemView);

            this.app_name_id = itemView.findViewById(R.id.app_name_id);
            this.app_size = itemView.findViewById(R.id.app_size);
            this.app_icon = itemView.findViewById(R.id.app_icon);
            this.app_download = itemView.findViewById(R.id.app_download);
            this.app_name_desc_id = itemView.findViewById(R.id.app_name_desc_id);
            this.app_name_level_id = itemView.findViewById(R.id.app_name_level_id);
        }
    }
}
