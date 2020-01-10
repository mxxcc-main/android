package com.qy.ccm.utils.blockchain.eth;

import ccm.web3j.protocol.Web3j;
import ccm.web3j.protocol.http.HttpService;


public class Web3JClient {
    /**
     * eth节点
     */
//    TODO eth    测试节点
//    private static final String ip = "https://mainnet.infura.io/v3/41980faa601547009c2630accddfc479";
//    private static final String ip = "https://ropsten.infura.io/v3/41980faa601547009c2630accddfc479";
//    private static final String ethip = "https://ropsten.infura.io/v3/41980faa601547009c2630accddfc479";
    private static final String ip = "http://47.74.242.199:7575";

    private volatile static Web3j web3j;

    public static Web3j getClient() {
        if (web3j == null) {
            synchronized (Web3JClient.class) {
                if (web3j == null) {
                    web3j = Web3j.build(new HttpService(ip));
                }
            }
        }

        return web3j;
    }
}
