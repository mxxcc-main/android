package com.qy.ccm.bean.wallet;


import com.qy.ccm.bean.base.BaseBean;

/**
 * Description:
 * Data：2019/1/11-11:43 AM
 *
 */
public class BtcTransactionBean extends BaseBean {


    /**
     * status : OK
     * pushed : Success
     * tx : 9975dcd1f574fe8356183e34eb435df6aa5620e7f9fea98a3c2349a10383a62b
     */

    private String status;
    private String pushed;
    private String tx;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPushed() {
        return pushed;
    }

    public void setPushed(String pushed) {
        this.pushed = pushed;
    }

    public String getTx() {
        return tx;
    }

    public void setTx(String tx) {
        this.tx = tx;
    }
}
