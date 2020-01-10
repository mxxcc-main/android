package com.qy.ccm.fragment;

import android.Manifest;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qy.ccm.R;
import com.qy.ccm.adapter.wallet.ApplicationBannerAdapter;
import com.qy.ccm.adapter.wallet.ApplicationWightListAdapter;
import com.qy.ccm.fragment.base.BaseFrag;
import com.qy.ccm.retrofit.HttpRequestCallback;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.FontUtil;
import com.qy.ccm.utils.UpdateVersionController;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.function.WidgetUtils;
import com.qy.ccm.utils.system.PermissionUtils;
import com.qy.ccm.view.group.NavigationLucencyLayout;
import com.qy.ccm.view.group.banner.BannerLayout;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;

public class ApplicationFrag extends BaseFrag implements HttpRequestCallback<Object> {

    @BindView(R.id.nll_main)
    NavigationLucencyLayout nllMain;

    @BindView(R.id.bl_include_main)
    BannerLayout blIncludeMain;

    @BindView(R.id.ll_id_root)
    LinearLayout ll_id_root;

    private JSONArray jsonArrayApplication;
    private ApplicationBannerAdapter managerBannerAdapter = null;

    private WidgetUtils dialogUtilsBannnerDia;
    private WidgetUtils dialogUtilsBannnerApplist;

