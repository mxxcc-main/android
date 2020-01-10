package com.qy.ccm.aty.my;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.qy.ccm.R;
import com.qy.ccm.adapter.other.UserEarnRecordsAdapter;
import com.qy.ccm.aty.account.LoginAndRegisterAty;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.retrofit.HttpRequestCallback;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.TreeMap;

import butterknife.BindView;

/**
 * Description:交易页面
 * Data：2019/5/9-8:01 PM
 */
public class UserEarnRecordsAty extends BaseAty implements OnLoadMoreListener, OnRefreshListener, HttpRequestCallback<Object> {
    @BindView(R.id.nll_wallet_market)
    NavigationLucencyLayout nllWalletMarket;
    @BindView(R.id.lv_id)
    LRecyclerView lRecyclerView;
    @BindView(R.id.all_price)
    TextView all_price;


    private JSONArray jsonNArray;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private UserEarnRecordsAdapter userEarnRecordsAdapter;
    private int pageNo = 1;
    private int pageSize = 20;


    @Override
    public int initView() {
        return R.layout.user_earn_records;
    }

    @Override
    public void setListener() {
    }

    @Override
    public void fillData() {

//        StatusBarUtil.setStatusColor(this, true, false, R.color.color_2E303B);
        nllWalletMarket.setTitle("搬砖成果");
//        requestData(0);

        dialogUtils.showProgressDialog();
        userEarnRecordsAdapter = new UserEarnRecordsAdapter(this, jsonNArray, (v, pojo, position) -> {

            //                            TODO 点击事件暂时无

        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lRecyclerView.setLayoutManager(manager);

        lRecyclerView.setOnRefreshListener(this);
        lRecyclerView.setOnLoadMoreListener(this);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(userEarnRecordsAdapter);
        lRecyclerView.setAdapter(lRecyclerViewAdapter);
        requestData(0);
        requestData(1);

    }

    private void requestData(int type) {
//        dialogUtils.showProgressDialog();
        TreeMap<String, Object> map;
        if (type == 0) {
            map = new TreeMap<>();
            map.put("pageNo", pageNo);
            map.put("pageSize", pageSize);
            HttpUtils.getHttpUtils().executeGet(this, map, "getManageIncordList", type, this);
        }
        if (type == 1) {
//            dialogUtils.showProgressDialog();
            HttpUtils.getHttpUtils().executeGet(this, "getAllIncome", type, this);
        }
    }

    /**
     * 接口请求成功
     */
    @Override
    public void onRequestSuccess(String result, int type) {

//        dialogUtils.dismissProgressDialog();
        try {
            if (!TextUtils.isEmpty(result)) {

                if (type == 0) {
                    dialogUtils.dismissProgressDialog();
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    Boolean isSuccess = jsonObject.getBoolean("success");

                    if (isSuccess) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
//                        if (jsonObject.getString("allIncome") != null && "".equals(jsonObject.getString("allIncome"))) {
//                            all_price.setText(jsonObject.getString("allIncome"));
//                        }
//                        循环数据拿取所有的分类

                        if (pageNo == 1) {
                            jsonNArray = jsonArray;
                            if (jsonNArray.size() == 0) {
                                lRecyclerView.setVisibility(View.GONE);
                            } else {
                                lRecyclerView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            jsonNArray.addAll(jsonArray);
                        }
                        userEarnRecordsAdapter.setqOrgs(jsonNArray);
                        lRecyclerViewAdapter.notifyDataSetChanged();
                    }
                    lRecyclerView.refreshComplete(pageSize);
                }
                if (type == 1) {
//                    dialogUtils.dismissProgressDialog();
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    Boolean isSuccess = jsonObject.getBoolean("success");

                    if (isSuccess) {

                        jsonObject = jsonObject.getJSONObject("data");
                        if (jsonObject.getString("allIncome") != null && !"".equals(jsonObject.getString("allIncome"))) {
                            all_price.setText(Utils.subZeroAndDot(new BigDecimal(jsonObject.getString("allIncome")).setScale(2, RoundingMode.DOWN).toPlainString()));
                        }
                    }
                    lRecyclerView.refreshComplete(pageSize);
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
        requestData(1);
        lRecyclerView.refreshComplete(pageSize);
    }

    @Override
    public void onLoadMore() {
        pageNo = pageNo + 1;
        requestData(0);
        lRecyclerView.refreshComplete(pageSize);
    }

}
