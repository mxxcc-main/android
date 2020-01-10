package com.qy.ccm.adapter.wallet;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.qy.ccm.R;
import com.qy.ccm.adapter.other.BannerAdapter;
import com.qy.ccm.app.MyApp;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.utils.function.safety.AES256;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.List;

/**
 * Created by Zhaoqi
 * <p>
 */
public class ExportPrikeyAdapter extends RecyclerView.Adapter<ExportPrikeyAdapter.MviewHolder> implements View.OnClickListener {
    private List<WalletBean> walletBeans;
    private Context context;
    private CallBack mCallBack;
    private BannerAdapter.ItemClickListener itemClickListener;

    public static final int SHARE_REQUEST_CODE = 1003;
    private Bitmap mBitmapAddress = null;
    private Bitmap mBitmapPriKey = null;

    private boolean isShowPri = false;

    private String tradePass = "";

    public ExportPrikeyAdapter(Context context, List<WalletBean> walletBeans, String tradePass, CallBack callBack) {
        this.walletBeans = walletBeans;
        this.context = context;
        this.tradePass = tradePass;
        mCallBack = callBack;
    }

    public void setqOrgs(List<WalletBean> walletBeans) {
        this.walletBeans = walletBeans;
    }

    @Override
    public void onClick(View v) {
//        mCallBack.click(v,);
    }

    public interface CallBack {
        void click(String text);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public MviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.export_pri_item, parent, false);
        MviewHolder mviewHolder = new MviewHolder(view);
        return mviewHolder;
    }

    @Override
    public void onBindViewHolder(MviewHolder holder, int position) {


        WalletBean pojo = walletBeans.get(position);

        mBitmapAddress = CodeUtils.createImage(pojo.getTokenAddress(), 400, 400, null);
        mBitmapPriKey = CodeUtils.createImage(AES256.SHA256Decrypt(pojo.getPrivateKey(), tradePass), 400, 400, null);

        holder.coin_name.setText(pojo.getCoin());
        holder.address_text.setText(pojo.getTokenAddress());
        holder.prikey_text.setText(AES256.SHA256Decrypt(pojo.getPrivateKey(), tradePass));
        holder.is_show_prikey.setOnClickListener(v -> {
//                是否显示私钥
            if (!isShowPri) {
                holder.prikey_text.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                holder.is_show_prikey.setImageResource(R.mipmap.icon_found_eyes);
                isShowPri = true;
            } else {
                holder.prikey_text.setTransformationMethod(PasswordTransformationMethod.getInstance());
                holder.is_show_prikey.setImageResource(R.mipmap.icon_close);
                isShowPri = false;
            }
        });
        holder.address_qcore.setImageBitmap(mBitmapAddress);
        holder.pri_key_img.setImageBitmap(mBitmapPriKey);
        holder.address_copy.setOnClickListener(v -> {
            mCallBack.click(holder.address_text.getText().toString());
        });
        holder.prikey_copy.setOnClickListener(v -> {
            mCallBack.click(holder.prikey_text.getText().toString());
        });

        if (pojo.getIconUrl().toUpperCase().contains("BTC")) {
            holder.coin_img.setImageResource(R.mipmap.btc);
        } else if (pojo.getIconUrl().toUpperCase().contains("ETH")) {
            holder.coin_img.setImageResource(R.mipmap.eth);
        } else {
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
            // 加载图片
            Glide.with(MyApp.getSingleInstance()).load(pojo.getIconUrl())
                    // 设置错误图片
                    .apply(requestOptions)
                    .into(holder.coin_img);

        }


    }

    @Override
    public int getItemCount() {
        if (walletBeans == null)
            return 0;
        else
            return walletBeans.size();
//            return 3;
    }

    public class MviewHolder extends RecyclerView.ViewHolder {

        public TextView coin_name, address_text, prikey_text;
        public ImageView coin_img, is_show_prikey, address_qcore, pri_key_img;
        public Button address_copy, prikey_copy;

        public MviewHolder(final View itemView) {
            super(itemView);

            this.coin_name = itemView.findViewById(R.id.coin_name);
            this.address_text = itemView.findViewById(R.id.address_text);
            this.prikey_text = itemView.findViewById(R.id.prikey_text);
            this.coin_img = itemView.findViewById(R.id.coin_img);
            this.is_show_prikey = itemView.findViewById(R.id.is_show_prikey);
            this.address_qcore = itemView.findViewById(R.id.address_qcore);
            this.pri_key_img = itemView.findViewById(R.id.pri_key_img);
            this.address_copy = itemView.findViewById(R.id.address_copy);
            this.prikey_copy = itemView.findViewById(R.id.prikey_copy);
        }

    }
}