    @Override
    public int initView() {
        return R.layout.frag_application;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillData() {
        dialogUtilsBannnerDia = new WidgetUtils(this.getContext());
        dialogUtilsBannnerApplist = new WidgetUtils(this.getContext());
        dialogUtilsBannnerDia.showProgressDialog();
        dialogUtilsBannnerApplist.showProgressDialog();
        requestData(1);
        requestData(2);
        nllMain.setBackArrowsVisibility(View.GONE);


    }

    private void initFirstBanner(JSONArray data) {
//        List<String> list = new ArrayList<>();
//        String prc = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1558266981202&di=aa33557d52ea3487c949a3682bec9588&imgtype=0&src=http%3A%2F%2Fpic61.nipic.com%2Ffile%2F20150302%2F2531170_165911490000_2.jpg";
//        list.add(prc);
//        list.add(prc);
//        list.add(prc);
//        list.add(prc);
//        list.add(prc);
//        list.add(prc);

        blIncludeMain.setAutoPlayDuration(3000);
        managerBannerAdapter = new ApplicationBannerAdapter(getActivity(), data);

        managerBannerAdapter.setOnBannerItemClickListener(position -> Utils.Toast("点击了"));
        blIncludeMain.setAdapter(managerBannerAdapter);
        if (!blIncludeMain.isPlaying()) {
            blIncludeMain.setAutoPlaying(true);
        }

    }

    private void requestData(int type) {
//        dialogUtils.showProgressDialog();
        TreeMap<String, Object> map;
//        轮播图
        if (type == 1) {
            map = new TreeMap<>();
            map.put("bannerType", 2);
            HttpUtils.getHttpUtils().executeGet(this.getActivity(), map, "getBannerByType", type, this);
        }
        if (type == 2) {
            HttpUtils.getHttpUtils().executeGet(this.getActivity(), "getApplicationList", type, this);
        }
    }

    /**
     * 接口请求成功
     */
    @Override
    public void onRequestSuccess(String result, int type) {

        try {
            if (!TextUtils.isEmpty(result)) {

                if (type == 1) {

                    dialogUtilsBannnerDia.dismissProgressDialog();
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    Boolean isSuccess = jsonObject.getBoolean("success");
                    if (isSuccess) {
                        initFirstBanner(jsonObject.getJSONArray("data"));
                    }
                }
                if (type == 2) {
                    dialogUtilsBannnerApplist.dismissProgressDialog();

                    JSONObject jsonObject = JSONObject.parseObject(result);
                    Boolean isSuccess = jsonObject.getBoolean("success");
                    if (isSuccess) {
                        jsonArrayApplication = jsonObject.getJSONArray("data");
//                        循环数据拿取所有的分类
                        Set<String> classs = new HashSet<>();
                        Map<String, Object> map = new HashMap<>();
                        for (Object object : jsonArrayApplication) {
                            classs.add(((JSONObject) object).getString("typeName"));
                        }

//                        这里想不到更好的办法， 只能对当前数据进行帅选,并且在最后一次循坏的时候，对视图进行刷新
//                        定义临时的数组
                        JSONArray jsonArray = null;
                        for (String classStr1 : classs) {
                            jsonArray = new JSONArray();
                            for (Object object1 : jsonArrayApplication) {
                                if (classStr1.equals(((JSONObject) object1).getString("typeName"))) {
                                    jsonArray.add(object1);
                                }
                            }
                            map.put(classStr1, jsonArray);
                        }

//                        循环类型，创建水平布局视图
                        for (String classStr : classs) {
                            //LinearLayout默认是水平(0)居中，现在改为垂直居中
                            ll_id_root.setOrientation(LinearLayout.VERTICAL);
                            //实例化一个LinearLayout
                            LinearLayout linearLayout = new LinearLayout(this.getContext());
                            //设置LinearLayout属性(宽和高)
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, FontUtil.dp2px(this.getContext(), 22));
                            //设置边距
                            layoutParams.setMargins(0, FontUtil.dp2px(this.getContext(), 5), 0, 0);
                            //设置背景色
                            linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                            //设置水平布局
                            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                            //将以上的属性赋给LinearLayout
                            linearLayout.setLayoutParams(layoutParams);
                            //实例化一个View
                            View ev = new View(this.getContext());
                            //设置宽高
                            LinearLayout.LayoutParams evParams = new LinearLayout.LayoutParams(FontUtil.dp2px(this.getContext(), 4), FontUtil.dp2px(this.getContext(), 14));
                            //设置view垂直居中
                            evParams.gravity = Gravity.CENTER_VERTICAL;
//                            设置左边距 10dp
                            evParams.setMargins(FontUtil.dp2px(this.getContext(), 10), 0, 0, 0);
                            ev.setLayoutParams(evParams);
                            Drawable drawable = getContext().getResources().getDrawable(R.drawable.shape_bg_color_2e303b_radius_4dp_1);
                            ev.setBackground(drawable);

//                            实例化TextView
                            TextView tv = new TextView(this.getContext());
//                            设置宽高
                            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            tvParams.setMargins(FontUtil.dp2px(this.getContext(), 5), 0, 0, 0);
                            tv.setLayoutParams(tvParams);
                            tv.setText(classStr);
                            tv.setTextColor(getResources().getColor(R.color.color_333333));
                            tv.setTextSize(16);

//                            添加列表视图
                            RecyclerView RecyclerView = new RecyclerView(this.getContext());
                            //设置宽高
                            LinearLayout.LayoutParams lrParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            RecyclerView.setLayoutParams(lrParams);

                            linearLayout.addView(ev);
                            linearLayout.addView(tv);
                            ll_id_root.addView(linearLayout);

                            ll_id_root.addView(RecyclerView);
                            ApplicationWightListAdapter applicationWightListAdapter = new ApplicationWightListAdapter(this.getContext(), ((JSONArray) map.get(classStr)), ((view, pojo, position) -> {
                                String appUrl = pojo.getString("androidHyperLink");
//                                appUrl = "http://a3gyxz.syzjxz2018.cn/kx1/rj_zjx1/jxtsc.apk";

                                if (PermissionUtils.permissionJudge(this.getContext(), PermissionUtils.WRITE_EXTERNAL_STORAGE)) {
                                    new UpdateVersionController(this.getContext()).downLoadApk(appUrl);

                                } else {
                                    rxPermissions
                                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                            .subscribe(granted -> {
                                                if (granted) {
                                                    new UpdateVersionController(this.getContext()).downLoadApk(appUrl);
                                                } else {
                                                    Utils.Toast("没有写内存的使用权限");
                                                }
                                            });
                                }
                            }));
//                            LRecyclerViewAdapter lRecyclerViewAdapter;
                            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                            manager.setOrientation(LinearLayoutManager.VERTICAL);
                            RecyclerView.setLayoutManager(manager);
//                            lRecyclerViewAdapter = new LRecyclerViewAdapter(applicationWightListAdapter);
                            RecyclerView.setAdapter(applicationWightListAdapter);
                            //                            lRecyclerViewAdapter.notifyDataSetChanged();
                            applicationWightListAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接口请求失败
     */
    @Override
    public void onRequestFail(String value, String failCode, int type) {
        switch (type) {
            case 1:
                dialogUtilsBannnerDia.dismissProgressDialog();
                break;
            case 2:
                dialogUtilsBannnerApplist.dismissProgressDialog();
                break;
            default:
                break;
        }

        if (!"null".equals(value)) {
            Utils.Toast(value);
        } else {
            Utils.Toast(failCode);
        }
    }

    @OnClick({R.id.zhongchu_app_id, R.id.shengtai_app})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zhongchu_app_id:
                Utils.Toast("此功能未开放");

//                Bundle bundle = new Bundle();
//                Utils.startActivity(getContext(), SHTransactionAty.class, bundle);
                break;
            case R.id.shengtai_app:
                Utils.Toast("此功能未开放");

//                Bundle bundle = new Bundle();
//                Utils.startActivity(getContext(), SHTransactionAty.class, bundle);
                break;
            default:
                break;
        }
    }


//    public static void main(String[] args) {
//
//        HttpUtils.initHttpRequest(HttpRetrofitRequest.getInstances());
//        new ApplicationFrag().requestData(2);
//    }


//    @Override
//    public void onJumpLogin() {
//        Bundle bundle = new Bundle();
//        Utils.startActivity(getContext(), LoginAndRegisterAty.class, bundle);
//        getActivity().finish();
//    }

}
