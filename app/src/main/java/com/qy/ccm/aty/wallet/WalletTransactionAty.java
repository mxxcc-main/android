package com.qy.ccm.aty.wallet;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.qy.ccm.R;
import com.qy.ccm.adapter.other.TransListAdapter;
import com.qy.ccm.adapter.other.TransListETHBTCAdapter;
import com.qy.ccm.aty.base.BaseAty;
import com.qy.ccm.bean.other.entity.WalletToken;
import com.qy.ccm.bean.wallet.BtcRecordBean;
import com.qy.ccm.bean.wallet.EthRecordBean;
import com.qy.ccm.bean.wallet.RecordBean;
import com.qy.ccm.constants.UrlConstants;
import com.qy.ccm.retrofit.HttpRequestCallback;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.StatusBarUtil;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.utils.blockchain.btc.BtcUtils;
import com.qy.ccm.utils.blockchain.eth.CCMUtils;
import com.qy.ccm.utils.blockchain.eth.ETHUtils;
import com.qy.ccm.utils.function.StringUtils;
import com.qy.ccm.utils.function.TimeUtils;
import com.qy.ccm.view.group.NavigationLucencyLayout;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:交易页面
 * <p>
 * Data：2019/5/9-8:01 PM
 */
public class WalletTransactionAty extends BaseAty implements OnRefreshListener, OnLoadMoreListener, HttpRequestCallback<Object> {
    @BindView(R.id.nll_wallet_transaction)
    NavigationLucencyLayout nllWalletTrasaction;
    //    @BindView(R.id.tv_transaction_name)
//    TextView tvTransactionName;
    @BindView(R.id.tv_transaction_amount)
    TextView tvTransactionAmount;
    @BindView(R.id.tv_transaction_price)
    TextView tvTransactionPrice;
    @BindView(R.id.wallet_address_id)
    TextView walletAddressId;
    @BindView(R.id.wallet_address_copy)
    ImageView walletAddressCopyImg;

    @BindView(R.id.trans_list_id)
    LRecyclerView trans_list_id;

    private LRecyclerViewAdapter lRecyclerViewAdapter;

    private TransListAdapter transListAdapter;

    WalletToken walletToken;
    private String tokenName;

    private int pageNo = 1;

    private int pageSize = 20;

    private JSONArray jsonNArray;

    private String address;

