package com.qy.ccm.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qy.ccm.R;
import com.qy.ccm.adapter.wallet.ApplicationBannerAdapter;
import com.qy.ccm.fragment.base.BaseFrag;
import com.qy.ccm.fragment.find.WebViewAty;
import com.qy.ccm.retrofit.HttpRequestCallback;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.function.WidgetUtils;
import com.qy.ccm.view.group.banner.BannerLayout;

import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:
 * Data：2019/5/8-4:54 PM
 *
 * @author
 */
public class FindFrag extends BaseFrag implements HttpRequestCallback<Object> {


    @BindView(R.id.bl_include_main)
    BannerLayout blIncludeMain;

    @BindView(R.id.etherscan_id)
    LinearLayout etherscan_id;

    @BindView(R.id.search_img)
    ImageView search_img;

    @BindView(R.id.search_text)
    EditText search_text;

    String baiduSeUrl = "https://m.baidu.com/s?wd=";

    @OnClick({R.id.etherscan_id, R.id.search_img, R.id.sar_id, R.id.btc_id, R.id.ccm_id, R.id.ht_id, R.id.lsk_id, R.id.okb_id})
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {

            case R.id.etherscan_id:
                bundle.putString("detailUrl", "https://cn.etherscan.com/");
                Utils.startActivity(view.getContext(), WebViewAty.class, bundle);
                break;

            case R.id.btc_id:
                bundle.putString("detailUrl", "https://www.blockchain.com/zh-cn/explorer");
                Utils.startActivity(view.getContext(), WebViewAty.class, bundle);
                break;

            case R.id.ccm_id:
                bundle.putString("detailUrl", "http://ccmisr.cc/#/index");
                Utils.startActivity(view.getContext(), WebViewAty.class, bundle);
                break;

            case R.id.ht_id:
                bundle.putString("detailUrl", "https://m.8btc.com/");
                Utils.startActivity(view.getContext(), WebViewAty.class, bundle);
                break;

            case R.id.okb_id:
                bundle.putString("detailUrl", "https://m.jinse.com/");
                Utils.startActivity(view.getContext(), WebViewAty.class, bundle);
                break;

            case R.id.lsk_id:
                bundle.putString("detailUrl", "https://lisk.io/");
                Utils.startActivity(view.getContext(), WebViewAty.class, bundle);
                break;

            case R.id.sar_id:
                bundle.putString("detailUrl", "https://www.ccm.one/dist/#/index");
                Utils.startActivity(view.getContext(), WebViewAty.class, bundle);
                break;

            case R.id.search_img:
                String detailUrl;
                if ("".equals(search_text.getText().toString())) {

                    detailUrl = "https://m.baidu.com";
                } else if (search_text.getText().toString().startsWith("http")) {
                    detailUrl = search_text.getText().toString();
                } else {
                    detailUrl = baiduSeUrl + search_text.getText().toString();
                }

                bundle.putString("detailUrl", detailUrl);
                Utils.startActivity(view.getContext(), WebViewAty.class, bundle);
                break;

            default:
                break;

        }
    }

    @Override
    public int initView() {
        return R.layout.frag_find;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillData() {

        StatusBarUtil.setStatusColor(this.getActivity(), false, true, R.color.colorPrimary);
        StatusBarUtil.setStatusBarColor(this.getActivity(), R.color.colorPrimary);
        dialogUtilsBannnerDia = new WidgetUtils(this.getContext());

        requestData(1);
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

    }

    @Override
    public void onRequestSuccess(String result, int type) throws Exception {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WidgetUtils dialogUtilsBannnerDia;

    @Override
    public void onRequestFail(String value, String failCode, int type) {
        switch (type) {
            case 1:
                dialogUtilsBannnerDia.dismissProgressDialog();
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

    private ApplicationBannerAdapter managerBannerAdapter = null;

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

//        managerBannerAdapter.setOnBannerItemClickListener(position -> Utils.Toast("点击了"));
        blIncludeMain.setAdapter(managerBannerAdapter);
        if (!blIncludeMain.isPlaying()) {
            blIncludeMain.setAutoPlaying(true);
        }

    }

}
