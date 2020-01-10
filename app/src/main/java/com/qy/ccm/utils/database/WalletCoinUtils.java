package com.qy.ccm.utils.database;

import com.qy.ccm.bean.other.database.WalletCoinBean;

import org.litepal.LitePal;

import java.util.List;

/**
 * Description:币的地址
 * Data：2019/5/16-11:09 AM
 */
public class WalletCoinUtils {

    /**
     * 设置钱包数据
     */
    public static void setCoinData(String coin, String coinAddress,String mobileMapping) {
        List<WalletCoinBean> walletList = getCoinList(coin, coinAddress,mobileMapping);
        if (walletList != null && walletList.size() > 0) {
            return;
        }
        WalletCoinBean walletCoinBean = new WalletCoinBean();
        walletCoinBean.setCoinName(coin);
        walletCoinBean.setCoinAddress(coinAddress);
        walletCoinBean.setCoinAddress(coinAddress);
        walletCoinBean.setMobileMapping(mobileMapping);
        walletCoinBean.save();

    }


    public static List<WalletCoinBean> getCoinList(String coinName, String coinAddress,String mobileMapping) {
        List<WalletCoinBean> localCoinBeansBeans = LitePal.where("coinName=? and coinAddress=? and mobileMapping = ?", coinName, coinAddress,mobileMapping).find(WalletCoinBean.class);
        if (localCoinBeansBeans.size() > 0) {
            return localCoinBeansBeans;
        } else {
            return null;
        }
    }


    public static List<WalletCoinBean> getCoinList(String coinName,String mobileMapping,int tag) {
        List<WalletCoinBean> localCoinBeansBeans = LitePal.where("coinName=?  and mobileMapping = ?", coinName,mobileMapping).find(WalletCoinBean.class);
        if (localCoinBeansBeans != null && localCoinBeansBeans.size() > 0) {
            return localCoinBeansBeans;
        } else {
            return null;
        }
    }


}
