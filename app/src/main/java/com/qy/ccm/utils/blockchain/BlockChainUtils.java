package com.qy.ccm.utils.blockchain;

import android.util.Log;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.config.Config;
import com.qy.ccm.other.thread.GetCurrencyBalance;
import com.qy.ccm.other.thread.GetETHCurrencyBalance;
import com.qy.ccm.utils.blockchain.btc.BtcUtils;
import com.qy.ccm.utils.blockchain.eth.CCMUtils;
import com.qy.ccm.utils.blockchain.eth.ETHUtils;
import com.qy.ccm.utils.database.DatabaseWalletUtils;
import com.qy.ccm.utils.function.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import ccm.web3j.crypto.CipherException;

/**
 * Description:
 * Data：2019/5/8-10:49 AM
 *
 * @author
 */
public class BlockChainUtils {
    public final static String QUERY_WALLET = "QUERY_WALLET";


    public static void importTheWallet_btc_eth(String mnemonic, String password, String coin, String mobileMapping, String walletName, int tag) {
        if (coin.equals("BTC")) {
            new BtcUtils().createBtc(mnemonic, password, coin, mobileMapping, walletName);
        } else if (coin.equals("ETH")) {
            try {
                new ETHUtils().createEth(mnemonic, password, coin, mobileMapping, walletName);
            } catch (Exception e) {

            }
        } else if (coin.equals("CCM")) {
            try {
                new CCMUtils().createWallet(mnemonic, password, coin, mobileMapping, walletName);
            } catch (Exception e) {

            }
        }
    }

    /**
     * Import the wallet
     */
    public static void importTheWallet(String mnemonic, String password, String mobileMapping, int tag) {
        try {
            new CCMUtils().createWallet(mnemonic, password, "", mobileMapping, "");
        } catch (CipherException e) {
            e.printStackTrace();
        }
    }

