package com.qy.ccm.utils.database;

import android.util.Log;

import com.qy.ccm.bean.other.database.WalletBean;
import com.qy.ccm.utils.function.StringUtils;

import org.litepal.LitePal;

import java.math.BigDecimal;
import java.util.List;

/**
 * 数据库工具类
 *
 * @author xq
 */
public class DatabaseWalletUtils {


    /**
     * 设置本地钱包数据
     */
    public static void setLocalCoinJson(String IconUrl, String coin, String tokenName, String tokenAddress, String contractAddress, String theMnemonicWord, String privateKey, BigDecimal price, BigDecimal amount, boolean isLocalWalletCreated, String mobileMapping, String walletName, String tokenNameZhCn) {


        List<WalletBean> walletList = getLocalCreatedWalletList(tokenName, tokenAddress, mobileMapping);
        if (walletList != null && walletList.size() > 0) {
            return;
        }
        WalletBean localCoinBean = new WalletBean();
        localCoinBean.setCoin(coin);
        localCoinBean.setTokenName(tokenName);
        localCoinBean.setIconUrl(IconUrl);
        // for EOS this equals account name
        localCoinBean.setTokenAddress(tokenAddress);
        localCoinBean.setContractAddress(contractAddress);
        localCoinBean.setTheMnemonicWord(theMnemonicWord);
        localCoinBean.setPrivateKey(privateKey);
        localCoinBean.setPrice(amount.toPlainString());
        localCoinBean.setAmount(StringUtils.bigDecimal8(amount));
        localCoinBean.setMobileMapping(mobileMapping);
        localCoinBean.setWalletName(walletName);
        localCoinBean.setTokenNameZhCN(tokenNameZhCn);
        Log.e("============11", "tokenName：" + tokenName);
        localCoinBean.save();
    }

    /**
     * walletList
     * 修改本地钱包数据
     */
    public static void updateLocalCoinJson(String tokenName, String tokenAddress, String contractAddress, String theMnemonicWord, String privateKey, BigDecimal price, BigDecimal amount, boolean isLocalWalletCreated, String mobileMapping) {
        WalletBean localCoinBean = new WalletBean();
        localCoinBean.setTokenName(tokenName);
        // for EOS this equals account name
        localCoinBean.setTokenAddress(tokenAddress);
        localCoinBean.setPrivateKey(privateKey);
        localCoinBean.setPrice(price.toPlainString());
        localCoinBean.setAmount("0");
        localCoinBean.setLocalWalletCreated(isLocalWalletCreated);
//        localCoinBean.setMobileMapping(mobileMapping);
        localCoinBean.updateAll("tokenName=? and mobileMapping = ?", tokenName, mobileMapping);
    }

    /**
     * walletList
     * 修改本地钱包数据
     */
    public static void updateLocalCoinJson_BTC_ETH(String IconUrl, String coin, String tokenName, String tokenAddress, String contractAddress, String theMnemonicWord, String privateKey, BigDecimal price, BigDecimal amount, boolean isLocalWalletCreated, String mobileMapping, String walletName, String tokenNameZhCn) {
        WalletBean localCoinBean = new WalletBean();
        localCoinBean.setTokenName(tokenName);
        // for EOS this equals account name
        localCoinBean.setTokenAddress(tokenAddress);
        localCoinBean.setPrivateKey(privateKey);
        localCoinBean.setPrice(price.toPlainString());
        localCoinBean.setAmount("0");
        localCoinBean.setLocalWalletCreated(isLocalWalletCreated);
//        localCoinBean.setMobileMapping(mobileMapping);
        localCoinBean.updateAll("tokenName=? and mobileMapping = ?", tokenName, mobileMapping);
    }


