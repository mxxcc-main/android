package com.qy.ccm.aty.wallet;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.qy.ccm.R;
import com.qy.ccm.adapter.other.AdvertisingListAdapter;
import com.qy.ccm.aty.account.LoginAndRegisterAty;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.retrofit.HttpRequestCallback;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import java.util.TreeMap;

import butterknife.BindView;

/**
 * Description:公告
 * Data：2019-05-24-11:28
 */
public class WalletAdvertisingAty extends BaseAty implements OnRefreshListener, OnLoadMoreListener, HttpRequestCallback<Object> {
    @BindView(R.id.nll_register)
    NavigationLucencyLayout nllRegister;

    @BindView(R.id.lr_id)
    LRecyclerView lr_id;

    private int pageNo = 1;
    private int pageSize = 20;
    private JSONArray jsonArray;

    private AdvertisingListAdapter advertisingListAdapter;

    private LRecyclerViewAdapter lRecyclerViewAdapter;

    private int noticeId;

    @Override
    public int initView() {
        return R.layout.aty_wallet_advertising;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillData() {
        nllRegister.setTitle("公告");
        dialogUtils.showProgressDialog();
        requestData(0);
        advertisingListAdapter = new AdvertisingListAdapter(this, jsonArray, (view, pojo, position) -> {
//                Utils.Toast("点击了条目：" + position);

            ImageView img_icon = view.findViewById(R.id.img_icon);
            TextView advertising_title_id = view.findViewById(R.id.advertising_title_id);
            TextView advertising_time = view.findViewById(R.id.advertising_time);
            TextView adversing_content_id = view.findViewById(R.id.adversing_content_id);

            img_icon.setImageResource(R.mipmap.icon_gray);
            advertising_title_id.setTextColor(Color.parseColor("#c3c3c3"));
            advertising_time.setTextColor(Color.parseColor("#c3c3c3"));
            adversing_content_id.setTextColor(Color.parseColor("#c3c3c3"));
            noticeId = pojo.getIntValue("noticeId");
            requestData(1);
        });

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lr_id.setLayoutManager(manager);
        lr_id.setOnRefreshListener(this);
        lr_id.setOnLoadMoreListener(this);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(advertisingListAdapter);
        lr_id.setAdapter(lRecyclerViewAdapter);
    }

    private void requestData(int type) {

        TreeMap<String, Object> map;
        if (type == 0) {

            map = new TreeMap<>();
            map.put("pageNo", pageNo);
            map.put("pageSize", pageSize);
            HttpUtils.getHttpUtils().executeGet(this, map, "getNoticeListByPage", type, this);
        }
        if (type == 1) {
            map = new TreeMap<>();
            map.put("noticeId", noticeId);
            HttpUtils.getHttpUtils().executeGet(this, map, "insertNoticeHandle", type, this);
        }
    }

    /**
     * 接口请求成功
     */
    @Override
    public void onRequestSuccess(String result, int type) {

        try {
            if (!TextUtils.isEmpty(result)) {
                if (type == 0) {
                    dialogUtils.dismissProgressDialog();
                    JSONObject jsonObject1 = JSONObject.parseObject(result);
                    if (pageNo == 1) {
                        jsonArray = jsonObject1.getJSONArray("data");
                        if (jsonArray.size() == 0) {
                            lr_id.setVisibility(View.GONE);
                        } else {
                            lr_id.setVisibility(View.VISIBLE);
                        }
                    } else {
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("data");
                        jsonArray.addAll(jsonArray1);

                    }

                    advertisingListAdapter.setData(jsonArray);
                    advertisingListAdapter.notifyDataSetChanged();
                }
                if (type == 1) {
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    if (jsonObject.getBoolean("success")) ;
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
        dialogUtils.dismissProgressDialog();
        if (!"null".equals(value)) {
            Utils.Toast(value);
        } else {
            Utils.Toast(failCode);
        }
    }


    @Override
    public void onRefresh() {
        pageNo = 1;
        requestData(0);
        lr_id.refreshComplete(pageSize);
    }

    @Override
    public void onLoadMore() {

        pageNo = pageNo + 1;
        requestData(0);

        lr_id.refreshComplete(pageSize);
    }


    private void initAdvertisingData() {

        requestData(0);
    }

}
