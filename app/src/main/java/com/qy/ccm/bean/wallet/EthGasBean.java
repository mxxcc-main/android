package com.qy.ccm.bean.wallet;


import com.qy.ccm.bean.base.BaseBean;

/**
 * Description:
 * Data：2019/1/26-12:02 PM
 *
 */
public class EthGasBean extends BaseBean {


    /**
     * lastUpdate : 2019-01-26T09:27:11.547230Z
     * lowest : 440000001
     * safeLow : 5000000001
     * standard : 9000000001
     * fast : 15000000001
     * fastest : 2366357142858
     */

    private String lastUpdate;
    private String lowest;
    private String safeLow;
    private String standard;
    private String fast;
    private String fastest;

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLowest() {
        return lowest;
    }

    public void setLowest(String lowest) {
        this.lowest = lowest;
    }

    public String getSafeLow() {
        return safeLow;
    }

    public void setSafeLow(String safeLow) {
        this.safeLow = safeLow;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getFast() {
        return fast;
    }

    public void setFast(String fast) {
        this.fast = fast;
    }

    public String getFastest() {
        return fastest;
    }

    public void setFastest(String fastest) {
        this.fastest = fastest;
    }
}
