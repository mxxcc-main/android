package com.qy.ccm.aty.wallet;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.qy.ccm.R;
import com.qy.ccm.adapter.wallet.NoticePageOneAdapter;
import com.qy.ccm.aty.IntroActivity;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.config.Config;
import com.qy.ccm.fragment.base.BaseFrag;
import com.qy.ccm.retrofit.HttpRequestCallback;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.blockchain.btc.BtcUtils;
import com.qy.ccm.utils.blockchain.eth.CCMUtils;
import com.qy.ccm.utils.database.DatabaseWalletUtils;
import com.qy.ccm.utils.function.TimeUtils;
import com.qy.ccm.utils.http.Http;
import com.qy.ccm.utils.http.OnSuccessAndFaultListener;
import com.qy.ccm.utils.http.OnSuccessAndFaultSub;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarException;

import butterknife.BindView;

/**
 * Description:钱包页
 * Data：2019/5/8-4:54 PM
 */
public class NoticeOneFrag extends BaseFrag implements OnRefreshListener, OnLoadMoreListener, HttpRequestCallback<Object> {

    @BindView(R.id.vp_wallet)
    LRecyclerView vpRecyclerView;

    private NoticePageOneAdapter noticePageOneAdapter;

    private JSONArray jsonArray;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    List<MultiItemEntity> tokenEntityList;
    //    private BigDecimal rmbAmount;
//    private BigDecimal ptbAmount;
    private String tradePwd;

    private String address;
    private String btcAddress;
    private String ethAddress;

    private String tttype;

    private String hash;

    private int pageNo = 1;


    @Override
    public int initView() {
        return R.layout.aty_notice_page_one;
    }

    @Override
    public void setListener() {
    }

    @Override
    public void fillData() {
        initWalletData();
        requestData(0);
        requestData(2);
        requestData(4);
    }