    @OnClick({R.id.tv_transaction_transfer, R.id.tv_transaction_gathering, R.id.wallet_address_copy})
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.tv_transaction_transfer:
                bundle.putSerializable("walletToken", walletToken);
                myStartActivity(WalletTransferAty.class, bundle);
                break;
            case R.id.tv_transaction_gathering:
                bundle.putString("tokenAddress", walletToken.getTokenAddress());
                myStartActivity(WalletCodeAty.class, bundle);
                break;
            case R.id.wallet_address_copy:
                StringUtils.isCopy(walletAddressId.getText().toString());
                break;
            default:
                break;
        }
    }

    @Override
    public int initView() {
        return R.layout.aty_wallet_transaction;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillData() {

        StatusBarUtil.setStatusColor(this, true, false, R.color.colorPrimary);
        StatusBarUtil.setStatusBarColor(this, R.color.color_2E303B);
        walletToken = (WalletToken) getIntent().getSerializableExtra("walletToken");
        nllWalletTrasaction.setTitle(walletToken.getTokenName() + " Wallet");
        nllWalletTrasaction.setIvLift(R.mipmap.icon_return_white);
        initData();
    }

    private void initData() {
        tokenName = walletToken.getTokenName();
        String amount = walletToken.getAmount();
        String price = walletToken.getPrice();
        address = walletToken.getTokenAddress();
//        tvTransactionName.setText(tokenName);
        walletAddressId.setText(address);
        tvTransactionAmount.setText(amount + " " + tokenName);
        tvTransactionPrice.setText("≈￥ " + price);


        transListAdapter = new TransListAdapter(this, (jsonNArray), address, (view, pojo, position) -> {
            String tokenName = pojo.getString("tokenName");
            if (tokenName == null || "".equals(tokenName)) {
                tokenName = "CCM";
            }
            switch (tokenName.toUpperCase()) {

                case "ETH":
                    break;
                case "USDT":
                    break;
                case "CCM":
                default:
                    String tttype;
                    String hash;
                    Bundle bundle = new Bundle();
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

//            bundle.putString("tokenAmount", walletToken.getAmount());
                    bundle.putString("txhash", pojo.getString("hash"));
                    bundle.putString("fee", new BigDecimal(pojo.getString("gas")).multiply(new BigDecimal(pojo.getString("gasPrice"))).divide(new BigDecimal("1000000000000000000"), 18, RoundingMode.DOWN).toPlainString());
                    String time = (TimeUtils.long2String(pojo.getLongValue("timestamp") * 1000, TimeUtils.FORMAT_TYPE_3).toString());

                    if (time == null || "".equals(time.trim())) {
                        time = TimeUtils.getCurrentDateStr(TimeUtils.FORMAT_TYPE_5);
                    }

                    bundle.putString("time", time);
                    Utils.startActivity(view.getContext(), WalletTransferSuccessAty.class, bundle);
                    break;

            }


        });

        if (tokenName.equals("ETH")) {

            ethHttp();
        } else if (tokenName.equals("BTC")) {
            btcHttp();
        } else {
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            trans_list_id.setLayoutManager(manager);
            trans_list_id.setOnRefreshListener(this);
            lRecyclerViewAdapter = new LRecyclerViewAdapter(transListAdapter);
            trans_list_id.setAdapter(lRecyclerViewAdapter);

            requestData(0);
        }

    }

    private void btcHttp() {
        String url = UrlConstants.BTC_RECORD_API + address;
        getHttp(url, null);
    }

    private void ethHttp() {
        Map<String, String> map = new HashMap<>();
        map.put("module", "account");
        map.put("action", "txlist");
        map.put("address", address);
        map.put("startblock", "0");
        map.put("endblock", "99999999");
        map.put("sort", "desc");
        map.put("apikey", "YourApiKeyToken");
        getHttp(UrlConstants.ETH_RECORD_API, map);
    }


    @Override
    public void onLoadMore() {
        if (tokenName.equals("CCM")) {

            pageNo = pageNo + 1;
            requestData(0);
        } else {

            trans_list_id.refreshComplete(pageSize);
        }

    }

    @Override
    public void onHttpSuccess(String result, String url) {
        super.onHttpSuccess(result, url);
        dialogUtils.dismissProgressDialog();
        if (UrlConstants.ETH_RECORD_API.equals(url)) {
            EthRecordBean recordBean = new Gson().fromJson(result, EthRecordBean.class);
            if ("OK".equals(recordBean.getMessage())) {
                List<EthRecordBean.ResultBean> recordList = recordBean.getResult();
                Log.e("==========", "recordList:" + recordList.size());

                for (EthRecordBean.ResultBean resultBean : recordList) {
                    String value = ETHUtils.formatEth(new BigInteger(resultBean.getValue())).toString();
                    String time = resultBean.getTimeStamp();
                    String fromAddress = resultBean.getFrom();
                    String toAddress = resultBean.getTo();
//                    if (fromAddress.toLowerCase().equals(toAddress.toLowerCase())) {
//                        addData(value, time);
//                    } else
                    if (address.toLowerCase().equals(fromAddress.toLowerCase())) {
                        addData(value, time, fromAddress, "ETH");
                    }
//                    continue;
//                    addData(value, time);
                }
            }
        } else if (url.contains(UrlConstants.BTC_RECORD_API)) {
            BtcRecordBean btcRecordBean = new Gson().fromJson(result, BtcRecordBean.class);
            List<BtcRecordBean.TxsBean> txsList = btcRecordBean.getTxs();
//            initNum(txsList.size());
            for (BtcRecordBean.TxsBean txsBean : txsList) {
                String fromAddress = txsBean.getInputs().get(0).getPrev_out().getAddr();

                String time = txsBean.getTime();
                BigInteger valueBig = new BigInteger("0");
                for (int i = 0; i < txsBean.getOut().size(); i++) {
                    String middle = txsBean.getOut().get(i).getAddr();
                    if (middle == null) {
                        continue;
                    }
                    if (middle.toLowerCase().equals(address.toLowerCase())) {
                        valueBig = new BigInteger(txsBean.getOut().get(i).getValue());
                    }
                }
                String value = BtcUtils.btcFormat(valueBig).toString();
                //支出
                if (address.toLowerCase().equals(fromAddress.toLowerCase())) {
                    addData(value, time, fromAddress, "BTC");
                }
            }
        }
        initAdapter();
    }

    private void initAdapter() {
        TransListETHBTCAdapter transListETHBTCAdapter = new TransListETHBTCAdapter(this, (listStr), address, (view, pojo, position) -> {
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        trans_list_id.setLayoutManager(manager);
        trans_list_id.setOnRefreshListener(this);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(transListETHBTCAdapter);
        trans_list_id.setAdapter(lRecyclerViewAdapter);

    }

    @Override
    public void onHttpFault(String errorMsg, String url) {
        super.onHttpFault(errorMsg, url);

    }

    private void addData(String amount, String time, String fromAddress, String coinName) {
        RecordBean recordBean = new RecordBean();
        recordBean.setAmunt(amount);
        recordBean.setAddress(fromAddress);
        recordBean.setCoinName(coinName);
        String timeStr = TimeUtils.TimeStamp2Date(time, TimeUtils.FORMAT_TYPE_3);
        recordBean.setTime(timeStr);
        listStr.add(recordBean);
    }

    List<RecordBean> listStr = new ArrayList<>();

    @Override
    public void onRefresh() {
        if (tokenName.equals("CCM")) {


            requestData(0);
        } else {

            trans_list_id.refreshComplete(pageSize);
        }
    }

    private void requestData(int type) {
        TreeMap map;
        if (type == 0) {

            if ("CCM".equals(walletToken.getTokenName())) {

                map = new TreeMap();
                map.put("address", address);
                map.put("pageSize", pageSize);
                map.put("pageNo", pageNo);
                String req_url = "getCcmTransRecord";
                HttpUtils.getHttpUtils().executeGet(this, map, req_url, type, this);
            } else {

                map = new TreeMap();
                map.put("address", address);
                map.put("pageSize", pageSize);
                map.put("pageNo", pageNo);
                map.put("contractAddress", walletToken.getContractAddress());
                String req_url = "getTokenTransRecord";
                HttpUtils.getHttpUtils().executeGet(this, map, req_url, type, this);
            }
        }
    }

    /**
     * 接口请求成功
     */
    @Override
    public void onRequestSuccess(String result, int type) {

//        dialogUtils.dismissProgressDialog();
        try {

            if (type == 0) {
                dialogUtils.dismissProgressDialog();
                JSONObject jsonObject = JSONObject.parseObject(result);
                Boolean isSuccess = jsonObject.getBoolean("success");

                if (isSuccess) {

                    String stringObj = jsonObject.getString("info");
                    JSONObject jsonObject1 = JSONObject.parseObject(stringObj);
                    JSONArray jsonArray = jsonObject1.getJSONArray("obj");

                    //   循环数据拿取所有的分类

                    if (pageNo == 1) {
                        jsonNArray = jsonArray;
                        if (jsonNArray.size() == 0) {
                            trans_list_id.setVisibility(View.GONE);
                        } else {
                            trans_list_id.setVisibility(View.VISIBLE);
                        }
                    } else {
                        jsonNArray.addAll(jsonArray);
                    }
                    transListAdapter.setqOrgs(jsonNArray);
                    lRecyclerViewAdapter.notifyDataSetChanged();
                }
                trans_list_id.refreshComplete(pageSize);
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
