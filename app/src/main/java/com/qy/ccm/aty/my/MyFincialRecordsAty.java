package com.qy.ccm.aty.my;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.qy.ccm.R;
import com.qy.ccm.adapter.other.FinacialRecordAdapter;
import com.qy.ccm.aty.account.LoginAndRegisterAty;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.retrofit.HttpRequestCallback;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import java.util.TreeMap;

import butterknife.BindView;

/**
 * Description:交易页面
 * Data：2019/5/9-8:01 PM
 */
public class MyFincialRecordsAty extends BaseAty implements HttpRequestCallback<Object>, OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.nll_wallet_market)
    NavigationLucencyLayout nllWalletMarket;

    @BindView(R.id.lr_id)
    LRecyclerView lr_id;

    private JSONArray jsonArrayFinalRecord;

    private LRecyclerViewAdapter lRecyclerViewAdapter;

    private FinacialRecordAdapter finacialRecordAdapter;
    private int pageNo = 1;
    private int pageSize = 20;


    @Override
    public int initView() {
        return R.layout.aty_finacial_record;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillData() {

//        StatusBarUtil.setStatusColor(this, true, false, R.color.color_2E303B);
        nllWalletMarket.setTitle("搬砖记录");

        dialogUtils.showProgressDialog();
        requestData(0);
        finacialRecordAdapter = new FinacialRecordAdapter(this, jsonArrayFinalRecord, (view, pojo, position) -> {
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lr_id.setLayoutManager(manager);
        lr_id.setOnRefreshListener(this);
        lr_id.setOnLoadMoreListener(this);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(finacialRecordAdapter);
        lr_id.setAdapter(lRecyclerViewAdapter);

    }


    @Override
    public void onLoadMore() {
        this.pageNo = pageNo + 1;
        requestData(0);

        lr_id.refreshComplete(pageSize);
    }

    @Override
    public void onRefresh() {
        this.pageNo = 1;
        requestData(0);
        lr_id.refreshComplete(pageSize);
    }


    private void requestData(int type) {

        TreeMap<String, Object> map;
        if (type == 0) {
            map = new TreeMap<>();
            map.put("pageNo", pageNo);
            map.put("pageSize", pageSize);
            HttpUtils.getHttpUtils().executeGet(this, map, "getManageRecordList", type, this);
        }
    }

    /**
     * 接口请求成功
     */
    @Override
    public void onRequestSuccess(String result, int type) {

        dialogUtils.dismissProgressDialog();
        try {
            if (!TextUtils.isEmpty(result)) {
                if (type == 0) {
                    JSONObject jsonO = null;
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    if (pageNo == 1) {
                        jsonArrayFinalRecord = jsonObject.getJSONArray("data");
                        if (jsonArrayFinalRecord.size() == 0) {
                            lr_id.setVisibility(View.GONE);
                        } else {
                            lr_id.setVisibility(View.VISIBLE);
                        }

                    } else {
                        JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                        jsonArrayFinalRecord.addAll(jsonArray1);
                    }


                    finacialRecordAdapter.setqOrgs(jsonArrayFinalRecord);
                    finacialRecordAdapter.notifyDataSetChanged();
                    lr_id.refreshComplete(pageSize);
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


}