    /**
     * 设置本地钱包数据
     */
    public static void localCoinJson(String coin, String tokenName, String tokenAddress, String contractAddress, String theMnemonicWord, String privateKey, BigDecimal price, BigDecimal amount, boolean isLocalWalletCreated, String mobileMapping, String walletName, String tokenNameZhCn) {
        List<WalletBean> getWalletList = getLocalCreatedWalletList(tokenName, tokenAddress, mobileMapping);
        if (getWalletList != null && getWalletList.size() > 0) {
            //已有数据修改
            updateLocalCoinJson(tokenName, tokenAddress, contractAddress, theMnemonicWord, privateKey, price, amount, isLocalWalletCreated, mobileMapping);
        } else {
            //没有数据设置
            setLocalCoinJson("", coin, tokenName, tokenAddress, contractAddress, theMnemonicWord, privateKey, price, amount, isLocalWalletCreated, mobileMapping, walletName, tokenNameZhCn);
        }
    }

    public static List<WalletBean> getLocalCoinList_CCM(String tokennamezhcn) {
//        tokennamezhcn = "CCM"; //空代表CCM
        List<WalletBean> localCoinBeansBeans = LitePal.where("tokenNameZhCN=?", tokennamezhcn).find(WalletBean.class);

        if (localCoinBeansBeans.size() > 0) {
            return localCoinBeansBeans;
        } else {
            return null;
        }
    }

    public static List<WalletBean> getLocalCoinList(int currentPosition) {
        if (currentPosition == 1) {
            List<WalletBean> localCoinBeansBeans = LitePal.where("tokenNameZhCN=?", "BTC").find(WalletBean.class);
            if (localCoinBeansBeans.size() > 0) {
                return localCoinBeansBeans;
            }
        } else if (currentPosition == 2) {
            List<WalletBean> localCoinBeansBeans = LitePal.where("tokenNameZhCN=?", "ETH").find(WalletBean.class);
            if (localCoinBeansBeans.size() > 0) {
                return localCoinBeansBeans;
            }
        } else if (currentPosition == 10) {
            List<WalletBean> localCoinBeansBeans = LitePal.findAll(WalletBean.class);
            if (localCoinBeansBeans.size() > 0) {
                return localCoinBeansBeans;
            }
        }
        return null;

    }

    /**
     * Local created wallet list
     */
    public static List<WalletBean> getLocalCreatedWalletList(String tokenAddress, String tokenName, String mobileMapping) {
        List<WalletBean> localCoinBeansBeans = LitePal.where("tokenAddress=? and tokenName=? and mobileMapping = ?", tokenAddress, tokenName, mobileMapping).find(WalletBean.class);
        if (localCoinBeansBeans.size() > 0) {
            return localCoinBeansBeans;
        } else {
            return null;
        }
    }

    public static List<WalletBean> getTokenNameList(String tokenName, String mobileMapping) {
        List<WalletBean> localCoinBeansBeans = LitePal.where("tokenName=? and mobileMapping = ?", tokenName, mobileMapping).find(WalletBean.class);
        if (localCoinBeansBeans.size() > 0) {
            return localCoinBeansBeans;
        } else {
            return null;
        }
    }

    public static List<WalletBean> getWalletCoinList(String coinAddress, String coin, String mobileMapping) {
        List<WalletBean> localCoinBeansBeans = LitePal.where("tokenAddress=? and coin=? and mobileMapping", coinAddress, coin, mobileMapping).find(WalletBean.class);
        if (localCoinBeansBeans.size() > 0) {
            return localCoinBeansBeans;
        } else {
            return null;
        }
    }

    public static void updateLocalData(WalletBean localCoinBean, String address, String tokenName, String mobileMapping, double tag) {
        localCoinBean.updateAll("tokenName = ?  and  mobileMapping = ?   and  tokenAddress = ?  ", tokenName, mobileMapping, address);

    }

//    public static void updateLocalData(WalletBean localCoinBean, String tokenAddress, String tokenName, String mobileMapping) {
//        localCoinBean.updateAll("tokenAddress=? and tokenName = ?", tokenAddress, tokenName, mobileMapping);
//
//    }



    /**
     * 清除本地钱包
     */
    public static void deleteAll(String mobileMapping) {
        LitePal.deleteAll(WalletBean.class);
    }

    public static void deleteLocalCoin(Long uid) {
        LitePal.delete(WalletBean.class,Long.valueOf(uid));
    }

}