    /**
     * Import the wallet
     */
    public static String importTheWallet(String mnemonic, String password, String coin, String walletName) {
        String address = null;
        switch (coin) {
            case "CCM":
                try {
                    address = new CCMUtils().createWallet(mnemonic, password, "", Config.getMobleMapping(), walletName);
                } catch (CipherException e) {
                    e.printStackTrace();
                }
                break;
            case "BTC":
            case "USDT":
                address = new BtcUtils().createBtc(mnemonic, password, coin, Config.getMobleMapping(), walletName);
                break;
            case "ETH":
                try {
                    address = new ETHUtils().createEth(mnemonic, password, "", Config.getMobleMapping(), walletName);
                } catch (org.web3j.crypto.CipherException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        return address;

    }

    public static String imputPricate(String privateKey, String password, String coin, String walletName) {
        String address = null;
        switch (coin) {
            case "CCM":
                try {
                    address = new CCMUtils().importPrivate(privateKey, password, Config.getMobleMapping(), walletName);
                } catch (CipherException e) {
                    e.printStackTrace();
                }

                break;
            case "BTC":
            case "USDT":
                address = new BtcUtils().inputPrivite(privateKey, password, coin, Config.getMobleMapping(), walletName);
                break;
            case "ETH":
                try {
                    address = new ETHUtils().importPrivate(privateKey, password, Config.getMobleMapping(), walletName);
                } catch (org.web3j.crypto.CipherException e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }
        return address;
    }


    /**
     * 查询ETH,ERC20代币余额
     */
    public static void getBlockchain() {
        CCMUtils CCMUtils = new CCMUtils();
        List<WalletBean> localWalletList = DatabaseWalletUtils.getLocalCoinList_CCM("CCM");
        if (localWalletList == null || localWalletList.isEmpty()) {
            return;
        }
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("wallet-ethereum-%d").build();
        GetCurrencyBalance threadPoolExecutor = new GetCurrencyBalance(localWalletList.size(), 100, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100), namedThreadFactory);
        for (WalletBean walletBean : localWalletList) {
            String coinName = walletBean.getTokenName();
            String coinAddress = walletBean.getTokenAddress();
            String contractAddress = walletBean.getContractAddress();

            switch (coinName) {
//                case "CCM":
//                    threadPoolExecutor.execute(new QueryBalance(CCMUtils, coinName, coinAddress, contractAddress));
//                    break;
                case "OMG":
                case "BNB":
                case "MKR":
                case "USDC":
                case "CRO":
                case "TUSD":
                case "ZRX":
                case "ZIL":
                case "BAT":
                case "LINK":
                case "ICX":
                case "PAX":
                case "R":
                case "REP":
                case "AE":
                case "GUSD":
                case "BCZERO":
                case "PPT":
                case "BTM":
                case "BTC":
                    break;
                case "USDT":
                    break;
                default:
                    threadPoolExecutor.execute(new QueryBalance(CCMUtils, coinName, coinAddress, contractAddress));
                    break;
            }

        }

    }


    /**
     * 查询ETH,ERC20代币余额
     */
    public static void getETHBlockchain() {
        ETHUtils ETHUtils = new ETHUtils();
        List<WalletBean> localWalletList = DatabaseWalletUtils.getLocalCoinList(2);
        if (localWalletList == null || localWalletList.isEmpty()) {
            return;
        }
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("wallet-ethereum-%d").build();
        GetETHCurrencyBalance threadPoolExecutor = new GetETHCurrencyBalance(localWalletList.size(), 100, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100), namedThreadFactory);
        for (WalletBean walletBean : localWalletList) {
            String coinName = walletBean.getTokenName();
            String coinAddress = walletBean.getTokenAddress();
            String contractAddress = walletBean.getContractAddress();

            switch (coinName) {
//                case "CCM":
//                    threadPoolExecutor.execute(new QueryBalance(CCMUtils, coinName, coinAddress, contractAddress));
//                    break;
                case "OMG":
                case "BNB":
                case "MKR":
                case "USDC":
                case "CRO":
                case "TUSD":
                case "ZRX":
                case "ZIL":
                case "BAT":
                case "LINK":
                case "ICX":
                case "PAX":
                case "R":
                case "REP":
                case "AE":
                case "GUSD":
                case "BCZERO":
                case "PPT":
                case "BTM":
                case "BTC":
                    break;
                case "USDT":
                    break;
                default:
                    threadPoolExecutor.execute(new QueryETHBalance(ETHUtils, coinName, coinAddress, contractAddress));
                    break;
            }

        }

    }


    /**
     * 查询ETH,ERC20代币余额线程
     */
    private static class QueryBalance implements Runnable {
        private String coinAddress;
        private CCMUtils CCMUtils;
        private String contractAddress;
        private String coinName;


        public QueryBalance(CCMUtils CCMUtils, String coinName, String coinAddress, String contractAddress) {
            this.coinAddress = coinAddress;
            this.CCMUtils = CCMUtils;
            this.contractAddress = contractAddress;
            this.coinName = coinName;
        }

        @Override
        public void run() {
            // 处理耗时逻辑
            WalletBean tokenBean = new WalletBean();
            BigDecimal balance;
            if ("CCM".equals(coinName)) {
                balance = CCMUtils.getBalance(coinAddress);
            } else {
                balance = CCMUtils.getTokenBalance(coinAddress, contractAddress);
            }
            Log.e("===========", "线程执行开始:" + coinName);
            tokenBean.setAmount(StringUtils.bigDecimal8(balance));
            DatabaseWalletUtils.updateLocalData(tokenBean, coinAddress, coinName, Config.getMobleMapping(), 3D);

        }
    }

    /**
     * 查询ETH,ERC20代币余额线程
     */
    private static class QueryETHBalance implements Runnable {
        private String coinAddress;
        private ETHUtils ETHUtils;
        private String contractAddress;
        private String coinName;


        public QueryETHBalance(ETHUtils ETHUtils, String coinName, String coinAddress, String contractAddress) {
            this.coinAddress = coinAddress;
            this.ETHUtils = ETHUtils;
            this.contractAddress = contractAddress;
            this.coinName = coinName;
        }

        @Override
        public void run() {
            // 处理耗时逻辑
            WalletBean tokenBean = new WalletBean();
            BigDecimal balance;
            if ("ETH".equals(coinName)) {
                balance = ETHUtils.getBalance(coinAddress);
            } else {
                balance = ETHUtils.getTokenBalance(coinAddress, contractAddress);
            }
            Log.e("===========", "线程执行开始:" + coinName);
            tokenBean.setAmount(StringUtils.bigDecimal8(balance));
            DatabaseWalletUtils.updateLocalData(tokenBean, coinAddress, coinName, Config.getMobleMapping(), 3D);

        }
    }

}
