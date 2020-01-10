package com.qy.ccm.bean.other.database;


import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Description:钱包地址
 * Data：2019/5/15-8:53 PM
 *
 * @author
 */
public class WalletCoinBean extends LitePalSupport implements Serializable {
    private String coinName;
    private String coinAddress;

    /**
     * 与手机号关联
     */
    private String mobileMapping;

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinAddress() {
        return coinAddress;
    }

    public void setCoinAddress(String coinAddress) {
        this.coinAddress = coinAddress;
    }

    public String getMobileMapping() {
        return mobileMapping;
    }

    public void setMobileMapping(String mobileMapping) {
        this.mobileMapping = mobileMapping;
    }
}
