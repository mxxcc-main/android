package com.qy.ccm.bean.my;

import com.qy.ccm.bean.base.BaseBean;

/**
 * Description:助记词选择bean
 * Data：2019/5/6-8:11 PM
 *
 * @author
 */

public class MnemonicBean extends BaseBean {
    private String context;
    private boolean select;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
