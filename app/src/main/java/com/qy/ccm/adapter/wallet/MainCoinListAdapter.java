package com.qy.ccm.adapter.wallet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.qy.ccm.R;
import com.qy.ccm.adapter.other.BannerAdapter;
import com.qy.ccm.app.MyApp;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.utils.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by Zhaoqi
 * <p>
 */
public class MainCoinListAdapter extends RecyclerView.Adapter<MainCoinListAdapter.MviewHolder> implements View.OnClickListener {
    private List<WalletBean> walletBeans;
    private Context context;
    private CallBack mCallBack;
    private BannerAdapter.ItemClickListener itemClickListener;

    public MainCoinListAdapter(Context context, List<WalletBean> walletBeans, CallBack callBack) {
        this.walletBeans = walletBeans;
        this.context = context;
        mCallBack = callBack;
        res =  context.getResources();
    }

    public void setqOrgs(List<WalletBean> walletBeans) {
        this.walletBeans = walletBeans;
    }

    @Override
    public void onClick(View v) {
//        mCallBack.click(v,);
    }

    public interface CallBack {
        void click(View view, WalletBean pojo, int position);
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public MviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet_address_site, parent, false);
        MviewHolder mviewHolder = new MviewHolder(view);
        return mviewHolder;
    }
   private Resources res ;
    @Override
    public void onBindViewHolder(MviewHolder holder, int position) {

        WalletBean pojo = walletBeans.get(position);
        holder.v_id_name.setText(pojo.getCoin()); //交易对名称。
        holder.usd_id_num.setText(Utils.subZeroAndDot(new BigDecimal(pojo.getAmount()).setScale(8, RoundingMode.HALF_UP).toPlainString()));           //以主币为单位，目前已成交的部分的平均价格。
        holder.rmb_id_num.setText("≈￥ " + Utils.subZeroAndDot(new BigDecimal(pojo.getPrice()).setScale(2, RoundingMode.HALF_UP).toPlainString()));

        RequestOptions requestOptions = new RequestOptions()
                //正在请求图片的时候展示的图片
                .placeholder(R.mipmap.ccm)
                //如果请求失败的时候展示的图片
                .error(R.mipmap.ccm)
                //如果请求的url/model为 null 的时候展示的图片
                .fallback(R.mipmap.ccm)
                // 缓存修改过的图片
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        if(pojo.getCoin().equals("BTC")){
            holder.v_id_img.setImageDrawable(res.getDrawable(R.drawable.btc_icon));
        }else if(pojo.getCoin().equals("ETH")){
            holder.v_id_img.setImageDrawable(res.getDrawable(R.drawable.eth_icon));
        }else{
            // 加载图片
            Glide.with(MyApp.getSingleInstance()).load(pojo.getIconUrl())
                    // 设置错误图片
                    .apply(requestOptions)
                    .into(holder.v_id_img);
        }

        holder.itemView.setOnClickListener(v -> mCallBack.click(v, pojo, position));
    }

    @Override
    public int getItemCount() {
        if (walletBeans == null)
            return 0;
        else
            return walletBeans.size();
    }

    public class MviewHolder extends RecyclerView.ViewHolder {

        public TextView v_id_name, v_id_wallet_name, usd_id_num, rmb_id_num;
        public ImageView v_id_img;

        public MviewHolder(final View itemView) {
            super(itemView);

            this.v_id_name = itemView.findViewById(R.id.v_id_name);
            this.usd_id_num = itemView.findViewById(R.id.usd_id_num);
            this.rmb_id_num = itemView.findViewById(R.id.rmb_id_num);
            this.v_id_img = itemView.findViewById(R.id.v_id_img);
        }
    }
}
