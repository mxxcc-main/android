package com.qy.ccm;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.qy.ccm.retrofit.HttpRequestCallback;
import com.qy.ccm.retrofit.HttpRetrofitRequest;
import com.qy.ccm.retrofit.HttpUtils;
import com.qy.ccm.utils.Utils;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest implements HttpRequestCallback<Object> {

    private final String address = "1CNKArGWupyLktzkWKYdnZAH3W5Z5g5giD";
    private final int propertyid = 31;
    private final String USDT_MAINNET_URL = "http://47.90.21.32:8888";
    private final String RESULT = "result";
    private final String METHOD_SEND_TO_ADDRESS = "omni_send";
    private final String METHOD_GET_TRANSACTION = "omni_gettransaction";
    private final String METHOD_NEW_ADDRESS = "getnewaddress";
    private final String METHOD_GET_BALANCE = "omni_getbalance";
    private final String METHOD_GET_LISTBLOCKTRANSACTIONS = "omni_listblocktransactions";
    private final String USDT_SERVER_USERNAME = "omnicore";
    private final String USDT_SERVER_PASSWORD = "f6vh13w6IUgeNhq1GG1I";
    private final String USDT_WALLET_PASSWORD = "7rqlUgj4W5r6nbVrFm1v";

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        HttpUtils.initHttpRequest(HttpRetrofitRequest.getInstances());


        TreeMap<String, Object> map = new TreeMap<>();
        map.put("id", new StringBuilder(String.valueOf(System.currentTimeMillis())).toString());
        map.put("jsonrpc", "2.0");
        map.put("method", METHOD_GET_BALANCE);
        TreeMap m = new TreeMap();
        map.put("19yr9Hoyt6RmkuTGdZxFawuASruPHHLuBQ", 31);

        String creb = Base64.encodeBase64String((USDT_SERVER_USERNAME + ":" + USDT_SERVER_PASSWORD).getBytes()).toString();
        map.put("Authorization", "Basic " + creb);

        HttpUtils.getHttpUtils().executeGet(appContext, map, "USDT_MAINNET_URL", 1, null);

    }


    @Override
    public void onRequestSuccess(String result, int type) throws Exception {
        System.out.println(result);
    }

    @Override
    public void onRequestFail(String value, String failCode, int type) {

    }

}
