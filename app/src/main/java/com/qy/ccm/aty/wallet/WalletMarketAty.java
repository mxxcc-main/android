package com.qy.ccm.aty.wallet;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.qy.ccm.R;
import com.qy.ccm.adapter.other.MarketAdapter;
import com.qy.ccm.aty.account.LoginAndRegisterAty;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.config.Config;
import com.qy.ccm.retrofit.HttpRequestCallback;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import java.util.Iterator;
import java.util.TreeMap;

import butterknife.BindView;

/**
 * Description:交易页面
 * Data：2019/5/9-8:01 PM
 */
public class WalletMarketAty extends BaseAty implements OnRefreshListener, HttpRequestCallback<Object> {
    @BindView(R.id.nll_wallet_market)
    NavigationLucencyLayout nllWalletMarket;

    @BindView(R.id.lr_id)
    LRecyclerView lr_id;
    private static final int TIMER = 999;
    private static boolean flag = true;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private MarketAdapter marketAdapter;
    private JSONArray jsonArray;

    private Double saptRate;

    @Override
    public int initView() {
        return R.layout.aty_wallet_market;
    }

    @Override
    public void setListener() {
        setTimer();
    }

    @Override
    public void fillData() {

        nllWalletMarket.setTitle("行情");
        requestData(0);
    }

    @Override
    public void onRefresh() {

        marketHttp();
        lr_id.refreshComplete(10);
    }

    private void marketHttp() {
//        eth_usd
        String url = "https://dncapi.bqiapp.com/api/coin/hotcoin_search";
        getHttp(url, null);
    }

    @Override
    public void onHttpSuccess(String result, String url) {
        super.onHttpSuccess(result, url);
        jsonArray = JSONObject.parseObject(result).getJSONArray("data");
        marketAdapter.setqOrgs(processJSONArray(jsonArray));
        marketAdapter.notifyDataSetChanged();

    }

    @Override
    public void onHttpFault(String errorMsg, String url) {
        super.onHttpFault(errorMsg, url);
    }


    private void requestData(int type) {

        dialogUtils.showProgressDialog();
        TreeMap<String, Object> map;
        if (type == 0) {
            HttpUtils.getHttpUtils().executeGet(this, "getSysConfig", type, this);
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
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    jsonObject = jsonObject.getJSONArray("data").getJSONObject(0);
                    saptRate = Double.valueOf(jsonObject.getString("saptRate").trim());
//                    TODO
                    jsonArray = new JSONArray();

                    marketAdapter = new MarketAdapter(this, saptRate, jsonArray, (view, pojo, position) -> {
                    });
                    LinearLayoutManager manager = new LinearLayoutManager(this);
                    manager.setOrientation(LinearLayoutManager.VERTICAL);
                    lr_id.setLayoutManager(manager);
                    lr_id.setOnRefreshListener(this);
                    lRecyclerViewAdapter = new LRecyclerViewAdapter(marketAdapter);
                    lr_id.setAdapter(lRecyclerViewAdapter);

                    dialogUtils.dismissProgressDialog();

                    marketHttp();
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


    //更新本金的定时
    private void setTimer() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (flag) {
                    try {
                        Thread.sleep(5000); //休眠一秒
                        if ("".equals(Config.getToken())) {
                            return;
                        }

                        mHanler.sendEmptyMessage(TIMER);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private Handler mHanler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIMER:


                    //去执行定时操作逻辑
                    marketHttp();
                    break;
                default:
                    break;
            }
        }
    };

    private void stopTimer() {
        flag = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    public JSONArray processJSONArray(JSONArray jsonArray) {
        JSONArray jsonArray1 = new JSONArray();
        Iterator<Object> iterator = jsonArray.iterator();

        JSONObject jsonObject = null;
        while (iterator.hasNext()) {
            jsonObject = (JSONObject) iterator.next();
            if ("BTC".equals(jsonObject.getString("name"))) {
                jsonArray1.add(jsonObject);
                iterator.remove();
                break;
            }
        }
        while (iterator.hasNext()) {
            jsonObject = (JSONObject) iterator.next();
            if ("CCM".equals(jsonObject.getString("name"))) {
                jsonArray1.add(jsonObject);
                iterator.remove();
                break;
            }
        }
        while (iterator.hasNext()) {
            jsonObject = (JSONObject) iterator.next();
            if ("USDT".equals(jsonObject.getString("name"))) {
                jsonArray1.add(jsonObject);
                iterator.remove();
                break;
            }
        }
        jsonArray1.addAll(jsonArray);
        return jsonArray1;
    }
}