    public void initWalletData() {
        List<WalletBean> walletBeans = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");
        List<WalletBean> btcWalletBeans = DatabaseWalletUtils.getLocalCoinList(1);
        List<WalletBean> ethWalletBeans = DatabaseWalletUtils.getLocalCoinList(2);
        if (ethWalletBeans != null && !ethWalletBeans.isEmpty()) {
            ethAddress = ethWalletBeans.get(0).getTokenAddress();
        }
        if (btcWalletBeans != null && !btcWalletBeans.isEmpty()) {
            btcAddress = btcWalletBeans.get(0).getTokenAddress();
        }
        if (walletBeans != null && !walletBeans.isEmpty()) {
            address = walletBeans.get(0).getTokenAddress();
        }
        noticePageOneAdapter = new NoticePageOneAdapter(getActivity(), (jsonArray), address,btcAddress,ethAddress, (view, pojo, position) -> {

            Bundle bundle = new Bundle();
            String tokenName = pojo.getString("tokenName");
            if (tokenName == null){
                tokenName = "CCM";
            }
            String btcName = pojo.getString("type");
            String ethName = pojo.getString("type");
            TextView title_id = view.findViewById(R.id.title_id);
            TextView content_id = view.findViewById(R.id.content_id);
            title_id.setTextColor(Color.parseColor("#999999"));
            content_id.setTextColor(Color.parseColor("#999999"));
            if (tokenName != null && !"".equals(tokenName)) {
                if (tokenName.equals("CCM")) {
                    bundle.putString("tokenName", tokenName);
                    bundle.putString("sendAddress", pojo.getString("from"));
                    if (address.toLowerCase().equals(pojo.getString("from"))) {
                        tttype = "from";
                    } else {
                        tttype = "to";
                    }
                    hash = pojo.getString("hash");

                    if (new BigDecimal(pojo.getString("value")).compareTo(new BigDecimal("0")) > 0) {
                        bundle.putString("receiveAddress", pojo.getString("to"));
                        if (tttype.equals("from")) {
                            bundle.putString("amount", "-" + CCMUtils.formatEth(new BigInteger(pojo.getString("value"))).toPlainString());

                        } else {
                            bundle.putString("amount", "+" + CCMUtils.formatEth(new BigDecimal(pojo.getString("value"))).toPlainString());

                        }
                    } else {
                        bundle.putString("receiveAddress", pojo.getString("realAddress"));
                        if (tttype.equals("from")) {
                            bundle.putString("amount", "-" + pojo.getString("realValue"));
                        } else {
                            bundle.putString("amount", "+" + pojo.getString("realValue"));

                        }
                    }
                    bundle.putString("txhash", pojo.getString("hash"));
                    if (pojo.getString("gas") != null && !pojo.getString("gas").equals("")) {
                        bundle.putString("fee", new BigDecimal(pojo.getString("gas")).multiply(new BigDecimal(pojo.getString("gasPrice"))).divide(new BigDecimal("1000000000000000000"), 18, RoundingMode.DOWN).toPlainString());
                    }
                    String time = TimeUtils.getCurrentDateStr(TimeUtils.FORMAT_TYPE_5);
                    bundle.putString("time", time);
                    requestData(1);
                    Utils.startActivity(this.getActivity(), WalletTransferSuccessAty.class, bundle);

                }else {
                    bundle.putString("tokenName", tokenName);
                    bundle.putString("sendAddress", pojo.getString("from"));
                    if (address.toLowerCase().equals(pojo.getString("from"))) {
                        tttype = "from";
                    } else {
                        tttype = "to";
                    }
                    hash = pojo.getString("hash");
                    requestData(1);

                    if (new BigDecimal(pojo.getString("value")).compareTo(new BigDecimal("0")) > 0) {
                        bundle.putString("receiveAddress", pojo.getString("to"));
                        if (tttype.equals("from")) {
                            bundle.putString("amount", "-" + CCMUtils.formatEth(new BigInteger(pojo.getString("value"))).toPlainString());

                        } else {
                            bundle.putString("amount", "+" + CCMUtils.formatEth(new BigDecimal(pojo.getString("value"))).toPlainString());

                        }
                    } else {
                        bundle.putString("receiveAddress", pojo.getString("realAddress"));
                        if (tttype.equals("from")) {
                            bundle.putString("amount", "-" + pojo.getString("realValue"));
                        } else {
                            bundle.putString("amount", "+" + pojo.getString("realValue"));

                        }
                    }
                    bundle.putString("txhash", pojo.getString("hash"));
                    bundle.putString("fee", new BigDecimal(pojo.getString("gas")).multiply(new BigDecimal(pojo.getString("gasPrice"))).divide(new BigDecimal("1000000000000000000"), 18, RoundingMode.DOWN).toPlainString());
                    String time = TimeUtils.getCurrentDateStr(TimeUtils.FORMAT_TYPE_5);
                    bundle.putString("time", time);
                    Utils.startActivity(this.getActivity(), WalletTransferSuccessAty.class, bundle);
                }

            }
            if (btcName != null && !btcName.equals("") && "btc".equals(btcName)) {

                bundle.putString("tokenName", pojo.getString("type"));
                bundle.putString("sendAddress", pojo.getString("from"));
                hash = pojo.getString("hash");
                if (btcAddress.toLowerCase().equals(pojo.getString("from"))) {
                    tttype = "from";
                } else {
                    tttype = "to";
                }
                if (new BigDecimal(pojo.getString("value")).compareTo(new BigDecimal("0")) > 0) {
                    bundle.putString("receiveAddress", pojo.getString("to"));
                    if (tttype.equals("from")) {
                        bundle.putString("amount", "-" + CCMUtils.formatEth(new BigInteger(pojo.getString("value"))).toPlainString());

                    } else {
                        JSONArray jsonArray = JSONArray.parseArray(pojo.getString("froms"));
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject object = JSONObject.parseObject(jsonArray.get(i).toString());
                            if (object != null) {
                                bundle.putString("amount", "+" + object.getString("value"));
                            } else {
                                bundle.putString("amount", "");
                            }
                        }
                    }
                }
                bundle.putString("txhash", pojo.getString("hash"));
                bundle.putString("fee", new BigDecimal(pojo.getString("fee")).toPlainString());
                String time = TimeUtils.getCurrentDateStr(TimeUtils.FORMAT_TYPE_5);
                bundle.putString("time", time);
                if (btcAddress != null){
                    requestData(3);
                }
                Utils.startActivity(this.getActivity(), WalletTransferSuccessAty.class, bundle);
            }
            if (ethName != null && !ethName.equals("") && "eth".equals(ethName)) {
                bundle.putString("tokenName", pojo.getString("type"));
                bundle.putString("sendAddress", pojo.getString("from"));
                hash = pojo.getString("hash");
                if (btcAddress.toLowerCase().equals(pojo.getString("from"))) {
                    tttype = "from";
                } else {
                    tttype = "to";
                }
                if (new BigDecimal(pojo.getString("value")).compareTo(new BigDecimal("0")) > 0) {
                    bundle.putString("receiveAddress", pojo.getString("to"));
                    if (tttype.equals("from")) {
                        if (pojo.getIntValue("kind") == 1){//转出
                            bundle.putString("amount", "-" + pojo.getString("value"));
                        }else {
                            bundle.putString("amount", "+" + pojo.getString("value"));
                        }
                    } else {
                        if (pojo.getIntValue("kind") == 0){//转入
                            bundle.putString("amount", "+" + pojo.getString("value"));
                        }else {
                            bundle.putString("amount", "-" + pojo.getString("value"));
                        }
                    }
                }
                bundle.putString("txhash", pojo.getString("hash"));
                if (pojo.getString("gas") != null && !pojo.getString("gas").equals("")) {
                    bundle.putString("fee", new BigDecimal(pojo.getString("gas")).multiply(new BigDecimal(pojo.getString("gasprice"))).divide(new BigDecimal("1000000000000000000"), 18, RoundingMode.DOWN).toPlainString());
                }
                String time = TimeUtils.getCurrentDateStr(TimeUtils.FORMAT_TYPE_5);
                bundle.putString("time", time);
                if (ethAddress != null){
                    requestData(5);
                }
                Utils.startActivity(this.getActivity(), WalletTransferSuccessAty.class, bundle);
            }else {
                if (tokenName != null && !"".equals(tokenName)) {
                    bundle.putString("tokenName", tokenName);
                    bundle.putString("sendAddress", pojo.getString("from"));
                    if (address.toLowerCase().equals(pojo.getString("from"))) {
                        tttype = "from";
                    } else {
                        tttype = "to";
                    }
                    hash = pojo.getString("hash");
                    requestData(1);

                    if (new BigDecimal(pojo.getString("value")).compareTo(new BigDecimal("0")) > 0) {
                        bundle.putString("receiveAddress", pojo.getString("to"));
                        if (tttype.equals("from")) {
                            bundle.putString("amount", "-" + CCMUtils.formatEth(new BigInteger(pojo.getString("value"))).toPlainString());

                        } else {
                            bundle.putString("amount", "+" + CCMUtils.formatEth(new BigDecimal(pojo.getString("value"))).toPlainString());

                        }
                    } else {
                        bundle.putString("receiveAddress", pojo.getString("realAddress"));
                        if (tttype.equals("from")) {
                            bundle.putString("amount", "-" + pojo.getString("realValue"));
                        } else {
                            bundle.putString("amount", "+" + pojo.getString("realValue"));

                        }
                    }
                    bundle.putString("txhash", pojo.getString("hash"));
                    bundle.putString("fee", new BigDecimal(pojo.getString("gas")).multiply(new BigDecimal(pojo.getString("gasPrice"))).divide(new BigDecimal("1000000000000000000"), 18, RoundingMode.DOWN).toPlainString());
                    String time = TimeUtils.getCurrentDateStr(TimeUtils.FORMAT_TYPE_5);
                    bundle.putString("time", time);
                    Utils.startActivity(this.getActivity(), WalletTransferSuccessAty.class, bundle);


                }
            }

        });
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        vpRecyclerView.setLayoutManager(manager);

