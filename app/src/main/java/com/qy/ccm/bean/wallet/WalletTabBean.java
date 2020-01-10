package com.qy.ccm.bean.wallet;

import com.qy.ccm.bean.base.BaseBean;

/**
 * Description:
 * Data：2019/5/8-7:59 PM
 *
 * @author
 */
public class WalletTabBean extends BaseBean {
    private String tabContext;
    private boolean tabSelect;
    private boolean tabSelect2;


    public String getTabContext() {
        return tabContext;
    }

    public void setTabContext(String tabContext) {
        this.tabContext = tabContext;
    }

    public boolean isTabSelect() {
        return tabSelect;
    }

    public void setTabSelect(boolean tabSelect) {
        this.tabSelect = tabSelect;
    }

    public boolean isTabSelect2() {
        return tabSelect2;
    }

    public void setTabSelect2(boolean tabSelect2) {
        this.tabSelect2 = tabSelect2;
    }
}
