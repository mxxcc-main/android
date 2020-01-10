package com.qy.ccm.bean.wallet;

import com.qy.ccm.bean.base.BaseBean;

/**
 * Description:交易记录
 * Data：2019/5/14-5:36 PM
 */
public class RecordBean extends BaseBean {
    private String time;
    private String amunt;
    private String address;
    private String coinName;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmunt() {
        return amunt;
    }

    public void setAmunt(String amunt) {
        this.amunt = amunt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }
}
