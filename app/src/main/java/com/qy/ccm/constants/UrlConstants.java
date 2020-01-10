package com.qy.ccm.constants;

/**
 * Description:
 * Data：2019/5/8-12:08 PM
 *
 * @author
 */
public class UrlConstants {
    public static String BASE_URL = "https://bitcoinfees.earn.com/";

    /**
     * Acquisition of Bitcoin
     */
    public static String BTC_ACQUISTION_FEE = "https://bitcoinfees.earn.com/api/v1/fees/recommended";

    /**
     * Ethereum GAS
     */
    public static String ETHGAS_API = "https://safe-relay.gnosis.pm/api/v1/gas-station/";

    /**
     * Send a deal
     */
    public static String USDT_SEND = "https://api.omniexplorer.info/v1/transaction/pushtx/";

    /**
     * 获取usdt交易记录
     */
    public static String USDT_DETAILS = "https://api.omniexplorer.info/v1/address/addr/details/";

    /**
     * 获取usdt余额
     */
    public static String USDT_BALANCE = "https://api.omniwallet.org/v2/address/addr/";

    /**
     * Get Bitcoin balance
     */

    public static String BTC_BASE_API = "https://blockchain.info/";
//    public static String BTC_BASE_API = "https://testnet.blockchain.info/";
    public static String BTC_BALANCE_API = BTC_BASE_API + "balance";

    public static String BTC_RECORD_API = BTC_BASE_API + "rawaddr/";

        public static String ETH_RECORD_API = "https://api.etherscan.io/api";
//    public static String ETH_RECORD_API = "https://api-ropsten.etherscan.io/api";
//    api-ropsten.etherscan.io
//
//    api-kovan.etherscan.io
//
//    api-rinkeby.etherscan.io

}