        lRecyclerViewAdapter = new LRecyclerViewAdapter(noticePageOneAdapter);
        vpRecyclerView.setAdapter(lRecyclerViewAdapter);
        vpRecyclerView.setOnRefreshListener(this);
        vpRecyclerView.setLoadMoreEnabled(true);
        vpRecyclerView.setOnLoadMoreListener(this);

    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        requestData(0);
        requestData(2);
        requestData(4);
        vpRecyclerView.refreshComplete(20);
    }

    private void requestData(int type) {

        TreeMap<String, Object> map ;

        if (type == 0) {
            map = new TreeMap<>();
            map.put("pageNo", pageNo);
            map.put("pageSize", "20");
            map.put("address", address);
            HttpUtils.getHttpUtils().executeGet(this.getActivity(), map, "getTokenNoticeTransRecord", type, this);

        }
        if (type == 1) {
            map = new TreeMap<>();
            map.put("type", tttype);
            map.put("hash", hash);
            HttpUtils.getHttpUtils().executeGet(this.getActivity(), map, "updateReadStatus", type, this);
        }
        if (type == 2) {
            List<WalletBean> btcWalletBeans = DatabaseWalletUtils.getLocalCoinList(1);
            if (btcWalletBeans != null && !btcWalletBeans.isEmpty()) {
                btcAddress = btcWalletBeans.get(0).getTokenAddress();
                map = new TreeMap<>();
                map.put("pageNo", pageNo);
                map.put("pageSize", "20");
                map.put("address", btcAddress);
                HttpUtils.getHttpUtils().executeGet(this.getActivity(), map, "getOthersTransRecord", type, this);

            }

        }
        if (type == 4) {
            List<WalletBean> ethWalletBeans = DatabaseWalletUtils.getLocalCoinList(2);
            if (ethWalletBeans != null && !ethWalletBeans.isEmpty()) {
                ethAddress = ethWalletBeans.get(0).getTokenAddress();
                map = new TreeMap<>();
                map.put("pageNo", pageNo);
                map.put("pageSize", "20");
                map.put("address", ethAddress);
                HttpUtils.getHttpUtils().executeGet(this.getActivity(), map, "getOthersTransRecord", type, this);

            }

        }
        if (type == 3) {
            if (btcAddress != null){
                TreeMap<String, Object> map4 = new TreeMap<>();
                map4.put("hash", hash);
                map4.put("address", btcAddress);
                HttpUtils.getHttpUtils().executeGet(this.getActivity(), map4, "updateStatusByHash", type, this);

            }

        }
        if (type == 5) {
            if (ethAddress!= null){
                TreeMap<String, Object> map5 = new TreeMap<>();
                map5.put("hash", hash);
                map5.put("address", ethAddress);
                HttpUtils.getHttpUtils().executeGet(this.getActivity(), map5, "updateStatusByHash", type, this);

            }

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
//                    rmbAmount = new BigDecimal(0);
                    JSONObject jsonO = null;

                    if (pageNo == 1) {
                        jsonArray = JSONObject.parseObject(result).getJSONObject("info").getJSONArray("obj");
                        if (jsonArray.size() == 0) {
                            vpRecyclerView.setVisibility(View.GONE);
                        } else {
                            vpRecyclerView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        JSONArray jsonArray1 = JSONObject.parseObject(result).getJSONObject("info").getJSONArray("obj");
                        jsonArray.addAll(jsonArray1);

                    }

                    noticePageOneAdapter.setData((jsonArray));
                    noticePageOneAdapter.notifyDataSetChanged();
                } else {
                    try {
                        vpRecyclerView.setVisibility(View.VISIBLE);
                        JSONObject jsonObject = JSONObject.parseObject(result);
                        if (jsonObject.getJSONArray("data") != null && jsonObject.getJSONArray("data").size()>0) {
                            vpRecyclerView.setVisibility(View.VISIBLE);
                            jsonArray.addAll(jsonObject.getJSONArray("data"));
                            noticePageOneAdapter.setData((jsonArray));
                            noticePageOneAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.fillInStackTrace();
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

        dialogUtils.dismissProgressDialog();
        if (!"null".equals(value)) {
            Utils.Toast(value);
        } else {
            Utils.Toast(failCode);

        }
    }

    @Override
    public void onLoadMore() {
        pageNo = pageNo + 1;
        requestData(0);
        requestData(2);
        requestData(4);
        vpRecyclerView.refreshComplete(20);
    }
}



