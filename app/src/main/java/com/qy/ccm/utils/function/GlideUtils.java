package com.qy.ccm.utils.function;


import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.qy.ccm.R;
import com.qy.ccm.app.MyApp;


/**
 * 图片框架
 *
 * @author xq
 */
public class GlideUtils {


    /**
     * (1)
     * 显示图片Imageview带缓存
     *
     * @param context  上下文
     * @param url      图片链接
     * @param imgeview 组件
     */
    public static void showImageView(Context context, String url,
                                     ImageView imgeview) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                RequestOptions requestOptions = new RequestOptions()
                        //正在请求图片的时候展示的图片
                        .placeholder(R.mipmap.image18)
                        //如果请求失败的时候展示的图片
                        .error(R.mipmap.image18)
                        //如果请求的url/model为 null 的时候展示的图片
                        .fallback(R.mipmap.image18)
                        // 缓存修改过的图片
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                // 加载图片
                Glide.with(MyApp.getSingleInstance()).load(url)
                        // 设置错误图片
                        .apply(requestOptions)
                        .into(imgeview);
            }
        });
    }


    /**
     * (1)
     * 显示图片Imageview
     *
     * @param context  上下文
     * @param url      图片链接
     * @param imgeview 组件
     */
    public static void showImageViewNo(Context context, String url,
                                       ImageView imgeview) {

        RequestOptions requestOptions = new RequestOptions()
                //正在请求图片的时候展示的图片
                .placeholder(R.mipmap.about_us_logo)
                //如果请求失败的时候展示的图片
                .error(R.mipmap.about_us_logo)
                //如果请求的url/model为 null 的时候展示的图片
                .fallback(R.mipmap.about_us_logo)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .signature(new ObjectKey(System.currentTimeMillis()));
        // 加载图片
        Glide.with(MyApp.getSingleInstance()).load(url)
                // 设置错误图片
                .apply(requestOptions)
                .into(imgeview);


    }


}
