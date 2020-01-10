package com.qy.ccm.utils;

import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.config.Config;
import com.qy.ccm.utils.database.DatabaseWalletUtils;
import com.qy.ccm.utils.http.NetUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CoinUsdtUtil {

    public final String address = "1CNKArGWupyLktzkWKYdnZAH3W5Z5g5giD";
    public static final int propertyid = 31;
    public static final String USDT_MAINNET_URL = "http://47.90.21.32:8888";
    public static final String RESULT = "result";
    public static final String METHOD_SEND_TO_ADDRESS = "omni_send";
    public static final String METHOD_GET_TRANSACTION = "omni_gettransaction";
    public static final String METHOD_NEW_ADDRESS = "getnewaddress";
    public static final String METHOD_GET_BALANCE = "omni_getbalance";
    public static final String METHOD_GET_LISTBLOCKTRANSACTIONS = "omni_listblocktransactions";
    public static final String USDT_SERVER_USERNAME = "omnicore";
    public static final String USDT_SERVER_PASSWORD = "f6vh13w6IUgeNhq1GG1I";
    public static final String USDT_WALLET_PASSWORD = "7rqlUgj4W5r6nbVrFm1v";


    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String creb = android.util.Base64.encodeToString((USDT_SERVER_USERNAME + ":" + USDT_SERVER_PASSWORD).getBytes(),
                    android.util.Base64.DEFAULT);

            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Basic " + creb);
            List<Object> objects = new ArrayList<>();
            List<WalletBean> usdtWalletList = DatabaseWalletUtils.getTokenNameList("USDT", Config.getMobleMapping());

            loo:
            for (WalletBean coinBean : usdtWalletList) {
                String address = coinBean.getTokenAddress();
                try {
//                有网络连接
                    if (NetUtil.isNetworkConnected()) {
                        objects.add(address);
                        objects.add(31);
                        JsonRpcHttpClient client = null;
                        try {
                            client = new JsonRpcHttpClient(new URL(CoinUsdtUtil.USDT_MAINNET_URL), headers);
                            Object result = null;

                            try {
                                result = client.invoke(METHOD_GET_BALANCE, objects, Object.class);
                                Log.e("iozhaq:::::::::::::::=", JSONObject.toJSONString(result));
                                JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(result));
                                String value = Utils.subZeroAndDot(jsonObject.getString("balance"));
                                WalletBean tokenBean = new WalletBean();
                                tokenBean.setAmount(value);
                                DatabaseWalletUtils.updateLocalData(tokenBean, address, "USDT", Config.getMobleMapping(), 3D);
                                break loo;
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void getUsdtBalance() {
        new Thread(networkTask).start();
    }
}
